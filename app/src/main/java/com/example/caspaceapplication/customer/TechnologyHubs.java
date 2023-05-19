package com.example.caspaceapplication.customer;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

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
import java.util.Locale;

public class TechnologyHubs extends AppCompatActivity {

    ImageButton backButton;

    RecyclerView recyclerView;
    ArrayList<BranchModel> branchModelArrayList;
    TechnologyHubsAdapter technologyHubsAdapter;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;

    SearchView search_techhubs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technology_hubs);


        AppCompatButton location_Button = findViewById(R.id.clickButtonTechHubLocation);
        location_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TechnologyHubs.this, Customer_SearchManagement.class));
            }
        });

        recyclerView = findViewById(R.id.recycler_cwstechhub);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(TechnologyHubs.this, 2));

        firebaseFirestore = FirebaseFirestore.getInstance();
        branchModelArrayList = new ArrayList<>();
        technologyHubsAdapter = new TechnologyHubsAdapter(TechnologyHubs.this, branchModelArrayList);
        recyclerView.setAdapter(technologyHubsAdapter);
        retrieveTechnologyHubs();

        search_techhubs = findViewById(R.id.searchCWStechhubs);
        search_techhubs.clearFocus();
        search_techhubs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void retrieveTechnologyHubs() {

        String FabLab = "Fabrication Laboratory (Fab lab)";
        String QBO = "QBO Innovation Hub";
        String TBI = "Technology Business Incubation Program (TBI)";

        firebaseFirestore.collection("CospaceBranches")
                .whereEqualTo("cospaceCategory", FabLab)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                        {
                            Log.d(TAG, "Document data: " + documentSnapshot.getData());
                            BranchModel modelClass = documentSnapshot.toObject(BranchModel.class);
                            branchModelArrayList.add(modelClass);
                        }
                        technologyHubsAdapter.notifyDataSetChanged();
                    }
                });

        firebaseFirestore.collection("CospaceBranches")
                .whereEqualTo("cospaceCategory", QBO)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                        {
                            Log.d(TAG, "Document data: " + documentSnapshot.getData());
                            BranchModel modelClass = documentSnapshot.toObject(BranchModel.class);
                            branchModelArrayList.add(modelClass);
                        }
                        technologyHubsAdapter.notifyDataSetChanged();
                    }
                });

        firebaseFirestore.collection("CospaceBranches")
                .whereEqualTo("cospaceCategory", TBI)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                        {
                            Log.d(TAG, "Document data: " + documentSnapshot.getData());
                            BranchModel modelClass = documentSnapshot.toObject(BranchModel.class);
                            branchModelArrayList.add(modelClass);
                        }
                        technologyHubsAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void searchList(String text)
    {
        List<BranchModel> dataSearchList = new ArrayList<>();
        for (BranchModel data: branchModelArrayList){
            if (data.getCospaceName().toLowerCase().contains(text.toLowerCase())){
                dataSearchList.add(data);
            }
        }technologyHubsAdapter.setSearchList(dataSearchList);
        if (dataSearchList.isEmpty()){
            Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
        }
    }
}