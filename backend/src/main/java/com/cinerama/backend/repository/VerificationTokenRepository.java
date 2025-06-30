package com.cinerama.backend.repository;

import com.cinerama.backend.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for managing VerificationToken entities.
 * This interface extends JpaRepository to provide CRUD operations
 */
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    /**
     * Finds a VerificationToken by its token value.
     *
     * @param token the token string to search for
     * @return an Optional containing the found VerificationToken, or empty if not found
     */
    Optional<VerificationToken> findByToken(String token);
}