package com.example.caspaceapplication.Owner.ProDisc;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
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
                OwnerProDisc_ModelClass model = dataClassList.get(clickedPosition);

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

                deleteButtonProdiscDetailPopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure you want to delete this promotion?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                CollectionReference collectionReference = firebaseFirestore.collection("OwnerPublishedPromotions");

                                Query query = collectionReference.whereEqualTo("promotionTitle", model.getPromotionTitle());
                                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                            String id = documentSnapshot.getId();

                                            // Delete data from Firestore using the retrieved ID
                                            firebaseFirestore.collection("OwnerPublishedPromotions").document(id)
                                                    .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            // Delete successful, close dialog and remove item from list
                                                            dialog.dismiss();
                                                            dataClassList.remove(clickedPosition);
                                                            notifyItemRemoved(clickedPosition);
                                                            Toast.makeText(context, "Promotion Deleted!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Delete failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }else {
                                            // Document with the promotion title not found
                                            Toast.makeText(context, "Document not found", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Query failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("No", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

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