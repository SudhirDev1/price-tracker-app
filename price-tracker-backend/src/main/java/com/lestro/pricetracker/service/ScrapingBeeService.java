package com.lestro.pricetracker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class ScrapingBeeService {

    @Value("${scrapingbee.api.key}")
    private String apiKey;

    @Value("${scrapingbee.api.url}")   // = https://app.scrapingbee.com/api/v1
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // -------------------------------
    // Fetch product by ASIN
    // -------------------------------
    public String fetchProductDetails(String asin) {

        URI uri = UriComponentsBuilder
                .fromUriString(apiUrl + "/amazon/product")
                .queryParam("api_key", apiKey)
                .queryParam("asin", asin)
                .queryParam("domain", "in")
                .build(true)
                .toUri();

        return restTemplate.getForObject(uri, String.class);
    }

    // -------------------------------
    // Search Amazon by keyword
    // -------------------------------
    public String searchAmazonProduct(String query) {

        // Fix: replace spaces with +
        String encodedQuery = query.replace(" ", "+");

        URI uri = UriComponentsBuilder
                .fromUriString(apiUrl + "/amazon/search")
                .queryParam("api_key", apiKey)
                .queryParam("query", encodedQuery)
                .queryParam("domain", "in")
                .queryParam("light_request", "true")
                .build(true)
                .toUri();

        return restTemplate.getForObject(uri, String.class);
    }
}