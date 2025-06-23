package com.cinerama.backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@AllArgsConstructor
@Builder
public class ConfectioneryProductRequest {
    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number")
    private Double price;

    private String image;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private Integer stock;

    private String stockUnit;
}