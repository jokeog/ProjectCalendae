package com.mikepenz.materialdrawer.app.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mikepenz.materialdrawer.app.calender.Contracaption;
import com.mikepenz.materialdrawer.app.calender.Pregnant;

import java.util.Calendar;

/**
 * Created
 */
public  class DBCont {

    private SQLiteDatabase database;

    public DBCont(DBHelper db)
    {
        database = db.getWritableDatabase();
    }

    public void insert(String cName,int cNumber,String cDate){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day =c.get(Calendar.DAY_OF_MONTH);
        String createDate = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);
        String temp =String.format("INSERT INTO contracaption (cName,cNumber,cDate,createDate) VALUES  ('%s',%s,'%s','%s')",cName,cNumber,cDate,createDate);
        database.execSQL(temp);
    }

    public void update(String cName,int cNumber,String cDate,int cid){

        String temp =String.format("update contracaption set cName='%s',cNumber=%s,cDate='%s'  where cid= %s" ,cName,cNumber,cDate,cid);
        database.execSQL(temp);
    }

    public Contracaption.mValue selectAllData(int id) {

        String strSQL = String.format("SELECT cid,cName,cNumber,cDate,createDate FROM contracaption where cid=%s",id );
        Cursor cursor = database.rawQuery(strSQL, null);
        Contracaption.mValue cont =new Contracaption.mValue();
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {
                    cont.cid = cursor.getInt(0);
                    cont.cName = cursor.getString(1);
                    cont.cNumber = cursor.getInt(2);
                    cont.cDate = cursor.getString(3);


                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return cont;

    }

    public int countAllData() {

        String strSQL = "SELECT  COUNT(*)  FROM contracaption";
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
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day =c.get(Calendar.DAY_OF_MONTH);
        String createDatetime = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);

        int id = 0;
        String strSQL = String.format("SELECT cid FROM contracaption where createDate ='%s'", createDatetime);
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
    public void delete(int id)
    {
        String delete =String.format( "delete from contracaption WHERE cid =%s " ,id );
        database.execSQL(delete);

    }


}
