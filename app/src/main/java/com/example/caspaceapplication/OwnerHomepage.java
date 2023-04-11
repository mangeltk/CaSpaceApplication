package com.example.caspaceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class OwnerHomepage extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_homepage);

        //get current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ImageView officeLayouts = findViewById(R.id.officeLayout_Imageview);

        BottomNavigationView navBar = findViewById(R.id.bottomNavigationView);
        navBar.setSelectedItemId(R.id.menuHome);


        officeLayouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerHomepage.this, Owner_OfficeLayouts.class));
            }
        });



    }
}