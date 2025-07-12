package com.cinerama.backend.exception.repository;

import com.cinerama.backend.entity.ConfectioneryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConfectioneryProductRepository extends JpaRepository<ConfectioneryProduct, Long> {
    List<ConfectioneryProduct> findByCategory_Id(Long categoryId);
}