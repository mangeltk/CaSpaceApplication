package com.example.caspaceapplication.Owner.OfficeLayouts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.caspaceapplication.Owner.OwnerHomepage;
import com.example.caspaceapplication.Owner.Profile.Owner_Profile;
import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Owner_OfficelayoutsRegistration extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView navigationView;//bottom navigation bar

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();;

    AppCompatButton saveLayoutButton;
    EditText layoutName, layoutMinPersonCap, layoutMaxPersonCap, layoutAreasize, layoutType, layoutHourlyPrice, layoutDailyPrice, layoutWeeklyPrice, layoutMonthlyPrice, layoutAnnualPrice;
    ImageView layoutImage;
    Uri filepath = null;
    private static final int GALLERY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_officelayouts_registration);

        layoutImage = findViewById(R.id.layoutImage_imageView);
        layoutName = findViewById(R.id.layoutName_editText);
        layoutMinPersonCap = findViewById(R.id.layoutMinPersonCap_editText);
        layoutMaxPersonCap = findViewById(R.id.layoutMaxPersonCap_editText);
        layoutAreasize = findViewById(R.id.layoutAreaSize_editText);
        layoutType = findViewById(R.id.layoutLayoutType_editText);
        layoutHourlyPrice = findViewById(R.id.layoutLayoutHourlyPrice_editText);
        layoutDailyPrice = findViewById(R.id.layoutLayoutDailyPrice_editText);
        layoutWeeklyPrice = findViewById(R.id.layoutLayoutWeeklyPrice_editText);
        layoutMonthlyPrice = findViewById(R.id.layoutLayoutMonthlyPrice_editText);
        layoutAnnualPrice = findViewById(R.id.layoutLayoutAnnualPrice_editText);


        saveLayoutButton = findViewById(R.id.saveLayout_Button);

        // Initialize the bottom navigation bar
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(this);

        layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            filepath = data.getData();
            layoutImage.setImageURI(filepath);
        }

        saveLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String layout_name = layoutName.getText().toString().trim();
                String layout_personMinCapacity = layoutMinPersonCap.getText().toString().trim();
                String layout_personMaxCapacity = layoutMaxPersonCap.getText().toString().trim();
                String layout_areasize = layoutAreasize.getText().toString().trim();
                String layout_type = layoutType.getText().toString().trim();
                String layout_HourlyRate = layoutHourlyPrice.getText().toString().trim();
                String layout_DailyRate = layoutDailyPrice.getText().toString().trim();
                String layout_WeeklyRate = layoutWeeklyPrice.getText().toString().trim();
                String layout_MonthlyRate = layoutMonthlyPrice.getText().toString().trim();
                String layout_AnnualRate = layoutAnnualPrice.getText().toString().trim();

                if (TextUtils.isEmpty(layout_name) || filepath == null || TextUtils.isEmpty(layout_personMinCapacity) ||
                        TextUtils.isEmpty(layout_personMaxCapacity) || TextUtils.isEmpty(layout_areasize) || TextUtils.isEmpty(layout_type) ||
                        TextUtils.isEmpty(layout_HourlyRate) || TextUtils.isEmpty(layout_DailyRate) || TextUtils.isEmpty(layout_WeeklyRate) ||
                        TextUtils.isEmpty(layout_MonthlyRate) || TextUtils.isEmpty(layout_AnnualRate)){
                    Toast.makeText(Owner_OfficelayoutsRegistration.this, "Please fill in all fields and select an image.", Toast.LENGTH_SHORT).show();
                }else{
                    // Validate the fields are valid numbers
                    try {
                        Integer.parseInt(layout_areasize);
                        Integer.parseInt(layout_personMinCapacity);
                        Integer.parseInt(layout_personMaxCapacity);
                    } catch (NumberFormatException e) {
                        layoutMinPersonCap.requestFocus();
                        layoutMinPersonCap.setError("Please enter a valid number for the min person capacity");

                        layoutMaxPersonCap.requestFocus();
                        layoutMaxPersonCap.setError("Please enter a valid number for the max person capacity");

                        layoutAreasize.requestFocus();
                        layoutAreasize.setError("Please enter a valid number for the area size");
                        return;
                    }

                    firebaseFirestore.collection("OfficeLayouts")
                            .whereEqualTo("layoutName", layout_name)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        boolean isExistingLayout = false;
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            isExistingLayout = true;
                                            break;
                                        }
                                        if (isExistingLayout) {
                                            Toast.makeText(Owner_OfficelayoutsRegistration.this, "A layout with the same name already exists.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            StorageReference path = firebaseStorage.getReference().child("LayoutImages").child(filepath.getLastPathSegment());
                                            path.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Uri> task) {
                                                            Map<String, String> layout = new HashMap<>();
                                                            layout.put("layoutImage", task.getResult().toString());
                                                            layout.put("layoutName", layout_name);
                                                            layout.put("minCapacity", layout_personMinCapacity);
                                                            layout.put("maxCapacity", layout_personMaxCapacity);
                                                            layout.put("layoutAreasize", layout_areasize);
                                                            layout.put("layoutType", layout_type);
                                                            layout.put("layoutHourlyPrice", layout_HourlyRate);
                                                            layout.put("layoutDailyPrice", layout_DailyRate);
                                                            layout.put("layoutWeeklyPrice", layout_WeeklyRate);
                                                            layout.put("layoutMonthlyPrice", layout_MonthlyRate);
                                                            layout.put("layoutAnnualPrice", layout_AnnualRate);
                                                            layout.put("owner_id", user.getUid());
                                                            layout.put("layout_id", "");

                                                            firebaseFirestore.collection("OfficeLayouts")
                                                                    .add(layout)
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                            FirebaseFirestore.getInstance().collection("OfficeLayouts")
                                                                                    .document(documentReference.getId())
                                                                                    .update("layout_id", documentReference.getId())
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {
                                                                                            Toast.makeText(Owner_OfficelayoutsRegistration.this, "Layout saved!", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                            startActivity(new Intent(Owner_OfficelayoutsRegistration.this, Owner_OfficeLayouts.class));
                                                                            Toast.makeText(Owner_OfficelayoutsRegistration.this, "Layout saved!", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    } else {
                                        Toast.makeText(Owner_OfficelayoutsRegistration.this, "Error checking for existing layouts.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
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
