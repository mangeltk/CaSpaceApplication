package com.example.caspaceapplication.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.ktx.Firebase;

public class NotificationFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    public NotificationFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private static final String TAG = "NotificationsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        LinearLayout notificationLinearLayout = view.findViewById(R.id.notificationLinearLayout);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.quicksand_medium);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(2, Color.parseColor("#000000")); // sets the border color to red
        gradientDrawable.setCornerRadius(16);
        gradientDrawable.setColor(Color.parseColor("#F6EBD9"));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(16, 16, 16, 16);
        String currentUserId = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CustomerNotificationStorage")
                .whereEqualTo("customerId",currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String title = documentSnapshot.getString("title");
                        String message = documentSnapshot.getString("message");
                        TextView notificationTextView = new TextView(getContext());
                        notificationTextView.setText(title + ": " + message);
                        notificationTextView.setTextSize(18);
                        notificationTextView.setTextColor(Color.parseColor("#000000"));
                        notificationTextView.setPadding(16, 16, 16, 16);
                        notificationTextView.setTypeface(typeface);
                        //notificationTextView.setBackgroundColor(Color.parseColor("#F6EBD9"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            notificationTextView.setBackground(gradientDrawable);
                        } else {
                            notificationTextView.setBackgroundDrawable(gradientDrawable);
                        }
                        notificationTextView.setLayoutParams(layoutParams);
                        notificationLinearLayout.addView(notificationTextView);
                        notificationTextView.setOnClickListener(v ->{
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Do you want to delete this notification?")
                                        .setCancelable(true)
                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                               FirebaseFirestore db= FirebaseFirestore.getInstance();
                                           CollectionReference notificationRef = db.collection("CustomerNotificationStorage");
                                           notificationRef
                                                   .whereEqualTo("customerId",currentUserId)
                                                        .get()
                                                        .addOnSuccessListener(documentSnapshots -> {
                                                            if (!documentSnapshots.isEmpty()) {
                                                                DocumentSnapshot document = documentSnapshots.getDocuments().get(0);
                                                                notificationRef.document(document.getId())
                                                                        .delete()
                                                                        .addOnSuccessListener(aVoid -> {
                                                                            Log.d(TAG, "Notification successfully deleted from Firestore.");
                                                                        })
                                                                        .addOnFailureListener(e -> {
                                                                            Log.w(TAG, "Error deleting notification from Firestore", e);
                                                                        });
                                                            } else {
                                                                Log.d(TAG, "Notification not found in Firestore.");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error deleting notification from Firestore", e);
                                                            }
                                                        });
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                        });

                        //notificationTextView.setOnLongClickListener(new MyOnLongClickListener(notificationTextView));
                        Log.d(TAG, "Current user ID: " + currentUserId);
                        Log.d(TAG, "Number of notifications retrieved: " + queryDocumentSnapshots.size());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting notifications", e);
                });

        return view;
    }
}