package com.cinerama.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for user authentication requests.
 *
 * <p>This DTO encapsulates the login credentials required for user authentication
 * in the Cinerama cinema booking system. It serves as the request payload for
 * authentication endpoints and includes comprehensive validation to ensure
 * data integrity and security.</p>
 *
 * <h2>Validation Features:</h2>
 * <ul>
 *   <li>Email format validation using Jakarta Bean Validation</li>
 *   <li>Required field validation for both email and password</li>
 *   <li>Custom error messages for validation failures</li>
 * </ul>
 *
 * <h2>Security Considerations:</h2>
 * <ul>
 *   <li>Password is transmitted as plain text (ensure HTTPS is used)</li>
 *   <li>No password complexity validation at DTO level (handled by business logic)</li>
 *   <li>Email serves as the unique user identifier</li>
 * </ul>
 *
 * <h2>Usage Context:</h2>
 * <ul>
 *   <li>POST /api/auth/login endpoint request body</li>
 *   <li>User authentication flow initiation</li>
 *   <li>JWT token generation process</li>
 * </ul>
 *
 * @author Cinerama Development Team
 * @see LoginResponse for the corresponding authentication response DTO
 */
@Data
public class LoginRequest {

    /**
     * User's email address used for authentication.
     *
     * <p>This field serves as the unique identifier for user authentication.
     * The email must be in valid format and is case-insensitive during
     * authentication processing.</p>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>Must conform to standard email format (RFC 5322)</li>
     *   <li>Validated using Jakarta Bean Validation annotations</li>
     * </ul>
     *
     * <p><strong>Example valid formats:</strong></p>
     * <ul>
     *   <li>user@example.com</li>
     *   <li>john.doe+cinema@domain.co.uk</li>
     *   <li>admin@cinerama.local</li>
     * </ul>
     *
     * <p><strong>Implementation Note:</strong> Email is used as the primary key for user lookup
     * in the authentication process. Email comparison should be case-insensitive in the
     * authentication service.</p>
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    /**
     * User's plain text password for authentication.
     *
     * <p>This field contains the user's password in plain text format as received
     * from the client. The password will be compared against the stored bcrypt hash
     * during the authentication process.</p>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>No length restrictions at DTO level (handled by business logic)</li>
     *   <li>No complexity requirements at DTO level (enforced during registration)</li>
     * </ul>
     *
     * <h3>Security Considerations:</h3>
     * <ul>
     *   <li>Password is transmitted in plain text - HTTPS is mandatory</li>
     *   <li>Password is never logged or stored in plain text</li>
     *   <li>Compared against bcrypt hash stored in database</li>
     *   <li>Should be cleared from memory after authentication</li>
     * </ul>
     *
     * <p><strong>Security Note:</strong> This field contains sensitive data
     * and should be handled with appropriate security measures. The
     * authentication service uses BCryptPasswordEncoder to protect against
     * timing attacks and ensure safe authentication.</p>
     */
    @NotBlank(message = "Password is required")
    private String password;
}