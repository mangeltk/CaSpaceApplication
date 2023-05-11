package com.example.caspaceapplication.Owner;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caspaceapplication.Owner.Profile.Owner_Profile;
import com.example.caspaceapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class OwnerNotification extends AppCompatActivity {

    private LinearLayout notificationLinearLayout;
    private Typeface typeface;
    private GradientDrawable gradientDrawable;
    private LinearLayout.LayoutParams layoutParams;
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_notification);
        notificationLinearLayout = findViewById(R.id.ownerNotificationLinearLayout);
        typeface = ResourcesCompat.getFont(this, R.font.quicksand_medium);
        gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(2, Color.parseColor("#000000"));
        gradientDrawable.setCornerRadius(16);
        gradientDrawable.setColor(Color.parseColor("#F6EBD9"));
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(16, 16, 16, 16);
        retrieveNotifications();
    }

    private void retrieveNotifications() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("OwnerNotificationStorage")
                .whereEqualTo("ownerId",currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String title = documentSnapshot.getString("title");
                        String message = documentSnapshot.getString("message");
                        TextView notificationTextView = new TextView(this);
                        notificationTextView.setText(title + ": " + message);
                        notificationTextView.setTextSize(18);
                        notificationTextView.setTextColor(Color.parseColor("#000000"));
                        notificationTextView.setPadding(16, 16, 16, 16);
                        notificationTextView.setTypeface(typeface);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            notificationTextView.setBackground(gradientDrawable);
                        } else {
                            notificationTextView.setBackgroundDrawable(gradientDrawable);
                        }
                        notificationTextView.setLayoutParams(layoutParams);
                        notificationLinearLayout.addView(notificationTextView);
                        // Set click listener for the notification textview
                        notificationTextView.setOnClickListener(v -> {
                            // Show confirmation dialog before deleting the notification
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("Do you want to delete this notification?")
                                    .setCancelable(true)
                                    .setPositiveButton("Delete", (dialog, id) -> {
                                        // Delete the notification from the database
                                        String notificationId = documentSnapshot.getId();
                                        db.collection("OwnerNotificationStorage").document(notificationId).delete()
                                                .addOnSuccessListener(aVoid -> {
                                                    Log.d(TAG, "Notification successfully deleted from Firestore.");
                                                    // Remove the textview from the layout
                                                    notificationLinearLayout.removeView(notificationTextView);
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.w(TAG, "Error deleting notification from Firestore", e);
                                                });
                                    })
                                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
                            AlertDialog alert = builder.create();
                            alert.show();
                        });
                        Log.d(TAG, "Current user ID: " + currentUserId);
                        Log.d(TAG, "Number of notifications retrieved: " + queryDocumentSnapshots.size());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting notifications", e);
                });
    }


}