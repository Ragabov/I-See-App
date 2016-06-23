package com.ragab.ahmed.educational.happenings.network.around;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ragab.ahmed.educational.happenings.ui.around.AroundFragment;

/**
 * Created by Ragabov on 6/19/2016.
 */
public class AroundAlarmReciever extends BroadcastReceiver {
    public static final int REQUEST_CODE = 101;

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AroundIntentService.class);

        i.putExtra(AroundIntentService.USER_ID, intent.getLongExtra(AroundFragment.USER_ID, -1));
        i.putExtra(AroundIntentService.USER_NAME, intent.getStringExtra(AroundFragment.USER_NAME));
        context.startService(i);
    }
}
