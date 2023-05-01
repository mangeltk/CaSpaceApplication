package com.example.caspaceapplication.Owner.OfficeLayouts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.Owner.OwnerHomepage;
import com.example.caspaceapplication.Owner.Profile.Owner_Profile;
import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Owner_OfficeLayoutEditDetails extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    TextView EDITdetailMinPersonCap, EDITdetailMaxPersonCap, EDITdetailName, EDITdetailAreasize,
            EDITdetailType, EDITlayoutLayoutAnnualPrice, EDITlayoutLayoutMonthlyPrice,
            EDITlayoutLayoutWeeklyPrice, EDITlayoutLayoutDailyPrice, EDITlayoutLayoutHourlyPrice;
    ImageView EDITdetailImage;
    Button editDetailsButton;
    Uri filepath = null;
    private static final int GALLERY_CODE = 1;

    BottomNavigationView navigationView;

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            filepath = data.getData();
            EDITdetailImage.setImageURI(filepath);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_office_layout_edit_details);

        EDITdetailImage = findViewById(R.id.EDITlayoutImage_imageButton);
        EDITdetailName = findViewById(R.id.EDITlayoutName_editText);
        EDITdetailMinPersonCap = findViewById(R.id.EDITlayoutMinPersonCap_editText);
        EDITdetailMaxPersonCap = findViewById(R.id.EDITlayoutMaxPersonCap_editText);
        EDITdetailAreasize = findViewById(R.id.EDITlayoutAreaSize_editText);
        EDITdetailType = findViewById(R.id.EDITlayoutType_editText);
        EDITlayoutLayoutHourlyPrice = findViewById(R.id.EDITlayoutLayoutHourlyPrice_editText);
        EDITlayoutLayoutDailyPrice = findViewById(R.id.EDITlayoutLayoutDailyPrice_editText);
        EDITlayoutLayoutWeeklyPrice = findViewById(R.id.EDITlayoutLayoutWeeklyPrice_editText);
        EDITlayoutLayoutMonthlyPrice = findViewById(R.id.EDITlayoutLayoutMonthlyPrice_editText);
        EDITlayoutLayoutAnnualPrice = findViewById(R.id.EDITlayoutLayoutAnnualPrice_editText);

        // Initialize the bottom navigation bar
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(this);

        EDITdetailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });

        //Set text details from intent recylerview---------------------------
        Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
            String imagePath = getIntent().getStringExtra("layoutImage");
            Picasso.get().load(imagePath).into(EDITdetailImage);
            EDITdetailName.setText(bundle.getString("layoutName"));
            EDITdetailMinPersonCap.setText(bundle.getString("minCapacity"));
            EDITdetailMaxPersonCap.setText(bundle.getString("maxCapacity"));
            EDITdetailAreasize.setText(bundle.getString("layoutAreasize"));
            EDITdetailType.setText(bundle.getString("layoutType"));
            EDITlayoutLayoutHourlyPrice.setText(bundle.getString("layoutHourlyPrice"));
            EDITlayoutLayoutDailyPrice.setText(bundle.getString("layoutDailyPrice"));
            EDITlayoutLayoutWeeklyPrice.setText(bundle.getString("layoutWeeklyPrice"));
            EDITlayoutLayoutMonthlyPrice.setText(bundle.getString("layoutMonthlyPrice"));
            EDITlayoutLayoutAnnualPrice.setText(bundle.getString("layoutAnnualPrice"));
        }

        editDetailsButton = findViewById(R.id.editLayout_Button);
        editDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String EDITlayout_name = EDITdetailName.getText().toString().trim();
                String EDITlayout_minPersonCap = EDITdetailMinPersonCap.getText().toString().trim();
                String EDITlayout_maxPersonCap = EDITdetailMaxPersonCap.getText().toString().trim();
                String EDITlayout_areasize = EDITdetailAreasize.getText().toString().trim();
                String EDITlayout_type = EDITdetailType.getText().toString().trim();
                String EDITlayout_hourlyPrice = EDITlayoutLayoutHourlyPrice.getText().toString().trim();
                String EDITlayout_dailyPrice = EDITlayoutLayoutDailyPrice.getText().toString().trim();
                String EDITlayout_weeklyPrice = EDITlayoutLayoutWeeklyPrice.getText().toString().trim();
                String EDITlayout_monthlyPrice = EDITlayoutLayoutMonthlyPrice.getText().toString().trim();
                String EDITlayout_annualPrice = EDITlayoutLayoutAnnualPrice.getText().toString().trim();

                // Validate that the area size is a valid number
                try {
                    Integer.parseInt(EDITlayout_areasize);
                    Integer.parseInt(EDITlayout_minPersonCap);
                    Integer.parseInt(EDITlayout_maxPersonCap);
                } catch (NumberFormatException e) {
                    EDITdetailMinPersonCap.requestFocus();
                    EDITdetailMinPersonCap.setError("Please enter a valid number for the min person capacity");

                    EDITdetailMaxPersonCap.requestFocus();
                    EDITdetailMaxPersonCap.setError("Please enter a valid number for the max person capacity");

                    EDITdetailAreasize.requestFocus();
                    EDITdetailAreasize.setError("Please enter a valid number for the area size");
                    return;
                }

                firebaseFirestore.collection("OfficeLayouts")
                        .whereEqualTo("layoutName", EDITlayout_name)
                        .whereEqualTo("owner_id", user.getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()){
                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                    String documentId = document.getId();

                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("layoutName", EDITlayout_name);
                                    updates.put("minCapacity", EDITlayout_minPersonCap);
                                    updates.put("maxCapacity", EDITlayout_maxPersonCap);
                                    updates.put("layoutAreasize", EDITlayout_areasize);
                                    updates.put("layoutType", EDITlayout_type);
                                    updates.put("layoutHourlyPrice", EDITlayout_hourlyPrice);
                                    updates.put("layoutDailyPrice", EDITlayout_dailyPrice);
                                    updates.put("layoutWeeklyPrice", EDITlayout_weeklyPrice);
                                    updates.put("layoutMonthlyPrice", EDITlayout_monthlyPrice);
                                    updates.put("layoutAnnualPrice", EDITlayout_annualPrice);

                                    if (filepath != null){
                                        firebaseStorage.getReference().child("LayoutImages").child(documentId).putFile(filepath)
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        firebaseStorage.getReference().child("LayoutImages").child(documentId).getDownloadUrl()
                                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        updates.put("layoutImage", uri.toString());
                                                                        firebaseFirestore.collection("OfficeLayouts")
                                                                                .document(documentId)
                                                                                .update(updates)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        Toast.makeText(Owner_OfficeLayoutEditDetails.this, "Layout updated!", Toast.LENGTH_SHORT).show();
                                                                                        startActivity(new Intent(Owner_OfficeLayoutEditDetails.this, Owner_OfficeLayouts.class));
                                                                                    }
                                                                                });
                                                                    }
                                                                });
                                                    }
                                                });

                                    }else {
                                        firebaseFirestore.collection("OfficeLayouts")
                                                .document(documentId)
                                                .update(updates)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(Owner_OfficeLayoutEditDetails.this, "Layout updated!", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(Owner_OfficeLayoutEditDetails.this, Owner_OfficeLayouts.class));
                                                    }
                                                });
                                    }


                                }
                            }
                        });

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