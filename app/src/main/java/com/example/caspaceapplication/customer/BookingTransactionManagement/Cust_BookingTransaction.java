package com.example.caspaceapplication.customer.BookingTransactionManagement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Cust_BookingTransaction extends AppCompatActivity {

    DatePickerDialog.OnDateSetListener setListener;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference OfficeLayoutsRef = firebaseFirestore.collection("OfficeLayouts");
    CollectionReference AllBranchesRef = firebaseFirestore.collection("CospaceBranches");

    TextView CustBTBranchNameTextView, CustBTLayoutNameTextView, CustBTLayoutAvailabilityTextView,
             CustBTLayoutPersonCapTextView, CustBTLayoutAreasizeTextView, CustBTLayoutHourlyRateTextview,
             CustBTLayoutDailyRateTextview, CustBTLayoutWeeklyRateTextview, CustBTLayoutMonthlyRateTextview, CustBTLayoutAnnualRateTextview,
             CustBTBranchLocationTextView, CustBTBranchHoursTextView, CustBTBranchContactInfoTextView;

    TextView seletedRateValueTextview;

    RadioButton hourlyRateRadioButton, dailyRateRadioButton, weeklyRateRadioButton, monthlyRateRadioButton, annualRateRadioButton;

    ImageView CustBTLayoutImageImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_booking_transaction);

        //todo: DETAILS NEEDED based on survey results
        // Customer Name
        // Organization Name
        // How many tenants
        // Lease duration
        // Industry/Line of business
        // Verification process (Submission of legal IDs and documents needed)
        // Mode of payment
        // ----------
        // OTHER INFO:
        // All chosen Layout info
        // how many days
        // dates needed
        // total payment
        // Customer contact info : phone and email
        // Customer address

        // Get the intent that started the activity
        Intent intent = getIntent();
        String layoutName = intent.getStringExtra("layoutName");
        String layout_id = intent.getStringExtra("layout_id");
        String owner_id = intent.getStringExtra("owner_id");

        CustBTBranchNameTextView = findViewById(R.id.CustBTBranchName_TextView);
        CustBTLayoutNameTextView = findViewById(R.id.CustBTLayoutName_TextView);
        CustBTLayoutAvailabilityTextView = findViewById(R.id.CustBTLayoutAvailability_TextView);
        CustBTLayoutPersonCapTextView = findViewById(R.id.CustBTLayoutPersonCap_TextView);
        CustBTLayoutAreasizeTextView = findViewById(R.id.CustBTLayoutAreasize_TextView);
        CustBTLayoutHourlyRateTextview = findViewById(R.id.CustBTLayoutHourlyRate_Textview);
        CustBTLayoutDailyRateTextview = findViewById(R.id.CustBTLayoutDailyRate_Textview);
        CustBTLayoutWeeklyRateTextview = findViewById(R.id.CustBTLayoutWeeklyRate_Textview);
        CustBTLayoutMonthlyRateTextview = findViewById(R.id.CustBTLayoutMonthlyRate_Textview);
        CustBTLayoutAnnualRateTextview = findViewById(R.id.CustBTLayoutAnnualRate_Textview);
        CustBTBranchLocationTextView = findViewById(R.id.CustBTBranchLocation_TextView);
        CustBTBranchHoursTextView = findViewById(R.id.CustBTBranchHours_TextView);
        CustBTBranchContactInfoTextView = findViewById(R.id.CustBTBranchContactInfo_TextView);
        CustBTLayoutImageImageView = findViewById(R.id.CustBTLayoutImage_ImageView);

        hourlyRateRadioButton = findViewById(R.id.hourlyRadioButton);
        dailyRateRadioButton = findViewById(R.id.daiyRadioButton);
        weeklyRateRadioButton = findViewById(R.id.weeklyRadioButton);
        monthlyRateRadioButton = findViewById(R.id.monthlyRadioButton);
        annualRateRadioButton = findViewById(R.id.annuallyRadioButton);

        seletedRateValueTextview = findViewById(R.id.seletedRateValue);



        AllBranchesRef.whereEqualTo("owner_id", owner_id)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                            String branchName = documentSnapshot.getString("cospaceName");
                            String StreetAddress = documentSnapshot.getString("cospaceStreetAddress");
                            String cityAddress = documentSnapshot.getString("cospaceCityAddress");
                            String storeHours = documentSnapshot.getString("cospaceStoreHours");
                            String contactInfo = documentSnapshot.getString("cospaceContactInfo");

                            CustBTBranchNameTextView.setText(branchName);
                            CustBTBranchLocationTextView.setText(StreetAddress + " " + cityAddress);

                            CustBTBranchHoursTextView.setText(storeHours);
                            CustBTBranchContactInfoTextView.setText(contactInfo);
                        }
                    }
                });


        OfficeLayoutsRef.whereEqualTo("owner_id", owner_id)
                .whereEqualTo("layout_id", layout_id)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                            String image = documentSnapshot.getString("layoutImage");
                            String layoutName = documentSnapshot.getString("layoutName");
                            String layoutAreasize = documentSnapshot.getString("layoutAreasize");
                            String minPersonCap = documentSnapshot.getString("minCapacity");
                            String maxPersonCap = documentSnapshot.getString("maxCapacity");
                            String layoutType = documentSnapshot.getString("layoutType");
                            String availability = documentSnapshot.getString("layoutAvailability");

                            String perHour = documentSnapshot.getString("layoutHourlyPrice");
                            String perDay = documentSnapshot.getString("layoutDailyPrice");
                            String perWeek = documentSnapshot.getString("layoutWeeklyPrice");
                            String perMonth = documentSnapshot.getString("layoutMonthlyPrice");
                            String perYear = documentSnapshot.getString("layoutAnnualPrice");


                            if (image != null || !image.isEmpty()){
                                Picasso.get().load(image).into(CustBTLayoutImageImageView);
                            }else{
                                Picasso.get().load(R.drawable.uploadphoto).into(CustBTLayoutImageImageView);
                            }

                            CustBTLayoutNameTextView.setText(layoutName);

                            CustBTLayoutAvailabilityTextView.setText(availability);

                            CustBTLayoutHourlyRateTextview.setText(perHour);

                            CustBTLayoutDailyRateTextview.setText(perDay);

                            CustBTLayoutWeeklyRateTextview.setText(perWeek);

                            CustBTLayoutMonthlyRateTextview.setText(perMonth);

                            CustBTLayoutAnnualRateTextview.setText(perYear);

                            CustBTLayoutPersonCapTextView.setText(minPersonCap + " - " + maxPersonCap);

                            CustBTLayoutAreasizeTextView.setText(layoutAreasize);

                            hourlyRateRadioButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    seletedRateValueTextview.setText(perHour);
                                }
                            });

                            dailyRateRadioButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    seletedRateValueTextview.setText(perDay);
                                }
                            });

                            weeklyRateRadioButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    seletedRateValueTextview.setText(perWeek);
                                }
                            });

                            monthlyRateRadioButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    seletedRateValueTextview.setText(perMonth);
                                }
                            });

                            annualRateRadioButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    seletedRateValueTextview.setText(perYear);
                                }
                            });

                        }

                    }
                });


        // Get the current date
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        final int second = calendar.get(Calendar.SECOND);

        // Set up the DatePickerDialog listener for the start date
        DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Do something with the selected start date
                //String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                String selectedDate = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month + 1, dayOfMonth, hour, minute, second);
                // For example, you can update a TextView with the selected start date
                TextView startDateTextView = findViewById(R.id.startDate);
                startDateTextView.setText(selectedDate);
            }
        };

        // Set up the DatePickerDialog listener for the end date
        DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Do something with the selected end date
                //String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                String selectedDate = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month + 1, dayOfMonth, hour, minute, second);
                // For example, you can update a TextView with the selected end date
                TextView endDateTextView = findViewById(R.id.endDate);
                endDateTextView.setText(selectedDate);
                TextView startDateTextView = findViewById(R.id.startDate);

                // Calculate the difference between the start and end dates
                String startDateString = startDateTextView.getText().toString();
                String endDateString = endDateTextView.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startDate = dateFormat.parse(startDateString);
                    Date endDate = dateFormat.parse(endDateString);
                    long diff = endDate.getTime() - startDate.getTime();
                    long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
                    long hours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
                    long minutes = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS) - TimeUnit.MINUTES.convert(hours, TimeUnit.HOURS);
                    long seconds = TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS) - TimeUnit.SECONDS.convert(hours, TimeUnit.HOURS) - TimeUnit.SECONDS.convert(minutes, TimeUnit.MINUTES);

                    // Calculate the total payment per hour

                    double hourlyRate = Double.parseDouble(seletedRateValueTextview.getText().toString()); // Example hourly rate
                    double totalPayment = hours * hourlyRate + (minutes / 60.0) * hourlyRate + (seconds / 3600.0) * hourlyRate;


                    TextView totalDays = findViewById(R.id.days);
                    totalDays.setText(String.valueOf(days));

                    TextView total = findViewById(R.id.total);
                    total.setText(String.format("₱%.2f", totalPayment));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        Button startDateButton = findViewById(R.id.date_picker_button);

        // Set up the DatePickerDialog for the start date and show it when the button is clicked
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Cust_BookingTransaction.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        startDateSetListener,
                        year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        Button endDateButton = findViewById(R.id.date_picker_button2);
        // Set up the DatePickerDialog for the end date and show it when the button is clicked
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Cust_BookingTransaction.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        endDateSetListener,
                        year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });


    }

}