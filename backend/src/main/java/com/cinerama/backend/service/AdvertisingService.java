package com.cinerama.backend.service;

import com.cinerama.backend.dto.AdvertisingRequest;
import jakarta.mail.MessagingException;

/**
 * Service interface for managing Advertising requests.
 *
 * <p>This service defines the contract for processing Advertising requests,
 * including saving advertising details to the database and sending summary emails
 * to the contact person.</p>
 *
 * <h2>Methods:</h2>
 * <ul>
 *   <li><b>processAdvertising:</b> Processes an Advertising request and sends a summary email</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This service is used in the application layer to handle Advertising-related operations.</p>
 *
 * @see com.cinerama.backend.dto.AdvertisingRequest
 */

public interface AdvertisingService {

    /* === Method to process an advertising request === */
    void processAdvertising(AdvertisingRequest req) throws MessagingException;
}
