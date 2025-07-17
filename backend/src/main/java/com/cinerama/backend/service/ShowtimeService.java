package com.cinerama.backend.service;

import com.cinerama.backend.dto.ShowtimeRequest;
import com.cinerama.backend.dto.ShowtimeResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShowtimeService {
    List<ShowtimeResponse> getAllShowtimes();
    Optional<ShowtimeResponse> getShowtimeById(Long id);
    List<ShowtimeResponse> getShowtimesByMovieId(Long movieId);
    List<ShowtimeResponse> getShowtimesByCinemaId(Long cinemaId);
    List<ShowtimeResponse> getShowtimesByDate(LocalDate date);
    ShowtimeResponse saveShowtime(ShowtimeRequest showtimeRequest);
    ShowtimeResponse updateShowtime(Long id, ShowtimeRequest showtimeRequest);
    void deleteShowtime(Long id);
}