package com.cinerama.backend.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    public String buildVerificationEmail(String userName, String verificationCode) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("verificationCode", verificationCode);
        return templateEngine.process("verification-email", context); // "verification-email.html" in templates
    }
}
