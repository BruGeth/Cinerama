package com.cinerama.backend.controller;

import com.cinerama.backend.dto.TicketPriceRequest;
import com.cinerama.backend.dto.TicketPriceResponse;
import com.cinerama.backend.enums.TicketType;
import com.cinerama.backend.service.TicketPriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing TicketPrice operations.
 * 
 * <p>This controller provides endpoints for CRUD operations
 * on TicketPrice entities and pricing-related queries.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@RestController
@RequestMapping("/api/ticket-prices")
@RequiredArgsConstructor
public class TicketPriceController {
    
    private final TicketPriceService ticketPriceService;
    
    /**
     * Retrieves all ticket prices.
     * 
     * @return List of all ticket price responses
     */
    @GetMapping
    public ResponseEntity<List<TicketPriceResponse>> getAllTicketPrices() {
        List<TicketPriceResponse> ticketPrices = ticketPriceService.getAllTicketPrices();
        return ResponseEntity.ok(ticketPrices);
    }
    
    /**
     * Retrieves a ticket price by its ID.
     * 
     * @param id The ticket price ID
     * @return The ticket price response if found, 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketPriceResponse> getTicketPriceById(@PathVariable Long id) {
        return ticketPriceService.getTicketPriceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Retrieves ticket prices by showtime ID.
     * 
     * @param showtimeId The showtime ID
     * @return List of ticket prices for the specified showtime
     */
    @GetMapping("/showtime/{showtimeId}")
    public ResponseEntity<List<TicketPriceResponse>> getTicketPricesByShowtimeId(@PathVariable Long showtimeId) {
        List<TicketPriceResponse> ticketPrices = ticketPriceService.getTicketPricesByShowtimeId(showtimeId);
        return ResponseEntity.ok(ticketPrices);
    }
    
    /**
     * Retrieves ticket prices by ticket type.
     * 
     * @param type The ticket type (GENERAL, CHILD, STUDENT, SENIOR, VIP)
     * @return List of ticket prices with the specified type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<TicketPriceResponse>> getTicketPricesByType(@PathVariable TicketType type) {
        List<TicketPriceResponse> ticketPrices = ticketPriceService.getTicketPricesByType(type);
        return ResponseEntity.ok(ticketPrices);
    }
    
    /**
     * Creates a new ticket price.
     * 
     * @param ticketPriceRequest The ticket price creation request
     * @return The created ticket price response with 201 status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TicketPriceResponse> createTicketPrice(@Valid @RequestBody TicketPriceRequest ticketPriceRequest) {
        TicketPriceResponse createdTicketPrice = ticketPriceService.createTicketPrice(ticketPriceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicketPrice);
    }
    
    /**
     * Creates multiple ticket prices for a showtime.
     * 
     * @param ticketPriceRequests List of ticket price creation requests
     * @return List of created ticket price responses with 201 status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/batch")
    public ResponseEntity<List<TicketPriceResponse>> createTicketPricesForShowtime(
            @Valid @RequestBody List<TicketPriceRequest> ticketPriceRequests) {
        List<TicketPriceResponse> createdTicketPrices = ticketPriceService.createTicketPricesForShowtime(ticketPriceRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicketPrices);
    }
    
    /**
     * Updates an existing ticket price.
     * 
     * @param id The ticket price ID
     * @param ticketPriceRequest The ticket price update request
     * @return The updated ticket price response
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TicketPriceResponse> updateTicketPrice(
            @PathVariable Long id, 
            @Valid @RequestBody TicketPriceRequest ticketPriceRequest) {
        TicketPriceResponse updatedTicketPrice = ticketPriceService.updateTicketPrice(id, ticketPriceRequest);
        return ResponseEntity.ok(updatedTicketPrice);
    }
    
    /**
     * Deletes a ticket price by its ID.
     * 
     * @param id The ticket price ID
     * @return 204 No Content if successful
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketPrice(@PathVariable Long id) {
        ticketPriceService.deleteTicketPrice(id);
        return ResponseEntity.noContent().build();
    }
}
