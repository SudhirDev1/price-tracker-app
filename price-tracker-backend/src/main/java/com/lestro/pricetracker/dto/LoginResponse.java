package com.lestro.pricetracker.dto;

public class LoginResponse {

    private String token;
    private Long userId;
    private String email;
    private String name;

    public LoginResponse(String token, Long userId, String email, String name) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}