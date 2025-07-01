package com.cinerama.backend.exception.user;
/*
 * Exception thrown when a user is not found in the system.
 * This exception is used to indicate that the requested user does not exist in the database.
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Creates a new instance of UserNotFoundException with a default message.
     * This constructor is used when no specific message is provided.
     */
    public UserNotFoundException() {
        super("User not found");
    }
    /**
     * Creates a new instance of UserNotFoundException with a default message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}