package com.cinerama.backend.service;

import com.cinerama.backend.dto.TicketPriceBatchRequest;
import com.cinerama.backend.dto.TicketPriceRequest;
import com.cinerama.backend.dto.TicketPriceResponse;
import com.cinerama.backend.enums.TicketType;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing TicketPrice operations.
 * 
 * <p>This interface defines the business logic operations
 * for TicketPrice entities.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
public interface TicketPriceService {
    
    /**
     * Retrieves all ticket prices.
     * 
     * @return List of all ticket price responses
     */
    List<TicketPriceResponse> getAllTicketPrices();
    
    /**
     * Retrieves a ticket price by its ID.
     * 
     * @param id The ticket price ID
     * @return Optional containing the ticket price response if found
     */
    Optional<TicketPriceResponse> getTicketPriceById(Long id);
    
    /**
     * Retrieves ticket prices by showtime ID.
     * 
     * @param showtimeId The showtime ID
     * @return List of ticket prices for the specified showtime
     */
    List<TicketPriceResponse> getTicketPricesByShowtimeId(Long showtimeId);
    
    /**
     * Retrieves ticket prices by type.
     * 
     * @param type The ticket type
     * @return List of ticket prices with the specified type
     */
    List<TicketPriceResponse> getTicketPricesByType(TicketType type);
    
    /**
     * Creates a new ticket price.
     * 
     * @param ticketPriceRequest The ticket price creation request
     * @return The created ticket price response
     */
    TicketPriceResponse createTicketPrice(TicketPriceRequest ticketPriceRequest);
    
    /**
     * Updates an existing ticket price.
     * 
     * @param id The ticket price ID
     * @param ticketPriceRequest The ticket price update request
     * @return The updated ticket price response
     */
    TicketPriceResponse updateTicketPrice(Long id, TicketPriceRequest ticketPriceRequest);
    
    /**
     * Deletes a ticket price by its ID.
     * 
     * @param id The ticket price ID
     */
    void deleteTicketPrice(Long id);
    
    /**
     * Creates multiple ticket prices for a showtime.
     * 
     * @param ticketPriceRequests List of ticket price creation requests
     * @return List of created ticket price responses
     */
    List<TicketPriceResponse> createTicketPricesForShowtime(List<TicketPriceRequest> ticketPriceRequests);
    
    /**
     * Creates multiple ticket prices using batch request.
     * 
     * @param batchRequest The batch request containing showtime ID and ticket price requests
     * @return List of created ticket price responses
     */
    List<TicketPriceResponse> createBatchTicketPrices(TicketPriceBatchRequest batchRequest);
}
