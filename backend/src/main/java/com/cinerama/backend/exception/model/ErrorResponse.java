package com.cinerama.backend.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private ErrorCode code;
    private String message;
}
