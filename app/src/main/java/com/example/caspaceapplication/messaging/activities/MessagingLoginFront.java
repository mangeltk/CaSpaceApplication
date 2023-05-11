package com.example.caspaceapplication.messaging.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.caspaceapplication.R;
import com.example.caspaceapplication.databinding.ActivityMessagingLoginFrontBinding;

public class MessagingLoginFront extends AppCompatActivity {

    private ActivityMessagingLoginFrontBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());


        Button signInCustomer, signInOwner;
        signInCustomer = findViewById(R.id.button_signInCustomer);
        signInOwner = findViewById(R.id.button_signInOwner);

        signInCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MessagingSignInActivity.class));
            }
        });

        signInOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MessagingSignInActivity.class));
            }
        });

    }

}