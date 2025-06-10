package com.cinerama.backend.service.mail;

import com.cinerama.backend.dto.TicketPurchaseRequest;
import com.cinerama.backend.dto.TicketPurchaseResponse;

public interface TicketService {
    TicketPurchaseResponse purchaseTicket(Long userId, TicketPurchaseRequest request);
}
