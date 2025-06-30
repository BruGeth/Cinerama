package com.cinerama.backend.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ErrorCode.INVALID_ARGUMENT, ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
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
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ErrorCode.CONFLICT, ex.getMessage()),
                HttpStatus.CONFLICT
        );
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
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        return new ResponseEntity<>(
                new ErrorResponse(ErrorCode.VALIDATION_ERROR, sb.toString()),
                HttpStatus.BAD_REQUEST
        );
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
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ErrorCode.CONSTRAINT_VIOLATION, ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
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
    @ExceptionHandler({ AccessDeniedException.class, AuthorizationDeniedException.class })
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(Exception ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ErrorCode.ACCESS_DENIED, "Access denied"),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ErrorCode.INTERNAL_ERROR, "An unexpected error occurred."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    /**
     * Handles UserNotFoundException for cases where a user is not found.
     *
     * <p>This handler catches UserNotFoundException instances thrown when
     * attempting to access or manipulate a user that does not exist in the system.
     * It maps these exceptions to HTTP 404 Not Found responses.</p>
     *
     * <h3>Common Scenarios:</h3>
     * <ul>
     *   <li>Fetching user details by ID or email</li>
     *   <li>Updating or deleting non-existent users</li>
     * </ul>
     *
     * <h3>Response Structure:</h3>
     * <ul>
     *   <li>HTTP Status: 404 Not Found</li>
     *   <li>Body: {"error": "User not found"}</li>
     * </ul>
     *
     * @param ex the UserNotFoundException that was thrown
     * @return ResponseEntity containing error message with 404 status
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ErrorCode.USER_NOT_FOUND, ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}
