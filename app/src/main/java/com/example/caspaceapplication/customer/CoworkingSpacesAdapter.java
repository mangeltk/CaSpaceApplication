package com.example.caspaceapplication.customer;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.caspaceapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CoworkingSpacesAdapter extends RecyclerView.Adapter
<CoworkingSpacesAdapter.MyViewHolder> {

    Context context;
    ArrayList<CoworkingSpacesModel> coworkingSpacesModelArrayList;

    public CoworkingSpacesAdapter(Context context, ArrayList<CoworkingSpacesModel> coworkingSpacesModelArrayList) {
        this.context = context;
        this.coworkingSpacesModelArrayList = coworkingSpacesModelArrayList;
    }


    @NonNull
    @Override
    public CoworkingSpacesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_cws, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CoworkingSpacesAdapter.MyViewHolder holder, int position) {

        CoworkingSpacesModel coworkingSpacesModel = coworkingSpacesModelArrayList.get(position);

        holder.cospaceName.setText(coworkingSpacesModel.cospaceName);
        //holder.cospaceAddress.setText(coworkingSpacesModel.cospaceAddress);

        //Load the image using Glide
        String imageUri = String.valueOf(coworkingSpacesModelArrayList.get(position).getCospaceImage());
        if (imageUri != null && !imageUri.isEmpty()){
            Picasso.get().load(imageUri).into(holder.cospaceImage);
        }
    }

    @Override
    public int getItemCount() {

        return coworkingSpacesModelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView cospaceName, cospaceAddress;
        ImageView cospaceImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cospaceName = itemView.findViewById(R.id.cospaceName);
            //cospaceAddress = itemView.findViewById(R.id.cospaceAddress);
            cospaceImage = itemView.findViewById(R.id.cospaceImage);

        }
    }
}
