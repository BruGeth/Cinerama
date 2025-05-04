package com.cinerama.backend.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class CodeGenerator {

    private static final SecureRandom random = new SecureRandom();

    public String generateCode() {
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
