package com.cinerama.backend.dto;

import com.cinerama.backend.enums.TicketType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for TicketPrice request operations.
 * 
 * <p>This class represents the data structure used for creating
 * and updating TicketPrice entities through API requests.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketPriceRequest {
    
    /**
     * ID of the showtime this price applies to.
     * Must not be null.
     */
    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;
    
    /**
     * Type of ticket this price applies to.
     * Must not be null.
     */
    @NotNull(message = "Ticket type is required")
    private TicketType type;
    
    /**
     * Price amount for this ticket type.
     * Must be positive.
     */
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @NotNull(message = "Price is required")
    private BigDecimal price;
}
