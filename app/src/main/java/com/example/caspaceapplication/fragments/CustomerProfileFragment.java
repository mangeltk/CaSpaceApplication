package com.example.caspaceapplication.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomerProfileFragment extends Fragment {

    public EditText customerFirstName, customerLastName, customerEmail, customerOrganization,customerPopulation,customerPassword;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String customersIDNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_profile, container, false);

        customerFirstName =view.findViewById(R.id.customerFirstname);
        customerLastName = view.findViewById(R.id.customerLastname);
        customerEmail = view.findViewById(R.id.customerEmail);
        customerOrganization = view.findViewById(R.id.customerOrganization);
        customerPopulation=view.findViewById(R.id.customerPopulation);
        /*customerPassword=view.findViewById(R.id.customerPassword);*/

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        customersIDNum = fAuth.getCurrentUser().getUid();
        retrieveMethod();

        return view;


    }
    Button customerEditProfileButton;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customerEditProfileButton=(Button) view.findViewById(R.id.customerEditProfileButton);
        customerEditProfileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CustomerEditProfileFragment.class) ;
               /* View viewPopupwindow = inflater.inflate(R.layout.fragment_customer_edit_profile, null);
                PopupWindow popupWindow = new PopupWindow(viewPopupwindow, 1100, 1500, true);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);*/
                openFragmentB();

            }
        });

    }
    private void openFragmentB() {
        CustomerEditProfileFragment fragmentB = new CustomerEditProfileFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.customerViewProfile, fragmentB);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void retrieveMethod(){
        Task<DocumentSnapshot> documentReference = fStore.collection("CustomerUserAccounts").document(customersIDNum)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String firstname  = documentSnapshot.getString("customersFirstName");
                            customerFirstName.setText(firstname);
                            String lastname  = documentSnapshot.getString("customersLastName");
                            customerLastName.setText(lastname);
                            String email  = documentSnapshot.getString("customersEmail");
                            customerEmail.setText(email);
                            String organization = documentSnapshot.getString("customersOrganization");
                            customerOrganization.setText(organization);
                            String population = documentSnapshot.getString("customersPopulation");
                            customerPopulation.setText(population);
                         /*   String password = documentSnapshot.getString("customersPassword");
                            customerPassword.setText(password);*/

                        }
                    }
                });

    }

}
