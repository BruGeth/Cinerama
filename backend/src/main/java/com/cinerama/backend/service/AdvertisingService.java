package com.cinerama.backend.service;

import com.cinerama.backend.dto.AdvertisingRequest;
import jakarta.mail.MessagingException;

public interface AdvertisingService {

    /* === Method to process an advertising request === */
    void processAdvertising(AdvertisingRequest req) throws MessagingException;
}
