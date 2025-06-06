package com.cinerama.backend.controller;

import com.cinerama.backend.dto.TicketPurchaseRequest;
import com.cinerama.backend.dto.TicketPurchaseResponse;
import com.cinerama.backend.service.mail.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor

public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketPurchaseResponse> purchaseTicket(@RequestBody TicketPurchaseRequest request) {
        Long userId = 1L; // Simulado por ahora
        return ResponseEntity.ok(ticketService.purchaseTicket(userId, request));
    }
}
