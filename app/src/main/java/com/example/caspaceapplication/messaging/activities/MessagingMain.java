package com.example.caspaceapplication.messaging.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.caspaceapplication.databinding.ActivityMessagingMainBinding;
import com.example.caspaceapplication.messaging.utilities.Constants;
import com.example.caspaceapplication.messaging.utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

public class MessagingMain extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ActivityMessagingMainBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessagingMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetails();
        getToken();
        setListeners();

    }

    private void setListeners() {
        //binding.imageSignOut.setOnClickListener(v -> signOut());
        binding.fabNewChat.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), MsgUsersActivity.class)));
    }



    private void loadUserDetails() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String userCombinedId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Query for Customer user type
        Query queryCustomer = database.collection(Constants.KEY_COMBINED_COLLECTION)
                .whereEqualTo(Constants.KEY_COMBINED_ID, userCombinedId)
                .whereEqualTo(Constants.KEY_USER_TYPE, "Customer");

        queryCustomer.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Retrieve user details from the document
                    String textFirstName = document.getString(Constants.KEY_COMBINED_FIRST_NAME);
                    String textLastName = document.getString(Constants.KEY_COMBINED_LAST_NAME);
                    String imageString = document.getString(Constants.KEY_COMBINED_IMAGE);

                    // Load and display user profile image
                    if (imageString != null) {
                        byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        binding.imageProfile.setImageBitmap(bitmap);
                    }

                    // Set user details in the UI
                    //binding.textFirstName.setText(textFirstName + " " + textLastName);
                    binding.textFirstName.setText(textFirstName);
                    binding.textLastName.setText(textLastName);
                }
            } else {
                // Handle case where user details could not be loaded
                Toast.makeText(this, "Failed to load user details", Toast.LENGTH_SHORT).show();
            }
        });

        // Query for Owner user type
        Query queryOwner = database.collection(Constants.KEY_COMBINED_COLLECTION)
                .whereEqualTo(Constants.KEY_COMBINED_ID, userCombinedId)
                .whereEqualTo(Constants.KEY_USER_TYPE, "Owner");

        queryOwner.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Retrieve user details from the document
                    String textFirstName = document.getString(Constants.KEY_COMBINED_FIRST_NAME);
                    String textLastName = document.getString(Constants.KEY_COMBINED_LAST_NAME);
                    String imageString = document.getString(Constants.KEY_COMBINED_IMAGE);

                    // Load and display user profile image
                    if (imageString != null) {
                        byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        binding.imageProfile.setImageBitmap(bitmap);
                    }

                    // Set user details in the UI
                    //binding.textFirstName.setText(user.userFirstName + " " + user.userLastName);
                    binding.textFirstName.setText(textFirstName);

                    binding.textLastName.setText(textLastName);
                }
            } else {
                // Handle case where user details could not be loaded
                Toast.makeText(this, "Failed to load user details", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getToken()
    {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);

    }

    /*private void updateToken(String token) {
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        String userId = preferenceManager.getString(Constants.KEY_USER_ID);
        if (userId == null || userId.isEmpty()) {
            showToast("User ID is null or empty");
            return;
        }

        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS)
                        .document(userId);
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("Unable to update token"));


    }*/

    private void updateToken(String token) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    //String token = task.getResult();
                    String customersIDNum = firebaseAuth.getCurrentUser().getUid();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("CustomerUserAccounts").document(customersIDNum)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String existingToken = documentSnapshot.getString("fcmToken");
                                    if (!TextUtils.isEmpty(existingToken) && existingToken.equals(token)) {
                                        Log.d(TAG, "FCM token already exists in database");
                                        return;
                                    }
                                }
                                db.collection("CustomerUserAccounts").document(customersIDNum)
                                        .update("fcmToken", token)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d(TAG, "FCM token updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Error updating FCM token", e);
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error getting FCM token for owner", e);
                            });
                });
    }

    /*private void signOut() {
        // Delete the FCM token from Firebase Cloud Messaging server
        FirebaseMessaging.getInstance().deleteToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // FCM token deleted successfully
                        // Proceed with user sign-out
                        FirebaseAuth.getInstance().signOut();
                        preferenceManager.clear();
                        // Redirect the user to the login page or any other appropriate screen
                        startActivity(new Intent(getApplicationContext(), LoginCustomerTrial.class));
                        finish();
                    } else {
                        // Error occurred while deleting FCM token
                        // Handle the error or log the error message
                        Exception exception = task.getException();
                        if (exception != null) {
                            String errorMessage = exception.getMessage();
                            Log.e(TAG, "FCM token deletion error: " + errorMessage);
                        }
                        // Proceed with user sign-out even if FCM token deletion fails
                        FirebaseAuth.getInstance().signOut();
                        preferenceManager.clear();
                        startActivity(new Intent(getApplicationContext(), LoginCustomerTrial.class));
                        finish();
                    }
                });
    }*/




}










