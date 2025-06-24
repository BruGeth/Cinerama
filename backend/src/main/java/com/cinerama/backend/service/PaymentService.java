package com.cinerama.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    /**
     * Constructs the PaymentService with dependencies for order and capture operations.
     */
    public PaymentService(PayPalOrderService orderService, PayPalCaptureService captureService) {
        this.orderService = orderService;
        this.captureService = captureService;
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
            logger.info("Creando orden de pago: {} {}", amount, currency);
            String orderId = orderService.createOrder(amount, currency);
            logger.info("Orden creada con ID: {}", orderId);

            if (orderId == null || orderId.isEmpty()) {
                logger.error("Error: No se generó un ID de orden válido.");
                return ResponseEntity.status(500).body("Error al crear pago en PayPal.");
            }

            Map<String, String> response = new HashMap<>();
            response.put("id", orderId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al crear pago: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error interno en el proceso de pago.");
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
            logger.info("Capturando pago con Order ID: {}", orderId);
            String result = captureService.captureOrder(orderId);
            logger.info("Pago capturado correctamente: {}", result);

            if (result == null || result.isEmpty()) {
                logger.error("Error: No se recibió una confirmación válida de PayPal.");
                return ResponseEntity.status(500).body("Error en la captura del pago.");
            }

            Map<String, String> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Pago capturado correctamente.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al capturar pago: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al procesar la captura del pago.");
        }
    }
}
