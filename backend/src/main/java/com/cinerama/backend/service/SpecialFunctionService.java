package com.cinerama.backend.service;

import com.cinerama.backend.dto.SpecialFunctionRequest;
import jakarta.mail.MessagingException;

/**
 * Service interface for managing SpecialFunction requests.
 *
 * <p>This service defines the contract for processing SpecialFunction requests,
 * including saving function details to the database and sending summary emails
 * to the contact person.</p>
 *
 * <h2>Methods:</h2>
 * <ul>
 *   <li><b>processFunction:</b> Processes a SpecialFunction request and sends a summary email</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This service is used in the application layer to handle SpecialFunction-related operations.</p>
 *
 * @see com.cinerama.backend.dto.SpecialFunctionRequest
 */

public interface SpecialFunctionService {

    /* === Method to process a special function request and potentially send emails === */
    void processFunction(SpecialFunctionRequest request) throws MessagingException;
}
