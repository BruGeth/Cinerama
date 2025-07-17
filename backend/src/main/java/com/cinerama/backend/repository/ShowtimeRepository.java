package com.cinerama.backend.repository;

import com.cinerama.backend.entity.Showtime;
import com.cinerama.backend.enums.ShowtimeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing Showtime entities.
 * 
 * <p>This interface provides CRUD operations and custom queries
 * for the Showtime entity by extending JpaRepository.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    
    /**
     * Finds all showtimes for a specific movie.
     * 
     * @param movieId The movie ID
     * @return List of showtimes for the specified movie
     */
    List<Showtime> findByMovieId(Long movieId);
    
    /**
     * Finds all showtimes for a specific cinema.
     * 
     * @param cinemaId The cinema ID
     * @return List of showtimes for the specified cinema
     */
    List<Showtime> findByCinemaId(Long cinemaId);
    
    /**
     * Finds all showtimes for a specific room.
     * 
     * @param roomId The room ID
     * @return List of showtimes for the specified room
     */
    List<Showtime> findByRoomId(Long roomId);
    
    /**
     * Finds all showtimes for a specific date.
     * 
     * @param showDate The show date
     * @return List of showtimes for the specified date
     */
    List<Showtime> findByShowDate(LocalDate showDate);
    
    /**
     * Finds all showtimes by status.
     * 
     * @param status The showtime status
     * @return List of showtimes with the specified status
     */
    List<Showtime> findByStatus(ShowtimeStatus status);
    
    /**
     * Finds active showtimes for a specific movie and date.
     * 
     * @param movieId The movie ID
     * @param showDate The show date
     * @return List of active showtimes for the movie on the specified date
     */
    @Query("SELECT s FROM Showtime s WHERE s.movie.id = :movieId AND s.showDate = :showDate AND s.status = 'ACTIVE'")
    List<Showtime> findActiveShowtimesByMovieAndDate(@Param("movieId") Long movieId, @Param("showDate") LocalDate showDate);
    
    /**
     * Finds active showtimes for a specific cinema and date.
     * 
     * @param cinemaId The cinema ID
     * @param showDate The show date
     * @return List of active showtimes for the cinema on the specified date
     */
    @Query("SELECT s FROM Showtime s WHERE s.cinema.id = :cinemaId AND s.showDate = :showDate AND s.status = 'ACTIVE'")
    List<Showtime> findActiveShowtimesByCinemaAndDate(@Param("cinemaId") Long cinemaId, @Param("showDate") LocalDate showDate);
    
    /**
     * Finds showtimes between two dates.
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return List of showtimes between the specified dates
     */
    List<Showtime> findByShowDateBetween(LocalDate startDate, LocalDate endDate);
}