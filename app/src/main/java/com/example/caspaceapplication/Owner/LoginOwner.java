package com.example.caspaceapplication.Owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginOwner extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ProgressDialog progressDialog;

    TextView forgotPassword;
    private EditText ownerEmail, ownerPassword;
    private Button loginButton;
    private CheckBox rememberMeCheckbox;
    private SharedPreferences sharedPreferences;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_owner);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        ownerEmail = findViewById(R.id.login_ownerEmail);
        ownerPassword = findViewById(R.id.login_ownerPassword);
        rememberMeCheckbox = findViewById(R.id.rememberme_ownerloginCheckbox);
        loginButton = findViewById(R.id.loginButton_owner);

        setRememberMeCheckbox();//remember me checkbox

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = ownerEmail.getText().toString().trim();
                String password = ownerPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    ownerEmail.setError("Please enter username");
                    ownerEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    ownerPassword.setError("Please enter password");
                    ownerPassword.requestFocus();
                    return;
                }

                if (rememberMeCheckbox.isChecked()) {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_EMAIL, email);
                    editor.putString(KEY_PASSWORD, password);
                    editor.apply();
                }



                DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("OwnerUserAccounts");
                Query query = dataRef.orderByChild("ownerUsername").equalTo(email);

                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user.isEmailVerified()) {
                                    progressDialog.setMessage("Logging in...");
                                    progressDialog.show();

                                    // Save the user's email and password in shared preferences
                                    sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_EMAIL, email);
                                    editor.putString(KEY_PASSWORD, password);
                                    editor.apply();

                                    // Check if the user has registered their store information before
                                    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("CospaceBranches");
                                    Query branchQuery = dataRef.orderByChild("ownerEmail").equalTo(email);
                                    //todo:still redirect user to create branchname
                                    branchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {

                                                // Redirect to homepage
                                                Toast.makeText(LoginOwner.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LoginOwner.this, OwnerHomepage.class));
                                            } else{
                                                startActivity(new Intent(LoginOwner.this, RegisterOwner_SpaceBranch.class));
                                            }
                                            progressDialog.cancel();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                    Toast.makeText(LoginOwner.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginOwner.this, OwnerHomepage.class));
                                } else {
                                    user.sendEmailVerification()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.cancel();
                                                    Toast.makeText(LoginOwner.this, "Please check and verify email.", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.cancel();
                                                    Toast.makeText(LoginOwner.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(LoginOwner.this, "Failed to log in. No user registered!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginOwner.this, RegisterOwner.class));
                            }
                        });
            }
        });

    }

    public void setRememberMeCheckbox(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String password = sharedPreferences.getString(KEY_PASSWORD, "");
        if (!email.isEmpty() && !password.isEmpty()){
            progressDialog.setMessage("Logging in...");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(LoginOwner.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginOwner.this, OwnerHomepage.class));
                            progressDialog.cancel();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.cancel();
                            Toast.makeText(LoginOwner.this, "Failed to log in. No user registered!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginOwner.this, RegisterOwner.class));
                        }
                    });
        }
    }
}
