package com.cinerama.backend.controller;

import com.cinerama.backend.dto.LoginRequest;
import com.cinerama.backend.dto.LoginResponse;
import com.cinerama.backend.dto.RegisterRequest;
import com.cinerama.backend.dto.VerificationRequest;
import com.cinerama.backend.entity.User;
import com.cinerama.backend.service.AuthService;
import com.cinerama.backend.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling user authentication operations.
 *
 * <p>This controller manages the complete user authentication flow for the Cinerama
 * cinema booking system, including registration, email verification, and login.
 * All endpoints are publicly accessible as configured in WebSecurityConfig.</p>
 *
 * <h2>Authentication Flow:</h2>
 * <ol>
 *   <li>User registers with email and password</li>
 *   <li>System sends verification email</li>
 *   <li>User enters their verification code</li>
 *   <li>User can now log in and receive JWT token</li>
 * </ol>
 *
 * <h2>Security Notes:</h2>
 * <ul>
 *   <li>All passwords are securely hashed before storage</li>
 *   <li>JWT tokens include user role information</li>
 *   <li>Verification tokens expire after 24 hours</li>
 * </ul>
 *
 * @author Cinerama Development Team
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JwtUtil jwtUtil;

    /**
     * Registers a new user in the system.
     *
     * <p>Creates a new user account and sends a verification email. The user account
     * will be in a "pending verification" state until the email is verified.</p>
     *
     * <h3>Registration Process:</h3>
     * <ol>
     *   <li>Validates the request data using {@code @Valid} annotation</li>
     *   <li>Checks if the email is already registered</li>
     *   <li>Hashes the password using BCrypt</li>
     *   <li>Creates the user with PENDING_VERIFICATION status</li>
     *   <li>Generates a verification token</li>
     *   <li>Sends verification email asynchronously</li>
     * </ol>
     *
     * @param request the registration details including email, password, and user info
     * @return ResponseEntity containing the created user (without sensitive data)
     *
     *
     * <h3>Security:</h3>
     * <p>This endpoint is publicly accessible and doesn't require authentication.</p>
     *
     * <h3>Implementation Details:</h3>
     * <p>User passwords are hashed before storage, and verification emails are sent asynchronously.</p>
     *
     * @todo Add proper exception handling and @throws documentation:
     * <ul>
     *   <li>ValidationException if the request data is invalid (email format, password strength, etc.)</li>
     *   <li>UserAlreadyExistsException if email is already registered in the system</li>
     *   <li>EmailServiceException if there's an error sending the verification email</li>
     * </ul>
     *
     * @see RegisterRequest for request structure and validation rules
     * @see User for returned user entity structure
     * @see AuthService#register(RegisterRequest) for the underlying business logic
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        User registeredUser = authService.register(request);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Verifies a user's email address using a verification token.
     *
     * <p>This endpoint completes the registration process by validating the token
     * sent to the user's email. Once verified, the user can log in to the system.</p>
     *
     * <h3>Verification Process:</h3>
     * <ol>
     *   <li>Validates the token format and structure</li>
     *   <li>Checks if the token exists and hasn't expired</li>
     *   <li>Updates user attribute {@code enabled} from false to true</li>
     *   <li>Removes the verification token</li>
     * </ol>
     *
     * @param request containing the verification token from the email
     * @return ResponseEntity with success message
     *
     *
     * <h3>Security:</h3>
     * <p>This endpoint is publicly accessible and should be called from email verification page in frontend.</p>
     *
     * <h3>Implementation Details:</h3>
     * <p>Verification tokens are single-use only.</p>
     *
     * @todo Add proper exception handling and @throws documentation:
     * <ul>
     *   <li>InvalidTokenException if the token is expired, invalid, or already used</li>
     *   <li>UserNotFoundException if the user associated with the token doesn't exist</li>
     *   <li>TokenExpiredException if the verification token has exceeded the 24-hour limit</li>
     *   <li>VerificationAlreadyCompletedException if the user has already verified their account</li>
     * </ul>
     *
     * <li>Verification tokens should expire after 24 hours</li>
     *
     * @see VerificationRequest for request structure
     * @see AuthService#verify(VerificationRequest) for the underlying verification logic
     */
    @PostMapping("/verify")
    public ResponseEntity<String> verify(@Valid @RequestBody VerificationRequest request) {
        authService.verify(request);
        return ResponseEntity.ok("Account verified successfully!");
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * <p>Validates user credentials and returns a JWT token that can be used
     * for accessing protected endpoints in the cinema booking system.</p>
     *
     * <h3>Authentication Process:</h3>
     * <ol>
     *   <li>Validates email format and password requirements</li>
     *   <li>Verifies user exists and account is verified</li>
     *   <li>Compares provided password with stored hash</li>
     *   <li>Generates JWT token with user information and roles</li>
     *   <li>Returns token along with user details</li>
     * </ol>
     *
     * <h3>JWT Token Contains:</h3>
     * <ul>
     *   <li>User ID and email</li>
     *   <li>Token expiration time</li>
     *   <li>Issuer and subject claims</li>
     * </ul>
     *
     * @param request containing email and password
     * @return ResponseEntity with JWT token and user information
     *
     *
     * <h3>Security:</h3>
     * <p>This endpoint is publicly accessible but requires valid user credentials.</p>
     *
     * <h3>Implementation Details:</h3>
     * <p>JWT tokens have a configurable expiration time and include user role information.</p>
     *
     * @todo Add proper exception handling and @throws documentation:
     * <ul>
     *   <li>BadCredentialsException if email/password combination is invalid</li>
     *   <li>AccountNotVerifiedException if user hasn't verified their email address</li>
     *   <li>UserNotFoundException if user doesn't exist in the system</li>
     *   <li>AccountLockedException if user account has been temporarily locked due to failed attempts</li>
     * </ul>
     * <li>Add role information (USER, ADMIN, etc.)</li>
     *
     * @see LoginRequest for request structure and validation rules
     * @see LoginResponse for response structure including JWT token
     * @see AuthService#login(LoginRequest) for the underlying authentication logic
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(
            @RequestHeader("Authorization") String authHeader) {

        //  Verifica formato del encabezado
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build(); // o lanzar una excepción personalizada
        }

        //  Extrae el token de la cabecera
        String token = authHeader.substring(7);

        //  Valida que el token aún sea válido
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body(null);
        }

        //  Extrae email y rol del token existente
        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        //  Genera nuevo token con la misma info pero tiempo renovado
        String newToken = jwtUtil.generateToken(email, role);

        //  Empaqueta respuesta como en el login
        LoginResponse response = new LoginResponse(newToken, email);

        return ResponseEntity.ok(response);
    }
}