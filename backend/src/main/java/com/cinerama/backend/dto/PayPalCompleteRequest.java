package com.cinerama.backend.dto;

/**
 * DTO for completing a PayPal payment.
 * 
 * Purpose and improvements:
 * - Used to transfer the paymentId and payerId from the frontend to the backend when finalizing a PayPal transaction.
 * - Ensures clear and structured data transfer for payment completion.
 * - Contains paymentId (PayPal order ID) and payerId (PayPal user ID) fields.
 */
public class PayPalCompleteRequest {
    // PayPal payment/order ID
    private String paymentId;
    // PayPal payer/user ID
    private String payerId;

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getPayerId() { return payerId; }
    public void setPayerId(String payerId) { this.payerId = payerId; }
} 