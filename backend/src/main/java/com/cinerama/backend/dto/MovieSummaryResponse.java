package com.cinerama.backend.dto;

import com.cinerama.backend.enums.MovieStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Movie summary information.
 * 
 * <p>This class represents a simplified view of Movie entities
 * used in responses that include movie information.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieSummaryResponse {
    
    /**
     * Unique identifier of the movie.
     */
    private Long id;
    
    /**
     * Title of the movie.
     */
    private String title;
    
    /**
     * Duration of the movie in minutes.
     */
    private Integer duration;
    
    /**
     * Rating of the movie (e.g., PG-13, R).
     */
    private String rating;
    
    /**
     * URL to the movie poster.
     */
    private String posterUrl;
    
    /**
     * Current status of the movie.
     */
    private MovieStatus status;
}
