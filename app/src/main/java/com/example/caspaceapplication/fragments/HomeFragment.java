package com.example.caspaceapplication.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.Owner.BranchModel;
import com.example.caspaceapplication.Owner.ProDisc.OwnerProDisc_ModelClass;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.CoworkingSpaces;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private List<OwnerProDisc_ModelClass> promotionList;
    private RecyclerView promotionsRecyclerView;
    private CustHomePage_Prodisc_Adapter promotionsAdapter;
    private CustHome_RecommendedList_Adapter recommendedList_adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageview_cws, imageview_techhubs;

        imageview_cws = view.findViewById(R.id.imageview_cws);
        imageview_techhubs = view.findViewById(R.id.imageview_techhubs);

        imageview_cws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CoworkingSpaces.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView  = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();


        TextView displayHomepageCustName = rootView.findViewById(R.id.displayHomepageCustName_Textview);
        firebaseFirestore.collection("CustomerUserAccounts")
                .document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String fname = documentSnapshot.getString("customersFirstName");
                            String lname = documentSnapshot.getString("customersLastName");
                            displayHomepageCustName.setText(fname + " " + lname);
                        }
                    }
                });


        promotionList = new ArrayList<>();

        // Initialize RecyclerView and adapter
        promotionsRecyclerView = rootView.findViewById(R.id.custHomePromotionsRecyclerview);
        promotionsAdapter = new CustHomePage_Prodisc_Adapter(promotionList);
        promotionsRecyclerView.setAdapter(promotionsAdapter);

        retrievePromotions();

        return rootView ;
    }

    public void retrievePromotions(){
        // Retrieve promotions from Firestore
        firebaseFirestore.collection("OwnerPublishedPromotions")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Log.d(TAG, "Document data: " + documentSnapshot.getData());
                            OwnerProDisc_ModelClass modelClass = documentSnapshot.toObject(OwnerProDisc_ModelClass.class);
                            promotionList.add(modelClass);
                        }
                        promotionsAdapter.notifyDataSetChanged();
                    }
                });


    }

    public class CustHomePage_Prodisc_Adapter extends RecyclerView.Adapter<HomeFragment.CustHomePage_Prodisc_Adapter.ViewHolder>{

        private List<OwnerProDisc_ModelClass> dataClass;

        public CustHomePage_Prodisc_Adapter(List<OwnerProDisc_ModelClass> dataClass) {
            this.dataClass = dataClass;
        }

        @NonNull
        @Override
        public CustHomePage_Prodisc_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyleritem_prodisc, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustHomePage_Prodisc_Adapter.ViewHolder holder, int position) {
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

    public class CustHome_RecommendedList_Adapter extends  RecyclerView.Adapter<HomeFragment.CustHome_RecommendedList_Adapter.ViewHolder>{

        private List<BranchModel> dataClassList;

        public CustHome_RecommendedList_Adapter(List<BranchModel> dataClassList) {
            this.dataClassList = dataClassList;
        }

        @NonNull
        @Override
        public CustHome_RecommendedList_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull CustHome_RecommendedList_Adapter.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return dataClassList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}