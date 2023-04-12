package com.example.caspaceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.Owner.RegisterOwner;

public class FrontRegister extends AppCompatActivity {

    Button registerCustomer, registerOwner, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_register);

        registerCustomer = findViewById(R.id.registerCustomer);
        registerOwner = findViewById(R.id.registerOwner);
        loginButton = findViewById(R.id.loginButton);

        registerCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FrontRegister.this,RegisterCustomer.class));
            }
        });

        registerOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FrontRegister.this, RegisterOwner.class));

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FrontRegister.this,Front.class));
            }
        });

    }
}
