package com.example.caspaceapplication.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.BookingTransactionManagement.BookingsFragment;
import com.example.caspaceapplication.fragments.CustomerProfileFragment;
import com.example.caspaceapplication.fragments.HomeFragment;
import com.example.caspaceapplication.fragments.NotificationFragment;
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

import java.util.Calendar;

public class Customer_Homepage_BottomNav extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String currentUserID = firebaseAuth.getCurrentUser().getUid();
    CollectionReference bookingRef = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_homepage_bottom_nav);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

        checkAndUpdateStatusToDeclined();
        checkAndUpdateStatusToOngoing();
        checkAndUpdateStatusAcceptedToCompleted();
        checkAndUpdateStatusOngoingToCompleted();
    }

    // if booking status is still pending after 24 hours it means, the booking submitted wasn't able to be accepted by owner, then automated declined,
    private void checkAndUpdateStatusToDeclined() {
        bookingRef.whereEqualTo("bookingStatus", "Pending")
                .whereEqualTo("customerId", currentUserID)
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

    // if booking status is on Accepted and is on between the start and end date and time, change status to ongoing
    private void checkAndUpdateStatusToOngoing() {
        bookingRef.whereEqualTo("bookingStatus", "Accepted")
                .whereEqualTo("customerId", currentUserID)
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
                .whereEqualTo("customerId", currentUserID)
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

    // if booking status is on Ongoing and is it passed the end date and time, change status to completed
    private void checkAndUpdateStatusOngoingToCompleted(){
        bookingRef.whereEqualTo("bookingStatus", "Ongoing")
                .whereEqualTo("customerId", currentUserID)
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

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuHome:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    return true;
                case R.id.menuBookings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BookingsFragment()).commit();
                    return true;
                case R.id.menuMessages:
                    Intent intent = new Intent(getApplicationContext(), MsgMain.class);
                    startActivity(intent);
                    return true;
                case R.id.menuNotification:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationFragment()).commit();
                    return true;
                case R.id.menuProfile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CustomerProfileFragment()).commit();
                    return true;
                default:
                    return false;
            }
        }
    };

}