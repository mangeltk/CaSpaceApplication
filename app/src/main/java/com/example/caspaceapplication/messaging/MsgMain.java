package com.example.caspaceapplication.messaging;

import static android.content.ContentValues.TAG;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caspaceapplication.R;
import com.example.caspaceapplication.databinding.ActivityMsgMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MsgMain extends MsgBaseActivity implements ConversationListener {

    private ActivityMsgMainBinding binding;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> conversations;
    private RecentConversationsAdapter conversationsAdapter;
    private FirebaseFirestore database;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMsgMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        init();
        loadUserDetails();
        //getToken();
        setListeners();
        listenConversations();

    }

    private void init()
    {
        conversations = new ArrayList<>();
        conversationsAdapter = new RecentConversationsAdapter(conversations, this);
        binding.conversationsRecyclerView.setAdapter(conversationsAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void setListeners() {
        //binding.imageSignOut.setOnClickListener(v -> signOut());
        binding.fabNewChat.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), MsgUsersActivity.class)));
    }



    /*private void loadUserDetails() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String userCombinedId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Query for Customer user type
        Query queryCustomer = database.collection(Constants.KEY_COMBINED_COLLECTION)
                .whereEqualTo(Constants.KEY_COMBINED_ID, userCombinedId)
                .whereEqualTo(Constants.KEY_USER_TYPE, "Customer");

        queryCustomer.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Retrieve user details from the document
                    String textFirstName = document.getString(Constants.KEY_COMBINED_FIRST_NAME);
                    String textLastName = document.getString(Constants.KEY_COMBINED_LAST_NAME);
                    String imageString = document.getString(Constants.KEY_COMBINED_IMAGE);

                    // Load and display user profile image
                    if (imageString != null) {
                        byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        binding.imageProfile.setImageBitmap(bitmap);
                    }

                    // Set user details in the UI
                    //binding.textFirstName.setText(textFirstName + " " + textLastName);
                    binding.textFirstName.setText(textFirstName);
                    binding.textLastName.setText(textLastName);
                }
            } else {
                // Handle case where user details could not be loaded
                Toast.makeText(this, "Failed to load user details", Toast.LENGTH_SHORT).show();
            }
        });

        // Query for Owner user type
        Query queryOwner = database.collection(Constants.KEY_COMBINED_COLLECTION)
                .whereEqualTo(Constants.KEY_COMBINED_ID, userCombinedId)
                .whereEqualTo(Constants.KEY_USER_TYPE, "Owner");

        queryOwner.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Retrieve user details from the document
                    String textFirstName = document.getString(Constants.KEY_COMBINED_FIRST_NAME);
                    String textLastName = document.getString(Constants.KEY_COMBINED_LAST_NAME);
                    String imageString = document.getString(Constants.KEY_COMBINED_IMAGE);

                    // Load and display user profile image
                    if (imageString != null) {
                        byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        binding.imageProfile.setImageBitmap(bitmap);
                    }

                    // Set user details in the UI
                    //binding.textFirstName.setText(user.userFirstName + " " + user.userLastName);
                    binding.textFirstName.setText(textFirstName);

                    binding.textLastName.setText(textLastName);
                }
            } else {
                // Handle case where user details could not be loaded
                Toast.makeText(this, "Failed to load user details", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

/*    private void loadUserDetails() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database.collection("UserAccounts")
                        .document(currentUser).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                binding.textFirstName.setText(preferenceManager.getString(Constants.KEY_COMBINED_FIRST_NAME));
                                binding.textLastName.setText(preferenceManager.getString(Constants.KEY_COMBINED_LAST_NAME));
                                String imageString = preferenceManager.getString(Constants.KEY_COMBINED_IMAGE);
                                if (imageString != null) {
                                    byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    binding.imageProfile.setImageBitmap(bitmap);
                                }
                            }
                        });
    }*/

    private void loadUserDetails() {
        binding.textFirstName.setText(preferenceManager.getString(Constants.KEY_COMBINED_FIRST_NAME));
        //binding.textLastName.setText(preferenceManager.getString(Constants.KEY_COMBINED_LAST_NAME));
        String imageString = preferenceManager.getString(Constants.KEY_COMBINED_IMAGE);


        if (imageString != null) {
          /*byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            binding.imageProfile.setImageBitmap(bitmap);*/

            Picasso.get().load(imageString).into(binding.imageProfile);
        }
    }


    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /*private void getToken()
    {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);

    }*/

    private void listenConversations()
    {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_COMBINED_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_COMBINED_ID))
                .addSnapshotListener(eventListener);
    }

    /*private void updateToken(String token) {
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        String userId = preferenceManager.getString(Constants.KEY_USER_ID);
        if (userId == null || userId.isEmpty()) {
            showToast("User ID is null or empty");
            return;
        }

        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS)
                        .document(userId);
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("Unable to update token"));


    }*/

    /*private void updateToken(String token) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    //String token = task.getResult();
                    String userCombinedId = firebaseAuth.getCurrentUser().getUid();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("UserAccounts").document(userCombinedId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String existingToken = documentSnapshot.getString("fcmToken");
                                    if (!TextUtils.isEmpty(existingToken) && existingToken.equals(token)) {
                                        Log.d(TAG, "FCM token already exists in database");
                                        return;
                                    }
                                }
                                db.collection("UserAccounts").document(userCombinedId)
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
    }*/

    private final EventListener<QuerySnapshot> eventListener = (value, error) ->
    {
        if(error!=null)
        {
            return;
        }
        if(value !=null)
        {
            for (DocumentChange documentChange : value.getDocumentChanges())
            {
                if(documentChange.getType() == DocumentChange.Type.ADDED)
                {
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = senderId;
                    chatMessage.receiverId = receiverId;
                    if(preferenceManager.getString(Constants.KEY_COMBINED_ID).equals(senderId))
                    {
                        chatMessage.conversationImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                        chatMessage.conversationName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                        chatMessage.conversationId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    }
                    else
                    {
                        chatMessage.conversationImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                        chatMessage.conversationName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        chatMessage.conversationId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    }
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    conversations.add(chatMessage);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED)
                {
                    for (int i=0; i<conversations.size(); i++)
                    {
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        if(conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId))
                        {
                            conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                        }
                    }
                }
            }
            Collections.sort(conversations, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
            conversationsAdapter.notifyDataSetChanged();
            binding.conversationsRecyclerView.smoothScrollToPosition(0);
            binding.conversationsRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    };


    @Override
    public void onConversationClicked(UserMdl user) {
        Intent intent = new Intent(getApplicationContext(), MsgChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);

    }

}










