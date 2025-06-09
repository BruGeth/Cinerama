package com.cinerama.backend.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Builds HTML email content using Thymeleaf templates.
 */
@Component
@RequiredArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    /**
     * Generates HTML verification email from template with user-specific data.
     *
     * @param userName recipient's display name for personalization
     * @param verificationCode unique code to embed in email content
     * @return rendered HTML email content ready for sending
     */
    public String buildVerificationEmail(String userName, String verificationCode) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("verificationCode", verificationCode);
        return templateEngine.process("verification-email", context);
    }
}
