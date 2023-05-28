package com.example.caspaceapplication.customer.BookingTransactionManagement;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.ModelClasses.Booking_ModelClass;
import com.example.caspaceapplication.Notification.FCMSend;
import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class BookingsFragment extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    CollectionReference AllSubmittedBookingRef = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");

    List<Booking_ModelClass> modelClassList, ongoingModelClassList;
    CustBookingFragmentAdapter adapter, adapter2;

    RecyclerView customerBookingsRecyclerview,ongoingCutomerBookingsRecyclerview;

    boolean isAscendingOrder = true;
    SearchView custBookingsSearchview;
    TextView recentTextview, oldestTextview;
    Spinner bookingStatusSpinner;

    TextView emptyBookings_Textview;

    public BookingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bookings, container, false);

        emptyBookings_Textview = rootView.findViewById(R.id.emptyBookings_Textview);

        customerBookingsRecyclerview = rootView.findViewById(R.id.customerBookings_Recyclerview);
        customerBookingsRecyclerview.setHasFixedSize(true);
        customerBookingsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        modelClassList = new ArrayList<>();
        adapter =  new CustBookingFragmentAdapter(modelClassList);
        customerBookingsRecyclerview.setAdapter(adapter);

        ongoingCutomerBookingsRecyclerview = rootView.findViewById(R.id.ongoingCutomerBookings_Recyclerview);
        ongoingCutomerBookingsRecyclerview.setHasFixedSize(true);
        ongoingCutomerBookingsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        ongoingModelClassList = new ArrayList<>();
        adapter2 = new CustBookingFragmentAdapter(ongoingModelClassList);
        ongoingCutomerBookingsRecyclerview.setAdapter(adapter2);

        displayOngoingBooking();

        custBookingsSearchview = rootView.findViewById(R.id.searchViewCustBookings);
        custBookingsSearchview.clearFocus();
        custBookingsSearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchBookingList(newText);
                return false;
            }
        });

        recentTextview = rootView.findViewById(R.id.recent_Textview);
        recentTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortRecent();
            }
        });

        oldestTextview = rootView.findViewById(R.id.oldest_Textview);
        oldestTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortOldest();
            }
        });

        bookingStatusSpinner = rootView.findViewById(R.id.custBookingsStatus_Spinner);
        String[] bookingStatusItems = {"","Accepted","Pending", "Ongoing", "Completed", "Cancelled", "Declined"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, bookingStatusItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookingStatusSpinner.setAdapter(adapter);
        bookingStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedStatus = adapterView.getItemAtPosition(position).toString();
                if (selectedStatus == "" && selectedStatus.equals("")){
                    displayAllBookings();
                }else{
                    sortByBookingStatus(selectedStatus);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                displayAllBookings();
            }
        });


        return rootView;
    }

    //Search by layout name or branch name
    private void searchBookingList(String newText) {
        List<Booking_ModelClass> dataSearchList = new ArrayList<>();
        for (Booking_ModelClass data : modelClassList){
            if (data.getBranchName().toLowerCase().contains(newText.toLowerCase())
                    || data.getLayoutName().toLowerCase().contains(newText.toLowerCase())){
                dataSearchList.add(data);
            }
            adapter.setSearchList(dataSearchList);
            if (dataSearchList.isEmpty()){
                Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void displayOngoingBooking() {
        AllSubmittedBookingRef.whereEqualTo("customerId", user.getUid())
                .whereEqualTo("bookingStatus", "Ongoing")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            emptyBookings_Textview.setVisibility(View.VISIBLE);
                        } else {
                            emptyBookings_Textview.setVisibility(View.GONE);
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                Booking_ModelClass modelClass = documentSnapshot.toObject(Booking_ModelClass.class);
                                ongoingModelClassList.add(modelClass);
                            }
                            adapter2.notifyDataSetChanged();
                        }
                    }
                });
    }


    //display all
    public void displayAllBookings(){
        modelClassList.clear();
        AllSubmittedBookingRef.whereEqualTo("customerId", user.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                            Booking_ModelClass modelClass = documentSnapshot.toObject(Booking_ModelClass.class);
                            modelClassList.add(modelClass);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    //sort by status
    public void sortByBookingStatus(String Status){
        modelClassList.clear();
        AllSubmittedBookingRef.whereEqualTo("customerId", user.getUid()).whereEqualTo("bookingStatus", Status)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Booking_ModelClass modelClass = documentSnapshot.toObject(Booking_ModelClass.class);
                            modelClassList.add(modelClass);
                        }
                        adapter.notifyDataSetChanged();
                        if (!modelClassList.isEmpty()){
                            Toast.makeText(getContext(), "Sort by " + Status + " status" , Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getContext(), "No booking with " + Status + " status", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void sortRecent(){
        Collections.sort(modelClassList, new Comparator<Booking_ModelClass>() {
            @Override
            public int compare(Booking_ModelClass o1, Booking_ModelClass o2) {
                if (o1.getBookSubmittedDate() == null || o2.getBookSubmittedDate() == null) {
                    return 0;
                }
                if (isAscendingOrder) {
                    return o1.getBookSubmittedDate().compareTo(o2.getBookSubmittedDate());
                } else {
                    return o2.getBookSubmittedDate().compareTo(o1.getBookSubmittedDate());
                }
            }
        });
        adapter.notifyDataSetChanged();
        if (modelClassList.isEmpty()){
            Toast.makeText(getContext(), "No bookings found", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(getContext(), "Sort by recent dates", Toast.LENGTH_SHORT).show();
    }

    public void sortOldest(){
        Collections.sort(modelClassList, new Comparator<Booking_ModelClass>() {
            @Override
            public int compare(Booking_ModelClass o1, Booking_ModelClass o2) {
                if (o1.getBookSubmittedDate() == null || o2.getBookSubmittedDate() == null) {
                    return 0;
                }
                if (isAscendingOrder) {
                    return o2.getBookSubmittedDate().compareTo(o1.getBookSubmittedDate());
                } else {
                    return o1.getBookSubmittedDate().compareTo(o2.getBookSubmittedDate());
                }
            }
        });
        adapter.notifyDataSetChanged();
        if (modelClassList.isEmpty()){
            Toast.makeText(getContext(), "No bookings found", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(getContext(), "Sort by oldest dates", Toast.LENGTH_SHORT).show();
    }

    public class CustBookingFragmentAdapter extends RecyclerView.Adapter<CustBookingFragmentAdapter.ViewHolder>{

        private  List<Booking_ModelClass> dataClass;

        public CustBookingFragmentAdapter(List<Booking_ModelClass> dataClass) {
            this.dataClass = dataClass;
        }

        public void setSearchList(List<Booking_ModelClass> dataSearchList){
            this.dataClass = dataSearchList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public CustBookingFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem_custbookingcardview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustBookingFragmentAdapter.ViewHolder holder, int position) {
            String branchImageUri = String.valueOf(dataClass.get(position).getBranchImage());
            if (branchImageUri != null && !branchImageUri.isEmpty()){
                Picasso.get().load(branchImageUri).into(holder.branchImage);
            }
            holder.branchName.setText(dataClass.get(position).getBranchName());
            String layoutImageUri = String.valueOf(dataClass.get(position).getLayoutImage());
            if (layoutImageUri !=null && !layoutImageUri.isEmpty()){
                Picasso.get().load(layoutImageUri).into(holder.layoutImage);
            }
            holder.layoutName.setText(dataClass.get(position).getLayoutName());

            String status = dataClass.get(position).getBookingStatus();
            holder.bookingStatus.setText(status);

            if (status.equals("Accepted")){
                int color = ContextCompat.getColor(holder.itemView.getContext(), R.color.green);
                holder.bookingStatus.setTextColor(color);
            }else if (status.equals("Pending")){
                int color = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorWarning);
                holder.bookingStatus.setTextColor(color);
            }else if (status.equals("Ongoing")){
                int color = ContextCompat.getColor(holder.itemView.getContext(), R.color.primary);
                holder.bookingStatus.setTextColor(color);
            }else if (status.equals("Declined") || status.equals("Cancelled") ){
                int color = ContextCompat.getColor(holder.itemView.getContext(), R.color.red);
                holder.bookingStatus.setTextColor(color);
            }else if (status.equals("Completed")){
                int color = ContextCompat.getColor(holder.itemView.getContext(), R.color.primary2);
                holder.bookingStatus.setTextColor(color);
            }



            holder.bookingRateType.setText(dataClass.get(position).getRateType());
            holder.bookingRateValue.setText("₱"+dataClass.get(position).getRatePrice());

            Timestamp submittedOn = dataClass.get(position).getBookSubmittedDate();
            Date dateSubmittedOn = submittedOn.toDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(dateSubmittedOn);
            holder.bookingStartDate.setText(formattedDate);

            holder.bookingTotalPayment.setText(dataClass.get(position).getTotalPayment());
            holder.seeMoreDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = holder.getAdapterPosition();
                    Booking_ModelClass model = modelClassList.get(clickedPosition);

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.recycleitem_custbookingcardview_moredetails, null);

                    ImageView branchImage, layoutImage, paymentImage;
                    TextView branchName, layoutName, bookingId,bookingStatusBelow, bookingPayment, rateType, ratePrice, paymentOption, tenantsNum, ProofOfPaymentTitle,
                            custFullname, orgName, custAddress, custPhoneNum, custEmail, startDate, endDate, startTime, endTime,
                            totalHours, totalHoursTitle, totalDays, totalDaysTitle, totalWeeks, totalWeeksTitle, totalMonths, totalMonthsTitle;

                    AppCompatButton declineButton, acceptButton, completeButton, cancelButton;

                    ImageButton exitButton;

                    branchImage = dialogView.findViewById(R.id.seemoreBranchImage_Imageview);
                    layoutImage = dialogView.findViewById(R.id.seemoreLayoutImage_Imageview);
                    ProofOfPaymentTitle = dialogView.findViewById(R.id.ProofOfPaymentTitle);
                    paymentImage = dialogView.findViewById(R.id.seemorePaymentPic_Imageview);
                    branchName = dialogView.findViewById(R.id.seemoreBranchName_Textview);
                    layoutName = dialogView.findViewById(R.id.seemoreLayoutName_Textview);
                    bookingId = dialogView.findViewById(R.id.seemoreBookingId_Textview);
                    bookingStatusBelow = dialogView.findViewById(R.id.seemoreBookingStatusBelow_Textview);
                    bookingPayment = dialogView.findViewById(R.id.seemoreTotalPayment_Textview);
                    rateType = dialogView.findViewById(R.id.seemoreRateType_Textview);
                    ratePrice = dialogView.findViewById(R.id.seemoreRatePrice_Textview);
                    paymentOption = dialogView.findViewById(R.id.seemorePaymentOption_Textview);
                    tenantsNum = dialogView.findViewById(R.id.seemoreTenantsNum_Textview);
                    startDate = dialogView.findViewById(R.id.seemoreStartDate_Textview);
                    endDate = dialogView.findViewById(R.id.seemoreEndDate_Textview);
                    startTime = dialogView.findViewById(R.id.seemoreStartTime_Textview);
                    endTime = dialogView.findViewById(R.id.seemoreEndTime_Textview);
                    totalHoursTitle = dialogView.findViewById(R.id.hoursTitle);
                    totalHours = dialogView.findViewById(R.id.seemoreTotalHours_Textview);
                    totalDaysTitle = dialogView.findViewById(R.id.daysTitle);
                    totalDays = dialogView.findViewById(R.id.seemoreTotalDays_Textview);
                    totalWeeksTitle = dialogView.findViewById(R.id.weeksTitle);
                    totalWeeks = dialogView.findViewById(R.id.seemoreTotalWeeks_Textview);
                    totalMonthsTitle = dialogView.findViewById(R.id.monthsTitle);
                    totalMonths = dialogView.findViewById(R.id.seemoreTotalMonths_Textview);
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

                    String branchImageUri = String.valueOf(dataClass.get(clickedPosition).getBranchImage());
                    String layoutImageUri = String.valueOf(dataClass.get(clickedPosition).getLayoutImage());
                    String paymentImageUri = String.valueOf(dataClass.get(clickedPosition).getProofOfPayment());

                    if (model.getRateType().equals("Hourly rate")){
                        totalDaysTitle.setVisibility(View.GONE);
                        totalDays.setVisibility(View.GONE);
                        totalWeeksTitle.setVisibility(View.GONE);
                        totalWeeks.setVisibility(View.GONE);
                        totalMonthsTitle.setVisibility(View.GONE);
                        totalMonths.setVisibility(View.GONE);
                    }

                    if (model.getRateType().equals("Daily rate")){
                        totalHoursTitle.setVisibility(View.GONE);
                        totalHours.setVisibility(View.GONE);
                        totalWeeksTitle.setVisibility(View.GONE);
                        totalWeeks.setVisibility(View.GONE);
                        totalMonthsTitle.setVisibility(View.GONE);
                        totalMonths.setVisibility(View.GONE);
                    }

                    if (model.getPaymentOption().equals("Onsite")){
                        paymentImage.setVisibility(View.GONE);
                        ProofOfPaymentTitle.setVisibility(View.GONE);
                    }

                    if (!branchImageUri.isEmpty() && branchImageUri !=null){
                        Picasso.get().load(branchImageUri).into(branchImage);
                    }
                    if (!layoutImageUri.isEmpty() && layoutImageUri!=null){
                        Picasso.get().load(layoutImageUri).into(layoutImage);
                    }
                    if (!paymentImageUri.isEmpty() && paymentImageUri!=null){
                        Picasso.get().load(paymentImageUri).into(paymentImage);
                    }else{

                    }

                    branchName.setText(model.getBranchName());
                    layoutName.setText(model.getLayoutName());
                    bookingId.setText(model.getBookingId());
                    bookingStatusBelow.setVisibility(View.VISIBLE);
                    bookingStatusBelow.setText(model.getBookingStatus());
                    bookingPayment.setText(model.getTotalPayment());
                    rateType.setText(model.getRateType());
                    ratePrice.setText("₱"+model.getRatePrice());
                    paymentOption.setText(model.getPaymentOption());
                    tenantsNum.setText(model.getNumOfTenants());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                    String startDateString = dateFormat.format(model.getBookStartTimeSelected().toDate());
                    startDate.setText(startDateString);

                    String endDateString = dateFormat.format(model.getBookEndTimeSelected().toDate());
                    endDate.setText(endDateString);

                    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                    String startTimeString = timeFormat.format(model.getBookStartTimeSelected().toDate());
                    startTime.setText(startTimeString);

                    String endTimeString = timeFormat.format(model.getBookEndTimeSelected().toDate());
                    endTime.setText(endTimeString);

                    totalHours.setText(model.getTotalHours());
                    totalDays.setText(model.getTotalDays());
                    //totalWeeks.setText(model.getTotalWeeks());
                    //totalMonths.setText(model.getTotalMonths());

                    custFullname.setText(model.getCustomerFullname());
                    orgName.setText(model.getOrganizationName());
                    custAddress.setText(model.getCustomerAddress());
                    custPhoneNum.setText(model.getCustomerPhoneNum());
                    custEmail.setText(model.getCustomerEmail());
                    String ownerId= model.getOwnerId();
                    String bookingID= model.getBookingId();
                    builder.setView(dialogView);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    exitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Exit", Toast.LENGTH_SHORT).show();
                        }
                    });

                    declineButton.setVisibility(View.GONE);
                    acceptButton.setVisibility(View.GONE);
                    completeButton.setVisibility(View.GONE);

                    if (bookingStatusBelow.getText().toString().equals("Pending")){
                        cancelButton.setVisibility(View.VISIBLE);
                        cancelButton.setText("Cancel Booking");
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                builder1.setTitle("Cancel confirmation");
                                builder1.setMessage("Are you sure you want to cancel this booking?");
                                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Query queryByCustId = AllSubmittedBookingRef.whereEqualTo("customerId", user.getUid()).whereEqualTo("bookingId",bookingId);
                                        queryByCustId.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                                        documentSnapshot.getReference().update("bookingStatus", "Cancelled");                                        }
                                                }
                                                Toast.makeText(getContext(), "Booking cancelled", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                displayAllBookings();

                                                String customerName= custFullname.getText().toString();
                                                String spaceName = layoutName.getText().toString();
                                                customerUserActivity(spaceName, branchName.getText().toString());
                                                LocalDateTime now = LocalDateTime.now();
                                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                                String dateTimeString = now.format(formatter);
                                                String title = "Booking Notification: "+dateTimeString;
                                                String message = "\n"+spaceName + " booking from "+customerName +" has been cancelled.";
                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                db.collection("OwnerUserAccounts").document(ownerId)
                                                        .get()
                                                        .addOnSuccessListener(documentSnapshot -> {
                                                            String ownerFCMToken = documentSnapshot.getString("fcmToken");
                                                            FCMSend.pushNotification(getContext(), ownerFCMToken, title, message);
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Log.e(TAG, "Error getting FCM token for owner", e);
                                                        });
                                                CollectionReference notificationsRef = db.collection("OwnerNotificationStorage");
                                                // Create a new notification document with a randomly generated ID
                                                DocumentReference newNotificationRef = notificationsRef.document();
                                                String newNotificationId = newNotificationRef.getId();
                                                // Add the notification document to the "Notifications" collection
                                                Map<String, Object> notification = new HashMap<>();
                                                notification.put("notificationId", newNotificationId);
                                                notification.put("title", title);
                                                notification.put("message", message);
                                                notification.put("ownerId", ownerId);
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
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog dialog1 = builder1.create();
                                dialog1.show();
                            }
                        });
                    }else{
                        cancelButton.setVisibility(View.GONE);
                    }

                }
            });


        }
        public void customerUserActivity( String spaceName, String branchName){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String customerId = firebaseAuth.getCurrentUser().getUid();
            String activity = "Cancelled " + spaceName+" from "+ branchName;

            Map<String, Object> data = new HashMap<>();
            data.put("customerId",customerId);
            data.put("activity", activity);
            data.put("dateTime", Timestamp.now());

            db.collection("CustomerActivity")
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
            return dataClass.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView branchImage, layoutImage;
            TextView branchName, bookingStatus, layoutName, bookingRateType, bookingRateValue, bookingStartDate,bookingTotalPayment;
            CardView seeMoreDetails;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                branchImage = itemView.findViewById(R.id.CustBookingBranchImage_Cardview);
                branchName = itemView.findViewById(R.id.CustBookingBranchName_Cardview);
                layoutImage = itemView.findViewById(R.id.CustBookingLayoutImage_Cardview);
                layoutName = itemView.findViewById(R.id.CustBookingLayoutName_Cardview);
                bookingStatus = itemView.findViewById(R.id.CustBookingBookingStatus_Cardview);
                bookingRateType = itemView.findViewById(R.id.CustBookingBookingRateType_Cardview);
                bookingRateValue = itemView.findViewById(R.id.CustBookingBookingRateValue_Cardview);
                bookingStartDate = itemView.findViewById(R.id.CustBookingBookingStartDate_Cardview);
                bookingTotalPayment = itemView.findViewById(R.id.CustBookingBookingTotalPayment_Cardview);
                seeMoreDetails = itemView.findViewById(R.id.CustBookingBookingSeeMoreTextview_Cardview);

            }
        }
    }


}