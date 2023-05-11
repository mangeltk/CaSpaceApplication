package com.example.caspaceapplication.messaging.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.caspaceapplication.databinding.ActivityMessagingUsersBinding;
import com.example.caspaceapplication.messaging.adapters.MessagingUsersAdapter;
import com.example.caspaceapplication.messaging.listeners.UserListener;
import com.example.caspaceapplication.messaging.models.UserMdl;
import com.example.caspaceapplication.messaging.utilities.Constants;
import com.example.caspaceapplication.messaging.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class MessagingUsersActivity extends MessagingBaseActivity implements UserListener {

    private ActivityMessagingUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessagingUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();
    }

    private void setListeners()
    {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void getUsers()
    {
        /*FirebaseFirestore database = FirebaseFirestore.getInstance();
        loading(true);

        database.collection(Constants.KEY_USER_COLLECTION).get().addOnCompleteListener(task -> {
            loading(false);
            if (task.isSuccessful() && task.getResult() != null) {
                List<UserMdl> users = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    UserMdl userMdl = new UserMdl();
                    userMdl.userFirstName = queryDocumentSnapshot.getString(Constants.KEY_FIRST_NAME);
                    userMdl.userEmail = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                    userMdl.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                    userMdl.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);

                    database.collection(Constants.KEY_USER_COLLECTION).get().addOnCompleteListener(task1 -> {
                        if (task.isSuccessful() && task.getResult()!=null){
                            for (QueryDocumentSnapshot queryDocumentSnapshot1 : task.getResult()){
                                userMdl.userIDNum = queryDocumentSnapshot1.getString(Constants.KEY_USER_ID);
                            }
                        }

                    });
                    userMdl.userIDNum = queryDocumentSnapshot.getId();
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
        });*/

        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_USER_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null)
                    {
                        List<UserMdl> users = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                        {
                            if(currentUserId.equals(queryDocumentSnapshot.getId()))
                            {
                                continue;
                            }
                            UserMdl userMdl = new UserMdl();
                            userMdl.userFirstName = queryDocumentSnapshot.getString(Constants.KEY_FIRST_NAME);
                            userMdl.userEmail = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            userMdl.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            userMdl.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            userMdl.userIDNum = queryDocumentSnapshot.getId();
                            users.add(userMdl);
                        }
                        if(users.size() > 0)
                        {
                            MessagingUsersAdapter usersAdapter = new MessagingUsersAdapter(users, this);
                            binding.usersRecyclerView.setAdapter(usersAdapter);
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            showErrorMessage();
                        }
                    }
                    else
                    {
                        showErrorMessage();
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
        Intent intent = new Intent(getApplicationContext(), MessagingChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}