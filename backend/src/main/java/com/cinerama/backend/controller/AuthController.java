package com.cinerama.backend.controller;

import com.cinerama.backend.dto.AuthResponse;
import com.cinerama.backend.dto.LoginRequest;
import com.cinerama.backend.dto.RegisterRequest;
import com.cinerama.backend.dto.SuccessResponse;
import com.cinerama.backend.dto.VerificationRequest;
import com.cinerama.backend.entity.User;
import com.cinerama.backend.exception.user.UserNotFoundException;
import com.cinerama.backend.repository.UserRepository;
import com.cinerama.backend.service.AuthService;
import com.cinerama.backend.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling user authentication operations.
 *
 * <p>This controller manages the complete user authentication flow for the Cinerama
 * cinema booking system, including registration, email verification, and login.
 * Supports both /api/auth and /auth prefixes for frontend compatibility.</p>
 *
 * @author Cinerama Development Team
 */
@RestController
@RequestMapping({"/api/auth", "/auth"})
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    /**
     * Registers a new user and returns JWT token immediately.
     *
     * @param request the registration details
     * @return ResponseEntity with 201 status containing AuthResponse { token, user }
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Verifies a user's email and returns JWT token.
     *
     * @param request containing the verification token
     * @return ResponseEntity with AuthResponse { token, user }
     */
    @PostMapping("/verify")
    public ResponseEntity<AuthResponse> verify(@Valid @RequestBody VerificationRequest request) {
        AuthResponse response = authService.verify(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticates a user and returns JWT token with user data.
     *
     * @param request containing email and password
     * @return ResponseEntity with AuthResponse { token, user }
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets the current authenticated user's profile.
     *
     * @param authentication Spring Security authentication object
     * @return ResponseEntity with user data wrapped in { user: {...} }
     */
    @GetMapping("/me")
    public ResponseEntity<AuthResponse.UserData> getCurrentUser(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        AuthResponse.UserData userData = AuthResponse.UserData.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .avatarUrl(null)
                .build();
        
        return ResponseEntity.ok(userData);
    }

    /**
     * Logs out the current user.
     * 
     * <p>Since JWT is stateless, logout is handled client-side by removing the token.
     * This endpoint returns success to confirm the logout action.</p>
     *
     * @return ResponseEntity with success response
     */
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse> logout() {
        return ResponseEntity.ok(SuccessResponse.of(true, "Logged out successfully"));
    }

    /**
     * Refreshes an existing JWT token.
     *
     * @param authHeader Authorization header with Bearer token
     * @return ResponseEntity with new AuthResponse
     */
    @GetMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body(null);
        }

        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String newToken = jwtUtil.generateToken(email, role);

        AuthResponse.UserData userData = AuthResponse.UserData.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(role)
                .avatarUrl(null)
                .build();

        AuthResponse response = AuthResponse.builder()
                .token(newToken)
                .user(userData)
                .build();

        return ResponseEntity.ok(response);
    }
}