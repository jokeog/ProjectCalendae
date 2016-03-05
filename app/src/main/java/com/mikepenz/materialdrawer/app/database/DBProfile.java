package com.mikepenz.materialdrawer.app.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mikepenz.materialdrawer.app.calender.Profile;

/**
 * Created
 */
public  class  DBProfile {

    private SQLiteDatabase database;

    public DBProfile (DBHelper db)
    {
        database = db.getWritableDatabase();
    }

    public void insert(String name,String birthday,String email){
        String temp =String.format("INSERT INTO profile (id,name,birthday,email) VALUES  (1 ,'%s','%s','%s')",name,birthday,email);
        database.execSQL(temp);
    }

    public void update(String name,String birthday,String email){
        String temp =String.format("update profile set name='%s',birthday='%s',email='%s' where id=1",name,birthday,email);
        database.execSQL(temp);
    }

    public Profile.mValue selectAllData() {

        String strSQL = "SELECT  name,birthday,email FROM profile where id=1";
        Cursor cursor = database.rawQuery(strSQL, null);
        Profile.mValue profile=new Profile.mValue();
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                do {
                    profile.name = cursor.getString(0);
                    profile.birthDay = cursor.getString(1);
                    profile.email = cursor.getString(2);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return profile;

    }

    public int countAllData() {

        String strSQL = "SELECT  COUNT(*)  FROM profile";
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
    public void delete()
    {
        String delete =String.format( "delete from profile WHERE id =1");
        database.execSQL(delete);

    }
}
