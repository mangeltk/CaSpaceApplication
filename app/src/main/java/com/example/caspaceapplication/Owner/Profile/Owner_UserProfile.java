package com.example.caspaceapplication.Owner.Profile;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.Front;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Owner_UserProfile extends Fragment {
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    CollectionReference colref = FirebaseFirestore.getInstance().collection("OwnerUserAccounts");

    ImageButton owner_userBranchButton;
    TextView ownerProfile_Status, ownerProfile_CompanyName, ownerProfile_Firstname, ownerProfile_Lastname, ownerProfile_IDNumber, ownerProfile_Email;
    AppCompatButton ownerEditUserProfileButton;
    Button selectImageButton;
    CircleImageView ownerProfileImageview;
    private static final int PICK_IMAGE_REQUEST = 1;
    public Uri selectedImageUri;

    public Owner_UserProfile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_owner__user_profile, container, false);

        owner_userBranchButton = rootView.findViewById(R.id.owner_userBranch_Button);


        owner_userBranchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                Owner_BranchProfile fragment = new Owner_BranchProfile();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout_ownerUserProfile, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        selectImageButton = rootView.findViewById(R.id.selectImage_Button);
        ownerProfileImageview = rootView.findViewById(R.id.ownerProfile_Image_Imageview);
        ownerProfile_Status = rootView.findViewById(R.id.ownerProfile_Status_Texview);
        ownerProfile_CompanyName = rootView.findViewById(R.id.ownerProfile_Company_Texview);
        ownerProfile_Firstname = rootView.findViewById(R.id.ownerProfile_Firstname_Texview);
        ownerProfile_Lastname = rootView.findViewById(R.id.ownerProfile_Lastname_Texview);
        ownerProfile_IDNumber = rootView.findViewById(R.id.ownerProfile_IDNumber_Texview);
        ownerProfile_Email = rootView.findViewById(R.id.ownerProfile_Email_Texview);

        retrieveOwnerUserDetails();

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        ownerEditUserProfileButton = rootView.findViewById(R.id.ownerEditUserProfile_Button);
        ownerEditUserProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit User Profile");
                builder.setMessage("Are you sure you want to edit your profile?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        popupEditDetails();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button deleteOwnerButton = view.findViewById(R.id.ownerDeleteProfile_Button);
        deleteOwnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete Account");
                builder.setMessage("Are you sure you want to delete your account?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        Button signOutButton = view.findViewById(R.id.ownerSignOutProfile_Button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Sign Out");
                builder.setMessage("Are you sure you want to sign out?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), Front.class);
                        startActivity(intent);
                        getActivity().finish();
                        ownerUserActivity();
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void ownerUserActivity(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String ownerId= firebaseAuth.getCurrentUser().getUid();
        String activity = "Sign Out";

        Map<String, Object> data = new HashMap<>();
        data.put("ownerId",ownerId);
        data.put("activity", activity);
        data.put("dateTime", Timestamp.now());

        db.collection("OwnerActivity")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Activity Stored.");
                    }
                });


    }

    private void deleteAccount() {
        // Delete owner's data from Firestore collections
        // Example:
        firebaseFirestore.collection("coworkingSpaces").whereEqualTo("ownerID", ownerProfile_IDNumber)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                        } else {
                            Log.d(TAG, "Error getting coworking spaces", task.getException());
                        }
                    }
                });

        // Delete owner's account from Firebase Auth
        firebaseAuth.getCurrentUser().delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Sign the owner out of the app
                            Intent intent = new Intent(getActivity(),Front.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Log.e(TAG, "Error deleting account", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            ownerProfileImageview.setImageURI(selectedImageUri);

            StorageReference profileImageRef = FirebaseStorage.getInstance().getReference().child("CWSOwner/" + user + "/profile");
            profileImageRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            colref.whereEqualTo("ownerIDNum", user).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()){
                                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                            String docId = documentSnapshot.getId();
                                            colref.document(docId).update("ownerImage", uri.toString())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(getContext(), "Profile image saved!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }


    public void retrieveOwnerUserDetails(){
        firebaseFirestore.collection("OwnerUserAccounts")
                .whereEqualTo("ownerIDNum", user)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                // Retrieve owner user details from Firestore
                                String image = documentSnapshot.getString("ownerImage");
                                String companyName = documentSnapshot.getString("ownerCompanyName");
                                String firstName = documentSnapshot.getString("ownerFirstname");
                                String lastName = documentSnapshot.getString("ownerLastname");
                                String idNumber = documentSnapshot.getString("ownerIDNum");
                                String email = documentSnapshot.getString("ownerEmail");
                                String ownerBranchStatus = documentSnapshot.getString("ownerBranchStatus");

                                // Set the retrieved details to the UI
                                if (image == null){
                                    Glide.with(getContext()).load(R.drawable.profileicon).into(ownerProfileImageview);
                                }else{
                                    Glide.with(getContext()).load(image).into(ownerProfileImageview);
                                }
                                ownerProfile_Status.setText("Branch " + ownerBranchStatus);
                                ownerProfile_CompanyName.setText(companyName);
                                ownerProfile_Firstname.setText(firstName);
                                ownerProfile_Lastname.setText(lastName);
                                ownerProfile_IDNumber.setText(idNumber);
                                ownerProfile_Email.setText(email);

                                if (ownerBranchStatus.equals("Unverified")){
                                    owner_userBranchButton.setVisibility(View.GONE);
                                    ownerProfile_Status.setTextColor(getResources().getColor(R.color.colorError));
                                }else{
                                    owner_userBranchButton.setVisibility(View.VISIBLE);
                                    ownerProfile_Status.setTextColor(getResources().getColor(R.color.colorSuccess));
                                }

                            }
                        }
                    }
                });
    }

    public void popupEditDetails(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());
        final View ownerEditDetailsPopup = getLayoutInflater().inflate(R.layout.owner_userprofileedit_popup,null);
        EditText ownerProfile_CompanyNamePOPUP = (EditText) ownerEditDetailsPopup.findViewById(R.id.ownerProfile_Company_Edittext);
        EditText ownerProfile_FirstnamePOPUP = (EditText) ownerEditDetailsPopup.findViewById(R.id.ownerProfile_Firstname_Edittext);
        EditText ownerProfile_LastnamePOPUP = (EditText) ownerEditDetailsPopup.findViewById(R.id.ownerProfile_Lastname_Edittext);
        TextView ownerProfile_IDNum_POPUP = (TextView) ownerEditDetailsPopup.findViewById(R.id.ownerProfile_IDNumber_TexviewPOPUP);
        TextView ownerProfile_Email_POPUP = (TextView) ownerEditDetailsPopup.findViewById(R.id.ownerProfile_Email_TexviewPOPUP);

        Button cancel = (Button) ownerEditDetailsPopup.findViewById(R.id.ownerCancelEditUserProfile_Button);
        Button edit = (Button) ownerEditDetailsPopup.findViewById(R.id.ownerEditUserProfile_Button);

        builder.setView(ownerEditDetailsPopup);
        Dialog dialog;
        dialog = builder.create();
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getContext(), "cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        ownerProfile_CompanyNamePOPUP.setText(ownerProfile_CompanyName.getText());
        ownerProfile_FirstnamePOPUP.setText(ownerProfile_Firstname.getText());
        ownerProfile_LastnamePOPUP.setText(ownerProfile_Lastname.getText());
        ownerProfile_IDNum_POPUP.setText(ownerProfile_IDNumber.getText());
        ownerProfile_Email_POPUP.setText(ownerProfile_Email.getText());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedCompanyName = ownerProfile_CompanyNamePOPUP.getText().toString().trim();
                String updatedFirstName = ownerProfile_FirstnamePOPUP.getText().toString().trim();
                String updatedLastName = ownerProfile_LastnamePOPUP.getText().toString().trim();

                if (updatedCompanyName.isEmpty()) {
                    ownerProfile_CompanyNamePOPUP.setError("Please enter company name");
                    ownerProfile_CompanyNamePOPUP.requestFocus();
                    return;
                }
                if (updatedFirstName.isEmpty()) {
                    ownerProfile_FirstnamePOPUP.setError("Please enter first name");
                    ownerProfile_FirstnamePOPUP.requestFocus();
                    return;
                }
                if (updatedLastName.isEmpty()) {
                    ownerProfile_LastnamePOPUP.setError("Please enter last name");
                    ownerProfile_LastnamePOPUP.requestFocus();
                    return;
                }

                String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference ownerUserDocRef = firebaseFirestore.collection("OwnerUserAccounts").document(user);

                ownerUserDocRef.update("ownerCompanyName", updatedCompanyName,
                        "ownerFirstname", updatedFirstName,
                        "ownerLastname", updatedLastName)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                retrieveOwnerUserDetails();
                            }
                        });

            }
        });
    }

}