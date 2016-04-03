package com.mikepenz.materialdrawer.app.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.NetworkInfo;

import com.mikepenz.materialdrawer.app.calender.Graph;
import com.mikepenz.materialdrawer.app.calender.Pregnant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    public static class ContracaptionValue {
        public  Calendar cdate;
        public  int cTwenty;
    }

    public List<CalenderValue> selectAllDataContracaptionList()
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        String sysdate=df.format(today);
        String strSQL = String.format("SELECT %s FROM %s where cDate>'%s'", "cid", "contracaption ", sysdate);
        Cursor cursor = database.rawQuery(strSQL, null);
        int id;
        List<CalenderValue> valueList = new ArrayList<CalenderValue>();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(0);

                    if(id<0)
                        continue;

                    ContracaptionValue con = selectAllDataContracaption(id);
                    Calendar cdate = con.cdate;
                    for(int i=0;i<con.cTwenty;i++)
                    {
                        CalenderValue value =new CalenderValue();

                        value.date = format1.format(cdate.getTime());

                        if(value.date.equals("null"))
                            continue;

                        String[] parts = value.date.split("-");
                        value.year = Integer.valueOf(parts[0]);
                        value.month = Integer.valueOf(parts[1]);
                        value.day = Integer.valueOf(parts[2]);
                        valueList.add(value);

                        cdate.add(Calendar.DATE, 1);
                    }


                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return valueList;
    }

    public ContracaptionValue selectAllDataContracaption(int id) {

        String strSQL = String.format("SELECT cDate,cTwenty FROM %s where cid=%s", "contracaption ",id);
        Cursor cursor = database.rawQuery(strSQL, null);
        Calendar cdate=Calendar.getInstance();
        ContracaptionValue con=new ContracaptionValue();
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {


                    String date = cursor.getString(0);

                    if(date.equals("null"))
                        continue;

                    String[] parts = date.split("-");
                    cdate.set(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]) - 1, Integer.valueOf(parts[2]));
                    con.cdate = cdate;
                    con.cTwenty = cursor.getInt(1);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return con;

    }
    public List<CalenderValue> selectAllData(String table,String column) {

        String strSQL = String.format("SELECT 2,%s FROM %s ",column,table);
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
