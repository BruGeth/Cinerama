package com.cinerama.backend.dto;

import com.cinerama.backend.enums.MovieStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Movie response operations.
 * 
 * <p>This class represents the data structure returned
 * when retrieving Movie entities through API responses.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieResponse {
    
    /**
     * Unique identifier of the movie.
     */
    private Long id;
    
    /**
     * Title of the movie.
     */
    private String title;
    
    /**
     * Short description for showtimes display.
     */
    private String descriptionShowtimes;
    
    /**
     * Complete movie synopsis.
     */
    private String descriptionMovie;
    
    /**
     * Movie duration in minutes.
     */
    private Integer duration;
    
    /**
     * Movie rating (e.g., PG, PG-13, R).
     */
    private String rating;
    
    /**
     * Name of the genre this movie belongs to.
     */
    private String genreName;
    
    /**
     * ID of the genre (for editing purposes).
     */
    private Long genreId;
    
    /**
     * URL of the movie poster image.
     */
    private String imageUrl;
    
    /**
     * URL of the movie trailer.
     */
    private String trailerUrl;
    
    /**
     * Director of the movie.
     */
    private String director;
    
    /**
     * List of main cast members.
     */
    private List<String> cast;
    
    /**
     * Release date of the movie.
     */
    private LocalDate releaseDate;
    
    /**
     * Current status of the movie.
     */
    private MovieStatus status;
    
    /**
     * Timestamp when the movie was created.
     */
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the movie was last updated.
     */
    private LocalDateTime updatedAt;
}