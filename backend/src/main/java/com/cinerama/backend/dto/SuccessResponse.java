package com.cinerama.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic success response DTO.
 * 
 * @author Cinerama Development Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse {
    
    /**
     * Success status flag.
     */
    private Boolean success;
    
    /**
     * Optional message.
     */
    private String message;
    
    /**
     * Optional data payload.
     */
    private Object data;
    
    /**
     * Creates a simple success response.
     */
    public static SuccessResponse of(Boolean success) {
        return SuccessResponse.builder().success(success).build();
    }
    
    /**
     * Creates a success response with message.
     */
    public static SuccessResponse of(Boolean success, String message) {
        return SuccessResponse.builder().success(success).message(message).build();
    }
    
    /**
     * Creates a success response with data.
     */
    public static SuccessResponse of(Boolean success, String message, Object data) {
        return SuccessResponse.builder().success(success).message(message).data(data).build();
    }
}
