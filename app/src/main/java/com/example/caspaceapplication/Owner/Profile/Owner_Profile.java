package com.example.caspaceapplication.Owner.Profile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.caspaceapplication.R;

public class Owner_Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Owner_BranchProfile branchProfileFragment = new Owner_BranchProfile();
        Owner_UserProfile userProfileFragment = new Owner_UserProfile();

        fragmentTransaction.add(R.id.frameLayout_ownerProfile, branchProfileFragment);
        fragmentTransaction.commit();

    }
}