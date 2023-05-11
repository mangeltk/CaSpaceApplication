package com.example.caspaceapplication.messaging.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.databinding.MessagingItemContainerUserBinding;
import com.example.caspaceapplication.messaging.listeners.UserListener;
import com.example.caspaceapplication.messaging.models.UserMdl;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessagingUsersAdapter extends RecyclerView.Adapter<MessagingUsersAdapter.UserViewHolder> {

    private final List<UserMdl> users;
    private final UserListener userListener;

    public MessagingUsersAdapter(List<UserMdl> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessagingItemContainerUserBinding messagingItemContainerUserBinding = MessagingItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false);

        return new UserViewHolder(messagingItemContainerUserBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        MessagingItemContainerUserBinding binding;

        UserViewHolder(MessagingItemContainerUserBinding messagingItemContainerUserBinding)
        {
            super(messagingItemContainerUserBinding.getRoot());
            binding = messagingItemContainerUserBinding;

        }

        void setUserData(UserMdl user)
        {
            binding.textFirstName.setText(user.userFirstName);
            binding.textEmail.setText(user.userEmail);
            binding.imageProfile.setImageBitmap(getUserImage(user.image));
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));

            if (user.image!=null && !user.image.isEmpty()){
                Picasso.get().load(user.image).into(binding.imageProfile);
            }
        }
    }

    /*private Bitmap getUserImage (String encodedImage)
    {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }*/
    private Bitmap getUserImage(String encodedImage) {
        if (encodedImage == null) {
            return null;
        }
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }



}