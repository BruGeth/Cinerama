package com.cinerama.backend.dto;

import com.cinerama.backend.enums.RoomStatus;
import com.cinerama.backend.enums.RoomType;
import com.cinerama.backend.enums.TechnologyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Room response operations.
 * 
 * <p>This class represents the data structure returned
 * when retrieving Room entities through API responses.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    
    /**
     * Unique identifier of the room.
     */
    private Long id;
    
    /**
     * Name of the room.
     */
    private String name;
    
    /**
     * Cinema information where this room is located.
     */
    private CinemaSummaryResponse cinema;
    
    /**
     * Seating capacity of the room.
     */
    private Integer capacity;
    
    /**
     * Type of the room.
     */
    private RoomType type;
    
    /**
     * Technology features available in the room.
     */
    private List<TechnologyType> technology;
    
    /**
     * Audio system description.
     */
    private String audioSystem;
    
    /**
     * Current status of the room.
     */
    private RoomStatus status;
    
    /**
     * Number of seats in this room.
     */
    private Integer totalSeats;
    
    /**
     * Timestamp when the room was created.
     */
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the room was last updated.
     */
    private LocalDateTime updatedAt;
}
