package com.cinerama.backend.service.impl;

import com.cinerama.backend.entity.Seat;
import com.cinerama.backend.repository.SeatRepository;
import com.cinerama.backend.service.SeatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the SeatService interface.
 * 
 * <p>This class provides the business logic for managing
 * Seat entities and their operations.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    
    private final SeatRepository seatRepository;

    @Override
    public List<Seat> getAvailableSeats(Long showtimeId) {
        log.debug("Retrieving available seats for showtime ID: {}", showtimeId);
        return seatRepository.findByShowtimeIdAndAvailableTrue(showtimeId);
    }

    @Override
    @Transactional
    public List<Seat> reserveSeats(Long showtimeId, List<Long> seatIds) {
        log.debug("Reserving seats for showtime ID: {} with seat IDs: {}", showtimeId, seatIds);
        List<Seat> seats = seatRepository.findByIdIn(seatIds);

        for (Seat seat : seats) {
            if (!seat.isAvailable()) {
                throw new RuntimeException("The seat " + seat.getSeatNumber() + " is already reserved");
            }
            if (!seat.getShowtime().getId().equals(showtimeId)) {
                throw new RuntimeException("The seat " + seat.getSeatNumber() + " does not belong to the showtime");
            }
            seat.setAvailable(false);
        }

        List<Seat> reservedSeats = seatRepository.saveAll(seats);
        log.info("Successfully reserved {} seats for showtime ID: {}", reservedSeats.size(), showtimeId);
        return reservedSeats;
    }
    
    @Override
    public List<Seat> getSeatsByShowtime(Long showtimeId) {
        log.debug("Retrieving all seats for showtime ID: {}", showtimeId);
        return seatRepository.findByShowtimeId(showtimeId);
    }
    
    @Override
    public List<Seat> getSeatsByRoom(Long roomId) {
        log.debug("Retrieving all seats for room ID: {}", roomId);
        return seatRepository.findByRoomId(roomId);
    }
    
    @Override
    public Long countAvailableSeats(Long showtimeId) {
        log.debug("Counting available seats for showtime ID: {}", showtimeId);
        return seatRepository.countAvailableSeatsByShowtime(showtimeId);
    }
}
