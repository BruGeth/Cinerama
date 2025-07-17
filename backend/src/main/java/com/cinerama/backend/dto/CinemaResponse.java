package com.cinerama.backend.dto;

import com.cinerama.backend.enums.CinemaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Cinema response operations.
 * 
 * <p>This class represents the data structure returned
 * when retrieving Cinema entities through API responses.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaResponse {
    
    /**
     * Unique identifier of the cinema.
     */
    private Long id;
    
    /**
     * Name of the cinema.
     */
    private String name;
    
    /**
     * Physical address of the cinema.
     */
    private String address;
    
    /**
     * City where the cinema is located.
     */
    private String city;
    
    /**
     * Contact phone number of the cinema.
     */
    private String phone;
    
    /**
     * Contact email address of the cinema.
     */
    private String email;
    
    /**
     * Current status of the cinema.
     */
    private CinemaStatus status;
    
    /**
     * List of room summaries in this cinema.
     */
    private List<RoomSummaryResponse> rooms;
    
    /**
     * Timestamp when the cinema was created.
     */
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the cinema was last updated.
     */
    private LocalDateTime updatedAt;
}
