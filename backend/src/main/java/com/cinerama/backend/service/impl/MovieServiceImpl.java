package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.MovieRequest;
import com.cinerama.backend.dto.MovieResponse;
import com.cinerama.backend.entity.Movie;
import com.cinerama.backend.repository.MovieRepository;
import com.cinerama.backend.repository.GenreRepository;
import com.cinerama.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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