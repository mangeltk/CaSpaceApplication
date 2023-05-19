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

import com.example.caspaceapplication.Owner.BranchModel;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.CWSProfile.CWS_ProfilePage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TechnologyHubsAdapter extends RecyclerView.Adapter<TechnologyHubsAdapter.MyViewHolder> {

    Context context;
    List<BranchModel> branchModelList;

    public TechnologyHubsAdapter(Context context, List<BranchModel> branchModelList) {
        this.context = context;
        this.branchModelList = branchModelList;
    }

    public void setSearchList(List<BranchModel> dataSearchList){
        this.branchModelList = dataSearchList;
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

        holder.cospaceName.setText(branchModelList.get(position).getCospaceName());

        //Load the image using Glide
        String imageUri = String.valueOf(branchModelList.get(position).getCospaceImage());
        if (imageUri != null && !imageUri.isEmpty()){
            Picasso.get().load(imageUri).into(holder.cospaceImage);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                Intent intent = new Intent(context, CWS_ProfilePage.class);
                intent.putExtra("cospaceName", branchModelList.get(clickedPosition).getCospaceName());
                intent.putExtra("owner_id", branchModelList.get(clickedPosition).getOwner_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return branchModelList.size();
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
