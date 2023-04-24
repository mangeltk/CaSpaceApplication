package com.example.caspaceapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.caspaceapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class CustomerEditProfileFragment extends Fragment {
    private EditText customerDisplayFirstName, customerDisplayLastName, customerDisplayEmail, customerDisplayOrganization,
            customerDisplayPopulation,customerDisplayPassword;
    private Button customerEditProfileSave;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String customersIDNum;
    private static final String TAG = "CustomerEditProfileFragment";


    public CustomerEditProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_edit_profile, container, false);
        customerDisplayFirstName = view.findViewById(R.id.customerEditFirstName);
        customerDisplayLastName = view.findViewById(R.id.customerEditLastName);
        customerDisplayEmail = view.findViewById(R.id.customerEditEmail);
        customerDisplayOrganization = view.findViewById(R.id.customerEditOrganization);
        customerDisplayPopulation=view.findViewById(R.id.customerEditPopulation);
      /*  customerDisplayPassword=view.findViewById(R.id.customerEditPassword);*/
        customerEditProfileSave=view.findViewById(R.id.customerEditProfileSaveButton);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        customersIDNum = fAuth.getCurrentUser().getUid();
        Task<DocumentSnapshot> documentReference = fStore.collection("CustomerUserAccounts").document(customersIDNum)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String firstname  = documentSnapshot.getString("customersFirstName");
                            customerDisplayFirstName.setText(firstname);
                            String lastname  = documentSnapshot.getString("customersLastName");
                            customerDisplayLastName.setText(lastname);
                            String email  = documentSnapshot.getString("customersEmail");
                            customerDisplayEmail.setText(email);
                            String organization = documentSnapshot.getString("customersOrganization");
                            customerDisplayOrganization.setText(organization);
                            String population = documentSnapshot.getString("customersPopulation");
                            customerDisplayPopulation.setText(population);
                            /*String password = documentSnapshot.getString("customersPassword");
                            customerDisplayPassword.setText(password);*/

                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"error"+e);
                    }
                });
                customerEditProfileSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newFirstName= customerDisplayFirstName.getText().toString();
                        String newLastName = customerDisplayLastName.getText().toString();
                        String newEmail = customerDisplayEmail.getText().toString();
                        String newOrganization = customerDisplayOrganization.getText().toString();
                        String newPopulation = customerDisplayPopulation.getText().toString();
                       /* String newPassword = customerDisplayPassword.getText().toString();*/

                        Map<String,Object> updates = new HashMap<>();
                        updates.put("customersFirstName",newFirstName);
                        updates.put("customersLastName",newLastName);
                        updates.put("customersEmail",newEmail);
                        updates.put("customersOrganization",newOrganization);
                        updates.put("customersPopulation",newPopulation);
                        /*updates.put("customersPassword",newPassword);*/
                        fStore.collection("CustomerUserAccounts").document(customersIDNum).update(updates)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"Update Success",Toast.LENGTH_SHORT).show();
                                        getActivity().onBackPressed();
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(),"Update Fail",Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                });



        return view;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }

}