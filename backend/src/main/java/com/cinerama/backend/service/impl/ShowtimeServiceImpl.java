package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.ShowtimeRequest;
import com.cinerama.backend.entity.Showtime;
import com.cinerama.backend.dto.ShowtimeResponse;
import com.cinerama.backend.repository.ShowtimeRepository;
import com.cinerama.backend.service.ShowtimeService;
import com.cinerama.backend.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;

    @Override
    public List<ShowtimeResponse> getAllShowtimes() {
        return showtimeRepository.findAll()
                .stream()
                .map(this::toShowtimeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ShowtimeResponse> getShowtimeById(Long id) {
        return showtimeRepository.findById(id)
                .map(this::toShowtimeResponse);
    }

    @Override
    public List<ShowtimeResponse> getShowtimesByMovieId(Long movieId) {
        return showtimeRepository.findByMovieId(movieId)
                .stream()
                .map(this::toShowtimeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ShowtimeResponse saveShowtime(ShowtimeRequest showtimeRequest) {
        Showtime showtime = toShowtimeEntity(showtimeRequest);
        Showtime saved = showtimeRepository.save(showtime);
        return toShowtimeResponse(saved);
    }

    @Override
    public void deleteShowtime(Long id) {
        showtimeRepository.deleteById(id);
    }

    private ShowtimeResponse toShowtimeResponse(Showtime showtime) {
        ShowtimeResponse dto = new ShowtimeResponse();
        dto.setId(showtime.getId());
        dto.setMovieTitle(showtime.getMovie() != null ? showtime.getMovie().getTitle() : null);
        dto.setAuditorium(showtime.getAuditorium());
        dto.setStartTime(showtime.getStartTime());
        return dto;
    }

    private Showtime toShowtimeEntity(ShowtimeRequest dto) {
        Showtime showtime = new Showtime();
        if (dto.getMovieId() != null) {
            showtime.setMovie(movieRepository.findById(dto.getMovieId()).orElse(null));
        }
        showtime.setAuditorium(dto.getAuditorium());
        showtime.setStartTime(dto.getStartTime());
        return showtime;
    }
}