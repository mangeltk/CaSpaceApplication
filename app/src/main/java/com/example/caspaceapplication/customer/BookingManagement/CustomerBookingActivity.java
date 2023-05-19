package com.example.caspaceapplication.customer.BookingManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class CustomerBookingActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    CollectionReference OfficeLayoutsRef = firebaseFirestore.collection("OfficeLayouts");
    CollectionReference AllBranchesRef = firebaseFirestore.collection("CospaceBranches");
    CollectionReference AllSubmittedBookingRef = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");
    CollectionReference CustomerAccountsRef = firebaseFirestore.collection("CustomerUserAccounts");

    String ownerId, layoutId, layoutName, branch_Image, branch_Name, layout_Image;

    TextView CBDBranchNameTextView, CBDLayoutNameTextView, CBDLayoutAvailabilityTextView,
            CBDLayoutPersonCapTextView, CBDLayoutAreasizeTextView, CBDLayoutHourlyRateTextview,
            CBDLayoutDailyRateTextview, CBDLayoutWeeklyRateTextview, CBDLayoutMonthlyRateTextview,
            CBDLayoutNameTextViewBelow;

    AppCompatButton browseCalendarAppCompatButton;

    ImageView CBDLayoutImageImageView, CBDbranchSmallPicImageview;

    CardView LayoutDetailsCardview, LayoutDetailsCardviewBelow;


    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_booking);

        Intent intent = getIntent();
        layoutName = intent.getStringExtra("layoutName");
        layoutId = intent.getStringExtra("layout_id");
        String owner_id = intent.getStringExtra("owner_id");
        ownerId = owner_id;


        findViews();
        getBranchDetails();
        getLayoutDetails();
        onCardviewClicks();

        browseCalendarAppCompatButton = findViewById(R.id.browseCalendar_AppCompatButton);
        viewPager = findViewById(R.id.viewPagerBookings);
        ViewPagerCustBookingsAdapter viewPagerAdapter = new ViewPagerCustBookingsAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);

        browseCalendarAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of the CustBooking1st fragment
                CustBooking1st bookingsStep1Fragment = new CustBooking1st();

                // Pass the intent content to the fragment
                Bundle bundle = new Bundle();
                bundle.putString("layoutName", layoutName);
                bundle.putString("layout_id", layoutId);
                bundle.putString("owner_id", ownerId);
                bundle.putString("layoutImage", layout_Image);
                bundle.putString("branchImage", branch_Image);
                bundle.putString("branchName", branch_Name);
                bookingsStep1Fragment.setArguments(bundle);

                // Redirect to the CustBooking1st fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerBookings, bookingsStep1Fragment);
                fragmentTransaction.commit();
            }
        });

    }


    public void findViews(){

        CBDBranchNameTextView = findViewById(R.id.CBDBranchName_TextView);
        CBDbranchSmallPicImageview = findViewById(R.id.CBDbranchSmallPic_Imageview);
        CBDLayoutNameTextView = findViewById(R.id.CBDLayoutName_TextView);
        CBDLayoutAvailabilityTextView = findViewById(R.id.CBDLayoutAvailability_TextView);
        CBDLayoutPersonCapTextView = findViewById(R.id.CBDLayoutPersonCap_TextView);
        CBDLayoutAreasizeTextView = findViewById(R.id.CBDLayoutAreasize_TextView);
        CBDLayoutHourlyRateTextview = findViewById(R.id.CBDLayoutHourlyRate_Textview);
        CBDLayoutDailyRateTextview = findViewById(R.id.CBDLayoutDailyRate_Textview);
        CBDLayoutWeeklyRateTextview = findViewById(R.id.CBDLayoutWeeklyRate_Textview);
        CBDLayoutMonthlyRateTextview = findViewById(R.id.CBDLayoutMonthlyRate_Textview);
        CBDLayoutImageImageView = findViewById(R.id.CBDLayoutImage_ImageView);
        LayoutDetailsCardview = findViewById(R.id.CBDcardViewBooking);
        LayoutDetailsCardviewBelow = findViewById(R.id.CBDcardViewBookingBelow);
        CBDLayoutNameTextViewBelow = findViewById(R.id.CBDLayoutNameBelow_TextView);

    }

    public void onCardviewClicks() {

        LayoutDetailsCardviewBelow.setVisibility(View.GONE);
        LayoutDetailsCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutDetailsCardviewBelow.setVisibility(View.VISIBLE);
                LayoutDetailsCardview.setVisibility(View.GONE);
            }
        });
        LayoutDetailsCardviewBelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutDetailsCardview.setVisibility(View.VISIBLE);
                LayoutDetailsCardviewBelow.setVisibility(View.GONE);
            }
        });
    }

    public void getBranchDetails(){
        AllBranchesRef.whereEqualTo("owner_id", ownerId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                            String branchName = documentSnapshot.getString("cospaceName");
                            String branchImage = documentSnapshot.getString("cospaceImage");
                            String StreetAddress = documentSnapshot.getString("cospaceStreetAddress");
                            String cityAddress = documentSnapshot.getString("cospaceCityAddress");
                            String contactInfo = documentSnapshot.getString("cospaceContactInfo");

                            CBDBranchNameTextView.setText(branchName);
                            branch_Name = branchName;
                            if (branchImage != null || !branchImage.isEmpty()){
                                Picasso.get().load(branchImage).into(CBDbranchSmallPicImageview);
                                branch_Image = branchImage;
                            }
                           /* CBDBranchLocationTextView.setText(StreetAddress + " " + cityAddress);
                            CBDBranchContactInfoTextView.setText(contactInfo);*/

                        }
                    }
                });
    }

    public void getLayoutDetails(){
        OfficeLayoutsRef.whereEqualTo("owner_id", ownerId)
                .whereEqualTo("layout_id", layoutId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            String image = documentSnapshot.getString("layoutImage");
                            String layout_Name = documentSnapshot.getString("layoutName");
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
                                Picasso.get().load(image).into(CBDLayoutImageImageView);
                                layout_Image = image;
                            }else{
                                Picasso.get().load(R.drawable.uploadphoto).into(CBDLayoutImageImageView);
                            }

                            CBDLayoutNameTextView.setText(layoutName);
                            CBDLayoutNameTextViewBelow.setText(layout_Name);
                            layoutName = layout_Name;
                            CBDLayoutAvailabilityTextView.setText(availability);

                            CBDLayoutHourlyRateTextview.setText(perHour);

                            CBDLayoutDailyRateTextview.setText(perDay);

                            CBDLayoutWeeklyRateTextview.setText(perWeek);

                            CBDLayoutMonthlyRateTextview.setText(perMonth);

                            //CBDLayoutAnnualRateTextview.setText(perYear);

                            CBDLayoutPersonCapTextView.setText(minPersonCap + " - " + maxPersonCap);

                            CBDLayoutAreasizeTextView.setText(layoutAreasize + " sq. m.");
                        }
                    }
                });
    }


}