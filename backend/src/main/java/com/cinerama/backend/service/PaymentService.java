package com.cinerama.backend.service;

import com.cinerama.backend.entity.Order;
import com.cinerama.backend.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Service responsible for handling the payment process using PayPal.
 * It delegates order creation and capture logic to the appropriate PayPal services.
 */
@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PayPalOrderService orderService;
    private final PayPalCaptureService captureService;
    private final OrderRepository orderRepository; // Added for updating and saving orders after capture
    private Double convertirSolesADolares(Double montoEnSoles) {
        double tipoCambio = 0.2818;
        return Math.round(montoEnSoles * tipoCambio * 100.0) / 100.0;
    }
    /**
     * Constructs the PaymentService with dependencies for order and capture operations.
     */
    public PaymentService(PayPalOrderService orderService, PayPalCaptureService captureService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.captureService = captureService;
        this.orderRepository = orderRepository;
    }

    /**
     * Creates a PayPal order based on the provided amount and currency.
     *
     * @param amount   the total payment amount
     * @param currency the currency code (e.g., "USD")
     * @return HTTP response with PayPal order ID or error message
     */
    public ResponseEntity<?> createPayment(Double amount, String currency) {
        try {
            logger.info("Creating payment order: {} {}", amount, currency);

            Double montoUSD;
            if ("USD".equalsIgnoreCase(currency)) {
                montoUSD = amount; // Ya viene en dólares, no convertir
                logger.info("💵 Monto recibido en USD: {}", montoUSD);
            } else {
                montoUSD = convertirSolesADolares(amount); // Solo convierte si no es USD
                logger.info("💵 Monto convertido a dólares: {} USD (original: {} PEN)", montoUSD, amount);
            }

            String orderId = orderService.createOrder(montoUSD, "USD");
            logger.info("Order created with ID: {}", orderId);

            if (orderId == null || orderId.isEmpty()) {
                logger.error("Error: No valid order ID generated.");
                return ResponseEntity.status(500).body("Failed to create PayPal payment.");
            }

            Map<String, String> response = new HashMap<>();
            response.put("id", orderId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creating payment: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Internal error during payment creation.");
        }
    }

    /**
     * Captures a previously approved PayPal order based on its ID.
     *
     * @param orderId the PayPal order ID to capture
     * @return HTTP response indicating success or failure of the capture
     */
    public ResponseEntity<?> capturePayment(String orderId) {
        try {
            logger.info("Capturing payment with Order ID: {}", orderId);
            String result = captureService.captureOrder(orderId);
            logger.info("Payment captured successfully: {}", result);

            if (result == null || result.isEmpty()) {
                logger.error("Error: No valid confirmation received from PayPal.");
                return ResponseEntity.status(500).body("Payment capture failed.");
            }

            // Retrieve the corresponding order from the database
            Order order = orderRepository.findByPaypalOrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Update order status and timestamp after successful capture
            order.setStatus("COMPLETED");
            order.setTimestamp(LocalDateTime.now());
            order.setPayerEmail("payer@example.com");

            // Save updated order in database
            orderRepository.save(order);
            logger.info("Order updated and saved successfully to the database.");

            Map<String, String> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Payment captured and order updated successfully.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error capturing payment: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error processing payment capture.");
        }
    }
}
