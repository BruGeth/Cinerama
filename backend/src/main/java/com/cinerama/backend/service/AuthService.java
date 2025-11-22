package com.cinerama.backend.service;

import com.cinerama.backend.dto.AuthResponse;
import com.cinerama.backend.dto.LoginRequest;
import com.cinerama.backend.dto.RegisterRequest;
import com.cinerama.backend.dto.VerificationRequest;

/**
 * Authentication service contract for user registration, verification, and login operations.
 *
 * <p>Handles the complete user authentication lifecycle from account creation through
 * email verification to secure login with JWT token generation.</p>
 */
public interface AuthService {

    /**
     * Registers a new user account and returns authentication token.
     *
     * @param request user registration details including password confirmation
     * @return AuthResponse with JWT token and user data
     * @throws IllegalArgumentException if passwords don't match or email already exists
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Activates a user account using the emailed verification code and returns token.
     *
     * @param request email and verification code pair
     * @return AuthResponse with JWT token and user data
     * @throws IllegalArgumentException if user not found or code is invalid
     */
    AuthResponse verify(VerificationRequest request);

    /**
     * Authenticates user credentials and returns JWT token for session management.
     *
     * @param request user login credentials
     * @return AuthResponse with JWT token and user data
     * @throws IllegalArgumentException if credentials are invalid
     * @throws IllegalStateException if account is not verified
     */
    AuthResponse login(LoginRequest request);

}
