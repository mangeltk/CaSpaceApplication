package com.example.caspaceapplication.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginCustomerTrial extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private ActivityLoginCustomerTrialBinding binding;
    private PreferenceManager preferenceManager;

    TextView forgotPassword;
    private EditText customerEmail, customerPassword;
    private Button loginButton;
    private CheckBox rememberMeCheckbox;
    private SharedPreferences sharedPreferences;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login_customer_trial);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        customerEmail = findViewById(R.id.customer_email);
        customerPassword = findViewById(R.id.customer_password);
        rememberMeCheckbox = findViewById(R.id.rememberMe_customerloginCheckbox);
        loginButton = findViewById(R.id.loginButton_customer);

        //creates a new instance of the PreferenceManager class using the application context,
        //which can be used to access and manage user preferences throughout the entire application.
        //preferenceManager  = new PreferenceManager(getApplicationContext());

        //this code is redirecting the user to the homepage screen if they have already signed in previously.
        /*if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN))
        {
            Intent intent = new Intent(getApplicationContext(), MessagingMain.class);
            startActivity(intent);
            finish();
        }*/
        //this calls in the onCreate() method of the activity to initialize the data binding and set the content view.
        binding = ActivityLoginCustomerTrialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //method


        setRememberMeCheckbox(); //remember me checkbox

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = customerEmail.getText().toString().trim();
                String password = customerPassword.getText().toString().trim();

                //validations
                if (email.isEmpty()) {
                    customerEmail.setError("Please enter email");
                    customerEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    customerPassword.setError("Please enter password");
                    customerPassword.requestFocus();
                    return;
                }

                //remember me
                if (rememberMeCheckbox.isChecked()) {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_EMAIL, email);
                    editor.putString(KEY_PASSWORD, password);
                    editor.apply();
                    Toast.makeText(LoginCustomerTrial.this, "Credentials saved!", Toast.LENGTH_SHORT).show();
                }
                else {
                    //If the remember me checkbox is not checked, clear the email and password saved in shared preferences
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(KEY_EMAIL);
                    editor.remove(KEY_PASSWORD);
                    editor.apply();
                }

                //retrieve user from the database
                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            //Successful Login with verified email
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user.isEmailVerified()) {
                                    progressDialog.setMessage("Logging in...");
                                    progressDialog.show();
                                    Toast.makeText(LoginCustomerTrial.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginCustomerTrial.this, Customer_Homepage_BottomNav.class));
                                }
                                //Error in logging in. Email not yet verified.
                                else {
                                    //Sending verification in email
                                    user.sendEmailVerification()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.cancel();
                                                    Toast.makeText(LoginCustomerTrial.this, "Please check and verify email.", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            //Error in sending verification email. It must be email is already verified.
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.cancel();
                                                    Toast.makeText(LoginCustomerTrial.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        })
                        //Error in logging in. Credentials is not yet registered.
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(LoginCustomerTrial.this, "Failed to log in. No user registered!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginCustomerTrial.this, RegisterCustomer.class));
                            }
                        });
            }
        });



    }



    //this method is called in setListeners()


    //this method is called in setListeners()
    private Boolean isValidSignInDetails()
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

    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void setRememberMeCheckbox(){
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
                        }
                    });
                    /*.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.cancel();
                            Toast.makeText(LoginCustomerTrial.this, "Failed to log in. No user registered!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginCustomerTrial.this, RegisterCustomer.class));
                        }
                    });*/
        }
    }
}
