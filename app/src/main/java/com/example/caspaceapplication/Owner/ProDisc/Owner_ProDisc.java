package com.example.caspaceapplication.Owner.ProDisc;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Owner_ProDisc extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    EditText prodiscTitle, prodiscDescription;
    ImageView prodiscImage;
    Uri filepath = null;
    private static final int GALLERY_CODE = 1;
    Button cancel, add, deleteButtonProdiscDetailPopup;
    BottomNavigationView navigationView;
    FloatingActionButton floatingActionButton;

    RecyclerView prodiscRecyclerView;
    List<OwnerProDisc_ModelClass> dataClassList;
    ProDisc_AdapterClass prodisc_adapterClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_pro_disc);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseStorage = firebaseStorage.getInstance();
        prodiscRecyclerView = findViewById(R.id.recyclerViewProDisc);
        dataClassList = new ArrayList<>();

        prodiscRecyclerView.setHasFixedSize(true);
        prodiscRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        dataClassList = new ArrayList<>();
        prodisc_adapterClass = new ProDisc_AdapterClass(this, dataClassList);
        prodiscRecyclerView.setAdapter(prodisc_adapterClass);

        // Initialize the bottom navigation bar
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(this);

        getProdiscList();

        floatingActionButton = findViewById(R.id.addFloatButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewDialog();
            }
        });

    }


    //dialog box for adding a new promotion or discount (pop up dialog)
    public void createNewDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View prodiscPopupview = getLayoutInflater().inflate(R.layout.popout_prodisc_add, null);
        prodiscTitle = (EditText) prodiscPopupview.findViewById(R.id.prodiscTitle_editText_popup);
        prodiscDescription = (EditText) prodiscPopupview.findViewById(R.id.prodiscDesc_editText_popup);
        prodiscImage = (ImageView) prodiscPopupview.findViewById(R.id.prodiscImage_imageButton_popup);
        prodiscImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });

        cancel = (Button) prodiscPopupview.findViewById(R.id.prodisc_cancelButton);
        add = (Button) prodiscPopupview.findViewById(R.id.prodisc_addButton);

        dialogBuilder.setView(prodiscPopupview);
        dialog = dialogBuilder.create();
        dialog.show();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String pdTitle = prodiscTitle.getText().toString().trim();
                String pdDescription = prodiscDescription.getText().toString().trim();

                if (!(pdTitle.isEmpty() && pdDescription.isEmpty() && filepath!=null)){
                    firebaseFirestore.collection("OwnerPublishedPromotions")
                            .whereEqualTo("promotionTitle", pdTitle)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && !task.getResult().isEmpty()){
                                        Toast.makeText(Owner_ProDisc.this, "A promotion with this title already exists!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        StorageReference path = firebaseStorage.getReference().child("PromotionsImages").child(filepath.getLastPathSegment());
                                        path.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Uri> task) {
                                                        Map<String,String> pd = new HashMap<>();
                                                        pd.put("owner_id", user.getUid());
                                                        pd.put("promotionImage", task.getResult().toString());
                                                        pd.put("promotionTitle", pdTitle);
                                                        pd.put("promotionDescription", pdDescription);
                                                        pd.put("promotion_id", "");

                                                        firebaseFirestore.collection("OwnerPublishedPromotions")
                                                                .add(pd)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        firebaseFirestore.collection("OwnerPublishedPromotions")
                                                                                .document(documentReference.getId())
                                                                                .update("promotion_id", documentReference.getId())
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        Toast.makeText(Owner_ProDisc.this, "Promotion added!", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });
                                                                        startActivity(new Intent(Owner_ProDisc.this, Owner_ProDisc.class));
                                                                    }
                                                                });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(Owner_ProDisc.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getProdiscList(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore.collection("OwnerPublishedPromotions")
                .whereEqualTo("owner_id", user.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Log.d(TAG, "Document data: " + documentSnapshot.getData());
                            OwnerProDisc_ModelClass modelClass = documentSnapshot.toObject(OwnerProDisc_ModelClass.class);
                            dataClassList.add(modelClass);
                        }
                        prodisc_adapterClass.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            filepath = data.getData();
            prodiscImage.setImageURI(filepath);
        }
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