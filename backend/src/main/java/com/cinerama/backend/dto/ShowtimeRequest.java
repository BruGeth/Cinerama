package com.cinerama.backend.dto;

import com.cinerama.backend.enums.ShowtimeFormat;
import com.cinerama.backend.enums.ShowtimeLanguage;
import com.cinerama.backend.enums.ShowtimeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Transfer Object for Showtime request operations.
 * 
 * <p>This class represents the data structure used for creating
 * and updating Showtime entities through API requests.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimeRequest {
    
    /**
     * ID of the movie for this showtime.
     * Must not be null.
     */
    @NotNull(message = "Movie ID is required")
    private Long movieId;
    
    /**
     * ID of the cinema for this showtime.
     * Must not be null.
     */
    @NotNull(message = "Cinema ID is required")
    private Long cinemaId;
    
    /**
     * ID of the room for this showtime.
     * Must not be null.
     */
    @NotNull(message = "Room ID is required")
    private Long roomId;
    
    /**
     * Date of the show.
     * Must not be null.
     */
    @NotNull(message = "Show date is required")
    private LocalDate showDate;
    
    /**
     * Time of the show.
     * Must not be null.
     */
    @NotNull(message = "Show time is required")
    private LocalTime showTime;
    
    /**
     * Format of the showtime (e.g., 2D, 3D, IMAX).
     */
    private ShowtimeFormat format;
    
    /**
     * Language of the showtime.
     */
    private ShowtimeLanguage language;
    
    /**
     * Status of the showtime.
     */
    private ShowtimeStatus status;
}