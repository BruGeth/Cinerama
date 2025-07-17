package com.cinerama.backend.dto;

/**
 * DTO representing a single item in a confectionery purchase.
 * 
 * Changes and improvements:
 * - Created as a dedicated DTO to separate item details from the main purchase request.
 * - Improves clarity, maintainability, and serialization for each product in the purchase.
 * - Contains productId and quantity fields for precise item tracking.
 */
public class ConfectioneryPurchaseItemRequest {
    // ID of the product to purchase
    private Long productId;
    // Quantity of the product to purchase
    private Integer quantity;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}