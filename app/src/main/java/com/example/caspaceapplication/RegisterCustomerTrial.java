package com.example.caspaceapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterCustomerTrial extends AppCompatActivity {

    TextView forgotPassword;
    EditText customer_email, customer_password;
    Button registerButtonCustomer;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer_trial);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        forgotPassword = findViewById(R.id.forgotPassword);
        customer_email = findViewById(R.id.customer_email);
        customer_password = findViewById(R.id.customer_password);
        registerButtonCustomer = findViewById(R.id.registerButtonCustomer);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterCustomerTrial.this, HomepageCustomer.class));
            }
        });

        registerButtonCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforAuth();
            }
        });
    }

        private void PerforAuth () {
            String email = customer_email.getText().toString();
            String password = customer_password.getText().toString();
            //String email = customer_email.getText().toString();

            if (!email.matches(emailPattern)) {
                customer_email.setError("Enter correct Email");
            } else if (password.isEmpty() || password.length() < 6) {
                customer_password.setError("Enter correct password");
            } else {
                progressDialog.setMessage("Please wait for registration...");
                progressDialog.setTitle("Registration");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    sendUserToNextActivity();
                                    Toast.makeText(RegisterCustomerTrial.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterCustomerTrial.this, "" + task.getException(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }

        }

        private void sendUserToNextActivity ()
        {
            Intent intent = new Intent(RegisterCustomerTrial.this, HomepageCustomer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
