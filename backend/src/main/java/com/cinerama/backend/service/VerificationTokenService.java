package com.cinerama.backend.service;

import com.cinerama.backend.entity.User;
import com.cinerama.backend.dto.VerificationRequest;

public interface VerificationTokenService {
    void createVerificationToken(User user);
    void verifyAccount(VerificationRequest request);
}