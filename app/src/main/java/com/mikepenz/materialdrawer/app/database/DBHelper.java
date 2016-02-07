package com.mikepenz.materialdrawer.app.database;

import android.content.Context;
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


        String CREATE_TABLE = String.format("CREATE TABLE %s " +
                "(%s INTEGER PRIMARY KEY , %s TEXT, %s NUMERIC, %s TEXT )",
                "profile",
                "id",
                "name",
                "birthday",
                "email");
        String createPregant = String.format(" CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY  AUTOINCREMENT , %s REAL, %s REAL, %s REAL,%s NUMERIC ,%s TEXT,%s NUMERIC)",
                "pregnant",
                "pid",
                "mWight",
                "bWight",
                "heart",
                "pDate",
                "message",
                "createDate");
        CREATE_TABLE=CREATE_TABLE+createPregant;

        Log.i(TAG, CREATE_TABLE);

        // create Profile table
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String  DROP_TABLE;

        String DROP_Profile_TABLE = "DROP TABLE IF EXISTS " + "profile";
        String DROP_Pregnant_TABLE = "DROP TABLE IF EXISTS " + "pregnant";


        DROP_TABLE=DROP_Profile_TABLE+DROP_Pregnant_TABLE;

        db.execSQL(DROP_TABLE);
        Log.i(TAG, "Upgrade Database from " + oldVersion + " to " + newVersion);
        onCreate(db);



    }

}
