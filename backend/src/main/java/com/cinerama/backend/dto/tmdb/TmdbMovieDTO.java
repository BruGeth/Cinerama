package com.cinerama.backend.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for TMDB Movie API responses.
 * Maps the external TMDB movie structure.
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-11-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TmdbMovieDTO {
    
    /**
     * TMDB movie ID (external).
     */
    @JsonProperty("id")
    private Long tmdbId;
    
    /**
     * Movie title.
     */
    private String title;
    
    /**
     * Original movie title.
     */
    @JsonProperty("original_title")
    private String originalTitle;
    
    /**
     * Movie overview/synopsis.
     */
    private String overview;
    
    /**
     * Release date (YYYY-MM-DD format).
     */
    @JsonProperty("release_date")
    private String releaseDate;
    
    /**
     * Poster path (relative path, needs base URL).
     */
    @JsonProperty("poster_path")
    private String posterPath;
    
    /**
     * Backdrop path (relative path, needs base URL).
     */
    @JsonProperty("backdrop_path")
    private String backdropPath;
    
    /**
     * Average vote/rating (0-10 scale).
     */
    @JsonProperty("vote_average")
    private Double voteAverage;
    
    /**
     * Number of votes.
     */
    @JsonProperty("vote_count")
    private Integer voteCount;
    
    /**
     * Popularity score.
     */
    private Double popularity;
    
    /**
     * Original language code.
     */
    @JsonProperty("original_language")
    private String originalLanguage;
    
    /**
     * Adult content flag.
     */
    private Boolean adult;
    
    /**
     * Genre IDs from TMDB.
     */
    @JsonProperty("genre_ids")
    private List<Integer> genreIds;
    
    /**
     * Runtime in minutes (only in detailed responses).
     */
    private Integer runtime;
    
    /**
     * Movie tagline.
     */
    private String tagline;
    
    /**
     * Production status (Released, Post Production, etc.).
     */
    private String status;
    
    /**
     * Budget in USD.
     */
    private Long budget;
    
    /**
     * Revenue in USD.
     */
    private Long revenue;
    
    /**
     * Homepage URL.
     */
    private String homepage;
    
    /**
     * IMDB ID.
     */
    @JsonProperty("imdb_id")
    private String imdbId;
    
    /**
     * Get full poster URL with base image URL.
     */
    public String getFullPosterUrl() {
        if (posterPath != null && !posterPath.isEmpty()) {
            return "https://image.tmdb.org/t/p/w500" + posterPath;
        }
        return null;
    }
    
    /**
     * Get full backdrop URL with base image URL.
     */
    public String getFullBackdropUrl() {
        if (backdropPath != null && !backdropPath.isEmpty()) {
            return "https://image.tmdb.org/t/p/original" + backdropPath;
        }
        return null;
    }
}
