package com.mikepenz.materialdrawer.app.calender;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.app.R;
import com.mikepenz.materialdrawer.app.database.DBHelper;
import com.mikepenz.materialdrawer.app.database.DBPregnant;
import com.mikepenz.materialdrawer.app.database.DBProfile;
import com.mikepenz.materialdrawer.app.utils.CalendarFont;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Pregnant extends AppCompatActivity {



    public static class mValue {
        public int pid;
        public double mWight;
        public double bWight;
        public double heart;
        public String pDate;
        public String message;

    }

    mValue value = new mValue();
    mValue dbValue = new mValue();
    private DBPregnant dataBase;
    private Drawer result = null;

    @Bind(R.id.pregnantButtonDay)
    Button pDate;
    @Bind(R.id.prenantMomm)
    TextView mWight;
    @Bind(R.id.prenantBabyT)
    TextView bWight;
    @Bind(R.id.prenantHeart)
    TextView heart;
    @Bind(R.id.prenantMessage)
    TextView message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregnant);
        setTitle(R.string.drawer_item_Pregnant_header);
        ButterKnife.bind(this);

        DBHelper mHelper;
        mHelper = new DBHelper(this);
        dataBase = new DBPregnant(mHelper);

        int value[] = {R.id.prenantMom, R.id.prenantBabyTT,R.id.prenantBabyT, R.id.prenantPre,R.id.prenantHeart,R.id.prenantMomm,
                R.id.prenantBkg, R.id.prenantBaby, R.id.prenantHeart, R.id.prenantBabya, R.id.prenantBabyH
                , R.id.prenantKg, R.id.prenantMessage, R.id.pregnantButtonDay, R.id.prenantMsg, R.id.pnSwitch1, R.id.pnTextView};

        CalendarFont font = new CalendarFont();

        font.setFonts(value, this);


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
                            Toast.makeText(Pregnant.this, ((Nameable) drawerItem).getName().getText(Pregnant.this), Toast.LENGTH_SHORT).show();
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


    @OnClick(R.id.pregnantButtonDay)
    void onMinClicked() {
        showDatePickerDialog(this, null, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String dateSelect;
                dateSelect = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth);
                pDate.setText(dateSelect);

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

    @OnClick(R.id.pregnantSave)
    void savePregnant() {
        android.app.AlertDialog.Builder builder =
                new android.app.AlertDialog.Builder(this);
        builder.setTitle("บันทึกข้อมูล");
        builder.setMessage("ยืนยันการบันทึกข้อมูล");
        builder.setPositiveButton(getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setValueInClass();
                        dataBase.insert(value.mWight, value.bWight, value.heart, value.pDate, value.message);
                        mValue a = dataBase.selectAllData();
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
        this.setMessage();
        this.setMwight();
        this.setHeart();
        this.setBwight();
        this.setPregnantDate();
    }

    void setMessage() {
        value.message = message.getText().toString();
    }

    void setMwight() {
        value.mWight = Double.parseDouble(mWight.getText().toString());
    }

    void setBwight() {
        value.bWight = Double.parseDouble(bWight.getText().toString());
    }

    void setHeart() {
        value.heart = Double.parseDouble(heart.getText().toString());
    }

    void setPregnantDate() {
        value.pDate = pDate.getText().toString();
    }

}
