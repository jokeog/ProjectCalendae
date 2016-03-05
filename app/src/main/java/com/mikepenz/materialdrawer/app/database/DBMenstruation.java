package com.mikepenz.materialdrawer.app.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mikepenz.materialdrawer.app.calender.Contracaption;
import com.mikepenz.materialdrawer.app.calender.Menstruation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;



/**
 * Created
 */
public  class DBMenstruation {

    private SQLiteDatabase database;

    public DBMenstruation(DBHelper db)
    {
        database = db.getWritableDatabase();
    }

    public void phaseDateAvg()
    {
        int id=checkID();
        int phaseDate=0;
        if(id<=1)
        {
            phaseDate=28;
        }
        else
        {
            String strSQL = String.format("SELECT startDate FROM menstuation where mid>=%s ",id-1 );
            String date[]=new String[2];
            int i =0;
            Cursor cursor = database.rawQuery(strSQL, null);
            if(cursor !=null)
            {
                if (cursor.moveToFirst()) {
                    do {
                        date[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date sDate = format.parse(date[1]);
                Date eDAte = format.parse(date[0]);
                long diff = sDate.getTime() - eDAte.getTime();
                phaseDate = (int)( diff/ (1000 * 60 * 60 * 24) );
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        String sqlUpdate = String.format("update menstuation set phaseDate = %s where mid=%s",phaseDate,id);

        database.execSQL(sqlUpdate);



        String strSQL = String.format("SELECT AVG(phaseDateAvg) FROM menstuation where mid>=%s ",id-2 );
        double avg=0;
        Cursor cursor = database.rawQuery(strSQL, null);

        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {
                    avg=cursor.getDouble(0);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        String phaseAvg = String.format("update menstuation set phaseDateAvg = %s where mid=%s",avg,id);

        database.execSQL(phaseAvg);

    }

   public int checkID()
    {
        int id=0;
        String strSQL = String.format("SELECT MAX(mid) FROM menstuation " );
        Cursor cursor = database.rawQuery(strSQL,null);
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {
                    id=cursor.getInt(0);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return id;

    }

    public void insert(String startDate,String endDate) throws ParseException {
        Calendar m = Calendar.getInstance();
        int year = m.get(Calendar.YEAR);
        int month = m.get(Calendar.MONTH);
        int day =m.get(Calendar.DAY_OF_MONTH);
        String createDate = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);

        int phaseDate=0;
        double phaseDateAvg=0;

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date sDate = format.parse(startDate);
            Date eDAte = format.parse(endDate);
            long diff = eDAte.getTime() - sDate.getTime();
            phaseDate = (int)( diff/ (1000 * 60 * 60 * 24) );
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String temp =String.format("INSERT INTO menstuation (startDate,endDate,createDate) VALUES  ('%s','%s','%s')",startDate,endDate,createDate);
        database.execSQL(temp);
    }

    public void update(String startDate,String endDate,int mid){

        int phaseDate=0;
        double phaseDateAvg=0;
        String temp =String.format("update menstuation set startDate='%s',endDate='%s',phaseDate=%s,phaseDateAvg=%s where mid= %s" ,startDate,endDate,phaseDate,phaseDateAvg,mid);
        database.execSQL(temp);
    }

    public Menstruation.mValue selectAllData(int id) {

        String strSQL = String.format("SELECT mid,startDate,endDate,phaseDate,phaseDateAvg,createDate FROM menstuation where mid=%s",id );
        Cursor cursor = database.rawQuery(strSQL, null);
        Menstruation.mValue mens = new Menstruation.mValue();
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {
                    mens.mid = cursor.getInt(0);
                    mens.startDate= cursor.getString(1);
                    mens.enDate = cursor.getString(2);



                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return mens;

    }

    public int countAllData() {

        String strSQL = "SELECT  COUNT(*)  FROM menstuation";
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
        String strSQL = String.format("SELECT mid FROM menstuation where createDate ='%s'", createDatetime);
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
        String delete =String.format( "delete from menstuation WHERE mid =%s " ,id );
        database.execSQL(delete);

    }


}
