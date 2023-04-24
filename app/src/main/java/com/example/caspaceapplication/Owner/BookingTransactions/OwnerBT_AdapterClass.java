package com.example.caspaceapplication.Owner.BookingTransactions;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OwnerBT_AdapterClass extends RecyclerView.Adapter<OwnerBT_AdapterClass.BookingViewHolder> {

    private List<OwnerBT_ModelClass> bookingList;
    private Context context;

    public OwnerBT_AdapterClass(List<OwnerBT_ModelClass> bookingList, Context context) {
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
        OwnerBT_ModelClass booking = bookingList.get(position);
        holder.recbookingID.setText(booking.getBookingID());
        holder.recCustomerName.setText(booking.getCustomerID());
        holder.recLayoutName.setText(booking.getSpaceID());
        holder.recDuration.setText(booking.getBookingDuration());
        holder.recTotalPayment.setText(booking.getBookingTotal());
        holder.recStatus.setText(booking.getBookingStatus());

        holder.recSeeMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.bt_moredetails_popup, null);

                TextView btdBookingID, btdCustomerName, btdLayoutName, btdDuration, btdDate, btdTotal, btdStatus;
                ImageView btdImagePayment;
                AppCompatButton btdDecline, btdAccept, btdComplete, btdCancel;

                btdBookingID = dialogView.findViewById(R.id.BTDbookingID_Textview);
                btdCustomerName = dialogView.findViewById(R.id.BTDcustomername_Textview);
                btdLayoutName = dialogView.findViewById(R.id.BTDlayoutName_Textview);
                btdDuration = dialogView.findViewById(R.id.BTDduration_Textview);
                btdDate = dialogView.findViewById(R.id.BTDdate_Textview);
                btdTotal = dialogView.findViewById(R.id.BTDtotalPayment_Textview);
                btdStatus = dialogView.findViewById(R.id.BTDstatus_Textview);
                btdImagePayment = dialogView.findViewById(R.id.BTDproofofpayment_imageview);
                btdDecline = dialogView.findViewById(R.id.declineButton_BTDCardview);
                btdAccept = dialogView.findViewById(R.id.acceptButton_BTDCardview);
                btdComplete = dialogView.findViewById(R.id.completeButton_BTDCardview);
                btdCancel = dialogView.findViewById(R.id.cancelButton_BTDCardview);

                //todo:code for image proof of payment
                /*String imageUri = String.valueOf(dataClassList.get(clickedPosition).getPromotionImage());
                if (imageUri != null && !imageUri.isEmpty()) {
                    Picasso.get().load(imageUri).into(pdImageDetailed);
                }*/
                btdBookingID.setText(bookingList.get(clickedPosition).getBookingID());
                btdCustomerName.setText(bookingList.get(clickedPosition).getCustomerID());
                btdLayoutName.setText(bookingList.get(clickedPosition).getSpaceID());
                btdDuration.setText(bookingList.get(clickedPosition).getBookingDuration());
                btdDate.setText(bookingList.get(clickedPosition).getBookingDate());
                btdTotal.setText(bookingList.get(clickedPosition).getBookingTotal());
                btdStatus.setText(bookingList.get(clickedPosition).getBookingStatus());

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();

                btdCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btdAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Update booking status to "ongoing"
                        booking.setBookingStatus("Ongoing");
                        // Update UI to reflect the new booking status
                        holder.recStatus.setText(booking.getBookingStatus());
                        // Store updated booking in Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("OwnerBookingTransactions").document(booking.getBookingID())
                                .set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "Booking status updated successfully");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "Booking update failed", e);
                                    }
                                });
                        dialog.dismiss();
                    }
                });

                btdComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Update booking status to "Completed"
                        booking.setBookingStatus("Completed");
                        // Update UI to reflect the new booking status
                        holder.recStatus.setText(booking.getBookingStatus());
                        // Store updated booking in Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("OwnerBookingTransactions").document(booking.getBookingID())
                                .set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "Booking status updated successfully");
                                    }
                                });
                    }
                });

                btdDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Update booking status to "Declined"
                        booking.setBookingStatus("Declined");
                        // Update UI to reflect the new booking status
                        holder.recStatus.setText(booking.getBookingStatus());
                        // Store updated booking in Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("OwnerBookingTransactions").document(booking.getBookingID())
                                .set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "Booking status updated successfully");

                                    }
                                });
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder {

        AppCompatButton recDecline, recAccept, recComplete, recCancel;
        TextView recbookingID, recCustomerName, recLayoutName, recDuration, recTotalPayment, recStatus;
        TextView recSeeMoreDetails;
        CardView recCardViewBT;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

           /* recDecline = itemView.findViewById(R.id.declineButton_BTDCardview);
            recAccept = itemView.findViewById(R.id.acceptButton_BTDCardview);
            recComplete = itemView.findViewById(R.id.completeButton_BTDCardview);
            recCancel = itemView.findViewById(R.id.cancelButton_BTDCardview);*/

            recAccept = itemView.findViewById(R.id.acceptButton_BTDCardview);


            recbookingID = itemView.findViewById(R.id.bookingID_BTCardview);
            recCustomerName = itemView.findViewById(R.id.customerName_BTCardview);
            recLayoutName = itemView.findViewById(R.id.layoutName_BTCardview);
            recDuration = itemView.findViewById(R.id.duration_BTCardview);
            recTotalPayment = itemView.findViewById(R.id.total_BTCardview);
            recStatus = itemView.findViewById(R.id.status_BTTextViewCardview);

            recSeeMoreDetails = itemView.findViewById(R.id.seeMoreDetails_TextviewCardview);

        }
    }

    public void refreshData(List<OwnerBT_ModelClass> bookingList) {
        this.bookingList = bookingList;
        notifyDataSetChanged();
    }
}
