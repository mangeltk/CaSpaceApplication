package com.example.caspaceapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class Owner_OfficeLayouts extends AppCompatActivity {

    BottomNavigationView navigationView;

    RecyclerView recyclerView;
    List<OfficeLayout_DataClass> dataClassList;
    OfficeLayout_AdapterClass layout_adapterClass;
    OfficeLayout_DataClass layout_dataClass;
    SearchView searchView;

    Button addalayoutButton;

    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_office_layouts);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        dataClassList = new ArrayList<>();
        addalayoutButton = findViewById(R.id.addalayout_Button);

        addalayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Owner_OfficeLayouts.this, Owner_OfficelayoutsRegistration.class));
            }
        });

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //searchList(newText);
                return false;
            }
        });

        //Navigation Bar------------------------------------------
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        Toast.makeText(Owner_OfficeLayouts.this, "Home", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Owner_OfficeLayouts.this, OwnerHomepage.class));
                        break;
                    case R.id.menuMessages:
                        Toast.makeText(Owner_OfficeLayouts.this, "Messages", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuNotification:
                        Toast.makeText(Owner_OfficeLayouts.this, "Notifications", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuProfile:
                        Toast.makeText(Owner_OfficeLayouts.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
                return true;
            }
        });//Navigation Bar------------------------------------------

        /*GridLayoutManager gridLayoutManager = new GridLayoutManager(Owner_OfficeLayouts.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataClassList = new ArrayList<>();

        data = new OfficeLayout_DataClass("Layout name 1", "1-2 people","20 m. sq." , R.drawable.layout_sample);
        dataClassList.add(data);
        data = new OfficeLayout_DataClass("Layout name 2", "2-3 people","20 m. sq." ,R.drawable.layout_sample);
        dataClassList.add(data);
        data = new OfficeLayout_DataClass("Layout name 3", "3-4 people","20 m. sq." ,R.drawable.layout_sample);
        dataClassList.add(data);
        data = new OfficeLayout_DataClass("Layout name 4", "4-5 people","20 m. sq." ,R.drawable.layout_sample);
        dataClassList.add(data);

        officeLayout_adapterClass = new OfficeLayout_AdapterClass(Owner_OfficeLayouts.this, dataClassList);
        recyclerView.setAdapter(officeLayout_adapterClass);*/

        /*CollectionReference dataref = firebaseFirestore.collection("OfficeLayouts");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("OfficeLayouts");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("LayoutImages");*/

        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Query query = dataref.whereEqualTo("owner_id", user.getUid());
        }*/

        CollectionReference collectionReference = firebaseFirestore.collection("OfficeLayouts");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataClassList =  new ArrayList<>();
        layout_adapterClass = new OfficeLayout_AdapterClass(this, dataClassList);
        recyclerView.setAdapter(layout_adapterClass);

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Log.d(TAG, "Document data: " + documentSnapshot.getData());
                    OfficeLayout_DataClass modelClass  = documentSnapshot.toObject(OfficeLayout_DataClass.class);
                    dataClassList.add(modelClass);
                }
                layout_adapterClass.notifyDataSetChanged();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //handle error
                    }
                });

    }

    //For search bar (still needs to e fixed keep reapeting the error message whatever you input)
    /*public void searchList(String text){
        List<OfficeLayout_DataClass> dataSearchList = new ArrayList<>();
        for (OfficeLayout_DataClass data: dataSearchList){
            if (data.getLayoutName().toLowerCase().contains(text.toLowerCase())){
                dataSearchList.add(data);
            }
        }
        officeLayout_adapterClass.setSearchList(dataSearchList);
    if (dataSearchList.isEmpty()){
        Toast.makeText(this, "ghorl not found", Toast.LENGTH_SHORT).show();
    }else{
        officeLayout_adapterClass.setSearchList(dataSearchList);
    }
    }*/


}