package com.ragab.ahmed.educational.happenings.network.around;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.ragab.ahmed.educational.happenings.R;
import com.ragab.ahmed.educational.happenings.data.models.Event;
import com.ragab.ahmed.educational.happenings.network.ApiHelper;
import com.ragab.ahmed.educational.happenings.network.IseeApi;
import com.ragab.ahmed.educational.happenings.network.LocationHelper;
import com.ragab.ahmed.educational.happenings.ui.MainActivity;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ragabov on 6/19/2016.
 */
public class AroundIntentService extends IntentService {

    public static String USER_ID = "userId";
    public static String USER_NAME = "userName";

    private static int count = 0 ;
    private static int notificationId = 102;
    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */

    IseeApi mApi;
    LocationHelper locationHelper;
    public AroundIntentService() {
        super("AroundIntentService");
        mApi = ApiHelper.buildApi();
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(final Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.

        locationHelper = new LocationHelper(getApplicationContext(), -1, null);
        long userId = intent.getLongExtra(USER_ID, -1);
        if (userId == -1)
            return ;

        try {
            //Wait for location
            Thread.sleep(5000);

            if (locationHelper.getCurrentLocation() == null)
                return ;

            double latitude = locationHelper.getCurrentLocation().getLatitude();
            double longitude = locationHelper.getCurrentLocation().getLongitude();

            mApi.getEventsAround(userId, latitude, longitude).enqueue(new Callback<ArrayList<Event>>() {
                @Override
                public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                    if (response.body() != null)
                        notifyUser(intent.getStringExtra(USER_NAME), response.body().size());
                }

                @Override
                public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "FAAAIL", Toast.LENGTH_LONG).show();
                }
            });

        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

    protected void notifyUser (String username, long count)
    {
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap())
                        .setSmallIcon(R.mipmap.ic_notify)
                        .setSound(uri)
                        .setContentTitle(getString(R.string.around_notification_title))
                        .setContentText(String.format(getString(R.string.around_notification_content), username, count));
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

}
