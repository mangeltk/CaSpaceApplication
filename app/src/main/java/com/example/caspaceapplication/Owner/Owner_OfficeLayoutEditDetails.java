package com.example.caspaceapplication.Owner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class Owner_OfficeLayoutEditDetails extends AppCompatActivity {

    TextView EDITdetailPeople, EDITdetailName, EDITdetailAreasize;
    ImageView EDITdetailImage;
    Button editDetailsButton;
    Uri filepath = null;
    private static final int GALLERY_CODE = 1;

    FirebaseAuth firebaseAuth;
    //FirebaseFirestore firebaseFirestore;
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
        EDITdetailPeople = findViewById(R.id.EDITlayoutPeopleSize_editText);
        EDITdetailAreasize = findViewById(R.id.EDITlayoutAreaSize_editText);

        //TODO: EDIT LAYOUT DETAILS - add lacking data:
        // space id - done auto from firestore collection UID
        // space name - done
        // space type
        // space price
        // space capacity - done
        // space amenities
        // space status - not yet

        EDITdetailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String imagePath = getIntent().getStringExtra("layoutImage");
                Picasso.get().load(imagePath).into(EDITdetailImage);
            EDITdetailName.setText(bundle.getString("layoutName"));
            EDITdetailPeople.setText(bundle.getString("layoutPeopleNum"));
            EDITdetailAreasize.setText(bundle.getString("layoutAreasize"));

        }
        //edit details TODO: Layout Name cannot be edit optional to fix
        editDetailsButton = findViewById(R.id.editLayout_Button);
        editDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String EDITlayout_name = EDITdetailName.getText().toString().trim();
                String EDITlayout_peoplesize = EDITdetailPeople.getText().toString().trim();
                String EDITlayout_areasize = EDITdetailAreasize.getText().toString().trim();

                firebaseFirestore.collection("OfficeLayouts")
                        .whereEqualTo("layout_id", EDITlayout_name)
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
                                    updates.put("layoutPeopleNum", EDITlayout_peoplesize);
                                    updates.put("layoutAreasize", EDITlayout_areasize);

                                    // Update the image only if a new image is selected
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

}