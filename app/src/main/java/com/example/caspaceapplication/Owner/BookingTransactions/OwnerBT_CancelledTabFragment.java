package com.example.caspaceapplication.Owner.BookingTransactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.ModelClasses.Booking_ModelClass;
import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OwnerBT_CancelledTabFragment extends Fragment {

    private RecyclerView cancelledRecyclerView;
    private OwnerBT_AdapterClass cancelledAdapterClass;
    private List<Booking_ModelClass>  cancelledDataClassList;

    private RecyclerView declinedRecyclerView;
    private OwnerBT_AdapterClass declinedAdapterClass;
    private List<Booking_ModelClass>  declinedDataClassList;

    private TextView sizeDeclinedBookingsTextview, sizeCancelledBookingsTextview;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    CollectionReference bookingRef = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");

    public OwnerBT_CancelledTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_owner_b_t__cancelled_tab, container, false);

        cancelledRecyclerView = rootView.findViewById(R.id.cancelledTransactionsList);
        cancelledRecyclerView.setHasFixedSize(true);
        cancelledRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cancelledDataClassList = new ArrayList<>();
        cancelledAdapterClass = new OwnerBT_AdapterClass(cancelledDataClassList, getActivity());
        cancelledRecyclerView.setAdapter(cancelledAdapterClass);

        declinedRecyclerView = rootView.findViewById(R.id.declinedTransactionsList);
        declinedRecyclerView.setHasFixedSize(true);
        declinedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        declinedDataClassList = new ArrayList<>();
        declinedAdapterClass = new OwnerBT_AdapterClass(declinedDataClassList, getActivity());
        declinedRecyclerView.setAdapter(declinedAdapterClass);

        sizeCancelledBookingsTextview = rootView.findViewById(R.id.sizeCancelledBookingsTextview);
        sizeDeclinedBookingsTextview = rootView.findViewById(R.id.sizeDeclinedBookingsTextview);

        loadCancelledBT();
        loadDeclineddBT();

        return rootView;
    }

    private void loadCancelledBT(){
        String currentUserID = firebaseAuth.getCurrentUser().getUid();
        Query query = bookingRef.whereEqualTo("bookingStatus", "Cancelled")
                .whereEqualTo("ownerId", currentUserID);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int size = queryDocumentSnapshots.size();
                sizeCancelledBookingsTextview.setText(String.valueOf(size));

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Booking_ModelClass modelClass = documentSnapshot.toObject(Booking_ModelClass.class);
                    cancelledDataClassList.add(modelClass);
                }
                cancelledAdapterClass.notifyDataSetChanged();
            }
        });

    }


    private void loadDeclineddBT() {
        String currentUserID = firebaseAuth.getCurrentUser().getUid();
        Query query = bookingRef.whereEqualTo("bookingStatus", "Declined")
                .whereEqualTo("ownerId", currentUserID);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int size = queryDocumentSnapshots.size();
                sizeDeclinedBookingsTextview.setText(String.valueOf(size));

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Booking_ModelClass modelClass = documentSnapshot.toObject(Booking_ModelClass.class);
                    declinedDataClassList.add(modelClass);
                }
                declinedAdapterClass.notifyDataSetChanged();
            }
        });

    }

}