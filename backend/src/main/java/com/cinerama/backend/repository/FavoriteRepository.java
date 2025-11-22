package com.cinerama.backend.repository;

import com.cinerama.backend.entity.Favorite;
import com.cinerama.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Favorite entity operations.
 * 
 * @author Cinerama Development Team
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    /**
     * Find all favorites for a specific user.
     */
    List<Favorite> findByUser(User user);
    
    /**
     * Find all favorites for a specific user by user ID.
     */
    List<Favorite> findByUserId(Long userId);
    
    /**
     * Check if a specific movie is favorited by a user.
     */
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
    
    /**
     * Find a specific favorite by user and movie.
     */
    Optional<Favorite> findByUserIdAndMovieId(Long userId, Long movieId);
    
    /**
     * Delete a specific favorite by user and movie.
     */
    void deleteByUserIdAndMovieId(Long userId, Long movieId);
}
