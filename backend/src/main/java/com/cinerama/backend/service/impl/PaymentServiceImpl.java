package com.cinerama.backend.service.impl;

import com.cinerama.backend.entity.Order;
import com.cinerama.backend.repository.OrderRepository;
import com.cinerama.backend.service.PaymentService;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Error;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Service responsible for handling the PayPal payment process.
 * Delegates creation and capture logic to appropriate PayPal services.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PayPalOrderServiceImpl orderService;
    private final PayPalCaptureServiceImpl captureService;
    private final OrderRepository orderRepository;

    private Double convertSolesToDollars(Double amountInSoles) {
        double exchangeRate = 0.2818;
        return Math.round(amountInSoles * exchangeRate * 100.0) / 100.0;
    }

    /**
     * Constructs the PaymentService with dependencies for order and capture.
     */
    public PaymentServiceImpl(PayPalOrderServiceImpl orderService, PayPalCaptureServiceImpl captureService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.captureService = captureService;
        this.orderRepository = orderRepository;
    }

    /**
     * Creates a PayPal order using the given amount and currency.
     *
     * @param amount     Payment amount
     * @param currency   Currency code (e.g., "USD")
     * @param returnUrl  Redirect URL after approval
     * @param cancelUrl  Redirect URL on cancelation
     * @return ResponseEntity containing the creation result
     */
    @Override
    public ResponseEntity<?> createPayment(Double amount, String currency, String returnUrl, String cancelUrl) {
        try {
            logger.info("Creating payment order: {} {}", amount, currency);

            Double amountUSD;
            if ("USD".equalsIgnoreCase(currency)) {
                amountUSD = amount;
                logger.info("Amount in USD: {}", amountUSD);
            } else {
                amountUSD = convertSolesToDollars(amount);
                logger.info("Converted to USD: {} (original: {} {})", amountUSD, amount, currency);
            }

            Map<String, String> result = orderService.createOrder(amountUSD, "USD", returnUrl, cancelUrl);

            if (result == null || !result.containsKey("approval_url") || !result.containsKey("payment_id")) {
                logger.error("Missing approval_url or payment_id from PayPalOrderService");
                return ResponseEntity.status(500).body(Map.of("error", "Failed to create PayPal approval link."));
            }

            logger.info("✅ Order created successfully. paymentId={}, approvalUrl={}", result.get("payment_id"), result.get("approval_url"));
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error("❌ Error creating PayPal payment: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                    "code", "PAYPAL_ERROR",
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Captures an approved PayPal payment and saves order details to the database.
     *
     * @param paymentId  PayPal payment ID
     * @param payerId    PayPal payer ID
     * @return ResponseEntity containing capture status or error info
     */
    @Override
    public ResponseEntity<?> capturePayment(String paymentId, String payerId) {
        try {
            logger.info("Capturing payment: paymentId={}, payerId={}", paymentId, payerId);

            Payment payment = captureService.executePayment(paymentId, payerId);

            // Prevent duplicate entries
            if (orderRepository.existsByPaypalOrderId(payment.getId())) {
                logger.warn("⚠️ Duplicate order capture attempt detected: {}", payment.getId());
                return ResponseEntity.status(409).body(Map.of(
                        "code", "DUPLICATE_ORDER",
                        "message", "This PayPal order has already been captured."
                ));
            }

            Order order = Order.builder()
                    .paypalOrderId(payment.getId())
                    .amount(new BigDecimal(payment.getTransactions().get(0).getAmount().getTotal()))
                    .currency(payment.getTransactions().get(0).getAmount().getCurrency())
                    .status(payment.getState())
                    .payerEmail(payment.getPayer().getPayerInfo().getEmail())
                    .timestamp(LocalDateTime.now())
                    .build();

            orderRepository.save(order);
            logger.info("✅ Order saved successfully: {}", order.getId());

            Map<String, String> response = new HashMap<>();
            response.put("status", payment.getState());
            response.put("paypalOrderId", payment.getId());
            response.put("payer", payment.getPayer().getPayerInfo().getEmail());

            return ResponseEntity.ok(response);

        } catch (PayPalRESTException ex) {
            Error paypalError = ex.getDetails();
            String code = paypalError != null ? paypalError.getName() : "UNKNOWN_ERROR";
            String message = paypalError != null ? paypalError.getMessage() : ex.getMessage();
            String info = paypalError != null ? paypalError.getInformationLink() : "https://developer.paypal.com/";

            logger.error("PayPal error - code: {}, message: {}", code, message, ex);
            return ResponseEntity.status(400).body(Map.of(
                    "code", code,
                    "message", message,
                    "info", info
            ));
        } catch (Exception e) {
            logger.error("Unexpected error during capture:", e);
            return ResponseEntity.status(500).body(Map.of(
                    "code", "CAPTURE_ERROR",
                    "message", e.getMessage()
            ));
        }
    }
}
