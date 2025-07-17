package com.cinerama.backend.service;

import com.cinerama.backend.dto.MovieRequest;
import com.cinerama.backend.dto.MovieResponse;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<MovieResponse> getAllMovies();
    Optional<MovieResponse> getMovieById(Long id);
    MovieResponse saveMovie(MovieRequest movieRequest);
    MovieResponse updateMovie(Long id, MovieRequest movieRequest);
    void deleteMovie(Long id);
    void updateMovieStatuses();
}