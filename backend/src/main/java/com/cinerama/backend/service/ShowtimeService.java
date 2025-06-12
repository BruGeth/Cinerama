package com.cinerama.backend.service;

import com.cinerama.backend.dto.ShowtimeRequest;
import com.cinerama.backend.dto.ShowtimeResponse;

import java.util.List;
import java.util.Optional;

public interface ShowtimeService {
    List<ShowtimeResponse> getAllShowtimes();
    Optional<ShowtimeResponse> getShowtimeById(Long id);
    ShowtimeResponse saveShowtime(ShowtimeRequest showtimeRequest);
    void deleteShowtime(Long id);
}