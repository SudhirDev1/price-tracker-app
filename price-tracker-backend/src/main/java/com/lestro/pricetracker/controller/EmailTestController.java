package com.lestro.pricetracker.controller;

import com.lestro.pricetracker.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailTestController {

    private final EmailService emailService;

    public EmailTestController(EmailService emailService) {
        this.emailService = emailService;
    }

    // Test endpoint to send email
    @GetMapping("/api/test-email")
    public String sendTestEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body
    ) {
        emailService.sendEmail(to, subject, body);
        return "Email request sent!";
    }
}