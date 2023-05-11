package com.example.caspaceapplication.messaging.listeners;

import com.example.caspaceapplication.messaging.models.UserMdl;
import com.google.firebase.firestore.auth.User;

public interface ConversationListener {
    void onConversationClicked(UserMdl user);
}