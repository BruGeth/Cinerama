package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.AuthResponse;
import com.cinerama.backend.dto.LoginRequest;
import com.cinerama.backend.dto.RegisterRequest;
import com.cinerama.backend.dto.VerificationRequest;
import com.cinerama.backend.entity.Role;
import com.cinerama.backend.entity.User;
import com.cinerama.backend.exception.user.PasswordsNotMatchException;
import com.cinerama.backend.exception.user.UserAlreadyExistsException;
import com.cinerama.backend.exception.user.UserNotFoundException;
import com.cinerama.backend.repository.RoleRepository;
import com.cinerama.backend.repository.UserRepository;
import com.cinerama.backend.service.AuthService;
import com.cinerama.backend.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Authentication service implementation providing secure user registration and login.
 *
 * <p>Implements email-based account verification workflow with BCrypt password hashing
 * and JWT token generation for stateless authentication.</p>
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final VerificationTokenServiceImpl verificationTokenService;
    private final JwtUtil jwtUtil;

    /**
     * Registers a new user with the provided registration details.
     *
     * @param request the registration request containing user details
     * @return AuthResponse with token and user data
     * @throws PasswordsNotMatchException if the provided passwords do not match
     * @throws RuntimeException if the default role "ROLE_USER" is not found
     */
    @Override
    public AuthResponse register(RegisterRequest request) {
        //  Verificar coincidencia de contraseñas
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordsNotMatchException("Passwords do not match");
        }
        //  Verificar si el correo ya está registrado
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(" El email ya está registrado. Utilice otro email. ");
        }
        //  Asignar rol por defecto
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        //  Construir entidad User
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)  // Enable immediately for simplified flow
                .role(defaultRole)
                .build();

        User savedUser = userRepository.save(user);

        //  Generar y enviar token de verificación (opcional para notificación)
        verificationTokenService.createVerificationToken(savedUser);

        // Generate JWT token immediately
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().getName());
        
        log.info("User registered and token generated for: {}", savedUser.getEmail());

        return buildAuthResponse(token, savedUser);
    }

    /**
     * Verifies a user's account using the provided verification request.
     *
     * @param request the verification request containing the email and verification code
     * @return AuthResponse with token and user data
     * @throws IllegalArgumentException if the verification code is invalid
     */
    @Override
    public AuthResponse verify(VerificationRequest request) {
        log.info("Verifying account for email: {}", request.getEmail());

        // Validate the verification code and activate the user account
        verificationTokenService.verifyAccount(request);

        // Fetch the verified user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getName());

        log.info("Account verified successfully for email: {}", request.getEmail());

        return buildAuthResponse(token, user);
    }

    /**
     * Logs in a user by validating credentials and generating a JWT token.
     *
     * @param request the login request containing email and password
     * @return AuthResponse with token and user data
     * @throws UserNotFoundException if no user is found with the provided email
     * @throws IllegalArgumentException if the password does not match
     * @throws IllegalStateException if the user account is not verified
     */
    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("No user found with email {}", request.getEmail());
                    return new UserNotFoundException("Invalid email or password");
                });

        log.info("User found: {}", user.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Password mismatch for user {}", user.getEmail());
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (!user.isEnabled()) {
            log.warn("User {} is not verified", user.getEmail());
            throw new IllegalStateException("Account is not verified");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getName());
        log.info("Token generated successfully for {}", user.getEmail());
        
        return buildAuthResponse(token, user);
    }

    /**
     * Helper method to build AuthResponse from token and user.
     */
    private AuthResponse buildAuthResponse(String token, User user) {
        AuthResponse.UserData userData = AuthResponse.UserData.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .avatarUrl(null)  // TODO: Add avatar support
                .build();

        return AuthResponse.builder()
                .token(token)
                .user(userData)
                .build();
    }
}
