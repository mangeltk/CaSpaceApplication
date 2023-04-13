package com.example.caspaceapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.caspaceapplication.CoworkingSpaces;
import com.example.caspaceapplication.HomepageAdapter;
import com.example.caspaceapplication.R;

import java.util.ArrayList;


public class RecyclerHomepageCustomerFragment extends Fragment {


    private String mParam1;
    private String mParam2;
    private ArrayList<CoworkingSpaces> coworkingSpacesArrayList;
    private String[] coworkingHeading;
    private int[] imageResourceID;
    private RecyclerView recyclerView;
    public RecyclerHomepageCustomerFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_homepage_customer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataInitialize();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        HomepageAdapter homepageAdapter = new HomepageAdapter(getContext(), coworkingSpacesArrayList);
        recyclerView.setAdapter(homepageAdapter);
        homepageAdapter.notifyDataSetChanged();
    }

    private void dataInitialize() {

        coworkingSpacesArrayList = new ArrayList<>();

        coworkingHeading = new String[]
                {
                        getString(R.string.cws1),
                        getString(R.string.cws2),
                        getString(R.string.cws3),
                        getString(R.string.cws4),
                        getString(R.string.cws5),
                        getString(R.string.cws6),
                        getString(R.string.cws7),

                };

        imageResourceID = new int[]
                {
                        R.drawable.img1,
                        R.drawable.img2,
                        R.drawable.img3,
                        R.drawable.img4,
                        R.drawable.img5,
                        R.drawable.img6,
                        R.drawable.img7,

                };

        for(int i=0; i<coworkingHeading.length; i++)
        {
            CoworkingSpaces coworkingSpaces = new CoworkingSpaces(coworkingHeading[i], imageResourceID[i]);
            coworkingSpacesArrayList.add(coworkingSpaces);

        }


    }
}