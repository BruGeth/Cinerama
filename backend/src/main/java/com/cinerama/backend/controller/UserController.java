package com.cinerama.backend.controller;

import com.cinerama.backend.entity.User;
import com.cinerama.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling user-related operations.
 *
 * <p>This controller manages user profile operations for authenticated users
 * in the Cinerama cinema booking system. All endpoints require valid JWT authentication
 * as configured in the WebSecurityConfig.</p>
 *
 * <h2>Security Context:</h2>
 * <ul>
 *   <li>All endpoints require valid JWT authentication</li>
 *   <li>User information is extracted from SecurityContext</li>
 *   <li>Email from JWT token is used to identify the authenticated user</li>
 * </ul>
 *
 * <h2>Available Operations:</h2>
 * <ul>
 *   <li>Get current authenticated user profile</li>
 * </ul>
 *
 * @author Cinerama Development Team
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    /**
     * Retrieves the profile information of the currently authenticated user.
     *
     * <p>This endpoint returns the complete user profile based on the JWT token
     * provided in the Authorization header. The user's email is extracted from
     * the SecurityContext and used to fetch the user details from the database.</p>
     *
     * <h3>Authentication Flow:</h3>
     * <ol>
     *   <li>Client sends request with JWT token in Authorization header</li>
     *   <li>JwtAuthenticationFilter validates the token</li>
     *   <li>User email is stored in SecurityContext</li>
     *   <li>This method extracts email from SecurityContext</li>
     *   <li>User details are fetched from database using email</li>
     * </ol>
     *
     * <h3>Response Data:</h3>
     * <p>Returns complete User entity including:</p>
     * <ul>
     *   <li>User ID and personal information</li>
     *   <li>Email address and verification status</li>
     *   <li>Account creation and modification timestamps</li>
     *   <li>User role and permissions</li>
     * </ul>
     *
     * <p><strong>Security Note:</strong> Sensitive information like password hashes
     * should be excluded from the response in production systems.</p>
     *
     * @return ResponseEntity containing the authenticated user's profile information
     *
     * @todo Add proper exception handling and @throws documentation:
     * <ul>
     *   <li>UserNotFoundException if the authenticated user is not found in database</li>
     *   <li>AuthenticationException if SecurityContext doesn't contain valid user information</li>
     *   <li>DataAccessException if there's an error accessing the user repository</li>
     * </ul>
     *
     * @todo Consider security improvements:
     * <ul>
     *   <li>Create UserProfileResponse DTO to exclude sensitive fields</li>
     *   <li>Add input validation for SecurityContext data</li>
     *   <li>Implement proper error responses instead of generic RuntimeException</li>
     * </ul>
     *
     * @see User for the complete user entity structure
     * @see UserRepository#findByEmail(String) for the database query used
     * @see SecurityContextHolder for Spring Security context management
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        // Extract authenticated user's email from Spring Security context
        // This email was set during JWT token validation in JwtAuthenticationFilter
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // Fetch user details from database using the authenticated email
        // TODO: Replace RuntimeException with proper UserNotFoundException
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Return complete user profile
        // TODO: Create UserProfileResponse DTO to exclude password and other sensitive fields
        return ResponseEntity.ok(user);
    }
}