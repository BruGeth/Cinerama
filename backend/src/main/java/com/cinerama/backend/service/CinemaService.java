package com.cinerama.backend.service;

import com.cinerama.backend.dto.CinemaRequest;
import com.cinerama.backend.dto.CinemaResponse;
import com.cinerama.backend.enums.CinemaStatus;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing Cinema operations.
 * 
 * <p>This interface defines the business logic operations
 * for Cinema entities.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
public interface CinemaService {
    
    /**
     * Retrieves all cinemas.
     * 
     * @return List of all cinema responses
     */
    List<CinemaResponse> getAllCinemas();
    
    /**
     * Retrieves a cinema by its ID.
     * 
     * @param id The cinema ID
     * @return Optional containing the cinema response if found
     */
    Optional<CinemaResponse> getCinemaById(Long id);
    
    /**
     * Retrieves cinemas by status.
     * 
     * @param status The cinema status
     * @return List of cinemas with the specified status
     */
    List<CinemaResponse> getCinemasByStatus(CinemaStatus status);
    
    /**
     * Retrieves cinemas by city.
     * 
     * @param city The city name
     * @return List of cinemas in the specified city
     */
    List<CinemaResponse> getCinemasByCity(String city);
    
    /**
     * Creates a new cinema.
     * 
     * @param cinemaRequest The cinema creation request
     * @return The created cinema response
     */
    CinemaResponse createCinema(CinemaRequest cinemaRequest);
    
    /**
     * Updates an existing cinema.
     * 
     * @param id The cinema ID
     * @param cinemaRequest The cinema update request
     * @return The updated cinema response
     */
    CinemaResponse updateCinema(Long id, CinemaRequest cinemaRequest);
    
    /**
     * Deletes a cinema by its ID.
     * 
     * @param id The cinema ID
     */
    void deleteCinema(Long id);
    
    /**
     * Searches cinemas by name.
     * 
     * @param name The name search term
     * @return List of cinemas matching the search term
     */
    List<CinemaResponse> searchCinemasByName(String name);
}
