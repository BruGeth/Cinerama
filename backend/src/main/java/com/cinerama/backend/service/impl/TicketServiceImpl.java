package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.TicketPurchaseRequest;
import com.cinerama.backend.dto.TicketPurchaseResponse;
import com.cinerama.backend.entity.Booking;
import com.cinerama.backend.entity.Seat;
import com.cinerama.backend.entity.Show;
import com.cinerama.backend.exception.repository.BookingRepository;
import com.cinerama.backend.exception.repository.SeatRepository;
import com.cinerama.backend.exception.repository.ShowRepository;
import com.cinerama.backend.service.TicketService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public TicketPurchaseResponse purchaseTicket(Long userId, TicketPurchaseRequest request) {
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new IllegalArgumentException("Show not found"));

        List<Seat> availableSeats = seatRepository.findAllByIdInAndAvailableTrue(request.getSeatIds());

        if (availableSeats.size() != request.getSeatIds().size()) {
            throw new IllegalStateException("Some seats are already booked");
        }

        availableSeats.forEach(seat -> seat.setAvailable(false));
        seatRepository.saveAll(availableSeats);

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setShow(show);
        booking.setBookingTime(LocalDateTime.now());
        booking.setConfirmationCode(UUID.randomUUID().toString());
        booking.setSeats(availableSeats);

        bookingRepository.save(booking);

        return TicketPurchaseResponse.builder()
                .confirmationCode(booking.getConfirmationCode())
                .bookingId(booking.getId())
                .showId(show.getId())
                .bookingTime(booking.getBookingTime())
                .seats(availableSeats.stream().map(Seat::getSeatNumber).toList())
                .message("Ticket purchase successful")
                .build();
    }
}
