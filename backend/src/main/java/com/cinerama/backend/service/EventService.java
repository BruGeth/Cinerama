package com.cinerama.backend.service;

import com.cinerama.backend.dto.EventRequest;
import jakarta.mail.MessagingException;

/**
 * Service interface for managing Event requests.
 *
 * <p>This service defines the contract for processing Event requests,
 * including saving event details to the database and sending summary emails
 * to the contact person.</p>
 *
 * <h2>Methods:</h2>
 * <ul>
 *   <li><b>processEvent:</b> Processes an Event request and sends a summary email</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This service is used in the application layer to handle Event-related operations.</p>
 *
 * @see com.cinerama.backend.dto.EventRequest
 */

/* === Method to process event requests === */
public interface EventService {
    void processEvent(EventRequest req) throws MessagingException;
}
