package com.example.caspaceapplication.Owner.Profile;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.caspaceapplication.Owner.AmenitiesOffered.Owner_AmenitiesOffered;
import com.example.caspaceapplication.Owner.OfficeLayouts.Owner_OfficeLayouts;
import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Owner_BranchProfile extends Fragment {

    CollectionReference colref = FirebaseFirestore.getInstance().collection("CospaceBranches");
    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TextView branchName, About, StreetLocation, CityLocation, StoreHours, Contact, FloorMapDesc, listOfRooms, Pricing, Plans;
    ImageView branchPicture, floorMap;
    AppCompatButton seeAllLayoutsButton, AmenitiesEdit;
    ImageButton EditBranchProfile;

    private static final int PICK_BRANCH_IMAGE_REQUEST = 1;
    private static final int PICK_FLOOR_MAP_IMAGE_REQUEST  = 2;
    public Uri selectedBranchImageUri, selectedFloorMapImageUri;
    Button selectBranchImageButton, selectFloorMapImageButton;

    public Owner_BranchProfile() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_owner__branch_profile, container, false);

        ImageButton owner_userProfileButton = rootview.findViewById(R.id.owner_userProfile_Button);
        owner_userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager();
                Owner_UserProfile fragment = new Owner_UserProfile();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout_branchProfile, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        branchName = rootview.findViewById(R.id.CWSProfile_BranchName_Textview);
        branchPicture = rootview.findViewById(R.id.CWSProfile_BranchPicture_Imageview);
        selectBranchImageButton = rootview.findViewById(R.id.selectBranchImage_Button);
        About = rootview.findViewById(R.id.CWSProfile_AboutContent_Textview);
        StreetLocation = rootview.findViewById(R.id.CWSProfile_LocationStreetContent_Textview);
        CityLocation = rootview.findViewById(R.id.CWSProfile_LocationCityContent_Textview);
        StoreHours = rootview.findViewById(R.id.CWSProfile_StoreHours_Textview);
        Contact = rootview.findViewById(R.id.CWSProfile_ContactInfo_Textview);
        floorMap = rootview.findViewById(R.id.CWSProfile_FloorMap_Imageview);
        selectFloorMapImageButton = rootview.findViewById(R.id.selectFloorMapImage_Button);
        FloorMapDesc = rootview.findViewById(R.id.CWSProfile_FloorMapDescription_Textview);
        seeAllLayoutsButton = rootview.findViewById(R.id.CWSProfile_SeeAllLayouts_Button);
        listOfRooms = rootview.findViewById(R.id.CWSProfile_ListOfRooms_Textview);
        Pricing = rootview.findViewById(R.id.CWSProfile_PricingContent_Textview);
        Plans = rootview.findViewById(R.id.CWSProfile_Plans_Textview);

        EditBranchProfile = rootview.findViewById(R.id. CWSProfile_EditButton);
        EditBranchProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit branch profile");
                builder.setMessage("Are you sure you want to edit your branch profile?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditPOPUP();
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

        AmenitiesEdit = rootview.findViewById(R.id.CWSProfile_AmenitiesEdit_Button);
        AmenitiesEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Owner_AmenitiesOffered.class));
            }
        });

        seeAllLayoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Owner_OfficeLayouts.class));
            }
        });

        selectBranchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_BRANCH_IMAGE_REQUEST);
            }
        });

        selectFloorMapImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FLOOR_MAP_IMAGE_REQUEST);
            }
        });


        retrieveAll();

        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_BRANCH_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedBranchImageUri = data.getData();
            StorageReference branchImageRef = FirebaseStorage.getInstance().getReference().child("CWSBranch/" + user + "/picture");
            // Uploading the branch image to Firebase Storage
            branchImageRef.putFile(selectedBranchImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    branchImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            colref.whereEqualTo("owner_id", user).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()){
                                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                            String docId = documentSnapshot.getId();
                                            colref.document(docId).update("cospaceImage", uri.toString())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(getContext(), "Branch image saved!", Toast.LENGTH_SHORT).show();
                                                            retrieveAll();
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

        if (requestCode == PICK_FLOOR_MAP_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedFloorMapImageUri = data.getData();
            StorageReference floorMapImageRef = FirebaseStorage.getInstance().getReference().child("CWSBranchFloorMap/" + user + "/picture");
            // Uploading the floor map image to Firebase Storage
            floorMapImageRef.putFile(selectedFloorMapImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    floorMapImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            colref.whereEqualTo("owner_id", user).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                            String docId = documentSnapshot.getId();
                                            colref.document(docId).update("cospaceFloorMapImage", uri.toString())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(getContext(), "Floor Map image saved!", Toast.LENGTH_SHORT).show();
                                                            retrieveAll();
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

    public void retrieveAll(){
        colref.whereEqualTo("owner_id", user).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                // Retrieve owner user details from Firestore
                                String Image = documentSnapshot.getString("cospaceImage");
                                String name = documentSnapshot.getString("cospaceName");
                                String category = documentSnapshot.getString("cospaceCategory");
                                String cityAddress = documentSnapshot.getString("cospaceCityAddress");
                                String streetAddress = documentSnapshot.getString("cospaceStreetAddress");
                                String storeHours = documentSnapshot.getString("cospaceStoreHours");
                                String contact = documentSnapshot.getString("cospaceContactInfo");
                                String floorMapImage = documentSnapshot.getString("cospaceFloorMapImage");
                                String pricing = documentSnapshot.getString("cospacePricing");
                                String plans = documentSnapshot.getString("cospacePlans");

                                // Set the retrieved details to the UI
                                if (Image == null || Image.isEmpty()){
                                    Picasso.get().load(R.drawable.uploadphoto).into(branchPicture);
                                }else{
                                    Picasso.get().load(Image).into(branchPicture);

                                }
                                branchName.setText(name);
                                About.setText("Category: " + category);
                                StreetLocation.setText(streetAddress);
                                CityLocation.setText(cityAddress);
                                StoreHours.setText(storeHours);
                                Contact.setText(contact);
                                if (floorMapImage == null || floorMapImage.isEmpty()){
                                    Picasso.get().load(R.drawable.uploadphoto).into(floorMap);
                                }
                                else{
                                    Picasso.get().load(floorMapImage).into(floorMap);
                                }
                                Pricing.setText(pricing);
                                Plans.setText(plans);
                            }
                        }
                    }
                });


    }

    public void EditPOPUP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View editPopup = getLayoutInflater().inflate(R.layout.cwsprofile_edittextviews, null);

        TextView CWSProfile_BranchName = editPopup.findViewById(R.id.CWSProfile_BranchName_Edittext);
        ImageButton CWSProfile_Cancel = editPopup.findViewById(R.id.CWSProfile_Cancel_Imagebutton);
        ImageButton CWSProfile_Save = editPopup.findViewById(R.id.CWSProfile_Save_Imagebutton);
        ImageView CWSProfile_BranchPicture = editPopup.findViewById(R.id.CWSProfile_BranchPicture_Imageview);//todo: branch pic
        EditText CWSProfile_AboutContent = editPopup.findViewById(R.id.CWSProfile_AboutContent_Edittext);
        EditText CWSProfile_LocationStreetContent = editPopup.findViewById(R.id.CWSProfile_LocationStreetContent_Edittext);
        EditText CWSProfile_LocationCityContent = editPopup.findViewById(R.id.CWSProfile_LocationCityContent_Edittext);
        EditText CWSProfile_StoreHours = editPopup.findViewById(R.id.CWSProfile_StoreHours_Edittext);
        EditText CWSProfile_ContactInfo = editPopup.findViewById(R.id.CWSProfile_ContactInfo_Edittext);
        ImageView CWSProfile_FloorMap = editPopup.findViewById(R.id.CWSProfile_FloorMap_Imageview);//todo: floor map
        EditText CWSProfile_FloorMapDescription = editPopup.findViewById(R.id.CWSProfile_FloorMapDescription_Edittext);
        EditText CWSProfile_ListOfRooms = editPopup.findViewById(R.id.CWSProfile_ListOfRooms_Edittext);
        EditText CWSProfile_PricingContent = editPopup.findViewById(R.id.CWSProfile_PricingContent_Edittext);
        EditText CWSProfile_Plans = editPopup.findViewById(R.id.CWSProfile_Plans_Edittext);

        //todo: branch picture
        CWSProfile_BranchName.setText(branchName.getText());
        CWSProfile_AboutContent.setText(About.getText());
        CWSProfile_LocationStreetContent.setText(StreetLocation.getText());
        CWSProfile_LocationCityContent.setText(CityLocation.getText());
        CWSProfile_StoreHours.setText(StoreHours.getText());
        CWSProfile_ContactInfo.setText(Contact.getText());
        // todo: CWSProfile_FloorMap picture
        CWSProfile_FloorMapDescription.setText(FloorMapDesc.getText());
        CWSProfile_ListOfRooms.setText(listOfRooms.getText());
        CWSProfile_PricingContent.setText(Pricing.getText());
        CWSProfile_Plans.setText(Plans.getText());

        builder.setView(editPopup);
        Dialog dialog;
        dialog = builder.create();
        dialog.show();

        CWSProfile_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Cancel edit", Toast.LENGTH_SHORT).show();
            }
        });

        CWSProfile_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: save branch image
                String name = CWSProfile_BranchName.getText().toString().trim();
                String about = CWSProfile_AboutContent.getText().toString().trim();
                String streetLocation = CWSProfile_LocationStreetContent.getText().toString().trim();
                String cityLocation = CWSProfile_LocationCityContent.getText().toString().trim();
                String storeHours = CWSProfile_StoreHours.getText().toString().trim();
                String contact = CWSProfile_ContactInfo.getText().toString().trim();
                //todo: floor plan image
                String floorMapDesc = CWSProfile_FloorMapDescription.getText().toString().trim();
                String pricing = CWSProfile_PricingContent.getText().toString().trim();
                String plans = CWSProfile_Plans.getText().toString().trim();

                colref.whereEqualTo("owner_id", user).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                        String documentId = documentSnapshot.getId();
                                        colref.document(documentId).update(
                                                        "cospaceName", name,
                                                        "cospaceAbout", about,
                                                        "cospaceStreetAddress", streetLocation,
                                                        "cospaceCityAddress", cityLocation,
                                                        "cospaceStoreHours", storeHours,
                                                        "cospaceContactInfo", contact,
                                                        "cospaceFloorMapDesc", floorMapDesc,
                                                        "cospacePricing", pricing,
                                                        "cospacePlans", plans)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getContext(), "Branch updated successfully", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                        retrieveAll();
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