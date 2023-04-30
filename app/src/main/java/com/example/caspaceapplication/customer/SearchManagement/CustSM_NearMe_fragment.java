package com.example.caspaceapplication.customer.SearchManagement;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.caspaceapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class CustSM_NearMe_fragment extends Fragment implements OnMapReadyCallback {

    //todo: this fragment will display pinned cws on a map (N) away from location
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 15;
    private GoogleMap mMap;
    private MapView mMapView;
    private View mView;
    private LocationManager locationManager;
    private FusedLocationProviderClient locationClient;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference coworkingSpacesRef = firebaseFirestore.collection("CospaceBranches");

    //todo: this fragment will display pinned cws on a map (N) away from location

    public CustSM_NearMe_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cust_s_m__near_me_fragment, container, false);

        mMapView = rootView.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

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

    private void addMarkerToMap(LatLng location, String title, String snippet) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(location)
                .title(title)
                .snippet(snippet);

        mMap.addMarker(markerOptions);
    }

    private void moveCameraToLocation(Location location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Check if the user has granted location permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Enable the location layer on the map
            mMap.setMyLocationEnabled(true);
            // Get the user's current location
            FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        // Move the camera to the user's current location
                        moveCameraToLocation(location);
                        LatLng userLoc = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(userLoc).title("Your location"))
                                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icons_user_location));

                        coworkingSpacesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                    LatLng coworkingSpaceLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                                    // Create a CircleOptions object and set its properties
                                    CircleOptions circleOptions = new CircleOptions()
                                            .center(coworkingSpaceLatLng)
                                            .radius(3000) // In meters
                                            .strokeWidth(2)
                                            .strokeColor(Color.RED)
                                            .fillColor(Color.argb(1, 255, 0, 0));
                                    // Add the circle to the map
                                    Circle circle = mMap.addCircle(circleOptions);

                                    GeoPoint geoPoint = documentSnapshot.getGeoPoint("location");
                                    String coworkingSpaceName = documentSnapshot.getString("cospaceName");
                                    if (geoPoint != null && coworkingSpaceName != null){
                                        double latitude = geoPoint.getLatitude();
                                        double longitude = geoPoint.getLongitude();
                                        LatLng latLng = new LatLng(latitude, longitude);

                                        Location userLocation = new Location("");
                                        userLocation.setLatitude(geoPoint.getLatitude());
                                        userLocation.setLongitude(geoPoint.getLongitude());

                                        float[] distance = new float[1];
                                        Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                                                userLocation.getLatitude(), userLocation.getLongitude(),
                                                distance);
                                        // convert the distance to kilometers
                                        float distanceInKm = distance[0] / 1000;
                                        int km = Math.round(distanceInKm);
                                        //"km" as the calculated distance between the current user's location and the cws
                                        if(km<=3)
                                            mMap.addMarker(new MarkerOptions().position(latLng).title(coworkingSpaceName)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icons_office));

                                    }

                                }
                            }
                        });
                    }
                }
            });
        }else {
            // Request location permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
}
