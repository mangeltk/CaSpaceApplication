package com.example.caspaceapplication.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.Owner.AmenitiesOffered.Owner_AmenitiesOffered;
import com.example.caspaceapplication.Owner.BookingTransactions.Owner_BookingTransactions;
import com.example.caspaceapplication.Owner.OfficeLayouts.Owner_OfficeLayouts;
import com.example.caspaceapplication.Owner.ProDisc.Owner_ProDisc;
import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OwnerHomepage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    FirebaseFirestore firebaseFirestore;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_homepage);

        //get current user ID
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ImageView officeLayouts = findViewById(R.id.officeLayout_Imageview);
        ImageView promotionsAndDiscounts = findViewById(R.id.proAndDisc_Imageview);
        ImageView bookingTransactions = findViewById(R.id.bookingTransactions_Imageview);
        ImageView AmenitiesOffered = findViewById(R.id.AmenitiesOffered_Imageview);
        TextView username = findViewById(R.id.textUsername);

        FirebaseFirestore.getInstance().collection("OwnerUserAccounts")
                .document(user)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("ownerUsername");
                            username.setText(name);
                        }
                    }
                });

        officeLayouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerHomepage.this, Owner_OfficeLayouts.class));
            }
        });

        promotionsAndDiscounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerHomepage.this, Owner_ProDisc.class));
            }
        });

        AmenitiesOffered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerHomepage.this, Owner_AmenitiesOffered.class));
            }
        });

        bookingTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerHomepage.this, Owner_BookingTransactions.class));
            }
        });


        // Initialize the bottom navigation bar
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHome:
                // Do nothing because already on home page
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuMessages:
                // startActivity(new Intent(this, MessageActivity.class));
                return true;
            case R.id.menuNotification:
                // startActivity(new Intent(this, NotificationActivity.class));
                return true;
            case R.id.menuProfile:
                // startActivity(new Intent(this, OwnerProfileActivity.class));
                return true;
            default:
                return false;
        }



   /* public void useBottomNavigationMenu(){
        //Navigation Bar------------------------------------------
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        Toast.makeText(OwnerHomepage.this, "Home", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(OwnerHomepage.this, OwnerHomepage.class));
                        break;
                    case R.id.menuMessages:
                        Toast.makeText(OwnerHomepage.this, "Messages", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuNotification:
                        Toast.makeText(OwnerHomepage.this, "Notifications", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuProfile:
                        Toast.makeText(OwnerHomepage.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
                return true;
            }
        });//Navigation Bar------------------------------------------
    }*/
    }

}