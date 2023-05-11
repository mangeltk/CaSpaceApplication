package com.example.caspaceapplication.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.caspaceapplication.Owner.LoginOwner;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.messaging.activities.MessagingRegisterFront;
import com.example.caspaceapplication.messaging.activities.MessagingSignInActivity;

public class Front extends AppCompatActivity {

    Button loginCustomer, loginOwner;
    TextView signUpHere;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        loginCustomer = findViewById(R.id.button_loginCustomer);
        loginOwner = findViewById(R.id.button_loginOwner);
        signUpHere = findViewById(R.id.textSignUp);


        loginCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Front.this, MessagingSignInActivity.class));
            }
        });

        loginOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Front.this, MessagingSignInActivity.class));
            }
        });

        signUpHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Front.this, MessagingRegisterFront.class));
            }
        });

    }

}