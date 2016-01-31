package com.mikepenz.materialdrawer.app.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by Minny on 24/1/2559.
 */
public class CalendarFont {
    public void setFonts(int value[],AppCompatActivity act)
    {
        for (int i=0;i<value .length ;i++)
        {
            Typeface myTypeface= Typeface.createFromAsset(act.getAssets(),"waffle.otf");
            TextView myTextview =(TextView)act.findViewById(value[i ]);
            myTextview.setTypeface(myTypeface);

        }
    }

}
