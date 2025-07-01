package com.cinerama.backend.service;

import com.cinerama.backend.entity.User;
import com.cinerama.backend.dto.VerificationRequest;

/**
 * Service interface for handling user account verification operations.
 * This service provides methods to create a verification token and verify the user's account.
 */
public interface VerificationTokenService {
    /**
     * Creates a verification token for the specified user.
     *
     * @param user the user for whom the verification token is created
     */
    void createVerificationToken(User user);
    /**
     * Verifies the user's account using the provided verification request.
     *
     * @param request the verification request containing user details and token
     */
    void verifyAccount(VerificationRequest request);
}