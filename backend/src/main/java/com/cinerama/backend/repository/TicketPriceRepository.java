package com.cinerama.backend.repository;

import com.cinerama.backend.entity.TicketPrice;
import com.cinerama.backend.enums.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing TicketPrice entities.
 * 
 * <p>This interface provides CRUD operations and custom queries
 * for the TicketPrice entity by extending JpaRepository.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Repository
public interface TicketPriceRepository extends JpaRepository<TicketPrice, Long> {
    
    /**
     * Finds all ticket prices for a specific showtime.
     * 
     * @param showtimeId The showtime ID
     * @return List of ticket prices for the specified showtime
     */
    List<TicketPrice> findByShowtimeId(Long showtimeId);
    
    /**
     * Finds all ticket prices by ticket type.
     * 
     * @param type The ticket type (GENERAL, CHILD, STUDENT, SENIOR, VIP)
     * @return List of ticket prices with the specified type
     */
    List<TicketPrice> findByType(TicketType type);
    
    /**
     * Finds a specific ticket price for a showtime and ticket type.
     * 
     * @param showtimeId The showtime ID
     * @param type The ticket type
     * @return Optional containing the ticket price if found
     */
    Optional<TicketPrice> findByShowtimeIdAndType(Long showtimeId, TicketType type);
    
    /**
     * Finds ticket prices within a price range.
     * 
     * @param minPrice The minimum price
     * @param maxPrice The maximum price
     * @return List of ticket prices within the specified range
     */
    List<TicketPrice> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Finds all ticket prices for showtimes of a specific movie.
     * 
     * @param movieId The movie ID
     * @return List of ticket prices for the specified movie
     */
    @Query("SELECT tp FROM TicketPrice tp WHERE tp.showtime.movie.id = :movieId")
    List<TicketPrice> findByMovieId(@Param("movieId") Long movieId);
    
    /**
     * Checks if a ticket price exists for a specific showtime and type.
     * 
     * @param showtimeId The showtime ID
     * @param type The ticket type
     * @return True if ticket price exists, false otherwise
     */
    boolean existsByShowtimeIdAndType(Long showtimeId, TicketType type);
}
