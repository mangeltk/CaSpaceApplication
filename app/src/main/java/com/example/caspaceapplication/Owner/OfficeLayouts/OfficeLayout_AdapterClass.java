package com.example.caspaceapplication.Owner.OfficeLayouts;

import android.content.Context;
import android.content.Intent;
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

public class OfficeLayout_AdapterClass extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<OfficeLayout_DataClass> dataClassList;

    public void setSearchList(List<OfficeLayout_DataClass> dataSearchList){
        this.dataClassList = dataSearchList;
        notifyDataSetChanged();
    }

    public OfficeLayout_AdapterClass (Context context, List<OfficeLayout_DataClass> dataClassList){
        this.context = context;
        this.dataClassList = dataClassList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_officelayouts, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String imageUri = String.valueOf(dataClassList.get(position).getLayoutImage());
            Picasso.get().load(imageUri).into(holder.recImage);
        holder.recName.setText(dataClassList.get(position).getLayoutName());
        holder.recPeople.setText(dataClassList.get(position).getMinCapacity() + "-" + dataClassList.get(position).getMaxCapacity());
        holder.recAreaSize.setText(dataClassList.get(position).getLayoutAreasize());

        String avail = dataClassList.get(position).getLayoutAvailability();
        if (avail == null || avail.isEmpty()){
            holder.textDirection.setVisibility(View.VISIBLE);
        }else{
            holder.recAvailability.setText(avail);
            holder.textDirection.setVisibility(View.GONE);
        }

        holder.recButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Owner_OfficeLayoutDetail.class);
                intent.putExtra("layoutImage", dataClassList.get(holder.getAdapterPosition()).getLayoutImage());
                intent.putExtra("layoutName", dataClassList.get(holder.getAdapterPosition()).getLayoutName());
                intent.putExtra("minCapacity", dataClassList.get(holder.getAdapterPosition()).getMinCapacity());
                intent.putExtra("maxCapacity", dataClassList.get(holder.getAdapterPosition()).getMaxCapacity());
                intent.putExtra("layoutAreasize", dataClassList.get(holder.getAdapterPosition()).getLayoutAreasize());
                intent.putExtra("layoutType", dataClassList.get(holder.getAdapterPosition()).getLayoutType());
                intent.putExtra("layoutHourlyPrice", dataClassList.get(holder.getAdapterPosition()).getLayoutHourlyPrice());
                intent.putExtra("layoutDailyPrice", dataClassList.get(holder.getAdapterPosition()).getLayoutDailyPrice());
                intent.putExtra("layoutWeeklyPrice", dataClassList.get(holder.getAdapterPosition()).getLayoutWeeklyPrice());
                intent.putExtra("layoutMonthlyPrice", dataClassList.get(holder.getAdapterPosition()).getLayoutMonthlyPrice());
                intent.putExtra("layoutAnnualPrice", dataClassList.get(holder.getAdapterPosition()).getLayoutAnnualPrice());
                intent.putExtra("layoutAvailability", dataClassList.get(holder.getAdapterPosition()).getLayoutAvailability());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataClassList.size();
    }
}

    class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recName, recPeople, recAreaSize, recAvailability, textDirection;
    CardView recCard;
    Button recButton;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage);
        recName = itemView.findViewById(R.id.recName);
        recPeople = itemView.findViewById(R.id.recPeopleAnswer);
        recAreaSize = itemView.findViewById(R.id.recAreasizeAnswer);
        recAvailability = itemView.findViewById(R.id.recAvailabilityStatus);
        recCard = itemView.findViewById(R.id.cardView);
        recButton = itemView.findViewById(R.id.recButton_OfficeLayout);
        textDirection = itemView.findViewById(R.id.textDirection);

    }
}