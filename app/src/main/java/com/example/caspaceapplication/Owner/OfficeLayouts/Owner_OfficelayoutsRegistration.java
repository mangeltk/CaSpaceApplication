package com.example.caspaceapplication.Owner.OfficeLayouts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.Owner.OwnerHomepage;
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

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;

    Button saveLayoutButton;
    EditText layoutName, layoutPeoplesize, layoutAreasize, layoutType, layoutPrice;
    ImageButton layoutImage;
    Uri filepath = null;
    private static final int GALLERY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_officelayouts_registration);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        layoutImage = findViewById(R.id.layoutImage_imageButton);
        layoutName = findViewById(R.id.layoutName_editText);
        layoutPeoplesize = findViewById(R.id.layoutPeopleSize_editText);
        layoutAreasize = findViewById(R.id.layoutAreaSize_editText);
        layoutType = findViewById(R.id.layoutLayoutType_editText);
        layoutPrice = findViewById(R.id.layoutLayoutPrice_editText);
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
                String layout_peoplesize = layoutPeoplesize.getText().toString().trim();
                String layout_areasize = layoutAreasize.getText().toString().trim();
                String layout_type = layoutType.getText().toString().trim();
                String layout_price = layoutPrice.getText().toString().trim();

                if (TextUtils.isEmpty(layout_name) || TextUtils.isEmpty(layout_peoplesize) || TextUtils.isEmpty(layout_areasize) || TextUtils.isEmpty(layout_type) || TextUtils.isEmpty(layout_price) || filepath == null){
                    Toast.makeText(Owner_OfficelayoutsRegistration.this, "Please fill in all fields and select an image.", Toast.LENGTH_SHORT).show();
                }else{
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
                                                            layout.put("layoutPeopleNum", layout_peoplesize);
                                                            layout.put("layoutAreasize", layout_areasize);
                                                            layout.put("layoutType", layout_type);
                                                            layout.put("layoutPrice", layout_price);
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

                /*if (!(layout_name.isEmpty() && layout_peoplesize.isEmpty() && layout_areasize.isEmpty() && filepath != null)) {

                }*/
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