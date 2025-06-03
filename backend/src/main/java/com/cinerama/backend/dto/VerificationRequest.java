package com.cinerama.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for email verification requests.
 *
 * <p>This DTO encapsulates the data required for email verification during
 * the user registration process in the Cinerama cinema booking system.
 * It enables users to activate their accounts by providing the verification
 * code sent to their email address.</p>
 *
 * <h2>Verification Flow:</h2>
 * <ul>
 *   <li>User registers account (account created but disabled)</li>
 *   <li>System generates 6-digit verification code</li>
 *   <li>Verification code is sent to user's email</li>
 *   <li>User submits email and code via this DTO</li>
 *   <li>System validates code and activates account</li>
 * </ul>
 *
 * <h2>Security Features:</h2>
 * <ul>
 *   <li>Time-limited verification codes</li>
 *   <li>Code is single-use and invalidated after successful verification</li>
 *   <li>Email validation ensures code is sent to correct address</li>
 *   <li>Rate limiting should be implemented to prevent brute force attacks</li>
 * </ul>
 *
 * <h2>Usage Context:</h2>
 * <ul>
 *   <li>POST /api/auth/verify endpoint request body</li>
 *   <li>Account activation process</li>
 *   <li>Email ownership confirmation</li>
 * </ul>
 *
 * @author Cinerama Development Team
 * @see RegisterRequest for the initial registration process
 */
@Data
public class VerificationRequest {

    /**
     * User's email address for verification lookup.
     *
     * <p>This field identifies which user account is being verified.
     * The email must match the one used during registration and must
     * correspond to an existing unverified user account.</p>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>Must conform to standard email format (RFC 5322)</li>
     *   <li>Must match an existing user account in the system</li>
     * </ul>
     *
     * <h3>Verification Process:</h3>
     * <ul>
     *   <li>Used to look up the corresponding user account</li>
     *   <li>Verification code is validated against this user's stored code</li>
     *   <li>Account activation is performed for this email</li>
     *   <li>Must match the email used during registration</li>
     * </ul>
     *
     * <h3>Security Considerations:</h3>
     * <ul>
     *   <li>Case-insensitive comparison recommended</li>
     *   <li>Should validate account exists and is unverified</li>
     *   <li>Consider rate limiting per email address</li>
     * </ul>
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    /**
     * 6-digit verification code sent to user's email.
     *
     * <p>This field contains the numeric verification code that was sent
     * to the user's email address during registration. The code must match
     * the one stored in the user's account for successful verification.</p>
     *
     * <h3>Code Characteristics:</h3>
     * <ul>
     *   <li>6-digit numeric code (e.g., "123456")</li>
     *   <li>Generated randomly during registration</li>
     *   <li>Single-use - invalidated after successful verification</li>
     * </ul>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li>Cannot be null, empty, or contain only whitespace</li>
     *   <li>Should be exactly 6 characters (validated at service level)</li>
     *   <li>Should contain only numeric characters</li>
     * </ul>
     *
     * <h3>Security Features:</h3>
     * <ul>
     *   <li>Limited lifespan prevents indefinite code validity</li>
     *   <li>Single-use prevents replay attacks</li>
     *   <li>Random generation prevents predictable codes</li>
     *   <li>Rate limiting should prevent brute force attempts</li>
     * </ul>
     *
     * <p><strong>Implementation Note:</strong> Consider implementing
     * rate limiting and account lockout mechanisms to prevent
     * brute force attacks on verification codes and Time-limited validity (typically 15-30 minutes)</p>
     */
    @NotBlank(message = "Verification code is required")
    private String verificationCode;
}
