package com.example.caspaceapplication.customer.FeedbackRatingsManagement;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.ModelClasses.BookingDetails_ModelClass;
import com.example.caspaceapplication.ModelClasses.Feedbacks_ModelClass;
import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CWS_FeedbackRatings extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference CospaceBranchesColref = firebaseFirestore.collection("CospaceBranches");
    CollectionReference SubmittedBookingTransactionsColref = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");
    CollectionReference CustomerSubmittedFeedbacksColref = firebaseFirestore.collection("CustomerSubmittedFeedbacks");
    CollectionReference CustomerDetailsColref = firebaseFirestore.collection("CustomerUserAccounts");

    String currentCustomerUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String ownerId;
    String branchName;
    String customerImageUri, customerCompleteName;

    ImageView BranchImageImageview;
    TextView BranchNameTextview;
    AppCompatButton clickAddRatingAppCompatButton;
    RecyclerView feedbacksRecyclerview;
    List<Feedbacks_ModelClass> feedbacksModelClassList;
    List<BookingDetails_ModelClass> bookingDetailsModelClassList;
    FeedbacksAdapter feedbacksAdapter;

    RatingBar ratingBarDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cws_feedback_ratings);

        //intent from card view clicked in manually search
        Intent intent = getIntent();
        ownerId = intent.getStringExtra("ownerId");
        branchName = intent.getStringExtra("branchName");

        BranchImageImageview = findViewById(R.id.FeedRate_BranchImage_Imageview);
        BranchNameTextview = findViewById(R.id.FeedRate_BranchName_Textview);
        clickAddRatingAppCompatButton = findViewById(R.id.clickAddRating_AppCompatButton);

        getCustomerDetails();
        displayBranchInfo();
        checkIfCustomerAlreadyBooked();
        addFeedbackRatings();

        feedbacksRecyclerview = findViewById(R.id.FeedRate_Feedbacks_Recyclerview);
        feedbacksRecyclerview.setHasFixedSize(true);
        feedbacksRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        feedbacksModelClassList = new ArrayList<>();
        feedbacksAdapter = new FeedbacksAdapter(feedbacksModelClassList);
        feedbacksRecyclerview.setAdapter(feedbacksAdapter);

        ratingBarDisplay = findViewById(R.id.ratingBar);


        getTotalRating();
        displayFeedbacks();
    }

    public void getTotalRating(){
        CustomerSubmittedFeedbacksColref.whereEqualTo("cospaceName", branchName)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        long totalCount = 0;
                        int count = 0;

                        for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){
                            Long value = documentSnapshot.getLong("ratingValue");
                            if (value != null) {
                                totalCount += value;
                                count++;
                            }
                        }

                        if (count > 0) {
                            int average = (int) totalCount / count;
                            ratingBarDisplay.setRating(average);
                            Toast.makeText(CWS_FeedbackRatings.this, String.valueOf(average), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CWS_FeedbackRatings.this, "No ratings found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void getCustomerDetails(){
        CustomerDetailsColref.whereEqualTo("customersIDNum", currentCustomerUser)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                customerImageUri = documentSnapshot.getString("customerImage");
                                String custFirstName = documentSnapshot.getString("customersFirstName");
                                String custlastName = documentSnapshot.getString("customersLastName");
                                customerCompleteName = custFirstName + " " + custlastName;
                            }
                        }
                    }
                });
    }

    public void displayBranchInfo(){
        BranchNameTextview.setText(branchName);
        CospaceBranchesColref.whereEqualTo("cospaceName", branchName)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                            String branchImage = documentSnapshot.getString("cospaceImage");
                            if (branchImage!=null && !branchImage.isEmpty()){
                                Picasso.get().load(branchImage).into(BranchImageImageview);
                            }
                        }
                    }
                });
    }

    public void checkIfCustomerAlreadyBooked(){
        clickAddRatingAppCompatButton.setVisibility(View.GONE);
        //should only be visible if customer has already booked in this coworking space branch that is status completed
        SubmittedBookingTransactionsColref.whereEqualTo("customerId", currentCustomerUser)
                .whereEqualTo("branchName", branchName).whereEqualTo("bookingStatus", "Completed")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            clickAddRatingAppCompatButton.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    public void addFeedbackRatings(){
        clickAddRatingAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CWS_FeedbackRatings.this);
                Dialog dialog;
                final View addFeedbackPopup = getLayoutInflater().inflate(R.layout.recycleritem_popup_addfeedback, null);
                ImageButton closeButton = (ImageButton) addFeedbackPopup.findViewById(R.id.feedbackCloseButton_ImageButton);
                EditText customerFeedbackMessage = (EditText) addFeedbackPopup.findViewById(R.id.CustomerFeedbackMessage_Edittext);
                RatingBar customerRating = (RatingBar) addFeedbackPopup.findViewById(R.id.CustomerRating_RatingBar);
                AppCompatButton submitFeedbackButton = (AppCompatButton) addFeedbackPopup.findViewById(R.id.SubmitFeedback_AppCompatButton);
                ShapeableImageView customerProfileImage = (ShapeableImageView) addFeedbackPopup.findViewById(R.id.customerProfileImage_ShapeableImageView);

                if (customerImageUri!=null && !customerImageUri.isEmpty()){
                    Picasso.get().load(customerImageUri).into(customerProfileImage);
                }

                dialogBuilder.setView(addFeedbackPopup);
                dialog = dialogBuilder.create();
                dialog.show();

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(CWS_FeedbackRatings.this, "Close", Toast.LENGTH_SHORT).show();
                    }
                });

                submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String feedbackMessage = customerFeedbackMessage.getText().toString();
                        float ratingValue = customerRating.getRating();

                        if (feedbackMessage.isEmpty() && ratingValue == 0.0) {
                            Toast.makeText(CWS_FeedbackRatings.this, "Please provide either a rating and feedback message", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Map<String, Object> feedback = new HashMap<>();
                        feedback.put("ratingValue", ratingValue);
                        feedback.put("feedbackMessage", feedbackMessage);
                        feedback.put("customerId", currentCustomerUser);
                        feedback.put("cospaceName", branchName);
                        feedback.put("customerImage", customerImageUri);
                        feedback.put("feedbackId", "");
                        feedback.put("timestamp", Timestamp.now());

                        CustomerSubmittedFeedbacksColref.add(feedback).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                CustomerSubmittedFeedbacksColref.document(documentReference.getId())
                                        .update("feedbackId", documentReference.getId())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(CWS_FeedbackRatings.this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                dialog.dismiss();
                            }
                        });
                    }
                });

            }
        });
    }

    public void displayFeedbacks(){
        CustomerSubmittedFeedbacksColref.whereEqualTo("cospaceName", branchName)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                       for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                           Log.d(TAG, "Document data: " + documentSnapshot.getData());
                           Feedbacks_ModelClass modelClass = documentSnapshot.toObject(Feedbacks_ModelClass.class);
                            feedbacksModelClassList.add(modelClass);
                       }
                       feedbacksAdapter.notifyDataSetChanged();
                    }
                });
    }

    public class FeedbacksAdapter extends RecyclerView.Adapter<FeedbacksAdapter.ViewHolder>{

        private List<Feedbacks_ModelClass> feedbacksModelClass;

        public FeedbacksAdapter(List<Feedbacks_ModelClass> feedbacksModelClass) {
            this.feedbacksModelClass = feedbacksModelClass;
        }

        @NonNull
        @Override
        public FeedbacksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem_popup_addfeedback , parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FeedbacksAdapter.ViewHolder holder, int position) {
            holder.PopupFeedbackTitleTextview.setText("Feedback Details");
            String message = feedbacksModelClass.get(position).getFeedbackMessage();
            if (!message.isEmpty() && message!=null){
                holder.feedbackMessage.setText(message);
            }else{
                holder.feedbackMessage.setText("Customer did not provide any message.");
                holder.feedbackMessage.setTextSize(15);
                holder.feedbackMessage.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.dialog_color1));
                holder.feedbackMessage.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.color.white));

            }
            holder.feedbackMessage.setEnabled(false);
            String imageUri = feedbacksModelClass.get(position).getCustomerImage();
            if (imageUri!=null && !imageUri.isEmpty()){
                Picasso.get().load(imageUri).into(holder.custProfileImage);
            }
            holder.ratingBar.setRating(feedbacksModelClass.get(position).getRatingValue());
            holder.ratingBar.setIsIndicator(true);
            holder.submitButton.setVisibility(View.GONE);
            holder.feedbackCloseButton.setVisibility(View.GONE);
            holder.feedbackMessageTitle.setText("Customer feedback message:");
            holder.ratingMessageTitle.setText("Customer rating:");
        }

        @Override
        public int getItemCount() {
            return feedbacksModelClass.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView PopupFeedbackTitleTextview, feedbackMessageTitle, ratingMessageTitle;
            EditText feedbackMessage;
            ShapeableImageView custProfileImage;
            AppCompatButton submitButton;
            ImageButton feedbackCloseButton;
            RatingBar ratingBar;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                PopupFeedbackTitleTextview = itemView.findViewById(R.id.PopupFeedbackTitle_Textview);
                feedbackMessageTitle = itemView.findViewById(R.id.feedbackMessageTitle_Textview);
                ratingMessageTitle = itemView.findViewById(R.id.ratingMessageTitle_Textview);
                feedbackMessage = itemView.findViewById(R.id.CustomerFeedbackMessage_Edittext);
                ratingBar = itemView.findViewById(R.id.CustomerRating_RatingBar);
                custProfileImage = itemView.findViewById(R.id.customerProfileImage_ShapeableImageView);
                submitButton = itemView.findViewById(R.id.SubmitFeedback_AppCompatButton);
                feedbackCloseButton = itemView.findViewById(R.id.feedbackCloseButton_ImageButton);
            }
        }
    }
}