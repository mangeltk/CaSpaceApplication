package com.example.caspaceapplication.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.Owner.ProDisc.OwnerProDisc_ModelClass;
import com.example.caspaceapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustHomePromotions_Adapter extends RecyclerView.Adapter<CustHomePromotions_Adapter.CustProDiscViewHolder> {

    private Context context;
    private List<OwnerProDisc_ModelClass> dataClassList;

    public CustHomePromotions_Adapter(Context context, List<OwnerProDisc_ModelClass> dataClassList) {
        this.context = context;
        this.dataClassList = dataClassList;
    }

    @NonNull
    @Override
    public CustProDiscViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem_promotionscusthomepage, parent, false);
        return new CustProDiscViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustProDiscViewHolder holder, int position) {
        String imageUri = String.valueOf(dataClassList.get(position).getPromotionImage());
        if (imageUri != null && !imageUri.isEmpty()){
            Picasso.get().load(imageUri).into(holder.recCustProDisc_Image);
        }
    }

    @Override
    public int getItemCount() {
        return dataClassList.size();
    }


    public class CustProDiscViewHolder extends RecyclerView.ViewHolder {

        ImageView recCustProDisc_Image;

        public CustProDiscViewHolder(@NonNull View itemView) {
            super(itemView);

            recCustProDisc_Image = itemView.findViewById(R.id.recItem_custPromotions_imageview);


        }
    }
}
