package com.example.caspaceapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.caspaceapplication.fragments.CustomerProfileFragment;
import com.example.caspaceapplication.fragments.HomeFragment;
import com.example.caspaceapplication.fragments.MessageFragment;
import com.example.caspaceapplication.fragments.NotificationFragment;
import com.google.android.material.navigation.NavigationView;

public class NavigationHomepage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_homepage);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.profile:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        fragmentR(new CustomerProfileFragment());
                        //Intent intent = new Intent(NavigationHomepage.this, CustomerProfileFragment.class);
                        Toast.makeText(NavigationHomepage.this, "Profile", Toast.LENGTH_SHORT).show();
                        //startActivity(intent);
                        break;

                    case R.id.home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        fragmentR(new HomeFragment());
                        Toast.makeText(NavigationHomepage.this, "Home", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.messages:
                        fragmentR(new MessageFragment());
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NavigationHomepage.this, "Messages", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.notification:
                        fragmentR(new NotificationFragment());
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NavigationHomepage.this, "Notifications", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });

    }

    private void fragmentR(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}