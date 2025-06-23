package com.cinerama.backend.dto;

import lombok.Data;
/**
 * Data Transfer Object representing the response to a payment operation.
 * Used to communicate the result of payment-related requests back to the client.
 */
@Data
public class PaymentResponse {

    /**
     * Indicates whether the payment was successful, failed, etc.
     * Example values: "SUCCESS", "FAILED"
     */
    private String status;

    /**
     * Descriptive message giving context about the payment result.
     * Could contain success confirmation or error details.
     */
    private String message;
}