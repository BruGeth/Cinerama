package com.cinerama.backend.service;

public interface MailService {
    void sendEmail(String to, String subject, String body);
}
