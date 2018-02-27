package com.example.won.plantswater;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

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
        String name = intent.getStringExtra("pname");

        NotificationManager notificationManager = ( NotificationManager )getSystemService(this.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(MyAlarmService.this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyAlarmService.this,plantId,intent1,0);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setSmallIcon(R.drawable.cactus)
                .setContentTitle(name)
                .setContentText("목이말라요 물을 주세요!!")
                .setContentIntent(pendingIntent)
                .setTicker("알림!!");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        Notification noti;
        if(Build.VERSION.SDK_INT < 16)
            noti = builder.getNotification();
        else
            noti = builder.build();

        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(plantId,noti);
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


}
