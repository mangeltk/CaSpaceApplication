package com.example.caspaceapplication.messaging.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.caspaceapplication.R;
import com.example.caspaceapplication.databinding.ActivityMessagingRegisterFrontBinding;

public class MessagingRegisterFront extends AppCompatActivity {

    private ActivityMessagingRegisterFrontBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_register_front);

        Button signUpCustomer, signUpOwner;
        signUpCustomer = findViewById(R.id.button_signUpCustomer);
        signUpOwner = findViewById(R.id.button_signUpOwner);

        signUpCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MessagingSignUpActivity.class));
            }
        });

        signUpOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MessagingSignUpActivity.class));
            }
        });
    }

}