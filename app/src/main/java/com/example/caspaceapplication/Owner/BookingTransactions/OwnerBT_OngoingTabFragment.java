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


public class OwnerBT_OngoingTabFragment extends Fragment {

    private RecyclerView ongoingRecyclerView;
    private OwnerBT_AdapterClass ongoingAdapterClass;
    private List<Booking_ModelClass> ongoingDataClassList;

    private RecyclerView acceptedRecyclerview;
    private OwnerBT_AdapterClass acceptedAdapterClass;
    private List<Booking_ModelClass> acceptedDataClassList;

    private TextView sizeOngoingBookingsTextview, sizeAcceptedBookingsTextview;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    CollectionReference bookingRef = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");

    public OwnerBT_OngoingTabFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView2 = inflater.inflate(R.layout.fragment_owner_b_t__ongoing_tab, container, false);

        ongoingRecyclerView = rootView2.findViewById(R.id.ongoingTransactionsList);
        ongoingRecyclerView.setHasFixedSize(true);
        ongoingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ongoingDataClassList = new ArrayList<>();
        ongoingAdapterClass = new OwnerBT_AdapterClass(ongoingDataClassList, getActivity());
        ongoingRecyclerView.setAdapter(ongoingAdapterClass);


        acceptedRecyclerview = rootView2.findViewById(R.id.acceptedTransactionsList);
        acceptedRecyclerview.setHasFixedSize(true);
        acceptedRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        acceptedDataClassList = new ArrayList<>();
        acceptedAdapterClass = new OwnerBT_AdapterClass(acceptedDataClassList, getActivity());
        acceptedRecyclerview.setAdapter(acceptedAdapterClass);

        sizeOngoingBookingsTextview = rootView2.findViewById(R.id.sizeOngoingBookingsTextview);
        sizeAcceptedBookingsTextview = rootView2.findViewById(R.id.sizeAcceptedBookingsTextview);


        loadOngoingBT();
        loadAcceptedBT();

        return rootView2;
    }

    private void loadOngoingBT() {
        String currentUserID = firebaseAuth.getCurrentUser().getUid();
        Query query = bookingRef.whereEqualTo("bookingStatus", "Ongoing")
                .whereEqualTo("ownerId", currentUserID);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int size = queryDocumentSnapshots.size();
                sizeOngoingBookingsTextview.setText(String.valueOf(size));

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Booking_ModelClass modelClass = documentSnapshot.toObject(Booking_ModelClass.class);
                    ongoingDataClassList.add(modelClass);
                }
                ongoingAdapterClass.notifyDataSetChanged();
            }
        });
    }


    private void loadAcceptedBT(){
        String currentUserID = firebaseAuth.getCurrentUser().getUid();
        Query query = bookingRef.whereEqualTo("bookingStatus", "Accepted")
                .whereEqualTo("ownerId", currentUserID);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int size = queryDocumentSnapshots.size();
                sizeAcceptedBookingsTextview.setText(String.valueOf(size));

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Booking_ModelClass modelClass = documentSnapshot.toObject(Booking_ModelClass.class);
                    acceptedDataClassList.add(modelClass);
                }
                acceptedAdapterClass.notifyDataSetChanged();
            }
        });
    }

}