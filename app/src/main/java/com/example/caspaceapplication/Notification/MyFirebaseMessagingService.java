package com.example.caspaceapplication.Notification;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingService";
    String userType;

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        // Store the token in Firestore or your preferred database

        storeFCMToken(token);
    }

    private void storeFCMToken(String token) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // get the current user's ID
        //String userType;

        // get the userType field value from either collection
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CustomerUserAccounts").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userType = documentSnapshot.getString("userType");
                    } else {
                        // if the document does not exist in the CustomerUserAccounts collection, check the OwnerUserAccounts collection
                        db.collection("OwnerUserAccounts").document(uid)
                                .get()
                                .addOnSuccessListener(ownerSnapshot -> {
                                    if (ownerSnapshot.exists()) {
                                        userType = ownerSnapshot.getString("userType");
                                    }
                                });
                    }

                    // store the FCM token in the appropriate collection based on the userType field
                    if (userType.equals("customer")) {
                        db.collection("CustomerUserAccounts").document(uid)
                                .update("fcmToken", token)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "FCM token updated successfully for customer");
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error updating FCM token for customer", e);
                                });
                    } else if (userType.equals("owner")) {
                        db.collection("OwnerUserAccounts").document(uid)
                                .update("fcmToken", token)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "FCM token updated successfully for owner");
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error updating FCM token for owner", e);
                                });
                    } else {
                        Log.e(TAG, "User type not recognized: " + userType);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting userType field value", e);
                });

    }

}
