package com.example.caspaceapplication;

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

        //holder.recImage.setImageResource(dataClassList.get(position).getLayoutImage());
        String imageUri = null;
        imageUri = String.valueOf(dataClassList.get(position).getLayoutImage());
        Picasso.get().load(imageUri).into(holder.recImage);

        holder.recName.setText(dataClassList.get(position).getLayoutName());
        holder.recPeople.setText(dataClassList.get(position).getLayoutPeopleNum());
        holder.recAreaSize.setText(dataClassList.get(position).getLayoutAreasize());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Owner_OfficeLayoutDetail.class);
                intent.putExtra("layoutImage", dataClassList.get(holder.getAdapterPosition()).getLayoutImage());
                intent.putExtra("layoutName", dataClassList.get(holder.getAdapterPosition()).getLayoutName());
                intent.putExtra("layoutPeopleNum", dataClassList.get(holder.getAdapterPosition()).getLayoutPeopleNum());
                intent.putExtra("layoutAreasize", dataClassList.get(holder.getAdapterPosition()).getLayoutAreasize());

                context.startActivity(intent);

            }
        });
        /*OfficeLayout_DataClass model = dataClassList.get(position);
        //holder.recImage.setImageResource(Integer.parseInt("Layout Image" + model.getLayoutImage()));
        holder.recName.setText("Layout Name" + model.getLayoutName());
        holder.recPeople.setText("Layout People size" + model.getLayoutPeopleNum());
        holder.recAreaSize.setText("Layout Area size" +  model.getLayoutAreasize());

        String imageUri = null;
        imageUri = String.valueOf(model.getLayoutImage());
        Picasso.get().load(imageUri).into(holder.recImage);*/

    }

    @Override
    public int getItemCount() {
        return dataClassList.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recName, recPeople, recAreaSize;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recName = itemView.findViewById(R.id.recName);
        recPeople = itemView.findViewById(R.id.recPeopleAnswer);
        recAreaSize = itemView.findViewById(R.id.recAreasizeAnswer);
        recCard = itemView.findViewById(R.id.cardView);
    }
}