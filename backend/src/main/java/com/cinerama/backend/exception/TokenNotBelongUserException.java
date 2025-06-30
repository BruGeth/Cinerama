package com.cinerama.backend.exception;

public class TokenNotBelongUserException extends RuntimeException {
    public TokenNotBelongUserException(String message) {
        super(message);
    }
}
