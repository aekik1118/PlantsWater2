package com.example.won.plantswater;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by aekik on 2018-01-06.
 */

public class myAlarmManager {


    public static final String TAG = "myAlarmManger";

    private Context context;
    private static myAlarmManager mAM;
    private AlarmManager AM;

    private myAlarmManager(Context context) {this.context = context;}

    public static myAlarmManager getInstance(Context context) {
        if (mAM == null) {
            mAM = new myAlarmManager(context);
        }

        return mAM;
    }

    public void setAlarm(String time)
    {
        AM = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context,myAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,0,intent,0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,10);
        calendar.set(Calendar.MINUTE,15);
        calendar.set(Calendar.SECOND,0);

        AM.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),sender);
        Log.d(TAG, "setAM");
    }




}
