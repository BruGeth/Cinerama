package com.cinerama.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents the response for an event request in the Cinerama system.
 *
 * <p>This class provides the structure for the response returned after processing
 * an event request. It includes the status of the operation and an optional message
 * for additional context.</p>
 *
 * <h2>Fields:</h2>
 * <ul>
 *   <li><b>status:</b> Status of the operation ("OK" or "ERROR")</li>
 *   <li><b>message:</b> Optional message providing additional details about the operation</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This DTO is used in the `EventController` to return the result of an event request.</p>
 *
 * @see com.cinerama.backend.controller.EventController
 */

@AllArgsConstructor
@Getter
public class EventResponse {
    private String status;   // "OK" | "ERROR"
    private String message;  // Optional message for additional context
}
