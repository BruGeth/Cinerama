package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.VerificationRequest;
import com.cinerama.backend.entity.User;
import com.cinerama.backend.entity.VerificationToken;
import com.cinerama.backend.exception.auth.ExpiredTokenException;
import com.cinerama.backend.exception.auth.InvalidTokenException;
import com.cinerama.backend.exception.auth.TokenNotBelongUserException;
import com.cinerama.backend.exception.repository.UserRepository;
import com.cinerama.backend.exception.repository.VerificationTokenRepository;
import com.cinerama.backend.service.MailService;
import com.cinerama.backend.service.VerificationTokenService;
import com.cinerama.backend.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementation of the VerificationTokenService interface.
 * Handles verification token creation, validation, and user account activation.
 */
@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private CodeGenerator codeGenerator;

    /**
     * Creates a verification token for the user and sends it via email.
     *
     * @param user The user for whom the verification token is created.
     */
    @Override
    public void createVerificationToken(User user) {
        String token = codeGenerator.generateCode();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();
        tokenRepository.save(verificationToken);
        // Send the verification email to the user
        mailService.sendVerificationEmail(user.getEmail(), token);
    }

    /**
     * Verifies the user's account using the provided verification request.
     *
     * @param request The verification request containing the email and verification code.
     * @throws InvalidTokenException if the token is invalid.
     * @throws TokenNotBelongUserException if the token does not match the user's email or does not belong to the user.
     * @throws ExpiredTokenException if the token has expired.
     */
    @Override
    public void verifyAccount(VerificationRequest request) {
        // Find the verification token by its value
        VerificationToken token = tokenRepository.findByToken(request.getVerificationCode())
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));
        // Check if the token is associated with the correct user
        if (!token.getUser().getEmail().equalsIgnoreCase(request.getEmail())) {
            throw new TokenNotBelongUserException("Email does not match the token");
        }
        // Check if the token has expired
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ExpiredTokenException("Expired token");
        }
        // Activate the user account
        User user = token.getUser();
        user.setEnabled(true);
        // Save the updated user and delete the token
        userRepository.save(user);
        tokenRepository.delete(token);
    }
}