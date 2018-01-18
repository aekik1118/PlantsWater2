package com.example.won.plantswater;

import android.app.Service;
import android.content.Intent;
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
            Toast.makeText(this,"알람"+intent.getIntExtra("pid",0) ,Toast.LENGTH_SHORT).show();


        }


        Log.d(TAG, "onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }
}
