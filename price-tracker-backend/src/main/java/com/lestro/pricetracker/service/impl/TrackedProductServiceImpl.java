package com.lestro.pricetracker.service.impl;

import com.lestro.pricetracker.dto.TrackedProductDto;
import com.lestro.pricetracker.exception.ResourceNotFoundException;
import com.lestro.pricetracker.model.TrackedProduct;
import com.lestro.pricetracker.model.User;
import com.lestro.pricetracker.repository.TrackedProductRepository;
import com.lestro.pricetracker.repository.UserRepository;
import com.lestro.pricetracker.service.TrackedProductService;
import com.lestro.pricetracker.service.EmailService;
import com.lestro.pricetracker.service.ScrapingBeeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TrackedProductServiceImpl implements TrackedProductService {

    private final TrackedProductRepository trackedProductRepository;
    private final UserRepository userRepository;
    private final ScrapingBeeService scrapingBeeService;
    private final EmailService emailService;

    public TrackedProductServiceImpl(
            TrackedProductRepository trackedProductRepository,
            UserRepository userRepository,
            ScrapingBeeService scrapingBeeService,
            EmailService emailService
    ) {
        this.trackedProductRepository = trackedProductRepository;
        this.userRepository = userRepository;
        this.scrapingBeeService = scrapingBeeService;
        this.emailService = emailService;
    }

    @Override
    public TrackedProduct addProduct(TrackedProductDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TrackedProduct product = new TrackedProduct();
        product.setUser(user);
        product.setUrl(dto.getUrl());
        product.setTargetPrice(dto.getTargetPrice());
        product.setProductName(dto.getProductName());
        product.setLastChecked(LocalDateTime.now());

        return trackedProductRepository.save(product);
    }

    @Override
    public TrackedProduct updateProduct(Long id, TrackedProductDto dto) {

        TrackedProduct product = trackedProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (dto.getUrl() != null) product.setUrl(dto.getUrl());
        if (dto.getTargetPrice() != null) product.setTargetPrice(dto.getTargetPrice());
        if (dto.getProductName() != null) product.setProductName(dto.getProductName());

        product.setLastChecked(LocalDateTime.now());

        return trackedProductRepository.save(product);
    }

    @Override
    public TrackedProduct getProductById(Long id) {
        return trackedProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public List<TrackedProduct> getProductsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return trackedProductRepository.findByUserAndIsActiveTrue(user);
    }

    @Override
    public void deleteProduct(Long id) {
        TrackedProduct product = trackedProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setActive(false);
        trackedProductRepository.save(product);
    }

    @Override
    public TrackedProduct refreshPrice(Long id) {

        TrackedProduct product = trackedProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        String searchQuery = product.getProductName();
        System.out.println("Searching Amazon for: " + searchQuery);

        // 1. Fetch JSON from ScrapingBee
        String jsonResponse = scrapingBeeService.searchAmazonProduct(searchQuery);

        if (jsonResponse == null || jsonResponse.isEmpty()) {
            System.out.println("⚠️ Empty response from ScrapingBee.");
            return product;
        }

        // 2. Extract price from JSON
        Double extractedPrice = extractPriceFromJson(jsonResponse);

        if (extractedPrice != null) {
            product.setCurrentPrice(extractedPrice);
            System.out.println("✅ Extracted Price: " + extractedPrice);
        } else {
            System.out.println("⚠️ Could not find price in JSON");
        }

        product.setLastChecked(LocalDateTime.now());

        // Save updated price
        TrackedProduct savedProduct = trackedProductRepository.save(product);

        // 3. Check target price → send email if price reached
        if (savedProduct.getCurrentPrice() != null &&
            savedProduct.getTargetPrice() != null &&
            savedProduct.getCurrentPrice() <= savedProduct.getTargetPrice()) {

            String userEmail = savedProduct.getUser().getEmail();
            String subject = "Price Drop Alert for " + savedProduct.getProductName();
            String body = "<h2>Good news!</h2>"
                        + "<p>The price for <b>" + savedProduct.getProductName() + "</b> has dropped.</p>"
                        + "<p><b>Current Price:</b> ₹" + savedProduct.getCurrentPrice() + "</p>"
                        + "<p><b>Your Target Price:</b> ₹" + savedProduct.getTargetPrice() + "</p>"
                        + "<p><a href='" + savedProduct.getUrl() + "'>View Product</a></p>";

            System.out.println("Attempting to send email to: " + userEmail);
            System.out.println("Current price: " + savedProduct.getCurrentPrice());
            System.out.println("Target price: " + savedProduct.getTargetPrice());

            try {
                emailService.sendEmail(userEmail, subject, body);
                System.out.println("📩 Email sent to: " + userEmail);
            } catch (Exception e) {
                System.out.println("⚠️ Failed to send email: " + e.getMessage());
            }
        }

        return savedProduct;
    }

//    // Extract title keywords from Amazon URL (fallback)
//    private String extractTitleFromUrl(String url) {
//        try {
//            Pattern pattern = Pattern.compile("amazon\\.in/.+?/(.+?)/dp/");
//            Matcher matcher = pattern.matcher(url);
//
//            if (matcher.find()) {
//                return matcher.group(1).replace("-", " ");
//            }
//        } catch (Exception ignored) {}
//
//        return "product";
//    }

    // Extract price from JSON response
    private Double extractPriceFromJson(String json) {
        try {
            Pattern pattern = Pattern.compile("\"price\"\\s*:\\s*([0-9\\.]+)");
            Matcher matcher = pattern.matcher(json);

            if (matcher.find()) {
                return Double.parseDouble(matcher.group(1));
            }
        } catch (Exception e) {
            System.out.println("❌ Failed parsing price: " + e.getMessage());
        }
        return null;
    }
}