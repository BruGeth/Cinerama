package com.cinerama.backend.service;

import com.cinerama.backend.entity.Genre;
import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<Genre> getAllGenres();
    Optional<Genre> getGenreById(Long id);
    Genre saveGenre(Genre genre);
    void deleteGenre(Long id);
}