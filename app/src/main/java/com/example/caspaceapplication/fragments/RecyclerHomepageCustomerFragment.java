package com.example.caspaceapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.caspaceapplication.customer.CoworkingSpaces;
import com.example.caspaceapplication.customer.HomepageAdapter;
import com.example.caspaceapplication.R;

import java.util.ArrayList;


public class RecyclerHomepageCustomerFragment extends Fragment {

    private static final String TAG = "RecyclerHomepageCustomer";

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void getImages()
    {
        Log.d(TAG, "initImageBitmap: preparing bitmaps.");


    }
}