package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.*;
import com.cinerama.backend.entity.Showtime;
import com.cinerama.backend.repository.CinemaRepository;
import com.cinerama.backend.repository.RoomRepository;
import com.cinerama.backend.repository.ShowtimeRepository;
import com.cinerama.backend.service.ShowtimeService;
import com.cinerama.backend.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the ShowtimeService interface.
 * 
 * <p>This class provides the business logic for managing
 * Showtime entities and their operations.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShowtimeServiceImpl implements ShowtimeService {
    
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<ShowtimeResponse> getAllShowtimes() {
        log.debug("Retrieving all showtimes");
        return showtimeRepository.findAll()
                .stream()
                .map(this::toShowtimeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ShowtimeResponse> getShowtimeById(Long id) {
        log.debug("Retrieving showtime with ID: {}", id);
        return showtimeRepository.findById(id)
                .map(this::toShowtimeResponse);
    }

    @Override
    public List<ShowtimeResponse> getShowtimesByMovieId(Long movieId) {
        log.debug("Retrieving showtimes for movie ID: {}", movieId);
        return showtimeRepository.findByMovieId(movieId)
                .stream()
                .map(this::toShowtimeResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ShowtimeResponse> getShowtimesByCinemaId(Long cinemaId) {
        log.debug("Retrieving showtimes for cinema ID: {}", cinemaId);
        return showtimeRepository.findByCinemaId(cinemaId)
                .stream()
                .map(this::toShowtimeResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ShowtimeResponse> getShowtimesByDate(LocalDate date) {
        log.debug("Retrieving showtimes for date: {}", date);
        return showtimeRepository.findByShowDate(date)
                .stream()
                .map(this::toShowtimeResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ShowtimeResponse saveShowtime(ShowtimeRequest showtimeRequest) {
        log.debug("Creating new showtime for movie ID: {}", showtimeRequest.getMovieId());
        Showtime showtime = toShowtimeEntity(showtimeRequest);
        Showtime saved = showtimeRepository.save(showtime);
        log.info("Showtime created successfully with ID: {}", saved.getId());
        return toShowtimeResponse(saved);
    }
    
    @Override
    @Transactional
    public ShowtimeResponse updateShowtime(Long id, ShowtimeRequest showtimeRequest) {
        log.debug("Updating showtime with ID: {}", id);
        
        Showtime existingShowtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Showtime not found with ID: " + id));
        
        // Update fields
        existingShowtime.setMovie(movieRepository.findById(showtimeRequest.getMovieId()).orElse(null));
        existingShowtime.setCinema(cinemaRepository.findById(showtimeRequest.getCinemaId()).orElse(null));
        existingShowtime.setRoom(roomRepository.findById(showtimeRequest.getRoomId()).orElse(null));
        existingShowtime.setShowDate(showtimeRequest.getShowDate());
        existingShowtime.setShowTime(showtimeRequest.getShowTime());
        existingShowtime.setFormat(showtimeRequest.getFormat());
        existingShowtime.setLanguage(showtimeRequest.getLanguage());
        existingShowtime.setStatus(showtimeRequest.getStatus());
        
        Showtime updated = showtimeRepository.save(existingShowtime);
        log.info("Showtime updated successfully with ID: {}", updated.getId());
        return toShowtimeResponse(updated);
    }

    @Override
    @Transactional
    public void deleteShowtime(Long id) {
        log.debug("Deleting showtime with ID: {}", id);
        if (!showtimeRepository.existsById(id)) {
            throw new RuntimeException("Showtime not found with ID: " + id);
        }
        showtimeRepository.deleteById(id);
        log.info("Showtime deleted successfully with ID: {}", id);
    }

    /**
     * Converts a Showtime entity to a ShowtimeResponse DTO.
     * 
     * @param showtime The showtime entity
     * @return The showtime response DTO
     */
    private ShowtimeResponse toShowtimeResponse(Showtime showtime) {
        MovieSummaryResponse movieSummary = showtime.getMovie() != null ? 
                MovieSummaryResponse.builder()
                        .id(showtime.getMovie().getId())
                        .title(showtime.getMovie().getTitle())
                        .duration(showtime.getMovie().getDuration())
                        .rating(showtime.getMovie().getRating())
                        .posterUrl(showtime.getMovie().getPosterUrl())
                        .status(showtime.getMovie().getStatus())
                        .build() : null;
        
        CinemaSummaryResponse cinemaSummary = showtime.getCinema() != null ?
                CinemaSummaryResponse.builder()
                        .id(showtime.getCinema().getId())
                        .name(showtime.getCinema().getName())
                        .city(showtime.getCinema().getCity())
                        .status(showtime.getCinema().getStatus())
                        .build() : null;
        
        RoomSummaryResponse roomSummary = showtime.getRoom() != null ?
                RoomSummaryResponse.builder()
                        .id(showtime.getRoom().getId())
                        .name(showtime.getRoom().getName())
                        .capacity(showtime.getRoom().getCapacity())
                        .type(showtime.getRoom().getType())
                        .status(showtime.getRoom().getStatus())
                        .build() : null;
        
        List<TicketPriceResponse> ticketPrices = showtime.getTicketPrices() != null ?
                showtime.getTicketPrices().stream()
                        .map(tp -> TicketPriceResponse.builder()
                                .id(tp.getId())
                                .showtimeId(tp.getShowtime().getId())
                                .type(tp.getType())
                                .format(tp.getFormat())
                                .price(tp.getPrice())
                                .createdAt(tp.getCreatedAt())
                                .updatedAt(tp.getUpdatedAt())
                                .build())
                        .collect(Collectors.toList()) : List.of();
        
        return ShowtimeResponse.builder()
                .id(showtime.getId())
                .movie(movieSummary)
                .cinema(cinemaSummary)
                .room(roomSummary)
                .showDate(showtime.getShowDate())
                .showTime(showtime.getShowTime())
                .format(showtime.getFormat())
                .language(showtime.getLanguage())
                .availableSeats(showtime.getAvailableSeats())
                .status(showtime.getStatus())
                .ticketPrices(ticketPrices)
                .createdAt(showtime.getCreatedAt())
                .updatedAt(showtime.getUpdatedAt())
                .build();
    }

    /**
     * Converts a ShowtimeRequest DTO to a Showtime entity.
     * 
     * @param dto The showtime request DTO
     * @return The showtime entity
     */
    private Showtime toShowtimeEntity(ShowtimeRequest dto) {
        return Showtime.builder()
                .movie(movieRepository.findById(dto.getMovieId()).orElse(null))
                .cinema(cinemaRepository.findById(dto.getCinemaId()).orElse(null))
                .room(roomRepository.findById(dto.getRoomId()).orElse(null))
                .showDate(dto.getShowDate())
                .showTime(dto.getShowTime())
                .format(dto.getFormat())
                .language(dto.getLanguage())
                .status(dto.getStatus())
                .build();
    }
}