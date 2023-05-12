package com.example.caspaceapplication.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.Owner.BranchModel;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.Front;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomerProfileFragment extends Fragment {

    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public TextView customerFirstName, customerLastName, customerEmail, customerOrganization,customerPopulation,customerPassword;
    public TextView firstName, lastName;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String customersIDNum;

    private ImageView profileImg;
    Uri filepath = null;
    private static final int GALLERY_CODE = 1;
    AppCompatButton seeMyFavoritesButton, seeMyHistoryTransactions;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference customerAccounts_colref = firebaseFirestore.collection("CustomerUserAccounts");

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    CustomerProfileFavorites_Adapter favorites_adapter;
    List<BranchModel> branchModelList;

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

        profileImg = view.findViewById(R.id.profileImg);
        firstName = view.findViewById(R.id.titleFirstname);
        lastName = view.findViewById(R.id.titleLastname);
        seeMyFavoritesButton = view.findViewById(R.id.seeMyFavorites_AppCompatButton);
        seeMyHistoryTransactions = view.findViewById(R.id.seeMyHistoryTransactions_AppCompatButton);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_CODE);
            }
        });

        seeMyFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: pop up for user's favorite cws
                dialogBuilder = new AlertDialog.Builder(getContext());
                final View myFavoritesPopup =getLayoutInflater().inflate(R.layout.recycleritem_customerprofile_popupbackground, null);
                TextView title =(TextView) myFavoritesPopup.findViewById(R.id.customerProfile_RecyclerBackground_Title);
                ImageView image = (ImageView) myFavoritesPopup.findViewById(R.id.customerProfile_RecyclerBackground_Image);
                RecyclerView favoritesRecyclerView = (RecyclerView) myFavoritesPopup.findViewById(R.id.customerProfile_RecyclerBackground_Recyclerview);

                title.setText("My List of favorites");
                image.setImageResource(R.drawable.icons_red_heart48);
                favoritesRecyclerView.setHasFixedSize(true);
                favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                branchModelList = new ArrayList<>();
                favorites_adapter = new CustomerProfileFavorites_Adapter(branchModelList);
                favoritesRecyclerView.setAdapter(favorites_adapter);
                firebaseFirestore.collection("CustomerLikes").whereEqualTo("userId", user)
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                    BranchModel modelClass = documentSnapshot.toObject(BranchModel.class);
                                    branchModelList.add(modelClass);
                                }
                                favorites_adapter.notifyDataSetChanged();
                            }
                        });


                dialogBuilder.setView(myFavoritesPopup);
                dialog = dialogBuilder.create();
                dialog.show();


            }
        });

        seeMyHistoryTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: pop up for completed bookings
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            filepath = data.getData();
            profileImg.setImageURI(filepath);
            StorageReference customerImageRef = FirebaseStorage.getInstance().getReference().child("Customer/" + user + "/profile");
            customerImageRef.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    customerImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            customerAccounts_colref.whereEqualTo("customersIDNum", user).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()){
                                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                            String docId = documentSnapshot.getId();
                                            customerAccounts_colref.document(docId).update("customerImage", uri.toString())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(getContext(), "Profile picture updated!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            });

        }
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
        Button deleteButton = view.findViewById(R.id.customerDeleteProfileButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete Account");
                builder.setMessage("Are you sure you want to delete your account?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        Button signOutButton = view.findViewById(R.id.customerSignOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Sign Out");
                builder.setMessage("Are you sure you want to sign out?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), Front.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void deleteAccount() {
        // Delete customer's data from Firestore
        fStore.collection("CustomerUserAccounts").document(customersIDNum)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        // Delete customer's account from Firebase Auth
        fAuth.getCurrentUser().delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Sign the customer out of the app
                            Intent intent = new Intent(getActivity(), Front.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Log.e(TAG, "Error deleting account", task.getException());
                        }
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
                            String customerImage = documentSnapshot.getString("customerImage");
                            if (customerImage!=null && !customerImage.isEmpty()){
                                Picasso.get().load(customerImage).into(profileImg);
                            }
                            String firstname  = documentSnapshot.getString("customersFirstName");
                            customerFirstName.setText(firstname);
                            firstName.setText(firstname);
                            String lastname  = documentSnapshot.getString("customersLastName");
                            customerLastName.setText(lastname);
                            lastName.setText(lastname);
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

    public class CustomerProfileFavorites_Adapter extends RecyclerView.Adapter<CustomerProfileFragment.CustomerProfileFavorites_Adapter.ViewHolder>{

        private List<BranchModel> branchModel;
        //dataclass that should have branchName, branchImage;


        public CustomerProfileFavorites_Adapter(List<BranchModel> branchModel) {
            this.branchModel = branchModel;
        }

        @NonNull
        @Override
        public CustomerProfileFavorites_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem_toplikedcws_cardview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomerProfileFavorites_Adapter.ViewHolder holder, int position) {
            //holder.rankNo.setText(branchModel.get(position).getRankNo());
            holder.branchName.setText(branchModel.get(position).getCospaceName());
            holder.branchAddress.setText(branchModel.get(position).getCospaceStreetAddress() + " " + branchModel.get(position).getCospaceCityAddress());
            holder.likesNo.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return branchModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView rankNo, branchName, branchAddress, likesNo;
            ImageView branchImage;
            CardView rankCardview;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                rankNo = itemView.findViewById(R.id.rankNo_Textview);
                branchImage = itemView.findViewById(R.id.rankCWSImage_Imageview);
                branchName = itemView.findViewById(R.id.rankCWSName_Textview);
                branchAddress = itemView.findViewById(R.id.rankCWSAddress_Textview);
                likesNo = itemView.findViewById(R.id.rankCWSLikes_Texview);
                rankCardview = itemView.findViewById(R.id.rankCardview);

            }
        }
    }

}
