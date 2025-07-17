package com.cinerama.backend.dto;

import com.cinerama.backend.enums.MovieStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object for Movie request operations.
 * 
 * <p>This class represents the data structure used for creating
 * and updating Movie entities through API requests.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRequest {
    
    /**
     * Title of the movie.
     * Must not be blank.
     */
    @NotBlank(message = "Movie title is required")
    private String title;
    
    /**
     * Short description for showtimes display.
     * Must not be blank.
     */
    @NotBlank(message = "Showtimes description is required")
    private String descriptionShowtimes;
    
    /**
     * Complete movie synopsis.
     * Must not be blank.
     */
    @NotBlank(message = "Movie description is required")
    private String descriptionMovie;
    
    /**
     * Movie duration in minutes.
     * Must be positive.
     */
    @Positive(message = "Duration must be positive")
    @NotNull(message = "Duration is required")
    private Integer duration;
    
    /**
     * Movie rating (e.g., PG, PG-13, R).
     * Must not be blank.
     */
    @NotBlank(message = "Movie rating is required")
    private String rating;
    
    /**
     * ID of the genre this movie belongs to.
     * Must not be null.
     */
    @NotNull(message = "Genre ID is required")
    private Long genreId;
    
    /**
     * URL of the movie poster image.
     * Must not be blank.
     */
    @NotBlank(message = "Image URL is required")
    private String imageUrl;
    
    /**
     * URL of the movie trailer (optional).
     */
    private String trailerUrl;
    
    /**
     * Director of the movie (optional).
     */
    private String director;
    
    /**
     * List of main cast members (optional).
     */
    private List<String> cast;
    
    /**
     * Release date of the movie (optional).
     */
    private LocalDate releaseDate;
    
    /**
     * Current status of the movie (optional).
     * Defaults to NOW_PLAYING if not specified.
     */
    private MovieStatus status;
}