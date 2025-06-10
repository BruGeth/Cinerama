package com.cinerama.backend.service;

import com.cinerama.backend.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> getAllMovies();
    Optional<Movie> getMovieById(Long id);
    Movie saveMovie(Movie movie);
    void deleteMovie(Long id);
}