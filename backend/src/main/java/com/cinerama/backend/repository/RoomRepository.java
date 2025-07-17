package com.cinerama.backend.repository;

import com.cinerama.backend.entity.Room;
import com.cinerama.backend.enums.RoomStatus;
import com.cinerama.backend.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Room entities.
 * 
 * <p>This interface provides CRUD operations and custom queries
 * for the Room entity by extending JpaRepository.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    /**
     * Finds all rooms belonging to a specific cinema.
     * 
     * @param cinemaId The cinema ID
     * @return List of rooms in the specified cinema
     */
    List<Room> findByCinemaId(Long cinemaId);
    
    /**
     * Finds all rooms by their status.
     * 
     * @param status The room status (ACTIVE, INACTIVE, MAINTENANCE)
     * @return List of rooms with the specified status
     */
    List<Room> findByStatus(RoomStatus status);
    
    /**
     * Finds all rooms by their type.
     * 
     * @param type The room type (STANDARD, PREMIUM, VIP, IMAX)
     * @return List of rooms with the specified type
     */
    List<Room> findByType(RoomType type);
    
    /**
     * Finds all active rooms in a specific cinema.
     * 
     * @param cinemaId The cinema ID
     * @return List of active rooms in the specified cinema
     */
    @Query("SELECT r FROM Room r WHERE r.cinema.id = :cinemaId AND r.status = 'ACTIVE'")
    List<Room> findActiveRoomsByCinema(@Param("cinemaId") Long cinemaId);
    
    /**
     * Finds rooms by name containing the search term (case-insensitive).
     * 
     * @param name The name search term
     * @return List of rooms with names containing the search term
     */
    List<Room> findByNameContainingIgnoreCase(String name);
    
    /**
     * Finds rooms with capacity greater than or equal to the specified value.
     * 
     * @param capacity The minimum capacity
     * @return List of rooms with capacity >= specified value
     */
    List<Room> findByCapacityGreaterThanEqual(Integer capacity);
    
    /**
     * Finds all rooms belonging to a specific cinema with a specific status.
     * 
     * @param cinemaId The cinema ID
     * @param status The room status
     * @return List of rooms in the specified cinema with the specified status
     */
    List<Room> findByCinemaIdAndStatus(Long cinemaId, RoomStatus status);
}
