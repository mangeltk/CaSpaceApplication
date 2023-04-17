package com.example.caspaceapplication.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class Owner_OfficeLayoutDetail extends AppCompatActivity {

    TextView detail_People, detail_Name, detail_Areasize;
    ImageView detail_Image;

    FloatingActionButton menu, edit, delete;
    boolean aBoolean = true;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_office_layout_detail);

        detail_Name = findViewById(R.id.detailName);
        detail_People = findViewById(R.id.detailPeopleAnswer);
        detail_Areasize = findViewById(R.id.detailAreasizeAnswer);
        detail_Image = findViewById(R.id.detailImage);

        //TODO: EDIT LAYOUT DETAILS - add lacking data:
        // space id - done auto from firestore collection UID
        // space name - done
        // space type
        // space price
        // space capacity - done
        // space amenities
        // space status - not yet

        firebaseFirestore = FirebaseFirestore.getInstance();

        //Set text details from intent recylerview---------------------------
        Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
            String imagePath = getIntent().getStringExtra("layoutImage");
                Picasso.get().load(imagePath).into(detail_Image);
            detail_Name.setText(bundle.getString("layoutName"));
            detail_People.setText(bundle.getString("layoutPeopleNum"));
            detail_Areasize.setText(bundle.getString("layoutAreasize"));
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
                intent.putExtra("layoutName", detail_Name.getText().toString());
                intent.putExtra("layoutPeopleNum", detail_People.getText().toString());
                intent.putExtra("layoutAreasize", detail_Areasize.getText().toString());
                intent.putExtra("layoutImage", getIntent().getStringExtra("layoutImage"));

                intent.putExtra("layout_id", getIntent().getStringExtra("layout_id"));
                startActivity(intent);
            }
        });

        //delete details-----------------------------
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });


    }
}