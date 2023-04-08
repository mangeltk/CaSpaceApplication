package com.example.caspaceapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                Intent intent = new Intent(FrontRegister.this, RegisterCustomerTrial.class);
                startActivity(intent);
                finish();
            }
        });

        registerOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontRegister.this, RegisterOwner.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontRegister.this, Front.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
