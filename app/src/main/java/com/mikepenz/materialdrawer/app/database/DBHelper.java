package com.mikepenz.materialdrawer.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;



public class DBHelper extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();

    private SQLiteDatabase sqLiteDatabase;
    private static final int DATABASE_VERSION = 15;
    //Ver 1 Add profile,pregnant

    public DBHelper(Context context) {
        super(context, "calendar.db", null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_PROFILE = String.format("CREATE TABLE %s " +
                "(%s INTEGER PRIMARY KEY , %s TEXT, %s NUMERIC, %s TEXT )",
                "profile",
                "id",
                "name",
                "birthday",
                "email");
        String CREATE_PERGANT = String.format(" CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT , %s REAL, %s REAL, %s REAL,%s NUMERIC ,%s TEXT,%s NUMERIC)",
                "pregnant",
                "pid",
                "mWight",
                "bWight",
                "heart",
                "pDate",
                "message",
                "createDate");

        String CREATE_CONT = String.format(" CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT , %s NUMERIC, %s INTEGER,%s NUMERIC ,%s NUMERIC)",
                "contracaption",
                "cid",
                "cName",
                "cTwenty",
                "cDate",
                "createDate");

        String CREATE_GRAPH = String.format(" CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT , %s REAL, %s REAL, %s REAL ,%s REAL ,%s NUMERIC)",
                "graph",
                "gid",
                "date",
                "weight",
                "height",
                "bmi",
                "createDate");

        String CREATE_MENSTUATION = String.format(" CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT , %s NUMERIC, %s NUMERIC, %s INTEGER ,%s REAL ,%s NUMERIC,%s NUMERIC,%s NUMERIC)",
                "menstuation",
                "mid",
                "startDate",
                "endDate",
                "phaseDate",
                "phaseDateAvg",
                "onlyDate",
                "nextMonth",
                "createDate");
        //Log.i(TAG, CREATE_TABLE);

        // create Profile table
        db.execSQL(CREATE_PERGANT);
        db.execSQL(CREATE_PROFILE);
        db.execSQL(CREATE_GRAPH);
        db.execSQL(CREATE_CONT);
        db.execSQL(CREATE_MENSTUATION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String  DROP_TABLE;

        String DROP_Profile_TABLE = "DROP TABLE IF EXISTS " + "profile";
        String DROP_Pregnant_TABLE = "DROP TABLE IF EXISTS " + "pregnant";
        String DROP_Graph_TABLE = "DROP TABLE IF EXISTS " + "graph";
        String DROP_Cont_TABLE = "DROP TABLE IF EXISTS " + "contracaption";
        String DROP_Menstuation_TABLE = "DROP TABLE IF EXISTS " + "menstuation";

        db.execSQL(DROP_Profile_TABLE);
        db.execSQL(DROP_Pregnant_TABLE);
        db.execSQL(DROP_Graph_TABLE);
        db.execSQL(DROP_Cont_TABLE);
        db.execSQL(DROP_Menstuation_TABLE);
        //Log.i(TAG, "Upgrade Database from " + oldVersion + " to " + newVersion);
        onCreate(db);



    }

}
