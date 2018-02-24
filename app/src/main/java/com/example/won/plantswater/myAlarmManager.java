package com.example.won.plantswater;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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

    public void setAlarm(int id, int time, String name)
    {
        AM = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context,myAlarmReceiver.class);

        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTime(new Date());
        cal.add(Calendar.HOUR,time);

        if(id == 0)
        {
            Toast.makeText(context,"에러",Toast.LENGTH_SHORT).show();
            return;
        }

        intent.putExtra("pid",id);
        intent.putExtra("pname",name);

        Log.d(TAG, "set"+intent.getIntExtra("pid",0));
        Log.d(TAG, "set"+id);

        PendingIntent sender = PendingIntent.getBroadcast(context,id,intent,PendingIntent.FLAG_ONE_SHOT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            AM.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),sender);
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            AM.setExact(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),sender);
        }
        else
        {
            AM.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),sender);
        }
        Log.d(TAG, "setAM");
    }

    private long getTriggerAtMillis(int hourOfDay, int minute) {
        GregorianCalendar currentCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
        int currentHourOfDay = currentCalendar.get(GregorianCalendar.HOUR_OF_DAY);
        int currentMinute = currentCalendar.get(GregorianCalendar.MINUTE);

        if ( currentHourOfDay < hourOfDay || ( currentHourOfDay == hourOfDay && currentMinute < minute ) )
            return getTimeInMillis(false, hourOfDay, minute);
        else
            return getTimeInMillis(true, hourOfDay, minute);
    }
    private long getTimeInMillis(boolean tomorrow, int hourOfDay, int minute) {
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();

        if ( tomorrow )
            calendar.add(GregorianCalendar.DAY_OF_YEAR, 1);

        calendar.set(GregorianCalendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(GregorianCalendar.MINUTE, minute);
        calendar.set(GregorianCalendar.SECOND, 0);
        calendar.set(GregorianCalendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }
}