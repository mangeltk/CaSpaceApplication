package com.example.caspaceapplication.messaging;

import com.example.caspaceapplication.messaging.UserMdl;

public interface ConversationListener {
    void onConversationClicked(UserMdl user);
}