package com.cinerama.backend.service;

import com.cinerama.backend.entity.Showtime;

import java.util.List;
import java.util.Optional;

public interface ShowtimeService {
    List<Showtime> getAllShowtimes();
    Optional<Showtime> getShowtimeById(Long id);
    Showtime saveShowtime(Showtime showtime);
    void deleteShowtime(Long id);
}