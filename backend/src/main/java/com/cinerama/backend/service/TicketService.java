package com.cinerama.backend.service;

import com.cinerama.backend.dto.TicketPurchaseRequest;
import com.cinerama.backend.dto.TicketPurchaseResponse;

public interface TicketService {
    TicketPurchaseResponse purchaseTicket(Long userId, TicketPurchaseRequest request);
}
