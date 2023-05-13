package com.example.caspaceapplication.Owner.BookingTransactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.R;
import com.example.caspaceapplication.ModelClasses.BookingDetails_ModelClass;
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
    private List<BookingDetails_ModelClass> dataClassList;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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

        loadPendingBT();

        return rootView;
    }

    private void loadPendingBT(){
        String currentUserID = firebaseAuth.getCurrentUser().getUid();
        Query query = bookingRef.whereEqualTo("bookingStatus", "Pending")
                                .whereEqualTo("ownerId", currentUserID);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    BookingDetails_ModelClass modelClass = documentSnapshot.toObject(BookingDetails_ModelClass.class);
                    dataClassList.add(modelClass);
                }
                adapterClass.notifyDataSetChanged();
            }
        });
    }

}