package com.cinerama.backend.service;

import com.cinerama.backend.entity.ConfectioneryPurchase;

public interface EmailService {
    void sendPurchaseConfirmation(String toEmail, ConfectioneryPurchase purchase);
}
