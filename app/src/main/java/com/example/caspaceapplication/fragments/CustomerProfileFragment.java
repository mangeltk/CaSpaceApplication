package com.example.caspaceapplication.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
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

import com.example.caspaceapplication.ModelClasses.BookingDetails_ModelClass;
import com.example.caspaceapplication.ModelClasses.MyFavorites_ModelClass;
import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.Front;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerProfileFragment extends Fragment {

    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public TextView customerFirstName, customerLastName, customerEmail, customerOrganization,customerPopulation;
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
    CollectionReference customerLikes_colref = firebaseFirestore.collection("CustomerLikes");
    CollectionReference CospaceBranches_colref = firebaseFirestore.collection("CospaceBranches");
    CollectionReference BookingTransactions_colref = firebaseFirestore.collection("CustomerSubmittedBookingTransactions");

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    CustomerProfileFavorites_Adapter favorites_adapter;
    CustomerProfileTransactionHistory_Adapter transactionHistory_adapter;
    List<MyFavorites_ModelClass> favoritesList;
    List<BookingDetails_ModelClass> bookingDetailsModelClassList;

    String cospaceName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_profile, container, false);

        customerFirstName =view.findViewById(R.id.customerFirstname);
        customerLastName = view.findViewById(R.id.customerLastname);
        customerEmail = view.findViewById(R.id.customerEmail);
        customerOrganization = view.findViewById(R.id.customerOrganization);
        customerPopulation=view.findViewById(R.id.customerPopulation);


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
                retrieveFavorites();
            }
        });

        seeMyHistoryTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: pop up for completed bookings
                // retrieve completed and cancelled bookings.
                retrieveTransactionHistory();
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
                                            customerAccounts_colref.document(docId).update("customer_image", uri.toString())
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

                        deleteAccountEmail();
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
                        customerUserActivity();
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

    public void customerUserActivity(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String customerId = fAuth.getCurrentUser().getUid();
        String activity = "Sign Out";

        Map<String, Object> data = new HashMap<>();
        data.put("customerId",customerId);
        data.put("activity", activity);
        data.put("dateTime", Timestamp.now());

        db.collection("CustomerActivity")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Activity Stored.");
                    }
                });


    }
    private  void deleteAccountEmail(){
      /*  final String email = "forcaspace@gmail.com";
        final String password = "xvgqwzvcxvkvqtff";
        final String recipientEmail = customerEmail.getText().toString();
        final String subject = "Account Deletion";
        final String messageBody = "Good day, you have requested an account deletion. Please give the admins a 3 days to delete your account." +
                "\n After 3 days, the CaSpace will email you to let you know that your account deletion is complete."+
                "\n If you wish to cancel your account deletion, please reply to this email."+
                "\n\n\n Best regards,\nCaSpace Team";
        ;
        // Create a new thread to send the email
        new Thread(() -> {
            try {
                // Create email properties
                Properties props = new Properties();
                props.put("mail.smtp.auth","true");
                props.put("mail.smtp.starttls.enable","true");
                props.put("mail.smtp.host","smtp.gmail.com");
                props.put("mail.smtp.port","587");

                // Create a session with authentication
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });
                // Create email message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(email));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject(subject);
                message.setText(messageBody);
                // Send the email'
                Transport.send(message);

                // Display a Toast or perform any UI updates on the main thread if needed
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Email sent", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MessagingException e) {
                e.printStackTrace();
                // Display a Toast or perform any UI updates using a Handler
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Failed to send email", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
*/
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
                            String customerImage = documentSnapshot.getString("customer_image");
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

    public void retrieveFavorites(){
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View myFavoritesPopup =getLayoutInflater().inflate(R.layout.recycleritem_customerprofile_popupbackground, null);
        TextView title =(TextView) myFavoritesPopup.findViewById(R.id.customerProfile_RecyclerBackground_Title);
        ImageView image = (ImageView) myFavoritesPopup.findViewById(R.id.customerProfile_RecyclerBackground_Image);
        RecyclerView favoritesRecyclerView = (RecyclerView) myFavoritesPopup.findViewById(R.id.customerProfile_RecyclerBackground_Recyclerview);
        AppCompatButton closePopupDialog_AppCompatButton = (AppCompatButton) myFavoritesPopup.findViewById(R.id.closePopupDialog_AppCompatButton);
        closePopupDialog_AppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Close my favorites list", Toast.LENGTH_SHORT).show();
            }
        });
        title.setText("My List of favorites");
        image.setImageResource(R.drawable.icons_red_heart48);
        favoritesRecyclerView.setHasFixedSize(true);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        favoritesList = new ArrayList<>();
        favorites_adapter = new CustomerProfileFavorites_Adapter(favoritesList);
        favoritesRecyclerView.setAdapter(favorites_adapter);
        customerLikes_colref.whereEqualTo("userId", user)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            MyFavorites_ModelClass modelClass = documentSnapshot.toObject(MyFavorites_ModelClass.class);
                            favoritesList.add(modelClass);
                        }
                        favorites_adapter.notifyDataSetChanged();
                    }
                });

        dialogBuilder.setView(myFavoritesPopup);
        dialog = dialogBuilder.create();
        dialog.show();

    }

    public void unlikeFavoriteBranch(){
        customerLikes_colref.whereEqualTo("userId", user).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            firebaseFirestore.collection("CustomerLikes").document(documentSnapshot.getId())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "CustomerLike document deleted successfully");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "Error deleting CustomerLike document", e);
                                        }
                                    });
                        } else {
                            Log.w(TAG, "No matching documents found in CustomerLikes collection");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting documents from CustomerLikes collection", e);
                    }
                });

        // Decrement the Likes count in the BranchInfo document
        CospaceBranches_colref.whereEqualTo("cospaceName", cospaceName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            Map<String, Object> updates = new HashMap<>();
                            Long likesCount = (Long) documentSnapshot.get("Likes");
                            if (likesCount == null) {
                                likesCount = 0L;
                            }
                            updates.put("Likes", likesCount - 1);
                            documentSnapshot.getReference().update(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "Likes count decremented");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "Failed to decrement Likes count", e);
                                        }
                                    });
                        } else {
                            Log.w(TAG, "No matching documents found in BranchInfo collection");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to fetch document", e);
                    }
                });
    }

    public class CustomerProfileFavorites_Adapter extends RecyclerView.Adapter<CustomerProfileFragment.CustomerProfileFavorites_Adapter.ViewHolder>{

        private List<MyFavorites_ModelClass> favoritesList;

        public CustomerProfileFavorites_Adapter(List<MyFavorites_ModelClass> favoritesList) {
            this.favoritesList = favoritesList;
        }

        @NonNull
        @Override
        public CustomerProfileFavorites_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem_myfavorites_cardview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomerProfileFavorites_Adapter.ViewHolder holder, int position) {
            cospaceName = favoritesList.get(position).getBranchName();
            holder.branchName.setText(cospaceName);
            String imageUri = favoritesList.get(position).getBranchImage();
            if (imageUri!=null){
                Picasso.get().load(imageUri).into(holder.branchImage);
            }
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(getContext());
                    deleteDialogBuilder.setTitle("Unlike favorite branch confirmation");
                    deleteDialogBuilder.setMessage("Are you sure you want to delete and unlike this branch?");
                    deleteDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            unlikeFavoriteBranch();
                            Toast.makeText(getContext(), "Unlike " + cospaceName, Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    Dialog dialog;
                    dialog = deleteDialogBuilder.create();
                    dialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return favoritesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView branchName, branchAddress;
            ImageView branchImage;
            AppCompatButton deleteButton;
            CardView cardview;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                branchName = itemView.findViewById(R.id.myfavorites_BranchName_Textview);
                //branchAddress = itemView.findViewById(R.id.myfavorites_BranchAddress_Textview);
                branchImage = itemView.findViewById(R.id.myfavorites_Imageview);
                cardview = itemView.findViewById(R.id.myfavorites_Cardview);
                deleteButton = itemView.findViewById(R.id.myfavorites_Delete_AppCompatButton);

            }
        }
    }

    public void retrieveTransactionHistory(){
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View myTransactionHistoryPopup =getLayoutInflater().inflate(R.layout.recycleritem_customerprofile_popupbackground, null);
        AppCompatButton closePopupDialog_AppCompatButton = (AppCompatButton) myTransactionHistoryPopup.findViewById(R.id.closePopupDialog_AppCompatButton);
        TextView title =(TextView) myTransactionHistoryPopup.findViewById(R.id.customerProfile_RecyclerBackground_Title);
        ImageView image = (ImageView) myTransactionHistoryPopup.findViewById(R.id.customerProfile_RecyclerBackground_Image);
        RecyclerView transactionHistoryRecyclerview = (RecyclerView) myTransactionHistoryPopup.findViewById(R.id.customerProfile_RecyclerBackground_Recyclerview);

        title.setText("My Transaction History");
        image.setImageResource(R.drawable.icons_booking);
        closePopupDialog_AppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Close my transaction history list", Toast.LENGTH_SHORT).show();
            }
        });
        transactionHistoryRecyclerview.setHasFixedSize(true);
        transactionHistoryRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingDetailsModelClassList = new ArrayList<>();
        transactionHistory_adapter = new CustomerProfileTransactionHistory_Adapter(bookingDetailsModelClassList);
        transactionHistoryRecyclerview.setAdapter(transactionHistory_adapter);
        BookingTransactions_colref.whereEqualTo("customerId", user)
                .whereIn("bookingStatus", Arrays.asList("Cancelled", "Declined", "Completed"))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size() == 0){
                            // handle empty result
                        } else {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                BookingDetails_ModelClass modelClass = documentSnapshot.toObject(BookingDetails_ModelClass.class);
                                bookingDetailsModelClassList.add(modelClass);
                            }
                            transactionHistory_adapter.notifyDataSetChanged();
                        }
                    }
                });

        dialogBuilder.setView(myTransactionHistoryPopup);
        dialog = dialogBuilder.create();
        dialog.show();

    }

    public class CustomerProfileTransactionHistory_Adapter extends RecyclerView.Adapter<CustomerProfileFragment.CustomerProfileTransactionHistory_Adapter.ViewHolder>{

        private List<BookingDetails_ModelClass> dataClass;

        public CustomerProfileTransactionHistory_Adapter(List<BookingDetails_ModelClass> dataClass) {
            this.dataClass = dataClass;
        }

        @NonNull
        @Override
        public CustomerProfileTransactionHistory_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem_custbookingcardview, parent, false);
            return new CustomerProfileTransactionHistory_Adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomerProfileTransactionHistory_Adapter.ViewHolder holder, int position) {
            String branchImageUri = String.valueOf(dataClass.get(position).getBranchImage());
            if (branchImageUri != null && !branchImageUri.isEmpty()){
                Picasso.get().load(branchImageUri).into(holder.branchImage);
            }
            holder.branchName.setText(dataClass.get(position).getBranchName());
            String layoutImageUri = String.valueOf(dataClass.get(position).getLayoutImage());
            if (layoutImageUri !=null && !layoutImageUri.isEmpty()){
                Picasso.get().load(layoutImageUri).into(holder.layoutImage);
            }
            holder.layoutName.setText(dataClass.get(position).getLayoutName());
            holder.bookingStatus.setText(dataClass.get(position).getBookingStatus());
            holder.bookingRateType.setText(dataClass.get(position).getRateType());
            holder.bookingRateValue.setText("₱"+dataClass.get(position).getRateValue());
            holder.bookingStartDate.setText(dataClass.get(position).getBookingStartDate());
            holder.bookingTotalPayment.setText(dataClass.get(position).getTotalPayment());
            holder.seeMoreDetails.setText("");

        }

        @Override
        public int getItemCount() {
            return bookingDetailsModelClassList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView branchImage, layoutImage;
            TextView branchName, bookingStatus, layoutName, bookingRateType, bookingRateValue, bookingStartDate,bookingTotalPayment, seeMoreDetails;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                branchImage = itemView.findViewById(R.id.CustBookingBranchImage_Cardview);
                branchName = itemView.findViewById(R.id.CustBookingBranchName_Cardview);
                layoutImage = itemView.findViewById(R.id.CustBookingLayoutImage_Cardview);
                layoutName = itemView.findViewById(R.id.CustBookingLayoutName_Cardview);
                bookingStatus = itemView.findViewById(R.id.CustBookingBookingStatus_Cardview);
                bookingRateType = itemView.findViewById(R.id.CustBookingBookingRateType_Cardview);
                bookingRateValue = itemView.findViewById(R.id.CustBookingBookingRateValue_Cardview);
                bookingStartDate = itemView.findViewById(R.id.CustBookingBookingStartDate_Cardview);
                bookingTotalPayment = itemView.findViewById(R.id.CustBookingBookingTotalPayment_Cardview);
                seeMoreDetails = itemView.findViewById(R.id.CustBookingBookingSeeMoreTextview_Cardview);
            }
        }
    }

}
