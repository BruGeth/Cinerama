package com.cinerama.backend.controller;

import com.cinerama.backend.dto.MovieEnrichedResponse;
import com.cinerama.backend.dto.MovieRequest;
import com.cinerama.backend.dto.MovieResponse;
import com.cinerama.backend.dto.tmdb.TmdbSearchResultDTO;
import com.cinerama.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    // ========== EXISTING ENDPOINTS (unchanged) ==========

    @GetMapping
    public List<MovieResponse> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public MovieResponse createMovie(@RequestBody MovieRequest movieRequest) {
        return movieService.saveMovie(movieRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public MovieResponse updateMovie(@PathVariable Long id, @RequestBody MovieRequest movieRequest) {
        return movieService.updateMovie(id, movieRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-status")
    public ResponseEntity<String> updateMovieStatuses() {
        movieService.updateMovieStatuses();
        return ResponseEntity.ok("Estados de películas actualizados correctamente");
    }
    
    // ========== NEW TMDB INTEGRATION ENDPOINTS ==========
    
    /**
     * Gets enriched movie combining local and TMDB data.
     * 
     * @param localId Local movie ID (optional)
     * @param tmdbId TMDB movie ID (optional)
     * @return Enriched movie response
     */
    @GetMapping("/enriched")
    public ResponseEntity<MovieEnrichedResponse> getEnrichedMovie(
            @RequestParam(required = false) Long localId,
            @RequestParam(required = false) Long tmdbId) {
        
        if (localId == null && tmdbId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return movieService.getEnrichedMovieById(localId, tmdbId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Gets all movies enriched with TMDB data where available.
     * 
     * @return List of enriched movies
     */
    @GetMapping("/enriched/all")
    public List<MovieEnrichedResponse> getAllEnrichedMovies() {
        return movieService.getAllEnrichedMovies();
    }
    
    /**
     * Searches movies in TMDB external API.
     * Supports both 'query' and 'q' parameters for frontend compatibility.
     * 
     * @param query Search query string (alternative parameter name)
     * @param q Search query string (frontend expected parameter)
     * @param page Page number (optional, default: 1)
     * @return TMDB search results
     */
    @GetMapping("/search")
    public TmdbSearchResultDTO searchMovies(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "1") Integer page) {
        
        String searchQuery = q != null ? q : query;
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return TmdbSearchResultDTO.builder()
                    .page(1)
                    .results(List.of())
                    .totalResults(0)
                    .totalPages(0)
                    .build();
        }
        
        return movieService.searchMoviesFromTmdb(searchQuery, page);
    }
    
    /**
     * Gets popular movies from TMDB.
     * 
     * @param page Page number (optional, default: 1)
     * @return Popular movies from TMDB
     */
    @GetMapping("/popular")
    public TmdbSearchResultDTO getPopularMovies(@RequestParam(defaultValue = "1") Integer page) {
        return movieService.getPopularMoviesFromTmdb(page);
    }
    
    /**
     * Gets now playing movies from TMDB.
     * Frontend expects this endpoint for current theater releases.
     * 
     * @param page Page number (optional, default: 1)
     * @return Now playing movies from TMDB
     */
    @GetMapping("/now_playing")
    public TmdbSearchResultDTO getNowPlayingMovies(@RequestParam(defaultValue = "1") Integer page) {
        return movieService.getNowPlayingMoviesFromTmdb(page);
    }
    
    /**
     * Gets upcoming movies from TMDB.
     * 
     * @param page Page number (optional, default: 1)
     * @return Upcoming movies from TMDB
     */
    @GetMapping("/upcoming")
    public TmdbSearchResultDTO getUpcomingMovies(@RequestParam(defaultValue = "1") Integer page) {
        return movieService.getUpcomingMoviesFromTmdb(page);
    }
    
    // Legacy TMDB endpoints (kept for backward compatibility)
    
    @GetMapping("/tmdb/search")
    public TmdbSearchResultDTO searchMoviesInTmdb(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") Integer page) {
        return movieService.searchMoviesFromTmdb(query, page);
    }
    
    @GetMapping("/tmdb/popular")
    public TmdbSearchResultDTO getPopularMoviesFromTmdb(
            @RequestParam(defaultValue = "1") Integer page) {
        return movieService.getPopularMoviesFromTmdb(page);
    }
    
    @GetMapping("/tmdb/now-playing")
    public TmdbSearchResultDTO getNowPlayingMoviesFromTmdb(
            @RequestParam(defaultValue = "1") Integer page) {
        return movieService.getNowPlayingMoviesFromTmdb(page);
    }
    
    @GetMapping("/tmdb/upcoming")
    public TmdbSearchResultDTO getUpcomingMoviesFromTmdb(
            @RequestParam(defaultValue = "1") Integer page) {
        return movieService.getUpcomingMoviesFromTmdb(page);
    }
}