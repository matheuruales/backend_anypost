package com.anypost.controller;

import com.anypost.model.UserProfile;
import com.anypost.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private final UserProfileService userProfileService;

    public ProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public ResponseEntity<UserProfile> getProfile(Authentication authentication) {
        try {
            String uid = (String) authentication.getPrincipal();
            logger.debug("Fetching profile for UID: {}", uid);
            
            UserProfile profile = userProfileService.getUserProfile(uid);
            return ResponseEntity.ok(profile);
            
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Failed to fetch profile: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            logger.error("Unexpected error fetching profile: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping
    public ResponseEntity<UserProfile> updateProfile(
            Authentication authentication,
            @RequestBody UserProfile profileUpdates) {
        try {
            String uid = (String) authentication.getPrincipal();
            logger.debug("Updating profile for UID: {}", uid);
            
            UserProfile updatedProfile = userProfileService.updateUserProfile(uid, profileUpdates);
            return ResponseEntity.ok(updatedProfile);
            
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Failed to update profile: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            logger.error("Unexpected error updating profile: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}