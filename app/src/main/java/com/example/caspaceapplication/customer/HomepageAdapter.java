package com.example.caspaceapplication.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class HomepageAdapter extends RecyclerView.Adapter<HomepageAdapter.MyViewHolder>{

    Context context;
    ArrayList<CoworkingSpaces> coworkingSpacesArrayList;

    public HomepageAdapter(Context context, ArrayList<CoworkingSpaces> coworkingSpacesArrayList)
    {
        this.context = context;
        this.coworkingSpacesArrayList = coworkingSpacesArrayList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false);
        return new MyViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CoworkingSpaces coworkingSpaces = coworkingSpacesArrayList.get(position);
        holder.description.setText(coworkingSpaces.heading);
        holder.titleImage.setImageResource(coworkingSpaces.titleImage);
    }


    @Override
    public int getItemCount() {
        return coworkingSpacesArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ShapeableImageView titleImage;
        TextView description;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleImage = itemView.findViewById(R.id.title_image);
            description = itemView.findViewById(R.id.description);
        }
    }
}
