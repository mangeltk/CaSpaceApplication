package com.example.caspaceapplication.customer;

import android.app.ProgressDialog;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.AllUserAccountsModelClass;
import com.example.caspaceapplication.databinding.ActivityRegisterCustomerBinding;
import com.example.caspaceapplication.databinding.ActivityRegisterOwnerBinding;
import com.example.caspaceapplication.messaging.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RegisterCustomer extends AppCompatActivity {

    ActivityRegisterCustomerBinding binding;
    private String encodedImage;
    private PreferenceManager preferenceManager;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();

    }

    private void setListeners()
    {
        binding.textSignIn.setOnClickListener(v -> onBackPressed());

        binding.registerButtonCustomer.setOnClickListener(v ->
        {
            if (isValidSignUpDetails()) {
                registerCustomer();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void registerCustomer() {

                String customer_email = binding.customerEmail.getText().toString().trim();
                String customer_firstName = binding.customerFirstName.getText().toString();
                String customer_lastName = binding.customerLastName.getText().toString();
                String customer_username = binding.customerUsername.getText().toString();
                String customer_password = binding.customerPassword.getText().toString();
                String customer_organization = binding.customerOrganization.getText().toString();
                String customer_population = binding.customerPopulation.getText().toString();
                String customer_image = String.valueOf(Uri.parse(encodedImage));
                String customer_account_status = "Enabled";
                String userType = "Customer";

                loading(true);

                firebaseAuth.createUserWithEmailAndPassword(customer_email, customer_password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                String usercombinedId = currentUser.getUid();

                                // Store the additional user data in the database

                                firebaseFirestore.collection("CustomerUserAccounts")
                                        .document(usercombinedId)
                                        .set(new CustomerRegistrationModel(usercombinedId, customer_email, customer_firstName, customer_lastName, customer_username,
                                                customer_password, customer_organization, customer_population, customer_account_status, userType, customer_image))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                loading(false);
                                                Toast.makeText(RegisterCustomer.this, "Successfully registered!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterCustomer.this, LoginCustomerTrial.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                loading(false);
                                                Toast.makeText(RegisterCustomer.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                firebaseFirestore.collection("UserAccounts").document(usercombinedId)
                                        .set(new AllUserAccountsModelClass(usercombinedId, customer_firstName, customer_lastName, customer_email, customer_image, userType))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loading(false);
                                Toast.makeText(RegisterCustomer.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

    private String encodedImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                            encodedImage = encodedImage(bitmap);
                        } catch (FileNotFoundException e) {
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

