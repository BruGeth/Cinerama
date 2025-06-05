package com.cinerama.backend.service;

import com.cinerama.backend.dto.LoginRequest;
import com.cinerama.backend.dto.LoginResponse;
import com.cinerama.backend.dto.RegisterRequest;
import com.cinerama.backend.dto.VerificationRequest;
import com.cinerama.backend.entity.User;

/**
 * Authentication service contract for user registration, verification, and login operations.
 *
 * <p>Handles the complete user authentication lifecycle from account creation through
 * email verification to secure login with JWT token generation.</p>
 */
public interface AuthService {

    /**
     * Registers a new user account with email verification required.
     *
     * @param request user registration details including password confirmation
     * @return the created user entity with verification code generated
     * @throws IllegalArgumentException if passwords don't match or email already exists
     */
    User register(RegisterRequest request);

    /**
     * Activates a user account using the emailed verification code.
     *
     * @param request email and verification code pair
     * @throws IllegalArgumentException if user not found or code is invalid
     */
    void verify(VerificationRequest request);

    /**
     * Authenticates user credentials and returns JWT token for session management.
     *
     * @param request user login credentials
     * @return login response containing username and JWT token
     * @throws IllegalArgumentException if credentials are invalid
     * @throws IllegalStateException if account is not verified
     */
    LoginResponse login(LoginRequest request);

}
