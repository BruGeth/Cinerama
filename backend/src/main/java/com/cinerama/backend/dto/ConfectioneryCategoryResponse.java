package com.cinerama.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfectioneryCategoryResponse {
    private Long id;
    private String name;
}