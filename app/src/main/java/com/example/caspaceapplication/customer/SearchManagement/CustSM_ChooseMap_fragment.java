package com.example.caspaceapplication.customer.SearchManagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

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


public class CustSM_ChooseMap_fragment extends Fragment {

    private SearchView locationSearchView;
    private Button submitLocationButton;

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerViewSMChooseMap;
    private List<BranchModel> dataClassList;
    private BranchAdapter branchAdapter;

    public CustSM_ChooseMap_fragment() {
        // Required empty public constructor
    }

    //todo: choose map lets user enter location then it will plot the cws on the map

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cust_s_m__choose_map_fragment, container, false);

        submitLocationButton = rootView.findViewById(R.id.submitLocationButton);
        recyclerViewSMChooseMap = rootView.findViewById(R.id.recyclerViewSMChooseMap);

        submitLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = locationSearchView.getQuery().toString();
                if (!TextUtils.isEmpty(searchText)) {
                    searchList(searchText);
                }
            }
        });

        locationSearchView = rootView.findViewById(R.id.locationSearchView);
        locationSearchView.clearFocus();
        locationSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerViewSMChooseMap.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataClassList = new ArrayList<>();
        branchAdapter = new BranchAdapter(getActivity(), dataClassList);
        recyclerViewSMChooseMap.setAdapter(branchAdapter);

        return rootView;
    }

    public void searchList(String text){
        dataClassList.clear();

        Query query = firebaseFirestore.collection("CospaceBranches")
                        .whereGreaterThanOrEqualTo("cospaceAddress", text)
                                .whereLessThan("cospaceAddress", text + "\\uf8ff");

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<BranchModel> searchResults = queryDocumentSnapshots.toObjects(BranchModel.class);
                dataClassList.addAll(searchResults);
                branchAdapter.notifyDataSetChanged();
            }
        });

    }

}