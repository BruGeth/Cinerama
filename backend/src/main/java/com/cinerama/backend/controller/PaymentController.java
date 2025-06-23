package com.cinerama.backend.controller;

import com.cinerama.backend.service.PayPalOrderService;
import com.cinerama.backend.service.PayPalCaptureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * Handles payment-related operations by interacting with PayPal services.
 * Provides endpoints to create and capture PayPal orders.
 */
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PayPalOrderService orderService;
    private final PayPalCaptureService captureService;

    /**
     * Constructor injecting PayPal services used for order creation and capture.
     */
    public PaymentController(PayPalOrderService orderService, PayPalCaptureService captureService) {
        this.orderService = orderService;
        this.captureService = captureService;
    }

    /**
     * Endpoint to create a PayPal order with the given amount and currency.
     * Returns the raw JSON response from PayPal as a String.
     *
     * @param amount   the total amount to charge
     * @param currency the currency code (e.g., "USD")
     * @return response containing the order data from PayPal
     */
    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestParam Double amount, @RequestParam String currency) {
        String order = orderService.createOrder(amount, currency);
        return ResponseEntity.ok(order);
    }

    /**
     * Endpoint to capture a previously approved PayPal order.
     *
     * @param orderId the PayPal order ID to be captured
     * @return response containing the capture result as a String
     */
    @PostMapping("/capture")
    public ResponseEntity<String> capturePayment(@RequestParam String orderId) {
        String result = captureService.captureOrder(orderId);
        return ResponseEntity.ok(result);
    }
}
