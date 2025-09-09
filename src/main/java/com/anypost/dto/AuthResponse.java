package com.anypost.dto;

public class AuthResponse {
    private String uid;
    private String email;
    private String message;
    private boolean success;

    public AuthResponse() {
        this.success = true;
    }

    public AuthResponse(String message) {
        this.message = message;
        this.success = false;
    }

    public AuthResponse(String uid, String email, String message) {
        this.uid = uid;
        this.email = email;
        this.message = message;
        this.success = true;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}