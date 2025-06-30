package com.cinerama.backend.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents an error response containing an error code and a message.
 * This class is used to standardize the format of error responses in the application.
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    /**
     * The error code representing the type of error.
     */
    private ErrorCode code;
    /**
     * A message providing additional details about the error.
     */
    private String message;
}
