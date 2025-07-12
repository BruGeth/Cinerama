package com.cinerama.backend.service.impl;

import com.cinerama.backend.entity.Genre;
import com.cinerama.backend.exception.repository.GenreRepository;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GenreServiceImplTest {

    @Test
    void getAllGenres_returnsAllGenres() {
        GenreRepository mockRepo = mock(GenreRepository.class);
        Genre genre1 = new Genre();
        Genre genre2 = new Genre();
        when(mockRepo.findAll()).thenReturn(Arrays.asList(genre1, genre2));

        GenreServiceImpl service = new GenreServiceImpl(mockRepo);

        List<Genre> result = service.getAllGenres();

        assertEquals(2, result.size());
        verify(mockRepo, times(1)).findAll();
    }
}
