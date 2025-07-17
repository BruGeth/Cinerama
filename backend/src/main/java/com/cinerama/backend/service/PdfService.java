package com.cinerama.backend.service;

import java.io.ByteArrayOutputStream;

public interface PdfService {
    byte[] generateConfectioneryOrderPdf(Long orderId);
} 