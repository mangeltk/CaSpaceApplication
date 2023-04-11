package com.example.caspaceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Front extends AppCompatActivity {

    Button loginCustomer, loginOwner, signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        loginCustomer = findViewById(R.id.loginCustomer);
        loginOwner = findViewById(R.id.loginOwner);
        signUpButton = findViewById(R.id.signUpButton);


        loginCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Front.this,LoginCustomerTrial.class));
            }
        });

        loginOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Front.this,LoginOwner.class));
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Front.this,FrontRegister.class));
            }
        });

    }
}