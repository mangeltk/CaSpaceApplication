package com.example.caspaceapplication.customer;

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
import com.example.caspaceapplication.databinding.ActivityLoginCustomerTrialBinding;
import com.example.caspaceapplication.messaging.Constants;
import com.example.caspaceapplication.messaging.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class LoginCustomerTrial extends AppCompatActivity  {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    TextView forgotPassword;
    private EditText customerEmail, customerPassword;
    private Button loginButton;
    private CheckBox rememberMeCheckbox;
    private SharedPreferences sharedPreferences;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private ActivityLoginCustomerTrialBinding binding;
    private PreferenceManager preferenceManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivityLoginCustomerTrialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        customerEmail = findViewById(R.id.customer_email);
        customerPassword = findViewById(R.id.customer_password);
        rememberMeCheckbox = findViewById(R.id.rememberMe_customerloginCheckbox);
        loginButton = findViewById(R.id.loginButton_customer);

        setListeners();
        setRememberMeCheckbox(); //remember me checkbox

    }

    private void setListeners()
    {
        binding.textCreateNewAccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginCustomerTrial.this, FrontRegister.class));
        });
        //when the user clicks the sign in button for customer
        binding.loginButtonCustomer.setOnClickListener( v ->
        {
            if(isValidLogInDetails())
            {
                loginCustomer();
            }
        });
    }

    private void loginCustomer()
    {
        String email = customerEmail.getText().toString().trim();
        String password = customerPassword.getText().toString().trim();

        if (email.isEmpty()) {
            customerEmail.setError("Please enter email");
            customerEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            binding.customerPassword.setError("Please enter password");
            binding.customerPassword.requestFocus();
            return;
        }

        if (binding.rememberMeCustomerloginCheckbox.isChecked()) {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_EMAIL, email);
            editor.putString(KEY_PASSWORD, password);
            editor.apply();
            showToast("Credentials saved!");
        }
        else {
            //If the remember me checkbox is not checked, clear the email and password saved in shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(KEY_EMAIL);
            editor.remove(KEY_PASSWORD);
            editor.apply();
        }


        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user.isEmailVerified()) {
                            progressDialog.setMessage("Logging in...");
                            progressDialog.show();

                            firebaseFirestore.collection("UserAccounts").document(user.getUid())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()){
                                                String userRole = documentSnapshot.getString("userType");

                                                if (userRole.equals("Customer")){
                                                    signIn();
                                                    Toast.makeText(LoginCustomerTrial.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                                    updateCustomerFCMToken();
                                                    customerUserActivity();
                                                }else {
                                                    Toast.makeText(LoginCustomerTrial.this, "No customer registered on this account credentials.", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), LoginCustomerTrial.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                }

                                            }
                                        }
                                    });


                        }
                        else {
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.cancel();
                                    Toast.makeText(LoginCustomerTrial.this, "Please check and verify email.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    Toast.makeText(LoginCustomerTrial.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(LoginCustomerTrial.this, "Failed to log in. No user registered!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginCustomerTrial.this, RegisterCustomer.class));
                    }
                });
    }

    private void signIn()
    {
        /*FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COMBINED_COLLECTION)

                .get()
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0)
                    {

                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_COMBINED_ID, documentSnapshot.getString(Constants.KEY_COMBINED_ID));
                        preferenceManager.putString(Constants.KEY_COMBINED_FIRST_NAME, documentSnapshot.getString(Constants.KEY_COMBINED_FIRST_NAME));
                        preferenceManager.putString(Constants.KEY_COMBINED_LAST_NAME, documentSnapshot.getString(Constants.KEY_COMBINED_LAST_NAME));
                        preferenceManager.putString(Constants.KEY_COMBINED_IMAGE, documentSnapshot.getString(Constants.KEY_COMBINED_IMAGE));
                        //Toast.makeText(this, "Firstname"+documentSnapshot.getString(Constants.KEY_COMBINED_FIRST_NAME), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Customer_Homepage_BottomNav.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                });*/

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // User is logged in
            String uid = user.getUid();


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference documentRef = db.collection(Constants.KEY_COMBINED_COLLECTION).document(uid);

            // Retrieve a specific column (field) from the document
            documentRef.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // The document exists
                            // Retrieve the specific column (field)
                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                            preferenceManager.putString(Constants.KEY_COMBINED_ID, documentSnapshot.getString(Constants.KEY_COMBINED_ID));
                            preferenceManager.putString(Constants.KEY_COMBINED_FIRST_NAME, documentSnapshot.getString(Constants.KEY_COMBINED_FIRST_NAME));
                            //preferenceManager.putString(Constants.KEY_COMBINED_LAST_NAME, documentSnapshot.getString(Constants.KEY_COMBINED_LAST_NAME));
                            preferenceManager.putString(Constants.KEY_COMBINED_IMAGE, documentSnapshot.getString(Constants.KEY_COMBINED_IMAGE));
                            Intent intent = new Intent(getApplicationContext(), Customer_Homepage_BottomNav.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            // The document does not exist
                            System.out.println("Document does not exist.");
                        }
                    })
                    .addOnFailureListener(e -> {
                                // Error retrieving the document
                                // Handle the error appropriately
                            }
                    );
        }
    }

    private void loading(Boolean isLoading)
    {
        if(isLoading)
        {
            binding.loginButtonCustomer.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.loginButtonCustomer.setVisibility(View.VISIBLE);
        }
    }

    private boolean isValidLogInDetails()
    {
        if(binding.customerEmail.getText().toString().trim().isEmpty())
        {
            showToast("Enter email");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.customerEmail.getText().toString()).matches())
        {
            showToast("Enter valid email");
            return false;
        }
        else if(binding.customerPassword.getText().toString().trim().isEmpty())
        {
            showToast("Enter password");
            return false;
        }
        else
        {
            return true;
        }
    }

    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateCustomerFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String token = task.getResult();
                    String customersIDNum = firebaseAuth.getCurrentUser().getUid();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("CustomerUserAccounts").document(customersIDNum)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String existingToken = documentSnapshot.getString("fcmToken");
                                    if (!TextUtils.isEmpty(existingToken) && existingToken.equals(token)) {
                                        Log.d(TAG, "FCM token already exists in database");
                                        return;
                                    }
                                }
                                db.collection("CustomerUserAccounts").document(customersIDNum)
                                        .update("fcmToken", token)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d(TAG, "FCM token updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Error updating FCM token", e);
                                        });
                                db.collection("UserAccounts").document(customersIDNum)
                                        .update("fcmToken", token)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d(TAG, "FCM token added to AnotherCollection successfully for customer");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Error adding FCM token to AnotherCollection for customer", e);
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error getting FCM token for owner", e);
                            });
                });
    }

    public void customerUserActivity(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String customerId = firebaseAuth.getCurrentUser().getUid();
        String activity = "Login";

        Map<String, Object> data = new HashMap<>();
        data.put("customerId",customerId);
        data.put("activity", activity);
        data.put("dateTime", Timestamp.now());

        db.collection("CustomerActivity")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Activity Stored.");
                    }
                });


    }
    public void setRememberMeCheckbox()
    {

        //Get the email and password saved in shared preferences
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
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user.isEmailVerified()) {
                                startActivity(new Intent(LoginCustomerTrial.this, Customer_Homepage_BottomNav.class));
                            } else{
                                user.sendEmailVerification()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.cancel();
                                                Toast.makeText(LoginCustomerTrial.this, "Please check and verify email.", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.cancel();
                                                Toast.makeText(LoginCustomerTrial.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                            customerUserActivity();
                        }
                    });
        }
    }
}