package com.mikepenz.materialdrawer.app.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.NetworkInfo;

import com.mikepenz.materialdrawer.app.calender.Graph;
import com.mikepenz.materialdrawer.app.calender.Pregnant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created
 */
public  class DBMain {

    private SQLiteDatabase database;

    public DBMain(DBHelper db)
    {
        database = db.getWritableDatabase();
    }

    public static class CalenderValue {
        public int dateType;
        public String date;
        public int year;
        public int month;
        public int day;

    }

    public static class GraphValue {
        public  int gid;
        public String date;
        public double weight;
        public double height;
        public double bmi;
        public String mg;
    }

    public List<CalenderValue> selectAllData() {

        String strSQL = "SELECT 2,pDate FROM pregnant ";
        Cursor cursor = database.rawQuery(strSQL, null);

        List<CalenderValue> valueList = new ArrayList<CalenderValue>();
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {
                    CalenderValue value =new CalenderValue();
                    value.dateType = cursor.getInt(0);
                    value.date = cursor.getString(1);

                    if(value.date.equals("null"))
                        continue;

                    String[] parts = value.date.split("-");
                    value.year = Integer.valueOf(parts[0]);
                    value.month = Integer.valueOf(parts[1]);
                    value.day = Integer.valueOf(parts[2]);
                    valueList.add(value);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return valueList;

    }

    public List<GraphValue> selectAllDataGraph() {

        String strSQL = String.format("SELECT gid,date,weight,height,bmi,createDate FROM graph");
        Cursor cursor = database.rawQuery(strSQL, null);

        List<GraphValue> valueList = new ArrayList<GraphValue>();
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {
                    GraphValue graph =new GraphValue();
                    graph.date = cursor.getString(1);
                    if(graph.date.equals("null"))
                        continue;
                    graph.gid = cursor.getInt(0);
                    graph.weight= cursor.getDouble(2);
                    graph.height = cursor.getDouble(3);
                    graph.bmi = cursor.getDouble(4);

                    valueList.add(graph);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return valueList;

    }

}
