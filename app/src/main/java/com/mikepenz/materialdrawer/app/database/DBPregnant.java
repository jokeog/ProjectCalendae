package com.mikepenz.materialdrawer.app.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mikepenz.materialdrawer.app.calender.Pregnant;

import java.util.Calendar;

/**
 * Created
 */
public  class DBPregnant {

    private SQLiteDatabase database;

    public DBPregnant(DBHelper db)
    {
        database = db.getWritableDatabase();
    }

    public void insert(Double mWight,Double bWight,Double heart,String pDate,String message){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day =c.get(Calendar.DAY_OF_MONTH);
        String createDate = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);

        String temp =String.format("INSERT INTO pregnant (mWight,bWight,heart,pDate,message,createDate) VALUES  (%s,%s,%s,'%s','%s','%s')",mWight,bWight,heart,pDate,message,createDate);

        database.execSQL(temp);
    }

    public void update(Double mWight,Double bWight,Double heart,String pDate,String message,int pid){

        String temp =String.format("update pregnant set mWight=%s,bWight=%s,heart=%s,pDate='%s',message='%s', where pid= %s" ,mWight,bWight,heart,pDate,message,pid);
        database.execSQL(temp);
    }

    public Pregnant.mValue selectAllData() {

        String strSQL = "SELECT pid,mWight,bWight,heart,pDate,message,createDate FROM pregnant ";
        Cursor cursor = database.rawQuery(strSQL, null);
        Pregnant.mValue pregnant =new Pregnant.mValue();
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {
                    pregnant.pid = cursor.getInt(0);
                    pregnant.mWight = cursor.getDouble(1);
                    pregnant.bWight = cursor.getDouble(2);
                    pregnant.heart = cursor.getDouble(3);
                    pregnant.pDate = cursor.getString(4);
                    pregnant.message = cursor.getString(5);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return pregnant;

    }

    public int countAllData() {

        String strSQL = "SELECT  COUNT(*)  FROM pregnant";
        Cursor cursor = database.rawQuery(strSQL, null);
        int countRow=0;
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {
                    countRow=Integer.parseInt(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return  countRow;

    }
}
