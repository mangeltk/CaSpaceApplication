package com.example.caspaceapplication.Owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

    //Bitmap bitmap;
    //int PICK_IMAGE_GALLERY_CODE = 78;
    //int CAMERA_PERMISSION_REQUEST_CODE = 12345;
    //int CAMERA_PICTURE_REQUEST_CODE = 56789;
    Uri filepath = null;
    private static final int GALLERY_CODE = 1;
    ImageButton branch_image;
    Button registerButton_SpaceBranch;
    EditText branchName, branchAdrress;


    String [] selectItem = {"Office space for individual",
            "Office space exclusive for an organization",
            "Fabrication Laboratory (Fab lab)",
            "Technology Business Incubation Program (TBI)",
            "QBO Innovation Hub"};

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

        categorySpinner= findViewById(R.id.category_spinner);

        // Populate the spinner with category options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Office space for individual",
                "Office space exclusive for an organization",
                "Fabrication Laboratory (Fab lab)",
                "Technology Business Incubation Program (TBI)",
                "QBO Innovation Hub"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


        branch_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });

    }

    /*    public void showImageSelectedDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select image");
        builder.setMessage("Please select an option");

        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkCameraPermission();
                    dialog.dismiss();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
            }
        });

        builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void checkCameraPermission(){
        if(ContextCompat.checkSelfPermission(RegisterOwner_SpaceBranch.this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(RegisterOwner_SpaceBranch.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(RegisterOwner_SpaceBranch.this, new String[]{
                    android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            },CAMERA_PERMISSION_REQUEST_CODE);
        } else{
            openCamera();
        }
    }

    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity((getPackageManager())) != null){
            startActivityForResult(intent, CAMERA_PICTURE_REQUEST_CODE);
        }
    }*///NOT DONE: FEATURE ALLOWS APP TO USE CAMERA TO SELECT AND IMAGE

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_GALLERY_CODE && resultCode == Activity.RESULT_OK){
            if (data == null || data.getData() == null)
                return;

            filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode == CAMERA_PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(bitmap);
            filepath = getImageUri(getApplicationContext(), bitmap);
        }
    }*/

    /*public Uri getImageUri(Context context, Bitmap bitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
        return Uri.parse(path);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            filepath = data.getData();
            branch_image.setImageURI(filepath);
        }

        registerButton_SpaceBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String namebranch = branchName.getText().toString().trim();
                String addressBranch = branchAdrress.getText().toString().trim();

                if (!(namebranch.isEmpty() && addressBranch.isEmpty() && filepath!=null)){
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
                                    branch.put("cospaceCategory","");
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
                                                    startActivity(new Intent(RegisterOwner_SpaceBranch.this,LoginOwner.class));
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
