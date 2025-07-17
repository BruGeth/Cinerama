package com.cinerama.backend.controller;

import com.cinerama.backend.entity.Seat;
import com.cinerama.backend.service.SeatService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST Controller for managing Seat operations.
 * 
 * <p>This controller provides endpoints for seat availability
 * and reservation operations.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/available")
    public ResponseEntity<List<Map<String, Object>>> getAvailableSeats(@RequestParam Long showtimeId) {
        List<Seat> seats = seatService.getAvailableSeats(showtimeId);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Seat seat : seats) {
            Map<String, Object> seatData = new HashMap<>();
            seatData.put("id", seat.getId());
            seatData.put("seatNumber", seat.getSeatNumber());
            seatData.put("seatType", seat.getSeatType());
            seatData.put("rowNumber", seat.getRowNumber());
            seatData.put("columnNumber", seat.getColumnNumber());
            seatData.put("available", seat.isAvailable());
            response.add(seatData);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserveSeats(@RequestBody ReserveRequest request) {
        try {
            List<Seat> reservedSeats = seatService.reserveSeats(request.getShowtimeId(), request.getSeatIds());
            List<Map<String, Object>> reservedInfo = new ArrayList<>();

            for (Seat seat : reservedSeats) {
                Map<String, Object> s = new HashMap<>();
                s.put("seatNumber", seat.getSeatNumber());
                s.put("seatType", seat.getSeatType());
                s.put("rowNumber", seat.getRowNumber());
                s.put("columnNumber", seat.getColumnNumber());
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
        private Long showtimeId;
        private List<Long> seatIds;
    }
}
