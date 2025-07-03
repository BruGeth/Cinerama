package com.cinerama.backend.controller;

import com.cinerama.backend.dto.AdvertisingRequest;
import com.cinerama.backend.service.AdvertisingService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * REST controller for managing advertising requests.
 *
 * <p>This controller handles the submission of advertising requests in the Cinerama system.
 * It ensures that the request data is processed and a notification email is sent to the
 * appropriate recipients. All endpoints are publicly accessible but should be secured
 * in production environments.</p>
 *
 * <h2>Advertising Request Flow:</h2>
 * <ol>
 *   <li>Client sends a request to submit an advertising proposal</li>
 *   <li>System processes the request details</li>
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
 * <h2>Author:</h2>
 * <p>Cinerama Development Team</p>
 */
@RestController
@RequestMapping("/api/advertising")
@RequiredArgsConstructor

public class AdvertisingController {

    /* === Injected service to process event logic === */
    private final AdvertisingService service;

    /**
     * Submits a new advertising request and sends a notification email.
     *
     * <p>This endpoint allows the submission of advertising proposals to the system.
     * Once the request is successfully processed, a notification email is sent to the
     * relevant recipients.</p>
     *
     * <h3>Advertising Request Process:</h3>
     * <ol>
     *   <li>Processes the advertising request details through the service layer</li>
     *   <li>Sends a notification email asynchronously</li>
     *   <li>Returns a success response if the operation is completed</li>
     * </ol>
     *
     * <h3>Response Details:</h3>
     * <ul>
     *   <li>On success: HTTP 200 (OK) with a success message</li>
     *   <li>On failure: HTTP 500 (Internal Server Error) with an error message</li>
     * </ul>
     *
     * @param request containing the advertising details such as company name, proposal, and contact info
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
     *   <li>AdvertisingProcessingException if the request cannot be processed</li>
     * </ul>
     *
     * @see AdvertisingRequest for request structure and validation rules
     * @see AdvertisingService#processAdvertising(AdvertisingRequest) for the underlying business logic
     */

    /* === Endpoint to submit advertising requests === */
    @PostMapping
    public ResponseEntity<Map<String, String>> submitAdvertising(@RequestBody AdvertisingRequest request) {
        try {

            /* === Process the advertising request === */
            service.processAdvertising(request);

            /* === Return success response === */
            return ResponseEntity.ok(Map.of("message", "🎉 ¡Solicitud enviada exitosamente!"));
        } catch (MessagingException e) {

            /* === Handle email sending errors === */
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "❌ Error al enviar correo: " + e.getMessage()));
        }
    }
}
