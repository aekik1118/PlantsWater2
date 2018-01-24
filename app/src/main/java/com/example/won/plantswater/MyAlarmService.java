package com.example.won.plantswater;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyAlarmService extends Service {

    public static final String TAG = "MyAlarmService";


    public MyAlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int plantId = intent.getIntExtra("pid",0);

        if(plantId == 0)
            Toast.makeText(this,"에러"+intent.getIntExtra("pid",0) ,Toast.LENGTH_SHORT).show();
        else
        {
            NotificationManager notificationManager = ( NotificationManager )getSystemService(this.NOTIFICATION_SERVICE);
            Intent intent1 = new Intent(MyAlarmService.this,MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MyAlarmService.this,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(getApplicationContext());
            builder.setSmallIcon(R.drawable.cactus)
                    .setContentTitle("Content Title")
                    .setContentText("Content Text")
                    .setContentIntent(pendingIntent)
                    .setTicker("알림!!");


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                builder.setCategory(Notification.CATEGORY_MESSAGE)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVisibility(Notification.VISIBILITY_PUBLIC);
            }

            if(Build.VERSION.SDK_INT < 16)
            {
                notificationManager.notify(0,builder.getNotification());
            }
            else
            {
                notificationManager.notify(0,builder.build());
            }

            //Toast.makeText(this,"알람"+intent.getIntExtra("pid",0) ,Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


}
