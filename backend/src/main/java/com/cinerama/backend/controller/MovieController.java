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
     * 
     * @param query Search query string
     * @param page Page number (optional, default: 1)
     * @return TMDB search results
     */
    @GetMapping("/tmdb/search")
    public TmdbSearchResultDTO searchMoviesInTmdb(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") Integer page) {
        return movieService.searchMoviesFromTmdb(query, page);
    }
    
    /**
     * Gets popular movies from TMDB.
     * 
     * @param page Page number (optional, default: 1)
     * @return Popular movies from TMDB
     */
    @GetMapping("/tmdb/popular")
    public TmdbSearchResultDTO getPopularMoviesFromTmdb(
            @RequestParam(defaultValue = "1") Integer page) {
        return movieService.getPopularMoviesFromTmdb(page);
    }
    
    /**
     * Gets now playing movies from TMDB.
     * 
     * @param page Page number (optional, default: 1)
     * @return Now playing movies from TMDB
     */
    @GetMapping("/tmdb/now-playing")
    public TmdbSearchResultDTO getNowPlayingMoviesFromTmdb(
            @RequestParam(defaultValue = "1") Integer page) {
        return movieService.getNowPlayingMoviesFromTmdb(page);
    }
    
    /**
     * Gets upcoming movies from TMDB.
     * 
     * @param page Page number (optional, default: 1)
     * @return Upcoming movies from TMDB
     */
    @GetMapping("/tmdb/upcoming")
    public TmdbSearchResultDTO getUpcomingMoviesFromTmdb(
            @RequestParam(defaultValue = "1") Integer page) {
        return movieService.getUpcomingMoviesFromTmdb(page);
    }
}