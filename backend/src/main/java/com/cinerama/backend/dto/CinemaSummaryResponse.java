package com.cinerama.backend.dto;

import com.cinerama.backend.enums.CinemaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Cinema summary information.
 * 
 * <p>This class represents a simplified view of Cinema entities
 * used in responses that include cinema information.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaSummaryResponse {
    
    /**
     * Unique identifier of the cinema.
     */
    private Long id;
    
    /**
     * Name of the cinema.
     */
    private String name;
    
    /**
     * City where the cinema is located.
     */
    private String city;
    
    /**
     * Current status of the cinema.
     */
    private CinemaStatus status;
}
