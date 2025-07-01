package com.cinerama.backend.exception.auth;

/**
 * Exception thrown when an invalid token is encountered.
 * This could be due to the token being expired, malformed, or otherwise not valid.
 */
public class InvalidTokenException extends RuntimeException {
    /**
     * Creates a new instance of InvalidTokenException with a default message.
     *
     * @param message the detail message explaining the reason for the exception.
     **/
    public InvalidTokenException(String message) {
        super(message);
    }
}
