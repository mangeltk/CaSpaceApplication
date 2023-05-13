package com.example.caspaceapplication.customer.SearchManagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.Owner.BranchAdapter;
import com.example.caspaceapplication.Owner.BranchModel;
import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CustSM_SearchManually_fragment extends Fragment {

    //todo: this fragment lets user to search manually location and display the cws

    private SearchView locationSearchView;
    private Button submitLocationButton;

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerViewSMEnterManually;
    private List<BranchModel> dataClassList;
    private BranchAdapter branchAdapter;

    public CustSM_SearchManually_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cust_s_m__search_manually_fragment, container, false);

        submitLocationButton = rootView.findViewById(R.id.submitLocationButton);
        recyclerViewSMEnterManually = rootView.findViewById(R.id.recyclerView_SMEnterManually);
        locationSearchView = rootView.findViewById(R.id.location_SearchView);
        locationSearchView.clearFocus();
        locationSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                String searchText = locationSearchView.getQuery().toString();
                if (!TextUtils.isEmpty(searchText.toLowerCase())) {
                    searchList(searchText);
                }
                return true;
            }
        });

        submitLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = locationSearchView.getQuery().toString();
                if (!TextUtils.isEmpty(searchText.toLowerCase())) {
                    searchList(searchText);
                }
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerViewSMEnterManually.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataClassList = new ArrayList<>();
        branchAdapter = new BranchAdapter(getActivity(), dataClassList);
        recyclerViewSMEnterManually.setAdapter(branchAdapter);

        return rootView;
    }

    public void searchList(String searchQuery) {
        //Clear the existing data
        dataClassList.clear();
        Query query = firebaseFirestore.collection("CospaceBranches")
                .whereGreaterThanOrEqualTo("cospaceCityAddress", searchQuery)
                .whereLessThan("cospaceCityAddress", searchQuery + "\\uf8ff");

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<BranchModel> searchResults = queryDocumentSnapshots.toObjects(BranchModel.class);
                if (searchResults.size() > 0) {
                    Log.d("Firebase", "Documents found: " + searchResults.size());
                    dataClassList.addAll(searchResults);
                    branchAdapter.notifyDataSetChanged();
                } else {
                    Log.d("Firebase", "No documents found");
                }
            }
        });

    }


}