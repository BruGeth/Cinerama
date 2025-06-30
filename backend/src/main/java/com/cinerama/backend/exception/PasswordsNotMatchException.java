package com.cinerama.backend.exception;

public class PasswordsNotMatchException extends RuntimeException {
    public PasswordsNotMatchException(String message) {
        super(message);
    }
}
