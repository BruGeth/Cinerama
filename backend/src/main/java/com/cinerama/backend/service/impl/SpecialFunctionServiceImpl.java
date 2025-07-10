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

/**
 * Service implementation for managing SpecialFunction requests.
 *
 * <p>This service handles the business logic for processing SpecialFunction requests,
 * including saving function details to the database and sending summary emails
 * to the contact person.</p>
 *
 * <h2>Methods:</h2>
 * <ul>
 *   <li><b>processFunction:</b> Processes a SpecialFunction request and sends a summary email</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>This service is used in the application layer to handle SpecialFunction-related operations.</p>
 *
 * @see com.cinerama.backend.repository.SpecialFunctionRepository
 * @see com.cinerama.backend.dto.SpecialFunctionRequest
 */

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
