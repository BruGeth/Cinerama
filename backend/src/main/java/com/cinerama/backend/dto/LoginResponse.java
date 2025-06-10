package com.cinerama.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for successful authentication responses.
 *
 * <p>This DTO encapsulates the response data sent to clients after successful
 * authentication in the Cinerama cinema booking system. It contains essential
 * user information and the JWT token required for subsequent authenticated requests.</p>
 *
 * <h2>Response Structure:</h2>
 * <ul>
 *   <li>User's display name for UI personalization</li>
 *   <li>JWT access token for API authentication</li>
 * </ul>
 *
 * <h2>Security Features:</h2>
 * <ul>
 *   <li>No sensitive user data exposed (password, email, internal IDs)</li>
 *   <li>JWT token enables stateless authentication</li>
 *   <li>Minimal data exposure following principle of the least privilege</li>
 * </ul>
 *
 * <h2>Usage Context:</h2>
 * <ul>
 *   <li>POST /api/auth/login endpoint response body</li>
 *   <li>Successful authentication confirmation</li>
 *   <li>Client-side token storage and user identification</li>
 * </ul>
 *
 * @author Cinerama Development Team
 * @see LoginRequest for the corresponding authentication request DTO
 */
@Data
@AllArgsConstructor
public class LoginResponse {

    /**
     * User's display name for client-side personalization.
     *
     * <p>This field contains the user's friendly display name as stored in the
     * system. It is used by client applications to personalize the user interface
     * and display welcome messages or user identification.</p>
     *
     * <h3>Characteristics:</h3>
     * <ul>
     *   <li>Safe for display in UI components</li>
     *   <li>No sensitive information exposed</li>
     *   <li>Directly from User entity's name field</li>
     *   <li>Used for greeting messages and user identification</li>
     * </ul>
     *
     * <p><strong>Example Usage:</strong></p>
     * <ul>
     *   <li>Welcome message: "Welcome back, {name}!"</li>
     *   <li>User profile displays</li>
     *   <li>Navigation bar user identification</li>
     * </ul>
     */
    private String name;

    /**
     * JWT access token for authenticated API requests.
     *
     * <p>This field contains the JSON Web Token (JWT) that clients must include
     * in the Authorization header for subsequent API requests. The token contains
     * encoded user information and has a configurable expiration time.</p>
     *
     * <h3>Token Characteristics:</h3>
     * <ul>
     *   <li>Base64-encoded JWT format</li>
     *   <li>Contains user email and expiration claims</li>
     *   <li>Signed with application's secret key</li>
     *   <li>Time-limited validity (configurable expiration)</li>
     * </ul>
     *
     * <h3>Client Usage:</h3>
     * <ul>
     *   <li>Store securely (localStorage, sessionStorage, or memory)</li>
     *   <li>Include in Authorization header: "Bearer {token}"</li>
     *   <li>Handle token expiration and refresh logic</li>
     * </ul>
     *
     * <h3>Security Considerations:</h3>
     * <ul>
     *   <li>Token should be transmitted over HTTPS only</li>
     *   <li>Client should implement secure storage practices</li>
     *   <li>Token expiration should be handled gracefully</li>
     *   <li>Consider implementing refresh token mechanism for long sessions</li>
     * </ul>
     */
    private String token;
}
