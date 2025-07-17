package com.cinerama.backend.exception.model;

/**
 * Enum representing various error codes used in the application.
 * Each error code corresponds to a specific type of error that can occur.
 */
public enum ErrorCode {
    USER_NOT_FOUND,
    INVALID_TOKEN,
    INVALID_ARGUMENT,
    CONFLICT,
    VALIDATION_ERROR,
    CONSTRAINT_VIOLATION,
    ACCESS_DENIED,
    INTERNAL_ERROR,
    TOKEN_NOT_BELONG_USER,
    EXPIRED_TOKEN,
    PASSWORDS_NOT_MATCH,
    UNAUTHORIZED,
    UNKNOWN_ERROR,
    USER_ALREADY_EXISTS // Añadido para validar registros duplicados
}