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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.Owner.ProDisc.OwnerProDisc_ModelClass;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.CoworkingSpaces;
import com.example.caspaceapplication.customer.CustHomePromotions_Adapter;
import com.example.caspaceapplication.customer.TechnologyHubs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private List<OwnerProDisc_ModelClass> promotionList;
    private RecyclerView promotionsRecyclerView;
    private CustHomePromotions_Adapter promotionsAdapter;

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

        imageview_techhubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TechnologyHubs.class);
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
        promotionsAdapter = new CustHomePromotions_Adapter(getActivity(),promotionList);
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
}