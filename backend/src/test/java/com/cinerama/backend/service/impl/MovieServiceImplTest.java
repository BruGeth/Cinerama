package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.MovieResponse;
import com.cinerama.backend.entity.Movie;
import com.cinerama.backend.exception.repository.MovieRepository;
import com.cinerama.backend.exception.repository.GenreRepository;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MovieServiceImplTest {
    @Test
    void getAllMovies_returnsAllMovies() {
        MovieRepository mockRepo = mock(MovieRepository.class);
        GenreRepository mockGenreRepo = mock(GenreRepository.class);
        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        when(mockRepo.findAll()).thenReturn(Arrays.asList(movie1, movie2));

        MovieServiceImpl service = new MovieServiceImpl(mockRepo, mockGenreRepo);

        List<MovieResponse> result = service.getAllMovies();

        assertEquals(2, result.size());
        verify(mockRepo, times(1)).findAll();
    }
}