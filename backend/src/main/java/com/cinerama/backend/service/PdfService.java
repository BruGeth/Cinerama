package com.cinerama.backend.service;

public interface PdfService {
    byte[] generateConfectioneryOrderPdf(Long orderId);
} 