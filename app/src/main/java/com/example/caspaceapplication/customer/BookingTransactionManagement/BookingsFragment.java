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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.ModelClasses.BookingDetails_ModelClass;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BookingsFragment extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    CollectionReference AllSubmittedBookingRef = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");

    List<BookingDetails_ModelClass> modelClassList;
    CustBookingFragmentAdapter adapter;

    RecyclerView customerBookingsRecyclerview;

    boolean isAscendingOrder = true; //for dates sorting
    SearchView custBookingsSearchview;
    TextView recentTextview, oldestTextview;
    Spinner bookingStatusSpinner;

    public BookingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bookings, container, false);


        customerBookingsRecyclerview = rootView.findViewById(R.id.customerBookings_Recyclerview);
        customerBookingsRecyclerview.setHasFixedSize(true);
        customerBookingsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        modelClassList = new ArrayList<>();
        adapter =  new CustBookingFragmentAdapter(modelClassList);
        customerBookingsRecyclerview.setAdapter(adapter);

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
        String[] bookingStatusItems = {"","Pending", "Ongoing", "Completed", "Cancelled", "Declined"};
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
        List<BookingDetails_ModelClass> dataSearchList = new ArrayList<>();
        for (BookingDetails_ModelClass data : modelClassList){
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

    //display all
    public void displayAllBookings(){
        modelClassList.clear();
        AllSubmittedBookingRef.whereEqualTo("customerId", user.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                            BookingDetails_ModelClass modelClass = documentSnapshot.toObject(BookingDetails_ModelClass.class);
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
                            BookingDetails_ModelClass modelClass = documentSnapshot.toObject(BookingDetails_ModelClass.class);
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
        Collections.sort(modelClassList, new Comparator<BookingDetails_ModelClass>() {
            @Override
            public int compare(BookingDetails_ModelClass o1, BookingDetails_ModelClass o2) {
                if (o1.getBookingStartDate() == null || o2.getBookingStartDate() == null) {
                    return 0;
                }
                if (isAscendingOrder) {
                    return o1.getBookingStartDate().compareTo(o2.getBookingStartDate());
                } else {
                    return o2.getBookingStartDate().compareTo(o1.getBookingStartDate());
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
        Collections.sort(modelClassList, new Comparator<BookingDetails_ModelClass>() {
            @Override
            public int compare(BookingDetails_ModelClass o1, BookingDetails_ModelClass o2) {
                if (o1.getBookingStartDate() == null || o2.getBookingStartDate() == null) {
                    return 0;
                }
                if (isAscendingOrder) {
                    return o2.getBookingStartDate().compareTo(o1.getBookingStartDate());
                } else {
                    return o1.getBookingStartDate().compareTo(o2.getBookingStartDate());
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

        private  List<BookingDetails_ModelClass> dataClass;

        public CustBookingFragmentAdapter(List<BookingDetails_ModelClass> dataClass) {
            this.dataClass = dataClass;
        }

        public void setSearchList(List<BookingDetails_ModelClass> dataSearchList){
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
            holder.bookingStatus.setText(dataClass.get(position).getBookingStatus());
            holder.bookingRateType.setText(dataClass.get(position).getRateType());
            holder.bookingRateValue.setText("₱"+dataClass.get(position).getRateValue());
            holder.bookingStartDate.setText(dataClass.get(position).getBookingStartDate());
            holder.bookingTotalPayment.setText(dataClass.get(position).getTotalPayment());
            holder.seeMoreDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = holder.getAdapterPosition();
                    BookingDetails_ModelClass model = modelClassList.get(clickedPosition);

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.recycleitem_custbookingcardview_moredetails, null);

                    ImageView branchImage, layoutImage, paymentImage;
                    TextView branchName, layoutName, bookingStatus, bookingPayment, rateType, ratePrice, paymentOption, tenantsNum,
                            startDate, endDate, startTime, endTime, totalHours, totalDays, totalWeeks, totalMonths, totalYears,
                            custFullname, orgName, custAddress, custPhoneNum, custEmail;

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
                    totalDays = dialogView.findViewById(R.id.seemoreTotalDays_Textview);
                    totalWeeks = dialogView.findViewById(R.id.seemoreTotalWeeks_Textview);
                    totalMonths = dialogView.findViewById(R.id.seemoreTotalMonths_Textview);
                    totalYears = dialogView.findViewById(R.id.seemoreTotalYears_Textview);
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
                    totalDays.setText(model.getTotalDays());
                    totalWeeks.setText(model.getTotalWeeks());
                    totalMonths.setText(model.getTotalMonths());
                    totalYears.setText(model.getTotalYears());
                    custFullname.setText(model.getCustomerFullname());
                    orgName.setText(model.getOrganizationName());
                    custAddress.setText(model.getCustomerAddress());
                    custPhoneNum.setText(model.getCustomerPhoneNum());
                    custEmail.setText(model.getCustomerEmail());
                    String ownerId= model.getOwnerId();
                    String bookingId= model.getBookingId();
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

                    if (bookingStatus.getText().toString().equals("Pending")){
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
            TextView branchName, bookingStatus, layoutName, bookingRateType, bookingRateValue, bookingStartDate,bookingTotalPayment, seeMoreDetails;

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