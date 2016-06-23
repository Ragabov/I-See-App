package com.ragab.ahmed.educational.happenings.network;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.ragab.ahmed.educational.happenings.ui.MainActivity;

/**
 * Created by Ragabov on 6/8/2016.
 */
public class LocationHelper implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener
{
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    static LocationHelper locationHelper;

    private final int DISPLACEMENT_TOLERANCE = 100 ;
    private long updateTimeInterval;
    private com.google.android.gms.location.LocationListener locationListener;
    private GoogleApiClient mGoogleApiClient;

    private Context mContext;

    private Location mCurrentLocation;

    private LocationRequest mLocationRequest;

    public LocationHelper(Context context, long updateInterval, com.google.android.gms.location.LocationListener locationListener)
    {
        updateTimeInterval = updateInterval;
        mContext = context;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mLocationRequest = new LocationRequest();

        if (updateInterval > 0) {
            mLocationRequest.setInterval(updateInterval);
            this.locationListener = locationListener;
        }

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT_TOLERANCE);

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates locationSettingsStates = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        if (updateTimeInterval > 0)
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                mGoogleApiClient, mLocationRequest,LocationHelper.this);
                        else
                            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            Activity activity = (Activity) mContext;
                            status.startResolutionForResult(
                                    activity,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (ClassCastException ex) {
                            // ignore
                        }
                        catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.

                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        locationListener.onLocationChanged(mCurrentLocation);
    }

    public Location getCurrentLocation()
    {
        return mCurrentLocation;
    }

}
