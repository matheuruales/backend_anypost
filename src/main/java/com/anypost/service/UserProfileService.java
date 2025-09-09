package com.anypost.service;

import com.anypost.model.UserProfile;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ExecutionException;

@Service
public class UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);
    private final Firestore firestore;

    public UserProfileService(Firestore firestore) {
        this.firestore = firestore;
    }

    public UserProfile getOrCreateUserProfile(String uid, String email) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("users").document(uid);
        DocumentSnapshot document = docRef.get().get();
        
        if (document.exists()) {
            logger.debug("User profile found for UID: {}", uid);
            return document.toObject(UserProfile.class);
        } else {
            logger.debug("Creating new user profile for UID: {}", uid);
            UserProfile newProfile = new UserProfile();
            newProfile.setUid(uid);
            newProfile.setEmail(email);
            newProfile.setCreatedAt(new Date());
            
            ApiFuture<WriteResult> result = docRef.set(newProfile);
            result.get(); // Wait for the operation to complete
            
            logger.info("Created new user profile for: {}", email);
            return newProfile;
        }
    }

    public UserProfile updateUserProfile(String uid, UserProfile profileUpdates) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("users").document(uid);
        DocumentSnapshot currentDocument = docRef.get().get();
        
        if (!currentDocument.exists()) {
            throw new RuntimeException("User profile not found for UID: " + uid);
        }
        
        
        if (profileUpdates.getDisplayName() != null) {
            docRef.update("displayName", profileUpdates.getDisplayName());
            logger.debug("Updated displayName for UID: {}", uid);
        }
        
        if (profileUpdates.getPhotoURL() != null) {
            docRef.update("photoURL", profileUpdates.getPhotoURL());
            logger.debug("Updated photoURL for UID: {}", uid);
        }
        
        // Get the updated document
        DocumentSnapshot updatedDocument = docRef.get().get();
        UserProfile updatedProfile = updatedDocument.toObject(UserProfile.class);
        
        logger.info("Updated profile for UID: {}", uid);
        return updatedProfile;
    }

    public UserProfile getUserProfile(String uid) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("users").document(uid);
        DocumentSnapshot document = docRef.get().get();
        
        if (!document.exists()) {
            throw new RuntimeException("User profile not found for UID: " + uid);
        }
        
        return document.toObject(UserProfile.class);
    }
}