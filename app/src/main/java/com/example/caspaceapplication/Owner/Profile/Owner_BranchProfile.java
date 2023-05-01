package com.example.caspaceapplication.Owner.Profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Owner_BranchProfile extends Fragment {

    CollectionReference colref = FirebaseFirestore.getInstance().collection("CospaceBranches");
    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TextView branchName, About, StreetLocation, CityLocation, StoreHours, Contact, FloorMapDesc, listOfRooms, Pricing, Plans;
    ImageView branchPicture, floorMap;
    AppCompatButton seeAllLayoutsButton, AmenitiesEdit;
    ImageButton EditBranchProfile;

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
        About = rootview.findViewById(R.id.CWSProfile_AboutContent_Textview);
        StreetLocation = rootview.findViewById(R.id.CWSProfile_LocationStreetContent_Textview);
        CityLocation = rootview.findViewById(R.id.CWSProfile_LocationCityContent_Textview);
        StoreHours = rootview.findViewById(R.id.CWSProfile_StoreHours_Textview);
        Contact = rootview.findViewById(R.id.CWSProfile_ContactInfo_Textview);
        //todo: how to amenities display
        floorMap = rootview.findViewById(R.id.CWSProfile_FloorMap_Imageview);//todo: picture!!
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


        retrieveAll();

        return rootview;
    }

    public void retrieveAll(){

        colref.whereEqualTo("owner_id", user).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                // Retrieve owner user details from Firestore
                                //todo: image
                                String Image = documentSnapshot.getString("cospaceImage");
                                String name = documentSnapshot.getString("cospaceName");
                                String category = documentSnapshot.getString("cospaceCategory");
                                String cityAddress = documentSnapshot.getString("cospaceCityAddress");
                                String streetAddress = documentSnapshot.getString("cospaceStreetAddress");
                                String storeHours = documentSnapshot.getString("cospaceStoreHours");
                                String contact = documentSnapshot.getString("cospaceContactInfo");
                                String pricing = documentSnapshot.getString("cospacePricing");
                                String plans = documentSnapshot.getString("cospacePlans");

                                // Set the retrieved details to the UI
                                branchName.setText(name);
                                About.setText("Category: " + category);
                                StreetLocation.setText(streetAddress);
                                CityLocation.setText(cityAddress);
                                StoreHours.setText(storeHours);
                                Contact.setText(contact);
                                Pricing.setText(pricing);
                                Plans.setText(plans);
                            }
                        }
                    }
                });


    }

    public void EditPOPUP(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View editPopup = getLayoutInflater().inflate(R.layout.cwsprofile_edittextviews, null);

        TextView CWSProfile_BranchName = editPopup.findViewById(R.id.CWSProfile_BranchName_Edittext);
        ImageButton CWSProfile_Cancel = editPopup.findViewById(R.id.CWSProfile_Cancel_Imagebutton);
        ImageButton CWSProfile_Save = editPopup.findViewById(R.id.CWSProfile_Save_Imagebutton);
        ImageView CWSProfile_BranchPicture = editPopup.findViewById(R.id.CWSProfile_BranchPicture_Imageview);
        EditText CWSProfile_AboutContent = editPopup.findViewById(R.id.CWSProfile_AboutContent_Edittext);
        EditText CWSProfile_LocationStreetContent = editPopup.findViewById(R.id.CWSProfile_LocationStreetContent_Edittext);
        EditText CWSProfile_LocationCityContent = editPopup.findViewById(R.id.CWSProfile_LocationCityContent_Edittext);
        EditText CWSProfile_StoreHours = editPopup.findViewById(R.id.CWSProfile_StoreHours_Edittext);
        EditText CWSProfile_ContactInfo = editPopup.findViewById(R.id.CWSProfile_ContactInfo_Edittext);
        ImageView CWSProfile_FloorMap = editPopup.findViewById(R.id.CWSProfile_FloorMap_Imageview);
        EditText CWSProfile_FloorMapDescription = editPopup.findViewById(R.id.CWSProfile_FloorMapDescription_Edittext);
        //skip button for see all layouts
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
        //CWSProfile_Amenities.setText("");todo: amenities
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