package com.example.caspaceapplication.Owner.Profile;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.caspaceapplication.Owner.AmenitiesOffered.Owner_AmenitiesOffered;
import com.example.caspaceapplication.Owner.BranchModel;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Owner_BranchProfile extends Fragment {

    CollectionReference colref = FirebaseFirestore.getInstance().collection("CospaceBranches");
    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TextView branchName, About, StreetLocation, CityLocation, Contact, FloorMapDesc, listOfRooms, Pricing, Plans;
    ImageView branchPicture, floorMap;
    AppCompatButton seeAllLayoutsButton, AmenitiesEdit;
    ImageButton EditBranchProfile;

    private static final int PICK_BRANCH_IMAGE_REQUEST = 1;
    private static final int PICK_FLOOR_MAP_IMAGE_REQUEST  = 2;
    public Uri selectedBranchImageUri, selectedFloorMapImageUri;
    Button selectBranchImageButton, selectFloorMapImageButton;

    TextView CWSHours_MondayStartTextview, CWSHours_MondayEndTextview,
            CWSHours_TuesdayStartTextview, CWSHours_TuesdayEndTextview,
            CWSHours_WednesdayStartTextview, CWSHours_WednesdayEndTextview,
            CWSHours_ThursdayStartTextview, CWSHours_ThursdayEndTextview,
            CWSHours_FridayStartTextview, CWSHours_FridayEndTextview,
            CWSHours_SaturdayStartTextview, CWSHours_SaturdayEndTextview,
            CWSHours_SundayStartTextview, CWSHours_SundayEndTextview;

    Boolean isMondayClosed = false, isTuesdayClosed = false, isWednesdayClosed = false, isThursdayClosed = false, isFridayClosed = false, isSaturdayClosed = false, isSundayClosed = false;


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
        Contact = rootview.findViewById(R.id.CWSProfile_ContactInfo_Textview);
        floorMap = rootview.findViewById(R.id.CWSProfile_FloorMap_Imageview);
        selectFloorMapImageButton = rootview.findViewById(R.id.selectFloorMapImage_Button);
        FloorMapDesc = rootview.findViewById(R.id.CWSProfile_FloorMapDescription_Textview);
        seeAllLayoutsButton = rootview.findViewById(R.id.CWSProfile_SeeAllLayouts_Button);
        listOfRooms = rootview.findViewById(R.id.CWSProfile_ListOfRooms_Textview);
        Pricing = rootview.findViewById(R.id.CWSProfile_PricingContent_Textview);
        Plans = rootview.findViewById(R.id.CWSProfile_Plans_Textview);

        CWSHours_MondayStartTextview = rootview.findViewById(R.id.ProfileCWSHours_MondayStart_Textview);
        CWSHours_MondayEndTextview = rootview.findViewById(R.id.ProfileCWSHours_MondayEnd_Textview);
        CWSHours_TuesdayStartTextview = rootview.findViewById(R.id.ProfileCWSHours_TuesdayStart_Textview);
        CWSHours_TuesdayEndTextview = rootview.findViewById(R.id.ProfileCWSHours_TuesdayEnd_Textview);
        CWSHours_WednesdayStartTextview = rootview.findViewById(R.id.ProfileCWSHours_WednesdayStart_Textview);
        CWSHours_WednesdayEndTextview = rootview.findViewById(R.id.ProfileCWSHours_WednesdayEnd_Textview);
        CWSHours_ThursdayStartTextview = rootview.findViewById(R.id.ProfileCWSHours_ThursdayStart_Textview);
        CWSHours_ThursdayEndTextview = rootview.findViewById(R.id.ProfileCWSHours_ThursdayEnd_Textview);
        CWSHours_FridayStartTextview = rootview.findViewById(R.id.ProfileCWSHours_FridayStart_Textview);
        CWSHours_FridayEndTextview = rootview.findViewById(R.id.ProfileCWSHours_FridayEnd_Textview);
        CWSHours_SaturdayStartTextview = rootview.findViewById(R.id.ProfileCWSHours_SaturdayStart_Textview);
        CWSHours_SaturdayEndTextview = rootview.findViewById(R.id.ProfileCWSHours_SaturdayEnd_Textview);
        CWSHours_SundayStartTextview = rootview.findViewById(R.id.ProfileCWSHours_SundayStart_Textview);
        CWSHours_SundayEndTextview = rootview.findViewById(R.id.ProfileCWSHours_SundayEnd_Textview);

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
                                String contact = documentSnapshot.getString("cospaceContactInfo");
                                String floorMapImage = documentSnapshot.getString("cospaceFloorMapImage");
                                String pricing = documentSnapshot.getString("cospacePricing");
                                String plans = documentSnapshot.getString("cospacePlans");

                                Map<String, Object> data = documentSnapshot.getData();
                                Map<String, Object> hours = (Map<String, Object>) data.get("hours");
                                if (hours.containsKey("Monday")) {
                                    Map<String, Object> mondayHours = (Map<String, Object>) hours.get("Monday");
                                    if (mondayHours.containsKey("openTime")) {
                                        String mondayStartTime = mondayHours.get("openTime").toString();
                                        CWSHours_MondayStartTextview.setText(mondayStartTime);
                                    }
                                    if (mondayHours.containsKey("closeTime")) {
                                        String mondayCloseTime = mondayHours.get("closeTime").toString();
                                        CWSHours_MondayEndTextview.setText(mondayCloseTime);
                                    }
                                }

                                if (hours.containsKey("Tuesday")) {
                                    Map<String, Object> tuesdayHours = (Map<String, Object>) hours.get("Tuesday");
                                    if (tuesdayHours.containsKey("openTime")) {
                                        String tuesdayStartTime = tuesdayHours.get("openTime").toString();
                                        CWSHours_TuesdayStartTextview.setText(tuesdayStartTime);
                                    }
                                    if (tuesdayHours.containsKey("closeTime")) {
                                        String tuesdayCloseTime = tuesdayHours.get("closeTime").toString();
                                        CWSHours_TuesdayEndTextview.setText(tuesdayCloseTime);
                                    }
                                }

                                if (hours.containsKey("Wednesday")) {
                                    Map<String, Object> wednesdayHours = (Map<String, Object>) hours.get("Wednesday");
                                    if (wednesdayHours.containsKey("openTime")) {
                                        String wednesdayStartTime = wednesdayHours.get("openTime").toString();
                                        CWSHours_WednesdayStartTextview.setText(wednesdayStartTime);
                                    }
                                    if (wednesdayHours.containsKey("closeTime")) {
                                        String wednesdayCloseTime = wednesdayHours.get("closeTime").toString();
                                        CWSHours_WednesdayEndTextview.setText(wednesdayCloseTime);
                                    }
                                }

                                if (hours.containsKey("Thursday")) {
                                    Map<String, Object> thursdayHours = (Map<String, Object>) hours.get("Thursday");
                                    if (thursdayHours.containsKey("openTime")) {
                                        String thursdayStartTime = thursdayHours.get("openTime").toString();
                                        CWSHours_ThursdayStartTextview.setText(thursdayStartTime);
                                    }
                                    if (thursdayHours.containsKey("closeTime")) {
                                        String thursdayCloseTime = thursdayHours.get("closeTime").toString();
                                        CWSHours_ThursdayEndTextview.setText(thursdayCloseTime);
                                    }
                                }

                                if (hours.containsKey("Friday")) {
                                    Map<String, Object> fridayHours = (Map<String, Object>) hours.get("Friday");
                                    if (fridayHours.containsKey("openTime")) {
                                        String fridayStartTime = fridayHours.get("openTime").toString();
                                        CWSHours_FridayStartTextview.setText(fridayStartTime);
                                    }
                                    if (fridayHours.containsKey("closeTime")) {
                                        String fridayCloseTime = fridayHours.get("closeTime").toString();
                                        CWSHours_FridayEndTextview.setText(fridayCloseTime);
                                    }
                                }

                                if (hours.containsKey("Saturday")) {
                                    Map<String, Object> saturdayHours = (Map<String, Object>) hours.get("Saturday");
                                    if (saturdayHours.containsKey("openTime")) {
                                        String saturdayStartTime = saturdayHours.get("openTime").toString();
                                        CWSHours_SaturdayStartTextview.setText(saturdayStartTime);
                                    }
                                    if (saturdayHours.containsKey("closeTime")) {
                                        String saturdayCloseTime = saturdayHours.get("closeTime").toString();
                                        CWSHours_SaturdayEndTextview.setText(saturdayCloseTime);
                                    }
                                }

                                if (hours.containsKey("Sunday")) {
                                    Map<String, Object> sundayHours = (Map<String, Object>) hours.get("Sunday");
                                    if (sundayHours.containsKey("openTime")) {
                                        String sundayStartTime = sundayHours.get("openTime").toString();
                                        CWSHours_SundayStartTextview.setText(sundayStartTime);
                                    }
                                    if (sundayHours.containsKey("closeTime")) {
                                        String sundayCloseTime = sundayHours.get("closeTime").toString();
                                        CWSHours_SundayEndTextview.setText(sundayCloseTime);
                                    }
                                }

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

    private void showTimePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Set selected time to EditText
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedTime.set(Calendar.MINUTE, minute);

                // Format time and set to EditText
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String formattedTime = sdf.format(selectedTime.getTime());
                editText.setText(formattedTime);

                // Display the date after the time
                String selectedDate = "Selected date: " + sdf.format(Calendar.getInstance().getTime());
                Toast.makeText(getContext(), selectedDate, Toast.LENGTH_SHORT).show();
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }


    public void EditPOPUP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View editPopup = getLayoutInflater().inflate(R.layout.cwsprofile_edittextviews, null);

        TextView CWSProfile_BranchName = editPopup.findViewById(R.id.CWSProfile_BranchName_Edittext);
        ImageButton CWSProfile_Cancel = editPopup.findViewById(R.id.CWSProfile_Cancel_Imagebutton);
        ImageButton CWSProfile_Save = editPopup.findViewById(R.id.CWSProfile_Save_Imagebutton);
        EditText CWSProfile_AboutContent = editPopup.findViewById(R.id.CWSProfile_AboutContent_Edittext);
        EditText CWSProfile_LocationStreetContent = editPopup.findViewById(R.id.CWSProfile_LocationStreetContent_Edittext);
        EditText CWSProfile_LocationCityContent = editPopup.findViewById(R.id.CWSProfile_LocationCityContent_Edittext);
        EditText ProfileCWSHours_MondayStart = editPopup.findViewById(R.id.ProfileCWSHours_MondayStart_Edittext);
        EditText ProfileCWSHours_MondayEnd = editPopup.findViewById(R.id.ProfileCWSHours_MondayEnd_Edittext);
        EditText ProfileCWSHours_TuesdayStart = editPopup.findViewById(R.id.ProfileCWSHours_TuesdayStart_Edittext);
        EditText ProfileCWSHours_TuesdayEnd = editPopup.findViewById(R.id.ProfileCWSHours_TuesdayEnd_Edittext);
        EditText ProfileCWSHours_WednesdayStart = editPopup.findViewById(R.id.ProfileCWSHours_WednesdayStart_Edittext);
        EditText ProfileCWSHours_WednesdayEnd = editPopup.findViewById(R.id.ProfileCWSHours_WednesdayEnd_Edittext);
        EditText ProfileCWSHours_ThursdayStart = editPopup.findViewById(R.id.ProfileCWSHours_ThursdayStart_Edittext);
        EditText ProfileCWSHours_ThursdayEnd = editPopup.findViewById(R.id.ProfileCWSHours_ThursdayEnd_Edittext);
        EditText ProfileCWSHours_FridayStart = editPopup.findViewById(R.id.ProfileCWSHours_FridayStart_Edittext);
        EditText ProfileCWSHours_FridayEnd = editPopup.findViewById(R.id.ProfileCWSHours_FridayEnd_Edittext);
        EditText ProfileCWSHours_SaturdayStart = editPopup.findViewById(R.id.ProfileCWSHours_SaturdayStart_Edittext);
        EditText ProfileCWSHours_SaturdayEnd = editPopup.findViewById(R.id.ProfileCWSHours_SaturdayEnd_Edittext);
        EditText ProfileCWSHours_SundayStart = editPopup.findViewById(R.id.ProfileCWSHours_SundayStart_Edittext);
        EditText ProfileCWSHours_SundayEnd = editPopup.findViewById(R.id.ProfileCWSHours_SundayEnd_Edittext);
        AppCompatButton ProfileCWSHours_Monday24hours = editPopup.findViewById(R.id.ProfileCWSHours_Monday24hours_AppCompButton);
        AppCompatButton ProfileCWSHours_MondayClose = editPopup.findViewById(R.id.ProfileCWSHours_MondayClose_AppCompButton);
        AppCompatButton ProfileCWSHours_Tuesday24hours = editPopup.findViewById(R.id.ProfileCWSHours_Tuesday24hours_AppCompButton);
        AppCompatButton ProfileCWSHours_TuesdayClose = editPopup.findViewById(R.id.ProfileCWSHours_TuesdayClose_AppCompButton);
        AppCompatButton ProfileCWSHours_Wednesday24hours = editPopup.findViewById(R.id.ProfileCWSHours_Wednesday24hours_AppCompButton);
        AppCompatButton ProfileCWSHours_WednesdayClose = editPopup.findViewById(R.id.ProfileCWSHours_WednesdayClose_AppCompButton);
        AppCompatButton ProfileCWSHours_Thursday24hours = editPopup.findViewById(R.id.ProfileCWSHours_Thursday24hours_AppCompButton);
        AppCompatButton ProfileCWSHours_ThursdayClose = editPopup.findViewById(R.id.ProfileCWSHours_ThursdayClose_AppCompButton);
        AppCompatButton ProfileCWSHours_Friday24hours = editPopup.findViewById(R.id.ProfileCWSHours_Friday24hours_AppCompButton);
        AppCompatButton ProfileCWSHours_FridayClose = editPopup.findViewById(R.id.ProfileCWSHours_FridayClose_AppCompButton);
        AppCompatButton ProfileCWSHours_Saturday24hours = editPopup.findViewById(R.id.ProfileCWSHours_Saturday24hours_AppCompButton);
        AppCompatButton ProfileCWSHours_SaturdayClose = editPopup.findViewById(R.id.ProfileCWSHours_SaturdayClose_AppCompButton);
        AppCompatButton ProfileCWSHours_Sunday24hours = editPopup.findViewById(R.id.ProfileCWSHours_Sunday24hours_AppCompButton);
        AppCompatButton ProfileCWSHours_SundayClose = editPopup.findViewById(R.id.ProfileCWSHours_SundayClose_AppCompButton);
        EditText CWSProfile_ContactInfo = editPopup.findViewById(R.id.CWSProfile_ContactInfo_Edittext);
        EditText CWSProfile_FloorMapDescription = editPopup.findViewById(R.id.CWSProfile_FloorMapDescription_Edittext);
        EditText CWSProfile_ListOfRooms = editPopup.findViewById(R.id.CWSProfile_ListOfRooms_Edittext);
        EditText CWSProfile_PricingContent = editPopup.findViewById(R.id.CWSProfile_PricingContent_Edittext);
        EditText CWSProfile_Plans = editPopup.findViewById(R.id.CWSProfile_Plans_Edittext);

        CWSProfile_BranchName.setText(branchName.getText());
        CWSProfile_AboutContent.setText(About.getText());
        CWSProfile_LocationStreetContent.setText(StreetLocation.getText());
        CWSProfile_LocationCityContent.setText(CityLocation.getText());
        CWSProfile_ContactInfo.setText(Contact.getText());
        CWSProfile_FloorMapDescription.setText(FloorMapDesc.getText());
        CWSProfile_ListOfRooms.setText(listOfRooms.getText());
        CWSProfile_PricingContent.setText(Pricing.getText());
        CWSProfile_Plans.setText(Plans.getText());
        ProfileCWSHours_MondayStart.setText(CWSHours_MondayStartTextview.getText());
        ProfileCWSHours_MondayEnd.setText(CWSHours_MondayEndTextview.getText());
        ProfileCWSHours_TuesdayStart.setText(CWSHours_TuesdayStartTextview.getText());
        ProfileCWSHours_TuesdayEnd.setText(CWSHours_TuesdayEndTextview.getText());
        ProfileCWSHours_WednesdayStart.setText(CWSHours_WednesdayStartTextview.getText());
        ProfileCWSHours_WednesdayEnd.setText(CWSHours_WednesdayEndTextview.getText());
        ProfileCWSHours_ThursdayStart.setText(CWSHours_ThursdayStartTextview.getText());
        ProfileCWSHours_ThursdayEnd.setText(CWSHours_ThursdayEndTextview.getText());
        ProfileCWSHours_FridayStart.setText(CWSHours_FridayStartTextview.getText());
        ProfileCWSHours_FridayEnd.setText(CWSHours_FridayEndTextview.getText());
        ProfileCWSHours_SaturdayStart.setText(CWSHours_SaturdayStartTextview.getText());
        ProfileCWSHours_SaturdayEnd.setText(CWSHours_SaturdayEndTextview.getText());
        ProfileCWSHours_SundayStart.setText(CWSHours_SundayStartTextview.getText());
        ProfileCWSHours_SundayEnd.setText(CWSHours_SundayEndTextview.getText());

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

        ProfileCWSHours_MondayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ProfileCWSHours_MondayStart);
            }
        });

        ProfileCWSHours_MondayEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ProfileCWSHours_MondayEnd);
            }
        });

        ProfileCWSHours_Monday24hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set opening hours to 24 hours
                ProfileCWSHours_MondayStart.setText("00:00 AM");
                ProfileCWSHours_MondayEnd.setText("12:00 AM");
            }
        });

        ProfileCWSHours_MondayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileCWSHours_MondayStart.setText("Closed");
                ProfileCWSHours_MondayEnd.setText("Closed");
                ProfileCWSHours_MondayStart.setPaintFlags(ProfileCWSHours_MondayStart.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ProfileCWSHours_MondayEnd.setPaintFlags(ProfileCWSHours_MondayEnd.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                isMondayClosed = true;
            }
        });

        ProfileCWSHours_TuesdayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ProfileCWSHours_TuesdayStart);
            }
        });

        ProfileCWSHours_TuesdayEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ProfileCWSHours_TuesdayEnd);
            }
        });

        ProfileCWSHours_Tuesday24hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set opening hours to 24 hours
                ProfileCWSHours_TuesdayStart.setText("00:00 AM");
                ProfileCWSHours_TuesdayEnd.setText("12:00 AM");
            }
        });

        ProfileCWSHours_TuesdayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileCWSHours_TuesdayStart.setText("Closed");
                ProfileCWSHours_TuesdayEnd.setText("Closed");
                ProfileCWSHours_TuesdayStart.setPaintFlags(ProfileCWSHours_TuesdayStart.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ProfileCWSHours_TuesdayEnd.setPaintFlags(ProfileCWSHours_TuesdayEnd.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                isTuesdayClosed = true;
            }
        });

        ProfileCWSHours_WednesdayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ProfileCWSHours_WednesdayStart);
            }
        });

        ProfileCWSHours_WednesdayEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ProfileCWSHours_WednesdayEnd);
            }
        });

        ProfileCWSHours_Wednesday24hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set opening hours to 24 hours
                ProfileCWSHours_WednesdayStart.setText("00:00 AM");
                ProfileCWSHours_WednesdayEnd.setText("12:00 AM");
            }
        });

        ProfileCWSHours_WednesdayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileCWSHours_WednesdayStart.setText("Closed");
                ProfileCWSHours_WednesdayEnd.setText("Closed");
                ProfileCWSHours_WednesdayStart.setPaintFlags(ProfileCWSHours_WednesdayStart.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ProfileCWSHours_WednesdayEnd.setPaintFlags(ProfileCWSHours_WednesdayEnd.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                isWednesdayClosed = true;
            }
        });

        ProfileCWSHours_ThursdayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ProfileCWSHours_ThursdayStart);
            }
        });

        ProfileCWSHours_ThursdayEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ProfileCWSHours_ThursdayEnd);
            }
        });

        ProfileCWSHours_Thursday24hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set opening hours to 24 hours
                ProfileCWSHours_ThursdayStart.setText("00:00 AM");
                ProfileCWSHours_ThursdayEnd.setText("12:00 AM");
            }
        });

        ProfileCWSHours_ThursdayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileCWSHours_ThursdayStart.setText("Closed");
                ProfileCWSHours_ThursdayEnd.setText("Closed");
                ProfileCWSHours_ThursdayStart.setPaintFlags(ProfileCWSHours_ThursdayStart.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ProfileCWSHours_ThursdayEnd.setPaintFlags(ProfileCWSHours_ThursdayEnd.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                isThursdayClosed = true;
            }
        });

        ProfileCWSHours_FridayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ProfileCWSHours_FridayStart);
            }
        });

        ProfileCWSHours_FridayEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ProfileCWSHours_FridayEnd);
            }
        });

        ProfileCWSHours_Friday24hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set opening hours to 24 hours
                ProfileCWSHours_FridayStart.setText("00:00 AM");
                ProfileCWSHours_FridayEnd.setText("12:00 AM");
            }
        });

        ProfileCWSHours_FridayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileCWSHours_FridayStart.setText("Closed");
                ProfileCWSHours_FridayEnd.setText("Closed");
                ProfileCWSHours_FridayStart.setPaintFlags(ProfileCWSHours_FridayStart.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ProfileCWSHours_FridayEnd.setPaintFlags(ProfileCWSHours_FridayEnd.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                isFridayClosed = true;
            }
        });

        ProfileCWSHours_SaturdayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ProfileCWSHours_SaturdayStart);
            }
        });

        ProfileCWSHours_SaturdayEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ProfileCWSHours_SaturdayEnd);
            }
        });

        ProfileCWSHours_Saturday24hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set opening hours to 24 hours
                ProfileCWSHours_SaturdayStart.setText("00:00 AM");
                ProfileCWSHours_SaturdayEnd.setText("12:00 AM");
            }
        });

        ProfileCWSHours_SaturdayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileCWSHours_SaturdayStart.setText("Closed");
                ProfileCWSHours_SaturdayEnd.setText("Closed");
                ProfileCWSHours_SaturdayStart.setPaintFlags(ProfileCWSHours_SaturdayStart.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ProfileCWSHours_SaturdayEnd.setPaintFlags(ProfileCWSHours_SaturdayEnd.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                isSaturdayClosed = true;
            }
        });

        ProfileCWSHours_SundayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ProfileCWSHours_SundayStart);
            }
        });

        ProfileCWSHours_SundayEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ProfileCWSHours_SundayEnd);
            }
        });

        ProfileCWSHours_Sunday24hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set opening hours to 24 hours
                ProfileCWSHours_SundayStart.setText("00:00 AM");
                ProfileCWSHours_SundayEnd.setText("12:00 AM");
            }
        });

        ProfileCWSHours_SundayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileCWSHours_SundayStart.setText("Closed");
                ProfileCWSHours_SundayEnd.setText("Closed");
                ProfileCWSHours_SundayStart.setPaintFlags(ProfileCWSHours_SundayStart.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ProfileCWSHours_SundayEnd.setPaintFlags(ProfileCWSHours_SundayEnd.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                isSundayClosed = true;
            }
        });


        CWSProfile_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, BranchModel.OpeningHours> updatedHours = new HashMap<>();
                updatedHours.put("Monday", new BranchModel.OpeningHours(isMondayClosed, ProfileCWSHours_MondayStart.getText().toString(), ProfileCWSHours_MondayEnd.getText().toString()));
                updatedHours.put("Tuesday", new BranchModel.OpeningHours(isTuesdayClosed, ProfileCWSHours_TuesdayStart.getText().toString(), ProfileCWSHours_TuesdayEnd.getText().toString()));
                updatedHours.put("Wednesday", new BranchModel.OpeningHours(isWednesdayClosed, ProfileCWSHours_WednesdayStart.getText().toString(), ProfileCWSHours_WednesdayEnd.getText().toString()));
                updatedHours.put("Thursday", new BranchModel.OpeningHours(isThursdayClosed, ProfileCWSHours_ThursdayStart.getText().toString(), ProfileCWSHours_ThursdayEnd.getText().toString()));
                updatedHours.put("Friday", new BranchModel.OpeningHours(isFridayClosed, ProfileCWSHours_FridayStart.getText().toString(), ProfileCWSHours_FridayEnd.getText().toString()));
                updatedHours.put("Saturday", new BranchModel.OpeningHours(isSaturdayClosed, ProfileCWSHours_SaturdayStart.getText().toString(), ProfileCWSHours_SaturdayEnd.getText().toString()));
                updatedHours.put("Sunday", new BranchModel.OpeningHours(isSundayClosed, ProfileCWSHours_SundayStart.getText().toString(), ProfileCWSHours_SundayEnd.getText().toString()));

                String name = CWSProfile_BranchName.getText().toString().trim();
                String about = CWSProfile_AboutContent.getText().toString().trim();
                String streetLocation = CWSProfile_LocationStreetContent.getText().toString().trim();
                String cityLocation = CWSProfile_LocationCityContent.getText().toString().trim();
                //String storeHours = CWSProfile_StoreHours.getText().toString().trim();
                String contact = CWSProfile_ContactInfo.getText().toString().trim();
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
                                                        "hours", updatedHours,
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