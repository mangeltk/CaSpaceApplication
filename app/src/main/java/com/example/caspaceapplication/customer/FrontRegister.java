package com.example.caspaceapplication.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.Owner.RegisterOwner;
import com.example.caspaceapplication.R;

public class FrontRegister extends AppCompatActivity {

    Button registerCustomer, registerOwner;
    TextView textSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_register);

        registerCustomer = findViewById(R.id.registerCustomer);
        registerOwner = findViewById(R.id.registerOwner);
        textSignIn = findViewById(R.id.textSignIn);

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

        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FrontRegister.this,Front.class));
            }
        });

    }
}
