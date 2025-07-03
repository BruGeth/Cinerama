package com.cinerama.backend.controller;
import com.cinerama.backend.dto.FestaRamaRequest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.cinerama.backend.service.FestaRamaService;

import java.util.Map;

/**
 * REST controller for managing FestaRama reservations.
 *
 * <p>This controller handles the reservation requests for FestaRama events in the Cinerama system.
 * It ensures that the request data is processed and a confirmation email is sent to the
 * appropriate recipients. All endpoints are publicly accessible but should be secured
 * in production environments.</p>
 *
 * <h2>FestaRama Reservation Flow:</h2>
 * <ol>
 *   <li>Client sends a request to reserve a FestaRama event</li>
 *   <li>System processes the reservation details</li>
 *   <li>A confirmation email is sent asynchronously</li>
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
 * <h2>Author:</h2>
 * <p>Cinerama Development Team</p>
 */
@RestController
@RequestMapping("/api/festarama")
@RequiredArgsConstructor
public class FestaRamaController {

    /* === Service to handle FestaRama business logic === */
    private final FestaRamaService service;

    /**
     * Reserves a FestaRama event and sends a confirmation email.
     *
     * <p>This endpoint allows the reservation of FestaRama events in the system. Once the
     * reservation is successfully processed, a confirmation email is sent to the relevant recipients.</p>
     *
     * <h3>Reservation Process:</h3>
     * <ol>
     *   <li>Processes the reservation request details through the service layer</li>
     *   <li>Sends a confirmation email asynchronously</li>
     *   <li>Returns a success response if the operation is completed</li>
     * </ol>
     *
     * <h3>Response Details:</h3>
     * <ul>
     *   <li>On success: HTTP 200 (OK) with a success message</li>
     *   <li>On failure: HTTP 500 (Internal Server Error) with an error message</li>
     * </ul>
     *
     * @param request containing the reservation details such as event name, date, and contact info
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
     *   <li>ReservationProcessingException if the reservation cannot be processed</li>
     * </ul>
     *
     * @see FestaRamaRequest for request structure and validation rules
     * @see FestaRamaService#procesarReserva(FestaRamaRequest) for the underlying business logic
     */

    /* === Endpoint to reserve a FestaRama and send confirmation email === */
    @PostMapping
        public ResponseEntity<Map<String, String>> reservarFesta(@RequestBody FestaRamaRequest request) {
            try {
                service.procesarReserva(request);
                return ResponseEntity.ok(Map.of(
                        "message", "🎉 ¡Solicitud enviada exitosamente!"
                ));
            } catch (MessagingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "message", "❌ Error al enviar correo: " + e.getMessage()
                ));
            }
        }
    }
