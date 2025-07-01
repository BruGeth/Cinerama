package com.cinerama.backend.service;

import com.cinerama.backend.entity.User;

/**
 * Service interface for handling password reset operations.
 * This service provides methods to create a password reset token,
 * validate the token, and change the user's password.
 */
public interface PasswordResetService {
    /**
     * Creates a password reset token for the specified user.
     *
     * @param user the user for whom the password reset token is created
     */
    void createPasswordResetToken(User user);
    /**
     * Validates the password reset token for the specified user.
     *
     * @param email the email of the user
     * @param token the password reset token to validate
     */
    void validatePasswordResetToken(String email, String token);
    /**
     * Changes the password for the specified user.
     *
     * @param email the email of the user
     * @param newPassword the new password to set
     * @param confirmPassword the confirmation of the new password
     */
    void changePassword(String email, String newPassword, String confirmPassword);
}