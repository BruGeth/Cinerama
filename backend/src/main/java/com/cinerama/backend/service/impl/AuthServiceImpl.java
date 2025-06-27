package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.LoginRequest;
import com.cinerama.backend.dto.LoginResponse;
import com.cinerama.backend.dto.RegisterRequest;
import com.cinerama.backend.dto.VerificationRequest;
import com.cinerama.backend.entity.Role;
import com.cinerama.backend.entity.User;
import com.cinerama.backend.repository.RoleRepository;
import com.cinerama.backend.repository.UserRepository;
import com.cinerama.backend.service.AuthService;
import com.cinerama.backend.service.MailService;
import com.cinerama.backend.util.CodeGenerator;
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
    private final MailService mailService;
    private final VerificationTokenServiceImpl verificationTokenService;
    private final CodeGenerator codeGenerator;
    private final JwtUtil jwtUtil;

    @Override
    public User register(RegisterRequest request) {
        // Check if the provided passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Check
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));


        // Create a new user entity with the provided details
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Encrypt the password
                .enabled(false) // Set the account as disabled until verification
                .role(defaultRole)
                .build();

        // Save the user to the database
        User savedUser = userRepository.save(user);

        // Generate a verification code for the user and send a verification email
        verificationTokenService.createVerificationToken(savedUser);
        return savedUser;
    }

    @Override
    public void verify(VerificationRequest request) {
        log.info("Verifying account for email: {}", request.getEmail());

        // Validate the verification code and activate the user account
        verificationTokenService.verifyAccount(request);

        log.info("Account verified successfully for email: {}", request.getEmail());
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("No user found with email {}", request.getEmail());
                    return new IllegalArgumentException("Invalid email or password");
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
        return new LoginResponse(user.getName(), token);
    }
}
