package com.cinerama.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity representing a verification token.
 * This token is used to verify the user's email address during registration.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationToken {
    /**
     * Unique identifier for the verification token.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The actual token string used for verification.
     */
    private String token;
/**
     * The user associated with this verification token.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * The date and time when this token expires.
     */
    private LocalDateTime expiryDate;
}