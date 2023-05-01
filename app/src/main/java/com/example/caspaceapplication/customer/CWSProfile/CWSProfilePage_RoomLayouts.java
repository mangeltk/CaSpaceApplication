package com.example.caspaceapplication.customer.CWSProfile;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.Owner.OfficeLayouts.OfficeLayout_DataClass;
import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CWSProfilePage_RoomLayouts extends AppCompatActivity {

    RecyclerView recyclerView;
    List<OfficeLayout_DataClass> dataClassList;
    CWSProfilePage_RoomLayouts_Adapter cwsProfilePageRoomLayoutsAdapter;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference colref = firebaseFirestore.collection("OfficeLayouts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cwsprofile_page_room_layouts);

        // Get the intent that started the activity
        Intent intent = getIntent();
        String cospaceName = intent.getStringExtra("cospaceName");
        String owner_id = intent.getStringExtra("owner_id");

        SearchView searchView = findViewById(R.id.searchView);
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

        //Person capacity spinner
        Spinner personCapacitySpinner = findViewById(R.id.personCapacity_spinner);
        String[] personCapacityOptions = {"Select","1", "2", "3", "4", "5","6", "7", "8", "9", "10 above"};
        ArrayAdapter<String> personCapAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, personCapacityOptions);
        personCapAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personCapacitySpinner.setAdapter(personCapAdapter);
        personCapacitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPersonCap = parent.getItemAtPosition(position).toString().trim();

                if (!selectedPersonCap.equals("Select")){
                    displayPersonCapacityResults(selectedPersonCap, owner_id);
                }else{
                    displayAll(owner_id);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                displayAll(owner_id);
            }
        });
        personCapacitySpinner.setSelection(0);

        //Area size spinner
        colref.whereEqualTo("owner_id", owner_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int maxAreaSize = Integer.MIN_VALUE; // initialize to smallest possible value
                int minAreaSize = Integer.MAX_VALUE; // initialize to largest possible value

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    String areasizeStored = documentSnapshot.getString("layoutAreasize");

                    if (areasizeStored != null) {
                        int areasizeInt = Integer.parseInt(areasizeStored.trim());
                        if (areasizeInt > maxAreaSize) {
                            maxAreaSize = areasizeInt;
                        }
                        if (areasizeInt < minAreaSize) {
                            minAreaSize = areasizeInt;
                        }
                    }
                }
                // Do something with the max and min values
                // Create the area size options array
                Spinner areaSizeSpinner = findViewById(R.id.areaSize_dropdown);
                String[] areaSizeOptions = new String[maxAreaSize - minAreaSize + 2];
                areaSizeOptions[0] = "Select";
                for (int i = 1; i <= maxAreaSize - minAreaSize + 1; i++) {
                    areaSizeOptions[i] = String.valueOf(minAreaSize + i - 1);
                }
                    areaSizeOptions[maxAreaSize - minAreaSize + 1] = maxAreaSize+"";

                // Populate the areaSizeSpinner with the options
                Context context = CWSProfilePage_RoomLayouts.this;
                ArrayAdapter<String> areaSizeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, areaSizeOptions);
                areaSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSizeSpinner.setAdapter(areaSizeAdapter);
                areaSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedAreasize = parent.getItemAtPosition(position).toString().trim();

                        if (!selectedAreasize.equals("Select")){
                            displayAreaSizeResults(selectedAreasize, owner_id);
                        }else{
                            displayAll(owner_id);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });

        //Availability spinner
        Spinner availabilitySpinner = findViewById(R.id.availability_dropdown);
        String[] availabilityOptions = {"Select", "Available", "Not available"};
        ArrayAdapter<String> availabilityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availabilityOptions);
        availabilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        availabilitySpinner.setAdapter(availabilityAdapter);
        availabilitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAvailability = parent.getItemAtPosition(position).toString().trim();

                if (!selectedAvailability.equals("Select")) {
                    if (selectedAvailability.equals("Available")) {
                        displayAvailabilityResults(selectedAvailability, owner_id);
                    } else if (selectedAvailability.equals("Not available")) {
                        displayAvailabilityResults(selectedAvailability, owner_id);
                    }
                } else {
                    displayAll(owner_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                displayAll(owner_id);
            }
        });availabilitySpinner.setSelection(0);

        recyclerView = findViewById(R.id.recyclerview_CWSProfPage_RoomLayouts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataClassList = new ArrayList<>();
        cwsProfilePageRoomLayoutsAdapter = new CWSProfilePage_RoomLayouts_Adapter(dataClassList);
        recyclerView.setAdapter(cwsProfilePageRoomLayoutsAdapter);

    }

    public void displayAll(String ownerId){
        colref.whereEqualTo("owner_id", ownerId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        dataClassList.clear();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            OfficeLayout_DataClass modelClass = documentSnapshot.toObject(OfficeLayout_DataClass.class);
                            dataClassList.add(modelClass);
                        }
                        cwsProfilePageRoomLayoutsAdapter.notifyDataSetChanged();
                    }
                });
    }

    private  void displayPersonCapacityResults(String selectedCapacity, String ownerID){
        //todo: display layouts method based on user selected in spinner layoutPersonCapacity (TO BE MODIFIED)
        List<OfficeLayout_DataClass> filteredList = new ArrayList<>();//initialize new data class for here
        //dataClassList.clear();
        /*for (OfficeLayout_DataClass data : dataClassList){
            if (data.getLayoutPersonCapacity().toLowerCase().contains(selectedCapacity.toLowerCase())){
                filteredList.add(data);
            }
            else {
                displayAll(ownerID);
            }
        }cwsProfilePageRoomLayoutsAdapter.setFilteredList(filteredList);*/

    }

    private void displayAreaSizeResults(String areaSize, String ownerID){
        //todo: display layouts method based on user selected in spinner area size
        colref.whereEqualTo("owner_id", ownerID).whereEqualTo("layoutAreasize", areaSize)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                       dataClassList.clear();
                       for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                           OfficeLayout_DataClass modelClass = documentSnapshot.toObject(OfficeLayout_DataClass.class);
                           dataClassList.add(modelClass);
                       }
                       cwsProfilePageRoomLayoutsAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void displayAvailabilityResults(String availability, String ownerID){
        //todo: display layouts method based on user selected in spinner availability
        Query query;

        if (availability.equals("Available")){
            query = colref.whereEqualTo("owner_id", ownerID)
                    .whereEqualTo("layoutAvailability", "Available");
        } else {
            query = colref.whereEqualTo("owner_id", ownerID)
                    .whereEqualTo("layoutAvailability", "Not available");
        }
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<OfficeLayout_DataClass> layoutList = queryDocumentSnapshots.toObjects(OfficeLayout_DataClass.class);
                    CWSProfilePage_RoomLayouts_Adapter adapter = new CWSProfilePage_RoomLayouts_Adapter(layoutList);
                    recyclerView.setAdapter(adapter);
                } else {
                    displayAll(ownerID);
                    Toast.makeText(CWSProfilePage_RoomLayouts.this, "No layouts found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting layouts: ", e);
                Toast.makeText(CWSProfilePage_RoomLayouts.this, "Error getting layouts", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void searchList(String text){
        List<OfficeLayout_DataClass> dataSearchList = new ArrayList<>();//initialize new data class for here
        for (OfficeLayout_DataClass data : dataClassList){
            if (data.getLayoutName().toLowerCase().contains(text.toLowerCase())){
                dataSearchList.add(data);
            }
        }cwsProfilePageRoomLayoutsAdapter.setSearchList(dataSearchList);
        if (dataSearchList.isEmpty()){
            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
        }
    }

    public class CWSProfilePage_RoomLayouts_Adapter extends RecyclerView.Adapter<CWSProfilePage_RoomLayouts_Adapter.ViewHolder> {

        private List<OfficeLayout_DataClass> dataClass;

        public void setSearchList(List<OfficeLayout_DataClass> dataSearchList){
            this.dataClass = dataSearchList;
            notifyDataSetChanged();
        }

        public void setFilteredList(List<OfficeLayout_DataClass> filteredList) {
            dataClass = filteredList;
            notifyDataSetChanged();
        }


        public CWSProfilePage_RoomLayouts_Adapter(List<OfficeLayout_DataClass> dataClass) {
            this.dataClass = dataClass;
        }

        @NonNull
        @Override
        public CWSProfilePage_RoomLayouts_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem_cwsprofile_pageroomlayouts, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CWSProfilePage_RoomLayouts_Adapter.ViewHolder holder, int position) {

            String imageUri = String.valueOf(dataClass.get(position).getLayoutImage());
                Picasso.get().load(imageUri).into(holder.LayoutImage_Imageview);
            holder.LayoutTitle_Textview.setText(dataClass.get(position).getLayoutName());
            //holder.PersonCapacity_Textview.setText(dataClass.get(position).getLayoutPersonCapacity());
            holder.Areasize_Textview.setText(dataClass.get(position).getLayoutAreasize());
            holder.Availability_Textview.setText(dataClass.get(position).getLayoutAvailability());
            holder.SeeMoreDetails_Textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextPosition = holder.getAdapterPosition();
                    OfficeLayout_DataClass model = new OfficeLayout_DataClass();

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.recycleitem_cwsprofile_pageroomlayouts_popupdetails, null);

                    TextView LayoutTitle_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_LayoutTitle_TextviewDETAILS);
                    ImageView LayoutImage_ImageviewDETAILS = dialogView.findViewById(R.id.CWSPP_LayoutImage_ImageviewDETAILS);
                    TextView Availability_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_Availability_TextviewDETAILS);
                    //TextView PersonCapacity_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_PersonCapacity_TextviewDETAILS);
                    TextView Areasize_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_Areasize_TextviewDETAILS);
                    TextView HourlyRate_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_HourlyRate_TextviewDETAILS);
                    TextView DailyRate_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_DailyRate_TextviewDETAILS);
                    TextView WeeklyRate_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_WeeklyRate_TextviewDETAILS);
                    TextView MonthyRate_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_MonthyRate_TextviewDETAILS);
                    TextView AnnualRate_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_AnnualRate_TextviewDETAILS);
                    AppCompatButton BookNow_AppComButtonDETAILS = dialogView.findViewById(R.id.CWSPP_BookNow_AppComButtonDETAILS);
                    AppCompatButton SeeOther_AppComButtonDETAILS = dialogView.findViewById(R.id.CWSPP_SeeOther_AppComButtonDETAILS);

                    LayoutTitle_TextviewDETAILS.setText(dataClass.get(nextPosition).getLayoutName());
                    String imageUri = String.valueOf(dataClass.get(nextPosition).getLayoutImage());
                    if (imageUri != null && !imageUri.isEmpty()) {
                        Picasso.get().load(imageUri).into(LayoutImage_ImageviewDETAILS);
                    }
                    Availability_TextviewDETAILS.setText(dataClass.get(nextPosition).getLayoutAvailability());
                    //PersonCapacity_TextviewDETAILS.setText(dataClass.get(nextPosition).getLayoutPersonCapacity());
                    Areasize_TextviewDETAILS.setText(dataClass.get(nextPosition).getLayoutAreasize());
                    HourlyRate_TextviewDETAILS.setText(dataClass.get(nextPosition).getLayoutHourlyPrice());
                    DailyRate_TextviewDETAILS.setText(dataClass.get(nextPosition).getLayoutDailyPrice());
                    WeeklyRate_TextviewDETAILS.setText(dataClass.get(nextPosition).getLayoutWeeklyPrice());
                    MonthyRate_TextviewDETAILS.setText(dataClass.get(nextPosition).getLayoutMonthlyPrice());
                    AnnualRate_TextviewDETAILS.setText(dataClass.get(nextPosition).getLayoutAnnualPrice());

                    //todo: book redirect

                    BookNow_AppComButtonDETAILS.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });


                    builder.setView(dialogView);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    SeeOther_AppComButtonDETAILS.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Toast.makeText(CWSProfilePage_RoomLayouts.this, "Other layouts", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });


        }

        @Override
        public int getItemCount() {
            return dataClass.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView LayoutImage_Imageview;
            TextView LayoutTitle_Textview, PersonCapacity_Textview, Areasize_Textview, Availability_Textview, SeeMoreDetails_Textview;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                LayoutImage_Imageview = itemView.findViewById(R.id.CWSPP_LayoutImage_Imageview);
                LayoutTitle_Textview = itemView.findViewById(R.id.CWSPP_LayoutTitle_Textview);
                PersonCapacity_Textview = itemView.findViewById(R.id.CWSPP_PersonCapacity_Textview);
                Areasize_Textview = itemView.findViewById(R.id.CWSPP_Areasize_Textview);
                Availability_Textview = itemView.findViewById(R.id.CWSPP_Availability_Textview);
                SeeMoreDetails_Textview = itemView.findViewById(R.id.CWSPP_SeeMoreDetails_Textview);

            }
        }
    }


}