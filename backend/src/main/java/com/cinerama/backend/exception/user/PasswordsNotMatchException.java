package com.cinerama.backend.exception.user;

/**
 * Exception thrown when passwords do not match during user registration or password change.
 * This exception is used to indicate that the provided passwords are not identical.
 */
public class PasswordsNotMatchException extends RuntimeException {
    /**
     * Creates a new instance of PasswordsNotMatchException with a default message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public PasswordsNotMatchException(String message) {
        super(message);
    }
}
