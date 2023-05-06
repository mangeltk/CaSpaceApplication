package com.example.caspaceapplication.customer.BookingTransactionManagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.R;

import java.util.List;


public class BookingsFragment extends Fragment {

    public BookingsFragment() {

    }

    List<BookingDetails_ModelClass> modelClassList;
    CustBookingFragmentAdapter adapter;


    RecyclerView customerBookingsRecyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bookings, container, false);

        customerBookingsRecyclerview = rootView.findViewById(R.id.customerBookings_Recyclerview);


        return rootView;
    }

    public class CustBookingFragmentAdapter extends RecyclerView.Adapter<CustBookingFragmentAdapter.ViewHolder>{

        private  List<BookingDetails_ModelClass> dataClass;

        public CustBookingFragmentAdapter(List<BookingDetails_ModelClass> dataClass) {
            this.dataClass = dataClass;
        }

        @NonNull
        @Override
        public CustBookingFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull CustBookingFragmentAdapter.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }


}