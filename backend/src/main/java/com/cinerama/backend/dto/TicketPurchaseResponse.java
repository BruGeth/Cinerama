package com.cinerama.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TicketPurchaseResponse {
    private String confirmationCode;
    private Long bookingId;
    private Long showtimeId;
    private List<String> seats;
    private LocalDateTime bookingTime;
    private String message;
}
