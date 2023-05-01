package com.example.caspaceapplication.Owner;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.R;
import com.example.caspaceapplication.customer.CWSProfile.CWS_ProfilePage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.MyViewHolder> {


    private Context context;
    private List<BranchModel> dataClassList;

    public BranchAdapter(Context context, List<BranchModel> dataClassList) {
        this.context = context;
        this.dataClassList = dataClassList;
    }

    @NonNull
    @Override
    public BranchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem_cws_cardview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchAdapter.MyViewHolder holder, int position) {

        String imageUri = String.valueOf(dataClassList.get(position).getCospaceImage());
        if (!imageUri.isEmpty()) {
            Picasso.get().load(imageUri).into(holder.branchImage);
        }
        holder.branchName.setText(dataClassList.get(position).getCospaceName());
        holder.branchStreetAddress.setText(dataClassList.get(position).getCospaceStreetAddress());
        holder.branchCityAddress.setText(dataClassList.get(position).getCospaceCityAddress());
        holder.branchCategory.setText(dataClassList.get(position).getCospaceCategory());
        holder.branchCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: redirect to CWS branch profile page with all info displayed
                int clickedPosition = holder.getAdapterPosition();
                Intent intent = new Intent(context, CWS_ProfilePage.class);
                intent.putExtra("cospaceName", dataClassList.get(clickedPosition).getCospaceName());
                intent.putExtra("owner_id", dataClassList.get(clickedPosition).getOwner_id());
                context.startActivity(intent);
            }
        });
        holder.showOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: show google map on with pinned location pop up already created name: enterlocation_googlemap_popup
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                String branchName = holder.branchName.getText().toString();

                firebaseFirestore.collection("CospaceBranches").whereEqualTo("cospaceName", branchName)
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()){
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                        GeoPoint location = documentSnapshot.getGeoPoint("location");
                                        if (location!=null){
                                            double branchlatitude = location.getLatitude();
                                            double branchlongitude = location.getLongitude();

                                            // Get user's current location
                                            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
                                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                                fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                                    @Override
                                                    public void onSuccess(Location location) {
                                                        if (location != null) {
                                                            double userLatitude = location.getLatitude();
                                                            double userLongitude = location.getLongitude();
                                                            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f (%s)&daddr=%f,%f", userLatitude, userLongitude, branchName, branchlatitude, branchlongitude);
                                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                                            intent.setPackage("com.google.android.apps.maps");
                                                            context.startActivity(intent);
                                                        }
                                                    }
                                                });
                                            }else {
                                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                                            }
                                        }
                                    }
                                }

                            }
                        });
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataClassList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView branchImage;
        TextView branchName, branchStreetAddress, branchCityAddress, branchCategory;
        CardView branchCardView;
        Button showOnMapButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            branchImage = itemView.findViewById(R.id.branchImage_cardview);
            branchName = itemView.findViewById(R.id.branchName_cardview);
            branchStreetAddress = itemView.findViewById(R.id.branchStreetAddress_cardview);
            branchCityAddress = itemView.findViewById(R.id.branchCityAddress_cardview);
            branchCategory = itemView.findViewById(R.id.branchCategory_cardview);
            branchCardView = itemView.findViewById(R.id.branch_cardView);
            showOnMapButton = itemView.findViewById(R.id.showOnMap_Button);

        }
    }
}
