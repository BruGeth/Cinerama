package com.cinerama.backend.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for TMDB Search Results.
 * Wraps paginated search responses from TMDB API.
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-11-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TmdbSearchResultDTO {
    
    /**
     * Current page number.
     */
    private Integer page;
    
    /**
     * List of movie results.
     */
    private List<TmdbMovieDTO> results;
    
    /**
     * Total number of results.
     */
    @JsonProperty("total_results")
    private Integer totalResults;
    
    /**
     * Total number of pages.
     */
    @JsonProperty("total_pages")
    private Integer totalPages;
}
