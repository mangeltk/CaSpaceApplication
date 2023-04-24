package com.example.caspaceapplication.messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.caspaceapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagingMain extends AppCompatActivity {

    private String customer_email;
    private String customer_firstName, customer_lastName;
    private RecyclerView messaging_recycler;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().
            getReferenceFromUrl("https://caspace-3dff1-default-rtdb.firebaseio.com/");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_main);

        final CircleImageView userProfilePic = findViewById(R.id.userProfilePic);
        messaging_recycler = findViewById(R.id.messaging_recycler);

        //get intent data from RegisterCustomer.class
        customer_email = getIntent().getStringExtra("email");
        customer_firstName = getIntent().getStringExtra("first name");
        customer_lastName = getIntent().getStringExtra("last name");

        messaging_recycler.setHasFixedSize(true);
        messaging_recycler.setLayoutManager(new LinearLayoutManager(this));


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        //get profile pic from firebase database
        /*databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                *//*final String profilePicUrl = snapshot.child("CustomerUserAccounts").child(mobile)
                        .child("profile_pic").getValue(String.class);*//*

                *//*if(profilePicUrl.isEmpty())
                {
                    //set profile pic to circle image view
                    Picasso.get().load(profilePicUrl).into(userProfilePic);
                }


            }*//*

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });*/
    }
}