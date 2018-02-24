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
    public static ListView listView;

    public static PlantsDB mDatabase = null;
    public static myAlarmManager myAM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView1);

        Log.d(TAG, "Oncreate()");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        ItemView itemView = null;

        int id = item.getItemId();

        if(id == R.id.action_insert)
        {
            Intent intent = new Intent(MainActivity.this,PlantsInsert.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_delete)
        {
            Toast.makeText(this, "삭제", Toast.LENGTH_SHORT).show();
            PlantsList(listView,2);
            return true;
        }

        if(id == R.id.action_watering)
        {
            Toast.makeText(this, "물주기", Toast.LENGTH_SHORT).show();
            PlantsList(listView,1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onStart() {

        // 데이터베이스 열기
        openDatabase();
        PlantsList(listView, 1);
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
