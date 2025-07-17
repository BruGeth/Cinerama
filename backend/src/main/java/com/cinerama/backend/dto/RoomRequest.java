package com.cinerama.backend.dto;

import com.cinerama.backend.enums.RoomStatus;
import com.cinerama.backend.enums.RoomType;
import com.cinerama.backend.enums.TechnologyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for Room request operations.
 * 
 * <p>This class represents the data structure used for creating
 * and updating Room entities through API requests.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequest {
    
    /**
     * Name of the room.
     * Must not be blank.
     */
    @NotBlank(message = "Room name is required")
    private String name;
    
    /**
     * ID of the cinema this room belongs to.
     * Must not be null.
     */
    @NotNull(message = "Cinema ID is required")
    private Long cinemaId;
    
    /**
     * Seating capacity of the room.
     * Must be a positive number.
     */
    @Positive(message = "Capacity must be positive")
    @NotNull(message = "Room capacity is required")
    private Integer capacity;
    
    /**
     * Type of the room.
     * Must not be null.
     */
    @NotNull(message = "Room type is required")
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
     * Must not be null.
     */
    @NotNull(message = "Room status is required")
    private RoomStatus status;
}
