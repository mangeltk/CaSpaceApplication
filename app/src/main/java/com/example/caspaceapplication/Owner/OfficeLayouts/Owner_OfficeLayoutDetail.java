package com.example.caspaceapplication.Owner.OfficeLayouts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.caspaceapplication.Owner.OwnerHomepage;
import com.example.caspaceapplication.Owner.Profile.Owner_Profile;
import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class Owner_OfficeLayoutDetail extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    TextView detail_MinPersonCapacity, detail_MaxPersonCapacity, detail_Name, detail_Areasize, detail_SpaceType,
            detail_HourlyPrice, detail_DailyPrice, detail_WeeklyPrice,
            detail_MonthlyPrice, detail_AnnualPrice, detail_Availability;
    ImageView detail_Image;
    AppCompatButton availStatus_Button, notAvailStatus_Button;

    FloatingActionButton menu, edit, delete;
    boolean aBoolean = true;

    FirebaseFirestore firebaseFirestore;

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_office_layout_detail);

        firebaseFirestore = FirebaseFirestore.getInstance();

        detail_Name = findViewById(R.id.detailName);
        detail_MinPersonCapacity = findViewById(R.id.detailMinPersonCapacity);
        detail_MaxPersonCapacity = findViewById(R.id.detailMaxPersonCapacity);
        detail_Areasize = findViewById(R.id.detailAreasizeAnswer);
        detail_Image = findViewById(R.id.detailImage);
        detail_SpaceType = findViewById(R.id.detailSpaceTypeAnswer);
        detail_HourlyPrice = findViewById(R.id.layoutLayoutHourlyPrice_Textview);
        detail_DailyPrice = findViewById(R.id.layoutLayoutDailyPrice_Textview);
        detail_WeeklyPrice = findViewById(R.id.layoutLayoutWeeklyPrice_Textview);
        detail_MonthlyPrice = findViewById(R.id.layoutLayoutMonthlyPrice_Textview);
        detail_AnnualPrice = findViewById(R.id.layoutLayoutAnnualPrice_Textview);
        availStatus_Button = findViewById(R.id.available_Button);
        notAvailStatus_Button = findViewById(R.id.notAvailable_Button);
        detail_Availability = findViewById(R.id.detailAvailabilityStatusAnswer);

        // Initialize the bottom navigation bar
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(this);


        availStatus_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detail_Availability.setText("Available");
                startActivity(new Intent(Owner_OfficeLayoutDetail.this, Owner_OfficeLayouts.class));
            }
        });
        notAvailStatus_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detail_Availability.setText("Not available");
                startActivity(new Intent(Owner_OfficeLayoutDetail.this, Owner_OfficeLayouts.class));
            }
        });

        detail_Availability.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //does nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update the Firestore document with the new text
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                firebaseFirestore.collection("OfficeLayouts")
                        .whereEqualTo("layoutName", detail_Name.getText().toString())
                        .whereEqualTo("owner_id", user.getUid())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String documentId = document.getId();
                                    firebaseFirestore.collection("OfficeLayouts")
                                            .document(documentId)
                                            .update("layoutAvailability", s.toString());

                                }
                            } else {
                                Log.e("Firestore Update", "Error getting document: ", task.getException());
                            }
                        });

            }

            @Override
            public void afterTextChanged(Editable s) {
                //does nothing
            }

        });

        //Set text details from intent recylerview---------------------------
        Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
            String imagePath = getIntent().getStringExtra("layoutImage");
                Picasso.get().load(imagePath).into(detail_Image);
            detail_Name.setText(bundle.getString("layoutName"));
            detail_MinPersonCapacity.setText(bundle.getString("minCapacity"));
            detail_MaxPersonCapacity.setText(bundle.getString("maxCapacity"));
            detail_Areasize.setText(bundle.getString("layoutAreasize"));
            detail_SpaceType.setText(bundle.getString("layoutType"));
            detail_HourlyPrice.setText(bundle.getString("layoutHourlyPrice"));
            detail_DailyPrice.setText(bundle.getString("layoutDailyPrice"));
            detail_WeeklyPrice.setText(bundle.getString("layoutWeeklyPrice"));
            detail_MonthlyPrice.setText(bundle.getString("layoutMonthlyPrice"));
            detail_AnnualPrice.setText(bundle.getString("layoutAnnualPrice"));
            detail_Availability.setText(bundle.getString("layoutAvailability"));

        }

        //Floating buttons START------------------------------------
        menu = findViewById(R.id.floatingMenu);
        edit = findViewById(R.id.floatingMenuEdit);
        delete = findViewById(R.id.floatingMenuDelete);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aBoolean){
                    edit.show();
                    delete.show();
                    aBoolean=false;
                }else{
                    edit.hide();
                    delete.hide();
                    aBoolean=true;
                }
            }
        });

        //Show details on next intent activity from last activity------------------------------
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Owner_OfficeLayoutDetail.this, Owner_OfficeLayoutEditDetails.class));
                Intent intent = new Intent(Owner_OfficeLayoutDetail.this, Owner_OfficeLayoutEditDetails.class);
                intent.putExtra("layoutImage", getIntent().getStringExtra("layoutImage"));
                intent.putExtra("layoutName", detail_Name.getText().toString());
                intent.putExtra("minCapacity", detail_MinPersonCapacity.getText().toString());
                intent.putExtra("maxCapacity", detail_MaxPersonCapacity.getText().toString());
                intent.putExtra("layoutAreasize", detail_Areasize.getText().toString());
                intent.putExtra("layoutType", detail_SpaceType.getText().toString());
                intent.putExtra("layoutHourlyPrice", detail_HourlyPrice.getText().toString());
                intent.putExtra("layoutDailyPrice", detail_DailyPrice.getText().toString());
                intent.putExtra("layoutWeeklyPrice", detail_WeeklyPrice.getText().toString());
                intent.putExtra("layoutMonthlyPrice", detail_MonthlyPrice.getText().toString());
                intent.putExtra("layoutAnnualPrice", detail_AnnualPrice.getText().toString());
                intent.putExtra("layoutAvailability", detail_Availability.getText().toString());
                intent.putExtra("layout_id", getIntent().getStringExtra("layout_id"));
                startActivity(intent);
            }
        });


        //delete details-----------------------------
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Owner_OfficeLayoutDetail.this);
                builder.setMessage("Are you sure you want to delete this layout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String layoutName = detail_Name.getText().toString().trim();

                                firebaseFirestore.collection("OfficeLayouts")
                                        .whereEqualTo("layoutName", layoutName)
                                        .whereEqualTo("owner_id", user.getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful() && !task.getResult().isEmpty()){
                                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                    String documentId = document.getId();

                                                    firebaseFirestore.collection("OfficeLayouts")
                                                            .document(documentId)
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(Owner_OfficeLayoutDetail.this, "Layout deleted!", Toast.LENGTH_SHORT).show();
                                                                    startActivity(new Intent(Owner_OfficeLayoutDetail.this, Owner_OfficeLayouts.class));

                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
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
                startActivity(new Intent(this, Owner_Profile.class));
                return true;
            default:
                return false;
        }
    }
}