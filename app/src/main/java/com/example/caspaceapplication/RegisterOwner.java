package com.example.caspaceapplication;

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
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterOwner extends AppCompatActivity {

    ActivityRegisterOwnerBinding binding;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivityRegisterOwnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        binding.registerButtonOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String owner_email = binding.ownerEmail.getText().toString().trim();
                String owner_fullname = binding.ownersFullName.getText().toString();
                String owner_CompanyName = binding.ownerCompanyName.getText().toString();
                String owner_idNum = binding.ownerIdnum.getText().toString();
                String owner_password = binding.ownerPassword.getText().toString();

                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(owner_email, owner_password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(RegisterOwner.this,LoginOwner.class));
                                progressDialog.cancel();

                                firebaseFirestore.collection("OwnerUser")
                                        .document("OwnerID")
                                        .set(new OwnerRegistrationModel(owner_email,owner_fullname,owner_CompanyName,owner_idNum,owner_password));

                                Toast.makeText(RegisterOwner.this, "Successfully registered! Please check email", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(RegisterOwner.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}