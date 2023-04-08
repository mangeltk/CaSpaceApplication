package com.example.caspaceapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.caspaceapplication.databinding.ActivityRegisterCustomerBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterCustomer extends AppCompatActivity {

    ActivityRegisterCustomerBinding binding;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterCustomerBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        binding.registerButtonCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.customerEmail.getText().toString().trim();
                String firstname = binding.customerFirstName.getText().toString();
                String lastname = binding.customerLastName.getText().toString();
                String username = binding.customerUsername.getText().toString();
                String password = binding.customerPassword.getText().toString();

                //progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(RegisterCustomer.this,LoginCustomer.class));
                                progressDialog.cancel();

                                firebaseFirestore.collection("User")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .set(new CustomerModel(email,firstname,lastname,username,password));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(RegisterCustomer.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });



    }
}