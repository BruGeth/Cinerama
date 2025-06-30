package com.cinerama.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for validating reset password tokens.
 *
 * <p>This DTO encapsulates the data required to validate a reset password token
 * during the password recovery process in the Cinerama cinema booking system.</p>
 *
 * <h2>Reset Password Flow:</h2>
 * <ul>
 *   <li>User requests password reset (token generated and sent to email)</li>
 *   <li>User submits email and token via this DTO</li>
 *   <li>System validates token and allows user to set a new password</li>
 * </ul>
 *
 * <h2>Security Features:</h2>
 * <ul>
 *   <li>Token must be valid and not expired</li>
 *   <li>Email validation ensures token is sent to correct address</li>
 * </ul>
 *
 * @author Cinerama Development Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateResetTokenRequest {
    /**
     * User's email address associated with the reset token.
     *
     * <p>This field identifies which user account is being validated.
     * The email must match the one used during the password reset request.</p>
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
    /**
     * Reset token for validating the password reset request.
     *
     * <p>This field contains the token generated during the password reset request.
     * It must be valid and not expired to proceed with resetting the password.</p>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     * </ul>
     */
    @NotBlank(message = "Token should not be blank")
    private String token;
}