package com.anypost.repository;

import com.anypost.model.UserProfile;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class UserProfileRepository {

    private final Firestore firestore;

    public UserProfileRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public UserProfile findByUid(String uid) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("users").document(uid);
        DocumentSnapshot document = docRef.get().get();
        
        if (document.exists()) {
            return document.toObject(UserProfile.class);
        }
        return null;
    }

    public void save(UserProfile userProfile) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("users").document(userProfile.getUid());
        docRef.set(userProfile).get();
    }

    public boolean existsByUid(String uid) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("users").document(uid);
        DocumentSnapshot document = docRef.get().get();
        return document.exists();
    }
}