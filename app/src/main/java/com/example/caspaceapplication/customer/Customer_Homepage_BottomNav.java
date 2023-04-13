package com.example.caspaceapplication.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.caspaceapplication.OwnerHomepage;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.fragments.BookingsFragment;
import com.example.caspaceapplication.fragments.CustomerProfileFragment;
import com.example.caspaceapplication.fragments.HomeFragment;
import com.example.caspaceapplication.fragments.MessageFragment;
import com.example.caspaceapplication.fragments.NotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Customer_Homepage_BottomNav extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_homepage_bottom_nav);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();


        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.menuHome:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.menuBookings:
                        selectedFragment = new BookingsFragment();
                        break;
                    case R.id.menuMessages:
                        selectedFragment = new MessageFragment();
                        break;
                    case R.id.menuNotification:
                        selectedFragment = new NotificationFragment();
                        break;
                    case R.id.menuProfile:
                        selectedFragment = new CustomerProfileFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });

    }
}