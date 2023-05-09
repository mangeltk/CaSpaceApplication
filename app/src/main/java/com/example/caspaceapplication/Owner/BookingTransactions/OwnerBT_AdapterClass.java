package com.example.caspaceapplication.Owner.BookingTransactions;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.BookingTransactionManagement.BookingDetails_ModelClass;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OwnerBT_AdapterClass extends RecyclerView.Adapter<OwnerBT_AdapterClass.BookingViewHolder> {

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

        holder.seeMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*int clickedPosition = holder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.recycleitem_custbookingcardview_moredetails, null);
*/
                /*TextView btdBookingID, btdCustomerName, btdLayoutName, btdDuration, btdDate, btdTotal, btdStatus;
                ImageView btdImagePayment;
                AppCompatButton btdDecline, btdAccept, btdComplete, btdCancel;*/


                /*btdBookingID = dialogView.findViewById(R.id.BTDbookingID_Textview);
                btdCustomerName = dialogView.findViewById(R.id.BTDcustomername_Textview);
                btdLayoutName = dialogView.findViewById(R.id.BTDlayoutName_Textview);
                btdDuration = dialogView.findViewById(R.id.BTDduration_Textview);
                btdDate = dialogView.findViewById(R.id.BTDdate_Textview);
                btdTotal = dialogView.findViewById(R.id.BTDtotalPayment_Textview);
                btdStatus = dialogView.findViewById(R.id.BTDstatus_Textview);
                //btdImagePayment = dialogView.findViewById(R.id.BTDproofofpayment_imageview);
                btdDecline = dialogView.findViewById(R.id.declineButton_BTDCardview);
                btdAccept = dialogView.findViewById(R.id.acceptButton_BTDCardview);
                btdComplete = dialogView.findViewById(R.id.completeButton_BTDCardview);
                btdCancel = dialogView.findViewById(R.id.cancelButton_BTDCardview);*/
                /*String imageUri = String.valueOf(dataClassList.get(clickedPosition).getPromotionImage());
                if (imageUri != null && !imageUri.isEmpty()) {
                    Picasso.get().load(imageUri).into(pdImageDetailed);
                }*/
               /* btdBookingID.setText(bookingList.get(clickedPosition).getBookingID());
                btdCustomerName.setText(bookingList.get(clickedPosition).getCustomerID());
                btdLayoutName.setText(bookingList.get(clickedPosition).getSpaceID());
                btdDuration.setText(bookingList.get(clickedPosition).getBookingDuration());
                btdDate.setText(bookingList.get(clickedPosition).getBookingDate());
                btdTotal.setText(bookingList.get(clickedPosition).getBookingTotal());
                btdStatus.setText(bookingList.get(clickedPosition).getBookingStatus());*/

                int clickedPosition = holder.getAdapterPosition();
                BookingDetails_ModelClass model = bookingList.get(clickedPosition);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.recycleitem_custbookingcardview_moredetails, null);

                ImageView branchImage, layoutImage, paymentImage;
                TextView branchName, layoutName, bookingStatus, bookingPayment, rateType, ratePrice, paymentOption, tenantsNum, startDate, endDate,
                        startTime, endTime, totalHours, custFullname, orgName, custAddress, custPhoneNum, custEmail;

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

                branchName.setText(bookingList.get(clickedPosition).getBranchName());
                layoutName.setText(bookingList.get(clickedPosition).getLayoutName());
                bookingStatus.setText(bookingList.get(clickedPosition).getBookingStatus());
                bookingPayment.setText(bookingList.get(clickedPosition).getTotalPayment());
                rateType.setText(bookingList.get(clickedPosition).getRateType());
                ratePrice.setText("₱"+bookingList.get(clickedPosition).getRateValue());
                paymentOption.setText(bookingList.get(clickedPosition).getPaymentOption());
                tenantsNum.setText(bookingList.get(clickedPosition).getNumOfTenants());
                startDate.setText(bookingList.get(clickedPosition).getBookingStartDate());
                endDate.setText(bookingList.get(clickedPosition).getBookingEndDate());
                startTime.setText(bookingList.get(clickedPosition).getBookingStartTime());
                endTime.setText(bookingList.get(clickedPosition).getBookingEndTime());
                totalHours.setText(bookingList.get(clickedPosition).getTotalHours());
                custFullname.setText(bookingList.get(clickedPosition).getCustomerFullname());
                orgName.setText(bookingList.get(clickedPosition).getOrganizationName());
                custAddress.setText(bookingList.get(clickedPosition).getCustomerAddress());
                custPhoneNum.setText(bookingList.get(clickedPosition).getCustomerPhoneNum());
                custEmail.setText(bookingList.get(clickedPosition).getCustomerEmail());

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();

               /* btdCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });*/

                /*btdAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Update booking status to "ongoing"
                        booking.setBookingStatus("Ongoing");
                        // Update UI to reflect the new booking status
                        holder.bookingStatus.setText(booking.getBookingStatus());
                        // Store updated booking in Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("OwnerBookingTransactions").document(booking.getBookingID())
                                .set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "Booking status updated successfully");
                                        Toast.makeText(context, "Booking accepted and is on ongoing tab", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                    }
                });*/

                /*btdComplete.setOnClickListener(new View.OnClickListener() {
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
                                        Toast.makeText(context, "Booking completed and is on the completed tab", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                    }
                });*/

                /*btdDecline.setOnClickListener(new View.OnClickListener() {
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
                                        Toast.makeText(context, "Booking declined and is on the completed tab", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                    }
                });*/
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
        }
    }

    public void refreshData(List<BookingDetails_ModelClass> bookingList) {
        this.bookingList = bookingList;
        notifyDataSetChanged();
    }
}
