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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.Owner.OfficeLayouts.OfficeLayout_DataClass;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.BookingManagement.CustomerBookingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

        AppCompatButton showFilterBar = findViewById(R.id.showFilterButton);
        AppCompatButton hideFilterBar = findViewById(R.id.hideFilterButton);

        LinearLayout filterBar = findViewById(R.id.filterBar);
        filterBar.setVisibility(View.GONE);

        showFilterBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBar.setVisibility(View.VISIBLE);
            }
        });

        //display all
        displayAll(owner_id);

        //Person capacity spinner
        Spinner personMinCapacitySpinner = findViewById(R.id.personMinCapacity_spinner);
        ArrayList<String> minOptions = new ArrayList<>();
        minOptions.add("Min");

        Spinner personMaxCapacitySpinner = findViewById(R.id.personMaxCapacity_spinner);
        ArrayList<String> maxOptions = new ArrayList<>();
        maxOptions.add("Max");
        colref.whereEqualTo("owner_id", owner_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int min_MinPersonCap = Integer.MIN_VALUE; // initialize to smallest possible value
                int max_MinPersonCap = Integer.MAX_VALUE; // initialize to largest possible value
                int min_MaxPersonCap = Integer.MIN_VALUE; // initialize to smallest possible value
                int max_MaxPersonCap = Integer.MAX_VALUE; // initialize to largest possible value

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    String minPersonCapStored =  documentSnapshot.getString("minCapacity");
                    String maxPersonCapStored =  documentSnapshot.getString("maxCapacity");

                    // Convert capacity values to integers
                    int minPersonCap = Integer.parseInt(minPersonCapStored);
                    int maxPersonCap = Integer.parseInt(maxPersonCapStored);

                    // Update minimum capacity variables
                    if (minPersonCap > min_MinPersonCap) {
                        min_MinPersonCap = minPersonCap;
                    }

                    if (minPersonCap < max_MinPersonCap) {
                        max_MinPersonCap = minPersonCap;
                    }

                    // Check if the value is already present in the ArrayList before adding it
                    if (!minOptions.contains(String.valueOf(minPersonCap))) {
                        minOptions.add(String.valueOf(minPersonCap));
                    }

                    // Update maximum capacity variables
                    if (maxPersonCap > min_MaxPersonCap) {
                        min_MaxPersonCap = maxPersonCap;
                    }

                    if (maxPersonCap < max_MaxPersonCap) {
                        max_MaxPersonCap = maxPersonCap;
                    }

                    if (!maxOptions.contains(String.valueOf(maxPersonCap))) {
                        maxOptions.add(String.valueOf(maxPersonCap));
                    }
                }

                Context context = CWSProfilePage_RoomLayouts.this;

                Collections.sort(minOptions.subList(1, minOptions.size()));
                ArrayAdapter<String> minAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, minOptions);
                minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                personMinCapacitySpinner.setAdapter(minAdapter);
                personMinCapacitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedMinPersonCap = parent.getItemAtPosition(position).toString().trim();
                        if (!selectedMinPersonCap.equals("Min")){
                            displayMinPersonCapacityResults(selectedMinPersonCap, owner_id);
                        }else{
                            displayAll(owner_id);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


                Collections.sort(maxOptions.subList(1, maxOptions.size()));
                ArrayAdapter<String> maxAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, maxOptions);
                maxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                personMaxCapacitySpinner.setAdapter(maxAdapter);
                personMaxCapacitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedMaxPersonCap = parent.getItemAtPosition(position).toString().trim();
                        if (!selectedMaxPersonCap.equals("Max")){
                            displayMaxPersonCapacityResults(selectedMaxPersonCap, owner_id);
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

        //Area size spinner
        Spinner areaSizeSpinner = findViewById(R.id.areaSize_dropdown);
        colref.whereEqualTo("owner_id", owner_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int minAreaSize = Integer.MAX_VALUE; // initialize to largest possible value
                int maxAreaSize = Integer.MIN_VALUE; // initialize to smallest possible value

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

                String[] areaSizeOptions = new String[maxAreaSize - minAreaSize + 2];
                areaSizeOptions[0] = "Select";
                for (int i = 1; i <= maxAreaSize - minAreaSize + 1; i++) {
                    areaSizeOptions[i] = String.valueOf(minAreaSize + i - 1);
                }
                    areaSizeOptions[maxAreaSize - minAreaSize + 1] = maxAreaSize+"";

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

        hideFilterBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBar.setVisibility(View.GONE);
                displayAll(owner_id);
                personMinCapacitySpinner.setSelection(0);
                personMaxCapacitySpinner.setSelection(0);
                availabilitySpinner.setSelection(0);
                areaSizeSpinner.setSelection(0);

                Toast.makeText(CWSProfilePage_RoomLayouts.this, "Clear filter", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView = findViewById(R.id.recyclerview_CWSProfPage_RoomLayouts);
        recyclerView.setHasFixedSize(true);
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

    private  void displayMinPersonCapacityResults(String selectedCapacity, String ownerID){
        //todo: display layouts method based on user selected in spinner layoutPersonCapacity (TO BE MODIFIED)
        List<OfficeLayout_DataClass> filteredList = new ArrayList<>();//initialize new data class for here
        dataClassList.clear();
        colref.whereEqualTo("owner_id", ownerID).whereEqualTo("minCapacity", selectedCapacity)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            OfficeLayout_DataClass modelClass = documentSnapshot.toObject(OfficeLayout_DataClass.class);
                            dataClassList.add(modelClass);
                            Toast.makeText(CWSProfilePage_RoomLayouts.this, "Layouts with minimum capacity " + selectedCapacity, Toast.LENGTH_SHORT).show();
                        }
                        cwsProfilePageRoomLayoutsAdapter.notifyDataSetChanged();
                    }
                });
    }

    private  void displayMaxPersonCapacityResults(String selectedCapacity, String ownerID){
        //todo: display layouts method based on user selected in spinner layoutPersonCapacity (TO BE MODIFIED)
        List<OfficeLayout_DataClass> filteredList = new ArrayList<>();//initialize new data class for here
        dataClassList.clear();
        colref.whereEqualTo("owner_id", ownerID).whereEqualTo("maxCapacity", selectedCapacity)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            OfficeLayout_DataClass modelClass = documentSnapshot.toObject(OfficeLayout_DataClass.class);
                            dataClassList.add(modelClass);
                            Toast.makeText(CWSProfilePage_RoomLayouts.this, "Layouts with maximum capacity " + selectedCapacity, Toast.LENGTH_SHORT).show();
                        }
                        cwsProfilePageRoomLayoutsAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void displayAreaSizeResults(String areaSize, String ownerID){
        dataClassList.clear();
        //todo: display layouts method based on user selected in spinner area size
        colref.whereEqualTo("owner_id", ownerID).whereEqualTo("layoutAreasize", areaSize)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                       for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                           OfficeLayout_DataClass modelClass = documentSnapshot.toObject(OfficeLayout_DataClass.class);
                           dataClassList.add(modelClass);
                           Toast.makeText(CWSProfilePage_RoomLayouts.this, "Layouts with area size " + areaSize, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CWSProfilePage_RoomLayouts.this, "Layouts that are " + availability, Toast.LENGTH_SHORT).show();
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
            cwsProfilePageRoomLayoutsAdapter.setSearchList(dataSearchList);
            if (dataSearchList.isEmpty()){
                Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public class CWSProfilePage_RoomLayouts_Adapter extends RecyclerView.Adapter<CWSProfilePage_RoomLayouts_Adapter.ViewHolder> {

        private List<OfficeLayout_DataClass> dataClass;
        private FirebaseFirestore firebaseFirestore;
        private CollectionReference AllSubmittedBookingRef;


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
            firebaseFirestore = FirebaseFirestore.getInstance();
            AllSubmittedBookingRef = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");
        }


        @NonNull
        @Override
        public CWSProfilePage_RoomLayouts_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem_cwsprofile_pageroomlayouts, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CWSProfilePage_RoomLayouts_Adapter.ViewHolder holder, int position) {

            OfficeLayout_DataClass layout = dataClass.get(position);

            String imageUri = String.valueOf(layout.getLayoutImage());
                Picasso.get().load(imageUri).into(holder.LayoutImage_Imageview);
            holder.LayoutTitle_Textview.setText(layout.getLayoutName());
            holder.PersonCapacity_Textview.setText(layout.getMinCapacity() + "-" + layout.getMaxCapacity());
            holder.Areasize_Textview.setText(layout.getLayoutAreasize() + " sq.m.");

            String layoutName = layout.getLayoutName();
            checkTrial(layoutName, holder.Availability_Textview);


            holder.SeeMoreDetails_Textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextPosition = holder.getAdapterPosition();
                    //OfficeLayout_DataClass model = new OfficeLayout_DataClass();

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.recycleitem_cwsprofile_pageroomlayouts_popupdetails, null);

                    TextView LayoutTitle_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_LayoutTitle_TextviewDETAILS);
                    ImageView LayoutImage_ImageviewDETAILS = dialogView.findViewById(R.id.CWSPP_LayoutImage_ImageviewDETAILS);
                    TextView Availability_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_Availability_TextviewDETAILS);
                    TextView PersonCapacity_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_PersonCapacity_TextviewDETAILS);
                    TextView Areasize_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_Areasize_TextviewDETAILS);
                    TextView HourlyRate_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_HourlyRate_TextviewDETAILS);
                    TextView DailyRate_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_DailyRate_TextviewDETAILS);
                    TextView WeeklyRate_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_WeeklyRate_TextviewDETAILS);
                    TextView MonthyRate_TextviewDETAILS = dialogView.findViewById(R.id.CWSPP_MonthyRate_TextviewDETAILS);
                    AppCompatButton BookNow_AppComButtonDETAILS = dialogView.findViewById(R.id.CWSPP_BookNow_AppComButtonDETAILS);
                    AppCompatButton SeeOther_AppComButtonDETAILS = dialogView.findViewById(R.id.CWSPP_SeeOther_AppComButtonDETAILS);

                    Availability_TextviewDETAILS.setText(holder.Availability_Textview.getText().toString());

                    LayoutTitle_TextviewDETAILS.setText(dataClass.get(nextPosition).getLayoutName());
                    String imageUri = String.valueOf(dataClass.get(nextPosition).getLayoutImage());
                    if (imageUri != null && !imageUri.isEmpty()) {
                        Picasso.get().load(imageUri).into(LayoutImage_ImageviewDETAILS);
                    }
                    PersonCapacity_TextviewDETAILS.setText(dataClass.get(nextPosition).getMinCapacity() + "-" + dataClass.get(nextPosition).getMaxCapacity());
                    Areasize_TextviewDETAILS.setText(dataClass.get(nextPosition).getLayoutAreasize());
                    HourlyRate_TextviewDETAILS.setText(dataClass.get(nextPosition).getLayoutHourlyPrice());
                    DailyRate_TextviewDETAILS.setText(dataClass.get(nextPosition).getLayoutDailyPrice());
                    WeeklyRate_TextviewDETAILS.setText(dataClass.get(nextPosition).getLayoutWeeklyPrice());
                    MonthyRate_TextviewDETAILS.setText(dataClass.get(nextPosition).getLayoutMonthlyPrice());

                    BookNow_AppComButtonDETAILS.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CWSProfilePage_RoomLayouts.this, CustomerBookingActivity.class);
                            Toast.makeText(CWSProfilePage_RoomLayouts.this, "Book now", Toast.LENGTH_SHORT).show();
                            int pos = holder.getAdapterPosition();
                            intent.putExtra("layoutName", dataClassList.get(pos).getLayoutName());
                            intent.putExtra("layout_id", dataClassList.get(pos).getLayout_id());
                            intent.putExtra("owner_id", dataClassList.get(pos).getOwner_id());
                            startActivity(intent);
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

        private void updateAvailabilityInFirestore(String layoutName, String availability) {
            firebaseFirestore.collection("OfficeLayouts")
                    .whereEqualTo("layoutName", layoutName)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String docId = documentSnapshot.getId();

                                firebaseFirestore.collection("OfficeLayouts")
                                        .document(docId)
                                        .update("layoutAvailability", availability)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Successfully updated availability in Firestore
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Failed to update availability in Firestore
                                            }
                                        });
                            }
                        }
                    });
        }

        private void checkAndUpdateLayoutAvailability(String layoutName, TextView availabilityTextView) {
            // Get the current date and time
            Calendar currentTime = Calendar.getInstance();

            // Perform two separate queries to check for current bookings
            Query startQuery = AllSubmittedBookingRef
                    .whereEqualTo("layoutName", layoutName)
                    .whereEqualTo("bookStartTimeSelected", currentTime.getTime());

            Query endQuery = AllSubmittedBookingRef
                    .whereEqualTo("layoutName", layoutName)
                    .whereEqualTo("bookEndTimeSelected", currentTime.getTime());

            Task<QuerySnapshot> startTask = startQuery.get();
            Task<QuerySnapshot> endTask = endQuery.get();

            Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(startTask, endTask);

            allTasks.addOnCompleteListener(new OnCompleteListener<List<QuerySnapshot>>() {
                @Override
                public void onComplete(@NonNull Task<List<QuerySnapshot>> task) {
                    List<QuerySnapshot> snapshots = task.getResult();

                    if (snapshots != null && snapshots.size() >= 2) {
                        QuerySnapshot startSnapshot = snapshots.get(0);
                        QuerySnapshot endSnapshot = snapshots.get(1);

                        boolean hasCurrentBooking = !startSnapshot.isEmpty() && !endSnapshot.isEmpty();

                        if (hasCurrentBooking) {
                            // Set the availability to "Not Available"
                            availabilityTextView.setText("Not Available");
                            updateAvailabilityInFirestore(layoutName, "Not Available");
                        } else {
                            // Set the availability to "Available"
                            availabilityTextView.setText("Available");
                            updateAvailabilityInFirestore(layoutName, "Available");
                        }
                    } else {
                        // Handle any errors or when no snapshots are available
                    }
                }
            });

        }
        private void checkTrial(String layoutName, TextView availabilityTextView) {
            // Get the current date and time
            Calendar currentTime = Calendar.getInstance();

            // Perform two separate queries to check for current bookings
            AllSubmittedBookingRef
                    .whereEqualTo("layoutName", layoutName)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            boolean hasCurrentBooking = false;

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                // Retrieve the bookStartTimeSelected and bookEndTimeSelected timestamps from the document
                                Timestamp startTimeSelected = documentSnapshot.getTimestamp("BookStartTimeSelected");
                                Timestamp endTimeSelected = documentSnapshot.getTimestamp("BookEndTimeSelected");
                                String status = documentSnapshot.getString("bookingStatus");

                                if (status.equals("Ongoing") || status.equals("Accepted")){
                                    if (startTimeSelected != null && endTimeSelected != null)  {
                                        Calendar currentTime = Calendar.getInstance();
                                        Calendar startTime = Calendar.getInstance();
                                        startTime.setTime(startTimeSelected.toDate());
                                        Calendar endTime = Calendar.getInstance();
                                        endTime.setTime(endTimeSelected.toDate());

                                        if(currentTime.getTimeInMillis() >= startTime.getTimeInMillis() &&
                                                currentTime.getTimeInMillis() <= endTime.getTimeInMillis()){
                                            hasCurrentBooking = true;
                                            //break;
                                        }else{
                                            hasCurrentBooking = false;
                                        }
                                    }
                                }
                            }

                            if (hasCurrentBooking) {
                                // Set the availability to "Not Available"
                                availabilityTextView.setText("Not Available");
                                updateAvailabilityInFirestore(layoutName, "Not Available");
                            } else {
                                // Set the availability to "Available"
                                availabilityTextView.setText("Available");
                                updateAvailabilityInFirestore(layoutName, "Available");
                            }
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return dataClass.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView LayoutImage_Imageview;
            TextView LayoutTitle_Textview, PersonCapacity_Textview, Areasize_Textview, Availability_Textview;
            LinearLayout SeeMoreDetails_Textview;

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