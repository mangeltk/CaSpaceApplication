package com.example.caspaceapplication.messaging.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.messaging.utilities.Constants;
import com.example.caspaceapplication.messaging.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MessagingBaseActivity extends AppCompatActivity {

    private DocumentReference documentReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        documentReference = database.collection(Constants.KEY_USER_COLLECTION)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
    }

    @Override
    protected void onPause() {
        super.onPause();
        documentReference.update(Constants.KEY_AVAILABILITY, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        documentReference.update(Constants.KEY_AVAILABILITY, 1);
    }
}