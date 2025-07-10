package com.cinerama.backend.service.impl;

import com.cinerama.backend.entity.Order;
import com.cinerama.backend.repository.OrderRepository;
import com.cinerama.backend.service.PaymentService;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Error;
import com.paypal.base.rest.PayPalRESTException;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Service responsible for handling the PayPal payment process for tickets.
 * Includes Micrometer metrics for monitoring.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PayPalOrderServiceImpl orderService;
    private final PayPalCaptureServiceImpl captureService;
    private final OrderRepository orderRepository;
    private final MeterRegistry meterRegistry;

    private Counter successCounter;
    private Counter errorCounter;

    public PaymentServiceImpl(PayPalOrderServiceImpl orderService, PayPalCaptureServiceImpl captureService,
                              OrderRepository orderRepository, MeterRegistry meterRegistry) {
        this.orderService = orderService;
        this.captureService = captureService;
        this.orderRepository = orderRepository;
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void initCounters() {
        successCounter = meterRegistry.counter("payment.ticket.success.total");
        errorCounter = meterRegistry.counter("payment.ticket.error.total");
    }

    private Double convertSolesToDollars(Double amountInSoles) {
        double exchangeRate = 0.2818;
        return Math.round(amountInSoles * exchangeRate * 100.0) / 100.0;
    }

    /**
     * Creates a PayPal order for ticket payment.
     */
    @Timed(value = "payment.ticket.order.creation.duration", description = "Duración al crear orden de boletos")
    @Override
    public ResponseEntity<?> createPayment(Double amount, String currency, String returnUrl, String cancelUrl) {
        try {
            logger.info("Creating payment order: {} {}", amount, currency);

            Double amountUSD = "USD".equalsIgnoreCase(currency)
                    ? amount
                    : convertSolesToDollars(amount);
            logger.info("Amount in USD: {}", amountUSD);

            Map<String, String> result = orderService.createOrder(amountUSD, "USD", returnUrl, cancelUrl);

            if (result == null || !result.containsKey("approval_url") || !result.containsKey("payment_id")) {
                logger.error("Missing approval_url or payment_id from PayPalOrderService");
                errorCounter.increment();
                return ResponseEntity.status(500).body(Map.of("error", "Failed to create PayPal approval link."));
            }

            logger.info("✅ Order created successfully. paymentId={}, approvalUrl={}",
                    result.get("payment_id"), result.get("approval_url"));
            successCounter.increment();
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error("❌ Error creating PayPal payment: {}", e.getMessage(), e);
            errorCounter.increment();
            return ResponseEntity.status(500).body(Map.of(
                    "code", "PAYPAL_ERROR",
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Captures an approved PayPal payment and persists the ticket order.
     */
    @Timed(value = "payment.ticket.capture.duration", description = "Duración al capturar pago de boletos")
    @Override
    public ResponseEntity<?> capturePayment(String paymentId, String payerId) {
        try {
            logger.info("Capturing payment: paymentId={}, payerId={}", paymentId, payerId);

            Payment payment = captureService.executePayment(paymentId, payerId);

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

            successCounter.increment();

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
            errorCounter.increment();
            return ResponseEntity.status(400).body(Map.of(
                    "code", code,
                    "message", message,
                    "info", info
            ));
        } catch (Exception e) {
            logger.error("Unexpected error during capture:", e);
            errorCounter.increment();
            return ResponseEntity.status(500).body(Map.of(
                    "code", "CAPTURE_ERROR",
                    "message", e.getMessage()
            ));
        }
    }
}
