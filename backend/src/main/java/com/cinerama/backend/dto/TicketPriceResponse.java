package com.cinerama.backend.dto;

import com.cinerama.backend.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for TicketPrice response operations.
 * 
 * <p>This class represents the data structure returned
 * when retrieving TicketPrice entities through API responses.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketPriceResponse {
    
    /**
     * Unique identifier of the ticket price.
     */
    private Long id;
    
    /**
     * ID of the showtime this price applies to.
     */
    private Long showtimeId;
    
    /**
     * Type of ticket this price applies to.
     */
    private TicketType type;
    
    /**
     * Price amount for this ticket type.
     */
    private BigDecimal price;
    
    /**
     * Timestamp when the ticket price was created.
     */
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the ticket price was last updated.
     */
    private LocalDateTime updatedAt;
}
