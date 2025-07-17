package com.cinerama.backend.service;

import com.cinerama.backend.entity.Seat;

import java.util.List;

/**
 * Service interface for managing Seat operations.
 * 
 * <p>This interface defines the business logic operations
 * for Seat entities.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
public interface SeatService {
    
    /**
     * Retrieves available seats for a specific showtime.
     * 
     * @param showtimeId The showtime ID
     * @return List of available seats for the showtime
     */
    List<Seat> getAvailableSeats(Long showtimeId);
    
    /**
     * Reserves seats for a specific showtime.
     * 
     * @param showtimeId The showtime ID
     * @param seatIds List of seat IDs to reserve
     * @return List of reserved seats
     */
    List<Seat> reserveSeats(Long showtimeId, List<Long> seatIds);
    
    /**
     * Retrieves all seats for a specific showtime.
     * 
     * @param showtimeId The showtime ID
     * @return List of all seats for the showtime
     */
    List<Seat> getSeatsByShowtime(Long showtimeId);
    
    /**
     * Retrieves all seats for a specific room.
     * 
     * @param roomId The room ID
     * @return List of all seats in the room
     */
    List<Seat> getSeatsByRoom(Long roomId);
    
    /**
     * Counts available seats for a specific showtime.
     * 
     * @param showtimeId The showtime ID
     * @return Number of available seats
     */
    Long countAvailableSeats(Long showtimeId);
}
