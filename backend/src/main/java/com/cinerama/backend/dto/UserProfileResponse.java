package com.cinerama.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for exposing non-sensitive user profile information.
 *
 * <p>This DTO is used to respond with basic details of the authenticated user.
 * It excludes sensitive fields like password or security tokens.</p>
 *
 * <h2>Fields:</h2>
 * <ul>
 *   <li><strong>name</strong>: Full name of the user</li>
 *   <li><strong>email</strong>: Registered email of the user</li>
 *   <li><strong>role</strong>: Role assigned to the user (e.g., ADMIN or USER)</li>
 * </ul>
 *
 * @author Cinerama
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String name;
    private String email;
    private String role;
}

