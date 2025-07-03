package com.cinerama.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for changing user password.
 *
 * <p>This DTO encapsulates the data required for a user to change their
 * password in the Cinerama cinema booking system. It includes validation
 * rules to ensure the integrity and security of the password change process.</p>
 *
 * <h2>Change Password Flow:</h2>
 * <ul>
 *   <li>User submits request to change password</li>
 *   <li>System validates email and new password</li>
 *   <li>If valid, updates user's password in the database</li>
 * </ul>
 *
 * <h2>Security Features:</h2>
 * <ul>
 *   <li>New password must meet minimum length requirements</li>
 *   <li>Confirm password must match new password</li>
 * </ul>
 *
 * @author Cinerama Development Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    /**
     * User's email address for password change request.
     *
     * <p>This field identifies which user account is being updated.
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
    /**
     * New password for the user.
     *
     * <p>This field contains the new password that the user wants to set.
     * It must meet minimum security requirements to ensure account safety.</p>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>Must be at least 8 characters long</li>
     * </ul>
     */
    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;
    /**
     * Confirmation of the new password.
     *
     * <p>This field is used to confirm that the user has entered their
     * new password correctly. It must match the new password field.</p>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>Must match the new password</li>
     * </ul>
     */
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}