package com.example.caspaceapplication.customer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.caspaceapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CustomerEditProfile extends AppCompatActivity {

    TextView customerFirstname,customerLastname,customerEmail,customerOrganization,customerPopulation,customerPassword;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_edit_profile);
        customerFirstname=findViewById(R.id.customerFirstname);
        customerLastname=findViewById(R.id.customerLastname);
        customerEmail=findViewById(R.id.customerEmail);
        customerOrganization=findViewById(R.id.customerOrganization);
        customerPopulation=findViewById(R.id.customerPopulation);
        customerPassword=findViewById(R.id.customerPassword);

        fAuth= FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        userId =fAuth.getCurrentUser().getUid();

        DocumentReference documentReference= fStore.collection("CustomerUserAccounts").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                customerFirstname.setText(value.getString("customersFirstName"));
                customerLastname.setText(value.getString("customersLastName"));
                customerEmail.setText(value.getString("customersEmail"));
                customerOrganization.setText(value.getString("customersOrganization"));

            }
        });



    }
}