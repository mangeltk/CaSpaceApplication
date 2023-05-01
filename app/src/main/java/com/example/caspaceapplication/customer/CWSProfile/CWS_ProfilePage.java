package com.example.caspaceapplication.customer.CWSProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.Owner.ProDisc.OwnerProDisc_ModelClass;
import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CWS_ProfilePage extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference colref_BranchInfo = firebaseFirestore.collection("CospaceBranches");
    CollectionReference colref_ProDisc = firebaseFirestore.collection("OwnerPublishedPromotions");
    CollectionReference colref_OfficeLayouts = firebaseFirestore.collection("OfficeLayouts");
    CollectionReference colref_Amenities = firebaseFirestore.collection("AmenitiesOffered");

    TextView ProfPage_Title, ProfPage_TitleSmall, ProfPage_Location, ProfPage_StoreHours, ProfPage_AboutContent,
            ProfPage_ContactInfo, ProfPage_FloorMapDescription,ProfPage_ListOfRooms, ProfPage_PricingContent,
            ProfPage_Plans;

    RecyclerView CWSProfPage_Prodisc;
    List<OwnerProDisc_ModelClass> dataClassList;
    CWSProfPage_Prodisc_Adapter cwsProfPageProdiscAdapter;

    AppCompatButton ProfPage_AmenitiesEdit_Button, ProfPage_SeeAllLayouts_Button;
    ImageView ProfPage_Image, ProfPage_FloorMap_Imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cws_profile_page);

        //intent from cardview clicked in manually search
        Intent intent = getIntent();
        String owner_id = intent.getStringExtra("owner_id");
        String cospaceName = intent.getStringExtra("cospaceName");

        ProfPage_Image = findViewById(R.id.CWS_ProfPage_Image);
        ProfPage_Title = findViewById(R.id.CWS_ProfPage_Title);
        ProfPage_TitleSmall = findViewById(R.id.CWS_ProfPage_TitleSmall);
        ProfPage_Location = findViewById(R.id.CWS_ProfPage_Location);
        ProfPage_StoreHours = findViewById(R.id.CWS_ProfPage_StoreHours);
        ProfPage_AboutContent = findViewById(R.id.CWSProfPage_AboutContent);
        ProfPage_ContactInfo = findViewById(R.id.CWSProfPage_ContactInfo);
        ProfPage_FloorMapDescription = findViewById(R.id.CWSProfPage_FloorMapDescription);
        ProfPage_ListOfRooms = findViewById(R.id.CWSProfPage_ListOfRooms);
        ProfPage_PricingContent = findViewById(R.id.CWSProfPage_PricingContent);
        ProfPage_Plans = findViewById(R.id.CWSProfPage_Plans);
        ProfPage_AmenitiesEdit_Button = findViewById(R.id.CWSProfPage_AmenitiesEdit_Button);
        ProfPage_SeeAllLayouts_Button = findViewById(R.id.CWSProfPage_SeeAllLayouts_Button);
        CWSProfPage_Prodisc = findViewById(R.id.recyclerview_CWSProfPage_Prodisc);
        ProfPage_FloorMap_Imageview = findViewById(R.id.CWSProfPage_FloorMap_Imageview);

        ProfPage_Title.setText(cospaceName);

        //get textview details
        colref_BranchInfo.whereEqualTo("cospaceName", cospaceName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                String image = documentSnapshot.getString("cospaceImage");
                                String name = documentSnapshot.getString("cospaceName");
                                String streetLocation = documentSnapshot.getString("cospaceStreetAddress");
                                String cityLocation = documentSnapshot.getString("cospaceCityAddress");
                                String storeHours = documentSnapshot.getString("cospaceStoreHours");
                                String about = documentSnapshot.getString("cospaceAbout");
                                String contact = documentSnapshot.getString("cospaceContactInfo");
                                String floorMapPic = documentSnapshot.getString("cospaceFloorMapImage");
                                String floorMapDesc = documentSnapshot.getString("cospaceFloorMapDesc");
                                String pricing = documentSnapshot.getString("cospacePricing");
                                String plans = documentSnapshot.getString("cospacePlans");

                                if (image == null || image.isEmpty()){
                                    Picasso.get().load(R.drawable.uploadphoto).into(ProfPage_Image);
                                }else{Picasso.get().load(image).into(ProfPage_Image);}
                                ProfPage_Title.setText(name);
                                ProfPage_TitleSmall.setText(name);
                                ProfPage_Location.setText(streetLocation + " " + cityLocation);
                                ProfPage_StoreHours.setText(storeHours);
                                ProfPage_AboutContent.setText(about);
                                ProfPage_ContactInfo.setText(contact);
                                if (floorMapPic == null || floorMapPic.isEmpty()){
                                    Picasso.get().load(R.drawable.uploadphoto).into(ProfPage_FloorMap_Imageview);
                                }else{
                                    Picasso.get().load(floorMapPic).into(ProfPage_FloorMap_Imageview);
                                }
                                ProfPage_FloorMapDescription.setText(floorMapDesc);
                                //ProfPage_ListOfRooms.setText();
                                ProfPage_PricingContent.setText(pricing);
                                ProfPage_Plans.setText(plans);
                            }
                        }
                    }
                });

        CWSProfPage_Prodisc.setHasFixedSize(true);
        dataClassList = new ArrayList<>();
        cwsProfPageProdiscAdapter = new CWSProfPage_Prodisc_Adapter(dataClassList);
        CWSProfPage_Prodisc.setAdapter(cwsProfPageProdiscAdapter);
        colref_ProDisc.whereEqualTo("owner_id", owner_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                        OwnerProDisc_ModelClass modelClass = documentSnapshot.toObject(OwnerProDisc_ModelClass.class);
                        dataClassList.add(modelClass);
                }
                cwsProfPageProdiscAdapter.notifyDataSetChanged();
            }
        });

        ProfPage_SeeAllLayouts_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CWS_ProfilePage.this, CWSProfilePage_RoomLayouts.class);
                intent.putExtra("cospaceName", cospaceName);
                intent.putExtra("owner_id", owner_id);
                startActivity(intent);
            }
        });

    }

    //adapter class for promotion discount modified
    public class CWSProfPage_Prodisc_Adapter extends RecyclerView.Adapter<CWSProfPage_Prodisc_Adapter.ViewHolder> {

        private List<OwnerProDisc_ModelClass> dataClass;

        public CWSProfPage_Prodisc_Adapter(List<OwnerProDisc_ModelClass> dataClass) {
            this.dataClass = dataClass;
        }

        @NonNull
        @Override
        public CWSProfPage_Prodisc_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyleritem_prodisc, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CWSProfPage_Prodisc_Adapter.ViewHolder holder, int position) {
            String imageUri = String.valueOf(dataClass.get(position).getPromotionImage());
            if (imageUri != null && !imageUri.isEmpty()){
                Picasso.get().load(imageUri).into(holder.recPD_Image);
            }
            holder.recPD_Title.setText(dataClass.get(position).getPromotionTitle());
            holder.recPD_Cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = holder.getAdapterPosition();
                    OwnerProDisc_ModelClass model = dataClass.get(clickedPosition);

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.prodisc_description_popup, null);

                    ImageView pdImageDetailed;
                    TextView pdTitleDetailed, pdDescriptionDetailed;
                    AppCompatButton deleteButtonProdiscDetailPopup;

                    pdImageDetailed = dialogView.findViewById(R.id.prodiscDetailImage_imageButton_popup);
                    pdTitleDetailed = dialogView.findViewById(R.id.popup_promotionTitleDetailed);
                    pdDescriptionDetailed = dialogView.findViewById(R.id.popup_promotionDescriptionDetailed);
                    deleteButtonProdiscDetailPopup = dialogView.findViewById(R.id.deleteButton_ProdiscDetailPopup);

                    deleteButtonProdiscDetailPopup.setVisibility(View.GONE);

                    String imageUri = String.valueOf(dataClass.get(clickedPosition).getPromotionImage());
                    if (imageUri != null && !imageUri.isEmpty()) {
                        Picasso.get().load(imageUri).into(pdImageDetailed);
                    }
                    pdTitleDetailed.setText(dataClass.get(clickedPosition).getPromotionTitle());
                    pdDescriptionDetailed.setText(dataClass.get(clickedPosition).getPromotionDescription());

                    builder.setView(dialogView);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataClass.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView recPD_Image;
            TextView recPD_Title;
            CardView recPD_Cardview;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                recPD_Image = itemView.findViewById(R.id.recPromotionImage);
                recPD_Title = itemView.findViewById(R.id.recPromotionTitle);
                recPD_Cardview = itemView.findViewById(R.id.recRDCardView);
            }
        }
    }



}

