package com.cinerama.backend.service.impl;

import com.cinerama.backend.entity.Seat;
import com.cinerama.backend.exception.repository.SeatRepository;
import com.cinerama.backend.service.SeatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;

    @Override
    public List<Seat> getAvailableSeats(Long showId) {
        return seatRepository.findByShowIdAndAvailableTrue(showId);
    }

    @Override
    @Transactional
    public List<Seat> reserveSeats(Long showId, List<Long> seatIds) {
        List<Seat> seats = seatRepository.findByIdIn(seatIds);

        for (Seat seat : seats) {
            if (!seat.isAvailable()) {
                throw new RuntimeException("The seat " + seat.getSeatNumber() + " is already reserved");
            }
            if (!seat.getShow().getId().equals(showId)) {
                throw new RuntimeException("The seat " + seat.getSeatNumber() + " does not belong to the show");
            }
            seat.setAvailable(false);
        }

        return seatRepository.saveAll(seats);
    }
}
