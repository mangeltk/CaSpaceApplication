package com.example.caspaceapplication.Owner.BookingTransactions;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.Notification.FCMSend;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.BookingTransactionManagement.BookingDetails_ModelClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OwnerBT_AdapterClass extends RecyclerView.Adapter<OwnerBT_AdapterClass.BookingViewHolder> {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private List<BookingDetails_ModelClass> bookingList;
    private Context context;

    public OwnerBT_AdapterClass(List<BookingDetails_ModelClass> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public OwnerBT_AdapterClass.BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem_bookingtransaction, parent,false);
        return new BookingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerBT_AdapterClass.BookingViewHolder holder, int position) {
        BookingDetails_ModelClass booking = bookingList.get(position);

        //holder.bookingId.setText(booking.getbook());
        holder.bookingStatus.setText(booking.getBookingStatus());
        String layoutImageUri = booking.getLayoutImage();
        if (layoutImageUri != null && !layoutImageUri.isEmpty()){
            Picasso.get().load(layoutImageUri).into( holder.layoutImage);
        }
        holder.layoutName.setText(booking.getLayoutName());
        holder.layoutRateType.setText(booking.getRateType());
        holder.layoutRatePrice.setText(booking.getRateValue());
        holder.customerFullname.setText(booking.getCustomerFullname());
        holder.bookingStartDate.setText(booking.getBookingStartDate());
        holder.bookingEndDate.setText(booking.getBookingEndDate());
        holder.totalPayment.setText(booking.getTotalPayment());
        holder.paymentOption.setText(booking.getPaymentOption());
        String customerID = booking.getCustomerId();
        String customerName =booking.getCustomerFullname();

        holder.cardViewBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                BookingDetails_ModelClass model = bookingList.get(clickedPosition);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.recycleitem_custbookingcardview_moredetails, null);

                ImageView branchImage, layoutImage, paymentImage;
                TextView branchName, layoutName, bookingStatus, bookingPayment, rateType, ratePrice, paymentOption, tenantsNum, startDate, endDate,
                        startTime, endTime, totalHours, custFullname, orgName, custAddress, custPhoneNum, custEmail;

                AppCompatButton declineButton, acceptButton, completeButton, cancelButton;
                ImageButton exitButton;

                branchImage = dialogView.findViewById(R.id.seemoreBranchImage_Imageview);
                layoutImage = dialogView.findViewById(R.id.seemoreLayoutImage_Imageview);
                paymentImage = dialogView.findViewById(R.id.seemorePaymentPic_Imageview);
                branchName = dialogView.findViewById(R.id.seemoreBranchName_Textview);
                layoutName = dialogView.findViewById(R.id.seemoreLayoutName_Textview);
                bookingStatus = dialogView.findViewById(R.id.seemoreBookingStatus_Textview);
                bookingPayment = dialogView.findViewById(R.id.seemoreTotalPayment_Textview);
                rateType = dialogView.findViewById(R.id.seemoreRateType_Textview);
                ratePrice = dialogView.findViewById(R.id.seemoreRatePrice_Textview);
                paymentOption = dialogView.findViewById(R.id.seemorePaymentOption_Textview);
                tenantsNum = dialogView.findViewById(R.id.seemoreTenantsNum_Textview);
                startDate = dialogView.findViewById(R.id.seemoreStartDate_Textview);
                endDate = dialogView.findViewById(R.id.seemoreEndDate_Textview);
                startTime = dialogView.findViewById(R.id.seemoreStartTime_Textview);
                endTime = dialogView.findViewById(R.id.seemoreEndTime_Textview);
                totalHours = dialogView.findViewById(R.id.seemoreTotalHours_Textview);
                custFullname = dialogView.findViewById(R.id.seemoreCustFullname_Textview);
                orgName = dialogView.findViewById(R.id.seemoreCustOrgName_Textview);
                custAddress = dialogView.findViewById(R.id.seemoreCustAddress_Textview);
                custPhoneNum = dialogView.findViewById(R.id.seemoreCustPhone_Textview);
                custEmail = dialogView.findViewById(R.id.seemoreCustEmail_Textview);
                declineButton = dialogView.findViewById(R.id.declineButton_BTDCardview);
                acceptButton = dialogView.findViewById(R.id.acceptButton_BTDCardview);
                completeButton = dialogView.findViewById(R.id.completeButton_BTDCardview);
                cancelButton = dialogView.findViewById(R.id.cancelButton_BTDCardview);
                exitButton = dialogView.findViewById(R.id.exitButtonBookingDetails_ImageButton);

                String branchImageUri = String.valueOf(bookingList.get(clickedPosition).getBranchImage());
                String layoutImageUri = String.valueOf(bookingList.get(clickedPosition).getLayoutImage());
                String paymentImageUri = String.valueOf(bookingList.get(clickedPosition).getProofOfPayment());

                if (!branchImageUri.isEmpty() && branchImageUri !=null){
                    Picasso.get().load(branchImageUri).into(branchImage);
                }
                if (!layoutImageUri.isEmpty() && layoutImageUri!=null){
                    Picasso.get().load(layoutImageUri).into(layoutImage);
                }
                if (!paymentImageUri.isEmpty() && paymentImageUri!=null){
                    Picasso.get().load(paymentImageUri).into(paymentImage);
                }

                branchName.setText(model.getBranchName());
                layoutName.setText(model.getLayoutName());
                bookingStatus.setText(model.getBookingStatus());
                bookingPayment.setText(model.getTotalPayment());
                rateType.setText(model.getRateType());
                ratePrice.setText("₱"+model.getRateValue());
                paymentOption.setText(model.getPaymentOption());
                tenantsNum.setText(model.getNumOfTenants());
                startDate.setText(model.getBookingStartDate());
                endDate.setText(model.getBookingEndDate());
                startTime.setText(model.getBookingStartTime());
                endTime.setText(model.getBookingEndTime());
                totalHours.setText(model.getTotalHours());
                custFullname.setText(model.getCustomerFullname());
                orgName.setText(model.getOrganizationName());
                custAddress.setText(model.getCustomerAddress());
                custPhoneNum.setText(model.getCustomerPhoneNum());
                custEmail.setText(model.getCustomerEmail());

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();

                exitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                CollectionReference AllSubmittedBookingRef = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        booking.setBookingStatus("Ongoing");
                        holder.bookingStatus.setText(booking.getBookingStatus());
                        AllSubmittedBookingRef.document(booking.getBookingId())
                                .set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "Booking status updated successfully");
                                        Toast.makeText(context, "Booking accepted and is on ongoing tab", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();

                                        ownerUserAcceptBookingActivity(custFullname.getText().toString());

                                        String branch_Name= branchName.getText().toString();
                                        String spaceName = layoutName.getText().toString();
                                        LocalDateTime now = LocalDateTime.now();
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                        String dateTimeString = now.format(formatter);
                                        String title = "Booking Notification: "+dateTimeString;
                                        String message = "\nThe "+spaceName+" that you booked from "+branch_Name + " has been approved.";
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("CustomerUserAccounts").document(customerID)
                                                .get()
                                                .addOnSuccessListener(documentSnapshot -> {
                                                    String customerFCMToken = documentSnapshot.getString("fcmToken");
                                                    FCMSend.pushNotification(context.getApplicationContext(), customerFCMToken, title, message);
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e(TAG, "Error getting FCM token for owner", e);
                                                });
                                        CollectionReference notificationsRef = db.collection("CustomerNotificationStorage");
                                        // Create a new notification document with a randomly generated ID
                                        DocumentReference newNotificationRef = notificationsRef.document();
                                        String newNotificationId = newNotificationRef.getId();
                                        // Add the notification document to the "Notifications" collection
                                        Map<String, Object> notification = new HashMap<>();
                                        notification.put("notificationId", newNotificationId);
                                        notification.put("title", title);
                                        notification.put("message", message);
                                        notification.put("customerId", customerID);
                                        notification.put("bookingTimeDate",com.google.firebase.Timestamp.now());
                                        newNotificationRef.set(notification)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "Notification added with ID: " + newNotificationId);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error adding notification", e);
                                                    }
                                                });

                                    }
                                });
                    }
                });

                completeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        booking.setBookingStatus("Completed");
                        holder.bookingStatus.setText(booking.getBookingStatus());
                        AllSubmittedBookingRef.document(booking.getBookingId())
                                .set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "Booking status updated successfully");
                                        Toast.makeText(context, "Booking completed and is on the completed tab", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();

                                        ownerUserCompletedBookingActivity(custFullname.getText().toString());

                                        String branch_Name= branchName.getText().toString();
                                        String spaceName = layoutName.getText().toString();
                                        LocalDateTime now = LocalDateTime.now();
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                        String dateTimeString = now.format(formatter);
                                        String title = "Booking Notification: "+dateTimeString;
                                        String message = "\nYour booking of "+spaceName+" from "+branch_Name + " is now complete.";
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("CustomerUserAccounts").document(customerID)
                                                .get()
                                                .addOnSuccessListener(documentSnapshot -> {
                                                    String customerFCMToken = documentSnapshot.getString("fcmToken");
                                                    FCMSend.pushNotification(context.getApplicationContext(), customerFCMToken, title, message);
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e(TAG, "Error getting FCM token for owner", e);
                                                });
                                        CollectionReference notificationsRef = db.collection("CustomerNotificationStorage");
                                        // Create a new notification document with a randomly generated ID
                                        DocumentReference newNotificationRef = notificationsRef.document();
                                        String newNotificationId = newNotificationRef.getId();
                                        // Add the notification document to the "Notifications" collection
                                        Map<String, Object> notification = new HashMap<>();
                                        notification.put("notificationId", newNotificationId);
                                        notification.put("title", title);
                                        notification.put("message", message);
                                        notification.put("customerId", customerID);
                                        notification.put("bookingTimeDate",com.google.firebase.Timestamp.now());
                                        newNotificationRef.set(notification)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "Notification added with ID: " + newNotificationId);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error adding notification", e);
                                                    }
                                                });
                                    }
                                });
                    }
                });

                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        booking.setBookingStatus("Declined");
                        holder.bookingStatus.setText(booking.getBookingStatus());
                        AllSubmittedBookingRef.document(booking.getBookingId())
                                .set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "Booking status updated successfully");
                                        Toast.makeText(context, "Booking declined and is on the completed tab", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();

                                        ownerUserDeclineBookingActivity(custFullname.getText().toString());

                                        String branch_Name= branchName.getText().toString();
                                        String spaceName = layoutName.getText().toString();
                                        LocalDateTime now = LocalDateTime.now();
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                        String dateTimeString = now.format(formatter);
                                        String title = "Booking Notification: "+dateTimeString;
                                        String message = "\nThe "+spaceName+" that you booked from "+branch_Name + " has been declined.";
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("CustomerUserAccounts").document(customerID)
                                                .get()
                                                .addOnSuccessListener(documentSnapshot -> {
                                                    String customerFCMToken = documentSnapshot.getString("fcmToken");
                                                    FCMSend.pushNotification(context.getApplicationContext(), customerFCMToken, title, message);
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e(TAG, "Error getting FCM token for owner", e);
                                                });
                                        CollectionReference notificationsRef = db.collection("CustomerNotificationStorage");
                                        // Create a new notification document with a randomly generated ID
                                        DocumentReference newNotificationRef = notificationsRef.document();
                                        String newNotificationId = newNotificationRef.getId();
                                        // Add the notification document to the "Notifications" collection
                                        Map<String, Object> notification = new HashMap<>();
                                        notification.put("notificationId", newNotificationId);
                                        notification.put("title", title);
                                        notification.put("message", message);
                                        notification.put("customerId", customerID);
                                        notification.put("bookingTimeDate",com.google.firebase.Timestamp.now());
                                        newNotificationRef.set(notification)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "Notification added with ID: " + newNotificationId);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error adding notification", e);
                                                    }
                                                });
                                    }
                                });
                    }
                });
            }
        });

    }

    public void ownerUserAcceptBookingActivity( String customerName){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String ownerId= firebaseAuth.getCurrentUser().getUid();
        String activity = "Accepted a booking from " + customerName ;

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
    public void ownerUserDeclineBookingActivity( String customerName){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String ownerId= firebaseAuth.getCurrentUser().getUid();
        String activity = "Declined a booking from " + customerName ;

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

    public void ownerUserCompletedBookingActivity( String customerName){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String ownerId= firebaseAuth.getCurrentUser().getUid();
        String activity = "Completed a booking from " + customerName ;

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

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder {

        TextView bookingStatus, layoutName, layoutRateType, layoutRatePrice,
                 customerFullname,bookingStartDate, bookingEndDate, totalPayment, paymentOption,
                 seeMoreDetails;
        ImageView layoutImage;
        CardView cardViewBT;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            bookingStatus = itemView.findViewById(R.id.OwnerBT_BookingStatus_Textview);
            layoutName = itemView.findViewById(R.id.OwnerBT_LayoutName_Textview);
            layoutRateType = itemView.findViewById(R.id.OwnerBT_BookingRateType_Textview);
            layoutRatePrice = itemView.findViewById(R.id.OwnerBT_BookingRateValue_Textview);
            customerFullname = itemView.findViewById(R.id.OwnerBT_CustomerFullname_Textview);
            bookingStartDate = itemView.findViewById(R.id.OwnerBT_BookingStartDate_Textview);
            bookingEndDate = itemView.findViewById(R.id.OwnerBT_BookingEndDate_Textview);
            totalPayment = itemView.findViewById(R.id.OwnerBT_TotalPayment_Textview);
            paymentOption = itemView.findViewById(R.id.OwnerBT_PaymentOption_Textview);
            seeMoreDetails = itemView.findViewById(R.id.OwnerBT_SeeMore_Textview);
            layoutImage = itemView.findViewById(R.id.OwnerBT_LayoutImage_Textview);
            cardViewBT = itemView.findViewById(R.id.cardViewBT);
        }
    }

}
