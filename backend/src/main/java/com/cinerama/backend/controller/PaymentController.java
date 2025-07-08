package com.cinerama.backend.controller;

import com.cinerama.backend.service.PayPalOrderService;
import com.cinerama.backend.service.PayPalCaptureService;
import com.cinerama.backend.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles payment-related operations by interacting with PayPal services and PaymentService.
 * Provides endpoints to create and capture PayPal orders.
 */
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PayPalOrderService orderService;
    private final PayPalCaptureService captureService;
    private final PaymentService paymentService;

    /**
     * Constructor injecting PayPal services and PaymentService used for order creation and capture.
     */
    public PaymentController(PaymentService paymentService, PayPalOrderService orderService, PayPalCaptureService captureService) {
        this.orderService = orderService;
        this.captureService = captureService;
        this.paymentService = paymentService;
    }

    /**
     * Original endpoint to create a PayPal order directly using PayPalOrderService.
     * Returns the raw JSON response from PayPal as a String.
     *
     * @param amount   the total amount to charge
     * @param currency the currency code (e.g., "USD")
     * @return response containing the raw order data from PayPal
     */
    @PostMapping("/create/raw")
    public ResponseEntity<String> createOrderRaw(@RequestParam Double amount, @RequestParam String currency) {
        String order = orderService.createOrder(amount, currency);
        return ResponseEntity.ok(order);
    }

    /**
     * New endpoint to create a PayPal order using PaymentService, which may include cart validation.
     *
     * @param amount   the total amount to charge
     * @param currency the currency code (e.g., "USD")
     * @return structured response with order ID and validation message
     */
    @PostMapping("/create")
    public ResponseEntity<?> createOrderValidated(@RequestParam Double amount, @RequestParam String currency) {
        return paymentService.createPayment(amount, currency);
    }

    /**
     * Endpoint to capture a PayPal order directly using PayPalCaptureService (raw flow).
     *
     * @param orderId the PayPal order ID to be captured
     * @return raw response string from PayPal
     */
    @PostMapping("/capture/raw")
    public ResponseEntity<String> captureRaw(@RequestParam String orderId) {
        String result = captureService.captureOrder(orderId);
        return ResponseEntity.ok(result);
    }

    /**
     * New endpoint to capture and update order status using PaymentService.
     *
     * @param orderId the PayPal order ID to be captured
     * @return structured response indicating success and database update
     */
    @PostMapping("/capture")
    public ResponseEntity<?> captureWithUpdate(@RequestParam String orderId) {
        return paymentService.capturePayment(orderId);
    }
}
