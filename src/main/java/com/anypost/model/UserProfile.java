package com.anypost.model;

import java.util.Date;

public class UserProfile {
    private String uid;
    private String email;
    private String displayName;
    private String photoURL;
    private Date createdAt;

    public UserProfile() {
    }

    public UserProfile(String uid, String email) {
        this.uid = uid;
        this.email = email;
        this.createdAt = new Date();
    }

    // Getters and Setters
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}