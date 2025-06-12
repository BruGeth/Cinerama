package com.cinerama.backend.service;

import com.cinerama.backend.entity.Seat;

import java.util.List;

public interface SeatService {
    List<Seat> getAvailableSeats(Long showId);
    List<Seat> reserveSeats(Long showId, List<Long> seatIds);
}
