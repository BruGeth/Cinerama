package com.cinerama.backend.controller;

import com.cinerama.backend.entity.Seat;
import com.cinerama.backend.service.impl.SeatServiceImpl;
import lombok.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatServiceImpl seatServiceImpl;

    @GetMapping("/available")
    public ResponseEntity<List<Map<String, Object>>> getAvailableSeats(@RequestParam Long showId) {
        List<Seat> seats = seatServiceImpl.getAvailableSeats(showId);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Seat seat : seats) {
            Map<String, Object> seatData = new HashMap<>();
            seatData.put("id", seat.getId());
            seatData.put("seatNumber", seat.getSeatNumber());
            seatData.put("type", seat.getType());
            seatData.put("price", seat.getPrice());
            seatData.put("available", seat.isAvailable());
            response.add(seatData);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserveSeats(@RequestBody ReserveRequest request) {
        try {
            List<Seat> reservedSeats = seatServiceImpl.reserveSeats(request.getShowId(), request.getSeatIds());
            List<Map<String, Object>> reservedInfo = new ArrayList<>();

            for (Seat seat : reservedSeats) {
                Map<String, Object> s = new HashMap<>();
                s.put("seatNumber", seat.getSeatNumber());
                s.put("price", seat.getPrice());
                s.put("type", seat.getType());
                reservedInfo.add(s);
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Seats successfully reserved",
                    "reservedSeats", reservedInfo
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Data
    public static class ReserveRequest {
        private Long showId;
        private List<Long> seatIds;
    }
}
