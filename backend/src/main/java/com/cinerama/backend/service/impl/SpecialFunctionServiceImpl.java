package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.SpecialFunctionRequest;
import com.cinerama.backend.entity.SpecialFunction;
import com.cinerama.backend.repository.SpecialFunctionRepository;
import com.cinerama.backend.service.SpecialFunctionService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class SpecialFunctionServiceImpl implements SpecialFunctionService {

    /* === Injected service to send emails === */
    private final JavaMailSender mailSender;

    /* === Injected service to process email templates === */
    private final SpringTemplateEngine thymeleaf;

    /* === Injected repository to persist special function entities === */
    private final SpecialFunctionRepository repository;

    @Override
    public void processFunction(SpecialFunctionRequest req) throws MessagingException {

        /* === Create and populate SpecialFunction entity from request === */
        SpecialFunction function = new SpecialFunction();
        function.setCinema(req.getCinema());
        function.setMovie(req.getMovie());
        function.setDate(req.getDate());
        function.setTime(req.getTime());
        function.setAttendees(req.getCapacity());
        function.setRequirements(req.getRequirements());
        function.setContactName(req.getContactName());
        function.setContactEmail(req.getContactEmail());
        function.setContactPhone(req.getContactPhone());
        function.setCompany(req.getCompany());
        function.setMessage(req.getMessage());

        /* === Save the special function entity to the database === */
        repository.save(function);

        /* === Prepare email context with request data === */
        Context context = new Context();
        context.setVariable("function", req);

        /* === Process HTML template for the email body === */
        String html = thymeleaf.process("specialfunction-summary-email.html", context);

        /* === Create and configure the MIME email message === */
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");

        /* === Set recipient, subject, and content for the email === */
        System.out.println("📧 Destinatario: " + req.getContactEmail());

        String to = req.getContactEmail();
        if (to == null || to.trim().isEmpty()) {
            throw new MessagingException("La dirección de correo electrónico está vacía o es inválida.");
        }

        helper.setFrom(((JavaMailSenderImpl) mailSender).getUsername());
        helper.setTo(to);
        helper.setTo(req.getContactEmail());
        helper.setSubject("Resumen de tu Función Especial en Cinerama");
        helper.setText(html, true);

        try {
            /* === Send the email === */
            mailSender.send(mime);
            System.out.println("✅ Correo enviado correctamente.");
        } catch (Exception e) {
            /* === Handle email sending errors === */
            System.out.println("❌ Error al enviar correo: " + e.getMessage());
            throw new MessagingException("Falló el envío: " + e.getMessage(), e);
        }
    }
}
