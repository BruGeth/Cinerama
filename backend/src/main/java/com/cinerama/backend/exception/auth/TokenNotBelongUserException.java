package com.cinerama.backend.exception.auth;

public class TokenNotBelongUserException extends RuntimeException {
    public TokenNotBelongUserException(String message) {
        super(message);
    }
}
