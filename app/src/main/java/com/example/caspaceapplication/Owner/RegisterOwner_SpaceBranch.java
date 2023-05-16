package com.example.caspaceapplication.Owner;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.example.caspaceapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterOwner_SpaceBranch extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();;
    ProgressDialog progressDialog;

    Uri filepath = null;
    private static final int GALLERY_CODE = 1;
    ImageButton branch_image;
    Button registerButton_SpaceBranch;
    EditText branchName, branchStreetAddress, branchCityAddress;

    AppCompatButton select_locationButton;
    TextView latitudeTextview, longitudeTextview;
    private MapView mapView;
    private GoogleMap googleMap;
    GeoPoint location;

    private Spinner categorySpinner;

    EditText CWSHours_MondayStartEdittext, CWSHours_MondayEndEdittext,
             CWSHours_TuesdayStartEdittext, CWSHours_TuesdayEndEdittext,
             CWSHours_WednesdayStartEdittext, CWSHours_WednesdayEndEdittext,
             CWSHours_ThursdayStartEdittext, CWSHours_ThursdayEndEdittext,
             CWSHours_FridayStartEdittext, CWSHours_FridayEndEdittext,
             CWSHours_SaturdayStartEdittext, CWSHours_SaturdayEndEdittext,
             CWSHours_SundayStartEdittext, CWSHours_SundayEndEdittext;

    AppCompatButton CWSHours_Monday24hoursAppCompButton, CWSHours_Tuesday24hoursAppCompButton,
                    CWSHours_Wednesday24hoursAppCompButton, CWSHours_Thursday24hoursAppCompButton,
                    CWSHours_Friday24hoursAppCompButton, CWSHours_Saturday24hoursAppCompButton, CWSHours_Sunday24hoursAppCompButton;

    AppCompatButton CWSHours_MondayCloseAppCompButton, CWSHours_TuesdayCloseAppCompButton,
                    CWSHours_WednesdayCloseAppCompButton, CWSHours_ThursdayCloseAppCompButton,
                    CWSHours_FridayCloseAppCompButton, CWSHours_SaturdayCloseAppCompButton, CWSHours_SundayCloseAppCompButton;

    Boolean isMondayClosed = false, isTuesdayClosed = false, isWednesdayClosed = false, isThursdayClosed = false, isFridayClosed = false, isSaturdayClosed = false, isSundayClosed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_owner_space_branch);

        progressDialog = new ProgressDialog(this);

        branch_image = findViewById(R.id.registerBranchImage_Imagebutton);
        registerButton_SpaceBranch = findViewById(R.id.registerButton_SpaceBranch);
        branchName = findViewById(R.id.registerBranchName_Edittext);
        branchStreetAddress = findViewById(R.id.registerBranchAddress1_Edittext);
        branchCityAddress = findViewById(R.id.registerBranchAddress2_Edittext);
        categorySpinner = findViewById(R.id.spinnerCategories);
        select_locationButton = findViewById(R.id.select_location_button);
        latitudeTextview = findViewById(R.id.latitudeDisplay);
        longitudeTextview = findViewById(R.id.longitudeDisplay);


        editTextFieldsForHours();

        branch_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });

        select_locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMap();
            }
        });

        registerButton_SpaceBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namebranch = branchName.getText().toString().trim();
                String streetAddressBranch = branchStreetAddress.getText().toString().trim();
                String cityAddressBranch = branchCityAddress.getText().toString().trim();
                String selectedCategory = categorySpinner.getSelectedItem().toString();

                registerBranch(namebranch, streetAddressBranch, cityAddressBranch, selectedCategory);
            }
        });

    }

    public void editTextFieldsForHours(){

        CWSHours_MondayStartEdittext = findViewById(R.id.CWSHours_MondayStart_Edittext);
        CWSHours_MondayEndEdittext = findViewById(R.id.CWSHours_MondayEnd_Edittext);
        CWSHours_Monday24hoursAppCompButton = findViewById(R.id.CWSHours_Monday24hours_AppCompButton);
        CWSHours_MondayCloseAppCompButton = findViewById(R.id.CWSHours_MondayClose_AppCompButton);

        CWSHours_TuesdayStartEdittext = findViewById(R.id.CWSHours_TuesdayStart_Edittext);
        CWSHours_TuesdayEndEdittext = findViewById(R.id.CWSHours_TuesdayEnd_Edittext);
        CWSHours_Tuesday24hoursAppCompButton = findViewById(R.id.CWSHours_Tuesday24hours_AppCompButton);
        CWSHours_TuesdayCloseAppCompButton = findViewById(R.id.CWSHours_TuesdayClose_AppCompButton);

        CWSHours_WednesdayStartEdittext = findViewById(R.id.CWSHours_WednesdayStart_Edittext);
        CWSHours_WednesdayEndEdittext = findViewById(R.id.CWSHours_WednesdayEnd_Edittext);
        CWSHours_Wednesday24hoursAppCompButton = findViewById(R.id.CWSHours_Wednesday24hours_AppCompButton);
        CWSHours_WednesdayCloseAppCompButton = findViewById(R.id.CWSHours_WednesdayClose_AppCompButton);

        CWSHours_ThursdayStartEdittext = findViewById(R.id.CWSHours_ThursdayStart_Edittext);
        CWSHours_ThursdayEndEdittext = findViewById(R.id.CWSHours_ThursdayEnd_Edittext);
        CWSHours_Thursday24hoursAppCompButton = findViewById(R.id.CWSHours_Thursday24hours_AppCompButton);
        CWSHours_ThursdayCloseAppCompButton = findViewById(R.id.CWSHours_ThursdayClose_AppCompButton);

        CWSHours_FridayStartEdittext = findViewById(R.id.CWSHours_FridayStart_Edittext);
        CWSHours_FridayEndEdittext = findViewById(R.id.CWSHours_FridayEnd_Edittext);
        CWSHours_Friday24hoursAppCompButton = findViewById(R.id.CWSHours_Friday24hours_AppCompButton);
        CWSHours_FridayCloseAppCompButton = findViewById(R.id.CWSHours_FridayClose_AppCompButton);

        CWSHours_SaturdayStartEdittext = findViewById(R.id.CWSHours_SaturdayStart_Edittext);
        CWSHours_SaturdayEndEdittext = findViewById(R.id.CWSHours_SaturdayEnd_Edittext);
        CWSHours_Saturday24hoursAppCompButton = findViewById(R.id.CWSHours_Saturday24hours_AppCompButton);
        CWSHours_SaturdayCloseAppCompButton = findViewById(R.id.CWSHours_SaturdayClose_AppCompButton);

        CWSHours_SundayStartEdittext = findViewById(R.id.CWSHours_SundayStart_Edittext);
        CWSHours_SundayEndEdittext = findViewById(R.id.CWSHours_SundayEnd_Edittext);
        CWSHours_Sunday24hoursAppCompButton = findViewById(R.id.CWSHours_Sunday24hours_AppCompButton);
        CWSHours_SundayCloseAppCompButton = findViewById(R.id.CWSHours_SundayClose_AppCompButton);

        CWSHours_MondayStartEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(CWSHours_MondayStartEdittext);
            }
        });

        CWSHours_MondayEndEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(CWSHours_MondayEndEdittext);
            }
        });

        CWSHours_Monday24hoursAppCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set opening hours to 24 hours
                CWSHours_MondayStartEdittext.setText("00:00 AM");
                CWSHours_MondayEndEdittext.setText("12:00 AM");
            }
        });

        CWSHours_MondayCloseAppCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CWSHours_MondayStartEdittext.setText("Closed");
                CWSHours_MondayEndEdittext.setText("Closed");
                CWSHours_MondayStartEdittext.setPaintFlags(CWSHours_MondayStartEdittext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                CWSHours_MondayEndEdittext.setPaintFlags(CWSHours_MondayEndEdittext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                isMondayClosed = true;
            }
        });

        CWSHours_TuesdayStartEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(CWSHours_TuesdayStartEdittext);
            }
        });

        CWSHours_TuesdayEndEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(CWSHours_TuesdayEndEdittext);
            }
        });

        CWSHours_Tuesday24hoursAppCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set opening hours to 24 hours
                CWSHours_TuesdayStartEdittext.setText("00:00 AM");
                CWSHours_TuesdayEndEdittext.setText("12:00 AM");
            }
        });

        CWSHours_TuesdayCloseAppCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CWSHours_TuesdayStartEdittext.setText("Closed");
                CWSHours_TuesdayEndEdittext.setText("Closed");
                CWSHours_TuesdayStartEdittext.setPaintFlags(CWSHours_TuesdayStartEdittext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                CWSHours_TuesdayEndEdittext.setPaintFlags(CWSHours_TuesdayEndEdittext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                isTuesdayClosed = true;
            }
        });

        CWSHours_WednesdayStartEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(CWSHours_WednesdayStartEdittext);
            }
        });

        CWSHours_WednesdayEndEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(CWSHours_WednesdayEndEdittext);
            }
        });

        CWSHours_Wednesday24hoursAppCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set opening hours to 24 hours
                CWSHours_WednesdayStartEdittext.setText("00:00 AM");
                CWSHours_WednesdayEndEdittext.setText("12:00 AM");
            }
        });

        CWSHours_WednesdayCloseAppCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CWSHours_WednesdayStartEdittext.setText("Closed");
                CWSHours_WednesdayEndEdittext.setText("Closed");
                CWSHours_WednesdayStartEdittext.setPaintFlags(CWSHours_WednesdayStartEdittext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                CWSHours_WednesdayEndEdittext.setPaintFlags(CWSHours_WednesdayEndEdittext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                isWednesdayClosed = true;
            }
        });

        CWSHours_ThursdayStartEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(CWSHours_ThursdayStartEdittext);
            }
        });

        CWSHours_ThursdayEndEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(CWSHours_ThursdayEndEdittext);
            }
        });

        CWSHours_Thursday24hoursAppCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set opening hours to 24 hours
                CWSHours_ThursdayStartEdittext.setText("00:00 AM");
                CWSHours_ThursdayEndEdittext.setText("12:00 AM");
            }
        });

        CWSHours_ThursdayCloseAppCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CWSHours_ThursdayStartEdittext.setText("Closed");
                CWSHours_ThursdayEndEdittext.setText("Closed");
                CWSHours_ThursdayStartEdittext.setPaintFlags(CWSHours_ThursdayStartEdittext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                CWSHours_ThursdayEndEdittext.setPaintFlags(CWSHours_ThursdayEndEdittext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                isThursdayClosed = true;
            }
        });

        CWSHours_FridayStartEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(CWSHours_FridayStartEdittext);
            }
        });

        CWSHours_FridayEndEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(CWSHours_FridayEndEdittext);
            }
        });

        CWSHours_Friday24hoursAppCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set opening hours to 24 hours
                CWSHours_FridayStartEdittext.setText("00:00 AM");
                CWSHours_FridayEndEdittext.setText("12:00 AM");
            }
        });

        CWSHours_FridayCloseAppCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CWSHours_FridayStartEdittext.setText("Closed");
                CWSHours_FridayEndEdittext.setText("Closed");
                CWSHours_FridayStartEdittext.setPaintFlags(CWSHours_FridayStartEdittext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                CWSHours_FridayEndEdittext.setPaintFlags(CWSHours_FridayEndEdittext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                isFridayClosed = true;
            }
        });

        CWSHours_SaturdayStartEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(CWSHours_SaturdayStartEdittext);
            }
        });

        CWSHours_SaturdayEndEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(CWSHours_SaturdayEndEdittext);
            }
        });

        CWSHours_Saturday24hoursAppCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set opening hours to 24 hours
                CWSHours_SaturdayStartEdittext.setText("00:00 AM");
                CWSHours_SaturdayEndEdittext.setText("12:00 AM");
            }
        });

        CWSHours_SaturdayCloseAppCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CWSHours_SaturdayStartEdittext.setText("Closed");
                CWSHours_SaturdayEndEdittext.setText("Closed");
                CWSHours_SaturdayStartEdittext.setPaintFlags(CWSHours_SaturdayStartEdittext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                CWSHours_SaturdayEndEdittext.setPaintFlags(CWSHours_SaturdayEndEdittext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                isSaturdayClosed = true;
            }
        });

        CWSHours_SundayStartEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(CWSHours_SundayStartEdittext);
            }
        });

        CWSHours_SundayEndEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(CWSHours_SundayEndEdittext);
            }
        });

        CWSHours_Sunday24hoursAppCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set opening hours to 24 hours
                CWSHours_SundayStartEdittext.setText("00:00 AM");
                CWSHours_SundayEndEdittext.setText("12:00 AM");
            }
        });

        CWSHours_SundayCloseAppCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CWSHours_SundayStartEdittext.setText("Closed");
                CWSHours_SundayEndEdittext.setText("Closed");
                CWSHours_SundayStartEdittext.setPaintFlags(CWSHours_SundayStartEdittext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                CWSHours_SundayEndEdittext.setPaintFlags(CWSHours_SundayEndEdittext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                isSundayClosed = true;
            }
        });
    }

    public void displayMap(){
        final Dialog mapDialog = new Dialog(RegisterOwner_SpaceBranch.this);
        mapDialog.setContentView(R.layout.enterlocation_googlemap_popup);

        mapView = mapDialog.findViewById(R.id.map_view);
        mapView.onCreate(mapDialog.onSaveInstanceState());
        mapView.onResume();
        try {
            MapsInitializer.initialize(RegisterOwner_SpaceBranch.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get the GoogleMap object from the MapView and set the click listener for the map
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // Check if location permission is granted
                if (ActivityCompat.checkSelfPermission(RegisterOwner_SpaceBranch.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Enable location layer on the map
                    googleMap.setMyLocationEnabled(true);

                    // Get the user's last known location
                    FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(RegisterOwner_SpaceBranch.this);
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Create LatLng object with the user's location
                                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                                // Move the camera to the user's location
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                            }
                        }
                    });
                } else {
                    // Request location permission if it is not granted
                    ActivityCompat.requestPermissions(RegisterOwner_SpaceBranch.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // Set the latitude and longitude TextViews with the selected location
                        latitudeTextview.setText(String.valueOf(latLng.latitude));
                        longitudeTextview.setText(String.valueOf(latLng.longitude));

                        location = new GeoPoint(latLng.latitude, latLng.longitude);

                        // Clear the existing marker
                        if (googleMap != null) {
                            googleMap.clear();
                        }

                        // Add a marker to the selected location and set it draggable
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).draggable(true);
                        googleMap.addMarker(markerOptions);

                        // Set a listener for marker drag events to update the location TextViews
                        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                            @Override
                            public void onMarkerDragStart(@NonNull Marker marker) {

                            }

                            @Override
                            public void onMarkerDrag(@NonNull Marker marker) {

                            }

                            @Override
                            public void onMarkerDragEnd(@NonNull Marker marker) {
                                LatLng position = marker.getPosition();
                                latitudeTextview.setText(String.valueOf(position.latitude));
                                longitudeTextview.setText(String.valueOf(position.longitude));

                            }
                        });
                        Toast.makeText(RegisterOwner_SpaceBranch.this, "If done, please click close button above.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Get the Close button and set the click listener to close the dialog
        Button closeButton = mapDialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapDialog.dismiss();
            }
        });

        // Show the dialog
        mapDialog.show();


    }

    private void showTimePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(RegisterOwner_SpaceBranch.this, new TimePickerDialog.OnTimeSetListener() {
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
                Toast.makeText(RegisterOwner_SpaceBranch.this, selectedDate, Toast.LENGTH_SHORT).show();
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            filepath = data.getData();
            branch_image.setImageURI(filepath);
        }
    }

    public void ownerUserActivity(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String ownerId= firebaseAuth.getCurrentUser().getUid();
        String activity = "Registered" + branchName;

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
    private void registerBranch(String namebranch, String streetAddressBranch, String cityAddressBranch, String selectedCategory){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(filepath == null || namebranch.isEmpty() || streetAddressBranch.isEmpty() || cityAddressBranch.isEmpty()) {
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
                            } else {
                                progressDialog.setMessage("Registering branch...");
                                progressDialog.show();
                                StorageReference path = firebaseStorage.getReference().child("BranchImages").child(filepath.getLastPathSegment());
                                path.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {

                                                Map<String, BranchModel.OpeningHours> hours = new HashMap<>();
                                                hours.put("Monday", new BranchModel.OpeningHours(isMondayClosed, CWSHours_MondayStartEdittext.getText().toString(), CWSHours_MondayEndEdittext.getText().toString()));
                                                hours.put("Tuesday", new BranchModel.OpeningHours(isTuesdayClosed, CWSHours_TuesdayStartEdittext.getText().toString(), CWSHours_TuesdayEndEdittext.getText().toString()));
                                                hours.put("Wednesday", new BranchModel.OpeningHours(isWednesdayClosed, CWSHours_WednesdayStartEdittext.getText().toString(), CWSHours_WednesdayEndEdittext.getText().toString()));
                                                hours.put("Thursday", new BranchModel.OpeningHours(isThursdayClosed, CWSHours_ThursdayStartEdittext.getText().toString(), CWSHours_ThursdayEndEdittext.getText().toString()));
                                                hours.put("Friday", new BranchModel.OpeningHours(isFridayClosed, CWSHours_FridayStartEdittext.getText().toString(), CWSHours_FridayEndEdittext.getText().toString()));
                                                hours.put("Saturday", new BranchModel.OpeningHours(isSaturdayClosed, CWSHours_SaturdayStartEdittext.getText().toString(), CWSHours_SaturdayEndEdittext.getText().toString()));
                                                hours.put("Sunday", new BranchModel.OpeningHours(isSundayClosed, CWSHours_SundayEndEdittext.getText().toString(), CWSHours_SundayEndEdittext.getText().toString()));

                                                Map<String, Object> branch = new HashMap<>();
                                                branch.put("cospaceImage", task.getResult().toString());
                                                branch.put("cospaceName", namebranch);
                                                branch.put("cospaceCategory", selectedCategory);
                                                branch.put("cospaceStreetAddress", streetAddressBranch);
                                                branch.put("cospaceCityAddress", cityAddressBranch);
                                                branch.put("location", location);
                                                branch.put("owner_id", user.getUid());
                                                branch.put("cospaceId", "");
                                                branch.put("hours", hours);

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
                                                                                ownerUserActivity();
                                                                            }
                                                                        });
                                                                progressDialog.dismiss();
                                                                startActivity(new Intent(RegisterOwner_SpaceBranch.this, OwnerHomepage.class));
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
