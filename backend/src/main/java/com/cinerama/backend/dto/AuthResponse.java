package com.cinerama.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Unified authentication response DTO for all auth endpoints.
 * 
 * <p>This response format ensures consistency across register, login, and verify endpoints.
 * The frontend expects this exact structure: { token, user }</p>
 * 
 * <h2>Response Structure:</h2>
 * <ul>
 *   <li>token: JWT token string for Bearer authentication</li>
 *   <li>user: Nested user object with essential profile information</li>
 * </ul>
 * 
 * @author Cinerama Development Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    /**
     * JWT token for authenticated requests.
     * Frontend stores this with key 'userToken' in secure storage.
     */
    private String token;
    
    /**
     * User profile information.
     */
    private UserData user;
    
    /**
     * Nested user data object.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserData {
        /**
         * User's unique identifier.
         */
        private Long id;
        
        /**
         * User's display name.
         */
        private String name;
        
        /**
         * User's email address.
         */
        private String email;
        
        /**
         * User's role (e.g., ROLE_USER, ROLE_ADMIN).
         */
        private String role;
        
        /**
         * Optional avatar URL for user profile image.
         */
        private String avatarUrl;
    }
}
