package com.example.caspaceapplication.Owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class RegisterOwner_SpaceBranch extends AppCompatActivity {

    //ActivityRegisterOwnerSpaceBranchBinding binding;//because wala na ta nag binding here
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    ProgressDialog progressDialog;

    Uri filepath = null;
    private static final int GALLERY_CODE = 1;
    ImageButton branch_image;
    Button registerButton_SpaceBranch;
    EditText branchName, branchAdrress;

    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_owner_space_branch);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseStorage = firebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);

        branch_image = findViewById(R.id.registerBranchImage_Imagebutton);
        registerButton_SpaceBranch = findViewById(R.id.registerButton_SpaceBranch);
        branchName = findViewById(R.id.registerBranchName_Edittext);
        branchAdrress = findViewById(R.id.registerBranchAddress_Edittext);
        categorySpinner = findViewById(R.id.spinnerCategories);

        branch_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });

        registerButton_SpaceBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namebranch = branchName.getText().toString().trim();
                String addressBranch = branchAdrress.getText().toString().trim();
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                registerBranch(namebranch, addressBranch, selectedCategory);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            filepath = data.getData();
            branch_image.setImageURI(filepath);
        }

       /* registerButton_SpaceBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String namebranch = branchName.getText().toString().trim();
                String addressBranch = branchAdrress.getText().toString().trim();
                String selectedCategory = categorySpinner.getSelectedItem().toString();

                if (!(namebranch.isEmpty() && addressBranch.isEmpty() && filepath!=null)){
                    firebaseFirestore.collection("CospaceBranches")
                            .whereEqualTo("cospaceName", namebranch)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                        Toast.makeText(RegisterOwner_SpaceBranch.this, "A branch with this name already exists", Toast.LENGTH_SHORT).show();
                                    } else {
                                        StorageReference path = firebaseStorage.getReference().child("BranchImages").child(filepath.getLastPathSegment());
                                        path.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Uri> task) {
                                                        Map<String,String> branch = new HashMap<>();
                                                        branch.put("cospaceImage",task.getResult().toString());
                                                        branch.put("cospaceName", namebranch);
                                                        branch.put("cospaceCategory", selectedCategory);
                                                        branch.put("cospaceAddress", addressBranch);
                                                        branch.put("owner_id", user.getUid());
                                                        branch.put("cospaceId", "");

                                                        firebaseFirestore.collection("CospaceBranches")
                                                                .add(branch)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        FirebaseFirestore.getInstance().collection("CospaceBranches")
                                                                                .document(documentReference.getId())
                                                                                .update("cospaceId", documentReference.getId())
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        Toast.makeText(RegisterOwner_SpaceBranch.this, "Branch registered!", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });
                                                                        startActivity(new Intent(RegisterOwner_SpaceBranch.this,OwnerHomepage.class));
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
        });*/
    }

    private void registerBranch(String namebranch, String addressBranch, String selectedCategory){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(filepath == null || namebranch.isEmpty() || addressBranch.isEmpty()) {
            Toast.makeText(RegisterOwner_SpaceBranch.this, "Please fill all fields and choose an image.", Toast.LENGTH_SHORT).show();
            return;
        }
            // Check for existing branch name based owner_id:current user
            firebaseFirestore.collection("CospaceBranches")
                    .whereEqualTo("cospaceName", namebranch)
                    .whereEqualTo("owner_id", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                Toast.makeText(RegisterOwner_SpaceBranch.this, "A branch with this name already exists.", Toast.LENGTH_SHORT).show();
                            } else{
                                progressDialog.setMessage("Registering branch...");
                                progressDialog.show();
                                StorageReference path = firebaseStorage.getReference().child("BranchImages").child(filepath.getLastPathSegment());
                                path.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                Map<String,String> branch = new HashMap<>();
                                                branch.put("cospaceImage",task.getResult().toString());
                                                branch.put("cospaceName", namebranch);
                                                branch.put("cospaceCategory", selectedCategory);
                                                branch.put("cospaceAddress", addressBranch);
                                                branch.put("owner_id", user.getUid());
                                                branch.put("cospaceId", "");

                                                firebaseFirestore.collection("CospaceBranches")
                                                        .add(branch)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                FirebaseFirestore.getInstance().collection("CospaceBranches")
                                                                        .document(documentReference.getId())
                                                                        .update("cospaceId", documentReference.getId())
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                Toast.makeText(RegisterOwner_SpaceBranch.this, "Branch registered!", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                progressDialog.dismiss();
                                                                startActivity(new Intent(RegisterOwner_SpaceBranch.this,OwnerHomepage.class));
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
