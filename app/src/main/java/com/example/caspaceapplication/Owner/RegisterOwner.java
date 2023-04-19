package com.example.caspaceapplication.Owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.databinding.ActivityRegisterOwnerBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterOwner extends AppCompatActivity {

    ActivityRegisterOwnerBinding binding;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterOwnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        progressDialog = new ProgressDialog(this);

        binding.registerButtonOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String owner_companyname = binding.ownerCompanyName.getText().toString();
                String owner_email = binding.ownerEmail.getText().toString().trim();
                String owner_firstname = binding.ownersFirstName.getText().toString();
                String owner_lastname = binding.ownersLastName.getText().toString();
                String owner_username = binding.ownerUsername.getText().toString();
                String owner_password = binding.ownerPassword.getText().toString();

                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(owner_email, owner_password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                String owner_idNum = currentUser.getUid();

                                // Store the additional user data in the database
                                firebaseFirestore.collection("OwnerUserAccounts")
                                        .document(owner_idNum)
                                        .set(new OwnerRegistrationModel(owner_idNum, owner_companyname, owner_email, owner_firstname, owner_lastname, owner_username, owner_password))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(RegisterOwner.this, "Successfully registered!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterOwner.this, LoginOwner.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(RegisterOwner.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterOwner.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }

        });

    }

}

