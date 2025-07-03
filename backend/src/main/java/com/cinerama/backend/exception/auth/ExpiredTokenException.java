package com.cinerama.backend.exception.auth;

/**
 * Exception thrown when a token has expired.
 * This exception is used to indicate that the provided token is no longer valid due to expiration.
 */
public class ExpiredTokenException extends RuntimeException {
    /**
     * Creates a new instance of ExpiredTokenException with a default message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public ExpiredTokenException(String message) {
        super(message);
    }
}
