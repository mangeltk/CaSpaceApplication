package com.example.caspaceapplication.customer.CWSProfile;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.Owner.ProDisc.OwnerProDisc_ModelClass;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.FeedbackRatingsManagement.CWS_FeedbackRatings;
import com.example.caspaceapplication.messaging.MsgMain;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CWS_ProfilePage extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser customerUserId = FirebaseAuth.getInstance().getCurrentUser();
    CollectionReference colref_CustomerUsers = firebaseFirestore.collection("CustomerUserAccounts");
    CollectionReference colref_BranchInfo = firebaseFirestore.collection("CospaceBranches");
    CollectionReference colref_ProDisc = firebaseFirestore.collection("OwnerPublishedPromotions");
    CollectionReference colref_OfficeLayouts = firebaseFirestore.collection("OfficeLayouts");
    CollectionReference colref_Amenities = firebaseFirestore.collection("AmenitiesOffered");

    TextView ProfPage_Title, ProfPage_TitleSmall, ProfPage_Location, ProfPage_StoreHours, ProfPage_AboutContent,
            ProfPage_ContactInfo, ProfPage_FloorMapDescription,ProfPage_ListOfRooms, ProfPage_PricingContent,
            ProfPage_Plans;

    TextView CWSHours_MondayStartTextview, CWSHours_MondayEndTextview,
            CWSHours_TuesdayStartTextview, CWSHours_TuesdayEndTextview,
            CWSHours_WednesdayStartTextview, CWSHours_WednesdayEndTextview,
            CWSHours_ThursdayStartTextview, CWSHours_ThursdayEndTextview,
            CWSHours_FridayStartTextview, CWSHours_FridayEndTextview,
            CWSHours_SaturdayStartTextview, CWSHours_SaturdayEndTextview,
            CWSHours_SundayStartTextview, CWSHours_SundayEndTextview;

    AppCompatImageButton likeButtonImageButton, unlikeButtonImageButton;

    RecyclerView CWSProfPage_Prodisc;
    List<OwnerProDisc_ModelClass> dataClassList;
    CWSProfPage_Prodisc_Adapter cwsProfPageProdiscAdapter;

    AppCompatButton ProfPage_AmenitiesEdit_Button, ProfPage_SeeAllLayouts_Button;
    ImageView ProfPage_Image, ProfPage_FloorMap_Imageview;

    ImageButton CWS_ProfPagebackButton;

    String cospaceId, cospaceImage;

    LinearLayout clickToSeeFeedbacksLinearLayout;

    AppCompatImageView toMessaging;

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

        CWSHours_MondayStartTextview = findViewById(R.id.CWSHours_MondayStart_Textview);
        CWSHours_MondayEndTextview = findViewById(R.id.CWSHours_MondayEnd_Textview);
        CWSHours_TuesdayStartTextview = findViewById(R.id.CWSHours_TuesdayStart_Textview);
        CWSHours_TuesdayEndTextview = findViewById(R.id.CWSHours_TuesdayEnd_Textview);
        CWSHours_WednesdayStartTextview = findViewById(R.id.CWSHours_WednesdayStart_Textview);
        CWSHours_WednesdayEndTextview = findViewById(R.id.CWSHours_WednesdayEnd_Textview);
        CWSHours_ThursdayStartTextview = findViewById(R.id.CWSHours_ThursdayStart_Textview);
        CWSHours_ThursdayEndTextview = findViewById(R.id.CWSHours_ThursdayEnd_Textview);
        CWSHours_FridayStartTextview = findViewById(R.id.CWSHours_FridayStart_Textview);
        CWSHours_FridayEndTextview = findViewById(R.id.CWSHours_FridayEnd_Textview);
        CWSHours_SaturdayStartTextview = findViewById(R.id.CWSHours_SaturdayStart_Textview);
        CWSHours_SaturdayEndTextview = findViewById(R.id.CWSHours_SaturdayEnd_Textview);
        CWSHours_SundayStartTextview = findViewById(R.id.CWSHours_SundayStart_Textview);
        CWSHours_SundayEndTextview = findViewById(R.id.CWSHours_SundayEnd_Textview);
        likeButtonImageButton = findViewById(R.id.likeButton_Imagebutton);
        unlikeButtonImageButton = findViewById(R.id.unlikeButton_Imagebutton);
        clickToSeeFeedbacksLinearLayout = findViewById(R.id.clickToSeeFeedbacks_LinearLayout);
        toMessaging = findViewById(R.id.toMessaging);

        CWS_ProfPagebackButton = findViewById(R.id.CWS_ProfPage_backButton);
        CWS_ProfPagebackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: back button
            }
        });

        /*toMessaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(CWS_ProfilePage.this, MsgMain.class);
                intent1.putExtra("ownerId", owner_id);
                startActivity(intent1);
            }
        });*/

        clickToSeeFeedbacksLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CWS_ProfilePage.this, "See all customer feedbacks", Toast.LENGTH_SHORT).show();
                //todo: redirect to feedbacks and ratings\
                Intent intent1 = new Intent(CWS_ProfilePage.this, CWS_FeedbackRatings.class);
                intent1.putExtra("branchName", cospaceName);
                intent1.putExtra("ownerId", owner_id);
                startActivity(intent1);
            }
        });

        unlikeButtonImageButton.setVisibility(View.VISIBLE);
        likeButtonImageButton.setVisibility(View.GONE);
        // Check if the user already liked the current cws branch
        firebaseFirestore.collection("CustomerLikes")
                .whereEqualTo("userId", customerUserId.getUid())
                .whereEqualTo("branchName", cospaceName)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            likeButtonImageButton.setVisibility(View.VISIBLE);
                            unlikeButtonImageButton.setVisibility(View.GONE);
                            Toast.makeText(CWS_ProfilePage.this, "Already liked this branch", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        unlikeButtonImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeButtonImageButton.setVisibility(View.VISIBLE);
                unlikeButtonImageButton.setVisibility(View.GONE);
                Toast.makeText(CWS_ProfilePage.this, "Liked " + cospaceName, Toast.LENGTH_SHORT).show();

                // Check if the like already exists for the customer
                firebaseFirestore.collection("CustomerLikes")
                        .whereEqualTo("userId", customerUserId.getUid())
                        .whereEqualTo("branchName", cospaceName)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.isEmpty()){
                                    // Store the like in the customer's favorites
                                    String documentId = firebaseFirestore.collection("CustomerLikes").document().getId();
                                    Timestamp timestamp = Timestamp.now();
                                    Map<String, Object> like = new HashMap<>();
                                    like.put("userId", customerUserId.getUid());
                                    like.put("branchName", cospaceName);
                                    like.put("branchImage", cospaceImage);
                                    like.put("timestamp", timestamp);
                                    firebaseFirestore.collection("CustomerLikes").document(documentId).set(like)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG, "Like stored in customer favorites");
                                                    // Increment the likes count for the branch
                                                    colref_BranchInfo.whereEqualTo("cospaceName", cospaceName).get()
                                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                    if (!queryDocumentSnapshots.isEmpty()){
                                                                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                                                        Map<String, Object> updates = new HashMap<>();
                                                                        Long likesCount = (Long) documentSnapshot.get("Likes");
                                                                        if (likesCount ==  null){
                                                                            likesCount = 0L;
                                                                        }
                                                                        updates.put("Likes", likesCount + 1);
                                                                        documentSnapshot.getReference().update(updates)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        Log.d(TAG, "Likes count incremented");
                                                                                    }
                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        Log.e(TAG, "Failed to increment Likes count", e);
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.e(TAG, "Failed to fetch document", e);
                                                                }
                                                            });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e(TAG, "Failed to store like in customer favorites", e);
                                                }
                                            });
                                }else {
                                    Log.d(TAG, "Like already exists for this customer and cospace");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Failed to check if like exists for this customer and cospace", e);
                            }
                        });


            }
        });

        likeButtonImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeButtonImageButton.setVisibility(View.GONE);
                unlikeButtonImageButton.setVisibility(View.VISIBLE);
                Toast.makeText(CWS_ProfilePage.this, "Unlike " + cospaceName, Toast.LENGTH_SHORT).show();

                // Delete the corresponding document from the CustomerLikes collection
                firebaseFirestore.collection("CustomerLikes")
                        .whereEqualTo("userId", customerUserId.getUid())
                        .whereEqualTo("branchName", cospaceName)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                    firebaseFirestore.collection("CustomerLikes").document(documentSnapshot.getId())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG, "CustomerLike document deleted successfully");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e(TAG, "Error deleting CustomerLike document", e);
                                                }
                                            });
                                } else {
                                    Log.w(TAG, "No matching documents found in CustomerLikes collection");
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error getting documents from CustomerLikes collection", e);
                            }
                        });

                // Decrement the Likes count in the BranchInfo document
                colref_BranchInfo.whereEqualTo("cospaceName", cospaceName)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                    Map<String, Object> updates = new HashMap<>();
                                    Long likesCount = (Long) documentSnapshot.get("Likes");
                                    if (likesCount == null) {
                                        likesCount = 0L;
                                    }
                                    updates.put("Likes", likesCount - 1);
                                    documentSnapshot.getReference().update(updates)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG, "Likes count decremented");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e(TAG, "Failed to decrement Likes count", e);
                                                }
                                            });
                                } else {
                                    Log.w(TAG, "No matching documents found in BranchInfo collection");
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Failed to fetch document", e);
                            }
                        });
            }
        });

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
                                String about = documentSnapshot.getString("cospaceAbout");
                                String contact = documentSnapshot.getString("cospaceContactInfo");
                                String floorMapPic = documentSnapshot.getString("cospaceFloorMapImage");
                                String floorMapDesc = documentSnapshot.getString("cospaceFloorMapDesc");
                                String pricing = documentSnapshot.getString("cospacePricing");
                                String plans = documentSnapshot.getString("cospacePlans");
                                cospaceId = documentSnapshot.getString("cospaceId");
                                cospaceImage = image;

                                Map<String, Object> data = documentSnapshot.getData();
                                Map<String, Object> hours = (Map<String, Object>) data.get("hours");
                                if (hours.containsKey("Monday")) {
                                    Map<String, Object> mondayHours = (Map<String, Object>) hours.get("Monday");
                                    if (mondayHours.containsKey("openTime")) {
                                        String mondayStartTime = mondayHours.get("openTime").toString();
                                        CWSHours_MondayStartTextview.setText(mondayStartTime);
                                    }
                                    if (mondayHours.containsKey("closeTime")) {
                                        String mondayCloseTime = mondayHours.get("closeTime").toString();
                                        CWSHours_MondayEndTextview.setText(mondayCloseTime);
                                    }
                                }

                                if (hours.containsKey("Tuesday")) {
                                    Map<String, Object> tuesdayHours = (Map<String, Object>) hours.get("Tuesday");
                                    if (tuesdayHours.containsKey("openTime")) {
                                        String tuesdayStartTime = tuesdayHours.get("openTime").toString();
                                        CWSHours_TuesdayStartTextview.setText(tuesdayStartTime);
                                    }
                                    if (tuesdayHours.containsKey("closeTime")) {
                                        String tuesdayCloseTime = tuesdayHours.get("closeTime").toString();
                                        CWSHours_TuesdayEndTextview.setText(tuesdayCloseTime);
                                    }
                                }

                                if (hours.containsKey("Wednesday")) {
                                    Map<String, Object> wednesdayHours = (Map<String, Object>) hours.get("Wednesday");
                                    if (wednesdayHours.containsKey("openTime")) {
                                        String wednesdayStartTime = wednesdayHours.get("openTime").toString();
                                        CWSHours_WednesdayStartTextview.setText(wednesdayStartTime);
                                    }
                                    if (wednesdayHours.containsKey("closeTime")) {
                                        String wednesdayCloseTime = wednesdayHours.get("closeTime").toString();
                                        CWSHours_WednesdayEndTextview.setText(wednesdayCloseTime);
                                    }
                                }

                                if (hours.containsKey("Thursday")) {
                                    Map<String, Object> thursdayHours = (Map<String, Object>) hours.get("Thursday");
                                    if (thursdayHours.containsKey("openTime")) {
                                        String thursdayStartTime = thursdayHours.get("openTime").toString();
                                        CWSHours_ThursdayStartTextview.setText(thursdayStartTime);
                                    }
                                    if (thursdayHours.containsKey("closeTime")) {
                                        String thursdayCloseTime = thursdayHours.get("closeTime").toString();
                                        CWSHours_ThursdayEndTextview.setText(thursdayCloseTime);
                                    }
                                }

                                if (hours.containsKey("Friday")) {
                                    Map<String, Object> fridayHours = (Map<String, Object>) hours.get("Friday");
                                    if (fridayHours.containsKey("openTime")) {
                                        String fridayStartTime = fridayHours.get("openTime").toString();
                                        CWSHours_FridayStartTextview.setText(fridayStartTime);
                                    }
                                    if (fridayHours.containsKey("closeTime")) {
                                        String fridayCloseTime = fridayHours.get("closeTime").toString();
                                        CWSHours_FridayEndTextview.setText(fridayCloseTime);
                                    }
                                }

                                if (hours.containsKey("Saturday")) {
                                    Map<String, Object> saturdayHours = (Map<String, Object>) hours.get("Saturday");
                                    if (saturdayHours.containsKey("openTime")) {
                                        String saturdayStartTime = saturdayHours.get("openTime").toString();
                                        CWSHours_SaturdayStartTextview.setText(saturdayStartTime);
                                    }
                                    if (saturdayHours.containsKey("closeTime")) {
                                        String saturdayCloseTime = saturdayHours.get("closeTime").toString();
                                        CWSHours_SaturdayEndTextview.setText(saturdayCloseTime);
                                    }
                                }

                                if (hours.containsKey("Sunday")) {
                                    Map<String, Object> sundayHours = (Map<String, Object>) hours.get("Sunday");
                                    if (sundayHours.containsKey("openTime")) {
                                        String sundayStartTime = sundayHours.get("openTime").toString();
                                        CWSHours_SundayStartTextview.setText(sundayStartTime);
                                    }
                                    if (sundayHours.containsKey("closeTime")) {
                                        String sundayCloseTime = sundayHours.get("closeTime").toString();
                                        CWSHours_SundayEndTextview.setText(sundayCloseTime);
                                    }
                                }

                                if (image == null || image.isEmpty()){
                                    Picasso.get().load(R.drawable.uploadphoto).into(ProfPage_Image);
                                }else{Picasso.get().load(image).into(ProfPage_Image);}
                                ProfPage_Title.setText(name);
                                ProfPage_TitleSmall.setText(name);
                                ProfPage_Location.setText(streetLocation + " " + cityLocation);
                                ProfPage_AboutContent.setText(about);
                                ProfPage_ContactInfo.setText(contact);
                                if (floorMapPic == null || floorMapPic.isEmpty()){
                                    Picasso.get().load(R.drawable.uploadphoto).into(ProfPage_FloorMap_Imageview);
                                }else{
                                    Picasso.get().load(floorMapPic).into(ProfPage_FloorMap_Imageview);
                                }
                                ProfPage_FloorMapDescription.setText(floorMapDesc);
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

