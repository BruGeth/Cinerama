package com.cinerama.backend.service;

import com.cinerama.backend.dto.SpecialFunctionRequest;
import jakarta.mail.MessagingException;

public interface SpecialFunctionService {

    /* === Method to process a special function request and potentially send emails === */
    void processFunction(SpecialFunctionRequest request) throws MessagingException;
}
