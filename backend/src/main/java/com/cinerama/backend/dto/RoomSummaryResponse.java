package com.cinerama.backend.dto;

import com.cinerama.backend.enums.RoomStatus;
import com.cinerama.backend.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Room summary information.
 * 
 * <p>This class represents a simplified view of Room entities
 * used in responses that include room information.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomSummaryResponse {
    
    /**
     * Unique identifier of the room.
     */
    private Long id;
    
    /**
     * Name of the room.
     */
    private String name;
    
    /**
     * Seating capacity of the room.
     */
    private Integer capacity;
    
    /**
     * Type of the room (STANDARD, PREMIUM, VIP, IMAX).
     */
    private RoomType type;
    
    /**
     * Current status of the room.
     */
    private RoomStatus status;
}
