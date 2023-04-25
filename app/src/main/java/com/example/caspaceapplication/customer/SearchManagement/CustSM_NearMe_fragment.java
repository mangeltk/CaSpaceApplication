package com.example.caspaceapplication.customer.SearchManagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.caspaceapplication.R;

public class CustSM_NearMe_fragment extends Fragment {

    public CustSM_NearMe_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cust_s_m__near_me_fragment, container, false);



        return rootView;
    }

}