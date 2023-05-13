package com.example.caspaceapplication.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.BookingTransactionManagement.BookingsFragment;
import com.example.caspaceapplication.fragments.CustomerProfileFragment;
import com.example.caspaceapplication.fragments.HomeFragment;
import com.example.caspaceapplication.fragments.NotificationFragment;
import com.example.caspaceapplication.messaging.activities.MessagingMain;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Customer_Homepage_BottomNav extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_homepage_bottom_nav);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuHome:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    return true;
                case R.id.menuBookings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BookingsFragment()).commit();
                    return true;
                case R.id.menuMessages:
                    Intent intent = new Intent(getApplicationContext(), MessagingMain.class);
                    startActivity(intent);
                    return true;
                case R.id.menuNotification:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationFragment()).commit();
                    return true;
                case R.id.menuProfile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CustomerProfileFragment()).commit();
                    return true;
                default:
                    return false;
            }
        }
    };



}