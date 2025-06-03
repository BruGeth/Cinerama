package com.cinerama.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for user registration requests.
 *
 * <p>This DTO encapsulates all required information for new user registration
 * in the Cinerama cinema booking system. It includes comprehensive validation
 * to ensure data integrity and provides password confirmation for enhanced security.</p>
 *
 * <h2>Registration Flow:</h2>
 * <ul>
 *   <li>Client submits registration form data</li>
 *   <li>Server validates all fields and password matching</li>
 *   <li>User account is created with email verification required</li>
 *   <li>Verification code is sent to provided email address</li>
 * </ul>
 *
 * <h2>Validation Features:</h2>
 * <ul>
 *   <li>All fields are required and cannot be blank</li>
 *   <li>Email format validation using Jakarta Bean Validation</li>
 *   <li>Password confirmation matching (handled by business logic)</li>
 *   <li>Custom error messages for validation failures</li>
 * </ul>
 *
 * <h2>Security Considerations:</h2>
 * <ul>
 *   <li>Passwords transmitted as plain text (ensure HTTPS is used)</li>
 *   <li>Password complexity validation handled by business logic</li>
 *   <li>Email uniqueness validation performed at service level</li>
 *   <li>Account created in disabled state pending email verification</li>
 * </ul>
 *
 * @author Cinerama Development Team
 * @see VerificationRequest for the email verification process
 */
@Data
public class RegisterRequest {

    /**
     * User's full name for account creation.
     *
     * <p>This field contains the user's display name that will be stored in the
     * system and used for personalization. The name will be visible in the user
     * interface and used for identification purposes.</p>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>No specific length restrictions at DTO level</li>
     *   <li>Additional validation may be applied at service level</li>
     * </ul>
     *
     * <h3>Usage:</h3>
     * <ul>
     *   <li>Stored directly in User entity</li>
     *   <li>Displayed in user profiles and greetings</li>
     *   <li>Used in LoginResponse for client personalization</li>
     * </ul>
     */
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * User's email address for account creation and authentication.
     *
     * <p>This field serves as the unique identifier for the user account.
     * The email will be used for authentication, account verification,
     * and system communications.</p>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>Must conform to standard email format (RFC 5322)</li>
     *   <li>Uniqueness validation performed at service level</li>
     * </ul>
     *
     * <h3>Registration Process:</h3>
     * <ul>
     *   <li>Email uniqueness is checked against existing users</li>
     *   <li>Verification code is sent to this email address</li>
     *   <li>Account activation requires email verification</li>
     *   <li>Used as login username after registration</li>
     * </ul>
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    /**
     * User's desired password for account security.
     *
     * <p>This field contains the user's chosen password in plain text format.
     * The password will be hashed using bcrypt before storage and never
     * stored in plain text in the database.</p>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>Password complexity rules enforced at service level</li>
     *   <li>Must match confirmPassword field</li>
     * </ul>
     *
     * <h3>Security Processing:</h3>
     * <ul>
     *   <li>Hashed using BCryptPasswordEncoder before database storage</li>
     *   <li>Plain text password is never persisted</li>
     *   <li>Compared against confirmPassword for validation</li>
     *   <li>Should be cleared from memory after processing</li>
     * </ul>
     */
    @NotBlank(message = "Password is required")
    private String password;

    /**
     * Password confirmation for validation purposes.
     *
     * <p>This field requires the user to re-enter their chosen password
     * to prevent typos and ensure password accuracy. The confirmation
     * is validated against the primary password field during registration.</p>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>Must exactly match the password field</li>
     *   <li>Validation performed at service level</li>
     * </ul>
     *
     * <h3>Security Features:</h3>
     * <ul>
     *   <li>Prevents password entry errors during registration</li>
     *   <li>Enhances user experience by catching typos early</li>
     *   <li>Not stored in database - used only for validation</li>
     *   <li>Should be cleared from memory after validation</li>
     * </ul>
     *
     * <p><strong>Implementation Note:</strong> Password matching validation
     * should be implemented using a custom validator or service-level
     * validation to ensure both fields are compared securely.</p>
     */
    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;
}
