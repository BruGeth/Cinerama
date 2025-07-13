package com.cinerama.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfectioneryPurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ConfectioneryProduct product;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private ConfectioneryPurchase purchase;
}
