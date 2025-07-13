package com.cinerama.backend.dto;

public class ConfectioneryPurchaseItemDTO {
    private Long productId;
    private int quantity;

    public ConfectioneryPurchaseItemDTO() {}

    public ConfectioneryPurchaseItemDTO(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
