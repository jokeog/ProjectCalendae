package com.mikepenz.materialdrawer.app.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;



public class DBHelper extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();

    private SQLiteDatabase sqLiteDatabase;

    public DBHelper(Context context) {
        super(context, "calendar.db", null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_Profile_TABLE = String.format("CREATE TABLE %s " +
                "(%s INTEGER PRIMARY KEY  AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT , %s TEXT )",
                "profile",
                BaseColumns._ID,
                "name",
                "date",
                "email",
                "weigth",
                "heigth",
                "bmi");

        Log.i(TAG, CREATE_Profile_TABLE);

        // create Profile table
        db.execSQL(CREATE_Profile_TABLE);
        /*db.execSQL("CREATE TABLE profile " +
                "(ID INTEGER PRIMARY KEY," +
                " USER_NAME TEXT(100)," +
                " WEIGHT TEXT(100)," +
                " HEIGHT TEXT(100)," +
                " AGE TEXT(100)," +
                " BIRTHDAY TEXT(100));");*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_Profile_TABLE = "DROP TABLE IF EXISTS " + "profile";

        db.execSQL(DROP_Profile_TABLE);

        Log.i(TAG, "Upgrade Database from " + oldVersion + " to " + newVersion);

        onCreate(db);
    }

}
