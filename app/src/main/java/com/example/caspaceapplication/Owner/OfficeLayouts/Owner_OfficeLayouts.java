package com.example.caspaceapplication.Owner.OfficeLayouts;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.Owner.OwnerHomepage;
import com.example.caspaceapplication.Owner.Profile.Owner_Profile;
import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Owner_OfficeLayouts extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView navigationView;

    RecyclerView recyclerView;
    List<OfficeLayout_DataClass> dataClassList;
    OfficeLayout_AdapterClass layout_adapterClass;
    SearchView searchView;
    FloatingActionButton addalayoutButton;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_office_layouts);

        firebaseFirestore = FirebaseFirestore.getInstance();

        addalayoutButton = findViewById(R.id.addFloatButton);
        addalayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Owner_OfficeLayouts.this, Owner_OfficelayoutsRegistration.class));
            }
        });

        //Search view bar------------------------------------------
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        // Initialize the bottom navigation bar
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(this);




        //Recyclerview------------------------------------------
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataClassList = new ArrayList<>();
        layout_adapterClass = new OfficeLayout_AdapterClass(this, dataClassList);
        recyclerView.setAdapter(layout_adapterClass);

        getOfficeLayoutList();//class called to display the recyclerview
    }

    public void getOfficeLayoutList() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        firebaseFirestore.collection("OfficeLayouts")
                .whereEqualTo("owner_id",currentUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Log.d(TAG, "Document data: " + documentSnapshot.getData());
                            OfficeLayout_DataClass modelClass = documentSnapshot.toObject(OfficeLayout_DataClass.class);
                            dataClassList.add(modelClass);
                        }
                        layout_adapterClass.notifyDataSetChanged();
                    }
                });
    }

    //For search bar---------------------------------------
    public void searchList(String text){
        List<OfficeLayout_DataClass> dataSearchList = new ArrayList<>();
        for (OfficeLayout_DataClass data: dataClassList){
            if (data.getLayoutName().toLowerCase().contains(text.toLowerCase())){
                dataSearchList.add(data);
            }
        }layout_adapterClass.setSearchList(dataSearchList);
        if (dataSearchList.isEmpty()){
            Toast.makeText(this, "Layout not found", Toast.LENGTH_SHORT).show();
            }
        }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHome:
                startActivity(new Intent(this, OwnerHomepage.class));
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuMessages:
                // startActivity(new Intent(this, MessageActivity.class));
                return true;
            case R.id.menuNotification:
                // startActivity(new Intent(this, NotificationActivity.class));
                return true;
            case R.id.menuProfile:
                startActivity(new Intent(this, Owner_Profile.class));
                return true;
            default:
                return false;
        }
    }
}