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


public class OwnerBT_PendingTabFragment extends Fragment {

    private RecyclerView recyclerView;
    private OwnerBT_AdapterClass adapterClass;
    private List<Booking_ModelClass> dataClassList;
    private TextView sizePendingBookingsTextview;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String currentUserID = firebaseAuth.getCurrentUser().getUid();
    CollectionReference bookingRef = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");

    public OwnerBT_PendingTabFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_owner_b_t__pending_tab, container, false);

        recyclerView = rootView.findViewById(R.id.pendingTransactionsList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataClassList = new ArrayList<>();
        adapterClass = new OwnerBT_AdapterClass(dataClassList, getActivity());
        recyclerView.setAdapter(adapterClass);

        sizePendingBookingsTextview = rootView.findViewById(R.id.sizePendingBookingsTextview);

        loadPendingBT();

        return rootView;
    }



    private void loadPendingBT() {
        Query query = bookingRef.whereEqualTo("bookingStatus", "Pending")
                .whereEqualTo("ownerId", currentUserID);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                dataClassList.clear();
                int size = queryDocumentSnapshots.size();
                sizePendingBookingsTextview.setText(String.valueOf(size));
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Booking_ModelClass modelClass = documentSnapshot.toObject(Booking_ModelClass.class);
                    dataClassList.add(modelClass);
                }
                adapterClass.notifyDataSetChanged();

                // Update the badge count in the activity
                if (getActivity() instanceof Owner_BookingTransactions) {
                    Owner_BookingTransactions activity = (Owner_BookingTransactions) getActivity();
                    int pendingCount = getPendingBTCount();
                    activity.updateBadgeCount(0, pendingCount);
                }
            }
        });
    }

    private int getPendingBTCount() {
        return dataClassList.size();
    }


}