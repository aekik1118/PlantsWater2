package com.example.won.plantswater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aekik on 2017-11-16.
 */

public class PlantsDB {

    public static final String TAG = "PlantsDB";
    public static String TABLE_NAME = "Plants";
    private static PlantsDB database;
    private SQLiteDatabase db;
    private Context context;
    public static int DATABASE_VERSION = 1;
    private DatabaseHelper dbHelper;
    private PlantsDB(Context context) {this.context = context;}

    public static PlantsDB getInstance(Context context) {
        if (database == null) {
            database = new PlantsDB(context);
        }
        return database;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, BasicInfo.DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {

            println("creating database [" + BasicInfo.DATABASE_NAME + "].");
            println("creating table [" + TABLE_NAME + "].");

            // create table
            String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  INPUT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "  NAME TEXT DEFAULT '', "
                    + "  WATER_PERIOD INTEGER, "
                    + "  PHOTO TEXT, "
                    + "  RECENT TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")";
            try {
                db.execSQL(CREATE_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }
        }

        public void onOpen(SQLiteDatabase db) {
            println("opened database [" + BasicInfo.DATABASE_NAME + "].");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Log.d(TAG, "UUUUUU");
        }
    }

        public String getName(int plantId)
        {
            String name;
            String sql = "SELECT * FROM " + PlantsDB.TABLE_NAME + " WHERE _id = " + plantId;

            Log.d(TAG, sql);
            db = dbHelper.getReadableDatabase();
            Cursor result =  db.rawQuery(sql,null);
            result.moveToFirst();
            name = result.getString(2);

            return name;
        }

        public List getAllPlants() {
            StringBuffer sb = new StringBuffer();
            sb.append(" SELECT NAME, WATER_PERIOD, PHOTO, RECENT, _id FROM Plants ");

            db = dbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(sb.toString(), null);

            List plants = new ArrayList();
            Plants plant = null;


            try{
                while(cursor.moveToNext()){
                    plant = new Plants();
                    plant.setName(cursor.getString(0));
                    plant.setWater_period(cursor.getInt(1));

                    String url = cursor.getString(2);
                    plant.setPhoto(url);
                    Log.d(TAG, " uri 테스트 33 "+url);

                    plant.setRecent(cursor.getString(3));
                    plant.setId(cursor.getInt(4));

                    plants.add(plant);
                }
            }catch (Exception e){

            }finally {
                cursor.close();
            }
           // db.close();
            println("getAllPlants\n");
            return plants;
        }

        private Bitmap getBitmap(String url) {
            URL imgUrl = null;
            HttpURLConnection connection = null;
            InputStream is = null;
            Bitmap retBitmap = null;

            try {
                imgUrl = new URL(url);
                connection = (HttpURLConnection) imgUrl.openConnection();
                connection.setDoInput(true);
                connection.connect();
                is = connection.getInputStream();
                retBitmap = BitmapFactory.decodeStream(is);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                return retBitmap;
            }
        }

    public boolean open() {
        println("opening database [" + BasicInfo.DATABASE_NAME + "].");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
        return true;
    }


    public Cursor rawQuery(String SQL) {
        println("\nexecuteQuery called.\n");

        Cursor c1 = null;
        try {
            c1 = db.rawQuery(SQL, null);
            println("cursor count : " + c1.getCount());
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
        }
        return c1;
    }

    public long insertData(String name, String photo, int waterPeriod)
    {
        db = dbHelper.getWritableDatabase();

        ContentValues val = new ContentValues();
        val.put("NAME",name);
        val.put("PHOTO",photo);
        val.put("WATER_PERIOD",waterPeriod);
        return db.insert(TABLE_NAME,null,val);
    }

    private void println(String msg) {
        Log.d(TAG, msg);
    }

}
