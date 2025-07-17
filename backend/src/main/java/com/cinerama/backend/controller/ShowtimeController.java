package com.cinerama.backend.controller;

import com.cinerama.backend.dto.ShowtimeRequest;
import com.cinerama.backend.dto.ShowtimeResponse;
import com.cinerama.backend.service.ShowtimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
@RequiredArgsConstructor
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @GetMapping
    public List<ShowtimeResponse> getAllShowtimes() {
        return showtimeService.getAllShowtimes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowtimeResponse> getShowtimeById(@PathVariable Long id) {
        return showtimeService.getShowtimeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/movie/{movieId}")
    public List<ShowtimeResponse> getShowtimesByMovieId(@PathVariable Long movieId) {
        return showtimeService.getShowtimesByMovieId(movieId);
    }
    
    @GetMapping("/cinema/{cinemaId}")
    public List<ShowtimeResponse> getShowtimesByCinemaId(@PathVariable Long cinemaId) {
        return showtimeService.getShowtimesByCinemaId(cinemaId);
    }
    
    @GetMapping("/date/{date}")
    public List<ShowtimeResponse> getShowtimesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return showtimeService.getShowtimesByDate(date);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ShowtimeResponse createShowtime(@Valid @RequestBody ShowtimeRequest showtimeRequest) {
        return showtimeService.saveShowtime(showtimeRequest);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ShowtimeResponse updateShowtime(@PathVariable Long id, @Valid @RequestBody ShowtimeRequest showtimeRequest) {
        return showtimeService.updateShowtime(id, showtimeRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.noContent().build();
    }
}