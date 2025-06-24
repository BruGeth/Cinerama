package com.cinerama.backend.service;

import com.cinerama.backend.dto.EventRequest;
import jakarta.mail.MessagingException;

public interface EventService {
    void processEvent(EventRequest req) throws MessagingException;
}
