package com.mikepenz.materialdrawer.app.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mikepenz.materialdrawer.app.calender.Graph;


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

    public void insert(String date,double weight,double height,double bmi,String mg){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day =c.get(Calendar.DAY_OF_MONTH);
        String createDate = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);
        String temp =String.format("INSERT INTO graph (date,weight,height,bmi,createDate) VALUES  ('%s',%s,%s,%s,'%s')",date,weight,height,bmi,createDate);
        database.execSQL(temp);
    }

    public void update(String date,double weight,double height,double bmi,int gid){
        String temp =String.format("update graph set date ='%s',weight=%s ,height=%s,bmi=%s where gid =%s" ,date,weight,height,bmi,gid);
        database.execSQL(temp);
    }

    public Graph.mValue selectAllData(int gid) {

        String strSQL = String.format("SELECT gid,date,weight,height,bmi,createDate FROM graph where gid=%s", gid);
        Cursor cursor = database.rawQuery(strSQL, null);
        Graph.mValue graph =new Graph.mValue();
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {
                    graph.gid = cursor.getInt(0);
                    graph.date = cursor.getString(1);
                    graph.weight= cursor.getDouble(2);
                    graph.height = cursor.getDouble(3);
                    graph.bmi = cursor.getDouble(4);



                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return graph;

    }

    public int countAllData() {

        String strSQL = "SELECT  COUNT(*)  FROM graph";
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

    public int CheckIDInDay()
    {
        Calendar g = Calendar.getInstance();
        int year = g.get(Calendar.YEAR);
        int month = g.get(Calendar.MONTH);
        int day =g.get(Calendar.DAY_OF_MONTH);
        String createDatetime = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);

        int id = 0;
        String strSQL = String.format("SELECT gid FROM graph where createDate ='%s'", createDatetime);
        Cursor cursor = database.rawQuery(strSQL, null);
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(0);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();


        return  id;
    }
    public void delete(int gid)
    {
        String delete =String.format( "delete from graph WHERE gid =%s " ,gid );
        database.execSQL(delete);

    }




}
