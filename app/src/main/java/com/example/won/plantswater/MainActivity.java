package com.example.won.plantswater;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private ListView listView;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    public static PlantsDB mDatabase = null;
    public static myAlarmManager myAM;
    public static int state = 0;
    public static Menu mMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,LIST_MENU);

        listView = (ListView) findViewById(R.id.listView1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); NotificationChannel channelMessage = new NotificationChannel("channel_id", "channel_name", android.app.NotificationManager.IMPORTANCE_DEFAULT);
            channelMessage.setDescription("channel description");
            channelMessage.enableLights(true);
            channelMessage.setLightColor(Color.GREEN);
            channelMessage.enableVibration(true);
            channelMessage.setVibrationPattern(new long[]{100, 200, 100, 200});
            channelMessage.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(channelMessage);
        }

        Log.d(TAG, "Oncreate()");

    }

    @Override
    public void onBackPressed() {
        if(state == 1)
        {
            mMenu.findItem(R.id.action_watering).setVisible(false);
            mMenu.findItem(R.id.action_delete).setVisible(true);
            state = 0;
            PlantsList(listView, state);
        }

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if(0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
        }
        else
        {
            Toast.makeText(this ,"뒤로가기를 한번 더 누르면 종료됩니다",Toast.LENGTH_SHORT).show();
            backPressedTime = tempTime;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu);
        mMenu = menu;

        if(state == 1)
        {
            MenuItem item = mMenu.findItem(R.id.action_watering);
            item.setVisible(true);

            item = mMenu.findItem(R.id.action_delete);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Menu menu;
        int id = item.getItemId();

        if(id == R.id.action_insert)
        {
            Intent intent = new Intent(MainActivity.this,PlantsInsert.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_watering)
        {
            item.setVisible(false);
            item = mMenu.findItem(R.id.action_delete);
            item.setVisible(true);

            state = 0;
            PlantsList(listView, state);
            return true;
        }

        if(id == R.id.action_delete)
        {
            item.setVisible(false);
            item = mMenu.findItem(R.id.action_watering);
            item.setVisible(true);

            state = 1;
            PlantsList(listView, state);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onStart() {
        // 데이터베이스 열기
        openDatabase();
        PlantsList(listView,state);
        super.onStart();
    }

    public void openDatabase() {
        // open database
        mDatabase = PlantsDB.getInstance(this);
        boolean isOpen = mDatabase.open();
        if (isOpen) {
            Log.d(TAG, "database is open.");
        } else {
            Log.d(TAG, "database is not open.");
        }
    }

    public void PlantsList(ListView listview, int id) {
        List plants = mDatabase.getAllPlants();
        listview.setAdapter(new PlantsListAdapter(plants, MainActivity.this, id));
    }
}