package com.ragab.ahmed.educational.happenings.ui.map;

/**
 * Created by Ragabov on 3/9/2016.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.GridBasedAlgorithm;
import com.google.maps.android.clustering.algo.PreCachingAlgorithmDecorator;
import com.ragab.ahmed.educational.happenings.R;
import com.ragab.ahmed.educational.happenings.data.models.Event;
import com.ragab.ahmed.educational.happenings.network.ApiHelper;
import com.ragab.ahmed.educational.happenings.network.IseeApi;
import com.ragab.ahmed.educational.happenings.ui.MainActivity;
import com.ragab.ahmed.educational.happenings.ui.map.clustering.CustomRenderer;
import com.ragab.ahmed.educational.happenings.ui.utility.DatePickerFragment;
import com.ragab.ahmed.educational.happenings.ui.utility.DateReciever;
import com.ragab.ahmed.educational.happenings.ui.utility.TimePickerFragment;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainMapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener, Callback<ArrayList<Event>>{

    private static int MIN_ZOOM = 5 ;
    private static int SECTION_NUMBER = 0;
    private static String LATITUDE = "lat";
    private static String LONGITUDE = "lng";

    public float currentZoom = 0;
    public LatLngBounds currentBounds;
    private boolean isMapReady = false;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ClusterManager<Event> mClusterManager;
    private IseeApi mApi;

    public int categoryId = 0;
    public  boolean isStartDate = true;
    public Long startDate, endDate;

    public static MainMapFragment newInstance(double lat, double lng) {
        MainMapFragment fragment = new MainMapFragment();
        Bundle args = new Bundle();
        args.putDouble(LATITUDE, lat);
        args.putDouble(LONGITUDE, lng);
        fragment.setArguments(args);
        return fragment;
    }

    public MainMapFragment()
    {
        mApi = ApiHelper.buildApi();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMapAsync(this);
        setUpMapIfNeeded();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_map, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
        isMapReady = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.start_date_picker:
                isStartDate = true;
                DialogFragment newFragment1 = new DatePickerFragment();
                newFragment1.show(getActivity().getSupportFragmentManager(), "datePicker");
                return true;

            case R.id.end_date_picker:
                isStartDate = false;
                DialogFragment newFragment2 = new DatePickerFragment();
                newFragment2.show(getActivity().getSupportFragmentManager(), "datePicker");
                return true;

            default:
                if (!item.isCheckable())
                    return super.onOptionsItemSelected(item);

                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                char lIndex = item.getNumericShortcut();
                if (Character.isDigit(lIndex))
                {
                    categoryId = Short.parseShort(Character.toString(lIndex));
                }
                else {
                    categoryId = 0;
                }
                loadEvents(currentBounds);
                return true;
        }
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

    private void setUpClusterer()
    {
        mClusterManager = new ClusterManager<Event>(getActivity(), mMap);
        mClusterManager.setRenderer(new CustomRenderer(getActivity(), mMap, mClusterManager, this));
        mClusterManager.setAlgorithm(new GridBasedAlgorithm<Event>());
        mMap.setOnCameraChangeListener(this);
        mMap.setOnMarkerClickListener(mClusterManager);

    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        double lat = getArguments().getDouble(LATITUDE);
        double lng = getArguments().getDouble(LONGITUDE);

        LatLng moveTo = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moveTo, 10));
        setUpClusterer();
    }
    @Override

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(SECTION_NUMBER);
    }

    //Load events based on camera target
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (cameraPosition.zoom < MIN_ZOOM) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(MIN_ZOOM));
        }
        else {
            LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
            currentZoom = cameraPosition.zoom;
            if (!bounds.equals(currentBounds)){
                mApi.getEvents(bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude,
                        startDate, endDate, categoryId).enqueue(this);
            }
            currentBounds = bounds;
            mClusterManager.onCameraChange(cameraPosition);

        }
    }

    public void loadEvents (LatLngBounds bounds)
    {
        mApi.getEvents(bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude,
                startDate, endDate, categoryId).enqueue(this);
    }

    @Override
    public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            mClusterManager.clearItems();
            if (response.body() != null)
                mClusterManager.addItems(response.body());
            mClusterManager.cluster();
        }
        else
        {
            Toast.makeText(getActivity(),getString(R.string.operation_failure), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
        Toast.makeText(getActivity(),getString(R.string.operation_failure), Toast.LENGTH_SHORT).show();
    }
}
