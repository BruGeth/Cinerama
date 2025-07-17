package com.cinerama.backend.repository;

import com.cinerama.backend.entity.Cinema;
import com.cinerama.backend.enums.CinemaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Cinema entities.
 * 
 * <p>This interface provides CRUD operations and custom queries
 * for the Cinema entity by extending JpaRepository.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    
    /**
     * Finds all cinemas by their status.
     * 
     * @param status The status to filter by (ACTIVE, INACTIVE)
     * @return List of cinemas with the specified status
     */
    List<Cinema> findByStatus(CinemaStatus status);
    
    /**
     * Finds all cinemas in a specific city.
     * 
     * @param city The city name
     * @return List of cinemas in the specified city
     */
    List<Cinema> findByCity(String city);
    
    /**
     * Finds all active cinemas in a specific city.
     * 
     * @param city The city name
     * @return List of active cinemas in the specified city
     */
    @Query("SELECT c FROM Cinema c WHERE c.city = :city AND c.status = 'ACTIVE'")
    List<Cinema> findActiveCinemasByCity(@Param("city") String city);
    
    /**
     * Finds cinemas by name containing the search term (case-insensitive).
     * 
     * @param name The name search term
     * @return List of cinemas with names containing the search term
     */
    List<Cinema> findByNameContainingIgnoreCase(String name);
}
