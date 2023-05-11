package com.example.caspaceapplication.messaging.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.caspaceapplication.databinding.MessagingItemContainerRecentConversationBinding;
import com.example.caspaceapplication.messaging.listeners.ConversationListener;
import com.example.caspaceapplication.messaging.models.ChatMessage;
import com.example.caspaceapplication.messaging.models.UserMdl;

import java.util.List;


public class MessagingRecentConversationsAdapter extends RecyclerView.Adapter<MessagingRecentConversationsAdapter.ConversationViewHolder>{

    private final List<ChatMessage> chatMessages;
    private final ConversationListener conversationListener;

    public MessagingRecentConversationsAdapter(List<ChatMessage> chatMessages, ConversationListener conversationListener) {
        this.chatMessages = chatMessages;
        this.conversationListener = conversationListener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationViewHolder(
                MessagingItemContainerRecentConversationBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        holder.setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        MessagingItemContainerRecentConversationBinding binding;

        ConversationViewHolder(MessagingItemContainerRecentConversationBinding itemContainerRecentConversationBinding) {
            super(itemContainerRecentConversationBinding.getRoot());
            binding = itemContainerRecentConversationBinding;
        }

        void setData(ChatMessage chatMessage) {
            binding.imageProfile.setImageBitmap(getConversationImage(chatMessage.conversationImage));
            binding.textFirstName.setText(chatMessage.conversationName);
            binding.textRecentMessage.setText(chatMessage.message);
            binding.getRoot().setOnClickListener(v ->
            {
                UserMdl user = new UserMdl();
                user.userIDNum = chatMessage.conversationId;
                user.userFirstName = chatMessage.conversationName;
                user.image = chatMessage.conversationImage;
                conversationListener.onConversationClicked(user);
            });
        }
    }

    private Bitmap getConversationImage(String encodedImage)
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
}