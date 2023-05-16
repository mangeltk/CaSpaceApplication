package com.example.caspaceapplication.messaging;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.databinding.MessagingItemContainerReceivedMessageBinding;
import com.example.caspaceapplication.databinding.MessagingItemContainerSentMessageBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MsgChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<ChatMessage> chatMessages;
    private String receiverProfileImage;
    private final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public MsgChatAdapter(List<ChatMessage> chatMessages, String receiverProfileImage, String senderId) {
        this.chatMessages = chatMessages;
        this.receiverProfileImage = receiverProfileImage;
        this.senderId = senderId;
    }

    public String getReceiverProfileImage() {
        return receiverProfileImage;
    }

    public void setReceiverProfileImage(String receiverProfileImage) {
        this.receiverProfileImage = receiverProfileImage;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT)
        {
            return new SentMessageViewHolder(MessagingItemContainerSentMessageBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false));

        }
        else
        {
            return new ReceivedMessageViewHolder(MessagingItemContainerReceivedMessageBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT)
        {
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        }
        else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), receiverProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        if (chatMessage != null && chatMessage.senderId != null && chatMessage.senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }


    static class SentMessageViewHolder extends RecyclerView.ViewHolder
    {
        private final MessagingItemContainerSentMessageBinding binding;

        SentMessageViewHolder(@NonNull MessagingItemContainerSentMessageBinding messagingItemContainerSentMessageBinding)
        {
            super(messagingItemContainerSentMessageBinding.getRoot());
            binding = messagingItemContainerSentMessageBinding;
        }

        void setData(ChatMessage chatMessage)
        {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder
    {
        private final MessagingItemContainerReceivedMessageBinding binding;
        ReceivedMessageViewHolder(@NonNull MessagingItemContainerReceivedMessageBinding messagingItemContainerReceivedMessageBinding)
        {
            super(messagingItemContainerReceivedMessageBinding.getRoot());
            binding = messagingItemContainerReceivedMessageBinding;
        }

        void setData(ChatMessage chatMessage, String receiverProfileImage)
        {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            if(receiverProfileImage != null)
            {
                Picasso.get().load(receiverProfileImage).into(binding.imageProfile);
            }
        }
    }

}