package com.cinerama.backend.dto;

import com.cinerama.backend.enums.ShowtimeFormat;
import com.cinerama.backend.enums.ShowtimeLanguage;
import com.cinerama.backend.enums.ShowtimeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Data Transfer Object for Showtime response operations.
 * 
 * <p>This class represents the data structure returned
 * when retrieving Showtime entities through API responses.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimeResponse {
    
    /**
     * Unique identifier of the showtime.
     */
    private Long id;
    
    /**
     * Movie information for this showtime.
     */
    private MovieSummaryResponse movie;
    
    /**
     * Cinema information for this showtime.
     */
    private CinemaSummaryResponse cinema;
    
    /**
     * Room information for this showtime.
     */
    private RoomSummaryResponse room;
    
    /**
     * Date of the show.
     */
    private LocalDate showDate;
    
    /**
     * Time of the show.
     */
    private LocalTime showTime;
    
    /**
     * Format of the showtime.
     */
    private ShowtimeFormat format;
    
    /**
     * Language of the showtime.
     */
    private ShowtimeLanguage language;
    
    /**
     * Number of available seats.
     */
    private Integer availableSeats;
    
    /**
     * Status of the showtime.
     */
    private ShowtimeStatus status;
    
    /**
     * Ticket prices for this showtime.
     */
    private List<TicketPriceResponse> ticketPrices;
    
    /**
     * Timestamp when the showtime was created.
     */
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the showtime was last updated.
     */
    private LocalDateTime updatedAt;
}
