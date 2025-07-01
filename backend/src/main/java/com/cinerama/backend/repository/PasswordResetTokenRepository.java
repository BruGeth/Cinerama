package com.cinerama.backend.repository;

import com.cinerama.backend.entity.PasswordResetToken;
import com.cinerama.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing PasswordResetToken entities.
 * This interface extends JpaRepository to provide CRUD operations.
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    /**
     * Finds a PasswordResetToken by its token value.
     *
     * @param token the token string to search for
     * @return an Optional containing the found PasswordResetToken, or empty if not found
     */
    Optional<PasswordResetToken> findByToken(String token);
    /**
     * Finds all PasswordResetTokens associated with a specific User.
     *
     * @param user the User whose tokens are to be found
     * @return a List of PasswordResetTokens associated with the User
     */
    List<PasswordResetToken> findAllByUser(User user);
}