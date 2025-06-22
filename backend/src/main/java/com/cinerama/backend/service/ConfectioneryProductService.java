package com.cinerama.backend.service;

import com.cinerama.backend.dto.ConfectioneryProductRequest;
import com.cinerama.backend.dto.ConfectioneryProductResponse;

import java.util.List;
import java.util.Optional;

public interface ConfectioneryProductService {
    List<ConfectioneryProductResponse> getAllProducts();
    Optional<ConfectioneryProductResponse> getProductById(Long id);
    ConfectioneryProductResponse createProduct(ConfectioneryProductRequest request);
    ConfectioneryProductResponse updateProduct(Long id, ConfectioneryProductRequest request);
    void deleteProduct(Long id);
}