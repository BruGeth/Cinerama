package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.LoginRequest;
import com.cinerama.backend.dto.LoginResponse;
import com.cinerama.backend.dto.RegisterRequest;
import com.cinerama.backend.dto.VerificationRequest;
import com.cinerama.backend.entity.User;
import com.cinerama.backend.repository.UserRepository;
import com.cinerama.backend.service.AuthService;
import com.cinerama.backend.service.MailService;
import com.cinerama.backend.util.CodeGenerator;
import com.cinerama.backend.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final MailService mailService;
    private final CodeGenerator codeGenerator;
    private final JwtUtil jwtUtil;

    @Override
    public User register(RegisterRequest request) {
        // Check if the provided passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Create a new user entity with the provided details
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Encrypt the password
                .enabled(false) // Set the account as disabled until verification
                .verificationCode(codeGenerator.generateCode()) // Generate a unique verification code
                .build();

        // Save the user to the database
        User savedUser = userRepository.save(user);

        // Send a verification email to the user
        mailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationCode());

        return savedUser;
    }

    @Override
    public void verify(VerificationRequest request) {
        // Find the user by email or throw an exception if not found
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the provided verification code matches the one stored
        if (!request.getVerificationCode().equals(user.getVerificationCode())) {
            throw new IllegalArgumentException("Invalid verification code");
        }

        // Enable the user account and clear the verification code
        user.setEnabled(true);
        user.setVerificationCode(null);
        userRepository.save(user);
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
