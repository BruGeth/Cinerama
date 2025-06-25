package com.cinerama.backend.service;

import com.cinerama.backend.entity.User;

public interface VerificationTokenService {
    void createVerificationToken(User user);
    void verifyAccount(String token);
}