package com.example.caspaceapplication.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.caspaceapplication.R;
import com.example.caspaceapplication.fragments.NotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Owner_BottomNavigationBar extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_bottom_navigation_bar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.owner_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHome:
                startActivity(new Intent(this, OwnerHomepage.class));
                return true;
            case R.id.menuMessages:
                //startActivity(new Intent(this, MessageActivity.class));
                return true;
            case R.id.menuNotification:
                // Create a new instance of the fragment
                //startActivity(new Intent(this, OwnerNotification.class));
                return true;
            case R.id.menuProfile:
                //startActivity(new Intent(this, OwnerProfileActivity.class));
                return true;
            default:
                return false;
        }
    }
}