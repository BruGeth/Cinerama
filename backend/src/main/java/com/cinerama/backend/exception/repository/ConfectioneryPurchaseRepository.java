package com.cinerama.backend.exception.repository;

import com.cinerama.backend.entity.ConfectioneryPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfectioneryPurchaseRepository extends JpaRepository<ConfectioneryPurchase, Long> {
}
