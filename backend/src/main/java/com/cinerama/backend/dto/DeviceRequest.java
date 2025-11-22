package com.cinerama.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for registering a push notification device token.
 * 
 * @author Cinerama Development Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequest {
    
    /**
     * Push notification provider (must be "expo").
     */
    @NotBlank(message = "Provider is required")
    @Pattern(regexp = "expo", message = "Only 'expo' provider is supported")
    private String provider;
    
    /**
     * Expo push token.
     */
    @NotBlank(message = "Token is required")
    private String token;
    
    /**
     * Device platform (ios, android, web).
     */
    @NotBlank(message = "Platform is required")
    private String platform;
}
