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
 * Service implementation for handling PayPal payments in the confectionery module.
 *
 * Main responsibilities and improvements:
 * - Integrates PayPal payment creation and capture for confectionery purchases.
 * - Ensures all PayPal transactions are processed in USD, converting from PEN if necessary.
 * - Prevents duplicate payment processing by checking for existing PayPal order IDs before saving.
 * - Provides robust error handling and detailed logging for all payment operations.
 * - Saves successful payment and order details to the database for tracking and reconciliation.
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

    /**
     * Converts an amount in Peruvian Soles (PEN) to US Dollars (USD) using a fixed exchange rate.
     * This ensures that all PayPal transactions are processed in USD, as required by PayPal.
     *
     * @param amountInSoles the amount in Peruvian Soles
     * @return the equivalent amount in US Dollars, rounded to two decimals
     */
    public Double convertSolesToDollars(Double amountInSoles) {
        double exchangeRate = 0.2818;
        return Math.round(amountInSoles * exchangeRate * 100.0) / 100.0;
    }

    /**
     * Constructs the PaymentServiceImpl with dependencies for order and capture services and the order repository.
     *
     * @param orderService    service for creating PayPal orders
     * @param captureService  service for capturing PayPal payments
     * @param orderRepository repository for persisting order data
     */
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

    /**
     * Creates a PayPal payment order for the specified amount and currency.
     *
     * Flow:
     * 1. Converts the amount to USD if necessary (PayPal only accepts USD).
     * 2. Delegates the order creation to PayPalOrderServiceImpl.
     * 3. Returns the approval URL and payment ID for frontend redirection.
     * 4. Handles and logs any errors that occur during the process.
     *
     * @param amount     the payment amount (in PEN or USD)
     * @param currency   the currency code ("PEN" or "USD")
     * @param returnUrl  the URL to redirect to after payment approval
     * @param cancelUrl  the URL to redirect to if payment is cancelled
     * @return ResponseEntity containing the approval URL and payment ID, or error details
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
     * Captures an approved PayPal payment and saves the order details to the database.
     *
     * Flow:
     * 1. Executes the payment capture using PayPalCaptureServiceImpl.
     * 2. Checks if the PayPal order ID already exists to prevent duplicate processing.
     * 3. If not a duplicate, saves the order details (amount, currency, status, payer email, timestamp).
     * 4. Returns the payment status and relevant information to the frontend.
     * 5. Handles PayPal-specific and unexpected errors with detailed logging and responses.
     *
     * @param paymentId  the PayPal payment/order ID
     * @param payerId    the PayPal payer/user ID
     * @return ResponseEntity containing the payment status and order info, or error details
     */
    @Timed(value = "payment.ticket.capture.duration", description = "Duration for capturing ticket payment")
    @Override
    public ResponseEntity<?> capturePayment(String paymentId, String payerId) {
        try {
            logger.info("Capturing payment: paymentId={}, payerId={}", paymentId, payerId);

            // Execute the payment capture in PayPal
            Payment payment = captureService.executePayment(paymentId, payerId);

            // Check if the order already exists by PayPal order ID
            Order order = orderRepository.findByPaypalOrderId(payment.getId()).orElse(null);
            if (order != null) {
                // Update existing order with PayPal payer email and status
                order.setPayerEmail(payment.getPayer().getPayerInfo().getEmail());
                
                // Try to get payer name from PayPal response
                String payerName = null;
                if (payment.getPayer() != null && payment.getPayer().getPayerInfo() != null) {
                    String firstName = payment.getPayer().getPayerInfo().getFirstName();
                    String lastName = payment.getPayer().getPayerInfo().getLastName();
                    if (firstName != null || lastName != null) {
                        payerName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
                        payerName = payerName.trim();
                        order.setPayerName(payerName);
                    }
                }
                
                order.setStatus(payment.getState());
                order.setCurrency(payment.getTransactions().get(0).getAmount().getCurrency()); // <-- Actualiza la moneda
                orderRepository.save(order);
                logger.info("✅ Updated existing order with PayPal payer email: {}", order.getPayerEmail());
                
                Map<String, Object> response = new HashMap<>();
                response.put("code", "DUPLICATE_ORDER");
                response.put("message", "This PayPal order has already been captured.");
                response.put("status", "already_completed");
                response.put("payer", payment.getPayer().getPayerInfo().getEmail());
                if (payerName != null) {
                    response.put("payer_name", payerName);
                }
                
                return ResponseEntity.ok(response);
            }

            // Save new order details to the database
            String payerName = null;
            if (payment.getPayer() != null && payment.getPayer().getPayerInfo() != null) {
                String firstName = payment.getPayer().getPayerInfo().getFirstName();
                String lastName = payment.getPayer().getPayerInfo().getLastName();
                if (firstName != null || lastName != null) {
                    payerName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
                    payerName = payerName.trim();
                }
            }
            
            order = Order.builder()

                    .paypalOrderId(payment.getId())
                    .amount(new BigDecimal(payment.getTransactions().get(0).getAmount().getTotal()))
                    .currency(payment.getTransactions().get(0).getAmount().getCurrency())
                    .status(payment.getState())
                    // Include buyer's email and name for better tracking and auditability
                    .payerEmail(payment.getPayer().getPayerInfo().getEmail())
                    .payerName(payerName)
                    .timestamp(LocalDateTime.now())
                    .build();

            orderRepository.save(order);
            logger.info("✅ Order saved successfully: {}", order.getId());
            successCounter.increment();

            // Generate the PDF summary
            byte[] pdfBytes = pdfService.generateTicketSummary(order);

            Map<String, String> response = new HashMap<>();
            response.put("status", payment.getState());
            response.put("paypalOrderId", payment.getId());
            response.put("payer", payment.getPayer().getPayerInfo().getEmail());
            if (payerName != null) {
                response.put("payer_name", payerName);
            }

            // Return the PDF as a response
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order_" + order.getId() + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (PayPalRESTException ex) {

            // Handle PayPal-specific errors with detailed information

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
}