package com.example.caspaceapplication.Owner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.R;

public class Owner_OfficeLayoutEditDetails extends AppCompatActivity {

    TextView EDITdetailPeople, EDITdetailName, EDITdetailAreasize;
    ImageView EDITdetailImage;
    Button editDetailsButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_office_layout_edit_details);

        EDITdetailImage = findViewById(R.id.EDITlayoutImage_imageButton);
        EDITdetailName = findViewById(R.id.EDITlayoutName_editText);
        EDITdetailPeople = findViewById(R.id.EDITlayoutPeopleSize_editText);
        EDITdetailAreasize = findViewById(R.id.EDITlayoutAreaSize_editText);
        editDetailsButton = findViewById(R.id.editLayout_Button);



    }




    public void saveDetails(){

        /*saveLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String layout_name = layoutName.getText().toString().trim();
                String layout_peoplesize = layoutPeoplesize.getText().toString().trim();
                String layout_areasize = layoutAreasize.getText().toString().trim();

                if (!(layout_name.isEmpty() && layout_peoplesize.isEmpty() && layout_areasize.isEmpty() && filepath != null)) {
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
                                                                    Toast.makeText(Owner_OfficelayoutsRegistration.this, "Layout save!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                    startActivity(new Intent(Owner_OfficelayoutsRegistration.this, Owner_OfficeLayouts.class));
                                                    Toast.makeText(Owner_OfficelayoutsRegistration.this, "Layout save!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    });

                }
            }
        });*/
    }
}