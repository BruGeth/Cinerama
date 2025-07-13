package com.cinerama.backend.service.impl;

import com.cinerama.backend.entity.ConfectioneryPurchase;
import com.cinerama.backend.service.EmailService;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendPurchaseConfirmation(String toEmail, ConfectioneryPurchase purchase) {
        // Aquí construirías el contenido del email
        String subject = "🎫 Tu compra en Cinerama fue exitosa";
        String body = "Gracias por tu compra el " + purchase.getDate() +
                ".\nTotal: S/. " + String.format("%.2f", purchase.getTotal()) +
                "\n\n¡Que disfrutes tus snacks!";

        // Aquí usarías tu método de envío real (JavaMailSender, SendGrid, etc.)
        System.out.println("📧 Enviando correo a: " + toEmail);
        System.out.println("Asunto: " + subject);
        System.out.println("Contenido:\n" + body);
    }
}
