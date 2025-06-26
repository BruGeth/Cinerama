package com.cinerama.backend.service;

import com.cinerama.backend.dto.FestaRamaRequest;
import jakarta.mail.MessagingException;

public interface FestaRamaService {
    /* Process a reservation request and send notification email */
    void procesarReserva(FestaRamaRequest request) throws MessagingException;
}
