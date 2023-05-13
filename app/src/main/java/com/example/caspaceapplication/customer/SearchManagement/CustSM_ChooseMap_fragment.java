package com.example.caspaceapplication.customer.SearchManagement;

import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.caspaceapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class CustSM_ChooseMap_fragment extends Fragment implements OnMapReadyCallback {

    //todo: this fragment will display a map with pinned cws everywhere (all)
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private MapView mMapView;
    private LocationManager locationManager;

    public CustSM_ChooseMap_fragment() {

    }

    //todo: choose map lets user enter location then it will plot the cws on the map

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cust_s_m__choose_map_fragment, container, false);

        mMapView = rootView.findViewById(R.id.map_viewAll);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap; // initialize mMap object
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference coworkingSpacesRef = db.collection("CospaceBranches");

        coworkingSpacesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int count = 0;
                double totalLat = 0;
                double totalLong = 0;

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    GeoPoint geoPoint = documentSnapshot.getGeoPoint("location");
                    String coworkingSpaceName = documentSnapshot.getString("cospaceName");
                    if (geoPoint != null && coworkingSpaceName != null) {
                        double latitude = geoPoint.getLatitude();
                        double longitude = geoPoint.getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);

                        mMap.addMarker(new MarkerOptions().position(latLng).title(coworkingSpaceName));

                        count++;
                        totalLat += latitude;
                        totalLong += longitude;

                    }
                }
                // zooms to a n average center point where there are mostly cws marked
                if (count > 0){
                    double avgLat = totalLat / count;
                    double avgLong = totalLong / count;
                    LatLng avgCenter = new LatLng(avgLat, avgLong);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(avgCenter, 12));
                }

               }
        });
    }


}