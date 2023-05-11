package com.example.caspaceapplication.Owner.BookingTransactions;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.caspaceapplication.Owner.OwnerHomepage;
import com.example.caspaceapplication.Owner.Profile.Owner_Profile;
import com.example.caspaceapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class Owner_BookingTransactions extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView navigationView;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TextView backClickable; //todo:back button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_booking_transactions2);

        tabLayout = findViewById(R.id.tabLayoutBTO);
        viewPager = findViewById(R.id.viewPagerBTO);
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.addFragment(new OwnerBT_PendingTabFragment(), "Pending");
        viewPagerAdapter.addFragment(new OwnerBT_OngoingTabFragment(), "Ongoing");
        viewPagerAdapter.addFragment(new OwnerBT_CompletedTabFragment(), "Completed");
        viewPager.setAdapter(viewPagerAdapter);

        // Initialize the bottom navigation bar
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHome:
                startActivity(new Intent(this, OwnerHomepage.class));
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuMessages:
                // startActivity(new Intent(this, MessageActivity.class));
                return true;
            case R.id.menuNotification:
                // startActivity(new Intent(this, NotificationActivity.class));
                return true;
            case R.id.menuProfile:
                startActivity(new Intent(this, Owner_Profile.class));
                return true;
            default:
                return false;
        }
    }

}