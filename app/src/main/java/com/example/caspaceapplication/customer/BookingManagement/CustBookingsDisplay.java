package com.example.caspaceapplication.customer.BookingManagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.ModelClasses.Booking_ModelClass;
import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class CustBookingsDisplay extends Fragment {

    String layoutName, layoutId, ownerId, layoutImage, branchName, branchImage, formattedDate;

    AppCompatButton closeButton;

    LinearLayout wholeScheduledLayoutLinearLayout;

    CalendarView calendarView;

    TextView DateSelectedTextview, ScheduledBookingsCountTextview;

    BookingsSchedulesFragmentAdapter adapter;
    List<Booking_ModelClass> modelClassList;
    RecyclerView ScheduledBookingsRecyclerview;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference AllSubmittedBookingRef = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");

    public CustBookingsDisplay() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cust_bookings_display, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            layoutName = bundle.getString("layoutName");
            layoutId = bundle.getString("layout_id");
            ownerId = bundle.getString("owner_id");
            layoutImage = bundle.getString("layoutImage");
            branchName = bundle.getString("branchName");
            branchImage = bundle.getString("branchImage");
        }

        wholeScheduledLayoutLinearLayout = rootView.findViewById(R.id.wholeScheduledLayout_LinearLayout);
        closeButton = rootView.findViewById(R.id.backToLayoutBookingDetails_LinearLayout);
        calendarView = rootView.findViewById(R.id.ScheduledCalendarView);
        DateSelectedTextview = rootView.findViewById(R.id.DateSelected_Textview);
        ScheduledBookingsCountTextview = rootView.findViewById(R.id.ScheduledBookingsCount_Textview);
        ScheduledBookingsRecyclerview = rootView.findViewById(R.id.ScheduledBookings_Recyclerview);

        ScheduledBookingsRecyclerview.setHasFixedSize(true);
        ScheduledBookingsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        modelClassList = new ArrayList<>();
        adapter =  new BookingsSchedulesFragmentAdapter(modelClassList);
        ScheduledBookingsRecyclerview.setAdapter(adapter);

        //ScheduledBookingsCountTextview.setVisibility(View.GONE);
        displayScheduled();


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeButton.setVisibility(View.GONE);
                wholeScheduledLayoutLinearLayout.setVisibility(View.GONE);
            }
        });

        return rootView;
    }

    private void displayScheduled(){

        Calendar calendar = Calendar.getInstance();
        calendarView.setMinDate(calendar.getTimeInMillis());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
                formattedDate = dateFormat.format(selectedDate.getTime());
                DateSelectedTextview.setText(formattedDate);

                // Clear the list

                //adapter.notifyDataSetChanged();
                getAllSubmittedBookings();

                // Retrieve accepted bookings
                //getAllSubmittedBookings(layoutName, formattedDate, "Accepted");

                // Retrieve ongoing bookings
                //getAllSubmittedBookings(layoutName, formattedDate, "Ongoing");

                // Retrieve completed bookings
                //getAllSubmittedBookings(layoutName, formattedDate, "Completed");

            }
        });
    }

    private void getAllSubmittedBookings() {
        final AtomicInteger acceptedCount = new AtomicInteger(0);
        final AtomicInteger ongoingCount = new AtomicInteger(0);
        final AtomicInteger completedCount = new AtomicInteger(0);

        modelClassList.clear(); // Clear the list before adding new bookings

        // Create tasks for each query
        Task<QuerySnapshot> acceptedTask = AllSubmittedBookingRef
                .whereEqualTo("layoutName", layoutName)
                .whereEqualTo("BookDateSelected", formattedDate)
                .whereEqualTo("bookingStatus", "Accepted")
                .get();

        Task<QuerySnapshot> ongoingTask = AllSubmittedBookingRef
                .whereEqualTo("layoutName", layoutName)
                .whereEqualTo("BookDateSelected", formattedDate)
                .whereEqualTo("bookingStatus", "Ongoing")
                .get();

        Task<QuerySnapshot> completedTask = AllSubmittedBookingRef
                .whereEqualTo("layoutName", layoutName)
                .whereEqualTo("BookDateSelected", formattedDate)
                .whereEqualTo("bookingStatus", "Completed")
                .get();

        // Combine tasks into a single task
        Task<List<QuerySnapshot>> combinedTask = Tasks.whenAllSuccess(acceptedTask, ongoingTask, completedTask);

        combinedTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                for (QuerySnapshot queryDocumentSnapshots : querySnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Booking_ModelClass booking = snapshot.toObject(Booking_ModelClass.class);
                            modelClassList.add(booking);
                        }
                    }
                }

                // Update counts
                acceptedCount.set(querySnapshots.get(0).size());
                ongoingCount.set(querySnapshots.get(1).size());
                completedCount.set(querySnapshots.get(2).size());

                adapter.notifyDataSetChanged();

                int totalCount = acceptedCount.get() + ongoingCount.get() + completedCount.get();

                if (totalCount>0){
                    ScheduledBookingsCountTextview.setVisibility(View.VISIBLE);
                }
                ScheduledBookingsCountTextview.setText(String.valueOf(totalCount));

            }
        });
    }


    public class BookingsSchedulesFragmentAdapter extends RecyclerView.Adapter<BookingsSchedulesFragmentAdapter.ViewHolder>{

        private  List<Booking_ModelClass> dataClass;

        public BookingsSchedulesFragmentAdapter(List<Booking_ModelClass> dataClass) {
            this.dataClass = dataClass;
        }

        @NonNull
        @Override
        public BookingsSchedulesFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyleritem_schedulebooking, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BookingsSchedulesFragmentAdapter.ViewHolder holder, int position) {

            String layoutImageString = dataClass.get(position).getLayoutImage();
            if (layoutImageString!=null && !layoutImageString.isEmpty()){
                Picasso.get().load(layoutImageString).into(holder.layoutImage);
            }

            holder.bookingId.setText(dataClass.get(position).getBookingId());
            holder.bookingStatus.setText(dataClass.get(position).getBookingStatus());

            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            String startTimeString = timeFormat.format(dataClass.get(position).getBookStartTimeSelected().toDate());
            String endTimeString = timeFormat.format(dataClass.get(position).getBookEndTimeSelected().toDate());
            holder.bookingTimeDuration.setText(startTimeString + " - "+ endTimeString);

            holder.EachHourTitle.setText(startTimeString);


        }

        @Override
        public int getItemCount() {
            return dataClass.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView layoutImage;
            TextView EachHourTitle, bookingId, bookingTimeDuration, bookingStatus;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                EachHourTitle = itemView.findViewById(R.id.ScheduledBookingsEachHourTitle_Textview);
                layoutImage = itemView.findViewById(R.id.ScheduledBookingsLayoutImage_Imageview);
                bookingId = itemView.findViewById(R.id.scheduledBookingsID_Textview);
                bookingTimeDuration = itemView.findViewById(R.id.scheduledBookingsTimeDuration_Textview);
                bookingStatus = itemView.findViewById(R.id.scheduledBookingsStatus_Textview);
            }
        }
    }

    }