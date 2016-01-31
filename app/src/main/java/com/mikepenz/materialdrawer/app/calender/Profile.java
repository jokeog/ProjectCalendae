package com.mikepenz.materialdrawer.app.calender;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.app.R;
import com.mikepenz.materialdrawer.app.utils.CalendarFont;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class Profile extends AppCompatActivity {

    private String name;
    private double weight;
    private double height;
    private double bmi;

    private EditText temp;

    public void setBmi()
    {
        temp  = (EditText)findViewById(R.id.editMessage);
        bmi = Double.parseDouble(temp.getText().toString());
    }

    public void setWeight()
    {
        temp  = (EditText)findViewById(R.id.editW);
        weight = Double.parseDouble(temp.getText().toString());
    }
    public void setHeight()
    {
        temp  = (EditText)findViewById(R.id.editH);
        height = Double.parseDouble(temp.getText().toString());
    }

    private Drawer result = null;

    MaterialCalendarView widget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(R.string.drawer_item_profile_header);
        ButterKnife.bind(this);

        int value[] = {R.id.textData,R.id.textName ,R.id.textEmail ,R.id.textH,R.id.textW,R.id.radioButton ,R.id.editName,
                R.id.editEmail ,R.id.editH ,R.id.editW ,R.id.editMessage ,R.id.pfButtonDate  ,R.id.textKg ,R.id.textCm};

        CalendarFont font =new CalendarFont() ;
        font.setFonts(value ,this) ;
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
                            Toast.makeText(Profile.this, ((Nameable) drawerItem).getName().getText(Profile.this), Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.pfButtonDate)
      void onMinClicked() {
        showDatePickerDialog(this, null, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                widget.setMinimumDate(CalendarDay.from(year, monthOfYear, dayOfMonth));
            }
        });
    }

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


    @OnClick (R.id.radioButton)
    void onClickBmi ()
    {
        this.setHeight();
        this.setWeight();

        double result = weight / ((height/100)*(height/100));
        String bmi;
        result = Math.floor(result * 100) / 100;

        if(result <= 18.50)
        {
            bmi ="น้ำหนักน้อย / ผอม" +
                    "แนะนำ\n" +
                    "1. ควรกินอาหารให้หลากหลายครบ 5 หมู่ในสัดส่วนที่เหมาะสมและปริมาณมากขึ้น โดยเพิ่มอาหารประเภทที่ให้พลังงานมากขึ้น เช่น ไขมัน แป้ง ข้าว เนื้อสัตว์ นม\n" +
                    "2. ควรเคลื่อนไหวและออกกำลังกายอย่างสม่ำเสมอทุกวันหรือเกือบทุกวัน ให้เหนื่อยพอควรโดยหายใจกระชั้นขึ้น เช่น เดินเร็ว ถีบจักรยาน รำมวยจีน ลีลาศจังหวะช้า รวมทั้งงานบ้าน งานสวน เป็นต้น สะสมให้ได้อย่างน้อยวันละ 30 นาทีอาจไม่จำเป็นต้องออกกำลังกายให้เหนื่อยมากหรือหอบ ที่ง่ายที่สุดคือ การเดิน\n";
        }
        else if (result >18.50 && result<= 22.90)
        {
            bmi ="ปกติ (สุขภาพดี)" +
                    "ข้อแนะนำ\n" +
                    "1. ควรกินอาหารให้หลากหลายครบ 5 หมู่ในสัดส่วนที่เหมาะสม กินเท่าที่ร่างกายต้องการวันไหนกินมากเกินไป วันต่อมาก็กินลดลง กินอาหารพวกข้าวและแป้งรวมทั้งเมล็ดธัญพืชอื่น ๆ ให้มากขึ้นไม่น้อยกว่าวันละ 6 ทัพพี กินผัก รวมทั้งเมล็ดถั่ว ผลไม้ ไม่ต่ำกว่าวันละ 5 ส่วน หรือครึ่งกิโลกรัม เพื่อไม่ให้มีพลังงานส่วนเกินจะทำให้ควบคุมน้ำหนักได้ดีและสมดุล\n" +
                    "2. ควรเคลื่อนไหวและออกกำลังกายอย่างสม่ำเสมอทุกวัน หรือเกือบทุกวัน อย่างน้อยให้เหนื่อยพอควร โดยหายใจกระชั้นขึ้น สะสมให้ได้อย่างน้อยวันละ 30 นาที โดยอาจจะแบ่งเป็น 2 - 3 ครั้งก็ได้ จะเป็นกิจกรรมออกกำลังกายที่เป็นเรื่องเป็นราวหรือการออกแรงในกิจวัตรประจำวัน เช่นเดินเร็ว ถีบจักรยาน ลีลาศ หรืองานบ้าน งานสวน ให้เลือกทำตามใจชอบ ถ้าคุณต้องการมีสมรรถภาพที่ดีก็ต้องออกกำลังกายแบบแอโรบิก เช่น เดินเร็ว ๆ วิ่งเหยาะ ถีบจักรยานเร็วๆ กระโดดเชือก ว่ายน้ำ เล่นกีฬา เป็นต้น ให้รู้สึกเหนื่อยมาก หรือหอบ อย่างน้อยวันละ 20 - 30 นาที อย่างน้อยสัปดาห์ละ 3 วัน ที่ง่าย ที่สุดคือ การเดิน\n";
        }
        else if (result >23 && result<=24.90 )
        {
            bmi ="ท้วม / โรคอ้วนระดับ 1" +
                    "ข้อแนะนำ\n" +
                    "1. ควรควบคุมอาหาร โดยลดปริมาณอาหารหรือปรับเปลี่ยนอาหารจากที่ให้พลังงานมากเป็นอาหารที่ให้พลังงานน้อย ทั้งนี้พลังงานที่ได้รับไม่ควรต่ำกว่า 1200 กิโลแคลอรีต่อวัน โดยลดอาหารไขมัน/ เนื้อสัตว์ อาหารผัด/ทอด ขนมหวาน เครื่องดื่มที่ใส่น้ำตาล แอลกอฮอล์ แต่ต้องกินอาหารให้หลากหลายในสัดส่วนที่เหมาะสม กินข้าวและแป้ง รวมทั้งเมล็ดธัญพืชอื่น ๆ ไม่น้อยกว่าวันละ 6 ทัพพี กินผัก รวมทั้งเมล็ดถั่ว ผลไม้ ไม่ต่ำกว่าวันละ 5 ส่วน หรือครึ่งกิโลกรัม เพื่อลดพลังงานเข้า ร่างกายจะได้ใช้พลังงานส่วนเกินที่สะสมอยู่ในรูปไขมันแทน\n" +
                    "2. ควรเคลื่อนไหวและออกกำลังกายแบบแอโรบิกอย่างสม่ำเสมอทุกวัน หรือเกือบทุกวันอย่างน้อยให้เหนื่อยพอควร โดยหายใจกระชั้นขึ้น สะสมอย่างน้อยวันละ 30 นาที อาจแบ่งเป็นวันละ 2 - 3 ครั้งก็ได้ เช่น เดินเร็ว ถีบจักรยาน เป็นต้น หากยังไม่เคยออกกำลังกายเริ่มแรกควร ออกกำลังเบา ๆ ที่ง่ายที่สุดคือ การเดิน ใช้เวลาน้อย ๆ ก่อน จากนั้นค่อย ๆ เพิ่มเวลาขึ้นในแต่ละสัปดาห์ โดยยังไม่เพิ่มความหนัก เมื่อร่างกายปรับตัวได้จึงค่อยเพิ่มความหนัก หรือความเหนื่อยตามที่ต้องการ และเพิ่มการเคลื่อนไหวร่างกายให้มากขึ้นในชีวิตประจำวัน เพื่อให้มีการใช้พลังงานเพิ่มขึ้น อย่างน้อยวันละ 200 - 300 กิโลแคลอรี\n" +
                    "3. ควรฝึกความแข็งแรงของกล้ามเนื้อ ด้วยการฝึกกายบริหารหรือยกน้ำหนัก จะช่วยเสริมให้ร่างกายมีการใช้พลังงานเพิ่มมากขึ้น ทำให้ไขมันลดลง\n";
        }
        else if (result >25 && result<=29.90 )
        {
            bmi ="อ้วน / โรคอ้วนระดับ 2" +
                    "ข้อแนะนำ\n" +
                    "1. ควรควบคุมอาหารโดยลดปริมาณอาหารหรือปรับเปลี่ยนอาหารจากที่ให้พลังงานมากเป็นอาหารที่ให้พลังงานน้อย ทั้งนี้พลังงานที่ได้รับไม่ควรต่ำกว่า 1200 กิโลแคลอรีต่อวัน โดยลดอาหารไขมัน/เนื้อสัตว์ อาหารผัด/ทอด ขนมหวาน เครื่องดื่มที่ใส่น้ำตาล แอลกอฮอล์ แต่ต้องกินอาหารให้หลากหลายในสัดส่วนที่เหมาะสม กินข้าวและแป้งรวมทั้งเมล็ดธัญพืชอื่น ๆ ไม่น้อยกว่าวันละ 6 ทัพพี กินผักรวมทั้งเมล็ดถั่ว ผลไม้ไม่ต่ำกว่าวันละ 5 ส่วน หรือครึ่งกิโลกรัมเพื่อลดพลังงานเข้า ร่างกายจะได้ใช้พลังงานส่วนเกินที่สะสมอยู่ในรูปไขมันแทน\n" +
                    "2. ควรเคลื่อนไหวและออกกำลังกายแบบแอโรบิกอย่างสม่ำเสมอทุกวันหรือเกือบทุกวันอย่างน้อยให้เหนื่อยพอควรโดยหายใจกระชั้นขึ้น ประมาณ 40-60 นาทีต่อวัน หรือแบ่งเป็นวันละ 2 ครั้ง ๆ ละ 20 - 30 นาที เช่น เดินเร็ว ถีบจักรยาน เป็นต้น หากยังไม่เคยออกกำลังกายเริ่มแรก ควรออกกำลังเบา ๆ ที่ง่ายที่สุดคือ การเดิน ใช้เวลาน้อยๆ ก่อน จากนั้น ค่อย ๆ เพิ่มเวลาขึ้นในแต่ละสัปดาห์ โดยยังไม่เพิ่มความหนัก เมื่อร่างกายปรับตัวได้จึงค่อยเพิ่มความหนัก หรือความเหนื่อยตามที่ต้องการและเพิ่มการเคลื่อนไหวร่างกายให้มากขึ้นในชีวิตประจำวัน เพื่อให้มีการใช้พลังงานเพิ่มขึ้น อย่างน้อยวันละ 200 - 300 กิโลแคลอรี\n" +
                    "3. ควรฝึกความแข็งแรงของกล้ามเนื้อ ด้วยการฝึกกายบริหารหรือยกน้ำหนัก จะช่วยเสริมให้ร่างกายมีการใช้พลังงานเพิ่มมากขึ้น ทำให้ไขมันลดลง\n" +
                    "4. ถ้าคุณสามารถลดพลังงานเข้าจากอาหารลงได้วันละ 400 กิโลแคลอรี และเพิ่มการใช้ พลังงานจากการออกกำลังกายวันละ 200 กิโลแคลอรี รวมแล้วคุณมีพลังงาพร่องลงไปวันละ 600 กิโลแคลอรี ออกกำลังกายประมาณ 6 วัน คิดเป็นพลังงานพร่อง 3600 กิโลแคลอรี คุณจะลดไขมันลงได้ประมาณครึ่งกิโลกรัมต่อสัปดาห์ พลังงานเข้าหรือออก 3500 กิโลแคลอรี จะเพิ่มหรือลดไขมันได้ 1 ปอนด์ หรือ 0.45 กิโลกรัม\n" +
                    "5. ควรปรึกษาแพทย์หรือผู้เชี่ยวชาญในการลดและควบคุมน้ำหนัก\n";
        }
        else
        {
            bmi ="อ้วนมาก / โรคอ้วนระดับ 3" +
                    "ข้อแนะนำ\n" +
                    "1. ควรควบคุมอาหารโดยลดปริมาณอาหารหรือปรับเปลี่ยนอาหารจากที่ให้พลังงานมากเป็นอาหารที่ให้พลังงานน้อย ทั้งนี้พลังงานที่ได้รับไม่ควรต่ำกว่า 1200 กิโลแคลอรีต่อวัน โดยลดอาหารไขมัน/เนื้อสัตว์ อาหารผัด/ทอด ขนมหวาน เครื่องดื่มที่ใส่น้ำตาล แอลกอฮอล์ แต่ต้องกินอาหารให้หลากหลายในสัดส่วนที่เหมาะสม กินข้าวและแป้งรวมทั้งเมล็ดธัญพืชอื่น ๆ ไม่น้อยกว่าวันละ 6 ทัพพี กินผักรวมทั้งเมล็ดถั่ว ผลไม้ไม่ต่ำกว่าวันละ 5 ส่วน หรือครึ่งกิโลกรัมเพื่อลดพลังงานเข้า ร่างกายจะได้ใช้พลังงานส่วนเกินที่สะสมอยู่ในรูปไขมันแทน\n" +
                    "2. ควรเคลื่อนไหวและออกกำลังกายแบบแอโรบิกอย่างสม่ำเสมอทุกวันหรือเกือบทุกวันอย่างน้อยให้เหนื่อยพอควรโดยหายใจกระชั้นขึ้น ประมาณ 40-60 นาทีต่อวัน หรือแบ่งเป็นวันละ 2 ครั้ง ๆ ละ 20 - 30 นาที เช่น เดินเร็ว ถีบจักรยาน เป็นต้น หากยังไม่เคยออกกำลังกายเริ่มแรก ควรออกกำลังเบา ๆ ที่ง่ายที่สุดคือ การเดิน ใช้เวลาน้อยๆ ก่อน จากนั้น ค่อย ๆ เพิ่มเวลาขึ้นในแต่ละสัปดาห์ โดยยังไม่เพิ่มความหนัก เมื่อร่างกายปรับตัวได้จึงค่อยเพิ่มความหนัก หรือความเหนื่อยตามที่ต้องการและเพิ่มการเคลื่อนไหวร่างกายให้มากขึ้นในชีวิตประจำวัน เพื่อให้มีการใช้พลังงานเพิ่มขึ้น อย่างน้อยวันละ 200 - 300 กิโลแคลอรี\n" +
                    "3. ควรฝึกความแข็งแรงของกล้ามเนื้อ ด้วยการฝึกกายบริหารหรือยกน้ำหนัก จะช่วยเสริมให้ร่างกายมีการใช้พลังงานเพิ่มมากขึ้น ทำให้ไขมันลดลง\n" +
                    "4. ถ้าคุณสามารถลดพลังงานเข้าจากอาหารลงได้วันละ 400 กิโลแคลอรี และเพิ่มการใช้ พลังงานจากการออกกำลังกายวันละ 200 กิโลแคลอรี รวมแล้วคุณมีพลังงาพร่องลงไปวันละ 600 กิโลแคลอรี ออกกำลังกายประมาณ 6 วัน คิดเป็นพลังงานพร่อง 3600 กิโลแคลอรี คุณจะลดไขมันลงได้ประมาณครึ่งกิโลกรัมต่อสัปดาห์ พลังงานเข้าหรือออก 3500 กิโลแคลอรี จะเพิ่มหรือลดไขมันได้ 1 ปอนด์ หรือ 0.45 กิโลกรัม\n" +
                    "5. ควรปรึกษาแพทย์หรือผู้เชี่ยวชาญในการลดและควบคุมน้ำหนัก\n";
        }

        EditText editText = (EditText)findViewById(R.id.editMessage);
        editText.setText(String.valueOf(result)+" "+bmi, TextView.BufferType.EDITABLE);
    }

}