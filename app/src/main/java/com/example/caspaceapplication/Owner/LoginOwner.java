package com.example.caspaceapplication.Owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.databinding.ActivityLoginOwnerBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginOwner extends AppCompatActivity {

    ActivityLoginOwnerBinding binding;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginOwnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString().trim();

                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user.isEmailVerified()) {
                                    progressDialog.cancel();
                                    Toast.makeText(LoginOwner.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginOwner.this, OwnerHomepage.class));
                                } else {
                                    user.sendEmailVerification()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.cancel();
                                                    Toast.makeText(LoginOwner.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.cancel();
                                                    Toast.makeText(LoginOwner.this, "Hoy Sayop!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });
            }
        });

        /*forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginOwner.this, ForgotPassword.class);
                startActivity(intent);
            }
        });*/
    }
}