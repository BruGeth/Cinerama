package com.cinerama.backend.dto.tmdb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for TMDB Video Results.
 * Wraps list of videos (trailers) for a movie.
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-11-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TmdbVideoResultDTO {
    
    private Long id;
    private List<TmdbVideoDTO> results;
}
