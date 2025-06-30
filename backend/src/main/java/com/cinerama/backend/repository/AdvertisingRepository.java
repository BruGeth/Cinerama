package com.cinerama.backend.repository;

import com.cinerama.backend.entity.Advertising;
import org.springframework.data.jpa.repository.JpaRepository;

/* === Repository interface for Advertising entity, providing CRUD operations === */
public interface AdvertisingRepository extends JpaRepository<Advertising, Long> {
}
