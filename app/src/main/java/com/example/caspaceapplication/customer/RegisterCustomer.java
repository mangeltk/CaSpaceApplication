package com.example.caspaceapplication.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.databinding.ActivityRegisterCustomerBinding;
import com.example.caspaceapplication.databinding.ActivityRegisterOwnerBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterCustomer extends AppCompatActivity {

    ActivityRegisterCustomerBinding binding;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        progressDialog = new ProgressDialog(this);

        binding.registerButtonCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customer_email = binding.customerEmail.getText().toString().trim();
                String customer_firstName = binding.customerFirstName.getText().toString();
                String customer_lastName = binding.customerLastName.getText().toString();
                String customer_username = binding.customerUsername.getText().toString();
                String customer_password = binding.customerPassword.getText().toString();
                String customer_organization = binding.customerOrganization.getText().toString();
                String customer_population = binding.customerPopulation.getText().toString();
                String customer_account_status= "Enabled";

                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(customer_email, customer_password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                String customer_idNum = currentUser.getUid();

                                // Store the additional user data in the database
                                firebaseFirestore.collection("CustomerUserAccounts")
                                        .document(customer_idNum)
                                        .set(new CustomerRegistrationModel(customer_idNum, customer_email, customer_firstName, customer_lastName, customer_username, customer_password, customer_organization, customer_population,customer_account_status))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(RegisterCustomer.this, "Successfully registered!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterCustomer.this, LoginCustomerTrial.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(RegisterCustomer.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterCustomer.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }

        });

    }

}

