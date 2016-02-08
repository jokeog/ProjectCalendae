package com.mikepenz.materialdrawer.app.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mikepenz.materialdrawer.app.calender.Graph;
import com.mikepenz.materialdrawer.app.calender.Profile;

import java.util.Calendar;

/**
 * Created
 */
public  class DBGraph {

    private SQLiteDatabase database;

    public DBGraph(DBHelper db)
    {
        database = db.getWritableDatabase();
    }

    public void insert(String date,double weight,double height,String bmi){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day =c.get(Calendar.DAY_OF_MONTH);
        String createDate = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);

        String temp =String.format("INSERT INTO graph (date,weight,height,bmi,createDate) VALUES  ('%s',%s,%s,'%s',%s)",date,weight,height,bmi,createDate);
        database.execSQL(temp);
    }

    public void update(String date,double weight,double height,String bmi,int gid){
        String temp =String.format("update graph set date ='%s',weight=%s ,height=%s,bmi='%s' where gid =%S" ,date,weight,height,bmi,gid);
        database.execSQL(temp);
    }


}
