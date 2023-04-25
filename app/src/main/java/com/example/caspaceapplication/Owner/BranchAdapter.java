package com.example.caspaceapplication.Owner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.MyViewHolder> {


    private Context context;
    private List<BranchModel> dataClassList;

    public BranchAdapter(Context context, List<BranchModel> dataClassList) {
        this.context = context;
        this.dataClassList = dataClassList;
    }

    @NonNull
    @Override
    public BranchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem_cws_cardview,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchAdapter.MyViewHolder holder, int position) {

        String imageUri = String.valueOf(dataClassList.get(position).getCospaceImage());
            Picasso.get().load(imageUri).into(holder.branchImage);
        holder.branchName.setText(dataClassList.get(position).getCospaceName());
        holder.branchStreetAddress.setText(dataClassList.get(position).getCospaceStreetAddress());
        holder.branchCityAddress.setText(dataClassList.get(position).getCospaceCityAddress());
        holder.branchCategory.setText(dataClassList.get(position).getCospaceCategory());
        holder.branchCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: redirect to CWS branch profile page with all info displayed
            }
        });
        holder.showOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: show google map on with pinned location pop up already created name: enterlocation_googlemap_popup

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataClassList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView branchImage;
        TextView branchName, branchStreetAddress, branchCityAddress, branchCategory;
        CardView branchCardView;
        Button showOnMapButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //todo: this if for cardview that i made when display results
            branchImage = itemView.findViewById(R.id.branchImage_cardview);
            branchName = itemView.findViewById(R.id.branchName_cardview);
            branchStreetAddress = itemView.findViewById(R.id.branchStreetAddress_cardview);
            branchCityAddress = itemView.findViewById(R.id.branchCityAddress_cardview);
            branchCategory = itemView.findViewById(R.id.branchCategory_cardview);
            branchCardView = itemView.findViewById(R.id.branch_cardView);
            showOnMapButton = itemView.findViewById(R.id.showOnMap_Button);

        }
    }
}
