/*
package com.example.caspaceapplication.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.Owner.ProDisc.OwnerProDisc_ModelClass;
import com.example.caspaceapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustHomePromotions_Adapter extends RecyclerView.Adapter<CustHomePromotions_Adapter.CustProDiscViewHolder> {

  */
/*  private Context context;
    private List<OwnerProDisc_ModelClass> dataClassList;

    public CustHomePromotions_Adapter(Context context, List<OwnerProDisc_ModelClass> dataClassList) {
        this.context = context;
        this.dataClassList = dataClassList;
    }
*//*

    @NonNull
    @Override
    public CustProDiscViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem_promotionscusthomepage, parent, false);
        return new CustProDiscViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustProDiscViewHolder holder, int position) {
        */
/*String imageUri = String.valueOf(dataClassList.get(position).getPromotionImage());
        if (imageUri != null && !imageUri.isEmpty()){
            Picasso.get().load(imageUri).into(holder.recCustProDisc_Image);
        }
        holder.recCustProDisc_Cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();

                //OwnerProDisc_ModelClass model = dataClassList.get(clickedPosition);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.prodisc_description_popup, null);

                ImageView pdImageDetailed;
                TextView pdTitleDetailed, pdDescriptionDetailed;
                AppCompatButton cancelButton;

                pdImageDetailed = dialogView.findViewById(R.id.prodiscDetailImage_imageButton_popup);
                pdTitleDetailed = dialogView.findViewById(R.id.popup_promotionTitleDetailed);
                pdDescriptionDetailed = dialogView.findViewById(R.id.popup_promotionDescriptionDetailed);
                cancelButton = dialogView.findViewById(R.id.deleteButton_ProdiscDetailPopup);

                cancelButton.setText("Cancel");

                String imageUri = String.valueOf(dataClassList.get(clickedPosition).getPromotionImage());
                if (imageUri != null && !imageUri.isEmpty()) {
                    Picasso.get().load(imageUri).into(pdImageDetailed);
                }
                pdTitleDetailed.setText(dataClassList.get(clickedPosition).getPromotionTitle());
                pdDescriptionDetailed.setText(dataClassList.get(clickedPosition).getPromotionDescription());

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });*//*



    }

    @Override
    public int getItemCount() {
        return dataClassList.size();
    }


    public class CustProDiscViewHolder extends RecyclerView.ViewHolder {

        ImageView recCustProDisc_Image;
        CardView recCustProDisc_Cardview;

        public CustProDiscViewHolder(@NonNull View itemView) {
            super(itemView);

            recCustProDisc_Image = itemView.findViewById(R.id.recItem_custPromotions_imageview);
            recCustProDisc_Cardview = itemView.findViewById(R.id.recItem_custPromotions_cardview);

        }
    }
}
*/
