package com.cinerama.backend.controller;

import com.cinerama.backend.dto.*;
import com.cinerama.backend.service.EventService;
import com.cinerama.backend.dto.EventRequest;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing event operations.
 *
 * <p>This controller handles the creation of events in the Cinerama system. It ensures
 * that event data is validated, processed, and a notification email is sent to the
 * appropriate recipients. All endpoints are publicly accessible but should be secured
 * in production environments.</p>
 *
 * <h2>Event Management Flow:</h2>
 * <ol>
 *   <li>Client sends a request to create a new event</li>
 *   <li>System validates the event details</li>
 *   <li>Event is processed and stored in the system</li>
 *   <li>A notification email is sent asynchronously</li>
 *   <li>Response is returned to the client</li>
 * </ol>
 *
 * <h2>Security Notes:</h2>
 * <ul>
 *   <li>Validation is applied to ensure data integrity</li>
 *   <li>Email notifications are sent asynchronously to avoid blocking</li>
 *   <li>Endpoints should be protected to prevent unauthorized access</li>
 * </ul>
 *
 * <h2>Implementation Details:</h2>
 * <p>The email sending process is handled asynchronously to improve performance. Exceptions
 * are caught and appropriate error responses are returned to the client.</p>
 *
 * @author Cinerama Development Team
 */
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    /* === Injected service to process event logic === */
    private final EventService service;  // ← Interface injection
    /**
     * Creates a new event and sends a notification email.
     *
     * <p>This endpoint allows the creation of a new event in the system. Once the event
     * is successfully processed, a notification email is sent to the relevant recipients.</p>
     *
     * <h3>Event Creation Process:</h3>
     * <ol>
     *   <li>Validates the event request data using {@code @Valid} annotation</li>
     *   <li>Processes the event details through the service layer</li>
     *   <li>Sends a notification email asynchronously</li>
     *   <li>Returns a success response if the operation is completed</li>
     * </ol>
     *
     * <h3>Response Details:</h3>
     * <ul>
     *   <li>On success: HTTP 201 (Created) with a success message</li>
     *   <li>On failure: HTTP 500 (Internal Server Error) with an error message</li>
     * </ul>
     *
     * @param request containing the event details such as name, date, and description
     * @return ResponseEntity with the status and a message indicating the result of the operation
     *
     * <h3>Security:</h3>
     * <p>This endpoint is publicly accessible but should be protected in production to prevent abuse.</p>
     *
     * <h3>Implementation Details:</h3>
     * <p>The email sending process is handled asynchronously to avoid blocking the request.</p>
     *
     * @todo Add proper exception handling and @throws documentation:
     * <ul>
     *   <li>ValidationException if the request data is invalid</li>
     *   <li>MessagingException if there is an error sending the email</li>
     *   <li>EventProcessingException if the event cannot be processed</li>
     * </ul>
     *
     * @see EventRequest for request structure and validation rules
     * @see EventResponse for response structure including status and message
     * @see EventService#processEvent(EventRequest) for the underlying business logic
     */
    /* === Endpoint to create a new event and send notification email === */
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody EventRequest request) {

        try {
            /* === Process the event and send email === */
            service.processEvent(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new EventResponse("OK", "🎉 ¡Solicitud enviada exitosamente!"));
        } catch (MessagingException ex) {
            /* === Handle email sending errors === */
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResponse("ERROR", ex.getMessage()));
        }
    }
}
