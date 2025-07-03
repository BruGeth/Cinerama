package com.cinerama.backend.exception.auth;

/**
 * Exception thrown when a token does not belong to the user.
 * This exception is used to indicate that the provided token is not associated with the user making the request.
 */
public class TokenNotBelongUserException extends RuntimeException {
    /**
     * Creates a new instance of TokenNotBelongUserException with a default message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public TokenNotBelongUserException(String message) {
        super(message);
    }
}
