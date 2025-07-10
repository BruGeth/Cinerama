package com.cinerama.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity representing a password reset token.
 * This token is used to verify the user's identity when resetting their password.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {
    /**
     * Unique identifier for the password reset token.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The actual token string used for password reset.
     */
    private String token;
    /**
     * The user associated with this password reset token.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * The date and time when this token expires.
     */
    private LocalDateTime expiryDate;
}