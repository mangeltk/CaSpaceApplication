package com.example.caspaceapplication.customer.SearchManagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.caspaceapplication.R;


public class CustSM_ChooseMap_fragment extends Fragment {

    //todo: this fragment will display a map with pinned cws everywhere (all)

    public CustSM_ChooseMap_fragment() {
        // Required empty public constructor
    }

    //todo: choose map lets user enter location then it will plot the cws on the map

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cust_s_m__choose_map_fragment, container, false);


        return rootView;
    }



}