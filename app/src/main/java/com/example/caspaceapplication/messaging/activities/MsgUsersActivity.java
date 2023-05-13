package com.example.caspaceapplication.messaging.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.databinding.ActivityMsgUsersBinding;
import com.example.caspaceapplication.messaging.adapters.UsersAdapter;
import com.example.caspaceapplication.messaging.listeners.UserListener;
import com.example.caspaceapplication.messaging.models.UserMdl;
import com.example.caspaceapplication.messaging.utilities.Constants;
import com.example.caspaceapplication.messaging.utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MsgUsersActivity extends AppCompatActivity implements UserListener {

    private ActivityMsgUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMsgUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();

    }

    private void setListeners()
    {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }


    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Query for Customer user type
        Query queryCustomer = database.collection(Constants.KEY_COMBINED_COLLECTION)
                .whereEqualTo(Constants.KEY_COMBINED_ID, userId)
                .whereEqualTo(Constants.KEY_USER_TYPE, "Customer");

        queryCustomer.get().addOnCompleteListener(customerTask -> {
            if (customerTask.isSuccessful() && customerTask.getResult() != null) {
                if (!customerTask.getResult().isEmpty()) {
                    // Current user is of type "Customer"
                    // Query for Owner user type
                    Query queryOwner = database.collection(Constants.KEY_COMBINED_COLLECTION)
                            .whereEqualTo(Constants.KEY_USER_TYPE, "Owner");

                    queryOwner.get().addOnCompleteListener(ownerTask -> {
                        loading(false);
                        if (ownerTask.isSuccessful() && ownerTask.getResult() != null) {
                            List<UserMdl> users = new ArrayList<>();
                            String currentUserId = preferenceManager.getString(Constants.KEY_COMBINED_ID);

                            for (QueryDocumentSnapshot queryDocumentSnapshot : ownerTask.getResult()) {
                                if (currentUserId != null && currentUserId.equals(queryDocumentSnapshot.getId())) {
                                    continue;
                                }
                                UserMdl userMdl = new UserMdl();
                                userMdl.userFirstName = queryDocumentSnapshot.getString(Constants.KEY_COMBINED_FIRST_NAME);
                                userMdl.userLastName = queryDocumentSnapshot.getString(Constants.KEY_COMBINED_LAST_NAME);
                                userMdl.userEmail = queryDocumentSnapshot.getString(Constants.KEY_COMBINED_EMAIL);
                                userMdl.userImage = queryDocumentSnapshot.getString(Constants.KEY_COMBINED_IMAGE);
                                userMdl.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                                userMdl.userCombinedId = queryDocumentSnapshot.getId();
                                users.add(userMdl);
                            }

                            if (users.size() > 0) {
                                UsersAdapter usersAdapter = new UsersAdapter(users, this);
                                binding.usersRecyclerView.setAdapter(usersAdapter);
                                binding.usersRecyclerView.setVisibility(View.VISIBLE);
                            } else {
                                showErrorMessage();
                            }
                        } else {
                            showErrorMessage();
                        }
                    });
                }
            }
        });

        // Query for Owner user type
        Query queryOwner = database.collection(Constants.KEY_COMBINED_COLLECTION)
                .whereEqualTo(Constants.KEY_COMBINED_ID, userId)
                .whereEqualTo(Constants.KEY_USER_TYPE, "Owner");

        queryOwner.get().addOnCompleteListener(ownerTask -> {
            if (ownerTask.isSuccessful() && ownerTask.getResult() != null) {
                if (!ownerTask.getResult().isEmpty()) {
                    // Current user is of type "Owner"
                    // Query for Customer user type
                    Query queryCustomer2 = database.collection(Constants.KEY_COMBINED_COLLECTION)
                            .whereEqualTo(Constants.KEY_USER_TYPE, "Customer");

                    queryCustomer2.get().addOnCompleteListener(customerTask -> {
                        loading(false);
                        if (customerTask.isSuccessful() && customerTask.getResult() != null) {
                            List<UserMdl> users = new ArrayList<>();
                            String currentUserId = preferenceManager.getString(Constants.KEY_COMBINED_ID);

                            for (QueryDocumentSnapshot queryDocumentSnapshot : customerTask.getResult()) {
                                if (currentUserId != null && currentUserId.equals(queryDocumentSnapshot.getId())) {
                                    continue;
                                }
                                UserMdl userMdl = new UserMdl();
                                userMdl.userFirstName = queryDocumentSnapshot.getString(Constants.KEY_COMBINED_FIRST_NAME);
                                userMdl.userLastName = queryDocumentSnapshot.getString(Constants.KEY_COMBINED_LAST_NAME);
                                userMdl.userEmail = queryDocumentSnapshot.getString(Constants.KEY_COMBINED_EMAIL);
                                userMdl.userImage = queryDocumentSnapshot.getString(Constants.KEY_COMBINED_IMAGE);
                                userMdl.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                                userMdl.userCombinedId = queryDocumentSnapshot.getId();
                                users.add(userMdl);
                            }

                            if (users.size() > 0) {
                                UsersAdapter usersAdapter = new UsersAdapter(users, this);
                                binding.usersRecyclerView.setAdapter(usersAdapter);
                                binding.usersRecyclerView.setVisibility(View.VISIBLE);
                            } else {
                                showErrorMessage();
                            }
                        } else {
                            showErrorMessage();
                        }
                    });
                }
            }
        });
    }
    private void showErrorMessage()
    {
        binding.textErrorMessage.setText(String.format("%s", "No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading)
    {
        if(isLoading)
        {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void onUserClicked(UserMdl user) {
        Intent intent = new Intent(getApplicationContext(), MsgChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}