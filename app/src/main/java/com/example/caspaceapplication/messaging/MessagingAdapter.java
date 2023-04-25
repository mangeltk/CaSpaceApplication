package com.example.caspaceapplication.messaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagingAdapter extends RecyclerView.Adapter<MessagingAdapter.MyViewHolder>{

    public MessagingAdapter(List<MessagingList> messagingList, Context context) {
        this.messagingList = messagingList;
        this.context = context;
    }

    private final List<MessagingList> messagingList;
    private final Context context;

    @NonNull
    @Override
    public MessagingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messaging_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagingAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return messagingList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{


        private CircleImageView profilePic;
        private TextView name, lastMessage, unseenMessage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePic);
            lastMessage = itemView.findViewById(R.id.name);
            name = itemView.findViewById(R.id.name);
            unseenMessage = itemView.findViewById(R.id.unseenMessage);
        }
    }
}
