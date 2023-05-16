package com.example.caspaceapplication.messaging;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.caspaceapplication.Notification.FCMSend;
import com.example.caspaceapplication.customer.BookingTransactionManagement.Cust_BookingTransaction;
import com.example.caspaceapplication.databinding.ActivityMsgChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MsgChatActivity extends MsgBaseActivity {

    private ActivityMsgChatBinding binding;
    private UserMdl receiverUser;
    private List<ChatMessage> chatMessages;
    private MsgChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private String conversationId = null;
    private Boolean isReceiverAvailable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMsgChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
        init();
        listenMessages();

    }

    private void init()
    {
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new MsgChatAdapter(chatMessages, (receiverUser.userImage),
                preferenceManager.getString(Constants.KEY_COMBINED_ID));

        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void sendMessage()
    {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_COMBINED_ID));
        message.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        if(conversationId != null)
        {
            updateConversation(binding.inputMessage.getText().toString());
        }
        else
        {
            HashMap<String, Object> conversation = new HashMap<>();
            conversation.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_COMBINED_ID));
            conversation.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_COMBINED_FIRST_NAME));
            conversation.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_COMBINED_IMAGE));
            conversation.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
            conversation.put(Constants.KEY_RECEIVER_NAME, receiverUser.userFirstName);
            conversation.put(Constants.KEY_RECEIVER_IMAGE, receiverUser.userImage);
            conversation.put(Constants.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
            conversation.put(Constants.KEY_TIMESTAMP, new Date());
            addConversation(conversation);
        }
        if(!isReceiverAvailable)
        {
            try{
                JSONArray tokens = new JSONArray();
                tokens.put(receiverUser.token);

                JSONObject data = new JSONObject();
                data.put(Constants.KEY_COMBINED_ID, preferenceManager.getString(Constants.KEY_COMBINED_ID));
                data.put(Constants.KEY_COMBINED_FIRST_NAME, preferenceManager.getString(Constants.KEY_COMBINED_FIRST_NAME));
                data.put(Constants.KEY_FCM_TOKEN, preferenceManager.getString(Constants.KEY_FCM_TOKEN));
                data.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());

                JSONObject body = new JSONObject();
                body.put(Constants.REMOTE_MSG_DATA, data);
                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

                sendNotification(body.toString());
            }
            catch(Exception exception)
            {
                showToast(exception.getMessage());
            }
        }
        binding.inputMessage.setText(null);
    }


    private void listenMessages()
    {
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_COMBINED_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.id)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiverUser.id)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_COMBINED_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) ->
    {
        if(error != null)
        {
            return;
        }
        if(value != null)
        {
            int count = chatMessages.size();
            for(DocumentChange documentChange : value.getDocumentChanges())

            {
                if(documentChange.getType() == DocumentChange.Type.ADDED)
                {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if(count == 0)
            {
                chatAdapter.notifyDataSetChanged();
            }
            else
            {
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() -1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
        if(conversationId == null)
        {
            checkForConversation();
        }
    };




    private void listenAvailabilityOfReceiver()
    {
        database.collection(Constants.KEY_COMBINED_COLLECTION).document(
                        receiverUser.id)
                .addSnapshotListener(MsgChatActivity.this, (value, error) ->
                {
                    if (error != null) {
                        return;
                    }
                    if(value!=null) {
                        if (value.getLong(Constants.KEY_AVAILABILITY) != null) {
                            int availability = Objects.requireNonNull(value.getLong(Constants.KEY_AVAILABILITY)).intValue();
                            isReceiverAvailable = availability == 1;
                        }
                        receiverUser.token = value.getString(Constants.KEY_FCM_TOKEN);
                        if(receiverUser.userImage == null)
                        {
                            receiverUser.userImage = value.getString(Constants.KEY_COMBINED_IMAGE);
                            chatAdapter.setReceiverProfileImage(receiverUser.userImage);
                            //chatAdapter.setReceiverProfileImage(getBitmapFromEncodedString(receiverUser.userImage));
                            chatAdapter.notifyItemRangeChanged(0, chatMessages.size());
                        }
                    }
                    if(isReceiverAvailable)
                    {
                        binding.textAvailability.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        binding.textAvailability.setVisibility(View.GONE);
                    }

                });
    }

    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /*private Bitmap getBitmapFromEncodedString(String encodedImage)
    {
        if(encodedImage != null) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        else
        {
            return null;
        }
    }
*/
    private void loadReceiverDetails()
    {
        receiverUser = (UserMdl) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textFirstName.setText(receiverUser.userFirstName);
    }

    private void setListeners()
    {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> sendMessage());
    }

    private String getReadableDateTime(Date date)
    {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void addConversation(HashMap<String, Object> conversation)
    {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversation)
                .addOnSuccessListener(documentReference -> conversationId = documentReference.getId());
    }

    private void updateConversation(String message)
    {
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(conversationId);
        documentReference.update(Constants.KEY_LAST_MESSAGE, message,
                Constants.KEY_TIMESTAMP, new Date());

    }

    private void checkForConversation()
    {
        if(chatMessages.size() !=0)
        {
            checkForConversionRemotely(
                    preferenceManager.getString(Constants.KEY_COMBINED_ID), receiverUser.id);
            checkForConversionRemotely(receiverUser.id, preferenceManager.getString(Constants.KEY_COMBINED_ID));
        }
    }

    private void checkForConversionRemotely(String senderId, String receiverId)
    {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversationOnCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversationOnCompleteListener = task ->
    {
        if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0)
        {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversationId = documentSnapshot.getId();
        }
    };

    /*private void sendNotification()
    {
        String customerName= ;
        String spaceName = layout_Name;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTimeString = now.format(formatter);
        String title = "Booking Notification: "+dateTimeString;
        String message = "\n"+customerName + " booked "+spaceName +".";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("OwnerUserAccounts").document(ownerId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String ownerFCMToken = documentSnapshot.getString("fcmToken");
                    FCMSend.pushNotification(Cust_BookingTransaction.this, ownerFCMToken, title, message);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting FCM token for owner", e);
                });
        CollectionReference notificationsRef = db.collection("OwnerNotificationStorage");
        // Create a new notification document with a randomly generated ID
        DocumentReference newNotificationRef = notificationsRef.document();
        String newNotificationId = newNotificationRef.getId();
        // Add the notification document to the "Notifications" collection
        Map<String, Object> notification = new HashMap<>();
        notification.put("notificationId", newNotificationId);
        notification.put("title", title);
        notification.put("message", message);
        notification.put("ownerId", ownerId);
        notification.put("bookingTimeDate",com.google.firebase.Timestamp.now());
        newNotificationRef.set(notification)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Notification added with ID: " + newNotificationId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding notification", e);
                    }
                });
    }*/

    private void sendNotification(String messageBody)
    {
        ApiClient.getClient().create(ApiService.class).sendMessage(
                        Constants.getRemoteMsgHeaders(), messageBody)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@org.checkerframework.checker.nullness.qual.NonNull Call<String> call, @org.checkerframework.checker.nullness.qual.NonNull Response<String> response) {
                        if(response.isSuccessful())
                        {
                            try{
                                if(response.body() !=null )
                                {
                                    JSONObject responseJson = new JSONObject(response.body());
                                    JSONArray results = responseJson.getJSONArray("results");
                                    if(responseJson.getInt("failure") == 1)
                                    {
                                        JSONObject error = (JSONObject) results.get(0);
                                        showToast(error.getString("error"));
                                        return;
                                    }
                                }
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                            //showToast("Notification sent successfully");
                        }
                        else
                        {
                            showToast("Error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, Throwable t) {
                        showToast(t.getMessage());
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        listenAvailabilityOfReceiver();
    }

}