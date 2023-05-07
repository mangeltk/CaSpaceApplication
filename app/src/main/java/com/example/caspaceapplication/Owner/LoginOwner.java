package com.example.caspaceapplication.Owner;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginOwner extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ProgressDialog progressDialog;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    TextView forgotPassword; //todo:forgot password
    private EditText ownerEmail, ownerPassword;
    private Button loginButton;
    private CheckBox rememberMeCheckbox;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_owner);

        progressDialog = new ProgressDialog(this);
        ownerEmail = findViewById(R.id.login_ownerEmail);
        ownerPassword = findViewById(R.id.login_ownerPassword);
        rememberMeCheckbox = findViewById(R.id.rememberme_ownerloginCheckbox);
        loginButton = findViewById(R.id.loginButton_owner);

        setRememberMeCheckbox();//remember me checkbox

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    public void loginUser(){
        String email = ownerEmail.getText().toString().trim();
        String password = ownerPassword.getText().toString().trim();

        if (email.isEmpty()) {
            ownerEmail.setError("Please enter email");
            ownerEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            ownerPassword.setError("Please enter password");
            ownerPassword.requestFocus();
            return;
        }
        if (rememberMeCheckbox.isChecked()) {
            // If the remember me checkbox is checked, save the user's email and password in shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_EMAIL, email);
            editor.putString(KEY_PASSWORD, password);
            editor.apply();
            Toast.makeText(LoginOwner.this, "Credentials saved!", Toast.LENGTH_SHORT).show();
        } else {
            // If the remember me checkbox is not checked, clear the email and password saved in shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(KEY_EMAIL);
            editor.remove(KEY_PASSWORD);
            editor.apply();
        }

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user.isEmailVerified()) {
                            progressDialog.setMessage("Logging in...");
                            progressDialog.show();
                            checkExistingBranch();
                        } else {
                            progressDialog.cancel();
                            Toast.makeText(LoginOwner.this, "Please check and verify email.", Toast.LENGTH_SHORT).show();
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

    public void checkExistingBranch(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Check if the user has registered their store information before
        CollectionReference branchesRef = firebaseFirestore.collection("CospaceBranches");
        Query queryBranch = branchesRef.whereEqualTo("owner_id", user.getUid());
        queryBranch.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot document = task.getResult();
                    if (document!=null && !document.isEmpty()){
                        // Redirect to homepage
                        startActivity(new Intent(LoginOwner.this, OwnerHomepage.class));
                    } else{
                        startActivity(new Intent(LoginOwner.this, RegisterOwner_SpaceBranch.class));
                    }
                }else{
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                progressDialog.cancel();
            }
        });
    }


    public void setRememberMeCheckbox(){
        // Get the email and password saved in shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String password = sharedPreferences.getString(KEY_PASSWORD, "");

        if (!email.isEmpty() && !password.isEmpty()){
            progressDialog.setMessage("Logging in...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user.isEmailVerified()) {
                                checkExistingBranch();
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
    }
}
