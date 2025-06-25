package com.cinerama.backend.service.impl;

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
    public void verifyAccount(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expirado");
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(verificationToken);
    }
}