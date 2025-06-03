package com.cinerama.backend.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT token utility for secure user authentication and session management.
 *
 * <p>Provides stateless authentication by generating, validating, and parsing JWT tokens
 * with configurable expiration and HMAC-SHA512 signing for enhanced security.</p>
 *
 * <p>Tokens contain user email as subject and are valid for 10 hours by default.
 * Invalid tokens are logged but do not throw exceptions to maintain application stability.</p>
 */
@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey secretKey;

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    /**
     * Initializes JWT signing key from application configuration.
     *
     * <p>Validates secret configuration and creates HMAC-SHA signing key.
     * Missing or empty secrets are logged as errors but don't prevent startup.</p>
     */
    @PostConstruct
    public void init() {
        if (secret == null || secret.isEmpty()) {
            log.error("[JWT] Secret is not defined ❌");
        } else {
            log.info("[JWT] Secret loaded successfully ✅");
            this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Generates a signed JWT token for user authentication.
     *
     * @param email user's email address to embed as token subject
     * @return signed JWT token string valid for 10 hours
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extracts user email from validated JWT token.
     *
     * @param token JWT token to parse
     * @return user email stored in token subject
     * @throws JwtException if token is invalid, expired, or malformed
     */
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates JWT token signature and expiration without throwing exceptions.
     *
     * <p>Invalid tokens are logged for security monitoring but return false
     * instead of propagating exceptions to maintain application flow.</p>
     *
     * @param token JWT token to validate
     * @return true if token is valid and not expired, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false; // Graceful failure for invalid tokens
        }
    }
}
