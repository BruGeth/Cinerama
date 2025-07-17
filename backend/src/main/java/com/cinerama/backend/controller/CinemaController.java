package com.cinerama.backend.controller;

import com.cinerama.backend.dto.CinemaRequest;
import com.cinerama.backend.dto.CinemaResponse;
import com.cinerama.backend.enums.CinemaStatus;
import com.cinerama.backend.service.CinemaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing Cinema operations.
 * 
 * <p>This controller provides endpoints for CRUD operations
 * on Cinema entities.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@RestController
@RequestMapping("/api/cinemas")
@RequiredArgsConstructor
public class CinemaController {
    
    private final CinemaService cinemaService;
    
    /**
     * Retrieves all cinemas.
     * 
     * @return List of all cinema responses
     */
    @GetMapping
    public ResponseEntity<List<CinemaResponse>> getAllCinemas() {
        List<CinemaResponse> cinemas = cinemaService.getAllCinemas();
        return ResponseEntity.ok(cinemas);
    }
    
    /**
     * Retrieves a cinema by its ID.
     * 
     * @param id The cinema ID
     * @return The cinema response if found, 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<CinemaResponse> getCinemaById(@PathVariable Long id) {
        return cinemaService.getCinemaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Retrieves cinemas by status.
     * 
     * @param status The cinema status
     * @return List of cinemas with the specified status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CinemaResponse>> getCinemasByStatus(@PathVariable CinemaStatus status) {
        List<CinemaResponse> cinemas = cinemaService.getCinemasByStatus(status);
        return ResponseEntity.ok(cinemas);
    }
    
    /**
     * Retrieves cinemas by city.
     * 
     * @param city The city name
     * @return List of cinemas in the specified city
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<CinemaResponse>> getCinemasByCity(@PathVariable String city) {
        List<CinemaResponse> cinemas = cinemaService.getCinemasByCity(city);
        return ResponseEntity.ok(cinemas);
    }
    
    /**
     * Searches cinemas by name.
     * 
     * @param name The name search term
     * @return List of cinemas matching the search term
     */
    @GetMapping("/search")
    public ResponseEntity<List<CinemaResponse>> searchCinemasByName(@RequestParam String name) {
        List<CinemaResponse> cinemas = cinemaService.searchCinemasByName(name);
        return ResponseEntity.ok(cinemas);
    }
    
    /**
     * Creates a new cinema.
     * 
     * @param cinemaRequest The cinema creation request
     * @return The created cinema response
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CinemaResponse> createCinema(@Valid @RequestBody CinemaRequest cinemaRequest) {
        CinemaResponse createdCinema = cinemaService.createCinema(cinemaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCinema);
    }
    
    /**
     * Updates an existing cinema.
     * 
     * @param id The cinema ID
     * @param cinemaRequest The cinema update request
     * @return The updated cinema response
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CinemaResponse> updateCinema(@PathVariable Long id, 
                                                       @Valid @RequestBody CinemaRequest cinemaRequest) {
        CinemaResponse updatedCinema = cinemaService.updateCinema(id, cinemaRequest);
        return ResponseEntity.ok(updatedCinema);
    }
    
    /**
     * Deletes a cinema by its ID.
     * 
     * @param id The cinema ID
     * @return 204 No Content if successful
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCinema(@PathVariable Long id) {
        cinemaService.deleteCinema(id);
        return ResponseEntity.noContent().build();
    }
}
