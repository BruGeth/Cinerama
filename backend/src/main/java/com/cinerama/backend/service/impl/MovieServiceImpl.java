package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.MovieRequest;
import com.cinerama.backend.dto.MovieResponse;
import com.cinerama.backend.entity.Movie;
import com.cinerama.backend.enums.MovieStatus;
import com.cinerama.backend.repository.MovieRepository;
import com.cinerama.backend.repository.GenreRepository;
import com.cinerama.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    @Override
    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(this::toMovieResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MovieResponse> getMovieById(Long id) {
        return movieRepository.findById(id)
                .map(this::toMovieResponse);
    }

    @Override
    public MovieResponse saveMovie(MovieRequest movieRequest) {
        Movie movie = toMovieEntity(movieRequest);
        Movie saved = movieRepository.save(movie);
        return toMovieResponse(saved);
    }

    @Override
    public MovieResponse updateMovie(Long id, MovieRequest movieRequest) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new java.util.NoSuchElementException("Movie not found"));

        movie.setTitle(movieRequest.getTitle());
        movie.setDescriptionShowtimes(movieRequest.getDescriptionShowtimes());
        movie.setDescriptionMovie(movieRequest.getDescriptionMovie());
        movie.setRating(movieRequest.getRating());
        movie.setGenre(genreRepository.findById(movieRequest.getGenreId()).orElse(null));
        movie.setPosterUrl(movieRequest.getImageUrl());
        movie.setDuration(movieRequest.getDuration());
        movie.setTrailerUrl(movieRequest.getTrailerUrl());
        movie.setDirector(movieRequest.getDirector());
        movie.setCast(movieRequest.getCast());
        movie.setReleaseDate(movieRequest.getReleaseDate());
        movie.setStatus(movieRequest.getStatus() != null ? movieRequest.getStatus() : com.cinerama.backend.enums.MovieStatus.NOW_PLAYING);

        Movie updated = movieRepository.save(movie);
        return toMovieResponse(updated);
    }

    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateMovieStatuses() {
        log.info("🎬 Iniciando actualización de estados de películas");
        
        List<Movie> movies = movieRepository.findAll();
        LocalDate today = LocalDate.now();
        int updatedCount = 0;
        
        log.info("📊 Total de películas encontradas: {}", movies.size());
        log.info("📅 Fecha actual: {}", today);

        for (Movie movie : movies) {
            if (movie.getReleaseDate() != null) {
                MovieStatus currentStatus = movie.getStatus();
                
                // Determine new status based on release date
                MovieStatus newStatus;
                LocalDate releaseDate = movie.getReleaseDate();
                long daysSinceRelease = java.time.temporal.ChronoUnit.DAYS.between(releaseDate, today);
                
                if (releaseDate.isAfter(today)) {
                    // Future date
                    newStatus = MovieStatus.COMING_SOON;
                } else if (daysSinceRelease <= 90) {
                    // Past but recent date (up to 90 days)
                    newStatus = MovieStatus.NOW_PLAYING;
                } else {
                    // Very past date (more than 90 days)
                    newStatus = MovieStatus.ENDED;
                }

                log.info("🎭 Película: '{}' | Fecha lanzamiento: {} | Días desde estreno: {} | Estado actual: {} | Nuevo estado: {}", 
                    movie.getTitle(), movie.getReleaseDate(), daysSinceRelease, currentStatus, newStatus);

                if (!newStatus.equals(currentStatus)) {
                    movie.setStatus(newStatus);
                    Movie savedMovie = movieRepository.save(movie);
                    updatedCount++;
                    
                    log.info("✅ Película '{}' (ID: {}) actualizada de {} a {} - Estado guardado: {}", 
                        movie.getTitle(), movie.getId(), currentStatus, newStatus, savedMovie.getStatus());
                } else {
                    log.info("➡️ Película '{}' ya tiene el estado correcto: {}", 
                        movie.getTitle(), currentStatus);
                }
            } else {
                log.warn("⚠️ Película '{}' (ID: {}) no tiene fecha de lanzamiento configurada", 
                    movie.getTitle(), movie.getId());
            }
        }
        
        log.info("✅ Actualización completada: {} películas actualizadas de {} total", 
            updatedCount, movies.size());
    }
    private MovieResponse toMovieResponse(Movie movie) {
        MovieResponse dto = new MovieResponse();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDescriptionShowtimes(movie.getDescriptionShowtimes());
        dto.setDescriptionMovie(movie.getDescriptionMovie());
        dto.setRating(movie.getRating());
        dto.setGenreName(movie.getGenre() != null ? movie.getGenre().getName() : null);
        dto.setGenreId(movie.getGenre() != null ? movie.getGenre().getId() : null);
        dto.setImageUrl(movie.getPosterUrl());
        dto.setDuration(movie.getDuration());
        dto.setTrailerUrl(movie.getTrailerUrl());
        dto.setDirector(movie.getDirector());
        dto.setCast(movie.getCast());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setStatus(movie.getStatus());
        dto.setCreatedAt(movie.getCreatedAt());
        dto.setUpdatedAt(movie.getUpdatedAt());
        return dto;
    }

    private Movie toMovieEntity(MovieRequest dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setDescriptionShowtimes(dto.getDescriptionShowtimes());
        movie.setDescriptionMovie(dto.getDescriptionMovie());
        movie.setRating(dto.getRating());
        // Debes buscar el Genre por id o nombre según lo que recibas en MovieRequest
        movie.setGenre(genreRepository.findById(dto.getGenreId()).orElse(null));
        movie.setPosterUrl(dto.getImageUrl());
        movie.setDuration(dto.getDuration());
        movie.setTrailerUrl(dto.getTrailerUrl());
        movie.setDirector(dto.getDirector());
        movie.setCast(dto.getCast());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setStatus(dto.getStatus() != null ? dto.getStatus() : com.cinerama.backend.enums.MovieStatus.NOW_PLAYING);
        return movie;
    }
}