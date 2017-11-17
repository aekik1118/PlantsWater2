package com.example.won.plantswater;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    private PlantsDB(Context context) {
        this.context = context;
    }


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
            String CREATE_SQL = "create table " + TABLE_NAME + "("
                    + "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  INPUT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "  NAME TEXT DEFAULT '', "
                    + "  WATER_PERIOD INTEGER, "
                    + "  PHOTO TEXT, "
                    + "  RECENT DATE "
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

        }
    }

    public boolean open() {
        println("opening database [" + BasicInfo.DATABASE_NAME + "].");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

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


    private void println(String msg) {
        Log.d(TAG, msg);
    }

}
