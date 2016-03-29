package com.mikepenz.materialdrawer.app.calender;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.app.R;
import com.mikepenz.materialdrawer.app.database.DBGraph;
import com.mikepenz.materialdrawer.app.database.DBHelper;
import com.mikepenz.materialdrawer.app.database.DBMenstruation;
import com.mikepenz.materialdrawer.app.utils.CalendarFont;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.mikepenz.materialdrawer.app.decorators.EventDecorator;
import com.mikepenz.materialdrawer.app.decorators.MySelectorDecorator;
import com.mikepenz.materialdrawer.app.decorators.HighlightWeekendsDecorator;
import com.mikepenz.materialdrawer.app.decorators.OneDayDecorator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Menstruation extends AppCompatActivity {
    public static class mValue {
        public int mid=0;
        public String startDate;
        public String enDate;
        public Double phaseDateAvg;
        public int phaseDate;
        public String onlyDate;
    }
    mValue value = new mValue();
    mValue dbValue = new mValue();
   private DBMenstruation dataBase;
    private Drawer result = null;

    boolean isTest=true;

    @Bind(R.id.msButtonS)
    Button startDate;
    @Bind(R.id.msButtonE)
    Button enDate;
    @Bind(R.id.msClear)
    Button clear;

    @Bind(R.id.calendarView)
    MaterialCalendarView widget;

    void setValueInClass() {
        this.setStartDate();
        this.setEnDate();
        value.mid = dbValue.mid;
    }
    void setStartDate() {
        value.startDate = startDate.getText().toString();
    }
    void setEnDate() {
        value.enDate = enDate.getText().toString();
    }

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menstruation);
        setTitle(R.string.drawer_item_Menstr_header);
        ButterKnife.bind(this);
        int value[] = {R.id.msButtonS,R.id.msButtonE,R.id.msClear,R.id.msTextView};

        CalendarFont font =new CalendarFont() ;
        font.setFonts(value, this) ;

        DBHelper mHelper;
        mHelper = new DBHelper(this);
        dataBase = new DBMenstruation(mHelper);
        int id=dataBase.CheckIDInDay();
        if(id!=0 && isTest==false)
        {
            dbValue = dataBase.selectAllData(id);
            dbToLayout();

        }

        enDate.setEnabled(false);
        int color = -65536;//red
        widget.setArrowColor(color);
        widget.setSelectionColor(color);

        /*widget.addDecorators(
                new MySelectorDecorator(this),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );

       new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());*/


        // Handle Toolbar
        result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(Menstruation.this, ((Nameable) drawerItem).getName().getText(Menstruation.this), Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }
                }).build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }
    private void dbToLayout(){

        startDate.setText(dbValue.startDate);
        enDate.setText(dbValue.enDate);
        startDate.setEnabled(false);
        enDate.setEnabled(false);
        int color = -16711936;//green
        widget.setArrowColor(color);
        widget.setSelectionColor(color);
        widget.clearSelection();

    }

    @OnClick (R.id.msButtonS)
    void clickButtonS()
    {
        CalendarDay date = widget.getSelectedDate();
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();
        String createDate = String.valueOf(year ) + "-" + String.valueOf(month +1) + "-" + String.valueOf(day);
        startDate.setText(createDate);



        Button bSDate =(Button)findViewById(R.id.msButtonS);
        bSDate.setEnabled(false);
        enDate.setEnabled(true);
        int color = -16711936;//green
        widget.setArrowColor(color);
        widget.setSelectionColor(color);
        widget.clearSelection();
    }

    @OnClick (R.id.msButtonE)
    void clickButtonE()
    {
        CalendarDay date = widget.getSelectedDate();
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();
        String createDate = String.valueOf(year ) + "-" + String.valueOf(month +1) + "-" + String.valueOf(day);
        enDate.setText(createDate);
        enDate.setEnabled(false);


    }

    @OnClick(R.id.msClear)
    void clickClear()
    {
        startDate.setText("วันแรกของรอบเดือน");
        enDate.setText("วันสุดท้ายของรอบเดือน");


        startDate.setEnabled(true);
        enDate.setEnabled(false);
        int color = -65536;//red
        widget.setArrowColor(color);
        widget.setSelectionColor(color);
        widget.clearSelection();

    }

    @OnClick(R.id.msSave)
    void clickSave()
    {
        android.app.AlertDialog.Builder builder =
                new android.app.AlertDialog.Builder(this);
        builder.setTitle("บันทึกข้อมูล");
        builder.setMessage("ยืนยันการบันทึกข้อมูล");
        builder.setPositiveButton(getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setValueInClass();
                        List<Menstruation.mValue> testData = dataBase.selectAllData();
                        if (dbValue.mid != 0 && isTest==false) {
                            dataBase.update(value.startDate, value.enDate, value.mid);
                           // dataBase.phaseDateAvg();
                        } else {
                            try {
                                dataBase.insert(value.startDate, value.enDate);
                                dataBase.phaseDateAvg();
                                dataBase.selectAllData();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        finish();
                    }
                });
        builder.setNegativeButton(getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();

    }
    @OnClick(R.id.msDelete)
    void clickDelete()
    {
        android.app.AlertDialog.Builder builder =
                new android.app.AlertDialog.Builder(this);
        builder.setTitle("ลบข้อมูล");
        builder.setMessage("ยืนยันการลบข้อมูล");
        builder.setPositiveButton(getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (dbValue.mid != 0) {
                            dataBase.delete(dbValue.mid);
                        }
                        finish();
                    }
                });
        builder.setNegativeButton(getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
//
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -2);
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
                calendar.add(Calendar.DATE, 5);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            widget.addDecorator(new EventDecorator(Color.RED, calendarDays));
        }
    }



}
