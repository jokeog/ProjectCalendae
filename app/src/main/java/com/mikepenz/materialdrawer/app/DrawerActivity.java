package com.mikepenz.materialdrawer.app;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.app.calender.Contracaption;
import com.mikepenz.materialdrawer.app.calender.Graph;
import com.mikepenz.materialdrawer.app.calender.Menstruation;
import com.mikepenz.materialdrawer.app.calender.Pregnant;
import com.mikepenz.materialdrawer.app.calender.Profile;
import com.mikepenz.materialdrawer.app.database.DBHelper;
import com.mikepenz.materialdrawer.app.database.DBMain;
import com.mikepenz.materialdrawer.app.decorators.EventDecorator;
import com.mikepenz.materialdrawer.app.decorators.HighlightWeekendsDecorator;
import com.mikepenz.materialdrawer.app.decorators.MySelectorDecorator;
import com.mikepenz.materialdrawer.app.decorators.OneDayDecorator;
import com.mikepenz.materialdrawer.app.utils.CalendarFont;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.RecyclerViewCacheUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrawerActivity extends AppCompatActivity {
    private static final int PROFILE_SETTING = 1;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private boolean opened = false;
    public  static  String appPath;

    @Bind(R.id.button)
    Button button;

    @Bind(R.id.calendarView)
    MaterialCalendarView widget;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_dark_toolbar);
        ButterKnife.bind(this);
        // <editor-fold desc=" Set Calendar">
        widget.addDecorators(
                new MySelectorDecorator(this),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );


        int value[] = {R.id.button,R.id.button2,R.id.button3,R.id.button4,R.id.button5};
        CalendarFont font = new CalendarFont() ;
        font.setFonts(value, this);


        widget.setHeaderTextAppearance(R.style.TextAppearance_AppCompat_Large);
        widget.setDateTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        widget.setWeekDayTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        //widget.setSelectionMode(1); 1 or 2
        //widget.setOnDateChangedListener(this);
        //widget.setTileSizeDp(42);
        //widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        Calendar calendar = Calendar.getInstance();
        widget.setSelectedDate(calendar.getTime());

        calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1);
        widget.setMinimumDate(calendar.getTime());

        calendar.set(calendar.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        widget.setMaximumDate(calendar.getTime());
        setCalendarWidget();
        // </editor-fold
        // <editor-fold desc=" Set ที่เก็บ File">
        PackageManager m = getPackageManager();
        appPath = getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(appPath, 0);
            appPath = p.applicationInfo.dataDir;
        } catch (PackageManager.NameNotFoundException e) {

        }
        // </editor-fold
        // <editor-fold desc=" Set Menu">
        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        final IProfile profile = new ProfileDrawerItem().withName("Minny").withEmail("Thipsudasansuwan@gmail.com").withIcon("https://scontent.fbkk1-1.fna.fbcdn.net/hphotos-xal1/v/t1.0-9/6599_936554129769340_1064584668432410236_n.jpg?oh=ee89ac6349e1165aaf977c27d610d1aa&oe=57449387").withIdentifier(100);
        final IProfile profile2 = new ProfileDrawerItem().withName("Bernat Borras").withEmail("alorma@github.com").withIcon(Uri.parse("https://avatars3.githubusercontent.com/u/887462?v=3&s=460")).withIdentifier(101);
        final IProfile profile3 = new ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(R.drawable.profile2).withIdentifier(102);
        final IProfile profile4 = new ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(R.drawable.profile3).withIdentifier(103);
        final IProfile profile5 = new ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(R.drawable.profile4).withIdentifier(104);
        final IProfile profile6 = new ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(R.drawable.profile5).withIdentifier(105);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile,
                        /*profile2,
                        profile3,
                        profile4,
                        profile5,
                        profile6,*/
                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        new ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_plus).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING),
                        new ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
            @Override
            public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                //sample usage of the onProfileChanged listener
                //if the clicked item has the identifier 1 add a new profile ;)
                if (profile instanceof IDrawerItem && ((IDrawerItem) profile).getIdentifier() == PROFILE_SETTING) {
                    int count = 100 + headerResult.getProfiles().size() + 1;
                    IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman" + count).withEmail("batman" + count + "@gmail.com").withIcon(R.drawable.profile5).withIdentifier(count);
                    if (headerResult.getProfiles() != null) {
                        //we know that there are 2 setting elements. set the new profile above them ;)
                        headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                    } else {
                        headerResult.addProfiles(newProfile);
                    }
                }

                //false if you have not consumed the event and it should close the drawer
                return false;
            }
        })
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_profile_header).withDescription(R.string.drawer_item_profile_header_desc).withIcon(GoogleMaterial.Icon.gmd_face).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_Menstr_header).withDescription(R.string.drawer_item_Menstr_header_desc).withIcon(GoogleMaterial.Icon.gmd_favorite).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_Pregnant_header).withDescription(R.string.drawer_item_Pregnant_header_desc).withIcon(GoogleMaterial.Icon.gmd_alarm).withIdentifier(3).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_Contracation_header).withDescription(R.string.drawer_item_Contracation_header_desc).withIcon(GoogleMaterial.Icon.gmd_favorite_outline).withIdentifier(4).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_Graph_header).withDescription(R.string.drawer_item_Graph_header_desc).withIcon(GoogleMaterial.Icon.gmd_trending_up).withIdentifier(5).withSelectable(false)
                    //    new SwitchDrawerItem().withName("").withIcon(GoogleMaterial.Icon.gmd_alarm).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)
                        /*new PrimaryDrawerItem().withName(R.string.drawer_item_compact_header).withDescription(R.string.drawer_item_compact_header_desc).withIcon(GoogleMaterial.Icon.gmd_sun).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_action_bar_drawer).withDescription(R.string.drawer_item_action_bar_drawer_desc).withIcon(FontAwesome.Icon.faw_home).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_multi_drawer).withDescription(R.string.drawer_item_multi_drawer_desc).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_non_translucent_status_drawer).withDescription(R.string.drawer_item_non_translucent_status_drawer_desc).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(4).withSelectable(false).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_advanced_drawer).withDescription(R.string.drawer_item_advanced_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_adb).withIdentifier(5).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_keyboard_util_drawer).withDescription(R.string.drawer_item_keyboard_util_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_labels).withIdentifier(6).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_embedded_drawer).withDescription(R.string.drawer_item_embedded_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_battery).withIdentifier(7).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_fullscreen_drawer).withDescription(R.string.drawer_item_fullscreen_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_labels).withIdentifier(8).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom_container_drawer).withDescription(R.string.drawer_item_custom_container_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_my_location).withIdentifier(9).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_menu_drawer).withDescription(R.string.drawer_item_menu_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_filter_list).withIdentifier(10).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_mini_drawer).withDescription(R.string.drawer_item_mini_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_battery_charging).withIdentifier(11).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_fragment_drawer).withDescription(R.string.drawer_item_fragment_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_disc_full).withIdentifier(12).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_collapsing_toolbar_drawer).withDescription(R.string.drawer_item_collapsing_toolbar_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_camera_rear).withIdentifier(13).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_persistent_compact_header).withDescription(R.string.drawer_item_persistent_compact_header_desc).withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(14).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_crossfade_drawer_layout_drawer).withDescription(R.string.drawer_item_crossfade_drawer_layout_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_format_bold).withIdentifier(15).withSelectable(false),
                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        new SecondaryDrawerItem().withName("Collapsable").withIcon(GoogleMaterial.Icon.gmd_collection_case_play).withIdentifier(19).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github).withIdentifier(20).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withIdentifier(21).withTag("Bullhorn"),
                        new DividerDrawerItem(),
                        new SwitchDrawerItem().withName("Switch").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        new SwitchDrawerItem().withName("Switch2").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener).withSelectable(false),
                        new ToggleDrawerItem().withName("Toggle").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        new DividerDrawerItem(),
                        new SecondarySwitchDrawerItem().withName("Secondary switch").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        new SecondarySwitchDrawerItem().withName("Secondary Switch2").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener).withSelectable(false),
                        new SecondaryToggleDrawerItem().withName("Secondary toggle").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)*/
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(DrawerActivity.this, Profile.class);
                            } else if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(DrawerActivity.this, Menstruation.class);
                            } else if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(DrawerActivity.this, Pregnant.class);
                            } else if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(DrawerActivity.this, Contracaption.class);
                            } else if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(DrawerActivity.this, Graph.class);
                            } else if (drawerItem.getIdentifier() == 6) {
                                intent = new Intent(DrawerActivity.this, KeyboardUtilActivity.class);
                            } else if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(DrawerActivity.this, EmbeddedDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 8) {
                                intent = new Intent(DrawerActivity.this, FullscreenDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 9) {
                                intent = new Intent(DrawerActivity.this, CustomContainerActivity.class);
                            } else if (drawerItem.getIdentifier() == 10) {
                                intent = new Intent(DrawerActivity.this, MenuDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 11) {
                                intent = new Intent(DrawerActivity.this, MiniDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 12) {
                                intent = new Intent(DrawerActivity.this, FragmentActivity.class);
                            } else if (drawerItem.getIdentifier() == 13) {
                                intent = new Intent(DrawerActivity.this, CollapsingToolbarActivity.class);
                            } else if (drawerItem.getIdentifier() == 14) {
                                intent = new Intent(DrawerActivity.this, PersistentDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 15) {
                                intent = new Intent(DrawerActivity.this, CrossfadeDrawerLayoutActvitiy.class);
                            } else if (drawerItem.getIdentifier() == 19) {
                                //showcase a simple collapsable functionality
                                if (opened) {
                                    //remove the items which are hidden
                                    result.removeItems(2000, 2001);
                                } else {
                                    int curPos = result.getPosition(drawerItem);
                                    result.addItemsAtPosition(
                                            curPos,
                                            new SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_8tracks).withIdentifier(2000),
                                            new SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_8tracks).withIdentifier(2001)
                                    );
                                }
                                opened = !opened;
                                return true;
                            } else if (drawerItem.getIdentifier() == 20) {
                                intent = new LibsBuilder()
                                        .withFields(R.string.class.getFields())
                                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                                        .intent(DrawerActivity.this);
                            }
                            if (intent != null) {
                                DrawerActivity.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        //if you have many different types of DrawerItems you can magically pre-cache those items to get a better scroll performance
        //make sure to init the cache after the DrawerBuilder was created as this will first clear the cache to make sure no old elements are in
        RecyclerViewCacheUtil.getInstance().withCacheSize(2).init(result);

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(21, false);

            //set the active profile
            headerResult.setActiveProfile(profile);
        }

        result.updateBadge(4, new StringHolder(""));
        // </editor-fold
    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
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

    @OnClick(R.id.mainRefesh)
    void setCalendarWidget(){
        DBHelper mHelper;
        mHelper = new DBHelper(this);
        DBMain dataBase=new DBMain(mHelper);
        pointCalendar(dataBase.selectAllDataContracaptionList(),Color.rgb(128, 0 ,128));
        pointCalendar(dataBase.selectAllData("pregnant", "pDate"), Color.rgb(139 ,101 ,8));
        pointCalendar(dataBase.selectAllData("menstuation", "startDate"), Color.rgb(139, 0, 0));
        //pointCalendar(dataBase.selectAllData("menstuation","endDate"),Color.rgb(0 ,0 ,139));
        pointCalendar(dataBase.selectAllData("menstuation","onlyDate"),Color.rgb(255 ,127 ,0));
        pointCalendar(dataBase.selectAllData("menstuation","nextMonth"),Color.rgb(0 ,0 ,139));



    }

    void pointCalendar (List<DBMain.CalenderValue> dateList,int color){
        Calendar calendar = Calendar.getInstance();
        ArrayList<CalendarDay> dates = new ArrayList<>();
        for(DBMain.CalenderValue e :dateList)
        {
            calendar.set(e.year, e.month-1, e.day);
            CalendarDay day = CalendarDay.from(calendar);
            dates.add(day);
        }
        widget.addDecorator(new EventDecorator(color, dates));
    }


}
