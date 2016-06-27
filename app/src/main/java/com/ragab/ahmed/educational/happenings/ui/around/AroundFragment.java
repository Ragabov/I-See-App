package com.ragab.ahmed.educational.happenings.ui.around;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.ragab.ahmed.educational.happenings.R;
import com.ragab.ahmed.educational.happenings.data.models.Event;
import com.ragab.ahmed.educational.happenings.data.models.User;
import com.ragab.ahmed.educational.happenings.network.ApiHelper;
import com.ragab.ahmed.educational.happenings.network.IseeApi;
import com.ragab.ahmed.educational.happenings.network.LocationHelper;
import com.ragab.ahmed.educational.happenings.network.around.AroundAlarmReciever;
import com.ragab.ahmed.educational.happenings.ui.MainActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AroundFragment extends  Fragment implements LocationListener{

    public static String USER_ID = "userid";
    public static String USER_NAME = "username";

    private static String GET_AROUND_KEY = "garound";
    private boolean notifyOn = false;

    public static final String AROUND_FILE = "around_file";
    SharedPreferences sharedPreferences;

    private static long THIRTY_SECONDS_PERIOD = 30000;

    private LocationHelper locationHelper;
    private IseeApi mApi;
    private ProgressDialog mDialog;
    private MainActivity mainActivity;
    private AroundAdapter aroundAdapter;
    private int SECTION_NUMBER = 1;
    public AroundFragment() {
        // Required empty public constructor
        mApi = ApiHelper.buildApi();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
        mainActivity.onSectionAttached(SECTION_NUMBER);
        sharedPreferences = mainActivity.getSharedPreferences(AROUND_FILE, 0);
        notifyOn = sharedPreferences.getBoolean(GET_AROUND_KEY, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null)
        {
            notifyOn = savedInstanceState.getBoolean(GET_AROUND_KEY);
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_around, container, false);

        RecyclerView eventsCards = (RecyclerView)view.findViewById(R.id.around_recycler_view);
        eventsCards.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        eventsCards.setLayoutManager(layoutManager);
        aroundAdapter = new AroundAdapter(new ArrayList<Event>(), getActivity());
        locationHelper = new LocationHelper(getActivity(), THIRTY_SECONDS_PERIOD, this);
        eventsCards.setAdapter(aroundAdapter);
        mDialog = new ProgressDialog(mainActivity);
        mDialog.setProgress(50);
        mDialog.setCancelable(false);
        mDialog.setMessage(getString(R.string.around_loading_progress));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.around, menu);

        MenuItem item =menu.findItem(R.id.around_switch);
        SwitchCompat switchCompat = (SwitchCompat)item.getActionView();
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    cancelAlarm();
                }
                notifyOn = isChecked;
                sharedPreferences.edit().putBoolean(GET_AROUND_KEY, isChecked).commit();
            }
        });

        switchCompat.setChecked(notifyOn);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStop() {
        if (notifyOn)
        {
            scheduleAlarm();
        }
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(GET_AROUND_KEY, notifyOn);
        super.onSaveInstanceState(outState);
    }

    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getActivity().getApplicationContext(), AroundAlarmReciever.class);
        intent.putExtra(USER_NAME, mainActivity.mUser.fname);
        intent.putExtra(USER_ID, mainActivity.mUser.id);

        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(getActivity(), AroundAlarmReciever.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES/3 , pIntent);
    }
    public void cancelAlarm() {
        Intent intent = new Intent(getActivity().getApplicationContext(), AroundAlarmReciever.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(getActivity(), AroundAlarmReciever.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

    @Override
    public void onLocationChanged(Location location) {
        mDialog.show();
        mApi.getEventsAround(mainActivity.mUser.id, location.getLatitude(), location.getLongitude()).enqueue(new Callback<ArrayList<Event>>() {

            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                aroundAdapter.clearItems();
                if (response.body() != null) {
                    aroundAdapter.addItems(response.body());
                    ArrayList<Integer> ids = new ArrayList<Integer>();
                    for (int i = 0 ; i < response.body().size(); i++)
                    {
                        ids.add(response.body().get(i).userID);
                    }
                    mApi.getUsers(ids).enqueue(new Callback<ArrayList<User>>() {
                        @Override
                        public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                            if (response.body() != null) {
                                aroundAdapter.addUsersInfo(response.body());
                                aroundAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Toast.makeText(mainActivity, "FAAAA!", Toast.LENGTH_LONG);
                            }
                            mDialog.hide();
                        }

                        @Override
                        public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                            Toast.makeText(mainActivity, "FAAAA!", Toast.LENGTH_LONG);
                            mDialog.hide();
                        }
                    });
                }
                else
                {
                    mDialog.hide();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                Toast.makeText(mainActivity, "FAAAA!", Toast.LENGTH_LONG);
                mDialog.hide();
            }
        });
    }
}