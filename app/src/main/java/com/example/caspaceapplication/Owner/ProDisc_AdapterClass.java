package com.example.caspaceapplication.Owner;

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

import com.example.caspaceapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProDisc_AdapterClass extends RecyclerView.Adapter<ProDiscViewHolder> {

    private Context context;
    private List<OwnerProDisc_ModelClass> dataClassList;

    public ProDisc_AdapterClass(Context context, List<OwnerProDisc_ModelClass> dataClassList) {
        this.context = context;
        this.dataClassList = dataClassList;
    }

    @NonNull
    @Override
    public ProDiscViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyleritem_prodisc, parent, false);
        return new ProDiscViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProDiscViewHolder holder, int position) {

        String imageUri = String.valueOf(dataClassList.get(position).getPromotionImage());
            if (imageUri != null && !imageUri.isEmpty()){
                Picasso.get().load(imageUri).into(holder.recPD_Image);
            }
        holder.recPD_Title.setText(dataClassList.get(position).getPromotionTitle());
        holder.recPD_Cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.prodisc_description_popup, null);
                ImageView pdImageDetailed;
                TextView pdTitleDetailed, pdDescriptionDetailed;
                AppCompatButton deleteButtonProdiscDetailPopup;
                pdImageDetailed = dialogView.findViewById(R.id.prodiscDetailImage_imageButton_popup);
                pdTitleDetailed = dialogView.findViewById(R.id.popup_promotionTitleDetailed);
                pdDescriptionDetailed = dialogView.findViewById(R.id.popup_promotionDescriptionDetailed);
                deleteButtonProdiscDetailPopup = dialogView.findViewById(R.id.deleteButton_ProdiscDetailPopup);

                String imageUri = String.valueOf(dataClassList.get(clickedPosition).getPromotionImage());
                if (imageUri != null && !imageUri.isEmpty()) {
                    Picasso.get().load(imageUri).into(pdImageDetailed);
                }
                pdTitleDetailed.setText(dataClassList.get(clickedPosition).getPromotionTitle());
                pdDescriptionDetailed.setText(dataClassList.get(clickedPosition).getPromotionDescription());
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();

                //todo: delete button on dialog
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataClassList.size();
    }
}

class ProDiscViewHolder<Button> extends RecyclerView.ViewHolder{

    ImageView recPD_Image;
    TextView recPD_Title;
    CardView recPD_Cardview;
    //AppCompatButton deleteButtonProdiscDetailPopup;

    public ProDiscViewHolder(@NonNull View itemView) {
        super(itemView);

        recPD_Image = itemView.findViewById(R.id.recPromotionImage);
        recPD_Title = itemView.findViewById(R.id.recPromotionTitle);
        recPD_Cardview = itemView.findViewById(R.id.recRDCardView);

        //deleteButtonProdiscDetailPopup = itemView.findViewById(R.id.deleteButton_ProdiscDetailPopup);

    }
}