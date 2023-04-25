package com.example.caspaceapplication.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.caspaceapplication.R;

public class CustomerRecyclerHomepageActivity extends AppCompatActivity {

    Button uploadButton;
    RecyclerView recyclerHomepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_recycler_homepage);

        uploadButton = findViewById(R.id.uploadButton);
        recyclerHomepage = findViewById(R.id.recyclerHomepage);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerRecyclerHomepageActivity.this, CustomerRecyclerCWSHomepage.class);
                startActivity(intent);
            }
        });
    }
}