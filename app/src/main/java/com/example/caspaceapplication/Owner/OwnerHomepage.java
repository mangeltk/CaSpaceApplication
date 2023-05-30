package com.example.caspaceapplication.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.caspaceapplication.Owner.AmenitiesOffered.Owner_AmenitiesOffered;
import com.example.caspaceapplication.Owner.BookingTransactions.Owner_BookingTransactions;
import com.example.caspaceapplication.Owner.OfficeLayouts.Owner_OfficeLayouts;
import com.example.caspaceapplication.Owner.ProDisc.Owner_ProDisc;
import com.example.caspaceapplication.Owner.Profile.Owner_Profile;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.messaging.MsgMain;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class OwnerHomepage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView navigationView;

    ConstraintLayout FourIconsHome;
    LinearLayout OwnerHomepageUnverifiedNotice;
    ImageView OwnerHomepageUploadedPermitImageview;
    String ownerBranchStatus, branchStatusFromFirestore;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String currentUserID = firebaseAuth.getCurrentUser().getUid();
    CollectionReference bookingRef = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_homepage);

        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ImageView officeLayouts = findViewById(R.id.officeLayout_Imageview);
        ImageView promotionsAndDiscounts = findViewById(R.id.proAndDisc_Imageview);
        ImageView bookingTransactions = findViewById(R.id.bookingTransactions_Imageview);
        ImageView AmenitiesOffered = findViewById(R.id.AmenitiesOffered_Imageview);
        TextView username = findViewById(R.id.textUsername);
        FourIconsHome = findViewById(R.id.OwnerHomepageIcons);
        OwnerHomepageUnverifiedNotice = findViewById(R.id.OwnerHomepageUnverifiedNotice);
        OwnerHomepageUploadedPermitImageview = findViewById(R.id.OwnerHomepageUploadedPermit_Imageview);

        //BRANCH STATUS
        Intent intent = getIntent();
        ownerBranchStatus = intent.getStringExtra("ownerBranchStatus");

        if (ownerBranchStatus.equals("Unverified")){
            FourIconsHome.setVisibility(View.GONE);
            OwnerHomepageUnverifiedNotice.setVisibility(View.VISIBLE);
        }else{
            FourIconsHome.setVisibility(View.VISIBLE);
            OwnerHomepageUnverifiedNotice.setVisibility(View.GONE);
        }
        /*if (ownerBranchStatus != null && ownerBranchStatus.equals("Unverified")) {
            FourIconsHome.setVisibility(View.GONE);
            OwnerHomepageUnverifiedNotice.setVisibility(View.VISIBLE);
        } else {
            FourIconsHome.setVisibility(View.VISIBLE);
            OwnerHomepageUnverifiedNotice.setVisibility(View.GONE);
        }*/


        firebaseFirestore.collection("OwnerUserAccounts")
                .document(user)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("ownerUsername");
                            String permitImage = documentSnapshot.getString("ownerBusinessPermit");
                            branchStatusFromFirestore = documentSnapshot.getString("ownerBranchStatus");

                            username.setText(name);

                            if (branchStatusFromFirestore.equals("Unverified")){
                                if (permitImage!=null){
                                    Picasso.get().load(permitImage).into(OwnerHomepageUploadedPermitImageview);
                                }
                            }

                        }
                    }
                });

        officeLayouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerHomepage.this, Owner_OfficeLayouts.class));
            }
        });

        promotionsAndDiscounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerHomepage.this, Owner_ProDisc.class));
            }
        });

        AmenitiesOffered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerHomepage.this, Owner_AmenitiesOffered.class));
            }
        });

        bookingTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerHomepage.this, Owner_BookingTransactions.class));
            }
        });

        checkAndUpdateStatusToDeclined();
        checkAndUpdateStatusToOngoing();
        checkAndUpdateStatusAcceptedToCompleted();
        checkAndUpdateStatusOngoingToCompleted();

        // Initialize the bottom navigation bar
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(this);

    }


    // if booking status is still pending after 24 hours it means, the booking submitted wasn't able to be accepted by owner, then automated declined,
    private void checkAndUpdateStatusToDeclined() {
        bookingRef.whereEqualTo("bookingStatus", "Pending")
                .whereEqualTo("ownerId", currentUserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            Timestamp submittedDate = documentSnapshot.getTimestamp("bookSubmittedDate");
                            String bookingId = documentSnapshot.getId();

                            if (submittedDate != null) {
                                Calendar currentTime = Calendar.getInstance();
                                Calendar submittedTime = Calendar.getInstance();
                                submittedTime.setTime(submittedDate.toDate());
                                submittedTime.add(Calendar.DAY_OF_MONTH, 1); // Add 24 hours to submitted time

                                if (currentTime.after(submittedTime)) {
                                    // Update Firestore status to "Declined"
                                    DocumentReference bookingRef2 = bookingRef.document(bookingId);
                                    bookingRef2.update("bookingStatus", "Declined")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Status updated successfully
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update status
                                                }
                                            });
                                }
                            }
                        }
                    }
                });
    }

    private void checkAndUpdateStatusToOngoing() {
        bookingRef.whereEqualTo("bookingStatus", "Accepted")
                .whereEqualTo("ownerId", currentUserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            Timestamp startTimeSelected = documentSnapshot.getTimestamp("BookStartTimeSelected");
                            Timestamp endTimeSelected = documentSnapshot.getTimestamp("BookEndTimeSelected");
                            String bookingId = documentSnapshot.getId();

                            if (startTimeSelected != null && endTimeSelected != null) {
                                Calendar currentTime = Calendar.getInstance();
                                Calendar startTime = Calendar.getInstance();
                                startTime.setTime(startTimeSelected.toDate());
                                Calendar endTime = Calendar.getInstance();
                                endTime.setTime(endTimeSelected.toDate());

                                if (currentTime.getTimeInMillis() >= startTime.getTimeInMillis() &&
                                        currentTime.getTimeInMillis() <= endTime.getTimeInMillis()) {
                                    DocumentReference bookingRef2 = bookingRef.document(bookingId);
                                    bookingRef2.update("bookingStatus", "Ongoing")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Status updated successfully
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update status
                                                }
                                            });
                                }
                            }
                        }
                    }
                });
    }

    // if booking status is on Accepted and is it passed the end date and time, change status to completed
    private void checkAndUpdateStatusAcceptedToCompleted() {
        bookingRef.whereEqualTo("bookingStatus", "Accepted")
                .whereEqualTo("ownerId", currentUserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            Timestamp endTimeSelected = documentSnapshot.getTimestamp("BookEndTimeSelected");
                            String bookingId = documentSnapshot.getId();

                            if (endTimeSelected != null) {
                                Calendar currentTime = Calendar.getInstance();
                                Calendar endTime = Calendar.getInstance();
                                endTime.setTime(endTimeSelected.toDate());

                                if (currentTime.getTimeInMillis() >= endTime.getTimeInMillis()) {
                                    DocumentReference bookingRef2 = bookingRef.document(bookingId);
                                    bookingRef2.update("bookingStatus", "Completed")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Status updated successfully
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update status
                                                }
                                            });
                                }
                            }
                        }
                    }
                });
    }

    private void checkAndUpdateStatusOngoingToCompleted(){
        bookingRef.whereEqualTo("bookingStatus", "Ongoing")
                .whereEqualTo("ownerId", currentUserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            Timestamp endTimeSelected = documentSnapshot.getTimestamp("BookEndTimeSelected");
                            String bookingId = documentSnapshot.getId();

                            if (endTimeSelected != null) {
                                Calendar currentTime = Calendar.getInstance();
                                Calendar endTime = Calendar.getInstance();
                                endTime.setTime(endTimeSelected.toDate());

                                if (currentTime.getTimeInMillis() >= endTime.getTimeInMillis()) {
                                    DocumentReference bookingRef2 = bookingRef.document(bookingId);
                                    bookingRef2.update("bookingStatus", "Completed")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Status updated successfully
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update status
                                                }
                                            });
                                }
                            }
                        }
                    }
                });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHome:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuMessages:
                startActivity(new Intent(this, MsgMain.class));
                return true;
            case R.id.menuNotification:
                 startActivity(new Intent(this, OwnerNotification.class));
                return true;
            case R.id.menuProfile:
                 if (!branchStatusFromFirestore.equals("Unverified")){
                     startActivity(new Intent(this, Owner_Profile.class));
                 }
                 else{
                     Intent intent = new Intent(this, Owner_Profile.class);
                     intent.putExtra("targetFragment", "Owner_UserProfile");
                     startActivity(intent);
                 }
                return true;
            default:
                return false;
        }

    }

}