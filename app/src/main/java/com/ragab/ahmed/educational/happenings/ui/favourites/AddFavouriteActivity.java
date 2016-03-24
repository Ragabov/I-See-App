package com.ragab.ahmed.educational.happenings.ui.favourites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ragab.ahmed.educational.happenings.R;

public class AddFavouriteActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    public static String LATITUDE = "lat";
    public static String LONGITUDE = "lng";
    public static String NAME = "favourite_name";
    private LatLng favourite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favourite);
        final EditText editText = ((EditText)this.findViewById(R.id.favourite_name_text));
        final Activity activity = this;
        ((ImageButton) this.findViewById(R.id.add_favourite_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favourite == null) {
                    Toast.makeText(activity, "Click a place on the map", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editText.getText() == null || editText.getText().length() == 0) {
                    Toast.makeText(activity, "Enter a name in the text box", Toast.LENGTH_SHORT).show();
                    editText.setSelected(true);
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(LATITUDE, favourite.latitude);
                intent.putExtra(LONGITUDE, favourite.longitude);
                intent.putExtra(NAME, editText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        ((ImageButton)this.findViewById(R.id.cancel_favourite_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        setUpMapIfNeeded();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Favourite Location"));
                mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(50000)
                        .visible(true)
                        .fillColor(R.color.color_accent));
                favourite = latLng;
            }
        });

    }
}
