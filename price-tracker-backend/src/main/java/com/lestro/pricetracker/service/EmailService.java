package com.lestro.pricetracker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.from.email}")
    private String fromEmail;

    private final WebClient webClient = WebClient.create("https://api.resend.com");

    public void sendEmail(String to, String subject, String htmlContent) {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("from", fromEmail);
        requestBody.put("to", to);
        requestBody.put("subject", subject);
        requestBody.put("html", htmlContent);

        webClient.post()
        .uri("/emails")
        .header("Authorization", "Bearer " + apiKey)
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(String.class)
        .doOnError(err -> System.out.println("Email sending failed: " + err.getMessage()))
        .block(); // <--- block to ensure execution

    }
}