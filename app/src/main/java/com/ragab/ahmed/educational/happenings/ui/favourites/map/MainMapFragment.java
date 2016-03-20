package com.ragab.ahmed.educational.happenings.ui.favourites.map;

/**
 * Created by Ragabov on 3/9/2016.
 */

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ragab.ahmed.educational.happenings.R;
import com.ragab.ahmed.educational.happenings.ui.MainActivity;


public class MainMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private static int SECTION_NUMBER = 0;
    private static String LATITUDE = "lat";
    private static String LONGITUDE = "lng";

    private boolean isMapReady = false;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Location mLastLocation;

    public static MainMapFragment newInstance(double lat, double lng) {
        MainMapFragment fragment = new MainMapFragment();
        Bundle args = new Bundle();
        args.putDouble(LATITUDE, lat);
        args.putDouble(LONGITUDE, lng);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMapAsync(this);
        setUpMapIfNeeded();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
        isMapReady = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isMapReady)
            setUpMapIfNeeded();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
           getMapAsync(this);
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        LatLng sydney2 = new LatLng(-33.9995, 151);
        LatLng sydney3 = new LatLng(-34, 151.0005);
        LatLng sydney4 = new LatLng(-33.9995, 151.0005);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney")
                        .snippet("Population: 4,137,400")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.abc_ic_voice_search_api_mtrl_alpha))
        );


        mMap.addMarker(new MarkerOptions().position(sydney4).title("Marker in Sydney")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.abc_list_selector_disabled_holo_dark)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        double lat = getArguments().getDouble(LATITUDE);
        double lng = getArguments().getDouble(LONGITUDE);

        LatLng moveTo = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moveTo, 10));

    }

    @Override

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(SECTION_NUMBER);
    }

}
