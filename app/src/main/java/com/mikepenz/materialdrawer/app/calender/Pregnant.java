package com.mikepenz.materialdrawer.app.calender;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.app.DrawerActivity;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Pregnant extends AppCompatActivity {



    public static class mValue {
        public int pid=0;
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
    @Bind(R.id.imageView1)
    ImageView imageView;
    @Bind(R.id.prenantHour)
    Spinner hour;
    @Bind(R.id.prenantMin)
    Spinner min;

    private static int RESULT_LOAD_IMAGE = 1;


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
        int id=dataBase.CheckIDInDay();
        if(id!=0)
        {
            dbValue = dataBase.selectAllData(id);
            dbToLayout();
        }




        int value[] = {R.id.prenantMom, R.id.prenantBabyTT,R.id.prenantBabyT, R.id.prenantPre,R.id.prenantHeart,R.id.prenantMomm,
                R.id.prenantBkg, R.id.prenantBaby, R.id.prenantHeart, R.id.prenantBabya, R.id.prenantBabyH
                , R.id.prenantKg, R.id.prenantMessage, R.id.pregnantButtonDay, R.id.prenantMsg, R.id.pnSwitch1, R.id.pnTextView
        ,R.id.pregtextView,R.id.pregnantPhoto};

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

    private void dbToLayout(){
        String parts[]= dbValue.pDate.split("-");
        String date = parts[0] +"-"+ parts[1] +"-"+ parts[2] ;
         String hourP = parts[3];
        String minP = parts[4];


        mWight.setText(String.valueOf(dbValue.mWight));
        bWight.setText(String.valueOf(dbValue.bWight));
        heart.setText(String.valueOf(dbValue.heart));
        pDate.setText(date);
        hour.setSelection(Integer.parseInt(hourP));
        min.setSelection(Integer.parseInt( minP));

        message.setText(dbValue.message);
        File imgFile = new  File(String.format("%s/image/imp%s.jpg",DrawerActivity.appPath,dataBase.CheckIDInDay()));

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.imageView1);

            myImage.setImageBitmap(myBitmap);

        }
    }


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
    @OnClick(R.id.pregnantDalete)
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

                            if (dbValue.pid != 0) {
                                dataBase.delete(dbValue.pid);
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
                        if (dbValue.pid != 0) {
                            dataBase.update(value.mWight, value.bWight, value.heart, value.pDate, value.message,value.pid);
                        } else {
                            dataBase.insert(value.mWight, value.bWight, value.heart, value.pDate, value.message);
                        }
                        if(photo != null)
                        {
                            createDirectoryAndSaveFile(String.format("imp%s",dataBase.CheckIDInDay()));
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


    void setValueInClass() {
        this.setMessage();
        this.setMwight();
        this.setHeart();
        this.setBwight();
        this.setPregnantDate();
        value.pid = dbValue.pid;
    }

    void setMessage() {
        value.message = message.getText().toString();
    }

    void setMwight() {
        if(heart.getText().toString().length()!=0)
        value.mWight = Double.parseDouble(mWight.getText().toString());
    }

    void setBwight() {
        if(heart.getText().toString().length()!=0)
        value.bWight = Double.parseDouble(bWight.getText().toString());
    }

    void setHeart() {
        if(heart.getText().toString().length()!=0)
        value.heart = Double.parseDouble(heart.getText().toString());
    }

    void setPregnantDate() {
        String hourText = hour.getSelectedItem().toString();
        String minText = min.getSelectedItem().toString();
        if( pDate.getText().toString()!= " "&& hourText != " "&& minText!="วว/ดด/ปป")
        value.pDate = pDate.getText().toString() +"-"+hourText + "-" +minText;

    }

    private static final int CAMERA_REQUEST = 1888;
    private  Bitmap photo;

    @OnClick(R.id.imageButton2)
    void OpenCamera(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }

    private void createDirectoryAndSaveFile( String fileName) {
        Bitmap imageToSave=photo;
        String s= DrawerActivity.appPath+"/image";
        File direct = new File(s);

        if (!direct.exists()) {
            File wallpaperDirectory = new File(s);
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File(s), fileName+".jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnClick(R.id.pregnantPhoto)
    void photo(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);

    }

}
