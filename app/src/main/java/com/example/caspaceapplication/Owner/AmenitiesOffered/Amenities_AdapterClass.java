package com.example.caspaceapplication.Owner.AmenitiesOffered;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.R;

import java.util.List;

public class Amenities_AdapterClass extends RecyclerView.Adapter<Amenities_AdapterClass.AmenitiesViewHolder> {

    //private Context context;
    private List<OwnerAmenities_ModelClass> amenitiesList;


    public Amenities_AdapterClass(List<OwnerAmenities_ModelClass> amenitiesList) {
        this.amenitiesList = amenitiesList;
    }

    @NonNull
    @Override
    public Amenities_AdapterClass.AmenitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem_amenitiesoffered, parent,false);
        return new AmenitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Amenities_AdapterClass.AmenitiesViewHolder holder, int position) {
        OwnerAmenities_ModelClass amenities = amenitiesList.get(position);
        holder.recAmenityName.setText(amenities.getAmenityName());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AmenitiesViewHolder extends RecyclerView.ViewHolder {

        TextView recAmenityName;

        public AmenitiesViewHolder(@NonNull View itemView) {
            super(itemView);

            recAmenityName = itemView.findViewById(R.id.recycleritem_amenityName);
        }
    }
}
