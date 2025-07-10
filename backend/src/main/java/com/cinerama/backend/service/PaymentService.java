package com.cinerama.backend.service;

import org.springframework.http.ResponseEntity;

public interface PaymentService {

    /**
     * Creates a PayPal order using the given amount and currency.
     *
     * @param amount    Payment amount
     * @param currency  Currency code (e.g., "USD", "PEN")
     * @param returnUrl URL to redirect the user after approval
     * @param cancelUrl URL to redirect the user if they cancel the payment
     * @return ResponseEntity containing the result of the creation request
     */
    ResponseEntity<?> createPayment(Double amount, String currency, String returnUrl, String cancelUrl);

    /**
     * Captures a previously approved PayPal payment.
     *
     * @param paymentId PayPal payment ID
     * @param payerId   PayPal payer ID
     * @return ResponseEntity containing the result of the capture request
     */
    ResponseEntity<?> capturePayment(String paymentId, String payerId);
}
