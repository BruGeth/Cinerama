package com.cinerama.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a payment order associated with a PayPal transaction.
 * Stores transaction details such as the total amount, status, timestamp,
 * buyer information, and the list of purchased items (CartItems).
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    /**
     * Primary key for the order entry (auto-generated).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * PayPal-generated order ID used to identify the transaction.
     */
    private String paypalOrderId;

    /**
     * Total amount charged for the order.
     */
    private BigDecimal amount;

    /**
     * Status of the order (e.g., CREATED, COMPLETED, FAILED).
     */
    private String status;

    /**
     * Date and time when the order was created or completed.
     */
    private LocalDateTime timestamp;

    /**
     * Currency code used for the transaction (e.g., USD).
     */
    private String currency;

    /**
     * Email of the payer retrieved from PayPal after order capture.
     */
    private String payerEmail;

    /**
     * Name of the payer for order tracking and receipt generation.
     */
    @Column(nullable = true)
    private String payerName;

    /**
     * List of items included in the order. Each CartItem is linked back to this order.
     * Cascade operations and orphan removal are enabled to manage persistence automatically.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cart;

    /**
     * List of confectionery items included in the order. Each ConfectioneryOrderItem is linked back to this order.
     * Cascade operations and orphan removal are enabled to manage persistence automatically.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConfectioneryOrderItem> confectioneryItems;
/**
 * Calculates the total cost of all items in the order based on quantity and price.
 * This ensures that the amount charged matches the cart contents.
 *
 * @return the total calculated amount
 */
public BigDecimal getCalculatedTotal() {
    if (cart == null || cart.isEmpty()) {
        return BigDecimal.ZERO;
    }

    return cart.stream()
            .map(item -> BigDecimal.valueOf(item.getPrice()).multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
}
}