package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.ConfectioneryProductResponse;
import com.cinerama.backend.dto.ConfectioneryCategoryResponse;
import com.cinerama.backend.entity.ConfectioneryProduct;
import com.cinerama.backend.repository.ConfectioneryProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConfectioneryProductServiceImpl {

    private final ConfectioneryProductRepository repository;

    public ConfectioneryProductServiceImpl(ConfectioneryProductRepository repository) {
        this.repository = repository;
    }

    public List<ConfectioneryProductResponse> getAllProducts() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ConfectioneryProductResponse toResponse(ConfectioneryProduct product) {
        return ConfectioneryProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .image(product.getImage())
                .category(
                        ConfectioneryCategoryResponse.builder()
                                .id(product.getCategory().getId())
                                .name(product.getCategory().getName())
                                .build()
                )
                .build();
    }
}