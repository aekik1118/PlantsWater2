package com.example.won.plantswater;

import android.content.Intent;
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
    public Menu mMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,LIST_MENU);

        listView = (ListView) findViewById(R.id.listView1);

        Log.d(TAG, "Oncreate()");

    }

    @Override
    public void onBackPressed() {
        mMenu.findItem(R.id.action_watering).setVisible(false);
        mMenu.findItem(R.id.action_delete).setVisible(true);
        PlantsList(listView, 1);

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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

            state = 1;
            PlantsList(listView, state);
            return true;
        }

        if(id == R.id.action_delete)
        {
            item.setVisible(false);
            item = mMenu.findItem(R.id.action_watering);
            item.setVisible(true);

            state = 2;
            PlantsList(listView, state);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onStart() {
        // 데이터베이스 열기
        openDatabase();

        if(state == 0)
        {
            PlantsList(listView, 1);
        }

        else
        {
            PlantsList(listView, state);
        }
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