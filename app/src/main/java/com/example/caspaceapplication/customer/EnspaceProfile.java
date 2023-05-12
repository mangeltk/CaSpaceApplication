package com.example.caspaceapplication.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.R;

public class EnspaceProfile extends AppCompatActivity {

private Button editProfileButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_customer);

        editProfileButton = findViewById(R.id.viewProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EnspaceProfile.this,Profile.class));
            }
        });
    }

}