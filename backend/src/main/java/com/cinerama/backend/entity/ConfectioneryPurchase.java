package com.cinerama.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfectioneryPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private Double total;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<ConfectioneryPurchaseItem> items;
}
