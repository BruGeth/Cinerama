package com.cinerama.backend.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@AllArgsConstructor
@Builder
public class ConfectioneryProductResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final Double price;
    private final String image;
    private final Integer stock;
    private final String stockUnit;
    private final ConfectioneryCategoryResponse category;
}