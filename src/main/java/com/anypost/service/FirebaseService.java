package com.anypost.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class FirebaseService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class);
    private final FirebaseAuth firebaseAuth;

    public FirebaseService(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public Authentication authenticate(String idToken) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            
            logger.debug("Successfully verified token for user: {}", uid);
            
            return new UsernamePasswordAuthenticationToken(
                uid, 
                null, 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } catch (FirebaseAuthException e) {
            logger.error("Firebase authentication failed: {}", e.getMessage());
            throw new RuntimeException("Invalid Firebase ID token", e);
        } catch (Exception e) {
            logger.error("Unexpected error during authentication: {}", e.getMessage());
            throw new RuntimeException("Authentication failed", e);
        }
    }

    public String getUidFromToken(String idToken) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            return decodedToken.getUid();
        } catch (FirebaseAuthException e) {
            logger.error("Failed to get UID from token: {}", e.getMessage());
            throw new RuntimeException("Invalid Firebase ID token", e);
        }
    }

    public FirebaseToken verifyToken(String idToken) {
        try {
            return firebaseAuth.verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            logger.error("Token verification failed: {}", e.getMessage());
            throw new RuntimeException("Token verification failed", e);
        }
    }
}