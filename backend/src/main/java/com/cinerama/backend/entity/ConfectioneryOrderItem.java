package com.cinerama.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a confectionery item within an order.
 * This entity is specifically designed for confectionery products,
 * separate from the general CartItem used for movie tickets.
 * 
 * Key features:
 * - Links to a specific confectionery product
 * - Stores quantity and price at time of purchase
 * - Associated with an order for tracking
 * - Includes product name for historical reference
 */
@Entity
@Table(name = "confectionery_order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"order"})
public class ConfectioneryOrderItem {
    
    /**
     * Primary key for the confectionery order item (auto-generated).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the parent order that contains this item.
     * Many confectionery items can belong to one order.
     */
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * Reference to the specific confectionery product.
     * This allows tracking which product was purchased.
     */
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ConfectioneryProduct product;

    /**
     * Name of the product at the time of purchase.
     * Stored for historical reference in case product details change.
     */
    @Column(nullable = false)
    private String productName;

    /**
     * Quantity of this product purchased in the order.
     */
    @Column(nullable = false)
    private int quantity;

    /**
     * Price per unit at the time of purchase (in PEN).
     * Stored to maintain historical pricing information.
     */
    @Column(nullable = false)
    private double unitPrice;

    /**
     * Total price for this item (quantity * unitPrice).
     * Calculated field for convenience and audit purposes.
     */
    @Column(nullable = false)
    private double totalPrice;

    /**
     * Calculates the total price for this item based on quantity and unit price.
     * This method ensures consistency between quantity, unit price, and total.
     *
     * @return the calculated total price for this item
     */
    public double calculateTotalPrice() {
        return this.quantity * this.unitPrice;
    }

    /**
     * Updates the total price based on current quantity and unit price.
     * Call this method after changing quantity or unit price.
     */
    public void updateTotalPrice() {
        this.totalPrice = calculateTotalPrice();
    }
} 