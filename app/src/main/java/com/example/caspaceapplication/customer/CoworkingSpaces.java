package com.example.caspaceapplication.customer;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.Owner.BranchModel;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.SearchManagement.Customer_SearchManagement;
import com.example.caspaceapplication.fragments.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CoworkingSpaces extends AppCompatActivity {

    AppCompatImageView backButton;

    RecyclerView recyclerView;
    ArrayList<BranchModel> branchModelArrayList;
    CoworkingSpacesAdapter coworkingSpacesAdapter;
    FirebaseFirestore firebaseFirestore;

    SearchView search_CWS;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coworking_spaces);

        AppCompatButton clickButton = findViewById(R.id.clickButton);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CoworkingSpaces.this, Customer_SearchManagement.class));
            }
        });

        recyclerView = findViewById(R.id.recycler_cws);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        firebaseFirestore = FirebaseFirestore.getInstance();
        branchModelArrayList = new ArrayList<>();
        coworkingSpacesAdapter = new CoworkingSpacesAdapter(this, branchModelArrayList);
        recyclerView.setAdapter(coworkingSpacesAdapter);
        retrieveCoworkingSpaces();

        search_CWS = findViewById(R.id.searchCWS);
        search_CWS.clearFocus();
        search_CWS.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return false;
            }
        });

    }

    public void retrieveCoworkingSpaces(){

        String PrivateOffice = "Office space exclusive for an organization";
        String IndivOffice = "Office space of individual";
        String other = "Individual desk or table for 4 people";

        firebaseFirestore.collection("CospaceBranches")
                .whereEqualTo("cospaceCategory", PrivateOffice)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Log.d(TAG, "Document data: " + documentSnapshot.getData());
                            BranchModel modelClass = documentSnapshot.toObject(BranchModel.class);
                            branchModelArrayList.add(modelClass);
                        }
                        coworkingSpacesAdapter.notifyDataSetChanged();
                    }
                });

        firebaseFirestore.collection("CospaceBranches")
                .whereEqualTo("cospaceCategory", IndivOffice)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Log.d(TAG, "Document data: " + documentSnapshot.getData());
                            BranchModel modelClass = documentSnapshot.toObject(BranchModel.class);
                            branchModelArrayList.add(modelClass);
                        }
                        coworkingSpacesAdapter.notifyDataSetChanged();
                    }
                });

        firebaseFirestore.collection("CospaceBranches")
                .whereEqualTo("cospaceCategory", other)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Log.d(TAG, "Document data: " + documentSnapshot.getData());
                            BranchModel modelClass = documentSnapshot.toObject(BranchModel.class);
                            branchModelArrayList.add(modelClass);
                        }
                        coworkingSpacesAdapter.notifyDataSetChanged();
                    }
                });

    }
    public void searchList(String text){
        List<BranchModel> dataSearchList = new ArrayList<>();
        for (BranchModel data: branchModelArrayList){
            if (data.getCospaceName().toLowerCase().contains(text.toLowerCase())){
                dataSearchList.add(data);
            }
        }coworkingSpacesAdapter.setSearchList(dataSearchList);
        if (dataSearchList.isEmpty()){
            Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
        }

    }

}