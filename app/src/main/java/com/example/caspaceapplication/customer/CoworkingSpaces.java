package com.example.caspaceapplication.customer;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    ImageButton backButton;

    RecyclerView recyclerView;
    ArrayList<CoworkingSpacesModel> coworkingSpacesModelArrayList;
    CoworkingSpacesAdapter coworkingSpacesAdapter;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;

    SearchView search_CWS;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coworking_spaces);


        backButton = findViewById(R.id.backImageButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment myFragment = new HomeFragment();

                // Replace the current fragment with the new fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, myFragment)
                        .commit();
            }
        });

        ImageView location_Button = findViewById(R.id.locationButton);
        location_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CoworkingSpaces.this, Customer_SearchManagement.class));
            }
        });



        /*progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data");
        progressDialog.show();*/


        recyclerView = findViewById(R.id.recycler_cws);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        firebaseFirestore = FirebaseFirestore.getInstance();
        coworkingSpacesModelArrayList = new ArrayList<>();
        coworkingSpacesAdapter = new CoworkingSpacesAdapter(CoworkingSpaces.this, coworkingSpacesModelArrayList);

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
        // Retrieve promotions from Firestore
        firebaseFirestore.collection("CospaceBranches")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Log.d(TAG, "Document data: " + documentSnapshot.getData());
                            CoworkingSpacesModel modelClass = documentSnapshot.toObject(CoworkingSpacesModel.class);
                            coworkingSpacesModelArrayList.add(modelClass);
                        }
                        coworkingSpacesAdapter.notifyDataSetChanged();
                    }
                });

    }
    public void searchList(String text){
        List<CoworkingSpacesModel> dataSearchList = new ArrayList<>();
        for (CoworkingSpacesModel data: coworkingSpacesModelArrayList){
            if (data.getCospaceName().toLowerCase().contains(text.toLowerCase())){
                dataSearchList.add(data);
            }
        }coworkingSpacesAdapter.setSearchList(dataSearchList);
        if (dataSearchList.isEmpty()){
            Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
        }

    }

}