package com.example.caspaceapplication.customer;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.caspaceapplication.Owner.ProDisc.OwnerProDisc_ModelClass;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.fragments.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CoworkingSpaces extends AppCompatActivity {


    ImageButton backButton;

    RecyclerView recyclerView;
    ArrayList<CoworkingSpacesModel> coworkingSpacesModelArrayList;
    CoworkingSpacesAdapter coworkingSpacesAdapter;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;

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




        /*progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data");
        progressDialog.show();*/


        recyclerView = findViewById(R.id.recycler_cws);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
        coworkingSpacesModelArrayList = new ArrayList<>();
        coworkingSpacesAdapter = new CoworkingSpacesAdapter(CoworkingSpaces.this, coworkingSpacesModelArrayList);

        recyclerView.setAdapter(coworkingSpacesAdapter);

        retrieveCoworkingSpaces();




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

/*    private void EventChangeListener() {

        db.collection("CospaceBranches").orderBy("cospaceName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){

                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for(DocumentChange dc: value.getDocumentChanges()){
                            if(dc.getType()==DocumentChange.Type.ADDED)
                            {
                                coworkingSpacesModelArrayList.add(dc.getDocument().toObject(CoworkingSpacesModel.class));

                            }

                            coworkingSpacesAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                });*/
    }
}