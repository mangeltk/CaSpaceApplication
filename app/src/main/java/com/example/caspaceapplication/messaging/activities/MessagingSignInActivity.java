package com.example.caspaceapplication.messaging.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.caspaceapplication.customer.Customer_Homepage_BottomNav;
import com.example.caspaceapplication.customer.Front;
import com.example.caspaceapplication.customer.RegisterCustomer;
import com.example.caspaceapplication.databinding.ActivityMessagingSignInBinding;
import com.example.caspaceapplication.messaging.utilities.Constants;
import com.example.caspaceapplication.messaging.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MessagingSignInActivity extends AppCompatActivity {

    private ActivityMessagingSignInBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN))
        {
            Intent intent = new Intent(getApplicationContext(), Customer_Homepage_BottomNav.class);
            startActivity(intent);
            finish();
        }
        binding = com.example.caspaceapplication.databinding.ActivityMessagingSignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners()
    {
        //Making new account
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), RegisterCustomer.class)));

        binding.switchacc.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), Front.class)));
        //Logging in
        binding.buttonSignIn.setOnClickListener(v -> {
            if(isValidSignInDetails()) //method
            {
                signIn(); //method
            }
        });
    }

    private void signIn() {
        loading(true); //loading is a method
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_USER_COLLECTION)
                .whereEqualTo(Constants.KEY_EMAIL, binding.customersEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.customersPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_FIRST_NAME, documentSnapshot.getString(Constants.KEY_FIRST_NAME));
                        preferenceManager.putString(Constants.KEY_LAST_NAME, documentSnapshot.getString(Constants.KEY_LAST_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        /*Intent intent = new Intent(getApplicationContext(), Homepage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/
                        startActivity(new Intent(MessagingSignInActivity.this, Customer_Homepage_BottomNav.class));
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    } else {
                        loading(false);
                        showToast("Unable to sign in");
                    }
                });
    }

    private void loading(Boolean isLoading)
    {
        if(isLoading)
        {
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    //this method is called in setListeners()
    private Boolean isValidSignInDetails()
    {
        if(binding.customersEmail.getText().toString().trim().isEmpty())
        {
            showToast("Enter email");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.customersEmail.getText().toString()).matches())
        {
            showToast("Enter valid email");
            return false;
        }
        else if(binding.customersPassword.getText().toString().trim().isEmpty())
        {
            showToast("Enter password");
            return false;
        }
        else
        {
            return true;
        }
    }

}