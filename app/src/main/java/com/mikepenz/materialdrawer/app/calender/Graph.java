package com.mikepenz.materialdrawer.app.calender;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.facebook.*;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.mikepenz.materialdrawer.app.DrawerActivity;
import com.mikepenz.materialdrawer.app.database.DBHelper;
import com.mikepenz.materialdrawer.app.database.DBGraph;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.app.R;
import com.mikepenz.materialdrawer.app.database.DBMain;
import com.mikepenz.materialdrawer.app.facebook.IntentUtil;
import com.mikepenz.materialdrawer.app.facebook.PrefUtil;
import com.mikepenz.materialdrawer.app.utils.CalendarFont;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import com.mikephil.charting.charts.CombinedChart;
import com.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.mikephil.charting.components.XAxis;
import com.mikephil.charting.components.XAxis.XAxisPosition;
import com.mikephil.charting.components.YAxis;
import com.mikephil.charting.data.BarData;
import com.mikephil.charting.data.BarDataSet;
import com.mikephil.charting.data.BarEntry;
import com.mikephil.charting.data.BubbleData;
import com.mikephil.charting.data.BubbleDataSet;
import com.mikephil.charting.data.BubbleEntry;
import com.mikephil.charting.data.CandleData;
import com.mikephil.charting.data.CandleDataSet;
import com.mikephil.charting.data.CandleEntry;
import com.mikephil.charting.data.CombinedData;
import com.mikephil.charting.data.Entry;
import com.mikephil.charting.data.LineData;
import com.mikephil.charting.data.LineDataSet;
import com.mikephil.charting.data.ScatterData;
import com.mikephil.charting.data.ScatterDataSet;
import com.mikephil.charting.interfaces.datasets.IDataSet;
import com.mikephil.charting.utils.ColorTemplate;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Graph extends AppCompatActivity {

    public static class mValue {
        public int gid =0;
        public String date;
        public double weight;
        public double height;
        public double bmi;
        public String mg;
    }

    mValue value = new mValue();
    mValue dbValue = new mValue();
    List<DBMain.GraphValue> dbValueG;
    private DBGraph dataBase;
    private DBMain dataBaseG;


    @Bind(R.id.gDateS)
    public Button rDateS;
    @Bind(R.id.gWeight)
    public EditText rWeight;
    @Bind(R.id.gHeight)
    public EditText rHeight;
    @Bind(R.id.gMessage)
    public TextView rBmi;
    @Bind(R.id.gMg)
    public TextView rMg;
    @Bind(R.id.shar_button1)
    public com.facebook.share.widget.ShareButton shareButton;
    private Drawer result = null;

    MaterialCalendarView widget;

    private CombinedChart mChart;
    private int itemcount = 12;

    protected String[] mMonths;

    public String resultBMI[];
    private CallbackManager callbackManager;
    private CallbackManager callbackManagerS;
    ShareDialog shareDialog;
    private TextView info;
    private ImageView profileImgView;
    private LoginButton loginButton;
    private PrefUtil prefUtil;
    private IntentUtil intentUtil;
    private  com.facebook.Profile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        callbackManagerS = CallbackManager.Factory.create();
        setContentView(R.layout.activity_graph);
        onFaceBook();
        profile = com.facebook.Profile.getCurrentProfile();
        if(profile != null)
        {
            String userId = profile.getId();
            String profileImgUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";
            Glide.with(Graph.this)
                    .load(profileImgUrl)
                    .into(profileImgView);
        }
        setTitle(R.string.drawer_item_Graph_header);
        ButterKnife.bind(this);//เริ่มให้ปลั๊กอิน
        // </editor-fold

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day =c.get(Calendar.DAY_OF_MONTH);
        String createDate = String.valueOf(year ) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);
        rDateS.setText(createDate);

        DBHelper mHelper;
        mHelper = new DBHelper(this);
        dataBase = new DBGraph(mHelper);
        dataBaseG = new DBMain(mHelper);
        int id=dataBase.CheckIDInDay();
        if(id!=0)
        {
            dbValue = dataBase.selectAllData(id);
            dbToLayout();
            onClickBmi();
        }

        rBmi.setMovementMethod(new ScrollingMovementMethod());
        resultBMI = new String[5];

        resultBMI[0]="\nแนะนำ\n" +
                "1. ควรกินอาหารให้หลากหลายครบ 5 หมู่ในสัดส่วนที่เหมาะสมและปริมาณมากขึ้น โดยเพิ่มอาหารประเภทที่ให้พลังงานมากขึ้น เช่น ไขมัน แป้ง ข้าว เนื้อสัตว์ นม\n" +
                "2. ควรเคลื่อนไหวและออกกำลังกายอย่างสม่ำเสมอทุกวันหรือเกือบทุกวัน ให้เหนื่อยพอควรโดยหายใจกระชั้นขึ้น เช่น เดินเร็ว ถีบจักรยาน รำมวยจีน ลีลาศจังหวะช้า รวมทั้งงานบ้าน งานสวน เป็นต้น สะสมให้ได้อย่างน้อยวันละ 30 นาทีอาจไม่จำเป็นต้องออกกำลังกายให้เหนื่อยมากหรือหอบ ที่ง่ายที่สุดคือ การเดิน\n";
        resultBMI[1]="\nข้อแนะนำ\n" +
                "1. ควรกินอาหารให้หลากหลายครบ 5 หมู่ในสัดส่วนที่เหมาะสม กินเท่าที่ร่างกายต้องการวันไหนกินมากเกินไป วันต่อมาก็กินลดลง กินอาหารพวกข้าวและแป้งรวมทั้งเมล็ดธัญพืชอื่น ๆ ให้มากขึ้นไม่น้อยกว่าวันละ 6 ทัพพี กินผัก รวมทั้งเมล็ดถั่ว ผลไม้ ไม่ต่ำกว่าวันละ 5 ส่วน หรือครึ่งกิโลกรัม เพื่อไม่ให้มีพลังงานส่วนเกินจะทำให้ควบคุมน้ำหนักได้ดีและสมดุล\n" +
                "2. ควรเคลื่อนไหวและออกกำลังกายอย่างสม่ำเสมอทุกวัน หรือเกือบทุกวัน อย่างน้อยให้เหนื่อยพอควร โดยหายใจกระชั้นขึ้น สะสมให้ได้อย่างน้อยวันละ 30 นาที โดยอาจจะแบ่งเป็น 2 - 3 ครั้งก็ได้ จะเป็นกิจกรรมออกกำลังกายที่เป็นเรื่องเป็นราวหรือการออกแรงในกิจวัตรประจำวัน เช่นเดินเร็ว ถีบจักรยาน ลีลาศ หรืองานบ้าน งานสวน ให้เลือกทำตามใจชอบ ถ้าคุณต้องการมีสมรรถภาพที่ดีก็ต้องออกกำลังกายแบบแอโรบิก เช่น เดินเร็ว ๆ วิ่งเหยาะ ถีบจักรยานเร็วๆ กระโดดเชือก ว่ายน้ำ เล่นกีฬา เป็นต้น ให้รู้สึกเหนื่อยมาก หรือหอบ อย่างน้อยวันละ 20 - 30 นาที อย่างน้อยสัปดาห์ละ 3 วัน ที่ง่าย ที่สุดคือ การเดิน\n";
        resultBMI[2]= "\nข้อแนะนำ\n" +
                "1. ควรควบคุมอาหาร โดยลดปริมาณอาหารหรือปรับเปลี่ยนอาหารจากที่ให้พลังงานมากเป็นอาหารที่ให้พลังงานน้อย ทั้งนี้พลังงานที่ได้รับไม่ควรต่ำกว่า 1200 กิโลแคลอรีต่อวัน โดยลดอาหารไขมัน/ เนื้อสัตว์ อาหารผัด/ทอด ขนมหวาน เครื่องดื่มที่ใส่น้ำตาล แอลกอฮอล์ แต่ต้องกินอาหารให้หลากหลายในสัดส่วนที่เหมาะสม กินข้าวและแป้ง รวมทั้งเมล็ดธัญพืชอื่น ๆ ไม่น้อยกว่าวันละ 6 ทัพพี กินผัก รวมทั้งเมล็ดถั่ว ผลไม้ ไม่ต่ำกว่าวันละ 5 ส่วน หรือครึ่งกิโลกรัม เพื่อลดพลังงานเข้า ร่างกายจะได้ใช้พลังงานส่วนเกินที่สะสมอยู่ในรูปไขมันแทน\n" +
                "2. ควรเคลื่อนไหวและออกกำลังกายแบบแอโรบิกอย่างสม่ำเสมอทุกวัน หรือเกือบทุกวันอย่างน้อยให้เหนื่อยพอควร โดยหายใจกระชั้นขึ้น สะสมอย่างน้อยวันละ 30 นาที อาจแบ่งเป็นวันละ 2 - 3 ครั้งก็ได้ เช่น เดินเร็ว ถีบจักรยาน เป็นต้น หากยังไม่เคยออกกำลังกายเริ่มแรกควร ออกกำลังเบา ๆ ที่ง่ายที่สุดคือ การเดิน ใช้เวลาน้อย ๆ ก่อน จากนั้นค่อย ๆ เพิ่มเวลาขึ้นในแต่ละสัปดาห์ โดยยังไม่เพิ่มความหนัก เมื่อร่างกายปรับตัวได้จึงค่อยเพิ่มความหนัก หรือความเหนื่อยตามที่ต้องการ และเพิ่มการเคลื่อนไหวร่างกายให้มากขึ้นในชีวิตประจำวัน เพื่อให้มีการใช้พลังงานเพิ่มขึ้น อย่างน้อยวันละ 200 - 300 กิโลแคลอรี\n" +
                "3. ควรฝึกความแข็งแรงของกล้ามเนื้อ ด้วยการฝึกกายบริหารหรือยกน้ำหนัก จะช่วยเสริมให้ร่างกายมีการใช้พลังงานเพิ่มมากขึ้น ทำให้ไขมันลดลง\n";

        resultBMI[3]="\nข้อแนะนำ\n" +
                "1. ควรควบคุมอาหารโดยลดปริมาณอาหารหรือปรับเปลี่ยนอาหารจากที่ให้พลังงานมากเป็นอาหารที่ให้พลังงานน้อย ทั้งนี้พลังงานที่ได้รับไม่ควรต่ำกว่า 1200 กิโลแคลอรีต่อวัน โดยลดอาหารไขมัน/เนื้อสัตว์ อาหารผัด/ทอด ขนมหวาน เครื่องดื่มที่ใส่น้ำตาล แอลกอฮอล์ แต่ต้องกินอาหารให้หลากหลายในสัดส่วนที่เหมาะสม กินข้าวและแป้งรวมทั้งเมล็ดธัญพืชอื่น ๆ ไม่น้อยกว่าวันละ 6 ทัพพี กินผักรวมทั้งเมล็ดถั่ว ผลไม้ไม่ต่ำกว่าวันละ 5 ส่วน หรือครึ่งกิโลกรัมเพื่อลดพลังงานเข้า ร่างกายจะได้ใช้พลังงานส่วนเกินที่สะสมอยู่ในรูปไขมันแทน\n" +
                "2. ควรเคลื่อนไหวและออกกำลังกายแบบแอโรบิกอย่างสม่ำเสมอทุกวันหรือเกือบทุกวันอย่างน้อยให้เหนื่อยพอควรโดยหายใจกระชั้นขึ้น ประมาณ 40-60 นาทีต่อวัน หรือแบ่งเป็นวันละ 2 ครั้ง ๆ ละ 20 - 30 นาที เช่น เดินเร็ว ถีบจักรยาน เป็นต้น หากยังไม่เคยออกกำลังกายเริ่มแรก ควรออกกำลังเบา ๆ ที่ง่ายที่สุดคือ การเดิน ใช้เวลาน้อยๆ ก่อน จากนั้น ค่อย ๆ เพิ่มเวลาขึ้นในแต่ละสัปดาห์ โดยยังไม่เพิ่มความหนัก เมื่อร่างกายปรับตัวได้จึงค่อยเพิ่มความหนัก หรือความเหนื่อยตามที่ต้องการและเพิ่มการเคลื่อนไหวร่างกายให้มากขึ้นในชีวิตประจำวัน เพื่อให้มีการใช้พลังงานเพิ่มขึ้น อย่างน้อยวันละ 200 - 300 กิโลแคลอรี\n" +
                "3. ควรฝึกความแข็งแรงของกล้ามเนื้อ ด้วยการฝึกกายบริหารหรือยกน้ำหนัก จะช่วยเสริมให้ร่างกายมีการใช้พลังงานเพิ่มมากขึ้น ทำให้ไขมันลดลง\n" +
                "4. ถ้าคุณสามารถลดพลังงานเข้าจากอาหารลงได้วันละ 400 กิโลแคลอรี และเพิ่มการใช้ พลังงานจากการออกกำลังกายวันละ 200 กิโลแคลอรี รวมแล้วคุณมีพลังงาพร่องลงไปวันละ 600 กิโลแคลอรี ออกกำลังกายประมาณ 6 วัน คิดเป็นพลังงานพร่อง 3600 กิโลแคลอรี คุณจะลดไขมันลงได้ประมาณครึ่งกิโลกรัมต่อสัปดาห์ พลังงานเข้าหรือออก 3500 กิโลแคลอรี จะเพิ่มหรือลดไขมันได้ 1 ปอนด์ หรือ 0.45 กิโลกรัม\n" +
                "5. ควรปรึกษาแพทย์หรือผู้เชี่ยวชาญในการลดและควบคุมน้ำหนัก\n";
        resultBMI[4]= "\nข้อแนะนำ\n" +
                "1. ควรควบคุมอาหารโดยลดปริมาณอาหารหรือปรับเปลี่ยนอาหารจากที่ให้พลังงานมากเป็นอาหารที่ให้พลังงานน้อย ทั้งนี้พลังงานที่ได้รับไม่ควรต่ำกว่า 1200 กิโลแคลอรีต่อวัน โดยลดอาหารไขมัน/เนื้อสัตว์ อาหารผัด/ทอด ขนมหวาน เครื่องดื่มที่ใส่น้ำตาล แอลกอฮอล์ แต่ต้องกินอาหารให้หลากหลายในสัดส่วนที่เหมาะสม กินข้าวและแป้งรวมทั้งเมล็ดธัญพืชอื่น ๆ ไม่น้อยกว่าวันละ 6 ทัพพี กินผักรวมทั้งเมล็ดถั่ว ผลไม้ไม่ต่ำกว่าวันละ 5 ส่วน หรือครึ่งกิโลกรัมเพื่อลดพลังงานเข้า ร่างกายจะได้ใช้พลังงานส่วนเกินที่สะสมอยู่ในรูปไขมันแทน\n" +
                "2. ควรเคลื่อนไหวและออกกำลังกายแบบแอโรบิกอย่างสม่ำเสมอทุกวันหรือเกือบทุกวันอย่างน้อยให้เหนื่อยพอควรโดยหายใจกระชั้นขึ้น ประมาณ 40-60 นาทีต่อวัน หรือแบ่งเป็นวันละ 2 ครั้ง ๆ ละ 20 - 30 นาที เช่น เดินเร็ว ถีบจักรยาน เป็นต้น หากยังไม่เคยออกกำลังกายเริ่มแรก ควรออกกำลังเบา ๆ ที่ง่ายที่สุดคือ การเดิน ใช้เวลาน้อยๆ ก่อน จากนั้น ค่อย ๆ เพิ่มเวลาขึ้นในแต่ละสัปดาห์ โดยยังไม่เพิ่มความหนัก เมื่อร่างกายปรับตัวได้จึงค่อยเพิ่มความหนัก หรือความเหนื่อยตามที่ต้องการและเพิ่มการเคลื่อนไหวร่างกายให้มากขึ้นในชีวิตประจำวัน เพื่อให้มีการใช้พลังงานเพิ่มขึ้น อย่างน้อยวันละ 200 - 300 กิโลแคลอรี\n" +
                "3. ควรฝึกความแข็งแรงของกล้ามเนื้อ ด้วยการฝึกกายบริหารหรือยกน้ำหนัก จะช่วยเสริมให้ร่างกายมีการใช้พลังงานเพิ่มมากขึ้น ทำให้ไขมันลดลง\n" +
                "4. ถ้าคุณสามารถลดพลังงานเข้าจากอาหารลงได้วันละ 400 กิโลแคลอรี และเพิ่มการใช้ พลังงานจากการออกกำลังกายวันละ 200 กิโลแคลอรี รวมแล้วคุณมีพลังงาพร่องลงไปวันละ 600 กิโลแคลอรี ออกกำลังกายประมาณ 6 วัน คิดเป็นพลังงานพร่อง 3600 กิโลแคลอรี คุณจะลดไขมันลงได้ประมาณครึ่งกิโลกรัมต่อสัปดาห์ พลังงานเข้าหรือออก 3500 กิโลแคลอรี จะเพิ่มหรือลดไขมันได้ 1 ปอนด์ หรือ 0.45 กิโลกรัม\n" +
                "5. ควรปรึกษาแพทย์หรือผู้เชี่ยวชาญในการลดและควบคุมน้ำหนัก\n";

        int value[] = {R.id.gBMI,R.id.gText1,R.id.gText3,R.id.gText4,R.id.gCalBMI,R.id.gMessage,R.id.gWeight,R.id.gHeight,R.id.gDateS,R.id.gText5,R.id.gText6,R.id.gMg};

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
                            Toast.makeText(Graph.this, ((Nameable) drawerItem).getName().getText(Graph.this), Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }
                }).build();

        dbValueG = dataBaseG.selectAllDataGraph();

        if(dbValueG.size()!=0)
        {
            itemcount = dbValueG.size();
            mMonths = new String[itemcount];
            float[] bmi = new float[itemcount];
            float[] wight = new float[itemcount];

            int i=0;
            for (DBMain.GraphValue e:dbValueG) {
                mMonths[i]= e.date;
                bmi[i]=(float) e.bmi;
                wight[i]=(float) e.weight;
                i++;
            }

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(false);

            mChart = (CombinedChart) findViewById(R.id.chart1);
            mChart.setDescription("");
            mChart.setBackgroundColor(Color.rgb(252,228,236));
            mChart.setDrawGridBackground(false);
            mChart.setDrawBarShadow(false);

            // draw bars behind lines
            mChart.setDrawOrder(new DrawOrder[] {
                    DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER
            });

            YAxis rightAxis = mChart.getAxisRight();
            rightAxis.setDrawGridLines(false);

            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.setDrawGridLines(false);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setPosition(XAxisPosition.BOTH_SIDED);

            CombinedData data = new CombinedData(mMonths);

            data.setData(generateLineData(bmi));
            data.setData(generateBarData(wight));
//        data.setData(generateBubbleData());
//         data.setData(generateScatterData());
//         data.setData(generateCandleData());

            mChart.setData(data);
            mChart.invalidate();
        }


    }
    private void dbToLayout(){

        rDateS.setText(dbValue.date);
        rWeight.setText(String.valueOf(dbValue.weight));
        rHeight.setText(String.valueOf(dbValue.height));
        rBmi.setText(String.valueOf(dbValue.bmi));

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

    void setValueInClass() {
        value.gid = dbValue.gid;
        this.setDate();
        this.setWeight();
        this.setHeight();
        this.setBMI();
        this.setMg();

    }
    void setDate() {
        value.date = rDateS.getText().toString();
    }
    void setWeight() {
        value.weight = Double.parseDouble(rWeight.getText().toString());
    }
    void setHeight() {
        value.height = Double.parseDouble(rHeight.getText().toString());
    }
    void setBMI() { value.bmi = value.weight / ((value.height/100)*(value.height/100));   }
    void setMg() { value.mg = rMg.getText().toString();    }


    @OnClick (R.id.gCalBMI)
    void onClickBmi ()
    {
        this.setHeight();
        this.setWeight();

        double result = value.weight / ((value.height/100)*(value.height/100));
        String gbmi;
        result = Math.floor(result * 100) / 100;

        if(result <= 18.50)
        {
            gbmi ="น้ำหนักน้อย / ผอม" ;


        }
        else if (result >18.50 && result<= 22.90)
        {
            gbmi ="ปกติ (สุขภาพดี)";


        }
        else if (result >23 && result<=24.90 )
        {
           gbmi ="ท้วม / โรคอ้วนระดับ 1";

        }
        else if (result >25 && result<=29.90 )
        {
            gbmi ="อ้วน / โรคอ้วนระดับ 2" ;

        }
        else
        {
            gbmi ="อ้วนมาก / โรคอ้วนระดับ 3" ;

        }

        TextView editText = (TextView)findViewById(R.id.gMessage);
        editText.setText(String.valueOf(result), TextView.BufferType.EDITABLE);
        TextView editTextbmi = (TextView)findViewById(R.id.gMg);
        editTextbmi.setText( gbmi, TextView.BufferType.EDITABLE);
    }
    @OnClick (R.id.gBMI)
    void onClickgBmi ()
    {
        this.setHeight();
        this.setWeight();



        double result = value.weight / ((value.height/100)*(value.height/100));
        String bmi;
        result = Math.floor(result * 100) / 100;

        if(result <= 18.50)
        {
            android.app.AlertDialog.Builder builder =
                    new android.app.AlertDialog.Builder(this);
            builder.setTitle("แนะนำสุขภาพ");
            builder.setMessage(resultBMI[0]);
            builder.setPositiveButton(getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setValueInClass();

                            finish();
                        }
                    });

            builder.show();
        }
        else if (result >18.50 && result<= 22.90)
        {
            android.app.AlertDialog.Builder builder =
                    new android.app.AlertDialog.Builder(this);
            builder.setTitle("แนะนำสุขภาพ");
            builder.setMessage(resultBMI[1]);
            builder.setPositiveButton(getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setValueInClass();

                            finish();
                        }
                    });

            builder.show();

        }
        else if (result >23 && result<=24.90 )
        {
            android.app.AlertDialog.Builder builder =
                    new android.app.AlertDialog.Builder(this);
            builder.setTitle("แนะนำสุขภาพ");
            builder.setMessage(resultBMI[2]);
            builder.setPositiveButton(getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setValueInClass();

                            finish();
                        }
                    });

            builder.show();

        }
        else if (result >25 && result<=29.90 )
        {
            android.app.AlertDialog.Builder builder =
                    new android.app.AlertDialog.Builder(this);
            builder.setTitle("แนะนำสุขภาพ");
            builder.setMessage(resultBMI[3]);
            builder.setPositiveButton(getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setValueInClass();

                            finish();
                        }
                    });

            builder.show();

        }
        else
        {
            android.app.AlertDialog.Builder builder =
                    new android.app.AlertDialog.Builder(this);
            builder.setTitle("แนะนำสุขภาพ");
            builder.setMessage(resultBMI[4]);
            builder.setPositiveButton(getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setValueInClass();

                            finish();
                        }
                    });

            builder.show();

        }

    }

    private LineData generateLineData(float[] value) {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < itemcount; index++)
            entries.add(new Entry(value[index], index));

        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.rgb(240, 238, 70));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        d.addDataSet(set);

        return d;
    }
    private BarData generateBarData(float[] value) {

        BarData d = new BarData();

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int index = 0; index < itemcount; index++)
            entries.add(new BarEntry(value[index], index));

        BarDataSet set = new BarDataSet(entries, "Bar DataSet");
        set.setColor(Color.rgb(60, 220, 78));
        set.setValueTextColor(Color.rgb(60, 220, 78));
        set.setValueTextSize(10f);
        d.addDataSet(set);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return d;
    }
    protected ScatterData generateScatterData() {

        ScatterData d = new ScatterData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < itemcount; index++)
            entries.add(new Entry(getRandom(20, 15), index));

        ScatterDataSet set = new ScatterDataSet(entries, "Scatter DataSet");
        set.setColor(Color.GREEN);
        set.setScatterShapeSize(7.5f);
        set.setDrawValues(false);
        set.setValueTextSize(10f);
        d.addDataSet(set);

        return d;
    }
    protected CandleData generateCandleData() {

        CandleData d = new CandleData();

        ArrayList<CandleEntry> entries = new ArrayList<CandleEntry>();

        for (int index = 0; index < itemcount; index++)
            entries.add(new CandleEntry(index, 20f, 10f, 13f, 17f));

        CandleDataSet set = new CandleDataSet(entries, "Candle DataSet");
        set.setColor(Color.rgb(80, 80, 80));
        set.setBodySpace(0.3f);
        set.setValueTextSize(10f);
        set.setDrawValues(false);
        d.addDataSet(set);

        return d;
    }
    protected BubbleData generateBubbleData() {

        BubbleData bd = new BubbleData();

        ArrayList<BubbleEntry> entries = new ArrayList<BubbleEntry>();

        for (int index = 0; index < itemcount; index++) {
            float rnd = getRandom(20, 30);
            entries.add(new BubbleEntry(index, rnd, rnd));
        }

        BubbleDataSet set = new BubbleDataSet(entries, "Bubble DataSet");
        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.WHITE);
        set.setHighlightCircleWidth(1.5f);
        set.setDrawValues(true);
        bd.addDataSet(set);

        return bd;
    }

    private float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    @OnClick(R.id.gSave)
    void saveGraph() {

        android.app.AlertDialog.Builder builder =
                new android.app.AlertDialog.Builder(this);
        builder.setTitle("บันทึกข้อมูล");
        builder.setMessage("ยืนยันการบันทึกข้อมูล");
        builder.setPositiveButton(getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setValueInClass();
                        if (dbValue.gid != 0) {
                            dataBase.update(value.date, value.weight, value.height, value.bmi, value.gid);
                        }else {
                            dataBase.insert(value.date, value.weight, value.height, value.bmi, value.mg);
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
    @OnClick(R.id.gDelete)
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

                        if (dbValue.gid != 0) {
                            dataBase.delete(dbValue.gid);
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


    private void deleteAccessToken() {
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null){
                    //User logged out
                    prefUtil.clearToken();
                    clearUserArea();
                }
            }
        };
    }

    private void clearUserArea() {
        info.setText("");
        profileImgView.setImageDrawable(null);
    }

    @Override
    public void onResume() {
        super.onResume();
//        deleteAccessToken();
//        Profile profile = Profile.getCurrentProfile();
//        info.setText(message(profile));
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private String message(Profile profile) {
        StringBuilder stringBuffer = new StringBuilder();
        if (profile != null) {
            stringBuffer.append("Welcome ").append(profile.getName());
        }
        return stringBuffer.toString();
    }
    public void onFaceBook() {


        prefUtil = new PrefUtil(this);
        intentUtil = new IntentUtil(this);

        info = (TextView) findViewById(R.id.info);
        profileImgView = (ImageView) findViewById(R.id.profile_img);
        loginButton = (LoginButton) findViewById(R.id.login_button1);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();
                //info.setText(message(profile));

                String userId = loginResult.getAccessToken().getUserId();
                String accessToken = loginResult.getAccessToken().getToken();

                // save accessToken to SharedPreference
                prefUtil.saveAccessToken(accessToken);

                String profileImgUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";


                Glide.with(Graph.this)
                        .load(profileImgUrl)
                        .into(profileImgView);
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
                info.setText("Login attempt failed.");
            }
        });
    }

    public void saveScrr(View v) {
        View view = findViewById(android.R.id.content).getRootView();
        view.setDrawingCacheEnabled(true);
        Bitmap bm = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        try {
            Date d = new Date();
            String filename  = (String)DateFormat.format("kkmmss-MMddyyyy"
                    , d.getTime());
            File dir = new File(Environment.getExternalStorageDirectory()
                    , "/Pictures/" + filename + ".jpg");
            FileOutputStream out = new FileOutputStream(dir);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            out.write(bos.toByteArray());

            if (dir.exists()) {

                Bitmap image = BitmapFactory.decodeFile(dir.getAbsolutePath());
                        SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();

                ImageView myImage = (ImageView) findViewById(R.id.profile_img);
                myImage.setImageBitmap(image);
                //ShareApi.share(content, null);
                shareDialog = new ShareDialog(this);

                        shareDialog = new ShareDialog(this);
        String userId = profile.getId();
        String profileImgUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("Calendar Wooman -History Weighs")
                .setContentDescription(
                        "Test Projuct Minny <3")
                .setContentUrl(Uri.parse("https://www.google.com/"))
                .setImageUrl(Uri.parse(profileImgUrl))
                .build();



                shareDialog.show(content);
            }



            Toast.makeText(getApplicationContext(), "Shared It!"
                    , Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.shar_button1)
    public void shardToFaceBook(){

        View v =new View(this);
        saveScrr(v);



//        shareDialog = new ShareDialog(this);
//        String userId = profile.getId();
//        String profileImgUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";
//        ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                .setContentTitle("Calendar Wooman -History Weighs")
//                .setContentDescription(
//                        "Test Projuct Minny <3")
//                .setContentUrl(Uri.parse("https://www.google.com/"))
//                .setImageUrl(Uri.parse(profileImgUrl))
//                .build();
//
//        shareDialog.show(linkContent);

    }



}
