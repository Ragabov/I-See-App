package com.ragab.ahmed.educational.happenings.ui;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.ragab.ahmed.educational.happenings.R;
import com.ragab.ahmed.educational.happenings.data.models.User;
import com.ragab.ahmed.educational.happenings.ui.around.AroundFragment;
import com.ragab.ahmed.educational.happenings.ui.drawer.NavigationDrawerFragment;
import com.ragab.ahmed.educational.happenings.ui.favourites.FavouritesFragment;
import com.ragab.ahmed.educational.happenings.ui.favourites.map.MainMapFragment;
import com.ragab.ahmed.educational.happenings.ui.login.LoginActivity;
import com.ragab.ahmed.educational.happenings.ui.submit.SubmitFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    /*
    Request codes
     */
    public static final int PERFORM_LOGIN = 10;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.3
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /*
    Storing the favourite fragment so as not to make a new instance each time
     */
    private FavouritesFragment favouritesFragment;

    /*
    Storing the around fragment so as not to make a new instance each time
     */
    private AroundFragment aroundFragment;

    /*
    Storing the around fragment so as not to make a new instance each time
     */
    private SubmitFragment submitFragment;

    /*
    Google Api Client for retrieving current location info
     */
    GoogleApiClient mGoogleApiClient;

    /*
    Check if use is logged in or not
     */
    boolean isLoggedIn = false;

    Location currentLocation;

    /*
    The current user object
     */
    public User mUser;
    /*
    save instance bundle keys
     */
    String STATE_USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Restore state
        if (savedInstanceState != null) {
            mUser = (User) savedInstanceState.getSerializable(STATE_USER);
        }

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mUser == null)
        {
            Intent intent = new Intent();
            intent.setClass(this.getApplicationContext(), LoginActivity.class);
            startActivityForResult(intent, PERFORM_LOGIN);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0 :
                if (favouritesFragment == null)
                {
                    favouritesFragment = new FavouritesFragment();
                }
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, favouritesFragment)
                        .commit();
                break;

            case 1 :
                if (aroundFragment == null)
                {
                    aroundFragment = new AroundFragment();
                }
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, aroundFragment)
                        .commit();
                break;

            case 2 :
                if (submitFragment == null)
                {
                    submitFragment = new SubmitFragment();
                }
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, submitFragment)
                        .commit();
                break;
        }

    }

    public void openEventsMap (double lat, double lng)
    {
        if (lat == -1 || lng == -1)
        {
            if (currentLocation != null)
            {
                lat = currentLocation.getLatitude();
                lng = currentLocation.getLongitude();
            }
            else
            {
                lat = 0;
                lng = 0;
            }
        }
        MainMapFragment mapFragment = MainMapFragment.newInstance(lat, lng);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mapFragment)
                .addToBackStack(null)
                .commit();
    }

    public void onSectionAttached(int number) {
        String[] titlesArray = getResources().getStringArray(R.array.navigation_titles);
        mTitle = titlesArray[number];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected  void onStop()
    {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    protected  void onStart()
    {
        mGoogleApiClient.connect();
        super.onStart();
    }
    @Override
    public void onConnected(Bundle bundle) {
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Api Client Connection Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode) {

                case PERFORM_LOGIN:
                    mUser = (User)data.getSerializableExtra(LoginActivity.USER_ARG);
                    if (mUser == null)
                        break;
                    mNavigationDrawerFragment.setUser(mUser);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        }
        else
        {
            if (requestCode == PERFORM_LOGIN)
            {
                //Failed to login
                finish();
                System.exit(0);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_USER, mUser);
        super.onSaveInstanceState(outState);
    }
}
