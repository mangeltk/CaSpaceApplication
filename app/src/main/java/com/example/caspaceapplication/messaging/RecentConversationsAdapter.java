package com.example.caspaceapplication.messaging;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.databinding.MessagingItemContainerRecentConversationBinding;

import java.util.List;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversationViewHolder> {

    private final List<ChatMessage> chatMessages;
    private final ConversationListener conversationListener;

    public RecentConversationsAdapter(List<ChatMessage> chatMessages, ConversationListener conversationListener) {
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

    class ConversationViewHolder extends RecyclerView.ViewHolder
    {
        MessagingItemContainerRecentConversationBinding binding;

        ConversationViewHolder(MessagingItemContainerRecentConversationBinding messagingItemContainerRecentConversationBinding)
        {
            super(messagingItemContainerRecentConversationBinding.getRoot());
            binding = messagingItemContainerRecentConversationBinding;
        }
        void setData(ChatMessage chatMessage)
        {
            binding.imageProfile.setImageBitmap(getConversationImage(chatMessage.conversationImage));
            binding.textFirstName.setText(chatMessage.conversationName);
            binding.textRecentMessage.setText(chatMessage.message);
            binding.getRoot().setOnClickListener(v ->
            {
                UserMdl user = new UserMdl();
                user.id = chatMessage.conversationId;
                user.userFirstName = chatMessage.conversationName;
                user.userImage = chatMessage.conversationImage;
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

