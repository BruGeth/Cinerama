package com.cinerama.backend.service;

import com.cinerama.backend.dto.FestaRamaRequest;
import jakarta.mail.MessagingException;

/**
 * Service interface for managing FestaRama reservations.
 *
 * <p>This service defines the contract for processing FestaRama reservations,
 * including saving reservation details to the database and sending summary emails
 * to the contact person.</p>
 *
 * <h2>Methods:</h2>
 * <ul>
 *   <li><b>procesarReserva:</b> Processes a FestaRama reservation and sends a summary email</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This service is used in the application layer to handle FestaRama-related operations.</p>
 *
 * @see com.cinerama.backend.dto.FestaRamaRequest
 */

public interface FestaRamaService {
    /* Process a reservation request and send notification email */
    void procesarReserva(FestaRamaRequest request) throws MessagingException;
}
