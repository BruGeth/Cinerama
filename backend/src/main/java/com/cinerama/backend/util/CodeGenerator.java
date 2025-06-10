package com.cinerama.backend.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * Generates cryptographically secure verification codes for user authentication.
 *
 * <p>Produces 6-digit numeric codes using SecureRandom for account verification,
 * password reset, and other security-sensitive operations requiring temporary codes.</p>
 */
@Component
public class CodeGenerator {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a secure 6-digit verification code.
     *
     * <p>Uses cryptographically strong random number generation to ensure
     * codes cannot be predicted or brute-forced easily.</p>
     *
     * @return 6-digit numeric code as string (100000-999999)
     */
    public String generateCode() {
        int code = random.nextInt(900000) + 100000; // Ensures 6-digit range
        return String.valueOf(code);
    }
}
