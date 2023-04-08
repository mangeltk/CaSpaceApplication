package com.example.caspaceapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Locale;

public class HomePage extends AppCompatActivity {

    BottomNavigationView navigation;
    SearchView searchView;
    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        navigation = findViewById(R.id.navigation);
        searchView=findViewById(R.id.searchView);
        listView=findViewById(R.id.listView);
        listView.setVisibility(View.GONE);

        arrayList=new ArrayList<>();
        arrayList.add("Enspace");
        arrayList.add("Regus");
        arrayList.add("The Company");
        arrayList.add("Workplace Cafe");
        arrayList.add("X Office");
        arrayList.add("GetHub");
        arrayList.add("Desk");

        adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        listView.setVisibility(View.VISIBLE);
                        adapter.getFilter().filter(s);
                        return false;
                    }
                });

        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home:
                        Toast.makeText(HomePage.this, "Home", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.booking:
                        Toast.makeText(HomePage.this, "Booking", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.profile:
                        Toast.makeText(HomePage.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;

                    default:

                }
                return false;
            }
        });
    }
}