package com.example.caspaceapplication.customer.BookingTransactionManagement;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import com.example.caspaceapplication.Notification.FCMSend;
import static android.content.ContentValues.TAG;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.caspaceapplication.Owner.BranchModel;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.Customer_Homepage_BottomNav;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class Cust_BookingTransaction extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference OfficeLayoutsRef = firebaseFirestore.collection("OfficeLayouts");
    CollectionReference AllBranchesRef = firebaseFirestore.collection("CospaceBranches");
    CollectionReference AllSubmittedBookingRef = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");

    TextView CustBTBranchNameTextView, CustBTLayoutNameTextView, CustBTLayoutAvailabilityTextView,
             CustBTLayoutPersonCapTextView, CustBTLayoutAreasizeTextView, CustBTLayoutHourlyRateTextview,
             CustBTLayoutDailyRateTextview, CustBTLayoutWeeklyRateTextview, CustBTLayoutMonthlyRateTextview, CustBTLayoutAnnualRateTextview,
             CustBTBranchLocationTextView, CustBTBranchHoursTextView, CustBTBranchContactInfoTextView, CustBTLayoutNameTextViewBelow;

    TextView seletedRateValueTextview, selectedRateTypeTextview, CustomerDetailsTitleLayout;

    GridLayout CustomerDetailsLayout;

    AppCompatButton CustBookingDetailsbackButton;

    RadioButton hourlyRateRadioButton, dailyRateRadioButton, weeklyRateRadioButton, monthlyRateRadioButton, annualRateRadioButton;

    ImageView CustBTLayoutImageImageView, branchSmallPicImageview;

    Uri filepath = null;
    private static final int GALLERY_CODE = 1;

    CardView cardView, cardViewBelow;
    LinearLayout radioButtonLinearLayout;
    ScrollView bookingDetailsScrollview;

    EditText customerFullNameEditText, organizationNameEditText, NoOfTenantsEditText,CustPhoneNumberEdittext,
             CustEmailEdittext, CustAddressEdittext;

    RadioButton payOnsiteRadioButton, payOtherOptionRadioButton;
    String selectedPaymentOption = null;

    Button CustProofOfPaymentButtonUpload;

    ImageView CustProofOfPaymentImageviewUpload;
    AppCompatButton submitBooking;

    String ownerId, branch_Image, branch_Name, layout_Image, layout_Name;

    TextView selectedStartDate, selectedEndDate, selectedStartTime, selectedEndTime,
            totalResultHours, totalResultDays, totalResultWeeks, totalResultMonths,
            totalResultYears, totalCalculatedFee, totalHoursTitle, totalDaysTitle, totalWeeksTitle,
            totalMonthsTitle, totalYearsTitle;

    AppCompatButton selectStartDateButton, selectedEndDateButton, selectStartTime, selectEndTime;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_booking_transaction);

        // Get the intent that started the activity
        IntentFilter filter = new IntentFilter("com.google.firebase.MESSAGING_EVENT");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
        Intent intent = getIntent();
        String layoutName = intent.getStringExtra("layoutName");
        String layout_id = intent.getStringExtra("layout_id");
        String owner_id = intent.getStringExtra("owner_id");
        ownerId = owner_id;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Submitting the booking details...");

        CustBookingDetailsbackButton = findViewById(R.id.CustBookingDetails_backButton);
        CustBTBranchNameTextView = findViewById(R.id.CustBTBranchName_TextView);
        branchSmallPicImageview = findViewById(R.id.branchSmallPic_Imageview);
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
        seletedRateValueTextview = findViewById(R.id.selectedRateDisplay);
        selectedRateTypeTextview = findViewById(R.id.selectedRateTypeDisplay);
        cardView = findViewById(R.id.cardViewBooking);
        CustBTLayoutNameTextViewBelow = findViewById(R.id.CustBTLayoutName_TextViewBelow);
        cardViewBelow = findViewById(R.id.cardViewBookingBelow);
        bookingDetailsScrollview = findViewById(R.id.bookingDetails_Scrollview);
        radioButtonLinearLayout = findViewById(R.id.radioButton_LinearLayout);
        payOnsiteRadioButton = findViewById(R.id.payOnsiteOption_RadioButton);
        payOtherOptionRadioButton = findViewById(R.id.payOtherOption_RadioButton);

        selectedStartDate = findViewById(R.id.selectedStartDate_Textview);
        selectedEndDate = findViewById(R.id.selectedEndDate_Textview);
        selectStartDateButton = findViewById(R.id.selectStartDate_Button);
        selectedEndDateButton = findViewById(R.id.selectEndDate_Button);
        selectStartTime = findViewById(R.id.selectStartTime_Button);
        selectEndTime = findViewById(R.id.selectEndTime_Button);
        selectedStartTime = findViewById(R.id.startTime_Textview);
        selectedEndTime = findViewById(R.id.endTime_Textview);
        totalResultDays = findViewById(R.id.totalDays_Textview);
        totalResultHours = findViewById(R.id.totalHours_Textview);
        totalResultWeeks = findViewById(R.id.totalWeek_Textview);
        totalResultMonths = findViewById(R.id.totalMonths_Textview);
        totalResultYears = findViewById(R.id.totalYears_Textview);
        totalCalculatedFee = findViewById(R.id.totalPayment);

        payOnsiteRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPaymentOption = payOnsiteRadioButton.getText().toString();
                Toast.makeText(Cust_BookingTransaction.this, "option is " + selectedPaymentOption, Toast.LENGTH_SHORT).show();
            }
        });
        payOtherOptionRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPaymentOption = payOtherOptionRadioButton.getText().toString();
                Toast.makeText(Cust_BookingTransaction.this, "option is " + selectedPaymentOption, Toast.LENGTH_SHORT).show();

            }
        });

        //Get customer's inputted details

         customerFullNameEditText = findViewById(R.id.customerFullName_EditText);
         organizationNameEditText = findViewById(R.id.organizationName_EditText);
         NoOfTenantsEditText = findViewById(R.id.NoOfTenants_EditText);
         CustPhoneNumberEdittext = findViewById(R.id.CustPhoneNumber_Edittext);
         CustEmailEdittext = findViewById(R.id.CustEmail_Edittext);
         CustAddressEdittext = findViewById(R.id.CustAddress_Edittext);
         CustProofOfPaymentButtonUpload = findViewById(R.id.CustProofOfPayment_ButtonUpload);
         CustProofOfPaymentImageviewUpload = findViewById(R.id.CustProofOfPayment_ImageviewUpload);
         submitBooking = findViewById(R.id.SubmitBooking_Button);

        CustProofOfPaymentButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });

        bookingDetailsScrollview.setVisibility(View.GONE);

        //initialize cardview below gone
        cardViewBelow.setVisibility(View.GONE);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewBelow.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.GONE);
            }
        });
        cardViewBelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.VISIBLE);
                cardViewBelow.setVisibility(View.GONE);
            }
        });

        CustomerDetailsTitleLayout = findViewById(R.id.CustomerDetails_Title);
        CustomerDetailsLayout = findViewById(R.id.CustomerDetails_Layout);

        CustomerDetailsTitleLayout.setVisibility(View.GONE);
        CustomerDetailsLayout.setVisibility(View.GONE);

        AllBranchesRef.whereEqualTo("owner_id", owner_id)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                            String branchName = documentSnapshot.getString("cospaceName");
                            String branchImage = documentSnapshot.getString("cospaceImage");
                            String StreetAddress = documentSnapshot.getString("cospaceStreetAddress");
                            String cityAddress = documentSnapshot.getString("cospaceCityAddress");
                            String contactInfo = documentSnapshot.getString("cospaceContactInfo");

                            CustBTBranchNameTextView.setText(branchName);
                            branch_Name = branchName;
                            if (branchImage != null || !branchImage.isEmpty()){
                                Picasso.get().load(branchImage).into(branchSmallPicImageview);
                                branch_Image = branchImage;
                            }
                            CustBTBranchLocationTextView.setText(StreetAddress + " " + cityAddress);
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
                            int minPersonCap = Integer.parseInt(documentSnapshot.getString("minCapacity"));
                            int maxPersonCap = Integer.parseInt(documentSnapshot.getString("maxCapacity"));
                            String layoutType = documentSnapshot.getString("layoutType");
                            String availability = documentSnapshot.getString("layoutAvailability");
                            String perHour = documentSnapshot.getString("layoutHourlyPrice");
                            String perDay = documentSnapshot.getString("layoutDailyPrice");
                            String perWeek = documentSnapshot.getString("layoutWeeklyPrice");
                            String perMonth = documentSnapshot.getString("layoutMonthlyPrice");
                            String perYear = documentSnapshot.getString("layoutAnnualPrice");

                            if (image != null || !image.isEmpty()){
                                Picasso.get().load(image).into(CustBTLayoutImageImageView);
                                layout_Image = image;
                            }else{
                                Picasso.get().load(R.drawable.uploadphoto).into(CustBTLayoutImageImageView);
                            }

                            CustBTLayoutNameTextView.setText(layoutName);
                            layout_Name = layoutName;
                            CustBTLayoutNameTextViewBelow.setText(layoutName);

                            CustBTLayoutAvailabilityTextView.setText(availability);

                            CustBTLayoutHourlyRateTextview.setText(perHour);

                            CustBTLayoutDailyRateTextview.setText(perDay);

                            CustBTLayoutWeeklyRateTextview.setText(perWeek);

                            CustBTLayoutMonthlyRateTextview.setText(perMonth);

                            CustBTLayoutAnnualRateTextview.setText(perYear);

                            CustBTLayoutPersonCapTextView.setText(minPersonCap + " - " + maxPersonCap);

                            CustBTLayoutAreasizeTextView.setText(layoutAreasize + " sq. m.");

                            hourlyRateRadioButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    seletedRateValueTextview.setText(perHour);
                                    HourlyCalculation(perHour, minPersonCap, maxPersonCap);
                                    selectedRateTypeTextview.setText("Hourly rate");
                                    bookingDetailsScrollview.setVisibility(View.VISIBLE);
                                }
                            });

                            dailyRateRadioButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    seletedRateValueTextview.setText(perDay);
                                    selectedRateTypeTextview.setText("Daily rate");
                                    DailyCalculation(perDay, minPersonCap, maxPersonCap);
                                    bookingDetailsScrollview.setVisibility(View.VISIBLE);

                                }
                            });

                            weeklyRateRadioButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    seletedRateValueTextview.setText(perWeek);
                                    selectedRateTypeTextview.setText("Weekly rate");
                                    WeeklyCalculation(perWeek, minPersonCap, maxPersonCap);
                                    bookingDetailsScrollview.setVisibility(View.VISIBLE);
                                }
                            });

                            monthlyRateRadioButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    seletedRateValueTextview.setText(perMonth);
                                    selectedRateTypeTextview.setText("Monthly rate");
                                }
                            });

                            annualRateRadioButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    seletedRateValueTextview.setText(perYear);
                                    selectedRateTypeTextview.setText("Annual rate");
                                }
                            });

                        }

                    }
                });


    }

    public static boolean isStoreOpen(BranchModel branchModel, LocalDateTime bookingStartDateTime, LocalDateTime bookingEndDateTime) {
        // Get the opening hours for the store on the selected booking start date
        LocalDate bookingStartDate = bookingStartDateTime.toLocalDate();
        String bookingStartDayOfWeek = bookingStartDate.getDayOfWeek().name().toLowerCase();
        BranchModel.OpeningHours openingHours = branchModel.getOpeningHours().get(bookingStartDayOfWeek);

        // Check if the store is closed on the selected booking start date
        if (openingHours.isClosed()) {
            return false;
        }

        // Parse the opening and closing times for the store on the selected booking start date
        LocalTime openingTime = LocalTime.parse(openingHours.getOpenTime());
        LocalTime closingTime = LocalTime.parse(openingHours.getCloseTime());

        // Check if the booking start time is before the store opens or after it closes
        if (bookingStartDateTime.toLocalTime().isBefore(openingTime) || bookingStartDateTime.toLocalTime().isAfter(closingTime)) {
            return false;
        }

        // Check if the booking end time is before the store opens or after it closes
        if (bookingEndDateTime.toLocalTime().isBefore(openingTime) || bookingEndDateTime.toLocalTime().isAfter(closingTime)) {
            return false;
        }

        // If none of the above conditions are true, the store is open during the booking period
        return true;
    }

    private int mYear, mMonth, mDay, mHour, mMinute;

    private void showDatePickerDialog(final TextView date) {
        // Get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(Cust_BookingTransaction.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Set the selected date to the button's text
                        //TextView selectedStartDate = findViewById(R.id.selectedStartDate_Textview);
                        date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        /*TextView selectedEndDate = findViewById(R.id.selectedEndDate_Textview);
                        date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);*/
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(final TextView time) {
        // Get the current time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog timePickerDialog = new TimePickerDialog(Cust_BookingTransaction.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // Set the selected time to the button's text
                        time.setText(String.format(Locale.getDefault(), "%02d:%02d %s",
                                hourOfDay % 12 == 0  ? 12 : hourOfDay % 12, minute,
                                hourOfDay < 12 ? "AM" : "PM"));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void clearInputs(){
        selectedStartDate.setText("");
        selectedEndDate.setText("");
        selectedStartTime.setText("");
        selectedEndTime.setText("");
        totalResultHours.setText("");
        totalResultDays.setText("");
        totalResultWeeks.setText("");
        totalResultMonths.setText("");
        totalResultYears.setText("");
        totalCalculatedFee.setText("");
        CustomerDetailsTitleLayout.setVisibility(View.GONE);
        CustomerDetailsLayout.setVisibility(View.GONE);
    }

    public void getDateAndTime(){
        selectStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(selectedStartDate);
            }
        });

        selectedEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(selectedEndDate);
            }
        });

        selectStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(selectedStartTime);
            }
        });

        selectEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(selectedEndTime);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            filepath = data.getData();
            CustProofOfPaymentImageviewUpload.setImageURI(filepath);
        }
    }

    public void store(){

        String custFullname = customerFullNameEditText.getText().toString();
        String custOrganizationName = organizationNameEditText.getText().toString();
        String noOfTenants = NoOfTenantsEditText.getText().toString();
        String custPhoneNum = CustPhoneNumberEdittext.getText().toString();
        String custEmail = CustEmailEdittext.getText().toString();
        String custAddress = CustAddressEdittext.getText().toString();

        if (filepath!=null && selectedPaymentOption != null && !custFullname.isEmpty() && !custOrganizationName.isEmpty() &&
                !noOfTenants.isEmpty() && !custPhoneNum.isEmpty() && !custEmail.isEmpty() && !custAddress.isEmpty()){
            Toast.makeText(Cust_BookingTransaction.this, "Please check and review all the details", Toast.LENGTH_SHORT).show();

            TextView fullname, orgName, numTenants, phoneNum, email, address, rateType, rateValue, bookStartDate, bookEndDate, startTime, endTime,
                    totalHours, totalDays, totalWeeks, totalMonths, totalYears, totalPay, paymentOption;

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Cust_BookingTransaction.this);
            AlertDialog dialog;
            final View custReviewDetails = getLayoutInflater().inflate(R.layout.enter_customer_detailsforbooking_popup, null);
            rateType = (TextView) custReviewDetails.findViewById(R.id.rateTypePopup);
            rateValue = (TextView) custReviewDetails.findViewById(R.id.rateValuePopup);
            bookStartDate = (TextView) custReviewDetails.findViewById(R.id.bookingStartDatePopup);
            bookEndDate = (TextView) custReviewDetails.findViewById(R.id.bookingEndDatePopup);
            startTime = (TextView) custReviewDetails.findViewById(R.id.bookingStartTimePopup);
            endTime = (TextView) custReviewDetails.findViewById(R.id.bookingEndTimePopup);
            totalHours = (TextView) custReviewDetails.findViewById(R.id.totalHoursPopup);
            totalDays = (TextView) custReviewDetails.findViewById(R.id.totalDaysPopup);
            totalWeeks = (TextView) custReviewDetails.findViewById(R.id.totalWeeksPopup);
            totalMonths = (TextView) custReviewDetails.findViewById(R.id.totalMonthsPopup);
            totalYears = (TextView) custReviewDetails.findViewById(R.id.totalYearsPopup);
            paymentOption = (TextView) custReviewDetails.findViewById(R.id.CustPaymentOption_Textview);
            totalPay = (TextView) custReviewDetails.findViewById(R.id.totalPaymentPopup);
            fullname = (TextView) custReviewDetails.findViewById(R.id.customerFullName_Textview);
            orgName = (TextView) custReviewDetails.findViewById(R.id.organizationName_Textview);
            numTenants = (TextView) custReviewDetails.findViewById(R.id.NoOfTenants_Textview);
            phoneNum = (TextView) custReviewDetails.findViewById(R.id.CustPhoneNumber_Textview);
            email = (TextView) custReviewDetails.findViewById(R.id.CustEmail_Textview);
            address = (TextView) custReviewDetails.findViewById(R.id.CustAddress_Textview);
            ImageView paymentPic = (ImageView) custReviewDetails.findViewById(R.id.CustProofOfPayment_Imageview);
            AppCompatButton confirmButton = (AppCompatButton) custReviewDetails.findViewById(R.id.confirmBooking_ButtonPopup);
            AppCompatButton cancelButton = (AppCompatButton) custReviewDetails.findViewById(R.id.cancelBooking_ButtonPopup);

            rateType.setText(selectedRateTypeTextview.getText());
            rateValue.setText(seletedRateValueTextview.getText().toString());
            bookStartDate.setText(selectedStartDate.getText().toString());
            bookEndDate.setText(selectedEndDate.getText().toString());
            startTime.setText(selectedStartTime.getText().toString());
            endTime.setText(selectedEndTime.getText().toString());
            totalHours.setText(totalResultHours.getText().toString());
            totalDays.setText(totalResultDays.getText().toString());
            totalWeeks.setText(totalResultWeeks.getText().toString());
            totalMonths.setText(totalResultMonths.getText().toString());
            totalYears.setText(totalResultYears.getText().toString());
            totalMonths.setText(totalResultMonths.getText().toString());
            totalYears.setText(totalResultYears.getText().toString());
            paymentOption.setText(selectedPaymentOption);
            totalPay.setText(totalCalculatedFee.getText().toString());
            fullname.setText(customerFullNameEditText.getText().toString());
            orgName.setText(organizationNameEditText.getText().toString());
            numTenants.setText(NoOfTenantsEditText.getText().toString());
            phoneNum.setText(CustPhoneNumberEdittext.getText().toString());
            email.setText(CustEmailEdittext.getText().toString());
            address.setText(CustAddressEdittext.getText().toString());
            paymentPic.setImageURI(filepath);

            dialogBuilder.setView(custReviewDetails);
            dialog = dialogBuilder.create();
            dialog.show();

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Toast.makeText(Cust_BookingTransaction.this, "cancelled", Toast.LENGTH_SHORT).show();
                }
            });

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();

                    FirebaseUser customerId = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference path = firebaseStorage.getReference().child("ProofOfPayment").child(filepath.getLastPathSegment());
                    path.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Map<String,String> bookingDetails = new HashMap<>();
                                    bookingDetails.put("customerId",customerId.getUid());
                                    bookingDetails.put("ownerId", ownerId);
                                    bookingDetails.put("bookingId", "");
                                    bookingDetails.put("rateType", rateType.getText().toString());
                                    bookingDetails.put("rateValue", rateValue.getText().toString());
                                    bookingDetails.put("bookingStartDate", bookStartDate.getText().toString());
                                    bookingDetails.put("bookingEndDate", bookEndDate.getText().toString());
                                    bookingDetails.put("bookingStartTime", startTime.getText().toString());
                                    bookingDetails.put("bookingEndTime", endTime.getText().toString());
                                    bookingDetails.put("totalPayment", totalPay.getText().toString());
                                    bookingDetails.put("customerFullname", fullname.getText().toString());
                                    bookingDetails.put("organizationName", orgName.getText().toString());
                                    bookingDetails.put("numOfTenants", numTenants.getText().toString());
                                    bookingDetails.put("customerPhoneNum", phoneNum.getText().toString());
                                    bookingDetails.put("customerEmail", email.getText().toString());
                                    bookingDetails.put("customerAddress", address.getText().toString());
                                    bookingDetails.put("paymentOption", selectedPaymentOption);
                                    bookingDetails.put("proofOfPayment", task.getResult().toString());
                                    bookingDetails.put("bookingStatus", "Pending");
                                    bookingDetails.put("branchImage",branch_Image);
                                    bookingDetails.put("branchName",branch_Name);
                                    bookingDetails.put("layoutImage",layout_Image);
                                    bookingDetails.put("layoutName",layout_Name);
                                    if (!totalResultHours.getText().toString().equals("") || !totalResultDays.getText().toString().equals("")
                                            || !totalResultWeeks.getText().toString().equals("") || !totalResultMonths.getText().toString().equals("")
                                            || !totalResultYears.getText().toString().equals("")){
                                        bookingDetails.put("totalHours", totalHours.getText().toString());
                                        bookingDetails.put("totalDays", totalDays.getText().toString());
                                        bookingDetails.put("totalWeeks", totalWeeks.getText().toString());
                                        bookingDetails.put("totalMonths", totalMonths.getText().toString());
                                        bookingDetails.put("totalYears", totalYears.getText().toString());
                                    }else{
                                        bookingDetails.put("totalHours", "");
                                        bookingDetails.put("totalDays", "");
                                        bookingDetails.put("totalWeeks", "");
                                        bookingDetails.put("totalMonths", "");
                                        bookingDetails.put("totalYears", "");
                                    }
                                    AllSubmittedBookingRef.add(bookingDetails)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    AllSubmittedBookingRef.document(documentReference.getId())
                                                            .update("bookingId", documentReference.getId())
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(Cust_BookingTransaction.this, "Booking details submitted!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                    progressDialog.dismiss();

                                                    Intent intent = new Intent(Cust_BookingTransaction.this, Customer_Homepage_BottomNav.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    dialog.dismiss();

                                                    /*String customerName= custFullname;
                                                    String title = "Your Space has been booked!";
                                                    String message = customerName + " booked a space.";
                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                    db.collection("OwnerUserAccounts").document(ownerId)
                                                            .get()
                                                            .addOnSuccessListener(documentSnapshot -> {
                                                                String ownerFCMToken = documentSnapshot.getString("fcmToken");
                                                                FCMSend.pushNotification(Cust_BookingTransaction.this, ownerFCMToken, title, message);
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Log.e(TAG, "Error getting FCM token for owner", e);
                                                            });*/
                                                    String customerName= custFullname;
                                                    String spaceName = layout_Name;
                                                    String title = "Booking Notification";
                                                    String message = customerName + " booked "+spaceName +"."+R.drawable.fivestara;
                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                    CollectionReference notificationsRef = db.collection("OwnerNotificationStorage");

                                                    // Create a new notification document with a randomly generated ID
                                                    DocumentReference newNotificationRef = notificationsRef.document();
                                                    String newNotificationId = newNotificationRef.getId();

                                                        // Add the notification document to the "Notifications" collection
                                                    Map<String, Object> notification = new HashMap<>();
                                                    notification.put("notificationId", newNotificationId);
                                                    notification.put("title", title);
                                                    notification.put("message", message);
                                                    notification.put("ownerId", ownerId);
                                                    newNotificationRef.set(notification)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d(TAG, "Notification added with ID: " + newNotificationId);
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w(TAG, "Error adding notification", e);
                                                                }
                                                            });



                                                }
                                            });
                                }
                            });
                        }
                    });

                }
            });
        }else{
            Toast.makeText(Cust_BookingTransaction.this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                for (String key : intent.getExtras().keySet()) {
                    Object value = intent.getExtras().get(key);
                    Log.d(TAG, "Key: " + key + " Value: " + value);
                }
            }
        }
    };

    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            boolean isForeground = isAppInForeground(context);
            if (isForeground) {
                // Show alert dialog
                String title = intent.getExtras().getString("title");
                String message = intent.getExtras().getString("message");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setPositiveButton("OK", null);
                builder.create().show();
            } else {
                // Display notification in system tray
                // ...
            }
        }
    }

    private boolean isAppInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            String packageName = tasks.get(0).topActivity.getPackageName();
            return packageName.equals(context.getPackageName());
        }
        return false;
    }


    public void HourlyCalculation(String perHour, int minPersonCap, int maxPersonCap){

        clearInputs();

        getDateAndTime();

        selectedStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                Date startDate = null;
                Date endDate = null;
                String startDateString = selectedStartDate.getText().toString() + " " + selectedStartTime.getText().toString();
                String endDateString = selectedEndDate.getText().toString() + " " + selectedEndTime.getText().toString();
                try {
                    startDate = sdf.parse(startDateString);
                    endDate = sdf.parse(endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startDate != null && endDate != null) {
                    long diffInMillis = endDate.getTime() - startDate.getTime();
                    if (endDate.before(startDate)) {
                        // end time is on a different day, add 24 hours to account for extra day(s)
                        diffInMillis += TimeUnit.HOURS.toMillis(24);
                    }
                    long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60;
                    //Double total = (hours + ((double) minutes / 60.0)) * rate;
                    Double total = hours * Double.parseDouble(perHour);
                    if (total >= 0) {
                        totalResultHours.setText(String.format(Locale.getDefault(), "%d:%02d", hours, minutes));
                        totalCalculatedFee.setText(String.format(Locale.getDefault(), "₱%.2f", total));
                    } else {
                        totalResultHours.setText("");
                        totalCalculatedFee.setText("");
                        Toast.makeText(getApplicationContext(), "End time should be after start time", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectedEndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                Date startDate = null;
                Date endDate = null;
                String startDateString = selectedStartDate.getText().toString() + " " + selectedStartTime.getText().toString();
                String endDateString = selectedEndDate.getText().toString() + " " + selectedEndTime.getText().toString();
                try {
                    startDate = sdf.parse(startDateString);
                    endDate = sdf.parse(endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startDate != null && endDate != null) {
                    long diffInMillis = endDate.getTime() - startDate.getTime();
                    if (endDate.before(startDate)) {
                        // end time is on a different day, add 24 hours to account for extra day(s)
                        diffInMillis += TimeUnit.HOURS.toMillis(24);
                    }
                    long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60;
                    //Double total = (hours + ((double) minutes / 60.0)) * rate;
                    Double total = hours * Double.parseDouble(perHour);
                    if (total >= 0) {
                        totalResultHours.setText(String.format(Locale.getDefault(), "%d:%02d", hours, minutes));
                        totalCalculatedFee.setText(String.format(Locale.getDefault(), "₱%.2f", total));
                    } else {
                        totalResultHours.setText("");
                        totalCalculatedFee.setText("");
                        Toast.makeText(getApplicationContext(), "End time should be after start time", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectedStartTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                Date startDate = null;
                Date endDate = null;
                String startDateString = selectedStartDate.getText().toString() + " " + selectedStartTime.getText().toString();
                String endDateString = selectedEndDate.getText().toString() + " " + selectedEndTime.getText().toString();
                try {
                    startDate = sdf.parse(startDateString);
                    endDate = sdf.parse(endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startDate != null && endDate != null) {
                    long diffInMillis = endDate.getTime() - startDate.getTime();
                    if (endDate.before(startDate)) {
                        // end time is on a different day, add 24 hours to account for extra day(s)
                        diffInMillis += TimeUnit.HOURS.toMillis(24);
                    }
                    long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60;
                    //Double total = (hours + ((double) minutes / 60.0)) * rate;
                    Double total = hours * Double.parseDouble(perHour);
                    if (total >= 0) {
                        totalResultHours.setText(String.format(Locale.getDefault(), "%d:%02d", hours, minutes));
                        totalCalculatedFee.setText(String.format(Locale.getDefault(), "₱%.2f", total));
                    } else {
                        totalResultHours.setText("");
                        totalCalculatedFee.setText("");
                        Toast.makeText(getApplicationContext(), "End time should be after start time", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        selectedEndTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                Date startDate = null;
                Date endDate = null;
                String startDateString = selectedStartDate.getText().toString() + " " + selectedStartTime.getText().toString();
                String endDateString = selectedEndDate.getText().toString() + " " + selectedEndTime.getText().toString();
                try {
                    startDate = sdf.parse(startDateString);
                    endDate = sdf.parse(endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startDate != null && endDate != null) {
                    long diffInMillis = endDate.getTime() - startDate.getTime();
                    if (endDate.before(startDate)) {
                        // end time is on a different day, add 24 hours to account for extra day(s)
                        diffInMillis += TimeUnit.HOURS.toMillis(24);
                    }
                    long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60;
                    //Double total = (hours + ((double) minutes / 60.0)) * rate;
                    Double total = hours * Double.parseDouble(perHour);
                    if (total >= 0) {
                        totalResultHours.setText(String.format(Locale.getDefault(), "%d:%02d", hours, minutes));
                        totalCalculatedFee.setText(String.format(Locale.getDefault(), "₱%.2f", total));

                    } else {
                        totalResultHours.setText("");
                        totalCalculatedFee.setText("");
                        Toast.makeText(getApplicationContext(), "End time should be after start time", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        totalCalculatedFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CustomerDetailsTitleLayout.setVisibility(View.VISIBLE);
                CustomerDetailsLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        NoOfTenantsEditText.addTextChangedListener(new TextWatcher() {
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
                        NoOfTenantsEditText.setError("Please enter only numbers");
                    } else {
                        int numTenants = Integer.parseInt(input);
                        if (numTenants > maxPersonCap || numTenants < 1) {
                            s.clear();
                            NoOfTenantsEditText.setError("Person capacity is at " + minPersonCap + " to " + maxPersonCap);
                        }
                    }
                }
            }
        });

        submitBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store();
            }
        });

    }


    public void DailyCalculation(String perDay, int minPersonCap, int maxPersonCap){

        clearInputs();
        getDateAndTime();

        selectedStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                Date startDate = null;
                Date endDate = null;
                String startDateString = selectedStartDate.getText().toString() + " " + selectedStartTime.getText().toString();
                String endDateString = selectedEndDate.getText().toString() + " " + selectedEndTime.getText().toString();
                try {
                    startDate = sdf.parse(startDateString);
                    endDate = sdf.parse(endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startDate != null && endDate != null) {
                    long diffInMillis = endDate.getTime() - startDate.getTime();
                    long totalDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);
                    long remainingHours = TimeUnit.MILLISECONDS.toHours(diffInMillis - TimeUnit.DAYS.toMillis(totalDays));
                    double fee = totalDays * Double.parseDouble(perDay);

                    if (totalDays < 0){
                        totalResultDays.setText("");
                        totalResultHours.setText("");
                        totalCalculatedFee.setText("");
                        Toast.makeText(getApplicationContext(), "End time should be after start time", Toast.LENGTH_SHORT).show();
                    }else{
                        Double total = fee;
                        if (totalDays > 0){
                            Double hourlyRate = fee / 24;
                            //total += hourlyRate * remainingHours;
                        }
                        totalCalculatedFee.setText(String.format(Locale.getDefault(), "₱%.2f", total));
                        totalResultDays.setText(Long.toString(totalDays));
                        totalResultHours.setText(Long.toString(remainingHours)  + " hour" + (remainingHours >= 1 ? "s" : ""));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectedEndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                Date startDate = null;
                Date endDate = null;
                String startDateString = selectedStartDate.getText().toString() + " " + selectedStartTime.getText().toString();
                String endDateString = selectedEndDate.getText().toString() + " " + selectedEndTime.getText().toString();
                try {
                    startDate = sdf.parse(startDateString);
                    endDate = sdf.parse(endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startDate != null && endDate != null) {
                    long diffInMillis = endDate.getTime() - startDate.getTime();
                    long totalDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);
                    long remainingHours = TimeUnit.MILLISECONDS.toHours(diffInMillis - TimeUnit.DAYS.toMillis(totalDays));
                    double fee = totalDays * Double.parseDouble(perDay);

                    if (totalDays < 0){
                        totalResultDays.setText("");
                        totalResultHours.setText("");
                        totalCalculatedFee.setText("");
                        Toast.makeText(getApplicationContext(), "End time should be after start time", Toast.LENGTH_SHORT).show();
                    }else{
                        Double total = fee;
                        if (totalDays > 0){
                            Double hourlyRate = fee / 24;
                            //total += hourlyRate * remainingHours;
                        }
                        totalCalculatedFee.setText(String.format(Locale.getDefault(), "₱%.2f", total));
                        totalResultDays.setText(Long.toString(totalDays));
                        totalResultHours.setText(Long.toString(remainingHours)  + " hour" + (remainingHours >= 1 ? "s" : ""));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectStartTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                Date startDate = null;
                Date endDate = null;
                String startDateString = selectedStartDate.getText().toString() + " " + selectedStartTime.getText().toString();
                String endDateString = selectedEndDate.getText().toString() + " " + selectedEndTime.getText().toString();
                try {
                    startDate = sdf.parse(startDateString);
                    endDate = sdf.parse(endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startDate != null && endDate != null) {
                    long diffInMillis = endDate.getTime() - startDate.getTime();
                    long totalDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);
                    long remainingHours = TimeUnit.MILLISECONDS.toHours(diffInMillis - TimeUnit.DAYS.toMillis(totalDays));
                    double fee = totalDays * Double.parseDouble(perDay);

                    if (totalDays < 0){
                        totalResultDays.setText("");
                        totalResultHours.setText("");
                        totalCalculatedFee.setText("");
                        Toast.makeText(getApplicationContext(), "End time should be after start time", Toast.LENGTH_SHORT).show();
                    }else{
                        Double total = fee;
                        if (totalDays > 0){
                            Double hourlyRate = fee / 24;
                            //total += hourlyRate * remainingHours;
                        }
                        totalCalculatedFee.setText(String.format(Locale.getDefault(), "₱%.2f", total));
                        totalResultDays.setText(Long.toString(totalDays));
                        totalResultHours.setText(Long.toString(remainingHours)  + " hour" + (remainingHours >= 1 ? "s" : ""));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectedEndTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                Date startDate = null;
                Date endDate = null;
                String startDateString = selectedStartDate.getText().toString() + " " + selectedStartTime.getText().toString();
                String endDateString = selectedEndDate.getText().toString() + " " + selectedEndTime.getText().toString();
                try {
                    startDate = sdf.parse(startDateString);
                    endDate = sdf.parse(endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startDate != null && endDate != null) {
                    long diffInMillis = endDate.getTime() - startDate.getTime();
                    long totalDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);
                    long remainingHours = TimeUnit.MILLISECONDS.toHours(diffInMillis - TimeUnit.DAYS.toMillis(totalDays));
                    double fee = totalDays * Double.parseDouble(perDay);

                    if (totalDays < 0){
                        totalResultDays.setText("");
                        totalResultHours.setText("");
                        totalCalculatedFee.setText("");
                        Toast.makeText(getApplicationContext(), "End time should be after start time", Toast.LENGTH_SHORT).show();
                    }else{
                        Double total = fee;
                        if (totalDays > 0){
                            Double hourlyRate = fee / 24;
                            //total += hourlyRate * remainingHours;
                        }
                        totalCalculatedFee.setText(String.format(Locale.getDefault(), "₱%.2f", total));
                        totalResultDays.setText(Long.toString(totalDays));
                        totalResultHours.setText(Long.toString(remainingHours)  + " hour" + (remainingHours >= 1 ? "s" : ""));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        totalCalculatedFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CustomerDetailsTitleLayout.setVisibility(View.VISIBLE);
                CustomerDetailsLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        NoOfTenantsEditText.addTextChangedListener(new TextWatcher() {
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
                        NoOfTenantsEditText.setError("Please enter only numbers");
                    } else {
                        int numTenants = Integer.parseInt(input);
                        if (numTenants > maxPersonCap || numTenants < 1) {
                            s.clear();
                            NoOfTenantsEditText.setError("Person capacity is at " + minPersonCap + " to " + maxPersonCap);
                        }
                    }
                }
            }
        });

        CustProofOfPaymentButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });
    }

    public void WeeklyCalculation(String perWeek, int minPersonCap, int maxPersonCap){

        clearInputs();
        getDateAndTime();

        selectedStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                Date startDate = null;
                Date endDate = null;
                String startDateString = selectedStartDate.getText().toString() + " " + selectedStartTime.getText().toString();
                String endDateString = selectedEndDate.getText().toString() + " " + selectedEndTime.getText().toString();
                try {
                    startDate = sdf.parse(startDateString);
                    endDate = sdf.parse(endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (startDate != null && endDate != null) {
                    long diffInMillis = endDate.getTime() - startDate.getTime();
                    long totalDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

                    // Check if the selected dates cover a full week
                    if (totalDays % 7 != 0) {
                        totalResultDays.setText("");
                        totalResultHours.setText("");
                        totalCalculatedFee.setText("");
                        Toast.makeText(getApplicationContext(), "Selected dates must cover a full week", Toast.LENGTH_SHORT).show();
                    } else{
                        double feePerWeek = Double.parseDouble(perWeek);
                        int totalWeeks = (int) (totalDays / 7);
                        double totalFee = totalWeeks * feePerWeek;

                        String calcFee = String.valueOf(totalFee);
                        totalCalculatedFee.setText(calcFee);
                        totalResultDays.setText("");
                        totalResultWeeks.setText(totalWeeks == 1 ? "1 week" : totalWeeks + " weeks");
                        totalResultHours.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectedEndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                Date startDate = null;
                Date endDate = null;
                String startDateString = selectedStartDate.getText().toString() + " " + selectedStartTime.getText().toString();
                String endDateString = selectedEndDate.getText().toString() + " " + selectedEndTime.getText().toString();
                try {
                    startDate = sdf.parse(startDateString);
                    endDate = sdf.parse(endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (startDate != null && endDate != null) {
                    long diffInMillis = endDate.getTime() - startDate.getTime();
                    long totalDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

                    // Check if the selected dates cover a full week
                    if (totalDays % 7 != 0) {
                        totalResultDays.setText("");
                        totalResultHours.setText("");
                        totalCalculatedFee.setText("");
                        Toast.makeText(getApplicationContext(), "Selected dates must cover a full week", Toast.LENGTH_SHORT).show();
                    } else{
                        double feePerWeek = Double.parseDouble(perWeek);
                        int totalWeeks = (int) (totalDays / 7);
                        double totalFee = totalWeeks * feePerWeek;

                        String calcFee = String.valueOf(totalFee);
                        totalCalculatedFee.setText(calcFee);
                        totalResultDays.setText("");
                        totalResultWeeks.setText(totalWeeks == 1 ? "1 week" : totalWeeks + " weeks");
                        totalResultHours.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectedStartTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                Date startDate = null;
                Date endDate = null;
                String startDateString = selectedStartDate.getText().toString() + " " + selectedStartTime.getText().toString();
                String endDateString = selectedEndDate.getText().toString() + " " + selectedEndTime.getText().toString();
                try {
                    startDate = sdf.parse(startDateString);
                    endDate = sdf.parse(endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (startDate != null && endDate != null) {
                    long diffInMillis = endDate.getTime() - startDate.getTime();
                    long totalDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

                    // Check if the selected dates cover a full week
                    if (totalDays % 7 != 0) {
                        totalResultDays.setText("");
                        totalResultHours.setText("");
                        totalCalculatedFee.setText("");
                        Toast.makeText(getApplicationContext(), "Selected dates must cover a full week", Toast.LENGTH_SHORT).show();
                    } else{
                        double feePerWeek = Double.parseDouble(perWeek);
                        int totalWeeks = (int) (totalDays / 7);
                        double totalFee = totalWeeks * feePerWeek;

                        String calcFee = String.valueOf(totalFee);
                        totalCalculatedFee.setText(calcFee);
                        totalResultDays.setText("");
                        totalResultWeeks.setText(totalWeeks == 1 ? "1 week" : totalWeeks + " weeks");
                        totalResultHours.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectedEndTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                Date startDate = null;
                Date endDate = null;
                String startDateString = selectedStartDate.getText().toString() + " " + selectedStartTime.getText().toString();
                String endDateString = selectedEndDate.getText().toString() + " " + selectedEndTime.getText().toString();
                try {
                    startDate = sdf.parse(startDateString);
                    endDate = sdf.parse(endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (startDate != null && endDate != null) {
                    long diffInMillis = endDate.getTime() - startDate.getTime();
                    long totalDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

                    // Check if the selected dates cover a full week
                    if (totalDays % 7 != 0) {
                        totalResultDays.setText("");
                        totalResultHours.setText("");
                        totalCalculatedFee.setText("");
                        Toast.makeText(getApplicationContext(), "Selected dates must cover a full week", Toast.LENGTH_SHORT).show();
                    } else{
                        double feePerWeek = Double.parseDouble(perWeek);
                        int totalWeeks = (int) (totalDays / 7);
                        double totalFee = totalWeeks * feePerWeek;

                        String calcFee = String.valueOf(totalFee);
                        totalCalculatedFee.setText(calcFee);
                        totalResultDays.setText("");
                        totalResultWeeks.setText(totalWeeks == 1 ? "1 week" : totalWeeks + " weeks");
                        totalResultHours.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        totalCalculatedFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CustomerDetailsTitleLayout.setVisibility(View.VISIBLE);
                CustomerDetailsLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


}