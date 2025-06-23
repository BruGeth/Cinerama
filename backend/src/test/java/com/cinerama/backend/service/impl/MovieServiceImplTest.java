package com.cinerama.backend.service.impl;

import com.cinerama.backend.entity.Movie;
import com.cinerama.backend.repository.MovieRepository;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MovieServiceImplTest {
    @Test
    void getAllMovies_returnsAllMovies() {
        MovieRepository mockRepo = mock(MovieRepository.class);
        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        when(mockRepo.findAll()).thenReturn(Arrays.asList(movie1, movie2));

        MovieServiceImpl service = new MovieServiceImpl(mockRepo);

        List<Movie> result = service.getAllMovies();

        assertEquals(2, result.size());
        verify(mockRepo, times(1)).findAll();
    }
}