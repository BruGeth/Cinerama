package com.cinerama.backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder
public class ConfectioneryCategoryRequest {
    @NotBlank(message = "Category name cannot be empty")
    private String name;
}