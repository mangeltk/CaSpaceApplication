package com.example.caspaceapplication.Owner.BookingTransactions;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.caspaceapplication.Owner.OwnerHomepage;
import com.example.caspaceapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class Owner_BookingTransactions extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_booking_transactions2);

        useBottomNavigationMenu();

        //todo:
        // Booking ID
        // Booking date
        // Booking total
        // Booking Payment Proof
        // Booking Duration
        // Booking Status - (pending, ongoing, completed )
        // Customer ID
        // Owner ID
        // CoSpace ID
        // Space ID

        tabLayout = findViewById(R.id.tabLayoutBTO);
        viewPager = findViewById(R.id.viewPagerBTO);
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.addFragment(new OwnerBT_PendingTabFragment(), "Pending");
        viewPagerAdapter.addFragment(new OwnerBT_OngoingTabFragment(), "Ongoing");
        viewPagerAdapter.addFragment(new OwnerBT_CompletedTabFragment(), "Completed");
        viewPager.setAdapter(viewPagerAdapter);

    }

    public void useBottomNavigationMenu(){
        //Navigation Bar------------------------------------------
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        Toast.makeText(Owner_BookingTransactions.this, "Home", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Owner_BookingTransactions.this, OwnerHomepage.class));
                        break;
                    case R.id.menuMessages:
                        Toast.makeText(Owner_BookingTransactions.this, "Messages", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuNotification:
                        Toast.makeText(Owner_BookingTransactions.this, "Notifications", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuProfile:
                        Toast.makeText(Owner_BookingTransactions.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
                return true;
            }
        });//Navigation Bar------------------------------------------
    }
}