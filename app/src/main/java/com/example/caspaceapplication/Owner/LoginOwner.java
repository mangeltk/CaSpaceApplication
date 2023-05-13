package com.example.caspaceapplication.Owner;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.Customer_Homepage_BottomNav;
import com.example.caspaceapplication.databinding.ActivityLoginCustomerTrialBinding;
import com.example.caspaceapplication.databinding.ActivityLoginOwnerBinding;
import com.example.caspaceapplication.messaging.utilities.Constants;
import com.example.caspaceapplication.messaging.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginOwner extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ProgressDialog progressDialog;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    TextView forgotPassword; //todo:forgot password
    private EditText ownerEmail, ownerPassword;
    private Button loginButtonOwner;
    private CheckBox rememberMeCheckbox;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private ActivityLoginOwnerBinding binding;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivityLoginOwnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        ownerEmail = findViewById(R.id.login_ownerEmail);
        ownerPassword = findViewById(R.id.login_ownerPassword);
        rememberMeCheckbox = findViewById(R.id.rememberme_ownerloginCheckbox);
        loginButtonOwner = findViewById(R.id.loginButton_owner);

        setListeners();
        setRememberMeCheckbox();//remember me checkbox


    }

    private void setListeners()
    {
        //when the user clicks the sign in button for customer
        binding.loginButtonOwner.setOnClickListener( v ->
        {
            if(isValidLogInDetails())
            {
                loginOwner();
            }
        });
    }

    private boolean isValidLogInDetails()
    {
        if(binding.loginOwnerEmail.getText().toString().trim().isEmpty())
        {
            showToast("Enter email");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.loginOwnerEmail.getText().toString()).matches())
        {
            showToast("Enter valid email");
            return false;
        }
        else if(binding.loginOwnerPassword.getText().toString().trim().isEmpty())
        {
            showToast("Enter password");
            return false;
        }
        else
        {
            return true;
        }
    }

    public void loginOwner(){
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

        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_OWNER)
                .whereEqualTo(Constants.KEY_EMAIL, binding.loginOwnerEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.loginOwnerPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user.isEmailVerified()) {
                                        progressDialog.setMessage("Logging in...");
                                        progressDialog.show();
                                        checkExistingBranch();
                                        updateOwnerFCMToken();
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

                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_COMBINED_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_COMBINED_FIRST_NAME, documentSnapshot.getString(Constants.KEY_COMBINED_FIRST_NAME));
                        preferenceManager.putString(Constants.KEY_COMBINED_LAST_NAME, documentSnapshot.getString(Constants.KEY_COMBINED_LAST_NAME));
                        preferenceManager.putString(Constants.KEY_COMBINED_IMAGE, documentSnapshot.getString(Constants.KEY_COMBINED_IMAGE));
                        Intent intent = new Intent(getApplicationContext(), Customer_Homepage_BottomNav.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    else {
                        loading(false);
                        showToast("Unable to sign in");

                    }
                });
    }



    private void updateOwnerFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    String token = task.getResult();

                    String ownerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("OwnerUserAccounts").document(ownerId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String existingToken = documentSnapshot.getString("fcmToken");
                                    if (!TextUtils.isEmpty(existingToken) && existingToken.equals(token)) {
                                        Log.d(TAG, "FCM token already exists in database");
                                        return;
                                    }
                                }

                                db.collection("OwnerUserAccounts").document(ownerId)
                                        .update("fcmToken", token)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d(TAG, "FCM token updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Error updating FCM token", e);
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error getting FCM token for owner", e);
                            });
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

        private void loading(Boolean isLoading)
        {
            if(isLoading)
            {
                binding.loginButtonOwner.setVisibility(View.INVISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);
            }
            else
            {
                binding.progressBar.setVisibility(View.INVISIBLE);
                binding.loginButtonOwner.setVisibility(View.VISIBLE);
            }
        }

        private void showToast(String message)
        {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
