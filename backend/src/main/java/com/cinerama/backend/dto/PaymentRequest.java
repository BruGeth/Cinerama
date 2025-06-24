package com.cinerama.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object representing a payment request made by a user.
 * Carries essential payment details from the client to the backend.
 */
@Setter
@Getter
public class PaymentRequest {
    /**
     * The ID of the user making the payment.
     * This field must not be null.
     */
    @NotNull
    private Integer userId;

    /**
     * The chosen payment method (e.g., PayPal, credit card).
     * This field must not be null or empty.
     */
    @NotEmpty
    private String paymentMethod;

    /**
     * The total amount to be paid.
     * This field must not be null.
     */
    @NotNull
    private Double amount;
}

