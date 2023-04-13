package com.example.caspaceapplication.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginCustomerTrial extends AppCompatActivity {

    TextView forgotPassword;
    EditText email, password;
    Button loginButton;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_customer_trial);
        //loginButton = findViewById(R.id.loginButton);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        email = findViewById(R.id.email);
        forgotPassword = findViewById(R.id.forgotPassword);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        TextView forgotPassword = findViewById(R.id.forgotPassword);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginCustomerTrial.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                PerforLogin();
            }
        });
    }
    private void PerforLogin () {
        String inputPassword = password.getText().toString();
        String inputEmail = email.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(inputEmail,inputPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user.isEmailVerified()){
                            progressDialog.cancel();
                            Toast.makeText(LoginCustomerTrial.this, "Logged in", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(LoginCustomerTrial.this, HomepageCustomer.class));
                            sendUserToNextActivity();
                        }else{
                            user.sendEmailVerification()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressDialog.cancel();
                                            Toast.makeText(LoginCustomerTrial.this, "Please verify email", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.cancel();
                                            Toast.makeText(LoginCustomerTrial.this, "No user registered", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(LoginCustomerTrial.this, "No customer user registered!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginCustomerTrial.this, RegisterCustomer.class));
                    }
                });

    }

    private void sendUserToNextActivity ()
    {
        Intent intent = new Intent(LoginCustomerTrial.this, Customer_Homepage_BottomNav.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        //Intent intent = new Intent(getActivity().getApplication(), EditProfileActivity.class);
    }
}