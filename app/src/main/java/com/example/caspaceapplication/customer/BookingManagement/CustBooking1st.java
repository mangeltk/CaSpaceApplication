package com.example.caspaceapplication.customer.BookingManagement;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.R;
import com.example.caspaceapplication.fragments.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


public class CustBooking1st extends Fragment {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    CollectionReference CustomerAccountsRef = firebaseFirestore.collection("CustomerUserAccounts");
    CollectionReference submittedBookingsCollection = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");
    CollectionReference OfficeLayoutsRef = firebaseFirestore.collection("OfficeLayouts");

    LinearLayout CalendarSectionLinearLayout, TimePlots_LinearLayout, selectedDateLinearLayout,selectEndDateLinearLayout,
                 selectDatesAndTimeLinearLayout, selectStartTimeLinearLayout, selectEndTimeLinearLayout,
                 rateDetailsLinearLayout, totalDurationLinearLayout, totalPaymentLinearLayout,
                 custBookingDetsLinearLayout, branchPaymentChannelsLinearLayout, uploadPaymentImageLinearLayout;

    CalendarView calendarView;

    TextView changeTextForDateSelectedTextview, selectedDateTitleTextview ,selectedDateTextview, selectedEndDateTextview, noBookingsNoteTextview, noBookingsaAvailableTextview, selectedStartTimeTextview, selectedEndTimeTextview,
             selectedRateTypeTextview, selectedRatePriceTextview, selectRateTitleTextview, totalCalcDurationTitleTextview, totalCalcDurationTextview, totalPaymentTextview, readTermsTextview;

    AppCompatButton bookNowAppCompatButton;

    String layoutName, layoutId, ownerId, layoutImage, branchName, branchImage;

    String rateType, hourlyRate, dailyRate, weeklyRate, monthlyRate;
    RadioButton hourlyRadioButton, dailyRadioButton, weeklyRadioButton, monthlyRadioButton;
    RadioGroup RadioButtonGroupForRates;

    String selectedStartTimeString, selectedEndTimeString;

    List<String> availableHours;
    List<String> bookedHours;

    String formattedDate = "";
    String totalDaysDailyFromFirestore=null, startDateDailyString=null, endDateDailyString=null;
    int totalDaysInt = 0;

    Timestamp endTimeSelectedDaily = null, startTimeSelectedDaily=null;

    String customerName, customerOrgName, customerPhoneNum, customerEmail, customerAddress, selectedPaymentOption, uploadedPaymentString;
    EditText custFullnameEdittext, custOrgnameEdittext, custTenantsNumEdittext, custPhoneNumEdittext,
             custEmailEdittext, custAddressEdittext;

    Button UploadButtonPaymentImage;

    ImageButton addButtonForDaysImageButton, deleteButtonForDaysImageButton;

    ImageView ProofPaymentImage;

    Uri filepath = null;
    private static final int GALLERY_CODE = 1;

    RadioButton OtherOptRadButton, OnsiteRadButton;

    CheckBox AgreeTermsCheckbox;

    AppCompatButton SubmitBookingDetailsButton;

    ProgressDialog progressDialog;

