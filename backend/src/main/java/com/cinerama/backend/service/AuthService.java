package com.cinerama.backend.service;

import com.cinerama.backend.dto.LoginRequest;
import com.cinerama.backend.dto.LoginResponse;
import com.cinerama.backend.dto.RegisterRequest;
import com.cinerama.backend.dto.VerificationRequest;
import com.cinerama.backend.entity.User;

public interface AuthService {

    User register(RegisterRequest request);

    void verify(VerificationRequest request);

    LoginResponse login(LoginRequest request);

}
