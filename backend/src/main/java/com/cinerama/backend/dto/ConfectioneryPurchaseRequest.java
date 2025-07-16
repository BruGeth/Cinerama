package com.cinerama.backend.dto;

import lombok.Data;
import java.util.List;

/**
 * DTO for confectionery purchase requests.
 * 
 * Changes and improvements:
 * - Uses a dedicated list of ConfectioneryPurchaseItemRequest to represent each item in the purchase, improving clarity and serialization.
 * - Includes returnUrl and cancelUrl fields to support PayPal payment flow (for redirecting after approval or cancellation).
 */
@Data
public class ConfectioneryPurchaseRequest {
    // List of items to purchase (each with productId and quantity)
    private List<ConfectioneryPurchaseItemRequest> items;
    // URL to redirect after successful PayPal payment
    private String returnUrl;
    // URL to redirect if PayPal payment is cancelled
    private String cancelUrl;
}