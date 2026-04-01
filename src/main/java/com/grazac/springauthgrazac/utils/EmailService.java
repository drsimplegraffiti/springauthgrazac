package com.grazac.springauthgrazac.utils;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private  final TemplateEngine templateEngine;

    public EmailService(JavaMailSender emailSender, TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }


    @Async
    public void sendVerificationEmail(String to, String subject, String templateName, Map<String, Object> templateModel)
            throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Set email details
        helper.setTo(to);
        helper.setSubject(subject);
//        helper.

        // Prepare the HTML content using the template and data
        Context context = new Context();
        context.setVariables(templateModel);
        String htmlContent = templateEngine.process(templateName, context);

        System.out.println("===================================email sent==============");
        helper.setText(htmlContent, true);
        emailSender.send(message);
        System.out.println("email sent");
    }
}