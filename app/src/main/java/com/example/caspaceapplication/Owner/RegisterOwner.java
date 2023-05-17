package com.example.caspaceapplication.Owner;

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
import com.example.caspaceapplication.customer.Front;
import com.example.caspaceapplication.customer.RegisterCustomer;
import com.example.caspaceapplication.databinding.ActivityRegisterCustomerBinding;
import com.example.caspaceapplication.databinding.ActivityRegisterOwnerBinding;
import com.example.caspaceapplication.messaging.PreferenceManager;
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

public class RegisterOwner extends AppCompatActivity {

    ActivityRegisterOwnerBinding binding;
    private String encodedImage;
    private PreferenceManager preferenceManager;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterOwnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();


    }
    private void setListeners()
    {
        binding.textSignIn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterOwner.this, Front.class));
        });

        binding.registerButtonOwner.setOnClickListener(v ->
        {
            if (isValidSignUpDetails()) {
                registerOwner();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void registerOwner() {
        String owner_companyname = binding.ownersCompanyName.getText().toString();
        String owner_email = binding.ownersEmail.getText().toString().trim();
        String owner_firstname = binding.ownersFirstName.getText().toString();
        String owner_lastname = binding.ownersLastName.getText().toString();
        String owner_username = binding.ownersUsername.getText().toString();
        String owner_password = binding.ownersPassword.getText().toString();
        String owner_image = String.valueOf(Uri.parse(encodedImage));
        String owner_account_status="Enabled";
        String userType="Owner";

        loading(true);

        firebaseAuth.createUserWithEmailAndPassword(owner_email, owner_password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        String userId = currentUser.getUid();

                        // Store the additional user data in the database
                        firebaseFirestore.collection("OwnerUserAccounts")
                                .document(userId)
                                .set(new OwnerRegistrationModel(userId, owner_companyname, owner_email, owner_firstname, owner_lastname, owner_username, owner_password,owner_account_status,userType))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        loading(false);
                                        Toast.makeText(RegisterOwner.this, "Successfully registered!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterOwner.this, LoginOwner.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loading(false);
                                        Toast.makeText(RegisterOwner.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                        firebaseFirestore.collection("UserAccounts").document(userId)
                                .set(new AllUserAccountsModelClass(userId, owner_firstname, owner_lastname, owner_email, owner_image, userType))
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
                        Toast.makeText(RegisterOwner.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.registerButtonOwner.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.registerButtonOwner.setVisibility(View.VISIBLE);
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
                            binding.ownerImageProfile.setImageBitmap(bitmap);
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
        } else if (binding.ownersFirstName.getText().toString().trim().isEmpty()) {
            showToast("Enter first name");
            return false;
        } else if (binding.ownersLastName.getText().toString().trim().isEmpty()) {
            showToast("Enter last name");
            return false;
        } else if (binding.ownersCompanyName.getText().toString().trim().isEmpty()) {
            showToast("Enter company name");
            return false;
        } else if (binding.ownersUsername.getText().toString().trim().isEmpty()) {
            showToast("Enter username");
            return false;
        } else if (binding.ownersEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.ownersEmail.getText().toString()).matches()) {
            showToast("Enter valid email address");
            return false;
        } else if (binding.ownersPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else if (binding.ownersConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Confirm your password");
            return false;
        } else if (!binding.ownersPassword.getText().toString().equals(binding.ownersConfirmPassword.getText().toString())) {
            showToast("Password must be the same");
            return false;
        } else {
            return true;
        }
    }

}
