package com.cinerama.backend.controller;

import com.cinerama.backend.service.impl.PayPalOrderServiceImpl;
import com.cinerama.backend.service.impl.PayPalCaptureServiceImpl;
import com.cinerama.backend.service.impl.PaymentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles payment-related operations by interacting with PayPal services and PaymentService.
 * Provides endpoints to create and capture PayPal orders.
 */
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PayPalOrderServiceImpl orderService;
    private final PayPalCaptureServiceImpl captureService;
    private final PaymentServiceImpl paymentServiceImpl;

/**
     * Constructs the PaymentController with dependencies for order and capture services.
     *
     * @param paymentServiceImpl  service for handling payment operations
     * @param orderService    service for creating PayPal orders
     * @param captureService  service for capturing PayPal payments
     */
    public PaymentController(PaymentServiceImpl paymentServiceImpl, PayPalOrderServiceImpl orderService, PayPalCaptureServiceImpl captureService) {
        this.orderService = orderService;
        this.captureService = captureService;
        this.paymentServiceImpl = paymentServiceImpl;
    }

    /**
     * Endpoint to create a PayPal order using PaymentService.
     *
     * @param amount      the total payment amount
     * @param currency    the currency code (e.g., "USD")
     * @param returnUrl   the URL to redirect to after successful payment
     * @param cancelUrl   the URL to redirect to if payment is cancelled
     * @return structured response with PayPal order approval URL or error message
     */
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(
            @RequestParam Double amount,
            @RequestParam String currency,
            @RequestParam String returnUrl,
            @RequestParam String cancelUrl
    ) {
        return paymentServiceImpl.createPayment(amount, currency, returnUrl, cancelUrl);
    }
    /**
     * New endpoint to capture a PayPal order using PaymentService, which may include order updates.
     *
     * @param paymentId the PayPal payment ID to capture
     * @param payerId   the PayPal payer ID to execute the payment
     * @return structured response with capture status and updated order details
     */
    @PostMapping("/capture")
    public ResponseEntity<?> captureWithUpdate(
            @RequestParam String paymentId,
            @RequestParam String payerId
    ) {
        return paymentServiceImpl.capturePayment(paymentId, payerId);
    }
}
