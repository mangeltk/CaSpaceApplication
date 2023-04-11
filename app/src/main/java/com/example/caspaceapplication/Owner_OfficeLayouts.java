package com.example.caspaceapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Owner_OfficeLayouts extends AppCompatActivity {

    RecyclerView recyclerView;
    List<OfficeLayout_DataClass> dataClassList;
    OfficeLayout_AdapterClass officeLayout_adapterClass;
    OfficeLayout_DataClass data;
    SearchView searchView;

    Button editDetails;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_office_layouts);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        firebaseFirestore = FirebaseFirestore.getInstance();

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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Owner_OfficeLayouts.this, 1);
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
        recyclerView.setAdapter(officeLayout_adapterClass);



    }

    public void searchList(String text){
        List<OfficeLayout_DataClass> dataSearchList = new ArrayList<>();
        for (OfficeLayout_DataClass data: dataSearchList){
            if (data.getLayoutName().toLowerCase().contains(text.toLowerCase())){
                dataSearchList.add(data);
            }
        }

        if (dataSearchList.isEmpty()){
            Toast.makeText(this, "ghorl not found", Toast.LENGTH_SHORT).show();
        }else{
            officeLayout_adapterClass.setSearchList(dataSearchList);
        }

    }

}