package com.anypost.controller;

import com.anypost.dto.AuthResponse;
import com.anypost.dto.LoginRequest;
import com.anypost.service.FirebaseService;
import com.anypost.service.UserProfileService;
import com.anypost.model.UserProfile;
import com.google.firebase.auth.FirebaseToken;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final FirebaseService firebaseService;
    private final UserProfileService userProfileService;

    public AuthController(FirebaseService firebaseService, UserProfileService userProfileService) {
        this.firebaseService = firebaseService;
        this.userProfileService = userProfileService;
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthResponse> verifyToken(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            logger.debug("Verifying token for email: {}", loginRequest.getEmail());
            
            FirebaseToken decodedToken = firebaseService.verifyToken(loginRequest.getIdToken());
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            
            // Use the email from the token if not provided in request
            String userEmail = email != null ? email : loginRequest.getEmail();
            
            UserProfile userProfile = userProfileService.getOrCreateUserProfile(uid, userEmail);
            
            AuthResponse response = new AuthResponse();
            response.setUid(uid);
            response.setEmail(userEmail);
            response.setMessage("Token verified successfully");
            response.setSuccess(true);
            
            logger.info("Token verified successfully for user: {}", userEmail);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Token verification failed: {}", e.getMessage());
            AuthResponse response = new AuthResponse();
            response.setMessage("Invalid token: " + e.getMessage());
            response.setSuccess(false);
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("{\"status\": \"OK\", \"service\": \"anypost-backend\"}");
    }

    @PostMapping("/validate")
    public ResponseEntity<AuthResponse> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Invalid authorization header");
            }
            
            String idToken = authHeader.substring(7);
            FirebaseToken decodedToken = firebaseService.verifyToken(idToken);
            
            AuthResponse response = new AuthResponse();
            response.setUid(decodedToken.getUid());
            response.setEmail(decodedToken.getEmail());
            response.setMessage("Token is valid");
            response.setSuccess(true);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            AuthResponse response = new AuthResponse();
            response.setMessage("Invalid token: " + e.getMessage());
            response.setSuccess(false);
            return ResponseEntity.status(401).body(response);
        }
    }
}