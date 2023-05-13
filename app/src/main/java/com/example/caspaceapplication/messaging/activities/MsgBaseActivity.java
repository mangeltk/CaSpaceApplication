package com.example.caspaceapplication.messaging.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.messaging.models.UserMdl;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MsgBaseActivity extends AppCompatActivity {

    UserMdl usermdl = new UserMdl();
    private DocumentReference documentReference;
    FirebaseFirestore database= FirebaseFirestore.getInstance();
    private DocumentSnapshot documentSnapshot;
    private String documentID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    protected void onPause() {
        super.onPause();

        // Get the current user ID
        String usercombinedId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Update the user availability to 0 in the Firestore database
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference userRef = database.collection("UserAccounts").document(usercombinedId);
        userRef.update("availability", 0)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // User availability updated successfully
                        Log.d("MyActivity", "User availability set to 0");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update user availability
                        Log.e("MyActivity", "Failed to set user availability to 0", e);
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        String usercombinedId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Update the user availability to 0 in the Firestore database
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference userRef = database.collection("UserAccounts").document(usercombinedId);
        userRef.update("availability", 1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // User availability updated successfully
                        Log.d("MyActivity", "User availability set to 0");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update user availability
                        Log.e("MyActivity", "Failed to set user availability to 0", e);
                    }
                });
    }
}