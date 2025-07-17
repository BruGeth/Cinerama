package com.cinerama.backend.repository;

import com.cinerama.backend.entity.Seat;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Seat entities.
 * 
 * <p>This interface provides CRUD operations and custom queries
 * for the Seat entity by extending JpaRepository.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    
    /**
     * Finds all available seats for a specific showtime.
     * 
     * @param showtimeId The showtime ID
     * @return List of available seats for the specified showtime
     */
    List<Seat> findByShowtimeIdAndAvailableTrue(Long showtimeId);
    
    /**
     * Finds all seats for a specific showtime.
     * 
     * @param showtimeId The showtime ID
     * @return List of all seats for the specified showtime
     */
    List<Seat> findByShowtimeId(Long showtimeId);
    
    /**
     * Finds all seats for a specific room.
     * 
     * @param roomId The room ID
     * @return List of all seats in the specified room
     */
    List<Seat> findByRoomId(Long roomId);
    
    /**
     * Finds seats by their IDs.
     * 
     * @param ids List of seat IDs
     * @return List of seats with the specified IDs
     */
    List<Seat> findByIdIn(List<Long> ids);
    
    /**
     * Finds available seats by their IDs.
     * 
     * @param seatIds List of seat IDs
     * @return List of available seats with the specified IDs
     */
    List<Seat> findAllByIdInAndAvailableTrue(@NotEmpty(message = "At least one seat must be selected") List<Long> seatIds);
    
    /**
     * Finds seats by seat type in a specific room.
     * 
     * @param roomId The room ID
     * @param seatType The seat type
     * @return List of seats with the specified type in the room
     */
    List<Seat> findByRoomIdAndSeatType(Long roomId, String seatType);
    
    /**
     * Counts available seats for a specific showtime.
     * 
     * @param showtimeId The showtime ID
     * @return Number of available seats
     */
    @Query("SELECT COUNT(s) FROM Seat s WHERE s.showtime.id = :showtimeId AND s.available = true")
    Long countAvailableSeatsByShowtime(@Param("showtimeId") Long showtimeId);
}
