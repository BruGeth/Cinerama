package com.cinerama.backend.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Cinerama cinema booking system.
 *
 * <p>This class provides centralized exception handling across all REST controllers
 * in the application. It intercepts various types of exceptions and converts them
 * into appropriate HTTP responses with consistent error message formatting.</p>
 *
 * <h2>Exception Handling Strategy:</h2>
 * <ul>
 *   <li>Converts Java exceptions to HTTP status codes</li>
 *   <li>Provides consistent error message structure</li>
 *   <li>Prevents sensitive internal information exposure</li>
 *   <li>Enables client-friendly error responses</li>
 * </ul>
 *
 * <h2>Supported Exception Types:</h2>
 * <ul>
 *   <li>IllegalArgumentException - Bad Request (400)</li>
 *   <li>IllegalStateException - Conflict (409)</li>
 *   <li>MethodArgumentNotValidException - Validation errors (400)</li>
 *   <li>ConstraintViolationException - Constraint violations (400)</li>
 *   <li>Generic Exception - Internal Server Error (500)</li>
 * </ul>
 *
 * <h2>Response Format:</h2>
 * <p>All error responses follow a consistent JSON structure:</p>
 * <ul>
 *   <li>Single errors: {"error": "message"}</li>
 *   <li>Validation errors: {"field1": "message1", "field2": "message2"}</li>
 * </ul>
 *
 * @author Cinerama Development Team
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles IllegalArgumentException for invalid input parameters.
     *
     * <p>This handler catches IllegalArgumentException instances typically thrown
     * when service methods receive invalid arguments or parameters. It maps these
     * exceptions to HTTP 400 Bad Request responses.</p>
     *
     * <h3>Common Scenarios:</h3>
     * <ul>
     *   <li>Invalid email format in business logic</li>
     *   <li>Incorrect parameter values in service methods</li>
     *   <li>Business rule violations related to input data</li>
     * </ul>
     *
     * <h3>Response Structure:</h3>
     * <ul>
     *   <li>HTTP Status: 400 Bad Request</li>
     *   <li>Body: {"error": "exception message"}</li>
     * </ul>
     *
     * @param ex the IllegalArgumentException that was thrown
     * @return ResponseEntity containing error message with 400 status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles IllegalStateException for invalid application state.
     *
     * <p>This handler catches IllegalStateException instances typically thrown
     * when operations are performed in an invalid application state. It maps
     * these exceptions to HTTP 409 Conflict responses.</p>
     *
     * <h3>Common Scenarios:</h3>
     * <ul>
     *   <li>Attempting to verify already verified accounts</li>
     *   <li>Duplicate registration attempts</li>
     *   <li>Operations on disabled accounts</li>
     *   <li>Conflicting business state conditions</li>
     * </ul>
     *
     * <h3>Response Structure:</h3>
     * <ul>
     *   <li>HTTP Status: 409 Conflict</li>
     *   <li>Body: {"error": "exception message"}</li>
     * </ul>
     *
     * @param ex the IllegalStateException that was thrown
     * @return ResponseEntity containing error message with 409 status
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Handles MethodArgumentNotValidException for bean validation failures.
     *
     * <p>This handler catches validation exceptions thrown when request DTOs
     * fail Jakarta Bean Validation constraints. It extracts field-specific
     * validation errors and returns them in a structured format.</p>
     *
     * <h3>Validation Sources:</h3>
     * <ul>
     *   <li>@NotBlank, @Email, @Valid annotations on DTO fields</li>
     *   <li>Request body validation in controller methods</li>
     *   <li>Custom validation annotations</li>
     * </ul>
     *
     * <h3>Response Structure:</h3>
     * <ul>
     *   <li>HTTP Status: 400 Bad Request</li>
     *   <li>Body: {"field1": "error message", "field2": "error message"}</li>
     * </ul>
     *
     * <p><strong>Example Response:</strong></p>
     * <pre>
     * {
     *   "email": "Email must be valid",
     *   "password": "Password is required"
     * }
     * </pre>
     *
     * @param ex the MethodArgumentNotValidException containing validation errors
     * @return ResponseEntity containing field-specific error messages with 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles ConstraintViolationException for JPA/database constraint violations.
     *
     * <p>This handler catches constraint violations that occur at the database
     * or JPA level, such as unique constraint violations, null constraint
     * violations, or other database-level validation failures.</p>
     *
     * <h3>Common Scenarios:</h3>
     * <ul>
     *   <li>Unique email constraint violations during registration</li>
     *   <li>Database-level validation failures</li>
     *   <li>JPA entity constraint violations</li>
     *   <li>Manual validation API usage</li>
     * </ul>
     *
     * <h3>Response Structure:</h3>
     * <ul>
     *   <li>HTTP Status: 400 Bad Request</li>
     *   <li>Body: {"error": "constraint violation message"}</li>
     * </ul>
     *
     * @param ex the ConstraintViolationException that was thrown
     * @return ResponseEntity containing error message with 400 status
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other unhandled exceptions as internal server errors.
     *
     * <p>This is the fallback exception handler that catches any exception
     * not specifically handled by other methods. It prevents internal system
     * details from being exposed to clients while providing a generic error response.</p>
     *
     * <h3>Security Features:</h3>
     * <ul>
     *   <li>Hides internal system details from clients</li>
     *   <li>Provides generic error message</li>
     *   <li>Logs actual exception for debugging (if logging configured)</li>
     *   <li>Prevents information disclosure</li>
     * </ul>
     *
     * <h3>Response Structure:</h3>
     * <ul>
     *   <li>HTTP Status: 500 Internal Server Error</li>
     *   <li>Body: {"error": "An unexpected error occurred."}</li>
     * </ul>
     *
     * <p><strong>Implementation Note:</strong> Consider adding logging
     * functionality to capture actual exception details for debugging
     * while keeping client responses generic.</p>
     *
     * @param ex the generic Exception that was thrown
     * @return ResponseEntity containing generic error message with 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "An unexpected error occurred.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ AccessDeniedException.class, AuthorizationDeniedException.class })
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Access denied");
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