    public CustBooking1st() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cust_booking1st, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            layoutName = bundle.getString("layoutName");
            layoutId = bundle.getString("layout_id");
            ownerId = bundle.getString("owner_id");
            layoutImage = bundle.getString("layoutImage");
            branchName = bundle.getString("branchName");
            branchImage = bundle.getString("branchImage");
        }

        progressDialog = new ProgressDialog(getContext());

        getLayoutDetails();
        getCustomerDetails();

        CalendarSectionLinearLayout = rootView.findViewById(R.id.CalendarSection_LinearLayout);
        changeTextForDateSelectedTextview = rootView.findViewById(R.id.changeTextForDateSelected_Textview);
        selectedDateTitleTextview = rootView.findViewById(R.id.selectedDateTitle_Textview);
        selectedDateTextview = rootView.findViewById(R.id.selectedDateFromCalendar_Textview);
        selectEndDateLinearLayout = rootView.findViewById(R.id.selectEndDate_LinearLayout);
        selectEndDateLinearLayout.setVisibility(View.GONE);
        selectedEndDateTextview = rootView.findViewById(R.id.selectedEndDateFromCalendar_Textview);
        noBookingsNoteTextview = rootView.findViewById(R.id.noBookings_Textview);
        noBookingsNoteTextview.setVisibility(View.GONE);
        noBookingsaAvailableTextview = rootView.findViewById(R.id.noBookingsaAvailable_Textview);
        noBookingsaAvailableTextview.setVisibility(View.GONE);
        calendarView = rootView.findViewById(R.id.calendar_view);
        RadioButtonGroupForRates = rootView.findViewById(R.id.RadioButtonGroup_ForRates);
        hourlyRadioButton = rootView.findViewById(R.id.hourlyRate_RadioButton);
        dailyRadioButton = rootView.findViewById(R.id.dailyRate_RadioButton);
        weeklyRadioButton = rootView.findViewById(R.id.weeklyRate_RadioButton);
        monthlyRadioButton = rootView.findViewById(R.id.monthlyRate_RadioButton);
        selectedRateTypeTextview = rootView.findViewById(R.id.selectedRateType_Textview);
        selectedRatePriceTextview = rootView.findViewById(R.id.selectedRatePrice_Textview);
        selectRateTitleTextview = rootView.findViewById(R.id.selectRateTitle_Textview);
        rateDetailsLinearLayout = rootView.findViewById(R.id.rateDetails_LinearLayout);
        selectStartTimeLinearLayout = rootView.findViewById(R.id.selectStartTime_LinearLayout);
        selectEndTimeLinearLayout = rootView.findViewById(R.id.selectEndTime_LinearLayout);
        totalDurationLinearLayout = rootView.findViewById(R.id.totalDuration_LinearLayout);
        totalCalcDurationTextview = rootView.findViewById(R.id.totalCalcDuration_Texview);
        addButtonForDaysImageButton = rootView.findViewById(R.id.addButtonForDays_ImageButton);
        deleteButtonForDaysImageButton = rootView.findViewById(R.id.deleteButtonForDays_ImageButton);
        totalCalcDurationTitleTextview = rootView.findViewById(R.id.totalCalcDurationTitle_Texview);
        totalPaymentLinearLayout = rootView.findViewById(R.id.totalPayment_LinearLayout);
        totalPaymentTextview = rootView.findViewById(R.id.totalCalcPayment_Texview);
        custBookingDetsLinearLayout = rootView.findViewById(R.id.custBookingDets_LinearLayout);
        TimePlots_LinearLayout = rootView.findViewById(R.id.TimePlots_LinearLayout);
        selectDatesAndTimeLinearLayout = rootView.findViewById(R.id.FORHOURLYselectDatesAndTime_LinearLayout);
        bookNowAppCompatButton = rootView.findViewById(R.id.bookNow_AppCompatButton);
        selectedStartTimeTextview = rootView.findViewById(R.id.selected_startTime_textview);
        selectedEndTimeTextview = rootView.findViewById(R.id.selected_endTime_textview);
        branchPaymentChannelsLinearLayout = rootView.findViewById(R.id.branchPaymentChannels_LinearLayout);
        uploadPaymentImageLinearLayout = rootView.findViewById(R.id.uploadPaymentImage_LinearLayout);
        UploadButtonPaymentImage = rootView.findViewById(R.id.CustomerDetails_UploadButtonPaymentImage);
        ProofPaymentImage = rootView.findViewById(R.id.CustomerDetails_ProofPaymentImage);
        readTermsTextview = rootView.findViewById(R.id.ReadTerms_Textview);
        AgreeTermsCheckbox = rootView.findViewById(R.id.Terms_Checkbox);
        SubmitBookingDetailsButton = rootView.findViewById(R.id.SubmitBookingDetails_Button);

        // customer details section
        custFullnameEdittext = rootView.findViewById(R.id.customerDetails_Fullname);
        custOrgnameEdittext = rootView.findViewById(R.id.customerDetails_OrgName);
        custTenantsNumEdittext = rootView.findViewById(R.id.customerDetails_TenantsNum);
        custPhoneNumEdittext = rootView.findViewById(R.id.customerDetails_PhoneNum);
        custEmailEdittext = rootView.findViewById(R.id.customerDetails_Email);
        custAddressEdittext = rootView.findViewById(R.id.customerDetails_Address);
        OnsiteRadButton = rootView.findViewById(R.id.customerDetails_OnsiteRadButton);
        OtherOptRadButton = rootView.findViewById(R.id.customerDetails_OtherOptRadButton);


        Calendar calendar = Calendar.getInstance();
        calendarView.setMinDate(calendar.getTimeInMillis());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                List<String> allHours = new ArrayList<>();
                bookedHours = new ArrayList<>();

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
                formattedDate = dateFormat.format(selectedDate.getTime());
                changeTextForDateSelectedTextview.setText(formattedDate);
                selectedDateTextview.setText(formattedDate);

                if (selectedDate != null) {
                    CalendarSectionLinearLayout.setVisibility(View.GONE);
                }

                changeTextForDateSelectedTextview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CalendarSectionLinearLayout.setVisibility(View.VISIBLE);
                    }
                });

                // Generate all possible hours from the opening time to closing time
                Calendar openingTime = Calendar.getInstance();
                openingTime.set(Calendar.HOUR_OF_DAY, 0); // Replace with your opening hour
                openingTime.set(Calendar.MINUTE, 0);
                Calendar closingTime = Calendar.getInstance();
                closingTime.set(Calendar.HOUR_OF_DAY, 23); // Replace with your closing hour
                closingTime.set(Calendar.MINUTE, 59);

                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                while (openingTime.before(closingTime)) {
                    if (formattedDate.equals(dateFormat.format(Calendar.getInstance().getTime()))) {
                        if (openingTime.getTime().after(Calendar.getInstance().getTime())) {
                            allHours.add(timeFormat.format(openingTime.getTime()));
                        }
                    } else {
                        allHours.add(timeFormat.format(openingTime.getTime()));
                    }
                    openingTime.add(Calendar.HOUR, 1);
                }
                availableHours = new ArrayList<>(allHours);

                submittedBookingsCollection
                        .whereEqualTo("layoutName", layoutName)
                        .whereEqualTo("BookDateSelected", formattedDate)
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    bookedHours.clear();
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                        String status = documentSnapshot.getString("bookingStatus");
                                        String RateType = documentSnapshot.getString("RateType");
                                        if ((status != null && (status.equals("Accepted") || status.equals("Ongoing"))) && (RateType != null && RateType.equals("Hourly rate"))) {
                                            Timestamp endTimeSelected = documentSnapshot.getTimestamp("BookEndTimeSelected");
                                            Timestamp startTimeSelected = documentSnapshot.getTimestamp("BookStartTimeSelected");

                                            if (endTimeSelected != null && startTimeSelected != null) {
                                                Date endTime = endTimeSelected.toDate();
                                                Date startTime = startTimeSelected.toDate();

                                                Calendar calendar = Calendar.getInstance();
                                                calendar.setTime(startTime);
                                                int startHour = calendar.get(Calendar.HOUR_OF_DAY);

                                                calendar.setTime(endTime);
                                                int endHour = calendar.get(Calendar.HOUR_OF_DAY);

                                                // Remove the booked hours from availableHours
                                                for (int i = startHour; i < endHour; i++) {
                                                    Calendar tempCalendar = (Calendar) calendar.clone();
                                                    tempCalendar.set(Calendar.HOUR_OF_DAY, i);
                                                    SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                                    String bookedHour = hourFormat.format(tempCalendar.getTime());
                                                    availableHours.remove(bookedHour);
                                                }

                                                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                                String formattedStartTime = timeFormat.format(startTime);
                                                String formattedEndTime = timeFormat.format(endTime);

                                                bookedHours.add(formattedStartTime + " - " + formattedEndTime);
                                            }
                                        } else if (RateType != null && RateType.equals("Daily rate")) {
                                            endTimeSelectedDaily = documentSnapshot.getTimestamp("BookEndTimeSelected");
                                            startTimeSelectedDaily = documentSnapshot.getTimestamp("BookStartTimeSelected");

                                            startDateDailyString = documentSnapshot.getString("BookDateSelected");
                                            endDateDailyString = documentSnapshot.getString("BookEndDateSelected");
                                            totalDaysDailyFromFirestore = documentSnapshot.getString("totalDays");
                                            totalDaysInt = Integer.parseInt(totalDaysDailyFromFirestore);

                                            if (endTimeSelectedDaily != null && startTimeSelectedDaily != null) {
                                                Date endTime = endTimeSelectedDaily.toDate();
                                                Date startTime = startTimeSelectedDaily.toDate();

                                                Calendar calendar = Calendar.getInstance();
                                                calendar.setTime(startTime);
                                                int startHour = calendar.get(Calendar.HOUR_OF_DAY);

                                                calendar.setTime(endTime);
                                                int endHour = calendar.get(Calendar.HOUR_OF_DAY);

                                                // Remove the booked hours from availableHours
                                                for (int i = startHour; i < endHour; i++) {
                                                    Calendar tempCalendar = (Calendar) calendar.clone();
                                                    tempCalendar.set(Calendar.HOUR_OF_DAY, i);
                                                    SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                                    String bookedHour = hourFormat.format(tempCalendar.getTime());
                                                    availableHours.remove(bookedHour);
                                                }

                                                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                                String formattedStartTime = timeFormat.format(startTime);
                                                String formattedEndTime = timeFormat.format(endTime);

                                                bookedHours.add(formattedStartTime + " - " + formattedEndTime);
                                                availableHours.clear();
                                            }
                                        }
                                    }

                                    boolean hasElevenPM = false;
                                    for (String booking : bookedHours) {
                                        String[] parts = booking.split(" - ");
                                        String startTime = parts[0];
                                        String endTime = parts[1];

                                        // Check if any hour between the start and end time falls on "11:00 PM"
                                        if (startTime.endsWith("11:00 pm") || endTime.endsWith("11:00 pm")) {
                                            hasElevenPM = true;
                                            break;
                                        }
                                    }

                                    if (hasElevenPM) {
                                        availableHours.remove("11:00 pm");
                                    }
                                }

                                if (totalDaysInt == 1 && formattedDate.equals(startDateDailyString)) {
                                    availableHours.clear();
                                } else {
                                    if (totalDaysInt > 1 && formattedDate.equals(startDateDailyString)) {
                                        availableHours.clear();
                                    } else if (totalDaysInt > 1 && formattedDate.equals(endDateDailyString)) {
                                        if (endTimeSelectedDaily != null && startTimeSelectedDaily != null){
                                            Date endTime = endTimeSelectedDaily.toDate();
                                            Date startTime = startTimeSelectedDaily.toDate();

                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTime(startTime);
                                            int startHour = calendar.get(Calendar.HOUR_OF_DAY);

                                            calendar.setTime(endTime);
                                            int endHour = calendar.get(Calendar.HOUR_OF_DAY);

                                            // Remove the booked hours from availableHours
                                            for (int i = startHour; i < endHour; i++) {
                                                Calendar tempCalendar = (Calendar) calendar.clone();
                                                tempCalendar.set(Calendar.HOUR_OF_DAY, i);
                                                SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                                String bookedHour = hourFormat.format(tempCalendar.getTime());
                                                availableHours.remove(bookedHour);
                                            }

                                            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                            String formattedStartTime = timeFormat.format(startTime);
                                            String formattedEndTime = timeFormat.format(endTime);

                                            bookedHours.add(formattedStartTime + " - " + formattedEndTime);
                                            availableHours.clear();
                                        }

                                    } else if (totalDaysInt > 1 && formattedDate.compareTo(startDateDailyString) > 0 && formattedDate.compareTo(endDateDailyString) < 0) {
                                        if (endTimeSelectedDaily != null && startTimeSelectedDaily != null){
                                            Date endTime = endTimeSelectedDaily.toDate();
                                            Date startTime = startTimeSelectedDaily.toDate();

                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTime(startTime);
                                            int startHour = calendar.get(Calendar.HOUR_OF_DAY);

                                            calendar.setTime(endTime);
                                            int endHour = calendar.get(Calendar.HOUR_OF_DAY);

                                            // Remove the booked hours from availableHours
                                            for (int i = startHour; i < endHour; i++) {
                                                Calendar tempCalendar = (Calendar) calendar.clone();
                                                tempCalendar.set(Calendar.HOUR_OF_DAY, i);
                                                SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                                String bookedHour = hourFormat.format(tempCalendar.getTime());
                                                availableHours.remove(bookedHour);
                                            }

                                            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                            String formattedStartTime = timeFormat.format(startTime);
                                            String formattedEndTime = timeFormat.format(endTime);

                                            bookedHours.add(formattedStartTime + " - " + formattedEndTime);
                                            availableHours.clear();
                                        }
                                    }
                                }

                                if (availableHours.size() == 1) {
                                    String lastHour = availableHours.get(0);
                                    String nextHour = getNextHour(lastHour);
                                    // Add the next hour to availableHours regardless of whether it is booked or not
                                    availableHours.add(nextHour);
                                }

                                RecyclerView recyclerView2 = rootView.findViewById(R.id.availableTime_Recylerview);
                                TimeSlotsAdapter adapter = new TimeSlotsAdapter(availableHours, false);
                                recyclerView2.setAdapter(adapter);

                                if (availableHours.isEmpty()) {
                                    noBookingsaAvailableTextview.setVisibility(View.VISIBLE);
                                    Toast.makeText(getContext(), "Date Fully booked. Please select another date", Toast.LENGTH_SHORT).show();
                                    changeTextForDateSelectedTextview.performClick();
                                } else {
                                    noBookingsaAvailableTextview.setVisibility(View.GONE);
                                }

                                if (bookedHours != null && !bookedHours.isEmpty()) {
                                    List<String> sortedBookedHours = sortBookedHours(bookedHours);

                                    RecyclerView recyclerView = rootView.findViewById(R.id.acceptedBookingsTime_Recylerview);
                                    TimeSlotsAdapter adapter2 = new TimeSlotsAdapter(sortedBookedHours, true);
                                    recyclerView.setAdapter(adapter2);
                                    noBookingsNoteTextview.setVisibility(View.GONE);
                                } else {
                                    List<String> sortedBookedHours = sortBookedHours(bookedHours);

                                    RecyclerView recyclerView = rootView.findViewById(R.id.acceptedBookingsTime_Recylerview);
                                    TimeSlotsAdapter adapter2 = new TimeSlotsAdapter(sortedBookedHours, true);
                                    recyclerView.setAdapter(adapter2);
                                    noBookingsNoteTextview.setText("No accepted bookings on this date");
                                    noBookingsNoteTextview.setVisibility(View.VISIBLE);
                                }

                            }
                        });



            }
        });


        bookNowAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePlots_LinearLayout.setVisibility(View.GONE);
                selectDatesAndTimeLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        selectedDateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePlots_LinearLayout.setVisibility(View.VISIBLE);
            }
        });
        selectedDateTextview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedEndDateTextview.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectedEndDateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setTitle("Fetching possible end dates");
                progressDialog.show();
                // Get the currently selected start date from the selectedDateTextview
                String selectedStartDate = selectedDateTextview.getText().toString();

                // Parse the selected start date into a Calendar object
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
                Calendar startDate = Calendar.getInstance();
                try {
                    startDate.setTime(dateFormat.parse(selectedStartDate));

                    // Create a list of dates for the ListView adapter, excluding the start date
                    List<String> dateList = new ArrayList<>();
                    for (int i = 1; i <= 5; i++) {
                        Calendar nextDate = (Calendar) startDate.clone();
                        nextDate.add(Calendar.DAY_OF_MONTH, i);
                        dateList.add(dateFormat.format(nextDate.getTime()));
                    }

                    // Query the booked dates from the database
                    List<String> bookedDates = new ArrayList<>();
                    List<String> availableDates = new ArrayList<>();  // Updated: Store available dates

                    // Define an AtomicBoolean to track if a booking is found within the 5-day range
                    final AtomicBoolean bookingFound = new AtomicBoolean(false);

                    // Create a list of tasks for each query
                    List<Task<QuerySnapshot>> queryTasks = new ArrayList<>();

                    for (String date : dateList) {
                        Task<QuerySnapshot> queryTask = submittedBookingsCollection
                                .whereEqualTo("layoutName", layoutName)
                                .whereEqualTo("BookDateSelected", date)
                                .get();

                        queryTasks.add(queryTask);
                    }

                    // Wait for all query tasks to complete
                    Tasks.whenAllSuccess(queryTasks)
                            .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                @Override
                                public void onSuccess(List<Object> results) {
                                    for (Object result : results) {
                                        QuerySnapshot querySnapshot = (QuerySnapshot) result;
                                        if (!querySnapshot.isEmpty()) {
                                            // The date is booked
                                            String bookedDate = dateList.get(results.indexOf(result));
                                            bookedDates.add(bookedDate);
                                            bookingFound.set(true);
                                        } else {
                                            // The date is available
                                            if (!bookingFound.get()) {
                                                String availableDate = dateList.get(results.indexOf(result));
                                                availableDates.add(availableDate);
                                            }
                                        }
                                    }

                                    // Sort the available dates in ascending order
                                    Collections.sort(availableDates, new Comparator<String>() {
                                        DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());

                                        @Override
                                        public int compare(String date1, String date2) {
                                            try {
                                                Date d1 = dateFormat.parse(date1);
                                                Date d2 = dateFormat.parse(date2);
                                                return d1.compareTo(d2);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            return 0;
                                        }
                                    });

                                    progressDialog.dismiss();
                                    // Show the AlertDialog with available dates
                                    showAvailableDatesDialog(availableDates);
                                }
                            });

                } catch (ParseException e) {
                    e.printStackTrace();
                    // Handle date parsing error
                }
            }
        });

        selectedStartTimeString = selectedStartTimeTextview.getText().toString();
        selectedStartTimeTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Start Time");

                // Convert the available hours list to an array
                final String[] availableHoursArray = availableHours.toArray(new String[0]);

                builder.setItems(availableHoursArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedStartTime = availableHoursArray[which];
                        String selectedEndTime = selectedEndTimeTextview.getText().toString();
                        selectedStartTimeTextview.setText(selectedStartTime);

                        if (!selectedEndTime.isEmpty() && isStartTimeAfterEndTime(selectedStartTime, selectedEndTime)) {
                            Toast.makeText(getContext(), "Start Time should be before end time!", Toast.LENGTH_SHORT).show();
                            selectedStartTimeTextview.setText(""); // Reset the selected start time
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        selectedEndTimeString = selectedEndTimeTextview.getText().toString();

        selectedEndTimeTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedStartTime = selectedStartTimeTextview.getText().toString();

                if (selectedStartTime.isEmpty()) {
                    Toast.makeText(getContext(), "Start Time should be selected first!", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select End Time");

                // Convert the available hours list to an array
                final String[] availableHoursArray = availableHours.toArray(new String[0]);

                // Get the index of the selected start time
                int selectedStartTimeIndex = -1;
                if (selectedStartTime != null) {
                    selectedStartTimeIndex = Arrays.asList(availableHoursArray).indexOf(selectedStartTime);
                }

                // Create a new list of available end times based on the selected start time
                List<String> availableEndTimes = new ArrayList<>();
                if (selectedStartTimeIndex >= 0 && selectedStartTimeIndex < availableHoursArray.length - 1) {
                    // Add the available hours after the selected start time as end times
                    availableEndTimes.addAll(Arrays.asList(availableHoursArray).subList(selectedStartTimeIndex + 1, availableHoursArray.length));
                }

                builder.setItems(availableEndTimes.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedEndTime = availableEndTimes.get(which);
                        if (isEndTimeBeforeStartTime(selectedEndTime, selectedStartTime)) {
                            Toast.makeText(getContext(), "End Time should be after start time!", Toast.LENGTH_SHORT).show();
                            selectedEndTimeTextview.setText("");
                        } else {
                            selectedEndTimeTextview.setText(selectedEndTime);
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //initializing visibilities of linear layouts
        selectDatesAndTimeLinearLayout.setVisibility(View.GONE);
        rateDetailsLinearLayout.setVisibility(View.GONE);
        selectStartTimeLinearLayout.setVisibility(View.GONE);
        selectEndTimeLinearLayout.setVisibility(View.GONE);
        totalDurationLinearLayout.setVisibility(View.GONE);
        totalPaymentLinearLayout.setVisibility(View.GONE);
        custBookingDetsLinearLayout.setVisibility(View.GONE);
        branchPaymentChannelsLinearLayout.setVisibility(View.GONE);
        uploadPaymentImageLinearLayout.setVisibility(View.GONE);


        OnsiteRadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPaymentOption = OnsiteRadButton.getText().toString();
                Toast.makeText(getContext(), "Selected Onsite Payment", Toast.LENGTH_SHORT).show();
                branchPaymentChannelsLinearLayout.setVisibility(View.GONE);
                uploadPaymentImageLinearLayout.setVisibility(View.GONE);
            }
        });

        OtherOptRadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPaymentOption = OtherOptRadButton.getText().toString();
                branchPaymentChannelsLinearLayout.setVisibility(View.VISIBLE);
                uploadPaymentImageLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        readTermsTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Booking Cancellation Terms and Agreement");
                builder.setMessage("Cancellation Policy:\n\n" +
                        "1. Cancellations made 24 hours or more prior to the booking will not be eligible for a refund.\n" +
                        "2. Cancellations made less than 24 hours prior to the booking will receive a full refund.\n" +
                        "3. In case of a 'No Show,' the full booking amount will be charged.\n\n" +
                        "By clicking 'Accept,' you agree to the above cancellation policy.");

                // Add an "Accept" button to close the dialog
                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AgreeTermsCheckbox.setChecked(true);
                        dialog.dismiss();
                    }
                });

                // Add a "Cancel" button to dismiss the dialog
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        UploadButtonPaymentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });

        rateDetailsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButtonGroupForRates.setVisibility(View.VISIBLE);
                selectRateTitleTextview.setText("Select rate type");
            }
        });


        return rootView;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            filepath = data.getData();
            ProofPaymentImage.setImageURI(filepath);
        }
    }

    public void clearInputs(){
        selectedStartTimeTextview.setText("");
        selectedEndTimeTextview.setText("");
        //totalHoursTextview.setText("");
        totalPaymentTextview.setText("");
        custTenantsNumEdittext.setText("");
        AgreeTermsCheckbox.setChecked(false);
        uploadPaymentImageLinearLayout.setVisibility(View.GONE);
        branchPaymentChannelsLinearLayout.setVisibility(View.GONE);
        custBookingDetsLinearLayout.setVisibility(View.GONE);
    }

    // Function to show the AlertDialog with available dates
    private void showAvailableDatesDialog(List<String> availableDates) {
        // Sort the available dates in ascending order
        Collections.sort(availableDates, new Comparator<String>() {
            DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
            @Override
            public int compare(String date1, String date2) {
                try {
                    Date d1 = dateFormat.parse(date1);
                    Date d2 = dateFormat.parse(date2);
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        // Create an AlertDialog with the ListView
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("List of available end dates from the selected start date.")
                .setItems(availableDates.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedEndDate = availableDates.get(which);
                        // Update the selected end date in selectedEndDateFromCalendarTextview
                        selectedEndDateTextview.setText(selectedEndDate);
                    }
                })
                .setNegativeButton("Cancel", null);

        // Show the AlertDialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.setIcon(R.drawable.logocaspace);
        dialog.show();
    }

    private List<String> sortBookedHours(List<String> bookedHours) {
        List<String> sortedBookedHours = new ArrayList<>(bookedHours);
        Collections.sort(sortedBookedHours, new Comparator<String>() {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

            @Override
            public int compare(String booking1, String booking2) {
                try {
                    String[] parts1 = booking1.split(" - ");
                    String[] parts2 = booking2.split(" - ");

                    Date startTime1 = timeFormat.parse(parts1[0]);
                    Date startTime2 = timeFormat.parse(parts2[0]);

                    return startTime1.compareTo(startTime2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0;
            }
        });

        return sortedBookedHours;
    }

    private String getNextHour(String hour) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        try {
            Date date = timeFormat.parse(hour);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, 1);
            return timeFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isEndTimeBeforeStartTime(String endTime, String startTime) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        try {
            Date endDate = format.parse(endTime);
            Date startDate = format.parse(startTime);
            return endDate.before(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isStartTimeAfterEndTime(String startTime, String endTime) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        try {
            Date start = format.parse(startTime);
            Date end = format.parse(endTime);
            return start.after(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void getLayoutDetails(){
        OfficeLayoutsRef.whereEqualTo("layoutName", layoutName)
                        .whereEqualTo("owner_id", ownerId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                hourlyRate = documentSnapshot.getString("layoutHourlyPrice");
                                dailyRate = documentSnapshot.getString("layoutDailyPrice");
                                weeklyRate = documentSnapshot.getString("layoutWeeklyPrice");
                                monthlyRate = documentSnapshot.getString("layoutMonthlyPrice");
                                int minPersonCap = Integer.parseInt(documentSnapshot.getString("minCapacity"));
                                int maxPersonCap = Integer.parseInt(documentSnapshot.getString("maxCapacity"));

                                //OnClick radio buttons fro rate type
                                hourlyRadioButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        HourlyCalculation(hourlyRate, minPersonCap, maxPersonCap);

                                        rateDetailsLinearLayout.setVisibility(View.VISIBLE);
                                        rateType ="Hourly rate";
                                        selectedRateTypeTextview.setText(rateType);
                                        selectedRatePriceTextview.setText(hourlyRate);
                                        selectStartTimeLinearLayout.setVisibility(View.VISIBLE);
                                        selectedStartTimeTextview.setText("");
                                        selectEndTimeLinearLayout.setVisibility(View.VISIBLE);
                                        selectedEndTimeTextview.setText("");
                                        selectEndDateLinearLayout.setVisibility(View.GONE);
                                        selectedEndDateTextview.setText("");
                                        addButtonForDaysImageButton.setVisibility(View.GONE);
                                        totalCalcDurationTextview.setText("");
                                        totalCalcDurationTitleTextview.setText("Total Hours:");
                                        totalPaymentTextview.setText("");
                                    }
                                });

                                dailyRadioButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DailyCalculation(dailyRate, minPersonCap, maxPersonCap);

                                        rateDetailsLinearLayout.setVisibility(View.VISIBLE);
                                        rateType = "Daily rate";
                                        selectedRateTypeTextview.setText(rateType);
                                        selectedRatePriceTextview.setText(dailyRate);
                                        selectStartTimeLinearLayout.setVisibility(View.GONE);
                                        selectedStartTimeTextview.setText("");
                                        selectEndTimeLinearLayout.setVisibility(View.GONE);
                                        selectedEndTimeTextview.setText("");
                                        totalPaymentTextview.setText("");
                                    }
                                });

                                weeklyRadioButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        rateDetailsLinearLayout.setVisibility(View.VISIBLE);
                                        selectedRateTypeTextview.setText("Weekly rate");
                                        selectedRatePriceTextview.setText(weeklyRate);
                                    }
                                });

                                monthlyRadioButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        rateDetailsLinearLayout.setVisibility(View.VISIBLE);
                                        selectedRateTypeTextview.setText("Monthly rate");
                                        selectedRatePriceTextview.setText(monthlyRate);
                                    }
                                });

                            }

                        }
                    }
                });



    }

    public void getCustomerDetails(){
        CustomerAccountsRef.whereEqualTo("customersIDNum", currentUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                String custFirstname = documentSnapshot.getString("customersFirstName");
                                String custLastname = documentSnapshot.getString("customersLastName");
                                customerEmail = documentSnapshot.getString("customersEmail");
                                customerOrgName = documentSnapshot.getString("customersOrganization");

                                custFullnameEdittext.setText(custFirstname + custLastname);
                                custEmailEdittext.setText(customerEmail);
                                custOrgnameEdittext.setText(customerOrgName);

                            }
                        }
                    }
                });
    }

    public void HourlyCalculation(String hourlyRate, int minPersonCap, int maxPersonCap){

        //clearInputs();

        if (!selectedRatePriceTextview.getText().toString().isEmpty() && !selectedRateTypeTextview.getText().toString().isEmpty()){
            RadioButtonGroupForRates.setVisibility(View.GONE);
            selectRateTitleTextview.setText("SELECTED RATE TYPE AND PRICE");
        }

        selectedStartTimeTextview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String startTime = selectedStartTimeTextview.getText().toString();
                String endTime = selectedEndTimeTextview.getText().toString();
                SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                try {
                    Date startDate = format.parse(startTime);
                    Date endDate = format.parse(endTime);

                    long timeDifferenceInMillis = endDate.getTime() - startDate.getTime();
                    long totalHours = timeDifferenceInMillis / (60 * 60 * 1000);
                    double totalPayment = totalHours * Double.parseDouble(hourlyRate);

                    totalDurationLinearLayout.setVisibility(View.VISIBLE);
                    totalPaymentLinearLayout.setVisibility(View.VISIBLE);
                    totalCalcDurationTitleTextview.setText("Total Hours:");
                    totalCalcDurationTextview.setText(String.format(Locale.getDefault(), "%d", totalHours));
                    totalPaymentTextview.setText(String.format(Locale.getDefault(), "₱%.2f", totalPayment));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectedEndTimeTextview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String startTime = selectedStartTimeTextview.getText().toString();
                String endTime = selectedEndTimeTextview.getText().toString();
                SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                try {
                    Date startDate = format.parse(startTime);
                    Date endDate = format.parse(endTime);

                    long timeDifferenceInMillis = endDate.getTime() - startDate.getTime();
                    long totalHours = timeDifferenceInMillis / (60 * 60 * 1000);
                    double totalPayment = totalHours * Double.parseDouble(hourlyRate);

                    totalDurationLinearLayout.setVisibility(View.VISIBLE);
                    totalPaymentLinearLayout.setVisibility(View.VISIBLE);
                    totalCalcDurationTitleTextview.setText("Total Hours:");
                    totalCalcDurationTextview.setText(String.format(Locale.getDefault(), "%d", totalHours));
                    totalPaymentTextview.setText(String.format(Locale.getDefault(), "₱%.2f", totalPayment));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        totalPaymentTextview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                custBookingDetsLinearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        custTenantsNumEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                if (!input.isEmpty()) {
                    if (!TextUtils.isDigitsOnly(input)) {
                        s.clear();
                        custTenantsNumEdittext.setError("Please enter only numbers");
                    } else {
                        int numTenants = Integer.parseInt(input);
                        if (numTenants > maxPersonCap || numTenants < 1) {
                            s.clear();
                            custTenantsNumEdittext.setError("Person capacity is at " + minPersonCap + " to " + maxPersonCap);
                        }
                    }
                }
            }
        });

        SubmitBookingDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCompleteCustomerDetails();
                if (OtherOptRadButton.isChecked() && !AgreeTermsCheckbox.isChecked()){
                    Toast.makeText(getContext(), "Please check the Terms and Agreement", Toast.LENGTH_SHORT).show();
                    return;
                }else if (OnsiteRadButton.isChecked()){
                    storeBookingDetails();
                }else if (OtherOptRadButton.isChecked() && AgreeTermsCheckbox.isChecked()) {
                    if (filepath == null) {
                        Toast.makeText(getContext(), "Please upload proof of payment", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    storeBookingDetails();
                }
            }
        });

    }

    public void DailyCalculation(String dailyRate, int minPersonCap, int maxPersonCap) {
        //clearInputs();

        if (selectedRatePriceTextview.getText().toString().isEmpty() ||
                selectedRateTypeTextview.getText().toString().isEmpty() ||
                selectedDateTextview.getText().toString().isEmpty()) {
            return;
        }

        RadioButtonGroupForRates.setVisibility(View.GONE);
        selectRateTitleTextview.setText("SELECTED RATE TYPE AND PRICE");
        String selectedStartDate = selectedDateTextview.getText().toString();
        String selectedEndDate = selectedEndDateTextview.getText().toString();

        final int totalDays; // Declare as final
        final double bookingFee; // Declare as final

        // Check if there are any booked hours for the selected date
        boolean hasBookedHours = false;
        if (selectedStartDate.equals(formattedDate)) {
            if (bookedHours != null && !bookedHours.isEmpty()) {
                hasBookedHours = true;
            }
        }

        if (hasBookedHours) {
            Toast.makeText(getContext(), "Cannot book for daily rate. There are existing hourly bookings on this date.", Toast.LENGTH_SHORT).show();
            return; // Exit the method to prevent further processing
        }

        totalDays = 1; // Assuming the booking is for one day

        double rate = Double.parseDouble(dailyRate);
        bookingFee = rate * totalDays;

        totalDurationLinearLayout.setVisibility(View.VISIBLE);
        totalCalcDurationTitleTextview.setText("Total Days:");
        totalCalcDurationTextview.setText(String.valueOf(totalDays));
        totalPaymentLinearLayout.setVisibility(View.VISIBLE);
        addButtonForDaysImageButton.setVisibility(View.VISIBLE);
        selectEndDateLinearLayout.setVisibility(View.GONE);

        addButtonForDaysImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDateTitleTextview.setText("Start Date:");
                selectEndDateLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        deleteButtonForDaysImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectEndDateLinearLayout.setVisibility(View.GONE);
                selectedEndDateTextview.setText("");
                totalCalcDurationTextview.setText("1");
                int rate = Integer.parseInt(dailyRate);
                double calculatedBookingFee = rate * 1;
                totalPaymentTextview.setText(String.format(Locale.getDefault(), "₱%.2f",calculatedBookingFee));
            }
        });

        selectedEndDateTextview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newEndDate = s.toString();
                if (!newEndDate.isEmpty()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
                    try {
                        Date startDate = dateFormat.parse(selectedStartDate);
                        Date endDate = dateFormat.parse(newEndDate);
                        long durationMillis = endDate.getTime() - startDate.getTime();
                        int calculatedTotalDays = (int) TimeUnit.MILLISECONDS.toDays(durationMillis) + 1;

                        double calculatedBookingFee = rate * calculatedTotalDays;

                        totalCalcDurationTextview.setText(String.valueOf(calculatedTotalDays));
                        totalPaymentTextview.setText(String.format(Locale.getDefault(), "₱%.2f", calculatedBookingFee));
                        custBookingDetsLinearLayout.setVisibility(View.VISIBLE);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        // Handle date parsing error
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        custTenantsNumEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                if (!input.isEmpty()) {
                    if (!TextUtils.isDigitsOnly(input)) {
                        s.clear();
                        custTenantsNumEdittext.setError("Please enter only numbers");
                    } else {
                        int numTenants = Integer.parseInt(input);
                        if (numTenants > maxPersonCap || numTenants < 1) {
                            s.clear();
                            custTenantsNumEdittext.setError("Person capacity is at " + minPersonCap + " to " + maxPersonCap);
                        }
                    }
                }
            }
        });

        SubmitBookingDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCompleteCustomerDetails();
                if (OtherOptRadButton.isChecked() && !AgreeTermsCheckbox.isChecked()){
                    Toast.makeText(getContext(), "Please check the Terms and Agreement", Toast.LENGTH_SHORT).show();
                    return;
                }else if (OnsiteRadButton.isChecked()){
                    storeBookingDetails();
                }else if (OtherOptRadButton.isChecked() && AgreeTermsCheckbox.isChecked()) {
                    if (filepath == null) {
                        Toast.makeText(getContext(), "Please upload proof of payment", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    storeBookingDetails();
                }
            }
        });
    }

    public void checkCompleteCustomerDetails(){

        if (custFullnameEdittext.getText().toString().isEmpty()){
            custFullnameEdittext.setError("Customer Fullname should not be empty.");
            custFullnameEdittext.requestFocus();
            return;
        }
        if (custOrgnameEdittext.getText().toString().isEmpty()){
            custOrgnameEdittext.setError("Customer organization name should not be empty.");
            custOrgnameEdittext.requestFocus();
            return;
        }
        if (custTenantsNumEdittext.getText().toString().isEmpty()){
            custTenantsNumEdittext.setError("Number of tenants should not be empty.");
            custTenantsNumEdittext.requestFocus();
            return;
        }
        if (custPhoneNumEdittext.getText().toString().isEmpty()){
            custPhoneNumEdittext.setError("Customer Phone Number should not be empty.");
            custPhoneNumEdittext.requestFocus();
            return;
        }
        if (custEmailEdittext.getText().toString().isEmpty()){
            custEmailEdittext.setError("Customer email should not be empty.");
            custEmailEdittext.requestFocus();
            return;
        }
        if (custAddressEdittext.getText().toString().isEmpty()){
            custAddressEdittext.setError("Customer address should not be empty");
            custAddressEdittext.requestFocus();
            return;
        }
        if (!OnsiteRadButton.isChecked() && !OtherOptRadButton.isChecked()){
            OnsiteRadButton.requestFocus();
            OtherOptRadButton.requestFocus();
            Toast.makeText(getContext(), "Please select a payment option to proceed", Toast.LENGTH_SHORT).show();
        }
    }

    public void storeBookingDetails(){

        Toast.makeText(getContext(), "Please review submitted details", Toast.LENGTH_SHORT).show();

        //submittedBookingsCollection
        String selectedDate = selectedDateTextview.getText().toString();
        String selectedEndDate = selectedEndDateTextview.getText().toString();
        String selectedRateType = selectedRateTypeTextview.getText().toString();
        String selectedRatePrice = selectedRatePriceTextview.getText().toString();

        String selectedStartTimeString = selectedStartTimeTextview.getText().toString();
        String selectedEndTimeString = selectedEndTimeTextview.getText().toString();
        String totalCalcDurationString = totalCalcDurationTextview.getText().toString();//can be hours, days, weeks, months
        String totalPaymentString = totalPaymentTextview.getText().toString();

        String customerId = currentUser.getUid();
        String customerFullnameString = custFullnameEdittext.getText().toString();
        String custOrgNameString = custOrgnameEdittext.getText().toString();
        String numOfTenantsString = custTenantsNumEdittext.getText().toString();
        String custPhoneNumString = custPhoneNumEdittext.getText().toString();
        String custEmailString = custEmailEdittext.getText().toString();
        String custAddressString = custAddressEdittext.getText().toString();
        String paymentOptionString = selectedPaymentOption;
        String paymentImageString;

        if (customerFullnameString!=null && custOrgNameString != null && numOfTenantsString != null && custPhoneNumString != null &&
            custEmailString != null && custAddressString != null && paymentOptionString != null){

            TextView fullname, orgName, numTenants, phoneNum, email, address, rateType, ratePrice, bookStartDate, endDateTitleTextview, bookEndDate,
                    bookingStartTimeTitle, startTime, bookingEndTimeTitle, endTime, totalDurationDetailsTitle, totalDuration, totalDays, totalWeeks, totalMonths, totalPay, paymentOption, startDateTitleTextview;

            android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(getContext());
            android.app.AlertDialog dialog;
            final View custReviewDetails = getLayoutInflater().inflate(R.layout.enter_customer_detailsforbooking_popup, null);

            rateType = (TextView) custReviewDetails.findViewById(R.id.rateTypePopup);
            ratePrice = (TextView) custReviewDetails.findViewById(R.id.rateValuePopup);
            startDateTitleTextview = (TextView) custReviewDetails.findViewById(R.id.startDateTitle_Textview);
            bookStartDate = (TextView) custReviewDetails.findViewById(R.id.bookingStartDatePopup);
            endDateTitleTextview = (TextView) custReviewDetails.findViewById(R.id.endDateTitle_Textview);
            bookEndDate = (TextView) custReviewDetails.findViewById(R.id.bookingEndDatePopup);
            bookingStartTimeTitle = (TextView) custReviewDetails.findViewById(R.id.bookingStartTimeTitlePopup);
            startTime = (TextView) custReviewDetails.findViewById(R.id.bookingStartTimePopup);
            bookingEndTimeTitle = (TextView) custReviewDetails.findViewById(R.id.bookingEndTimeTitlePopup);
            endTime = (TextView) custReviewDetails.findViewById(R.id.bookingEndTimePopup);
            totalDurationDetailsTitle = (TextView) custReviewDetails.findViewById(R.id.totalHoursDetailsTitle);
            totalDuration = (TextView) custReviewDetails.findViewById(R.id.totalHoursPopup);
            totalDays = (TextView) custReviewDetails.findViewById(R.id.totalDaysPopup);
            totalWeeks = (TextView) custReviewDetails.findViewById(R.id.totalWeeksPopup);
            totalMonths = (TextView) custReviewDetails.findViewById(R.id.totalMonthsPopup);
            paymentOption = (TextView) custReviewDetails.findViewById(R.id.CustPaymentOption_Textview);
            totalPay = (TextView) custReviewDetails.findViewById(R.id.totalPaymentPopup);
            fullname = (TextView) custReviewDetails.findViewById(R.id.customerFullName_Textview);
            orgName = (TextView) custReviewDetails.findViewById(R.id.organizationName_Textview);
            numTenants = (TextView) custReviewDetails.findViewById(R.id.NoOfTenants_Textview);
            phoneNum = (TextView) custReviewDetails.findViewById(R.id.CustPhoneNumber_Textview);
            email = (TextView) custReviewDetails.findViewById(R.id.CustEmail_Textview);
            address = (TextView) custReviewDetails.findViewById(R.id.CustAddress_Textview);
            TextView CustProofOfPaymentTitle = (TextView) custReviewDetails.findViewById(R.id.CustProofOfPaymentTitle_Textview);
            ImageView paymentPic = (ImageView) custReviewDetails.findViewById(R.id.CustProofOfPayment_Imageview);
            AppCompatButton confirmButton = (AppCompatButton) custReviewDetails.findViewById(R.id.confirmBooking_ButtonPopup);
            AppCompatButton cancelButton = (AppCompatButton) custReviewDetails.findViewById(R.id.cancelBooking_ButtonPopup);

            if (selectedPaymentOption.equals("Onsite")){
                CustProofOfPaymentTitle.setVisibility(View.GONE);
                paymentPic.setVisibility(View.GONE);
            }

            fullname.setText(customerFullnameString);
            orgName.setText(custOrgNameString);
            numTenants.setText(numOfTenantsString);
            phoneNum.setText(custPhoneNumString);
            email.setText(custEmailString);
            address.setText(custAddressString);
            paymentOption.setText(paymentOptionString);

            rateType.setText(selectedRateType);
            ratePrice.setText("₱"+selectedRatePrice);
            startTime.setText(selectedStartTimeString);
            endTime.setText(selectedEndTimeString);
            totalDuration.setText(totalCalcDurationString);
            totalPay.setText(totalPaymentString);

            if (selectedRateType.equals("Hourly rate")){
                startDateTitleTextview.setText("Booking Date");
                bookStartDate.setText(selectedDate);
                endDateTitleTextview.setVisibility(View.GONE);
                endDateTitleTextview.setVisibility(View.GONE);
                totalDurationDetailsTitle.setText("Total Hours:");
            }

            if (selectedRateType.equals("Daily rate")){
                totalDurationDetailsTitle.setText("Total Days:");
                startDateTitleTextview.setText("Start Date");
                bookStartDate.setText(selectedDate);
                endDateTitleTextview.setVisibility(View.VISIBLE);
                bookEndDate.setVisibility(View.VISIBLE);
                bookEndDate.setText(selectedEndDate);

                if (totalCalcDurationString.equals("1")){
                    totalDurationDetailsTitle.setText("Total Days:");
                    endDateTitleTextview.setVisibility(View.GONE);
                    bookEndDate.setVisibility(View.GONE);

                    totalDuration.setText("1");
                    SimpleDateFormat timeFormat24 = new SimpleDateFormat("HH:mm", Locale.US);
                    SimpleDateFormat timeFormat12 = new SimpleDateFormat("hh:mm a", Locale.US);

                    // Set the start time to the first hour (0:00) of the selected date
                    try {
                        Date startTimeDate = timeFormat24.parse("00:00");
                        startTime.setText(timeFormat12.format(startTimeDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // Set the end time to the last hour (23:59) of the selected date
                    try {
                        Date endTimeDate = timeFormat24.parse("23:59");
                        endTime.setText(timeFormat12.format(endTimeDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    bookingStartTimeTitle.setVisibility(View.GONE);
                    startTime.setVisibility(View.GONE);
                    bookingEndTimeTitle.setVisibility(View.GONE);
                    endTime.setVisibility(View.GONE);
                }



            }

            if (filepath!=null && selectedPaymentOption.equals("Other Payment Platform")){
                Picasso.get().load(filepath).into(paymentPic);
            }

            dialogBuilder.setView(custReviewDetails);
            dialog = dialogBuilder.create();
            dialog.show();

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                }
            });

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Submitting your booking details", Toast.LENGTH_SHORT).show();
                    progressDialog.show();
                    if (paymentOptionString.equals("Other Payment Platform")){
                        uploadPaymentPicAndsubmitBookingDetails();
                        dialog.dismiss();
                    }else{
                        submitBookingDetails();
                        dialog.dismiss();
                    }

                }
            });
        }

    }

    public void uploadPaymentPicAndsubmitBookingDetails(){
        if (filepath!=null){
            StorageReference path = firebaseStorage.getReference().child("ProofOfPayment").child(filepath.getLastPathSegment());
            path.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                uploadedPaymentString = task.getResult().toString();
                                submitBookingDetails();
                            }
                        }
                    });
                }
            });
        }
    }

    public void submitBookingDetails(){
        progressDialog.setIcon(R.drawable.logocaspace);
        progressDialog.setTitle("Submitting booking details");
        progressDialog.show();

        String bookingStatus = null;
        if (selectedPaymentOption.equals("Other Payment Platform")){
            bookingStatus = "Pending";
        }
        if (selectedPaymentOption.equals("Onsite")){
            bookingStatus = "Accepted";
            uploadedPaymentString = "";
        }

        Timestamp bookDateTimestamp = null,bookStartTimeSelected = null, bookEndTimeSelected = null;
        String totalHours = "", totalDays="";

        if (rateType.equals("Hourly rate")){
            totalHours = totalCalcDurationTextview.getText().toString();
            totalDays = "";

            String selectedDate = selectedDateTextview.getText().toString();
            String startTime = selectedStartTimeTextview.getText().toString(); // Assuming you have a TextView for start time
            String endTime = selectedEndTimeTextview.getText().toString();

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
            try {
                Date date = dateFormat.parse(selectedDate);
                bookDateTimestamp = new Timestamp(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            try {
                Date startTimeDate = timeFormat.parse(startTime);
                bookStartTimeSelected = new Timestamp(startTimeDate);

                Calendar cal = Calendar.getInstance();
                cal.setTime(startTimeDate);
                cal.set(Calendar.YEAR, bookDateTimestamp.toDate().getYear() + 1900);
                cal.set(Calendar.MONTH, bookDateTimestamp.toDate().getMonth());
                cal.set(Calendar.DAY_OF_MONTH, bookDateTimestamp.toDate().getDate());
                bookStartTimeSelected = new Timestamp(cal.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                Date endTimeDate = timeFormat.parse(endTime);
                bookEndTimeSelected = new Timestamp(endTimeDate);

                Calendar cal = Calendar.getInstance();
                cal.setTime(endTimeDate);
                cal.set(Calendar.YEAR, bookDateTimestamp.toDate().getYear() + 1900);
                cal.set(Calendar.MONTH, bookDateTimestamp.toDate().getMonth());
                cal.set(Calendar.DAY_OF_MONTH, bookDateTimestamp.toDate().getDate());
                bookEndTimeSelected = new Timestamp(cal.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        String selectedEndDate = null;
        if (rateType.equals("Daily rate")) {
            String selectedDate = selectedDateTextview.getText().toString();

            totalHours = "";
            totalDays = totalCalcDurationTextview.getText().toString();

            if (totalDays.equals("1")) {
                selectedEndDate = selectedDate;

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
                try {
                    Date date = dateFormat.parse(selectedDate);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);

                    // Set the start and end time to the same day
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    bookStartTimeSelected = new Timestamp(cal.getTime());
                    bookEndTimeSelected = new Timestamp(cal.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                selectedEndDate = selectedEndDateTextview.getText().toString();

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
                try {
                    Date startDate = dateFormat.parse(selectedDate);
                    Date endDate = dateFormat.parse(selectedEndDate);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(startDate);

                    // Set the start time to the first hour of the start date
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    bookStartTimeSelected = new Timestamp(cal.getTime());

                    cal.setTime(endDate);

                    // Set the end time to the last hour of the end date
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    bookEndTimeSelected = new Timestamp(cal.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


        Map<String,Object> bookingDetails = new HashMap<>();
        bookingDetails.put("bookingId", "");
        bookingDetails.put("bookSubmittedDate", Timestamp.now());
        bookingDetails.put("BookDateSelected", selectedDateTextview.getText().toString());
        bookingDetails.put("BookEndDateSelected", selectedEndDate);
        bookingDetails.put("RateType", rateType);
        bookingDetails.put("ratePrice", selectedRatePriceTextview.getText().toString());
        bookingDetails.put("proofOfPayment", uploadedPaymentString);
        bookingDetails.put("bookingStatus", bookingStatus);
        bookingDetails.put("layoutAvailability", ""); // todo: availability
        bookingDetails.put("layoutName", layoutName);
        bookingDetails.put("layoutImage", layoutImage);
        bookingDetails.put("branchImage", branchImage);
        bookingDetails.put("branchName", branchName);
        bookingDetails.put("BookEndTimeSelected", bookEndTimeSelected);
        bookingDetails.put("BookStartTimeSelected", bookStartTimeSelected);
        bookingDetails.put("customerId", currentUser.getUid());
        bookingDetails.put("ownerId", ownerId);
        bookingDetails.put("customerFullname", custFullnameEdittext.getText().toString());
        bookingDetails.put("organizationName", custOrgnameEdittext.getText().toString());
        bookingDetails.put("numOfTenants", custTenantsNumEdittext.getText().toString());
        bookingDetails.put("customerPhoneNum", custPhoneNumEdittext.getText().toString());
        bookingDetails.put("customerEmail", custEmailEdittext.getText().toString());
        bookingDetails.put("customerAddress", custAddressEdittext.getText().toString());
        bookingDetails.put("paymentOption", selectedPaymentOption);
        bookingDetails.put("totalPayment", totalPaymentTextview.getText().toString());
        bookingDetails.put("totalHours", totalHours);
        bookingDetails.put("totalDays", totalDays);

        submittedBookingsCollection.add(bookingDetails)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentReference.update("bookingId", documentReference.getId())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Booking submitted!", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                        startActivity(new Intent(getContext(), HomeFragment.class));

                                    }
                                });
                            }
                        });

    }

    private class TimeSlotsAdapter extends RecyclerView.Adapter<TimeSlotsAdapter.TimeSlotViewHolder> {
        private List<String> timeSlots;
        private boolean isBooked;

        public TimeSlotsAdapter(List<String> timeSlots, boolean isBooked) {
            this.timeSlots = timeSlots;
            this.isBooked = isBooked;
        }
        @NonNull
        @Override
        public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate the layout for a single time slot item
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slot_item, parent, false);
            return new TimeSlotViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
            // Bind the time slot data to the view
            String timeSlot = timeSlots.get(position);
            holder.TimeTextview.setText(timeSlot);

            holder.cardviewTime.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            /*holder.TimeTextview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.TimeTextview.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.orange));
                }
            });*/

            // Set the text color based on the booked status
            if (isBooked) {
                holder.TimeTextview.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorError));
                holder.cardviewTime.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));
                holder.TimeTextview.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));

            } else {
                holder.TimeTextview.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                holder.cardviewTime.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                holder.TimeTextview.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            }
        }

        @Override
        public int getItemCount() {
            return timeSlots.size();
        }

        class TimeSlotViewHolder extends RecyclerView.ViewHolder {
            TextView TimeTextview;
            CardView cardviewTime;

            public TimeSlotViewHolder(@NonNull View itemView) {
                super(itemView);
                TimeTextview = itemView.findViewById(R.id.TimeTextview);
                cardviewTime = itemView.findViewById(R.id.cardviewTime);
            }
        }
    }
}