package com.example.caspaceapplication.customer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CoworkingSpacesAdapter extends RecyclerView.Adapter<CoworkingSpacesAdapter.MyViewHolder> {

    Context context;
    List<CoworkingSpacesModel> coworkingSpacesModelArrayList;

    public CoworkingSpacesAdapter(Context context, List<CoworkingSpacesModel> coworkingSpacesModelArrayList) {
        this.context = context;
        this.coworkingSpacesModelArrayList = coworkingSpacesModelArrayList;
    }

    public void setSearchList(List<CoworkingSpacesModel> dataSearchList){
        this.coworkingSpacesModelArrayList = dataSearchList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoworkingSpacesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_cws, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CoworkingSpacesAdapter.MyViewHolder holder, int position) {

        holder.cospaceName.setText(coworkingSpacesModelArrayList.get(position).getCospaceName());

        //Load the image using Glide
        String imageUri = String.valueOf(coworkingSpacesModelArrayList.get(position).getCospaceImage());
        if (imageUri != null && !imageUri.isEmpty()){
            Picasso.get().load(imageUri).into(holder.cospaceImage);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CWS_ProfilePage.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return coworkingSpacesModelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView cospaceName;
        ImageView cospaceImage;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cospaceName = itemView.findViewById(R.id.cospaceName);
            cardView = itemView.findViewById(R.id.recRDCardView);
            cospaceImage = itemView.findViewById(R.id.cospaceImage);

        }
    }
}
