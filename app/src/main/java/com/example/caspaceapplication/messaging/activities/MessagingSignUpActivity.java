package com.example.caspaceapplication.messaging.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.caspaceapplication.databinding.ActivityMessagingSignUpBinding;
import com.example.caspaceapplication.messaging.utilities.Constants;
import com.example.caspaceapplication.messaging.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class MessagingSignUpActivity extends AppCompatActivity {

    private ActivityMessagingSignUpBinding binding;
    private String encodedImage;
    private PreferenceManager preferenceManager;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessagingSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }

    private void setListeners()
    {
        //Making new account
        binding.textSignIn.setOnClickListener(v -> onBackPressed());

        binding.registerButtonCustomer.setOnClickListener(v ->
        {
            if (isValidSignUpDetails()) {
                signUp();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signUp() {
        String customer_email = binding.customerEmail.getText().toString().trim();
        String customer_password = binding.customerPassword.getText().toString();
        String customer_account_status = "Enabled";

        //User Authentication
        firebaseAuth.createUserWithEmailAndPassword(customer_email, customer_password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        loading(true);
                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                        HashMap<String, Object> MessagingUser = new HashMap<>();
                        //String customer_idNum = currentUser.getUid();


                        MessagingUser.put(Constants.KEY_FIRST_NAME, binding.customerFirstName.getText().toString());
                        MessagingUser.put(Constants.KEY_LAST_NAME, binding.customerLastName.getText().toString());
                        MessagingUser.put(Constants.KEY_USERNAME, binding.customerUsername.getText().toString());
                        MessagingUser.put(Constants.KEY_ORGANIZATION, binding.customerOrganization.getText().toString());
                        MessagingUser.put(Constants.KEY_POPULATION, binding.customerPopulation.getText().toString());
                        MessagingUser.put(Constants.KEY_EMAIL, binding.customerEmail.getText().toString());
                        MessagingUser.put(Constants.KEY_PASSWORD, binding.customerPassword.getText().toString());
                        //MessagingUser.put(Constants.KEY_COMPANY, binding.customerPassword.getText().toString());


                        MessagingUser.put(Constants.KEY_IMAGE, encodedImage);
                        database.collection(Constants.KEY_USER_COLLECTION)
                                .add(MessagingUser)
                                .addOnSuccessListener(documentReference -> {
                                    loading(false);
                                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                                    preferenceManager.putString(Constants.KEY_FIRST_NAME, binding.customerFirstName.getText().toString());
                                    preferenceManager.putString(Constants.KEY_LAST_NAME, binding.customerLastName.getText().toString());
                                    /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);*/
                                    startActivity(new Intent(MessagingSignUpActivity.this, MessagingSignInActivity.class));
                                })
                                .addOnFailureListener(exception -> {
                                    loading(false);
                                    showToast(exception.getMessage());
                                });
                    }
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    showToast(exception.getMessage());
                });

    }

    private String encodedImage(Bitmap bitmap)
    {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[]bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.registerButtonCustomer.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.registerButtonCustomer.setVisibility(View.VISIBLE);
        }
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK)
                {
                    if(result.getData() != null)
                    {
                        Uri imageUri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                            encodedImage = encodedImage(bitmap);
                        }
                        catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private Boolean isValidSignUpDetails() {
        if (encodedImage == null) {
            showToast("Select profile image");
            return false;
        } else if (binding.customerFirstName.getText().toString().trim().isEmpty()) {
            showToast("Enter first name");
            return false;
        } else if (binding.customerLastName.getText().toString().trim().isEmpty()) {
            showToast("Enter last name");
            return false;
        } else if (binding.customerUsername.getText().toString().trim().isEmpty()) {
            showToast("Enter username");
            return false;
        } else if (binding.customerEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.customerEmail.getText().toString()).matches()) {
            showToast("Enter valid email address");
            return false;
        } else if (binding.customerPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else if (binding.confirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Confirm your password");
            return false;
        } else if (!binding.customerPassword.getText().toString().equals(binding.confirmPassword.getText().toString())) {
            showToast("Password must be the same");
            return false;
        } else {
            return true;
        }
    }




}
