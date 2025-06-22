package com.cinerama.backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class ConfectioneryCategoryRequest {
    @NotBlank(message = "Category name cannot be empty")
    private String name;
}