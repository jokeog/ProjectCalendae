package com.mikepenz.materialdrawer.app.calender;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.app.R;
import com.mikepenz.materialdrawer.app.database.DBCont;
import com.mikepenz.materialdrawer.app.database.DBHelper;
import com.mikepenz.materialdrawer.app.utils.CalendarFont;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Contracaption extends AppCompatActivity {

    public static class mValue {
        public int cid=0;
        public String cName;
        public int cNumber;
        public String cDate;
    }

    mValue value = new mValue();
    mValue dbValue = new mValue();
    private DBCont dataBase;
    private Drawer result = null;

    @Bind(R.id.cName)
    EditText cName;
    @Bind(R.id.cNumber)
    EditText cNumber;
    @Bind(R.id.cDate)
    Button cDate;
    @Bind(R.id.cHour)
    Spinner hour;
    @Bind(R.id.cMin)
    Spinner min;
    @Bind(R.id.cSwitch1)
    Switch cSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contraception);
        setTitle(R.string.drawer_item_Contracation_header);
        ButterKnife.bind(this);


        int value[] = {R.id.cView,R.id.cName,R.id.cNumber,R.id.cDate,R.id.cText1,R.id.cView,R.id.cText2,R.id.cText3,R.id.cText4,R.id.cText5,R.id.cSwitch1};

        CalendarFont font = new CalendarFont() ;
        font.setFonts(value, this);

        DBHelper mHelper;
        mHelper = new DBHelper(this);
        dataBase = new DBCont(mHelper);
        int id=dataBase.CheckIDInDay();
        if(id!=0)
        {
            dbValue = dataBase.selectAllData(id);
            dbToLayout();
        }


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
                            Toast.makeText(Contracaption.this, ((Nameable) drawerItem).getName().getText(Contracaption.this), Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }
                }).build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    private void dbToLayout(){
        String parts[]= dbValue.cDate.split("-");
        String date = parts[0] +"-"+ parts[1] +"-"+ parts[2] ;
        String hourP = parts[3];
        String minP = parts[4];

        cName.setText(dbValue.cName);
        cNumber.setText(String.valueOf(dbValue.cNumber));
        cDate.setText(date);
        hour.setSelection(Integer.parseInt(hourP));
        min.setSelection(Integer.parseInt(minP));

}

    @OnClick(R.id.cDate)
    void onMinClicked() {
        showDatePickerDialog(this, null, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String dateSelect;
                dateSelect = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth);
                cDate.setText(dateSelect);

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void showDatePickerDialog(Context context, CalendarDay day,
                                            DatePickerDialog.OnDateSetListener callback) {
        if (day == null) {
            day = CalendarDay.today();
        }
        DatePickerDialog dialog = new DatePickerDialog(
                context, 0, callback, day.getYear(), day.getMonth(), day.getDay()
        );
        dialog.show();

    }

    @OnClick(R.id.cDalete)
    void delete()
    {
        android.app.AlertDialog.Builder builder =
                new android.app.AlertDialog.Builder(this);
        builder.setTitle("ลบข้อมูล");
        builder.setMessage("ยืนยันการลบข้อมูล");
        builder.setPositiveButton(getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (dbValue.cid != 0) {
                            dataBase.delete(dbValue.cid);
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

    @OnClick(R.id.cSave)
    void savePregnant() {
        android.app.AlertDialog.Builder builder =
                new android.app.AlertDialog.Builder(this);
        builder.setTitle("แจ้งเตือน");

        builder.setPositiveButton(getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        builder.setNegativeButton(getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        setCnumber();
        if(value.cNumber != 21 && value.cNumber != 28){
            builder.setMessage("กรุณาใส่จำนวนยาคุม 21 หรือ 28 เม็ด");
            builder.show();
            return;
        }
        setCname();
        if(value.cName.equals("")){
            builder.setMessage("กรุณากรอกชื่อยาคุมกำเนิด");
            builder.show();
            return;
        }
        setcDate();
        if(value.cDate.equals("    วว/ดด/ปป    -00-00")){
            builder.setMessage("กรุณาใส่ วว/ดด/ปป");
            builder.show();
            return;
        }



        builder.setTitle("บันทึกข้อมูล");
        builder.setMessage("ยืนยันการบันทึกข้อมูล");
        builder.setPositiveButton(getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setValueInClass();
                        if (dbValue.cid != 0) {
                            dataBase.update(value.cName, value.cNumber, value.cDate,value.cid);
                        } else {
                            dataBase.insert(value.cName, value.cNumber, value.cDate);
                        }
                        if(cSwitch.isChecked() && value.cDate != null)
                            onAddEventClicked();
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
    void setValueInClass() {
        this.setCname();
        this.setCnumber();
        this.setcDate();
        value.cid = dbValue.cid;
    }
    void setCname() {
        value.cName = cName.getText().toString();
    }

    void setCnumber() {
        value.cNumber = Integer.parseInt(cNumber.getText().toString());
    }

    void setcDate() {
        String hourText = hour.getSelectedItem().toString();
        String minText = min.getSelectedItem().toString();

        if( cDate.getText().toString()!= " "&& hourText != " "&& minText!="วว/ดด/ปป")
            value.cDate = cDate.getText().toString() +"-"+hourText + "-" +minText;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

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
    public void onAddEventClicked(){
        View view;
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        Calendar cal = Calendar.getInstance();
        String[] parts = value.cDate.split("-");
        cal.set(
                Integer.parseInt(parts[0])
                ,Integer.parseInt(parts[1])
                ,Integer.parseInt(parts[2])
                ,Integer.parseInt(parts[3])
                ,Integer.parseInt(parts[4])
        );

        long startTime = cal.getTimeInMillis();
        long endTime = cal.getTimeInMillis()  + 60 * 60 * 1000;


        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);

        intent.putExtra(CalendarContract.Events.TITLE, "แจ้งเตือนการคุมกำเนิด");
        intent.putExtra(CalendarContract.Events.DESCRIPTION,  "วันที่ต้องกินยาคุม");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "My Guest House");
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");

        startActivity(intent);
    }
}
