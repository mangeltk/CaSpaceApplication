package com.example.caspaceapplication.customer.BookingTransactionManagement;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class BookingsFragment extends Fragment {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    CollectionReference custBookingsCollectionRef = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");

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
        String[] bookingStatusItems = {"","Pending", "Ongoing", "Completed", "Cancelled"};
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
        custBookingsCollectionRef.whereEqualTo("customerId", user.getUid())
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
        //List<BookingDetails_ModelClass> listForStatus = new ArrayList<>();
        modelClassList.clear();
        custBookingsCollectionRef.whereEqualTo("customerId", user.getUid()).whereEqualTo("bookingStatus", Status)
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
            holder.branchName.setText(dataClass.get(position).branchName);
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
                    }

                    branchName.setText(dataClass.get(clickedPosition).getBranchName());
                    layoutName.setText(dataClass.get(clickedPosition).getLayoutName());
                    bookingStatus.setText(dataClass.get(clickedPosition).getBookingStatus());
                    bookingPayment.setText(dataClass.get(clickedPosition).getTotalPayment());
                    rateType.setText(dataClass.get(clickedPosition).getRateType());
                    ratePrice.setText("₱"+dataClass.get(clickedPosition).getRateValue());
                    paymentOption.setText(dataClass.get(clickedPosition).getPaymentOption());
                    tenantsNum.setText(dataClass.get(clickedPosition).getNumOfTenants());
                    startDate.setText(dataClass.get(clickedPosition).getBookingStartDate());
                    endDate.setText(dataClass.get(clickedPosition).getBookingEndDate());
                    startTime.setText(dataClass.get(clickedPosition).getBookingStartTime());
                    endTime.setText(dataClass.get(clickedPosition).getBookingEndTime());
                    totalHours.setText(dataClass.get(clickedPosition).getTotalHours());
                    custFullname.setText(dataClass.get(clickedPosition).getCustomerFullname());
                    orgName.setText(dataClass.get(clickedPosition).getOrganizationName());
                    custAddress.setText(dataClass.get(clickedPosition).getCustomerAddress());
                    custPhoneNum.setText(dataClass.get(clickedPosition).getCustomerPhoneNum());
                    custEmail.setText(dataClass.get(clickedPosition).getCustomerEmail());

                    builder.setView(dialogView);
                    AlertDialog dialog = builder.create();
                    dialog.show();
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