package com.cinerama.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for batch TicketPrice operations.
 * 
 * <p>This class represents the data structure used for creating
 * multiple TicketPrice entities in a single request.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketPriceBatchRequest {
    
    /**
     * ID of the showtime these prices apply to.
     * Must not be null.
     */
    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;
    
    /**
     * List of ticket price entries to create.
     * Must not be empty.
     */
    @NotEmpty(message = "At least one price entry is required")
    @Valid
    private List<TicketPriceEntryRequest> prices;
    
    /**
     * Individual price entry for batch creation.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TicketPriceEntryRequest {
        /**
         * Type of ticket this price applies to.
         */
        @NotNull(message = "Ticket type is required")
        private com.cinerama.backend.enums.TicketType type;
        
        /**
         * Format this price applies to.
         */
        @NotNull(message = "Format is required")
        private com.cinerama.backend.enums.ShowtimeFormat format;
        
        /**
         * Price amount.
         */
        @NotNull(message = "Price is required")
        private java.math.BigDecimal price;
    }
}
