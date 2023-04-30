package com.example.caspaceapplication.Owner.Profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Owner_UserProfile extends Fragment {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    ImageButton owner_userBranchButton;
    TextView ownerProfile_FullName, ownerProfile_CompanyName, ownerProfile_Firstname, ownerProfile_Lastname, ownerProfile_IDNumber, ownerProfile_Email;
    AppCompatButton ownerEditUserProfileButton;


    public Owner_UserProfile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_owner__user_profile, container, false);

        owner_userBranchButton = rootView.findViewById(R.id.owner_userBranch_Button);

        owner_userBranchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                Owner_BranchProfile fragment = new Owner_BranchProfile();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout_ownerUserProfile, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        ownerProfile_FullName = rootView.findViewById(R.id.ownerProfile_FullName_Texview);
        ownerProfile_CompanyName = rootView.findViewById(R.id.ownerProfile_Company_Texview);
        ownerProfile_Firstname = rootView.findViewById(R.id.ownerProfile_Firstname_Texview);
        ownerProfile_Lastname = rootView.findViewById(R.id.ownerProfile_Lastname_Texview);
        ownerProfile_IDNumber = rootView.findViewById(R.id.ownerProfile_IDNumber_Texview);
        ownerProfile_Email = rootView.findViewById(R.id.ownerProfile_Email_Texview);

        retrieveOwnerUserDetails();

        ownerEditUserProfileButton = rootView.findViewById(R.id.ownerEditUserProfile_Button);
        ownerEditUserProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit User Profile");
                builder.setMessage("Are you sure you want to edit your profile?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        popupEditDetails();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return rootView;
    }

    public void retrieveOwnerUserDetails(){

        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseFirestore.collection("OwnerUserAccounts")
                .whereEqualTo("ownerIDNum", user)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                // Retrieve owner user details from Firestore
                                String companyName = documentSnapshot.getString("ownerCompanyName");
                                String firstName = documentSnapshot.getString("ownerFirstname");
                                String lastName = documentSnapshot.getString("ownerLastname");
                                String idNumber = documentSnapshot.getString("ownerIDNum");
                                String email = documentSnapshot.getString("ownerEmail");

                                // Set the retrieved details to the UI
                                ownerProfile_FullName.setText(firstName + " " + lastName);
                                ownerProfile_CompanyName.setText(companyName);
                                ownerProfile_Firstname.setText(firstName);
                                ownerProfile_Lastname.setText(lastName);
                                ownerProfile_IDNumber.setText(idNumber);
                                ownerProfile_Email.setText(email);

                            }
                        }
                    }
                });

    }

    public void popupEditDetails(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());
        final View ownerEditDetailsPopup = getLayoutInflater().inflate(R.layout.owner_userprofileedit_popup,null);
        EditText ownerProfile_CompanyNamePOPUP = (EditText) ownerEditDetailsPopup.findViewById(R.id.ownerProfile_Company_Edittext);
        EditText ownerProfile_FirstnamePOPUP = (EditText) ownerEditDetailsPopup.findViewById(R.id.ownerProfile_Firstname_Edittext);
        EditText ownerProfile_LastnamePOPUP = (EditText) ownerEditDetailsPopup.findViewById(R.id.ownerProfile_Lastname_Edittext);
        TextView ownerProfile_IDNum_POPUP = (TextView) ownerEditDetailsPopup.findViewById(R.id.ownerProfile_IDNumber_TexviewPOPUP);
        TextView ownerProfile_Email_POPUP = (TextView) ownerEditDetailsPopup.findViewById(R.id.ownerProfile_Email_TexviewPOPUP);

        Button cancel = (Button) ownerEditDetailsPopup.findViewById(R.id.ownerCancelEditUserProfile_Button);
        Button edit = (Button) ownerEditDetailsPopup.findViewById(R.id.ownerEditUserProfile_Button);

        builder.setView(ownerEditDetailsPopup);
        Dialog dialog;
        dialog = builder.create();
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getContext(), "cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        ownerProfile_CompanyNamePOPUP.setText(ownerProfile_CompanyName.getText());
        ownerProfile_FirstnamePOPUP.setText(ownerProfile_Firstname.getText());
        ownerProfile_LastnamePOPUP.setText(ownerProfile_Lastname.getText());
        ownerProfile_IDNum_POPUP.setText(ownerProfile_IDNumber.getText());
        ownerProfile_Email_POPUP.setText(ownerProfile_Email.getText());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedCompanyName = ownerProfile_CompanyNamePOPUP.getText().toString().trim();
                String updatedFirstName = ownerProfile_FirstnamePOPUP.getText().toString().trim();
                String updatedLastName = ownerProfile_LastnamePOPUP.getText().toString().trim();

                if (updatedCompanyName.isEmpty()) {
                    ownerProfile_CompanyNamePOPUP.setError("Please enter company name");
                    ownerProfile_CompanyNamePOPUP.requestFocus();
                    return;
                }
                if (updatedFirstName.isEmpty()) {
                    ownerProfile_FirstnamePOPUP.setError("Please enter first name");
                    ownerProfile_FirstnamePOPUP.requestFocus();
                    return;
                }
                if (updatedLastName.isEmpty()) {
                    ownerProfile_LastnamePOPUP.setError("Please enter last name");
                    ownerProfile_LastnamePOPUP.requestFocus();
                    return;
                }

                String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference ownerUserDocRef = firebaseFirestore.collection("OwnerUserAccounts").document(user);

                ownerUserDocRef.update("ownerCompanyName", updatedCompanyName,
                        "ownerFirstname", updatedFirstName,
                        "ownerLastname", updatedLastName)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                retrieveOwnerUserDetails();
                            }
                        });

            }
        });


    }
}