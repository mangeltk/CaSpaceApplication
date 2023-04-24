package com.example.caspaceapplication.Owner.AmenitiesOffered;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.Owner.OwnerHomepage;
import com.example.caspaceapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Owner_AmenitiesOffered extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView navigationView;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    private RecyclerView amenitiesRecyclerview;
    private CheckBox creatorLabCheckbox, trainingRoomCheckbox, storageLockerRoomCheckbox;
    //private Dialog popupDialog;
    private TextView updateTextview;

    private Amenities_AdapterClass adapterClass;
    private List<OwnerAmenities_ModelClass> amenitiesList = new ArrayList<>();

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();;
    private CollectionReference amenitiesOffered = firebaseFirestore.collection("AmenitiesOffered");
    private String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_amenities_offered);

        //Todo: Collection name  -> AmenitiesOffered
        // AMENITIES TO USE:
        // Creator Lab
        // Training room
        // Storage Locker room
        // Parking
        // Event space
        // Fitness center
        // Recreational games
        // Tech ServicesMeeting rooms
        // Onsite staff
        // Phone booths
        // Stocked kitchens
        // Business-class printers
        // Professional events and programming
        // Security Guards
        // CCTV Cameras
        // Fire Extinguisher
        // Solo Cubicle
        // Bar side/Caffeine fix
        // Seating (Ergonomic chairs, Beanbags)

        //amenitiesRecyclerview = findViewById(R.id.AmenitiesOffered_recyclerview);

        //useBottomNavigationMenu();

        updateTextview = findViewById(R.id.updateAmenities_Textview);

        updateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopupDialog();
            }
        });

        amenitiesRecyclerview = findViewById(R.id.AmenitiesOffered_recyclerview);

        // Initialize the bottom navigation bar
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(this);

    }

    public void setPopupDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View amenitiesPopupview = getLayoutInflater().inflate(R.layout.addamenities_popup, null);
        creatorLabCheckbox = (CheckBox) amenitiesPopupview.findViewById(R.id.checkbox_creator_lab);
        trainingRoomCheckbox = (CheckBox) amenitiesPopupview.findViewById(R.id.checkbox_training_room);
        storageLockerRoomCheckbox = (CheckBox) amenitiesPopupview.findViewById(R.id.checkbox_storage_locker_room);

        Button cancel = (Button) amenitiesPopupview.findViewById(R.id.cancelAmenitiesButton);
        Button save = (Button) amenitiesPopupview.findViewById(R.id.saveAmenitiesButton);

        dialogBuilder.setView(amenitiesPopupview);
        dialog = dialogBuilder.create();
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(Owner_AmenitiesOffered.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: code save amenities to firestore and show on recyclerview
            }
        });

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
                // startActivity(new Intent(this, OwnerProfileActivity.class));
                return true;
            default:
                return false;
        }
    }
}