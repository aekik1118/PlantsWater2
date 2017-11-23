package com.example.won.plantswater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    static final String[] LIST_MENU = {"LIST1","LIST2","LIST3"};

    public static PlantsDB mDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,LIST_MENU);

        final ListView listView = (ListView) findViewById(R.id.listView1);
        //listView.setAdapter(adapter);

        Button button = (Button)findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,PlantsInsert.class);
                startActivity(intent);
            }
        });

        Button button1 = (Button)findViewById(R.id.button2);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //listView.setVisibility(View.VISIBLE);
                PlantsList(listView);
            }
        });

    }


    protected void onStart() {

        // 데이터베이스 열기
        openDatabase();

        super.onStart();
    }


    public void openDatabase() {
        // open database

        mDatabase = PlantsDB.getInstance(this);
        boolean isOpen = mDatabase.open();
        if (isOpen) {
            Log.d(TAG, "Memo database is open.");
        } else {
            Log.d(TAG, "Memo database is not open.");
        }
    }

    public void PlantsList(ListView listview) {
        PlantsDB.DatabaseHelper helper = mDatabase.DBHelper();

        List plants = helper.getAllPlants();

        listview.setAdapter(new PlantsListAdapter(plants, MainActivity.this));
    }


}
