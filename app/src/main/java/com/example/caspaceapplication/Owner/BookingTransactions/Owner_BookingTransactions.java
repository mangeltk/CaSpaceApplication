package com.example.caspaceapplication.Owner.BookingTransactions;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.caspaceapplication.Owner.OwnerHomepage;
import com.example.caspaceapplication.Owner.Profile.Owner_Profile;
import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class Owner_BookingTransactions extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView navigationView;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String currentUserID = firebaseAuth.getCurrentUser().getUid();
    CollectionReference bookingRef = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_booking_transactions2);

        tabLayout = findViewById(R.id.tabLayoutBTO);
        viewPager = findViewById(R.id.viewPagerBTO);
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.addFragment(new OwnerBT_PendingTabFragment(), "Pending", 0);
        viewPagerAdapter.addFragment(new OwnerBT_OngoingTabFragment(), "Accepted & Ongoing", 0);
        viewPagerAdapter.addFragment(new OwnerBT_CancelledTabFragment(), "Declined & Cancelled", 0);
        viewPagerAdapter.addFragment(new OwnerBT_CompletedTabFragment(), "Completed", 0);
        viewPager.setAdapter(viewPagerAdapter);

        checkAndUpdateStatusToDeclined();
        checkAndUpdateStatusToOngoing();
        checkAndUpdateStatusAcceptedToCompleted();
        checkAndUpdateStatusOngoingToCompleted();

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

    public void updateBadgeCount(int position, int count) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        if (tab != null) {
            CharSequence title = tab.getText();
            if (count > 0) {
                String newTitle = title.toString().replaceAll("\\(\\d+\\)", "") + "(" + count + ")";
                tab.setText(newTitle);
            } else {
                String newTitle = title.toString().replaceAll("\\(\\d+\\)", "");
                tab.setText(newTitle);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHome:
                startActivity(new Intent(Owner_BookingTransactions.this, OwnerHomepage.class));
                Toast.makeText(Owner_BookingTransactions.this, "Home", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuMessages:
                // startActivity(new Intent(this, MessageActivity.class));
                return true;
            case R.id.menuNotification:
                // startActivity(new Intent(this, NotificationActivity.class));
                return true;
            case R.id.menuProfile:
                startActivity(new Intent(Owner_BookingTransactions.this, Owner_Profile.class));
                return true;
            default:
                return false;
        }
    }

}