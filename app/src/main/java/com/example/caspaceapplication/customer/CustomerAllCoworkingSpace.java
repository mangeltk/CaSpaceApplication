package com.example.caspaceapplication.customer;

import android.os.Bundle;
import android.widget.Adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        setContentView(R.layout.fragment_home2);

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