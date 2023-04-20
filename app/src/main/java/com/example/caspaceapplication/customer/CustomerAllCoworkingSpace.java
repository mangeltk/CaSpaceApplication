package com.example.caspaceapplication.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.example.caspaceapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerAllCoworkingSpace extends AppCompatActivity {

    RecyclerView recyclerView;
    List<String> abcd;
    List <Integer> abcdImage;
    Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_all_coworking_space);

        recyclerView = findViewById(R.id.recyclerView);
        abcd = new ArrayList<>();
        abcdImage = new ArrayList<>();

        abcd.add("Enspace");
        abcd.add("Regus");
        abcd.add("The Company");

        abcdImage.add(R.drawable.recoone);
        abcdImage.add(R.drawable.recotwo);
        abcdImage.add(R.drawable.recothree);

//        adapter = (Adapter) new CustomerCWSAdapter(this, abcd, abcdImage);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);


    }
}