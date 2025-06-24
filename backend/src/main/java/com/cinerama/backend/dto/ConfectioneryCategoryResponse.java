package com.cinerama.backend.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@AllArgsConstructor
@Builder
public class ConfectioneryCategoryResponse {
    private Long id;
    private String name;
}