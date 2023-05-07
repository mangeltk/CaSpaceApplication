package com.example.caspaceapplication.customer.SearchManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.caspaceapplication.Owner.BookingTransactions.ViewPagerAdapter;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.Customer_Homepage_BottomNav;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Customer_SearchManagement extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private TextView backClickable;
    private BottomNavigationView navigationView;
    private AppCompatButton searchManually,nearMeButton, chooseMapButton;
    private ViewPager viewPager;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_search_management);

        backClickable = findViewById(R.id.backTextview_SearchManagement);
        backClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Customer_SearchManagement.this, Customer_Homepage_BottomNav.class));
                Toast.makeText(Customer_SearchManagement.this, "Back", Toast.LENGTH_SHORT).show();
            }
        });

        searchManually = findViewById(R.id.enterManually_Button);
        nearMeButton = findViewById(R.id.nearMe_Button);
        chooseMapButton = findViewById(R.id.chooseMap_Button);
        viewPager = findViewById(R.id.viewPagerSM);
        fragmentContainer = findViewById(R.id.fragment_container);

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CustSM_SearchManually_fragment searchManually_fragment = new CustSM_SearchManually_fragment();
        CustSM_NearMe_fragment nearMe_fragment = new CustSM_NearMe_fragment();
        CustSM_ChooseMap_fragment chooseMap_fragment = new CustSM_ChooseMap_fragment();*/

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.addFragment(new CustSM_SearchManually_fragment(), "Search Manually");
        viewPagerAdapter.addFragment(new CustSM_NearMe_fragment(), "Near me");
        viewPagerAdapter.addFragment(new CustSM_ChooseMap_fragment(), "Choose Map");
        viewPager.setAdapter(viewPagerAdapter);

        searchManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        nearMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        chooseMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        searchManually.performClick();
                        break;
                    case 1:
                        nearMeButton.performClick();
                        break;
                    case 2:
                        chooseMapButton.performClick();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        // Initialize the bottom navigation bar
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHome:
                startActivity(new Intent(this, Customer_Homepage_BottomNav.class));
                break;
            case R.id.menuMessages:
                // startActivity(new Intent(this, MessageActivity.class));
                return true;
            case R.id.menuNotification:
                // startActivity(new Intent(this, NotificationActivity.class));
                return true;
            case R.id.menuProfile:
                // startActivity(new Intent(this, OwnerProfileActivity.class));
                return true;
        }
        return true;
    }

}