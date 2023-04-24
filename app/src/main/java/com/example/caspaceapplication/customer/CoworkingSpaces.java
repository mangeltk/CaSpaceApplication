package com.example.caspaceapplication.customer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.example.caspaceapplication.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CoworkingSpaces extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<CoworkingSpacesModel> coworkingSpacesModelArrayList;
    CoworkingSpacesAdapter coworkingSpacesAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coworking_spaces);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data");
        progressDialog.show();

        recyclerView = findViewById(R.id.recycler_cws);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        coworkingSpacesModelArrayList = new ArrayList<>();
        coworkingSpacesAdapter = new CoworkingSpacesAdapter(CoworkingSpaces.this, coworkingSpacesModelArrayList);

        recyclerView.setAdapter(coworkingSpacesAdapter);

        EventChangeListener();


    }

    private void EventChangeListener() {

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
                });
    }
}