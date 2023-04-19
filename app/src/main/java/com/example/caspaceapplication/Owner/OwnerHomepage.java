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

import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OwnerHomepage extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_homepage);

        useBottomNavigationMenu();

        //get current user ID
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ImageView officeLayouts = findViewById(R.id.officeLayout_Imageview);
        ImageView promotionsAndDiscounts = findViewById(R.id.proAndDisc_Imageview);
        ImageView bookingTransactions = findViewById(R.id.bookingTransactions_Imageview);//todo:create layout and activity for booking transactions
        ImageView AmenitiesOffered = findViewById(R.id.AmenitiesOffered_Imageview); //todo: create layout and activity for amenities offered
        TextView username = findViewById(R.id.textUsername);

        FirebaseFirestore.getInstance().collection("OwnerUserAccounts")
                .document(user)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String firstname  = documentSnapshot.getString("ownerFirstname");
                            username.setText(firstname);
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




    }



    public void useBottomNavigationMenu(){
        //Navigation Bar------------------------------------------
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        Toast.makeText(OwnerHomepage.this, "Home", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OwnerHomepage.this, OwnerHomepage.class));
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
    }

}