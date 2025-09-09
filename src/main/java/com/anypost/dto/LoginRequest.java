package com.anypost.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    
    @NotBlank(message = "ID token is required")
    private String idToken;
    
    private String email;

    public LoginRequest() {
    }

    public LoginRequest(String idToken, String email) {
        this.idToken = idToken;
        this.email = email;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "idToken='" + idToken + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}