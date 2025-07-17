package com.cinerama.backend.exception.user;

/**
 * Thrown when an email already exists during user registration.
 *
 * <p>Prevents duplicate accounts from being created and maintains data integrity.</p>
 */
public class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}

