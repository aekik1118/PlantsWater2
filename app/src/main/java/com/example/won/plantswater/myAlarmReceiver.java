package com.example.won.plantswater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by aekik on 2018-01-06.
 */

public class myAlarmReceiver extends BroadcastReceiver {

    public static final String TAG = "myAlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent Serviceintent = new Intent(context,MyAlarmService.class);

        Log.d(TAG, "onReceive");

        context.startService(Serviceintent);
    }

}