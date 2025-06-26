package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.VerificationRequest;
import com.cinerama.backend.entity.User;
import com.cinerama.backend.entity.VerificationToken;
import com.cinerama.backend.repository.UserRepository;
import com.cinerama.backend.repository.VerificationTokenRepository;
import com.cinerama.backend.service.MailService;
import com.cinerama.backend.service.VerificationTokenService;
import com.cinerama.backend.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Override
    public void createVerificationToken(User user) {
        String token = codeGenerator.generateCode();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();
        tokenRepository.save(verificationToken);
        mailService.sendVerificationEmail(user.getEmail(), token);
    }

    @Override
    public void verifyAccount(VerificationRequest request) {
        // Find the verification token by its value
        VerificationToken token = tokenRepository.findByToken(request.getVerificationCode())
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        // Check if the token is associated with the correct user
        if (!token.getUser().getEmail().equalsIgnoreCase(request.getEmail())) {
            throw new IllegalArgumentException("Email does not match the token");
        }
        // Check if the token has expired
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Expired token");
        }
        // Activate the user account
        User user = token.getUser();
        user.setEnabled(true);
        // Save the updated user and delete the token
        userRepository.save(user);
        tokenRepository.delete(token);
    }
}