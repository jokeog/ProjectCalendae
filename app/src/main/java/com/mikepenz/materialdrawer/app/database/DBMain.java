package com.mikepenz.materialdrawer.app.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.NetworkInfo;

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
                    String string = cursor.getString(1);
                    String[] parts = string.split("-");
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

}
