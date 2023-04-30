package com.example.caspaceapplication.customer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class CWS_ProfilePage extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cws_profile_page);



    }
}