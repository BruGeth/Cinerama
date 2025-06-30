package com.cinerama.backend.repository;

import com.cinerama.backend.entity.ConfectioneryCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfectioneryCategoryRepository extends JpaRepository<ConfectioneryCategory, Long> {
}