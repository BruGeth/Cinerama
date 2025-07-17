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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException;

/**
 * Service responsible for handling the PayPal payment process for tickets.
 * Includes Micrometer metrics for monitoring.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    // Logger for logging information and errors
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    // Dependencies required for payment processing
    private final PayPalOrderServiceImpl orderService;
    private final PayPalCaptureServiceImpl captureService;
    private final OrderRepository orderRepository;
    private final MeterRegistry meterRegistry;
    private final PdfService pdfService;

    // Counters for success and error metrics
    private Counter successCounter;
    private Counter errorCounter;

    // Constructor with dependency injection
    public PaymentServiceImpl(PayPalOrderServiceImpl orderService,
                              PayPalCaptureServiceImpl captureService,
                              OrderRepository orderRepository,
                              MeterRegistry meterRegistry,
                              PdfService pdfService) {
        this.orderService = orderService;
        this.captureService = captureService;
        this.orderRepository = orderRepository;
        this.meterRegistry = meterRegistry;
        this.pdfService = pdfService;
    }

    // Initializes metric counters after bean construction
    @PostConstruct
    public void initCounters() {
        successCounter = meterRegistry.counter("payment.ticket.success.total");
        errorCounter = meterRegistry.counter("payment.ticket.error.total");
    }

    // Converts Peruvian soles to US dollars using a fixed exchange rate
    private Double convertSolesToDollars(Double amountInSoles) {
        double exchangeRate = 0.2818;
        return Math.round(amountInSoles * exchangeRate * 100.0) / 100.0;
    }

    /**
     * Creates a payment order in PayPal.
     * @param amount Amount to pay
     * @param currency Currency
     * @param returnUrl Return URL after payment
     * @param cancelUrl Cancel URL
     * @return ResponseEntity with the result
     */
    @Timed(value = "payment.ticket.order.creation.duration", description = "Duration for creating ticket order")
    @Override
    public ResponseEntity<?> createPayment(Double amount, String currency, String returnUrl, String cancelUrl) {
        try {
            logger.info("Creating payment order: {} {}", amount, currency);

            // If the currency is not USD, convert the amount
            Double amountUSD = "USD".equalsIgnoreCase(currency)
                    ? amount
                    : convertSolesToDollars(amount);
            logger.info("Amount in USD: {}", amountUSD);

            // Create the order in PayPal
            Map<String, String> result = orderService.createOrder(amountUSD, "USD", returnUrl, cancelUrl);

            // Check if the response contains the required data
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
     * Captures the payment of a PayPal order.
     * @param paymentId Payment ID
     * @param payerId Payer ID
     * @return ResponseEntity with the result
     */
    @Timed(value = "payment.ticket.capture.duration", description = "Duration for capturing ticket payment")
    @Override
    public ResponseEntity<?> capturePayment(String paymentId, String payerId) {
        try {
            logger.info("Capturing payment: paymentId={}, payerId={}", paymentId, payerId);

            // Execute the payment capture in PayPal
            Payment payment = captureService.executePayment(paymentId, payerId);

            // Check if the order was already captured
            if (orderRepository.existsByPaypalOrderId(payment.getId())) {
                logger.warn("⚠️ Duplicate order capture attempt detected: {}", payment.getId());
                return ResponseEntity.status(409).body(Map.of(
                        "code", "DUPLICATE_ORDER",
                        "message", "This PayPal order has already been captured."
                ));
            }

            // Build and save the order in the database
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

            // Generate the PDF summary
            byte[] pdfBytes = pdfService.generateTicketSummary(order);

            // Return the PDF as a response
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order_" + order.getId() + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (PayPalRESTException ex) {
            // Handle PayPal-specific errors
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
        } catch (IOException ioException) {
            // Error generating the PDF
            logger.error("Error generating PDF summary: {}", ioException.getMessage(), ioException);
            return ResponseEntity.status(500).body(Map.of(
                    "code", "PDF_GENERATION_ERROR",
                    "message", "Failed to generate purchase summary PDF"
            ));
        } catch (Exception e) {
            // Handle other unexpected errors
            logger.error("Unexpected error during capture:", e);
            errorCounter.increment();
            return ResponseEntity.status(500).body(Map.of(
                    "code", "CAPTURE_ERROR",
                    "message", e.getMessage()
            ));
        }
    }