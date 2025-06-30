package com.cinerama.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for requesting a password reset.
 *
 * <p>This DTO encapsulates the data required for a user to request a password reset
 * in the Cinerama cinema booking system. It includes validation rules to ensure
 * the integrity of the request.</p>
 *
 * <h2>Forgot Password Flow:</h2>
 * <ul>
 *   <li>User submits email to request password reset</li>
 *   <li>System validates email and sends reset instructions</li>
 * </ul>
 *
 * <h2>Security Features:</h2>
 * <ul>
 *   <li>Email must be valid and associated with an existing account</li>
 * </ul>
 *
 * @author Cinerama Development Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {
    /**
     * User's email address for password reset request.
     *
     * <p>This field identifies which user account is being reset.
     * The email must match the one associated with the user's account.</p>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>Must conform to standard email format (RFC 5322)</li>
     * </ul>
     */
    @NotBlank(message = "Email should not be blank")
    @Email(message = "Email should be valid")
    private String email;
}