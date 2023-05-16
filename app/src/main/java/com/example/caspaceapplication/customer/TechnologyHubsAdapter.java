package com.example.caspaceapplication.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TechnologyHubsAdapter extends RecyclerView.Adapter<TechnologyHubsAdapter.MyViewHolder> {

    Context context;
    List<TechnologyHubsModel> technologyHubsModelList;

    public TechnologyHubsAdapter(Context context, List<TechnologyHubsModel> technologyHubsModelList) {
        this.context = context;
        this.technologyHubsModelList = technologyHubsModelList;
    }

    public void setSearchList(List<TechnologyHubsModel> dataSearchList)
    {
        this.technologyHubsModelList = dataSearchList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TechnologyHubsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_cws, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TechnologyHubsAdapter.MyViewHolder holder, int position) {

        holder.cospaceName.setText(technologyHubsModelList.get(position).getCospaceName());
        String imageUri = String.valueOf(technologyHubsModelList.get(position).getCospaceImage());
        if(imageUri != null && !imageUri.isEmpty())
        {
            Picasso.get().load(imageUri).into(holder.cospaceImage);
        }


    }

    @Override
    public int getItemCount() {
        return technologyHubsModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView cospaceName;
        ImageView cospaceImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cospaceName = itemView.findViewById(R.id.cospaceName);
            cospaceImage = itemView.findViewById(R.id.cospaceImage);
        }
    }
}
