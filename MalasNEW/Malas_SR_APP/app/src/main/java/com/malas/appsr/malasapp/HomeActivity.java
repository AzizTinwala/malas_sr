package com.malas.appsr.malasapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.reflect.TypeToken;
import com.malas.appsr.malasapp.BeanClasses.AttendanceBean;
import com.malas.appsr.malasapp.BeanClasses.FocusedProductBean;
import com.malas.appsr.malasapp.BeanClasses.JointWorkBean;
import com.malas.appsr.malasapp.BeanClasses.PreviousDateAttendance;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.activities.AboutUsActivity;
import com.malas.appsr.malasapp.activities.ActivitySR;
import com.malas.appsr.malasapp.activities.BirthdayGreetings;
import com.malas.appsr.malasapp.activities.BroadcastActivity;
import com.malas.appsr.malasapp.activities.Catalogue;
import com.malas.appsr.malasapp.activities.ClockInOut;
import com.malas.appsr.malasapp.activities.ExpenseManagement;
import com.malas.appsr.malasapp.activities.HolidayCalendar;
import com.malas.appsr.malasapp.activities.LeaveMangementActivity;
import com.malas.appsr.malasapp.activities.LoginActivity;
import com.malas.appsr.malasapp.activities.PlaceOrderListActivity;
import com.malas.appsr.malasapp.activities.PlaceOrderToDistributor;
import com.malas.appsr.malasapp.activities.ReportGridActivity;
import com.malas.appsr.malasapp.activities.ShowOutLetActivity;
import com.malas.appsr.malasapp.activities.ShowRouteActivity;
import com.malas.appsr.malasapp.activities.TakeOrderActivity;
import com.malas.appsr.malasapp.activities.TakeStockActivity;
import com.malas.appsr.malasapp.activities.TourPlan;
import com.malas.appsr.malasapp.activities.UserProfileActivity;
import com.malas.appsr.malasapp.activities.salaryslip.SalarySlipPreview;
import com.malas.appsr.malasapp.adapter.AttendanceAdapter;
import com.malas.appsr.malasapp.adapter.JointWorkAdapter;
import com.malas.appsr.malasapp.adapter.PreviousAttendanceAdapter;
import com.malas.appsr.malasapp.adapter.SyncAttendanceAdapter;
import com.malas.appsr.malasapp.dbHandler.DatabaseHandler;
import com.malas.appsr.malasapp.location.GPSTracker;
import com.malas.appsr.malasapp.serverconnection.BackgroundWork;
import com.malas.appsr.malasapp.session.SessionManagement;
import com.malas.appsr.malasapp.util.NotificationUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@SuppressLint("SetTextI18n")
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DialogInterface.OnCancelListener, View.OnClickListener {
    private DatabaseHandler db;
    private GoogleApiClient mGoogleApiClient;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ArrayList<AttendanceReasonBean> attendanceReasonBeanArrayList;
    private ArrayList<PreviousDateAttendance> previousDayAttendance;
    private UserLoginInfoBean mUserLoginInfoBean;
    private boolean isPreviousDayAvailable;
    private String dateToAddAttendance;
    private TextView tvFocused;
    private boolean returnFromDSR;
    private TextView tvUpdateAttendanceStatus;

    private String rowId;
    private boolean isUnApprove;
    private String attendanceTypeStatus;
    private String attendanceReason;
    private AttendanceReasonBean institutionalArray;
    private String isFromTab;
    private LinearLayout llSync;
    private String offlineDateAdd = "";
    private boolean isFromSync;
    private boolean isFromProgressView;
    private boolean isFromPause;
    private boolean isDailyReportClicked;

    HashMap<String, Integer> count;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    //Session
    SessionManagement session;

    //Joint Work
    ArrayList<JointWorkBean> jointwork;
    JointWorkBean JointWork_With;

    private TextView reminderText;
    private Button inOut;
    private CardView reminderView;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initlizaing the Components
        setContentView(R.layout.activity_home);


        reminderView = this.findViewById(R.id.clock_inout_reminder);
        reminderText = this.findViewById(R.id.clock_inout_reminder_text);
        inOut = this.findViewById(R.id.clock_inout_reminder_btn);

        //initlize session
        session = new SessionManagement(this);
        if (session.showBirthday()) {
            startActivity(new Intent(this, BirthdayGreetings.class));
        }
        toolbar = findViewById(R.id.toolbar);
        ImageView imageViewLogout = findViewById(R.id.logout_imageview);
        TextView tvtrotry = findViewById(R.id.tv_territory);
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        //Initlizating Database Handler
        db = new DatabaseHandler(this);

        if (Utils.isInternetConnected(this)) {
            getJointWork();
        }
        //Attendace Status TextView
        tvUpdateAttendanceStatus = findViewById(R.id.attendance_update_status);
        tvFocused = findViewById(R.id.ll_focused_text);
        llSync = findViewById(R.id.ll_sync);
        TextView reminderStock = findViewById(R.id.ll_last_three_days);
        Button btnSync = findViewById(R.id.btn_sync);
        btnSync.setOnClickListener(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home Screen");

        //Location Services
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (mUserLoginInfoBean.getSalesType().equals("MT")) {
            Menu mtMenu = navigationView.getMenu();
            mtMenu.findItem(R.id.nav_route).setVisible(false);
            mtMenu.findItem(R.id.nav_outlet).setVisible(false);
            mtMenu.findItem(R.id.nav_take_stock).setVisible(false);
            mtMenu.findItem(R.id.nav_take_order).setVisible(false);
            mtMenu.findItem(R.id.nav_place_order).setVisible(false);
            mtMenu.findItem(R.id.nav_report).setVisible(false);
            mtMenu.findItem(R.id.nav_broadcast).setVisible(false);
            mtMenu.findItem(R.id.nav_activity).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        if (!mUserLoginInfoBean.getTerritoryName().equalsIgnoreCase("null")) {
            tvtrotry.setText(mUserLoginInfoBean.getTerritoryName());
        } else {
            tvtrotry.setText("SO");
        }


        TextView tvAppVersion = findViewById(R.id.app_version);
        TextView tvName = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        TextView tvEmail = navigationView.getHeaderView(0).findViewById(R.id.email);
        ImageView user_img = navigationView.getHeaderView(0).findViewById(R.id.nav_header_user_image);
        String url = Constant.BASE_URL2 + "user_image/" + mUserLoginInfoBean.getUserId() + ".jpg?v=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_person_add_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .circleCrop()
                .into(user_img);

        tvAppVersion.setText("App.Version: " + BuildConfig.VERSION_NAME);
        tvName.setText(mUserLoginInfoBean.getUserName());
        tvEmail.setText(mUserLoginInfoBean.getUserEmail());

        navigationView.getHeaderView(0).setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, UserProfileActivity.class)));
        imageViewLogout.setOnClickListener(v -> logout());

        if (dateOfTheMonth() && mUserLoginInfoBean.getSalesType().equals("GT")) {
            reminderStock.setVisibility(View.VISIBLE);
        } else {
            reminderStock.setVisibility(View.GONE);

        }
        Intent intent = getIntent();
        boolean isTerMul = intent.getBooleanExtra("isTerMultiple", false);
        //Entery POint
        if (isTerMul) {
            isFromProgressView = true;
            confirmTerritoryDialog();
        } else {
            progressView();
        }


        displayFirebaseRegId();
        inOut.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ClockInOut.class)));
    }

    private void getJointWork() {
        BackgroundWork back = new BackgroundWork(this);

        back.execute("Get JointWork Name");
        back.getDailog().setOnDismissListener(it -> {
            if (!back.getResult().isEmpty()) {
                if (db.getJointWorkList().size() > 0) {
                    db.deleteAllJointWorkListRecords();
                }
                try {
                    jointwork = new ArrayList<>();
                    JSONObject json;
                    json = new JSONObject(back.getResult());
                    JSONArray jsonArray = json.optJSONArray("response");

                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j_obj = jsonArray.getJSONObject(i);
                            jointwork.add(new JointWorkBean(j_obj.getInt("UID"), j_obj.getString("UNAME"), j_obj.getInt("Designation")));
                        }
                    }
                    db.addJointWrorkList(jointwork);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void progressView() {
        isFromProgressView = true;
        //focusedProduct
        if (Utils.isInternetConnected(HomeActivity.this)) {
            db.deleteAllFocusedRecords(mUserLoginInfoBean.getUserId());
            new focusedProduct().execute(String.valueOf(month()), String.valueOf(year()));

        } else {

            ArrayList<FocusedProductBean> focusedProductNameArray = db.getFocusedProduct(String.valueOf(month()), String.valueOf(year()), mUserLoginInfoBean.getUserId());
            String arrayFocusedResult = "";
            if (focusedProductNameArray != null && focusedProductNameArray.size() > 0) {
                for (int i = 0; i < focusedProductNameArray.size(); i++) {

                    if (i == focusedProductNameArray.size() - 1) {
                        arrayFocusedResult = arrayFocusedResult.concat(focusedProductNameArray.get(i).getProductName());

                    } else {
                        arrayFocusedResult = arrayFocusedResult.concat(focusedProductNameArray.get(i).getProductName() + " , ");

                    }

                }

                tvFocused.setText("Focus Product Of the Month - " + arrayFocusedResult);

            } else {
                // tvFocused.setText("No Focus Product Of the Month                   \t\t\t\t\t\t\t\t\t\t\t        ");
                tvFocused.setText("Please Clock-In & Clock-Out Timely to mark Your Attendance.                 \t\t\t\t\t\t\t\t\t\t\t        ");
            }
            tvFocused.setSelected(true);
        }

        if (Utils.isInternetConnected(HomeActivity.this)) {
            db.deleteAllAttendanceStatusRecords(mUserLoginInfoBean.getUserId());
            new getAttendanceStatus().execute();
        } else {

            AttendanceReasonBean attendanceReasonBean = db.getAttendanceStatusHome(currentDate(), mUserLoginInfoBean.getUserId());
            ComplexPreferences attendancePref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_LIST_PREF, MODE_PRIVATE);
            attendancePref.putObject(Constant.ATTENDANCE_LIST_OBJ, attendanceReasonBean);
            attendancePref.commit();
            if (attendanceReasonBean.getAttendanceMessage() != null && !attendanceReasonBean.getAttendanceMessage().equalsIgnoreCase("")) {

                //         tvUpdateAttendanceStatus.setVisibility(View.VISIBLE);
                tvUpdateAttendanceStatus.setText(attendanceReasonBean.getAttendanceMessage());

            } else {
                tvUpdateAttendanceStatus.setVisibility(View.GONE);
            }

            ///ReasonList of current day
            if (db.getAttendanceReasonList().size() > 0) {
                attendanceReasonBeanArrayList = new ArrayList<>();

                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

            } else {

                Type reasonType = new TypeToken<ArrayList<AttendanceReasonBean>>() {
                }.getType();
                ComplexPreferences reasonListPref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_REASON_LIST_PREF, MODE_PRIVATE);
                attendanceReasonBeanArrayList = reasonListPref.getArray(Constant.ATTENDANCE_REASON_LIST_OBJ, reasonType);
            }
            String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
            if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {

                final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                if (attendanceBeans != null && attendanceBeans.size() > 0) {
                    llSync.setVisibility(View.VISIBLE);
                    ArrayList<String> datesInAttendanceList = new ArrayList<>();

                    for (int i = 0; i < attendanceBeans.size(); i++) {
                        datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                    }
                    if (!datesInAttendanceList.contains(currentDate())) {
                        attendanceReasonBeanArrayList = new ArrayList<>();
                        attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                        dialogAttendance();
                    } else {
                        db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                        db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());
                    }
                } else {
                    llSync.setVisibility(View.GONE);
                    attendanceReasonBeanArrayList = new ArrayList<>();
                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                    dialogAttendance();
                }
            } else {

                //If current day false but if its last date is already aded to sync then need both dialog and sync
                assert isTodayAttendanceMarked != null;
                final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                if (isTodayAttendanceMarked.equalsIgnoreCase("false")) {
                    if (attendanceBeans != null && attendanceBeans.size() > 0) {
                        llSync.setVisibility(View.VISIBLE);
                        ArrayList<String> datesInAttendanceList = new ArrayList<>();

                        for (int i = 0; i < attendanceBeans.size(); i++) {
                            datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                        }

                        if (!datesInAttendanceList.contains(currentDate())) {
                            attendanceReasonBeanArrayList = new ArrayList<>();
                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        } else {
                            db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                            db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                        }
                    } else {
                        llSync.setVisibility(View.GONE);
                        attendanceReasonBeanArrayList = new ArrayList<>();
                        attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                        dialogAttendance();

                    }

                } else {
               /* final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                if (attendanceBeans != null && attendanceBeans.size() > 0) {
                    db.deleteAttendance();
                }*/
                    if (attendanceBeans != null && attendanceBeans.size() > 0) {
                        llSync.setVisibility(View.VISIBLE);
                        ArrayList<String> datesInAttendanceList = new ArrayList<>();

                        for (int i = 0; i < attendanceBeans.size(); i++) {
                            datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                        }
                        if (!datesInAttendanceList.contains(currentDate())) {
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        }
                    } else {
                        llSync.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_show_profile) {
            Intent in_showprofile = new Intent(HomeActivity.this, UserProfileActivity.class);
            startActivity(in_showprofile);
        }
        if (id == R.id.nav_leave_management) {
            Intent intent = new Intent(HomeActivity.this, LeaveMangementActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_outlet) {
            Intent in_navOut = new Intent(HomeActivity.this, ShowOutLetActivity.class);
            startActivity(in_navOut);
        } else if (id == R.id.nav_aboutus) {
            Intent in_navOut = new Intent(HomeActivity.this, AboutUsActivity.class);
            startActivity(in_navOut);
        } else if (id == R.id.nav_route) {
            Intent in_navOut = new Intent(HomeActivity.this, ShowRouteActivity.class);
            startActivity(in_navOut);
        } else if (id == R.id.nav_take_stock) {
            Intent in_navOut = new Intent(HomeActivity.this, TakeStockActivity.class);
            startActivity(in_navOut);
        } else if (id == R.id.nav_take_order) {

            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_LIST_PREF, MODE_PRIVATE);
            AttendanceReasonBean attendanceReasonBean = complexPreferences.getObject(Constant.ATTENDANCE_LIST_OBJ, AttendanceReasonBean.class);

            attendanceReason = attendanceReasonBean.getAttendanceReason();
            attendanceTypeStatus = attendanceReasonBean.getAttendanceType();

            if (attendanceTypeStatus != null && attendanceTypeStatus.equalsIgnoreCase("F")) {

                Intent in_navOut = new Intent(HomeActivity.this, TakeOrderActivity.class);
                in_navOut.putExtra("from", "HomeActivity");
                startActivity(in_navOut);

            } else if (attendanceTypeStatus != null && attendanceTypeStatus.equalsIgnoreCase("O")) {

                if (attendanceReason != null && attendanceReason.equalsIgnoreCase("Institutional / Work From Home")) {
                    Intent in_navOut = new Intent(HomeActivity.this, TakeOrderActivity.class);
                    in_navOut.putExtra("from", "HomeActivity");
                    startActivity(in_navOut);
                } else {
                    Toast.makeText(HomeActivity.this, "You Can't take order as you have marked attendance Other Work as '" + Objects.requireNonNull(attendanceReason).toUpperCase() + "'", Toast.LENGTH_SHORT).show();
                }

            } else if (attendanceTypeStatus != null && attendanceTypeStatus.equalsIgnoreCase("L")) {

                Toast.makeText(HomeActivity.this, "You Can't take order as you have marked attendance as 'LEAVE'", Toast.LENGTH_SHORT).show();

            } else if (attendanceTypeStatus != null && attendanceTypeStatus.equalsIgnoreCase("Off")) {

                Toast.makeText(HomeActivity.this, "You Can't take order as you have marked attendance as 'DAY OFF'", Toast.LENGTH_SHORT).show();

            } else {

                if (db.getAttendance() != null && db.getAttendance().size() > 0) {
                    ArrayList<String> datesInAttendanceList = new ArrayList<>();
                    String attendanceType = "";
                    String attendanceTypeId = "";

                    for (int i = 0; i < db.getAttendance().size(); i++) {
                        datesInAttendanceList.add(db.getAttendance().get(i).getAttendanceDate());
                        if (db.getAttendance().get(i).getAttendanceDate().equalsIgnoreCase(currentDate())) {
                            attendanceType = db.getAttendance().get(i).getAttendanceType();
                            attendanceTypeId = db.getAttendance().get(i).getAttendanceTypeId();
                        }
                    }
                    if (!datesInAttendanceList.contains(currentDate())) {
                        Toast.makeText(HomeActivity.this, "You Can't take order as you have Not marked attendance", Toast.LENGTH_SHORT).show();
                    } else if (attendanceType != null && !attendanceType.equalsIgnoreCase("") && attendanceType.equalsIgnoreCase("F")) {
                        Intent in_navOut = new Intent(HomeActivity.this, TakeOrderActivity.class);
                        in_navOut.putExtra("from", "HomeActivity");
                        startActivity(in_navOut);
                    } else if (attendanceType != null && !attendanceType.equalsIgnoreCase("") && attendanceType.equalsIgnoreCase("O")) {
                        if (getAttendanceReasonDataByTypeId(attendanceTypeId) != null && !attendanceTypeId.equalsIgnoreCase("") && getAttendanceReasonDataByTypeId(attendanceTypeId).get(0).getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                            Intent in_navOut = new Intent(HomeActivity.this, TakeOrderActivity.class);
                            in_navOut.putExtra("from", "HomeActivity");
                            startActivity(in_navOut);
                        } else {
                            Toast.makeText(HomeActivity.this, "You Can't take order as you have marked attendance Other Work as '" + Objects.requireNonNull(attendanceReason).toUpperCase() + "'", Toast.LENGTH_SHORT).show();
                        }
                    } else if (attendanceType != null && !attendanceType.equalsIgnoreCase("") && attendanceType.equalsIgnoreCase("L")) {
                        Toast.makeText(HomeActivity.this, "You Can't take order as you have marked attendance as 'LEAVE'", Toast.LENGTH_SHORT).show();

                    } else if (attendanceType != null && !attendanceType.equalsIgnoreCase("") && attendanceType.equalsIgnoreCase("Off")) {
                        Toast.makeText(HomeActivity.this, "You Can't take order as you have marked attendance as 'DAY OFF'", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "You Can't take order as you have Not marked attendance", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (id == R.id.nav_place_order) {
//          Intent in_navOut = new Intent(HomeActivity.this, ShowPlaceOrder.class);
            Intent in_navOut = new Intent(HomeActivity.this, PlaceOrderListActivity.class);
            startActivity(in_navOut);
        } else if (id == R.id.nav_report) {
            Intent in_navOut = new Intent(HomeActivity.this, ReportGridActivity.class);
            startActivity(in_navOut);
        } else if (id == R.id.nav_daily_report) {
            isDailyReportClicked = true;
            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_LIST_PREF, MODE_PRIVATE);
            AttendanceReasonBean attendanceReasonBean = complexPreferences.getObject(Constant.ATTENDANCE_LIST_OBJ, AttendanceReasonBean.class);
            attendanceReason = attendanceReasonBean.getAttendanceReason();
            attendanceTypeStatus = attendanceReasonBean.getAttendanceType();

            if (attendanceTypeStatus.equals("")) {
                Toast.makeText(getApplicationContext(), "is Empty", Toast.LENGTH_LONG).show();
            }

            if (attendanceTypeStatus != null && attendanceTypeStatus.equalsIgnoreCase("F")) {
                Intent in_navOut = new Intent(HomeActivity.this, PlaceOrderToDistributor.class);
                startActivity(in_navOut);

            } else if (attendanceTypeStatus != null && attendanceTypeStatus.equalsIgnoreCase("O")) {
                if (attendanceReason != null && attendanceReason.equalsIgnoreCase("Institutional / Work From Home")) {
                    Intent in_navOut = new Intent(HomeActivity.this, PlaceOrderToDistributor.class);
                    startActivity(in_navOut);
                } else {
                    assert attendanceReason != null;
                    if (!mUserLoginInfoBean.getSalesType().equals("MT")) {
                        Toast.makeText(HomeActivity.this, "You Can't Send DSR as you have marked attendance Other Work as '"
                                + attendanceReason.toUpperCase() + "'", Toast.LENGTH_SHORT).show();
                    }
                    if (Utils.isInternetConnected(this)) {
                        if (session.getUserDetails().get(session.getKEY_ClockOut()) == null) {
                            Intent in_navOut = new Intent(HomeActivity.this, ClockInOut.class);
                            startActivity(in_navOut);
                        }
                    }

                }
            } else if (attendanceTypeStatus != null && attendanceTypeStatus.equalsIgnoreCase("L") && !mUserLoginInfoBean.getSalesType().equals("MT")) {
                Toast.makeText(HomeActivity.this, "You Can't Send DSR as you have marked 'LEAVE'", Toast.LENGTH_SHORT).show();

            } else if (attendanceTypeStatus != null && attendanceTypeStatus.equalsIgnoreCase("Off") && !mUserLoginInfoBean.getSalesType().equals("MT")) {
                Toast.makeText(HomeActivity.this, "You Can't Send DSR as you have marked attendance as 'DAY OFF'", Toast.LENGTH_SHORT).show();

            } else {
                if (db.getAttendance() != null && db.getAttendance().size() > 0) {
                    ArrayList<String> datesInAttendanceList = new ArrayList<>();
                    String attendanceType = "";
                    String attendanceTypeId = "";

                    for (int i = 0; i < db.getAttendance().size(); i++) {
                        datesInAttendanceList.add(db.getAttendance().get(i).getAttendanceDate());
                        if (db.getAttendance().get(i).getAttendanceDate().equalsIgnoreCase(currentDate())) {
                            attendanceType = db.getAttendance().get(i).getAttendanceType();
                            attendanceTypeId = db.getAttendance().get(i).getAttendanceTypeId();
                        }
                    }
                    if (!datesInAttendanceList.contains(currentDate()) && !mUserLoginInfoBean.getSalesType().equals("MT")) {
                        Toast.makeText(HomeActivity.this, "You Can't Send DSR as you have Not marked attendance", Toast.LENGTH_SHORT).show();
                    } else if (attendanceType != null && !attendanceType.equalsIgnoreCase("") && attendanceType.equalsIgnoreCase("F") && !mUserLoginInfoBean.getSalesType().equals("MT")) {
                        Toast.makeText(HomeActivity.this, "You Can't Send DSR ,Please Sync Attendance Data", Toast.LENGTH_SHORT).show();

                       /* Intent in_navOut = new Intent(HomeActivity.this, PlaceOrderToDistributor.class);
                        startActivity(in_navOut);*/
                    } else if (attendanceType != null && !attendanceType.equalsIgnoreCase("") && attendanceType.equalsIgnoreCase("O")) {
                        getAttendanceReasonDataByTypeId(attendanceTypeId);
                        if (!attendanceTypeId.equalsIgnoreCase("") && getAttendanceReasonDataByTypeId(attendanceTypeId).get(0).getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home") && !mUserLoginInfoBean.getSalesType().equals("MT")) {
                         /*   Intent in_navOut = new Intent(HomeActivity.this, PlaceOrderToDistributor.class);
                            startActivity(in_navOut);*/
                            Toast.makeText(HomeActivity.this, "You Can't Send DSR , Please Sync Attendance Data", Toast.LENGTH_SHORT).show();

                        } else {
                            assert attendanceReason != null;
                            if (!mUserLoginInfoBean.getSalesType().equals("MT")) {
                                Toast.makeText(HomeActivity.this, "You Can't Send DSR as you have marked attendance Other Work as '"
                                        + getAttendanceReasonDataByTypeId(attendanceTypeId).get(0).getAttendanceReason().toUpperCase() + "'", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (attendanceType != null && !attendanceType.equalsIgnoreCase("") && attendanceType.equalsIgnoreCase("L") && !mUserLoginInfoBean.getSalesType().equals("MT")) {
                        Toast.makeText(HomeActivity.this, "You Can't Send DSR as you have marked 'LEAVE'", Toast.LENGTH_SHORT).show();

                    } else if (attendanceType != null && !attendanceType.equalsIgnoreCase("") && attendanceType.equalsIgnoreCase("Off") && !mUserLoginInfoBean.getSalesType().equals("MT")) {
                        Toast.makeText(HomeActivity.this, "You Can't Send DSR as you have marked attendance as 'DAY OFF'", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(HomeActivity.this, "You Can't Send DSR as you have Not marked attendance", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (id == R.id.nav_broadcast) {
            Intent in_navOut = new Intent(HomeActivity.this, BroadcastActivity.class);
            startActivity(in_navOut);
        } else if (id == R.id.nav_activity) {
            Intent in_navOut = new Intent(HomeActivity.this, ActivitySR.class);
            startActivity(in_navOut);
        } else if (id == R.id.nav_holiday_calendar) {
            Intent in_navOut = new Intent(HomeActivity.this, HolidayCalendar.class);
            startActivity(in_navOut);
        } else if (id == R.id.nav_expense_management) {
            Intent in_navOut = new Intent(HomeActivity.this, ExpenseManagement.class);
            startActivity(in_navOut);
        } else if (id == R.id.nav_salary_slip) {
            Intent in_navOut = new Intent(HomeActivity.this, SalarySlipPreview.class);
            startActivity(in_navOut);
        } else if (id == R.id.nav_catalogue) {
            Intent in_navOut = new Intent(HomeActivity.this, Catalogue.class);
            startActivity(in_navOut);
        }else if (id == R.id.nav_tour_plan) {
            Intent in_navOut = new Intent(HomeActivity.this, TourPlan.class);
            startActivity(in_navOut);
        }

    /*else if (id =
    = R.id.nav_target) {
        Intent in_navOut = new Intent(HomeActivity.this, TargetActivity.class);
        startActivity(in_navOut);
    }else if (id == R.id.nav_avg_line_cut) {
        Intent in_navOut = new Intent(HomeActivity.this, AverageLineCutActivity.class);
        startActivity(in_navOut);
    }else if (id == R.id.nav_avg_bill_value) {
        Intent in_navOut = new Intent(HomeActivity.this, AverageBillReport.class);
        startActivity(in_navOut);
    }else if (id == R.id.nav_unique_bill_outlet_report) {
        Intent in_navOut = new Intent(HomeActivity.this, UniqueBillOutletReport.class);
        startActivity(in_navOut);
    }else if (id == R.id.nav_productivity_report) {
        Intent in_navOut = new Intent(HomeActivity.this, ProductivityReport.class);
        startActivity(in_navOut);
    }*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getMyLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void getMyLastLocation(GoogleApiClient mGoogleApiClient) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Constant.mGoogleApiClient = mGoogleApiClient;
        double latitude = mLastLocation.getLatitude();
        double longitude = mLastLocation.getLongitude();
        Constant.LATITUDE = latitude;
        Constant.LONGITUDE = longitude;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            Constant.ADDRESS = address + ", " + city + ", " + state;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
        GPSTracker gps = new GPSTracker(this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            String latitude = "" + gps.getLatitude();
            String longitude = "" + gps.getLongitude();

        } else {
            gps.showSettingsAlert();
        }
        //Log.v("first", "resume");

        if (returnFromDSR) {
            returnFromDSR = false;
            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_LIST_PREF, MODE_PRIVATE);
            AttendanceReasonBean attendanceReasonBean = complexPreferences.getObject(Constant.ATTENDANCE_LIST_OBJ, AttendanceReasonBean.class);
            attendanceReason = attendanceReasonBean.getAttendanceReason();
            attendanceTypeStatus = attendanceReasonBean.getAttendanceType();
            if (isUnApprove) {
                //unapprove means update the record for a particular rowid if a manager disapproves therecord then add new attendance
                isUnApprove = false;
                if (attendanceReason != null && attendanceReason.equalsIgnoreCase("Institutional / Work From Home")) {
                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);
                } else if (attendanceReason != null && attendanceReason.equalsIgnoreCase("Joint Work")) {
                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId, JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                } else {
                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), currentDate(), getAttendanceReasonData("F").get(0).getAttendanceId(), rowId);
                }

            } else {
                SharedPreferences prefs = getSharedPreferences(Constant.Update_DSR_PREF, MODE_PRIVATE);
                boolean update_dsr = prefs.getBoolean(Constant.Update_DSR_OBJ, false);

                if (update_dsr) {
                    SharedPreferences sharedpreferences = getSharedPreferences(Constant.Update_DSR_PREF, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(Constant.Update_DSR_OBJ, false);
                    editor.apply();
                    ComplexPreferences complexPreferences1 = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.PREVIOUS_LIST_PREF, MODE_PRIVATE);
                    String previousDateAttendance = complexPreferences1.getObject(Constant.PREVIOUS_LIST_OBJ, String.class);
                    if (previousDateAttendance != null)
                        dateToAddAttendance = previousDateAttendance;

                    if (isUnApprove) {
                        isUnApprove = false;
                        if (isFromTab.equalsIgnoreCase("Field Work")) {

                            if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                if (db.getAttendanceDate(offlineDateAdd).size() > 0) {

                                    if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                        if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("F"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                            db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                            if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("F")) {
                                                offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                isFromTab = "Field Work";
                                                new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                            }
                                        }
                                    } else {
                                        if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), offlineDateAdd, getAttendanceReasonData("F").get(0).getAttendanceId(), db.getAttendancerowid());
                                        }
                                    }
                                } else {
                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), currentDate(), getAttendanceReasonData("F").get(0).getAttendanceId(), rowId);
                                }
                            } else {
                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), currentDate(), getAttendanceReasonData("F").get(0).getAttendanceId(), rowId);
                            }
                        } else if (isFromTab.equalsIgnoreCase("Other Work")) {

                            if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                if (db.getAttendanceDate(offlineDateAdd).size() > 0) {
                                    if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                        if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                            if (db.getAttendance().get(0).getAttendanceReason() != null && db.getAttendance().get(0).getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid(), db.getAttendance().get(0).getJointUserID(), db.getAttendance().get(0).getJointUserDesig());
                                            } else {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                            }
                                            db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                            if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("O")) {
                                                offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                isFromTab = "Other Work";
                                                new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                            }
                                        }
                                    } else {
                                        if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                            if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());
                                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());
                                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId, JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                            } else {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());
                                            }
                                        }
                                    }
                                } else {
                                    if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                    } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                    } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId, JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                    } else {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                    }
                                }
                            } else {
                                if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId, JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                } else {
                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                }
                            }
                        } else if (isFromTab.equalsIgnoreCase("Leave")) {
                            if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                if (db.getAttendanceDate(offlineDateAdd).size() > 0) {

                                    if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                        if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("L"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                            db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                            if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("L")) {
                                                offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                isFromTab = "Leave";
                                                new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                            }
                                        }
                                    } else {
                                        if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {

                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), offlineDateAdd, getAttendanceReasonData("L").get(0).getAttendanceId(), db.getAttendancerowid());
                                        }
                                    }
                                } else {
                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), currentDate(), getAttendanceReasonData("L").get(0).getAttendanceId(), rowId);

                                }
                            } else {
                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), currentDate(), getAttendanceReasonData("L").get(0).getAttendanceId(), rowId);

                            }


                        } else if (isFromTab.equalsIgnoreCase("Off")) {
                            if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                if (db.getAttendanceDate(offlineDateAdd).size() > 0) {

                                    if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                        if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("Off"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                            db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                            if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("Off")) {
                                                offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                isFromTab = "Off";
                                                new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                            }
                                        }
                                    } else {
                                        if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {

                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());
                                        }
                                    }
                                } else {
                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), currentDate(), institutionalArray.getAttendanceId(), rowId);

                                }
                            } else {
                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), currentDate(), institutionalArray.getAttendanceId(), rowId);
                            }
                        }
                    } else {
                        if (isFromTab.equalsIgnoreCase("Field Work")) {
                            if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                if (db.getAttendanceDate(offlineDateAdd).size() > 0) {
                                    if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("F"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId());
                                        db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());

                                        if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("F")) {
                                            offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                            isFromTab = "Field Work";
                                            new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                        }
                                    }
                                } else {
                                    if (isPreviousDayAvailable) {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), dateToAddAttendance, getAttendanceReasonData("F").get(0).getAttendanceId());

                                    } else {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), currentDate(), getAttendanceReasonData("F").get(0).getAttendanceId());

                                    }
                                }
                            } else {
                                if (isPreviousDayAvailable) {
                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), dateToAddAttendance, getAttendanceReasonData("F").get(0).getAttendanceId());

                                } else {
                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), currentDate(), getAttendanceReasonData("F").get(0).getAttendanceId());

                                }
                            }
                        } else if (isFromTab.equalsIgnoreCase("Other Work")) {
                            if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                if (db.getAttendanceDate(offlineDateAdd).size() > 0) {
/*
                                            if (getAttendanceReasonDataByTypeId(db.getAttendance().get(0).getAttendanceTypeId()) != null && getAttendanceReasonDataByTypeId(db.getAttendance().get(0).getAttendanceTypeId()).get(0).getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
*/
                                    if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                        if (db.getAttendance().get(0).getAttendanceReason() != null && db.getAttendance().get(0).getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendance().get(0).getJointUserID(), db.getAttendance().get(0).getJointUserDesig());
                                        } else {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId());
                                        }/* } else {
                                                new markAttendance().execute(mUserLoginInfoBean.getUser_id(), db.getAttendance().get(0).getAttendanceType(), "0", getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId());
                                            }*/
                                        // new markAttendance().execute(mUserLoginInfoBean.getUser_id(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), offlineDateAdd, getAttendanceReasonData("L").get(0).getAttendanceId());
                                        db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());

                                        if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("O")) {
                                            offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                            isFromTab = "Other Work";
                                            new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                        }
                                    }

                                } else {
                                    if (isPreviousDayAvailable) {
                                        if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), dateToAddAttendance, institutionalArray.getAttendanceId());
                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId(), JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                        } else {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                                        }

                                    } else {

                                        if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId());

                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId());

                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                        } else {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId());

                                        }


                                    }
                                }
                            } else {
                                if (isPreviousDayAvailable) {
                                    if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                                    } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), dateToAddAttendance, institutionalArray.getAttendanceId());
                                    } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId(), JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                    } else {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                                    }

                                } else {

                                    if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId());

                                    } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId());

                                    } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                    } else {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId());

                                    }


                                }

                            }
                        } else if (isFromTab.equalsIgnoreCase("Leave")) {
                            if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                if (db.getAttendanceDate(offlineDateAdd).size() > 0) {
                                    if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("L"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId());
                                        db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());

                                        if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("L")) {
                                            offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                            isFromTab = "Leave";
                                            new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                        }
                                    }
                                } else {
                                    if (isPreviousDayAvailable) {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), dateToAddAttendance, getAttendanceReasonData("L").get(0).getAttendanceId());

                                    } else {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), currentDate(), getAttendanceReasonData("L").get(0).getAttendanceId());
                                    }
                                }
                            } else {
                                if (isPreviousDayAvailable) {
                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), dateToAddAttendance, getAttendanceReasonData("L").get(0).getAttendanceId());

                                } else {
                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), currentDate(), getAttendanceReasonData("L").get(0).getAttendanceId());
                                }
                            }
                        } else if (isFromTab.equalsIgnoreCase("Off")) {
                            if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                if (db.getAttendanceDate(offlineDateAdd).size() > 0) {
                                    if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("Off"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId());
                                        db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());

                                        if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("Off")) {
                                            offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                            isFromTab = "Off";
                                            new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                        }
                                    }
                                } else {
                                    if (isPreviousDayAvailable) {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), dateToAddAttendance, institutionalArray.getAttendanceId());

                                    } else {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), currentDate(), institutionalArray.getAttendanceId());
                                    }
                                }
                            } else {
                                if (isPreviousDayAvailable) {
                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), dateToAddAttendance, institutionalArray.getAttendanceId());

                                } else {
                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), currentDate(), institutionalArray.getAttendanceId());
                                }
                            }
                        }
                    }
                            /*if (isPreviousDayAvailable) {
                                new markAttendance().execute(mUserLoginInfoBean.getUser_id(), getAttendanceReasonData("Off").get(0).getAttendanceType(), "0", getAttendance("Off"), dateToAddAttendance, getAttendanceReasonData("Off").get(0).getAttendanceId());

                            } else {
                                new markAttendance().execute(mUserLoginInfoBean.getUser_id(), getAttendanceReasonData("Off").get(0).getAttendanceType(), "0", getAttendance("Off"), currentDate(), getAttendanceReasonData("Off").get(0).getAttendanceId());
                            }
                        }*/

                } else {
                    Toast.makeText(HomeActivity.this, "You have not submitted End day Report", Toast.LENGTH_SHORT).show();
                    if (Utils.isInternetConnected(HomeActivity.this)) {
                        new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUserId(), currentDate());

                    } else {

                        String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                        //If current day false but if its last date is already aded to sync then need both dialog and sync


                        if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {

                            final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                            if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                llSync.setVisibility(View.VISIBLE);
                                ArrayList<String> datesInAttendanceList = new ArrayList<>();

                                for (int i = 0; i < attendanceBeans.size(); i++) {
                                    datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                                }
                                if (!datesInAttendanceList.contains(currentDate())) {
                                    attendanceReasonBeanArrayList = new ArrayList<>();

                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                                    dialogAttendance();

                                } else {
                                    db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                    db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                                }
                            } else {
                                llSync.setVisibility(View.GONE);
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                            }

                        } else {

                            final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                            if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("false")) {

                                if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                    llSync.setVisibility(View.VISIBLE);
                                    ArrayList<String> datesInAttendanceList = new ArrayList<>();

                                    for (int i = 0; i < attendanceBeans.size(); i++) {
                                        datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                                    }
                                    if (!datesInAttendanceList.contains(currentDate())) {
                                        attendanceReasonBeanArrayList = new ArrayList<>();

                                        attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                        dialogAttendance();
                                    } else {
                                        db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                        db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                                    }
                                } else {
                                    llSync.setVisibility(View.GONE);
                                    attendanceReasonBeanArrayList = new ArrayList<>();

                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                    dialogAttendance();
                                }


                            } else {

                                if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                    llSync.setVisibility(View.VISIBLE);
                                    ArrayList<String> datesInAttendanceList = new ArrayList<>();

                                    for (int i = 0; i < attendanceBeans.size(); i++) {
                                        datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                                    }
                                    if (!datesInAttendanceList.contains(currentDate())) {
                                        attendanceReasonBeanArrayList = new ArrayList<>();

                                        attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                        dialogAttendance();
                                    }
                                } else {
                                    llSync.setVisibility(View.GONE);

                                }
                            }
                        }
                    }

                }

            }
        } else {

            if (Utils.isInternetConnected(HomeActivity.this)) {

                if (!isFromPause) {
                    if (!isFromProgressView) {
                        db.deleteAllAttendanceStatusRecords(mUserLoginInfoBean.getUserId());
                        new getAttendanceStatus().execute();
                    } else {
                        isFromProgressView = false;
                    }

                } else {
                    if (isDailyReportClicked) {
                        isFromPause = false;
                        isDailyReportClicked = false;
                        db.deleteAllAttendanceStatusRecords(mUserLoginInfoBean.getUserId());
                        new getAttendanceStatus().execute();
                    } else {
                        isFromPause = false;
                    }

                }

            } else {

                AttendanceReasonBean attendanceReasonBean = db.getAttendanceStatusHome(currentDate(), mUserLoginInfoBean.getUserId());
                ComplexPreferences attendancePref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_LIST_PREF, MODE_PRIVATE);
                attendancePref.putObject(Constant.ATTENDANCE_LIST_OBJ, attendanceReasonBean);
                attendancePref.commit();
                if (attendanceReasonBean.getAttendanceMessage() != null && !attendanceReasonBean.getAttendanceMessage().equalsIgnoreCase("")) {

                    //tvUpdateAttendanceStatus.setVisibility(View.VISIBLE);
                    tvUpdateAttendanceStatus.setText(attendanceReasonBean.getAttendanceMessage());

                } else {

                    tvUpdateAttendanceStatus.setVisibility(View.GONE);

                }
            }

            if (Utils.isInternetConnected(HomeActivity.this)) {
                if (!isFromProgressView) {
                    db.deleteAllFocusedRecords(mUserLoginInfoBean.getUserId());
                    new focusedProduct().execute(String.valueOf(month()), String.valueOf(year()));
                } else {
                    isFromProgressView = false;
                }
            } else {
                String arrayFocusedResult = "";
                ArrayList<FocusedProductBean> focusedProductNameArray = db.getFocusedProduct(String.valueOf(month()), String.valueOf(year()), mUserLoginInfoBean.getUserId());
                if (focusedProductNameArray != null && focusedProductNameArray.size() > 0) {
                    for (int i = 0; i < focusedProductNameArray.size(); i++) {

                        if (i == focusedProductNameArray.size() - 1) {
                            arrayFocusedResult = arrayFocusedResult.concat(focusedProductNameArray.get(i).getProductName());
                        } else {
                            arrayFocusedResult = arrayFocusedResult.concat(focusedProductNameArray.get(i).getProductName() + " , ");
                        }
                    }
                    tvFocused.setText("Focus Product Of the Month - " + arrayFocusedResult);
                } else {
                    // tvFocused.setText("No Focus Product Of the Month                   \t\t\t\t\t\t\t\t\t\t\t        ");
                    tvFocused.setText("Please Clock-In & Clock-Out Timely to mark Your Attendance.                  \t\t\t\t\t\t\t\t\t\t\t        ");
                }
                tvFocused.setSelected(true);
            }
        }
    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("first", "activity");
        switch (requestCode) {
            case STATIC_INTEGER_VALUE: {
                if (resultCode == Activity.RESULT_OK) {
                    update_dsr = data.getBooleanExtra("dsr_update", false);
                    //
                    Log.v("first", update_dsr + "");
                }
                break;
            }
        }
    }*/

    // Fetches reg id from shared preferences
    // and displays on the screen

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);


        if (!TextUtils.isEmpty(regId))
            //  Toast.makeText(getApplicationContext(), "Firebase Reg Id: " + regId, Toast.LENGTH_LONG).show();
            Log.e("Firebase Reg Id:", regId);
        else
            // Toast.makeText(getApplicationContext(), "Firebase Reg Id is not received yet! ", Toast.LENGTH_LONG).show();
            Log.e("Firebase Reg Id:", "Firebase Reg Id is not received yet! ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isFromPause = true;
    }

    private void dialogAttendance() {

        Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.attendance_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        final TextView tvAttendanceDate = dialog.findViewById(R.id.tv_attendance_date);
        Button btnFieldWork = dialog.findViewById(R.id.btn_field_work);
        Button btnOtherWork = dialog.findViewById(R.id.btn_other_work);
        Button btnLeaveWork = dialog.findViewById(R.id.btn_leave);
        Button btnOff = dialog.findViewById(R.id.btn_off_day);
        // dialog.show();

        if (mUserLoginInfoBean.getSalesType().equals("MT")) {
            btnFieldWork.setVisibility(View.GONE);
            btnOtherWork.setText("Work Type");
        }
        if (Utils.isInternetConnected(HomeActivity.this)) {

            BackgroundWork back = new BackgroundWork(this);

            back.execute("Admin/Other Work Eligibility");
            back.getDailog().setOnDismissListener(it -> {
                count = new HashMap();
                if (!back.getResult().isEmpty()) {
                    try {
                        JSONObject json;
                        json = new JSONObject(back.getResult());
                        JSONArray jsonArray = json.optJSONArray("response");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject j_obj = jsonArray.getJSONObject(i);
                            count.put(j_obj.getString("Type"), j_obj.getInt("Count"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dialog.show();
            });

            if (isPreviousDayAvailable) {
                tvAttendanceDate.setText(convertDate(dateToAddAttendance));
            } else {
                tvAttendanceDate.setText(convertDate(currentDate()));
                dateToAddAttendance = currentDate();
            }
        } else {

            dialog.show();
            tvAttendanceDate.setText(convertDate(currentDate()));

        }

        btnFieldWork.setOnClickListener(v -> {
            if (Utils.isInternetConnected(HomeActivity.this)) {
                dialog.dismiss();
                dialogFieldWork();

            } else {
                if (db.getAttendanceDate(currentDate()).size() == 0) {
                    dialogFieldWork();

                } else {
                    ArrayList<String> datesInAttendanceList = new ArrayList<>();

                    for (int i = 0; i < db.getAttendance().size(); i++) {
                        datesInAttendanceList.add(db.getAttendance().get(i).getAttendanceDate());
                    }
                    if (!datesInAttendanceList.contains(currentDate())) {
                        dialogFieldWork();

                    } else {
                        Toast.makeText(HomeActivity.this, "Data is already present to SYNC", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            }

        });

        btnOtherWork.setOnClickListener(v -> {
            if (Utils.isInternetConnected(HomeActivity.this)) {
                dialogOtherWork();
            } else {
                if (db.getAttendanceDate(currentDate()).size() == 0) {
                    dialogOtherWork();
                } else {
                    ArrayList<String> datesInAttendanceList = new ArrayList<>();

                    for (int i = 0; i < db.getAttendance().size(); i++) {
                        datesInAttendanceList.add(db.getAttendance().get(i).getAttendanceDate());
                    }
                    if (!datesInAttendanceList.contains(currentDate())) {
                        dialogOtherWork();
                    } else {
                        Toast.makeText(HomeActivity.this, "Data is already present to SYNC", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            dialog.dismiss();


        });

        btnOff.setOnClickListener(v -> {
            if (Utils.isInternetConnected(HomeActivity.this)) {
                dialogDayOff();
            } else {

                if (db.getAttendanceDate(currentDate()).size() == 0) {
                    dialogDayOff();
                } else {
                    ArrayList<String> datesInAttendanceList = new ArrayList<>();

                    for (int i = 0; i < db.getAttendance().size(); i++) {
                        datesInAttendanceList.add(db.getAttendance().get(i).getAttendanceDate());
                    }
                    if (!datesInAttendanceList.contains(currentDate())) {
                        dialogDayOff();

                    } else {
                        Toast.makeText(HomeActivity.this, "Data is already present to SYNC", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            dialog.dismiss();
        });

        btnLeaveWork.setOnClickListener(v -> {
            if (Utils.isInternetConnected(HomeActivity.this)) {
                dialogLeaveConfirm();
                dialog.dismiss();
            } else {
                if (db.getAttendanceDate(currentDate()).size() == 0) {
                    dialogLeaveConfirm();
                } else {
                    ArrayList<String> datesInAttendanceList = new ArrayList<>();

                    for (int i = 0; i < db.getAttendance().size(); i++) {
                        datesInAttendanceList.add(db.getAttendance().get(i).getAttendanceDate());
                    }
                    if (!datesInAttendanceList.contains(currentDate())) {
                        dialogLeaveConfirm();

                    } else {
                        Toast.makeText(HomeActivity.this, "Data is already present to SYNC", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            }
        });

    }

    private ArrayList<AttendanceReasonBean> getAttendanceReasonDataByTypeId(String attendanceTypeId) {
        ArrayList<AttendanceReasonBean> attendanceReasonBeanList = new ArrayList<>();

        if (attendanceReasonBeanArrayList != null) {
            if (attendanceReasonBeanArrayList.size() > 0) {

                for (int i = 0; i < attendanceReasonBeanArrayList.size(); i++) {
                    if (attendanceReasonBeanArrayList.get(i).getAttendanceId().equalsIgnoreCase(attendanceTypeId)) {
                        AttendanceReasonBean attendanceReasonBean = new AttendanceReasonBean(attendanceReasonBeanArrayList.get(i).getAttendanceId(), attendanceReasonBeanArrayList.get(i).attendanceReason, attendanceReasonBeanArrayList.get(i).attendanceType);
                        attendanceReasonBeanList.add(attendanceReasonBean);
                    }

                }
            }
        }

        return attendanceReasonBeanList;

    }

    private ArrayList<AttendanceReasonBean> getAttendanceReasonData(String attendanceType) {
        ArrayList<AttendanceReasonBean> attendanceReasonBeanList = new ArrayList<>();

        if (attendanceReasonBeanArrayList != null) {
            if (attendanceReasonBeanArrayList.size() > 0) {

                for (int i = 0; i < attendanceReasonBeanArrayList.size(); i++) {
                    if (attendanceReasonBeanArrayList.get(i).getAttendanceType().equalsIgnoreCase(attendanceType)) {

                        AttendanceReasonBean attendanceReasonBean = new AttendanceReasonBean(attendanceReasonBeanArrayList.get(i).getAttendanceId(), attendanceReasonBeanArrayList.get(i).attendanceReason, attendanceReasonBeanArrayList.get(i).attendanceType);
                        attendanceReasonBeanList.add(attendanceReasonBean);

                    }

                }
            }
        }
        return attendanceReasonBeanList;
    }

    private void dialogFieldWork() {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmation_dialog);
        // dialog.setCanceledOnTouchOutside(false);
        /* dialog.setOnCancelListener(this);*/
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        final TextView tvTool = dialog.findViewById(R.id.tv_tool);
        final TextView tvMessage = dialog.findViewById(R.id.tv_message);
        final Button btnConfirm = dialog.findViewById(R.id.confirm);
        final Button btnCancel = dialog.findViewById(R.id.cancel);

        dialog.show();
        btnConfirm.setText(R.string.confirm);

        tvTool.setText(R.string.confrimvalue);
        String text = "You have Selected Field Work. Do you want to Confirm?";
        tvMessage.setText(text);


        btnConfirm.setOnClickListener(v -> {

            isFromTab = "Field Work";
            if (Utils.isInternetConnected(HomeActivity.this)) {
                ComplexPreferences complexPreferences1 = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.PREVIOUS_LIST_PREF, MODE_PRIVATE);
                String previousDateAttendance = complexPreferences1.getObject(Constant.PREVIOUS_LIST_OBJ, String.class);

                if (previousDateAttendance != null)
                    dateToAddAttendance = previousDateAttendance;

                if (isPreviousDayAvailable) {

                    if (dateToAddAttendance != null && !dateToAddAttendance.equalsIgnoreCase("")) {
                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), dateToAddAttendance);
                    } else {
                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), currentDate());
                    }

                } else {

                    if (db.getAttendance().size() != 0) {

                        if (db.getAttendanceDate(currentDate()).size() == 0) {

                            if (attendanceReasonBeanArrayList != null && attendanceReasonBeanArrayList.size() > 0) {

                                db.addAttendance(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType()
                                        , "0", getAttendance("F"), currentDate(),
                                        getAttendanceReasonData("F").get(0).getAttendanceId(), 0, 0);
                                db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                                Toast.makeText(HomeActivity.this, "Internet Not Available ,Attendance Marked for date " + currentDate(), Toast.LENGTH_SHORT).show();

                                //new markAttendance().execute(mUserLoginInfoBean.getUser_id(), getAttendanceReasonData("Off").get(0).getAttendanceType(), "0", getAttendance("Off"), currentDate(), getAttendanceReasonData("Off").get(0).getAttendanceId());

                                final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                                if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                    llSync.setVisibility(View.VISIBLE);
                                } else {
                                    llSync.setVisibility(View.GONE);
                                }

                            }

                        } else {
                            Toast.makeText(HomeActivity.this, "Data is already present to SYNC", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        if (dateToAddAttendance != null && !dateToAddAttendance.equalsIgnoreCase("")) {
                            new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), dateToAddAttendance);
                        } else {
                            if (dateToAddAttendance != null) {
                                new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), currentDate());
                            } else {
                                new getAttendanceStatus().execute();
                                Toast.makeText(HomeActivity.this, "Data is getting refreshed ,as you have not refreshed your application .Please Mark Attendance Now", Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                }
            } else {
                if (db.getAttendanceDate(currentDate()).size() == 0) {
                    if (attendanceReasonBeanArrayList != null && attendanceReasonBeanArrayList.size() > 0) {
                        db.addAttendance(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(),
                                "0", getAttendance("F"), currentDate(),
                                getAttendanceReasonData("F").get(0).getAttendanceId(), 0, 0);
                        db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                        db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());
                        Toast.makeText(HomeActivity.this, "Internet Not Available ,Attendance Marked for date " + currentDate(), Toast.LENGTH_SHORT).show();

                        //new markAttendance().execute(mUserLoginInfoBean.getUser_id(), getAttendanceReasonData("Off").get(0).getAttendanceType(), "0", getAttendance("Off"), currentDate(), getAttendanceReasonData("Off").get(0).getAttendanceId());
                        final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);

                        } else {
                            llSync.setVisibility(View.GONE);
                        }
                    } else {
                        if (db.getAttendanceReasonList().size() > 0) {
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                        } else {

                            Type reasonType = new TypeToken<ArrayList<AttendanceReasonBean>>() {
                            }.getType();
                            ComplexPreferences reasonListPref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_REASON_LIST_PREF, MODE_PRIVATE);
                            attendanceReasonBeanArrayList = reasonListPref.getArray(Constant.ATTENDANCE_REASON_LIST_OBJ, reasonType);

                        }
                        String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                        if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {
                            db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                            db.addIsTodayAttendanceMarkeds("false", currentDate(), mUserLoginInfoBean.getUserId());

                            if (!db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId()).equalsIgnoreCase("true")) {
                                attendanceReasonBeanArrayList = new ArrayList<>();
                                if (db.getAttendanceReasonList().size() > 0) {
                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                    dialogAttendance();
                                } else {
                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                    dialogAttendance();
                                    Toast.makeText(HomeActivity.this, "You Installed new Application.Please Connect to Internet and refresh application", Toast.LENGTH_LONG).show();


                                }
                            }
                        } else {
                            if (!db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId()).equalsIgnoreCase("true")) {
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                if (db.getAttendanceReasonList().size() > 0) {
                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                    dialogAttendance();
                                } else {
                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                    dialogAttendance();
                                    Toast.makeText(HomeActivity.this, "You Installed new Application.Please Connect to Internet  and refresh application", Toast.LENGTH_LONG).show();

                                }

                            }
                        }

                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Data is already present to SYNC", Toast.LENGTH_SHORT).show();
                }
            }
            dialog.dismiss();
        });
        btnCancel.setOnClickListener(v -> {
            if (Utils.isInternetConnected(HomeActivity.this)) {
                //new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUser_id(), currentDate());


                String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                //If current day false but if its last date is already aded to sync then need both dialog and sync

                if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {

                    final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                    if (attendanceBeans != null && attendanceBeans.size() > 0) {
                        llSync.setVisibility(View.VISIBLE);
                        ArrayList<String> datesInAttendanceList = new ArrayList<>();

                        for (int i = 0; i < attendanceBeans.size(); i++) {
                            datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                        }
                        if (!datesInAttendanceList.contains(currentDate())) {
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        } else {
                            db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                            db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                        }
                    } else {
                        llSync.setVisibility(View.GONE);
                        new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUserId(), currentDate());

                    }

                } else {

                    final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                    if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("false")) {

                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);
                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                            }
                            if (!datesInAttendanceList.contains(currentDate())) {
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                            } else {
                                db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                            }
                        } else {
                            new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUserId(), currentDate());

                            llSync.setVisibility(View.GONE);

                        }


                    } else {

                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);
                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                            }
                            if (!datesInAttendanceList.contains(currentDate())) {
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                            }
                        } else {
                            llSync.setVisibility(View.GONE);

                        }
                    }
                }

            } else {

                String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                //If current day false but if its last date is already aded to sync then need both dialog and sync


                final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {

                    if (attendanceBeans != null && attendanceBeans.size() > 0) {
                        llSync.setVisibility(View.VISIBLE);
                        ArrayList<String> datesInAttendanceList = new ArrayList<>();

                        for (int i = 0; i < attendanceBeans.size(); i++) {
                            datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                        }
                        if (!datesInAttendanceList.contains(currentDate())) {
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        } else {
                            db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                            db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                        }
                    } else {
                        llSync.setVisibility(View.GONE);
                        attendanceReasonBeanArrayList = new ArrayList<>();

                        attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                        dialogAttendance();
                    }

                } else {

                    if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("false")) {

                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);
                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                            }
                            if (!datesInAttendanceList.contains(currentDate())) {
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                            } else {
                                db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                            }
                        } else {
                            llSync.setVisibility(View.GONE);
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        }


                    } else {

                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);
                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                            }

                            if (!datesInAttendanceList.contains(currentDate())) {
                                attendanceReasonBeanArrayList = new ArrayList<>();
                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                                dialogAttendance();
                            }

                        } else {
                            llSync.setVisibility(View.GONE);
                        }
                    }
                }
            }
            dialog.dismiss();
        });

        dialog.setOnDismissListener(dialog1 -> {
            if (Utils.isInternetConnected(HomeActivity.this)) {
                onResume();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void dialogDayOff() {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.other_work_attendance_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(this);
        // dialog.setCancelable(false);
        final ListView lvAttendance = dialog.findViewById(R.id.attendance_listview);
        final TextView tvAttendanceDate = dialog.findViewById(R.id.tv_attendance_date);
        final TextView tvAttendanceType = dialog.findViewById(R.id.tv_attendance_sub_type);

        tvAttendanceType.setText("Day Off");

        dialog.show();


        if (Utils.isInternetConnected(HomeActivity.this)) {
            if (isPreviousDayAvailable) {
                tvAttendanceDate.setText(convertDate(dateToAddAttendance));
            } else {
                tvAttendanceDate.setText(convertDate(currentDate()));
            }
        } else {
            tvAttendanceDate.setText(convertDate(currentDate()));
        }
        if (attendanceReasonBeanArrayList.size() > 0) {
            final AttendanceAdapter attendanceAdapter = new AttendanceAdapter(HomeActivity.this, getAttendanceReasonData("off"));
            lvAttendance.setAdapter(attendanceAdapter);
        } else {
            dialog.dismiss();
            attendanceReasonBeanArrayList = db.getAttendanceReasonList();
            dialogAttendance();
            Toast.makeText(HomeActivity.this, "You Installed new Application.Please Connect to Internet  and refresh application", Toast.LENGTH_LONG).show();
        }

        lvAttendance.setOnItemClickListener((parent, view, position, id) -> {
            institutionalArray = getAttendanceReasonData("off").get(position);
            dialog.dismiss();
            final Dialog alt = new Dialog(HomeActivity.this);
            alt.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alt.setContentView(R.layout.confirmation_dialog);
            alt.setCanceledOnTouchOutside(false);
            //dialog.setOnCancelListener(this);
            alt.setCancelable(false);
            final TextView tvTool = alt.findViewById(R.id.tv_tool);
            final TextView tvMessage = alt.findViewById(R.id.tv_message);
            final Button btnConfirm = alt.findViewById(R.id.confirm);
            final Button btnCancel = alt.findViewById(R.id.cancel);

            alt.show();
            btnConfirm.setText("CONFIRM");

            tvTool.setText("Day Off Confirmation");
            String text = "You have Selected " + institutionalArray.getAttendanceReason() + " . Do you want to Confirm? ";
            tvMessage.setText(text);


            btnConfirm.setOnClickListener(v -> {
                if (institutionalArray.getAttendanceReason().equals("Leave")) {
                    AlertDialog.Builder b = new AlertDialog.Builder(this);
                    b.setMessage("Please Mark The Leave From Leave Management")
                            .setCancelable(true)
                            .setIcon(R.mipmap.malas_logo)
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();

                }
                isFromTab = "Off";
                if (Utils.isInternetConnected(HomeActivity.this)) {
                    ComplexPreferences complexPreferences1 = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.PREVIOUS_LIST_PREF, MODE_PRIVATE);
                    String previousDateAttendance = complexPreferences1.getObject(Constant.PREVIOUS_LIST_OBJ, String.class);
                    if (previousDateAttendance != null)
                        dateToAddAttendance = previousDateAttendance;

                    if (isPreviousDayAvailable) {
                        if (dateToAddAttendance != null && !dateToAddAttendance.equalsIgnoreCase("")) {
                            new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), dateToAddAttendance);
                        } else {
                            new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), currentDate());
                        }

                    } else {
                        if (db.getAttendance().size() != 0) {
                            if (db.getAttendanceDate(currentDate()).size() == 0) {
                                if (attendanceReasonBeanArrayList != null && attendanceReasonBeanArrayList.size() > 0) {
                                    db.addAttendance(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(),
                                            "0", getAttendance("Off"), currentDate(),
                                            institutionalArray.getAttendanceId(), 0, 0);
                                    db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                    db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());
                                    Toast.makeText(HomeActivity.this, "Internet Not Available ,Attendance Marked for date " + currentDate(), Toast.LENGTH_SHORT).show();

                                    //new markAttendance().execute(mUserLoginInfoBean.getUser_id(), getAttendanceReasonData("Off").get(0).getAttendanceType(), "0", getAttendance("Off"), currentDate(), getAttendanceReasonData("Off").get(0).getAttendanceId());
                                    final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                                    if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                        llSync.setVisibility(View.VISIBLE);

                                    } else {
                                        llSync.setVisibility(View.GONE);

                                    }
                                }
                            } else {
                                Toast.makeText(HomeActivity.this, "Data is already present to SYNC", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (dateToAddAttendance != null && !dateToAddAttendance.equalsIgnoreCase("")) {
                                new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), dateToAddAttendance);
                            } else {
                                if (dateToAddAttendance != null) {
                                    new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), currentDate());

                                } else {
                                    new getAttendanceStatus().execute();
                                    Toast.makeText(HomeActivity.this, "Data is getting refreshed ,as you have not refreshed your application .Please Mark Attendance Now", Toast.LENGTH_LONG).show();

                                }


                            }
                        }

                    }
                } else {
                    if (db.getAttendanceDate(currentDate()).size() == 0) {
                        if (attendanceReasonBeanArrayList != null && attendanceReasonBeanArrayList.size() > 0) {
                            db.addAttendance(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(),
                                    "0", getAttendance("Off"), currentDate(),
                                    institutionalArray.getAttendanceId(), 0, 0);
                            db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                            db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());
                            Toast.makeText(HomeActivity.this, "Internet Not Available ,Attendance Marked for date " + currentDate(), Toast.LENGTH_SHORT).show();

                            //new markAttendance().execute(mUserLoginInfoBean.getUser_id(), getAttendanceReasonData("Off").get(0).getAttendanceType(), "0", getAttendance("Off"), currentDate(), getAttendanceReasonData("Off").get(0).getAttendanceId());
                            final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                            if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                llSync.setVisibility(View.VISIBLE);

                            } else {
                                llSync.setVisibility(View.GONE);
                            }
                        } else {
                            if (db.getAttendanceReasonList().size() > 0) {
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            } else {

                                Type reasonType = new TypeToken<ArrayList<AttendanceReasonBean>>() {
                                }.getType();
                                ComplexPreferences reasonListPref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_REASON_LIST_PREF, MODE_PRIVATE);
                                attendanceReasonBeanArrayList = reasonListPref.getArray(Constant.ATTENDANCE_REASON_LIST_OBJ, reasonType);

                            }
                            String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                            if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {
                                db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                db.addIsTodayAttendanceMarkeds("false", currentDate(), mUserLoginInfoBean.getUserId());

                            }
                            if (!db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId()).equalsIgnoreCase("true")) {
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                if (db.getAttendanceReasonList().size() > 0) {
                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                    dialogAttendance();
                                } else {
                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                    dialogAttendance();
                                    Toast.makeText(HomeActivity.this, "You Installed new Application.Please Connect to Internet  and refresh application", Toast.LENGTH_LONG).show();

                                }
                            }

                        }
                    } else {
                        Toast.makeText(HomeActivity.this, "Data is already present to SYNC", Toast.LENGTH_SHORT).show();
                    }
                }
                alt.dismiss();
            });
            btnCancel.setOnClickListener(v -> {
                if (Utils.isInternetConnected(HomeActivity.this)) {
                    //new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUser_id(), currentDate());


                    String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                    //If current day false but if its last date is already aded to sync then need both dialog and sync

                    final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                    if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {

                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);
                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                            }
                            if (!datesInAttendanceList.contains(currentDate())) {
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                            } else {
                                db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                            }
                        } else {
                            llSync.setVisibility(View.GONE);
                            new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUserId(), currentDate());

                        }

                    } else {

                        if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("false")) {

                            if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                llSync.setVisibility(View.VISIBLE);
                                ArrayList<String> datesInAttendanceList = new ArrayList<>();

                                for (int i = 0; i < attendanceBeans.size(); i++) {
                                    datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                                }
                                if (!datesInAttendanceList.contains(currentDate())) {
                                    attendanceReasonBeanArrayList = new ArrayList<>();

                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                    dialogAttendance();
                                } else {
                                    db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                    db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());
                                }
                            } else {
                                new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUserId(), currentDate());
                                llSync.setVisibility(View.GONE);
                            }


                        } else {

                            if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                llSync.setVisibility(View.VISIBLE);
                                ArrayList<String> datesInAttendanceList = new ArrayList<>();

                                for (int i = 0; i < attendanceBeans.size(); i++) {
                                    datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                                }
                                if (!datesInAttendanceList.contains(currentDate())) {
                                    attendanceReasonBeanArrayList = new ArrayList<>();
                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                                    dialogAttendance();
                                }
                            } else {
                                llSync.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {

                    String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                    //If current day false but if its last date is already aded to sync then need both dialog and sync

                    final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                    if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {

                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);
                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                            }
                            if (!datesInAttendanceList.contains(currentDate())) {
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                            } else {
                                db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                            }
                        } else {
                            llSync.setVisibility(View.GONE);
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        }

                    } else {

                        if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("false")) {

                            if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                llSync.setVisibility(View.VISIBLE);
                                ArrayList<String> datesInAttendanceList = new ArrayList<>();

                                for (int i = 0; i < attendanceBeans.size(); i++) {
                                    datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                                }
                                if (!datesInAttendanceList.contains(currentDate())) {
                                    attendanceReasonBeanArrayList = new ArrayList<>();

                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                    dialogAttendance();
                                } else {
                                    db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                    db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                                }
                            } else {
                                llSync.setVisibility(View.GONE);
                                attendanceReasonBeanArrayList = new ArrayList<>();
                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                                dialogAttendance();
                            }


                        } else {

                            if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                llSync.setVisibility(View.VISIBLE);
                                ArrayList<String> datesInAttendanceList = new ArrayList<>();

                                for (int i = 0; i < attendanceBeans.size(); i++) {
                                    datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                                }
                                if (!datesInAttendanceList.contains(currentDate())) {
                                    attendanceReasonBeanArrayList = new ArrayList<>();

                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                    dialogAttendance();
                                }
                            } else {
                                llSync.setVisibility(View.GONE);
                            }
                        }
                    }
                }
                alt.dismiss();
            });
        });
    }

    @SuppressLint("SetTextI18n")
    private void dialogLeaveConfirm() {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmation_dialog);
        dialog.setCanceledOnTouchOutside(false);
        //     dialog.setOnCancelListener(this);
        dialog.setCancelable(false);
        final TextView tvTool = dialog.findViewById(R.id.tv_tool);
        final TextView tvMessage = dialog.findViewById(R.id.tv_message);
        final Button btnConfirm = dialog.findViewById(R.id.confirm);
        final Button btnCancel = dialog.findViewById(R.id.cancel);

        dialog.show();
        btnConfirm.setText("CONFIRM");

        tvTool.setText("Leave Confirmation");
        String text = "You have Selected Leave. Do you want to Confirm? ";
        tvMessage.setText(text);


        btnConfirm.setOnClickListener(v -> {
            isFromTab = "Leave";
            if (Utils.isInternetConnected(HomeActivity.this)) {
                ComplexPreferences complexPreferences1 = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.PREVIOUS_LIST_PREF, MODE_PRIVATE);
                String previousDateAttendance = complexPreferences1.getObject(Constant.PREVIOUS_LIST_OBJ, String.class);
                if (previousDateAttendance != null)
                    dateToAddAttendance = previousDateAttendance;

                if (isPreviousDayAvailable) {
                    if (dateToAddAttendance != null && !dateToAddAttendance.equalsIgnoreCase("")) {
                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), dateToAddAttendance);
                    } else {
                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), currentDate());
                    }

                } else {
                    if (db.getAttendance().size() != 0) {
                        if (db.getAttendanceDate(currentDate()).size() == 0) {
                            if (attendanceReasonBeanArrayList != null && attendanceReasonBeanArrayList.size() > 0) {
                                db.addAttendance(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(),
                                        "0", getAttendance("L"), currentDate(),
                                        getAttendanceReasonData("L").get(0).getAttendanceId(), 0, 0);
                                db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());
                                Toast.makeText(HomeActivity.this, "Internet Not Available ,Attendance Marked for date " + currentDate(), Toast.LENGTH_SHORT).show();

                                //new markAttendance().execute(mUserLoginInfoBean.getUser_id(), getAttendanceReasonData("Off").get(0).getAttendanceType(), "0", getAttendance("Off"), currentDate(), getAttendanceReasonData("Off").get(0).getAttendanceId());
                                final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                                if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                    llSync.setVisibility(View.VISIBLE);

                                } else {
                                    llSync.setVisibility(View.GONE);

                                }
                            }
                        } else {
                            Toast.makeText(HomeActivity.this, "Data is already present to SYNC", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (dateToAddAttendance != null && !dateToAddAttendance.equalsIgnoreCase("")) {
                            new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), dateToAddAttendance);
                        } else {
                            if (dateToAddAttendance != null) {
                                new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), currentDate());
                            } else {
                                new getAttendanceStatus().execute();
                                Toast.makeText(HomeActivity.this, "Data is getting refreshed ,as you have not refreshed your application .Please Mark Attendance Now", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            } else {
                if (db.getAttendanceDate(currentDate()).size() == 0) {
                    if (attendanceReasonBeanArrayList != null && attendanceReasonBeanArrayList.size() > 0) {
                        db.addAttendance(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(),
                                "0", getAttendance("L"), currentDate(),
                                getAttendanceReasonData("L").get(0).getAttendanceId(), 0, 0);
                        db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                        db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());
                        Toast.makeText(HomeActivity.this, "Internet Not Available ,Attendance Marked for date " + currentDate(), Toast.LENGTH_SHORT).show();
                        final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);

                        } else {
                            llSync.setVisibility(View.GONE);
                        }
                    } else {
                        if (db.getAttendanceReasonList().size() > 0) {
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                        } else {

                            Type reasonType = new TypeToken<ArrayList<AttendanceReasonBean>>() {
                            }.getType();
                            ComplexPreferences reasonListPref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_REASON_LIST_PREF, MODE_PRIVATE);
                            attendanceReasonBeanArrayList = reasonListPref.getArray(Constant.ATTENDANCE_REASON_LIST_OBJ, reasonType);

                        }
                        String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                        if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {
                            db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                            db.addIsTodayAttendanceMarkeds("false", currentDate(), mUserLoginInfoBean.getUserId());

                        }
                        if (!db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId()).equalsIgnoreCase("true")) {
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            if (db.getAttendanceReasonList().size() > 0) {
                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                            } else {
                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                                Toast.makeText(HomeActivity.this, "You Installed new Application.Please Connect to Internet  and refresh application", Toast.LENGTH_LONG).show();

                            }

                        }

                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Data is already present to SYNC", Toast.LENGTH_SHORT).show();
                }


            }

            dialog.dismiss();
        });
        btnCancel.setOnClickListener(v -> {
            if (Utils.isInternetConnected(HomeActivity.this)) {
                //new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUser_id(), currentDate());


                String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                //If current day false but if its last date is already aded to sync then need both dialog and sync

                final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {

                    if (attendanceBeans != null && attendanceBeans.size() > 0) {
                        llSync.setVisibility(View.VISIBLE);
                        ArrayList<String> datesInAttendanceList = new ArrayList<>();

                        for (int i = 0; i < attendanceBeans.size(); i++) {
                            datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                        }
                        if (!datesInAttendanceList.contains(currentDate())) {
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        } else {
                            db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                            db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                        }
                    } else {
                        llSync.setVisibility(View.GONE);
                        new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUserId(), currentDate());

                    }

                } else {

                    if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("false")) {

                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);
                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                            }
                            if (!datesInAttendanceList.contains(currentDate())) {
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                            } else {
                                db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());
                            }
                        } else {
                            new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUserId(), currentDate());

                            llSync.setVisibility(View.GONE);

                        }


                    } else {

                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);
                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                            }
                            if (!datesInAttendanceList.contains(currentDate())) {
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                            }
                        } else {
                            llSync.setVisibility(View.GONE);

                        }
                    }
                }

            } else {

                String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                //If current day false but if its last date is already aded to sync then need both dialog and sync


                final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {

                    if (attendanceBeans != null && attendanceBeans.size() > 0) {
                        llSync.setVisibility(View.VISIBLE);
                        ArrayList<String> datesInAttendanceList = new ArrayList<>();

                        for (int i = 0; i < attendanceBeans.size(); i++) {
                            datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                        }
                        if (!datesInAttendanceList.contains(currentDate())) {
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        } else {
                            db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                            db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                        }
                    } else {
                        llSync.setVisibility(View.GONE);
                        attendanceReasonBeanArrayList = new ArrayList<>();

                        attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                        dialogAttendance();
                    }

                } else {

                    if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("false")) {

                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);
                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                            }
                            if (!datesInAttendanceList.contains(currentDate())) {
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                            } else {
                                db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                            }
                        } else {
                            llSync.setVisibility(View.GONE);
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        }


                    } else {

                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);
                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                            }
                            if (!datesInAttendanceList.contains(currentDate())) {
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                            }
                        } else {
                            llSync.setVisibility(View.GONE);

                        }
                    }
                }
            }
            dialog.dismiss();

        });


    }

    private void dialogOtherWork() {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.other_work_attendance_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(this);
        // dialog.setCancelable(false);
        final ListView lvAttendance = dialog.findViewById(R.id.attendance_listview);
        final TextView tvAttendanceDate = dialog.findViewById(R.id.tv_attendance_date);
        final TextView tvAttendanceType = dialog.findViewById(R.id.tv_attendance_sub_type);

        if (mUserLoginInfoBean.getSalesType().equals("MT")) {
            tvAttendanceType.setText("Work Type");
        }
        dialog.show();


        if (Utils.isInternetConnected(HomeActivity.this)) {
            if (isPreviousDayAvailable) {
                tvAttendanceDate.setText(convertDate(dateToAddAttendance));
            } else {
                tvAttendanceDate.setText(convertDate(currentDate()));
            }
        } else {
            tvAttendanceDate.setText(convertDate(currentDate()));
        }
        if (attendanceReasonBeanArrayList.size() > 0) {
            final AttendanceAdapter attendanceAdapter = new AttendanceAdapter(HomeActivity.this, getAttendanceReasonData("O"));
            lvAttendance.setAdapter(attendanceAdapter);

        } else {
            dialog.dismiss();
            attendanceReasonBeanArrayList = db.getAttendanceReasonList();
            dialogAttendance();
            Toast.makeText(HomeActivity.this, "You Installed new Application.Please Connect to Internet  and refresh application", Toast.LENGTH_LONG).show();
        }

        lvAttendance.setOnItemClickListener((parent, view, position, id) -> {
            //user_id,attendance_type,dsr_status, attendance (P ,A ,PA),attendance_date,created_date
            //dsr_status=0 it means dsr resort not submit update this column at the time to submission of report;
            //created date ,from server NOW() when record get submit

            showConfirmDialog(getAttendanceReasonData("O").get(position), "otherConfirm");
            if (getAttendanceReasonData("O").get(position).getAttendanceReason().equals("Joint Work")) {
                dialogJointWork();
            }
            dialog.dismiss();
        });

    }

    private void dialogJointWork() {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.other_work_attendance_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(this);
        // dialog.setCancelable(false);
        final ListView lvAttendance = dialog.findViewById(R.id.attendance_listview);
        final TextView tvAttendanceDate = dialog.findViewById(R.id.tv_attendance_date);
        final TextView tvAttendanceType = dialog.findViewById(R.id.tv_attendance_sub_type);

        tvAttendanceType.setText("Joint Work");
        dialog.show();


        if (Utils.isInternetConnected(HomeActivity.this)) {
            if (isPreviousDayAvailable) {
                tvAttendanceDate.setText(convertDate(dateToAddAttendance));
            } else {
                tvAttendanceDate.setText(convertDate(currentDate()));
            }
        } else {
            tvAttendanceDate.setText(convertDate(currentDate()));
        }
        ArrayList<JointWorkBean> temp = db.getJointWorkList();
        for (Integer i = 0; i < temp.size(); i++) {
            Log.d("UserID", temp.get(i).getUserID().toString());
            Log.d("UserName", temp.get(i).getUserName());
            Log.d("UserDesig", temp.get(i).getDesig().toString());
        }
        final JointWorkAdapter attendanceAdapter = new JointWorkAdapter(HomeActivity.this, db.getJointWorkList());
        lvAttendance.setAdapter(attendanceAdapter);

        JointWork_With = new JointWorkBean();
        lvAttendance.setOnItemClickListener((parent, view, position, id) -> {
            //user_id,attendance_type,dsr_status, attendance (P ,A ,PA),attendance_date,created_date
            //dsr_status=0 it means dsr resort not submit update this column at the time to submission of report;
            //created date ,from server NOW() when record get submit
            JointWork_With = (JointWorkBean) parent.getItemAtPosition(position);
            // Toast.makeText(this, "UserID:" + JointWork_With.getUserID() + "User Name:" + JointWork_With.getUserName() + "Desig:" + JointWork_With.getDesig(), Toast.LENGTH_LONG).show();
            dialog.dismiss();
        });

    }

    @SuppressLint("SetTextI18n")
    private void showConfirmDialog(final AttendanceReasonBean attendanceReasonBean, final String confirm) {

        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmation_dialog);
        dialog.setCanceledOnTouchOutside(false);
        //dialog.setOnCancelListener(this);
        dialog.setCancelable(false);
        final TextView tvTool = dialog.findViewById(R.id.tv_tool);
        final TextView tvMessage = dialog.findViewById(R.id.tv_message);
        final Button btnConfirm = dialog.findViewById(R.id.confirm);
        final Button btnCancel = dialog.findViewById(R.id.cancel);

        dialog.show();

        if (confirm.equalsIgnoreCase("unapprove")) {
            tvTool.setText("Request Rejected");
            btnConfirm.setText("OK");
            String text = "Your Manager has Unapproved the request .Do you want to update your attendance?";
            tvMessage.setText(text);
        } else {
            btnConfirm.setText("CONFIRM");

            tvTool.setText("Other Work Confirmation");

            String text = "You have Selected " + attendanceReasonBean.getAttendanceReason() + " . Do you want to Confirm? ";
            tvMessage.setText(text);

        }


        btnConfirm.setOnClickListener(v -> {

            if (confirm.equalsIgnoreCase("unapprove")) {
                isUnApprove = true;
                if (Utils.isInternetConnected(HomeActivity.this)) {
                    dialog.dismiss();
                    new attendanceReasonList().execute();
                } else {
                    dialog.dismiss();
                    attendanceReasonBeanArrayList = new ArrayList<>();

                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                    dialogAttendance();
                }


            } else {
                isFromTab = "Other Work";
                if (Utils.isInternetConnected(HomeActivity.this)) {
                    if (attendanceReasonBean.getAttendanceReason().equals("Admin Work") && count.get("Admin Work") >= 5) {
                        Utils.showSnackBar(this, tvUpdateAttendanceStatus, "Admin Work Exceeds More than 5 times");
                        dialogAttendance();
                    } else if (attendanceReasonBean.getAttendanceReason().equals("Other Market") && count.get("Other Market") >= 4) {
                        Utils.showSnackBar(this, tvUpdateAttendanceStatus, "Other Market Exceeds More than 4 times");
                        dialogAttendance();
                    } else if (attendanceReasonBean.getAttendanceReason().equals("Store Visit (MT)") && mUserLoginInfoBean.getSalesType().equals("GT")) {
                        Utils.showSnackBar(this, tvUpdateAttendanceStatus, "Only For Morden Trade ");
                        dialogAttendance();
                    } else {
                        //   Utils.showSnackBar(this, tvUpdateAttendanceStatus, "You Can Mark The Attendance");
                        ComplexPreferences complexPreferences1 = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.PREVIOUS_LIST_PREF, MODE_PRIVATE);
                        String previousDateAttendance = complexPreferences1.getObject(Constant.PREVIOUS_LIST_OBJ, String.class);
                        if (previousDateAttendance != null)
                            dateToAddAttendance = previousDateAttendance;

                        attendanceReason = attendanceReasonBean.getAttendanceReason();
                        institutionalArray = attendanceReasonBean;
                        if (isPreviousDayAvailable) {
                            if (dateToAddAttendance != null && !dateToAddAttendance.equalsIgnoreCase("")) {
                                new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), dateToAddAttendance);
                            } else {
                                new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), currentDate());
                            }
                        } else {
                            if (db.getAttendance().size() != 0) {
                                if (db.getAttendanceDate(currentDate()).size() == 0) {
                                    if (attendanceReasonBeanArrayList != null && attendanceReasonBeanArrayList.size() > 0) {
                                        attendanceReason = attendanceReasonBean.getAttendanceReason();
                                        institutionalArray = attendanceReasonBean;

                                        if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                            db.addAttendance(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), 0, 0);
                                            // new markAttendance().execute(mUserLoginInfoBean.getUser_id(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                            db.addAttendance(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId(), 0, 0);
                                            // new markAttendance().execute(mUserLoginInfoBean.getUser_id(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                            db.addAttendance(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId(), JointWork_With.getUserID(), JointWork_With.getDesig());
                                        } else {
                                            db.addAttendance(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), 0, 0);
                                            // new markAttendance().execute(mUserLoginInfoBean.getUser_id(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                                        }

                                        db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                        db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());
                                        Toast.makeText(HomeActivity.this, "Internet Not Available ,Attendance Marked for date " + currentDate(), Toast.LENGTH_SHORT).show();
                                        final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                            llSync.setVisibility(View.VISIBLE);

                                        } else {
                                            llSync.setVisibility(View.GONE);
                                        }
                                    }
                                } else {
                                    Toast.makeText(HomeActivity.this, "Data is already present to SYNC", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                if (dateToAddAttendance != null && !dateToAddAttendance.equalsIgnoreCase("")) {
                                    new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), dateToAddAttendance);
                                } else {

                                    if (dateToAddAttendance != null) {
                                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), currentDate());
                                    } else {
                                        new getAttendanceStatus().execute();
                                        Toast.makeText(HomeActivity.this, "Data is getting refreshed ,as you have not refreshed your application .Please Mark Attendance Now", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (db.getAttendanceDate(currentDate()).size() == 0) {
                        if (attendanceReasonBeanArrayList != null && attendanceReasonBeanArrayList.size() > 0) {
                            attendanceReason = attendanceReasonBean.getAttendanceReason();
                            institutionalArray = attendanceReasonBean;

                            if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                db.addAttendance(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(),
                                        "-1", getAttendance(institutionalArray.attendanceType), currentDate(),
                                        institutionalArray.getAttendanceId(), 0, 0);

                                // new markAttendance().execute(mUserLoginInfoBean.getUser_id(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                db.addAttendance(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(),
                                        "0", getAttendance("F"), currentDate(),
                                        institutionalArray.getAttendanceId(), 0, 0);
                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                db.addAttendance(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId(), JointWork_With.getUserID(), JointWork_With.getDesig());
                            } else {
                                db.addAttendance(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(),
                                        "0", getAttendance(institutionalArray.attendanceType), currentDate(),
                                        institutionalArray.getAttendanceId(), 0, 0);
                                // new markAttendance().execute(mUserLoginInfoBean.getUser_id(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                            }
                            db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                            db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());
                            Toast.makeText(HomeActivity.this, "Internet Not Available ,Attendance Marked for date " + currentDate(), Toast.LENGTH_SHORT).show();
                            final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                            if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                llSync.setVisibility(View.VISIBLE);

                            } else {
                                llSync.setVisibility(View.GONE);
                            }
                        } else {
                            if (db.getAttendanceReasonList().size() > 0) {
                                attendanceReasonBeanArrayList = new ArrayList<>();
                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            } else {

                                Type reasonType = new TypeToken<ArrayList<AttendanceReasonBean>>() {
                                }.getType();
                                ComplexPreferences reasonListPref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_REASON_LIST_PREF, MODE_PRIVATE);
                                attendanceReasonBeanArrayList = reasonListPref.getArray(Constant.ATTENDANCE_REASON_LIST_OBJ, reasonType);

                            }
                            String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                            if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {
                                db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                db.addIsTodayAttendanceMarkeds("false", currentDate(), mUserLoginInfoBean.getUserId());

                            }
                            if (!db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId()).equalsIgnoreCase("true")) {
                                attendanceReasonBeanArrayList = new ArrayList<>();
                                if (db.getAttendanceReasonList().size() > 0) {
                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                                    dialogAttendance();
                                } else {
                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                                    dialogAttendance();
                                    Toast.makeText(HomeActivity.this, "You Installed new Application.Please Connect to Internet  and refresh application", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, "Data is already present to SYNC", Toast.LENGTH_SHORT).show();
                    }


                }
                dialog.dismiss();
            }
            dialog.setOnDismissListener(dialog1 -> {
                if (Utils.isInternetConnected(HomeActivity.this)) {
                    onResume();
                }
            });
        });
        btnCancel.setOnClickListener(v -> {
            if (confirm.equalsIgnoreCase("unapprove")) {
                AttendanceReasonBean attendanceReasonBean1 = db.getAttendanceStatusHome(currentDate(), mUserLoginInfoBean.getUserId());
                ComplexPreferences attendancePref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_LIST_PREF, MODE_PRIVATE);
                attendancePref.putObject(Constant.ATTENDANCE_LIST_OBJ, attendanceReasonBean1);
                attendancePref.commit();
                if (attendanceReasonBean1.getAttendanceMessage() != null && !attendanceReasonBean1.getAttendanceMessage().equalsIgnoreCase("")) {
                    //   tvUpdateAttendanceStatus.setVisibility(View.VISIBLE);
                    tvUpdateAttendanceStatus.setText(attendanceReasonBean1.getAttendanceMessage());

                } else {

                    tvUpdateAttendanceStatus.setVisibility(View.GONE);
                }
                //isUnApprovedStatusCancel = true;

            } else {
                if (Utils.isInternetConnected(HomeActivity.this)) {
                    dialogOtherWork();
                    dialog.dismiss();
                } else {

                    String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                    if (!isTodayAttendanceMarked.equalsIgnoreCase("true")) {
                        final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);
                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                            }
                            if (!datesInAttendanceList.contains(currentDate())) {
                                //if previous day att. present in sync and today is new day .
                                // so first he has to add previous day attendance then only today attendance
                                //else have to add in offline mode
                                //  Toast.makeText(HomeActivity.this, "Internet is available please sync the data then add new attendance", Toast.LENGTH_SHORT).show();
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                            }
                        } else {
                            llSync.setVisibility(View.GONE);
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        }
                    }
                }
            }
            dialog.dismiss();
        });

    }


    private void checkPreviousDialogAttendance() {
        Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.previous_attendance_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        final ListView lvAttendance = dialog.findViewById(R.id.attendance_listview);

        dialog.show();
        if (previousDayAttendance.size() != 0) {

            final PreviousAttendanceAdapter attendanceAdapter = new PreviousAttendanceAdapter(HomeActivity.this, previousDayAttendance);
            lvAttendance.setAdapter(attendanceAdapter);
        } else {
            dialog.dismiss();
        }


        lvAttendance.setOnItemClickListener((parent, view, position, id) -> {
            //user_id,attendance_type,dsr_status, attendance (P ,A ,PA),attendance_date,created_date
            //dsr_status=0 it means dsr resort not submit update this column at the time to submission of report;
            //created date ,from server NOW() when record get submit
            if (position > 0) {
                Toast.makeText(HomeActivity.this, "Please Add attendance in a Sequence", Toast.LENGTH_SHORT).show();
            } else {
                dialog.dismiss();

                isPreviousDayAvailable = true;
                if (previousDayAttendance != null && previousDayAttendance.size() > 0) {
                    dateToAddAttendance = previousDayAttendance.get(position).getDate();
                    ComplexPreferences attendancePref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.PREVIOUS_LIST_PREF, MODE_PRIVATE);
                    attendancePref.putObject(Constant.PREVIOUS_LIST_OBJ, dateToAddAttendance);
                    attendancePref.commit();
                }

                //new markAttendance().execute(mUserLoginInfoBean.getUser_id(), attendanceReasonBeanArrayList.get(position).attendanceType, "0", getAttendance(attendanceReasonBeanArrayList.get(position).attendanceType), currentDate());
                if (Utils.isInternetConnected(HomeActivity.this)) {
                    new attendanceReasonList().execute();
                } else {


                    Toast.makeText(HomeActivity.this, "Please connect to Internet,You can Add Today Attendance", Toast.LENGTH_SHORT).show();
                    if (db.getAttendanceReasonList().size() > 0) {
                        attendanceReasonBeanArrayList = new ArrayList<>();

                        attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                    } else {

                        Type reasonType = new TypeToken<ArrayList<AttendanceReasonBean>>() {
                        }.getType();
                        ComplexPreferences reasonListPref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_REASON_LIST_PREF, MODE_PRIVATE);
                        attendanceReasonBeanArrayList = reasonListPref.getArray(Constant.ATTENDANCE_REASON_LIST_OBJ, reasonType);

                    }
                    String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                    if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {
                        db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                        db.addIsTodayAttendanceMarkeds("false", currentDate(), mUserLoginInfoBean.getUserId());
                    }
                    if (!db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId()).equalsIgnoreCase("true")) {
                        attendanceReasonBeanArrayList = new ArrayList<>();
                        attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                        dialogAttendance();
                    }

                }

            }

        });

    }


    private String currentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return df.format(c.getTime());
    }

    private String getAttendance(String attendanceType) {
        switch (attendanceType) {
            case "F":
                return "P";
            case "O":
            case "L":
                return "PA";
            case "Off":
                return "Off";
        }
        return "";
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        //if a user confirm the other work then the dialog gets dismiss it means it is cancelled
        //new checkPreviousDayAttendance().execute(mUserLoginInfoBean.getUser_id(), currentDate());
        if (Utils.isInternetConnected(HomeActivity.this)) {

            String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
            //If current day false but if its last date is already aded to sync then need both dialog and sync

            final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
            if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {

                if (attendanceBeans != null && attendanceBeans.size() > 0) {
                    llSync.setVisibility(View.VISIBLE);
                    ArrayList<String> datesInAttendanceList = new ArrayList<>();

                    for (int i = 0; i < attendanceBeans.size(); i++) {
                        datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                    }
                    if (!datesInAttendanceList.contains(currentDate())) {
                        attendanceReasonBeanArrayList = new ArrayList<>();

                        attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                        dialogAttendance();
                    } else {
                        db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                        db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                    }
                } else {
                    llSync.setVisibility(View.GONE);
                    new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUserId(), currentDate());

                }

            } else {

                if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("false")) {

                    if (attendanceBeans != null && attendanceBeans.size() > 0) {
                        llSync.setVisibility(View.VISIBLE);
                        ArrayList<String> datesInAttendanceList = new ArrayList<>();

                        for (int i = 0; i < attendanceBeans.size(); i++) {
                            datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                        }
                        if (!datesInAttendanceList.contains(currentDate())) {
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        } else {
                            db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                            db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                        }
                    } else {
                        new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUserId(), currentDate());

                        llSync.setVisibility(View.GONE);

                    }


                } else {

                    if (attendanceBeans != null && attendanceBeans.size() > 0) {
                        llSync.setVisibility(View.VISIBLE);
                        ArrayList<String> datesInAttendanceList = new ArrayList<>();

                        for (int i = 0; i < attendanceBeans.size(); i++) {
                            datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                        }
                        if (!datesInAttendanceList.contains(currentDate())) {
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        }
                    } else {
                        llSync.setVisibility(View.GONE);

                    }
                }
            }
        } else {

            String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
            //If current day false but if its last date is already aded to sync then need both dialog and sync


            final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
            if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {

                if (attendanceBeans != null && attendanceBeans.size() > 0) {
                    llSync.setVisibility(View.VISIBLE);
                    ArrayList<String> datesInAttendanceList = new ArrayList<>();

                    for (int i = 0; i < attendanceBeans.size(); i++) {
                        datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                    }
                    if (!datesInAttendanceList.contains(currentDate())) {
                        attendanceReasonBeanArrayList = new ArrayList<>();

                        attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                        dialogAttendance();
                    } else {
                        db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                        db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                    }
                } else {
                    llSync.setVisibility(View.GONE);
                    attendanceReasonBeanArrayList = new ArrayList<>();

                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                    dialogAttendance();
                }

            } else {

                if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("false")) {

                    if (attendanceBeans != null && attendanceBeans.size() > 0) {
                        llSync.setVisibility(View.VISIBLE);
                        ArrayList<String> datesInAttendanceList = new ArrayList<>();

                        for (int i = 0; i < attendanceBeans.size(); i++) {
                            datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                        }
                        if (!datesInAttendanceList.contains(currentDate())) {
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        } else {
                            db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                            db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                        }
                    } else {
                        llSync.setVisibility(View.GONE);
                        attendanceReasonBeanArrayList = new ArrayList<>();

                        attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                        dialogAttendance();
                    }


                } else {

                    if (attendanceBeans != null && attendanceBeans.size() > 0) {
                        llSync.setVisibility(View.VISIBLE);
                        ArrayList<String> datesInAttendanceList = new ArrayList<>();

                        for (int i = 0; i < attendanceBeans.size(); i++) {
                            datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                        }
                        if (!datesInAttendanceList.contains(currentDate())) {
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        }
                    } else {
                        llSync.setVisibility(View.GONE);

                    }
                }
            }
        }
    }


    private void dialogSync(final ArrayList<AttendanceBean> attendanceBeans) {


        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sync_attendance_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(this);

        final ListView lvAttendance = dialog.findViewById(R.id.attendance_listview);
        final Button btn_submit = dialog.findViewById(R.id.sync_submit);
        final Button btnCancel = dialog.findViewById(R.id.sync_cancel);


        dialog.show();


        final SyncAttendanceAdapter syncAttendanceAdapter = new SyncAttendanceAdapter(HomeActivity.this, attendanceBeans);
        lvAttendance.setAdapter(syncAttendanceAdapter);

        btn_submit.setOnClickListener(v -> {
            if (Utils.isInternetConnected(HomeActivity.this)) {
                if (attendanceBeans.get(0).getAttendanceType().equalsIgnoreCase("F")) {
                    offlineDateAdd = attendanceBeans.get(0).getAttendanceDate();
                    isFromTab = "Field Work";
                    new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), attendanceBeans.get(0).getAttendanceDate());

                } else if (attendanceBeans.get(0).getAttendanceType().equalsIgnoreCase("L")) {
                    offlineDateAdd = attendanceBeans.get(0).getAttendanceDate();
                    isFromTab = "Leave";
                    new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), attendanceBeans.get(0).getAttendanceDate());

                } else if (attendanceBeans.get(0).getAttendanceType().equalsIgnoreCase("O")) {
                    offlineDateAdd = attendanceBeans.get(0).getAttendanceDate();
                    isFromTab = "Other work";
                    new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), attendanceBeans.get(0).getAttendanceDate());

                } else if (attendanceBeans.get(0).getAttendanceType().equalsIgnoreCase("Off")) {
                    offlineDateAdd = attendanceBeans.get(0).getAttendanceDate();
                    isFromTab = "Off";
                    new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), attendanceBeans.get(0).getAttendanceDate());
                }
                    /*if (attendanceBeans.get(i).getAttendanceType().equalsIgnoreCase("F")) {
                        offlineDateAdd = attendanceBeans.get(i).getAttendanceDate();
                        isFromTab = "Field Work";
                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUser_id(), attendanceBeans.get(i).getAttendanceDate());

                    } else if (attendanceBeans.get(i).getAttendanceType().equalsIgnoreCase("L")) {
                        offlineDateAdd = attendanceBeans.get(i).getAttendanceDate();
                        isFromTab = "Leave";
                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUser_id(), attendanceBeans.get(i).getAttendanceDate());

                    } else if (attendanceBeans.get(i).getAttendanceType().equalsIgnoreCase("O")) {
                        offlineDateAdd = attendanceBeans.get(i).getAttendanceDate();
                        isFromTab = "Other work";
                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUser_id(), attendanceBeans.get(i).getAttendanceDate());

                    } else if (attendanceBeans.get(i).getAttendanceType().equalsIgnoreCase("Off")) {
                        offlineDateAdd = attendanceBeans.get(i).getAttendanceDate();
                        isFromTab = "Off";

                    }*/

                //  new markAttendance().execute(attendanceBeans.get(i).getUserId(), attendanceBeans.get(i).getAttendanceType(), attendanceBeans.get(i).getDsrStatus(), attendanceBeans.get(i).getPresAbsent(), attendanceBeans.get(i).getAttendanceDate(), attendanceBeans.get(i).getAttendanceTypeId());

                /*}*/
                if (db.getAttendance().size() == 0) {
                    llSync.setVisibility(View.GONE);
                } else {
                    llSync.setVisibility(View.VISIBLE);

                }
            } else {
                Toast.makeText(HomeActivity.this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();


        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_sync) {
            if (Utils.isInternetConnected(HomeActivity.this)) {
                isFromSync = true;
                new checkPreviousDayAttendance().execute(mUserLoginInfoBean.getUserId(), currentDate());

            } else {
                Toast.makeText(HomeActivity.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();

            }
        }

    }

    private String convertDate(String inputDate) {

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        Date date = null;
        try {

            date = inputFormat.parse(inputDate);

            if (date != null) {
                return outputFormat.format(date);
            }

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private List<String> getDates(String dateString1, String dateString2) {
        ArrayList<String> dates = new ArrayList<>();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        if (date1 != null) {
            cal1.setTime(date1);
        }


        Calendar cal2 = Calendar.getInstance();
        if (date2 != null) {
            cal2.setTime(date2);
        }

        while (!cal1.after(cal2)) {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            // if (!isSunday(df.format(cal1.getTime()))) {
            dates.add(df.format(cal1.getTime()));
            //}


            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    private int month() {
        Calendar c = Calendar.getInstance();

        return c.get(Calendar.MONTH) + 1;
    }

    private int year() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public boolean isSunday(String date) {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1;

        String dayOfTheWeek = "";
        try {
            date1 = df1.parse(date);
            SimpleDateFormat dayOfTheWeekoff = new SimpleDateFormat("EEEE", Locale.getDefault());
            if (date1 != null) {
                dayOfTheWeek = dayOfTheWeekoff.format(date1);
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return dayOfTheWeek.equalsIgnoreCase("sunday");

    }

    public void confirmTerritoryDialog() {

        Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.user_territory_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        final TextView terMessage = dialog.findViewById(R.id.ter_message);
        Button btnProceed = dialog.findViewById(R.id.btn_proceed);
        Button btn_logout = dialog.findViewById(R.id.btn_logout);

        dialog.show();
        terMessage.setText(" " + mUserLoginInfoBean.getTerritoryName());
        btnProceed.setOnClickListener(v -> {
            dialog.dismiss();
            progressView();

        });
        btn_logout.setOnClickListener(v -> {
            isFromProgressView = false;
            dialog.dismiss();
            logout();

        });
    }

    private void logout() {
        ComplexPreferences complexPreferences1 = ComplexPreferences.getComplexPreferences(HomeActivity.this, "", MODE_PRIVATE);

        complexPreferences1.clear(Constant.USER_GOOGLE_GCM_REG_INFO);
        complexPreferences1.clear(Constant.CITY_DISTIC_COUNTRY_PREF);
        complexPreferences1.clear(Constant.UserRegInfoPref);
        complexPreferences1.clear(Constant.DISTRIBUTOR_LIST_PREF);
        complexPreferences1.clear(Constant.ROUTE_LIST_PREF);
        complexPreferences1.clear(Constant.Retailer_LIST_PREF);
        complexPreferences1.clear(Constant.SHOW_OUTLET_PREF);
        complexPreferences1.clear(Constant.ProductListPref);
        complexPreferences1.clear(Constant.takestocklistPref);
        complexPreferences1.clear(Constant.EditedProductListPref);
        complexPreferences1.clear(Constant.EditedOrderProductListPref);
        complexPreferences1.clear(Constant.SHOW_OUTLET_ORDER_PREF);
        complexPreferences1.clear(Constant.PlaceOrderProductListPref);
        complexPreferences1.clear(Constant.REASON_LIST_PREF);
        complexPreferences1.clear(Constant.PREVIOUS_LIST_PREF);
        complexPreferences1.clear(Constant.ATTENDANCE_LIST_PREF);
        complexPreferences1.clear(Constant.Update_DSR_PREF);

        Intent in = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(in);
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    class attendanceReasonList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(HomeActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "get_attendance_reason"));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {

                        //lvActivityList.setVisibility(View.GONE);
                        //tvEmpty.setVisibility(View.VISIBLE);
                        JSONArray mJsonArray = object.getJSONArray("getReasonList");
                        attendanceReasonBeanArrayList = new ArrayList<>();
                        for (int i = 0; i < mJsonArray.length(); i++) {

                            String attendanceId = mJsonArray.getJSONObject(i).getString("id");
                            String attendanceReason = mJsonArray.getJSONObject(i).getString("reason");
                            String attendanceType = mJsonArray.getJSONObject(i).getString("type");


                            AttendanceReasonBean attendanceReasonBean = new AttendanceReasonBean(attendanceId, attendanceReason, attendanceType);
                            attendanceReasonBeanArrayList.add(attendanceReasonBean);
                        }
                        ComplexPreferences reasonListPref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_REASON_LIST_PREF, MODE_PRIVATE);
                        reasonListPref.putObject(Constant.ATTENDANCE_REASON_LIST_OBJ, attendanceReasonBeanArrayList);
                        reasonListPref.commit();
                        if (db.getAttendanceReasonList().size() > 0) {
                            db.deleteAllAttendanceReasonListRecords();
                        }
                        db.addAttendanceReasonList(attendanceReasonBeanArrayList);
                        mDialog.dismiss();
                        dialogAttendance();
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(HomeActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    mDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            } else {
                mDialog.dismiss();
                Toast.makeText(HomeActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    class markAttendance extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(HomeActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "mark_attendance"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("attendance_type", params[1]));
            nameValuePair.add(new BasicNameValuePair("dsr_status", params[2]));
            nameValuePair.add(new BasicNameValuePair("attendance", params[3]));
            nameValuePair.add(new BasicNameValuePair("attendance_date", params[4]));
            nameValuePair.add(new BasicNameValuePair("attendance_type_Id", params[5]));

            if (params[1].equals("O") && params[5].equals("5")) {
                nameValuePair.add(new BasicNameValuePair("joint_user_id", params[6]));
                nameValuePair.add(new BasicNameValuePair("joint_designation_id", params[7]));
            }

            //         return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL3, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        Toast.makeText(HomeActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        if (!isPreviousDayAvailable) {
                            if (Utils.isInternetConnected(HomeActivity.this)) {
                                db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                                new getAttendanceStatus().execute();
                                if (db.getAttendanceDate(currentDate()).size() == 0) {
                                    llSync.setVisibility(View.GONE);
                                }


                            } else {
                                //Attendance Status

                                AttendanceReasonBean attendanceReasonBean = db.getAttendanceStatusHome(currentDate(), mUserLoginInfoBean.getUserId());
                                ComplexPreferences attendancePref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_LIST_PREF, MODE_PRIVATE);
                                attendancePref.putObject(Constant.ATTENDANCE_LIST_OBJ, attendanceReasonBean);
                                attendancePref.commit();
                                if (attendanceReasonBean.getAttendanceMessage() != null && !attendanceReasonBean.getAttendanceMessage().equalsIgnoreCase("")) {
                                    //               tvUpdateAttendanceStatus.setVisibility(View.VISIBLE);
                                    tvUpdateAttendanceStatus.setText(attendanceReasonBean.getAttendanceMessage());


                                } else {

                                    tvUpdateAttendanceStatus.setVisibility(View.GONE);
                                }
                            }

                        } else {
                            isPreviousDayAvailable = false;
                            if (Utils.isInternetConnected(HomeActivity.this)) {
                                new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUserId(), currentDate());


                            } else {

                                String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                                //If current day false but if its last date is already added to sync then need both dialog and sync


                                final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                                if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {

                                    if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                        llSync.setVisibility(View.VISIBLE);
                                        ArrayList<String> datesInAttendanceList = new ArrayList<>();

                                        for (int i = 0; i < attendanceBeans.size(); i++) {
                                            datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                                        }
                                        if (!datesInAttendanceList.contains(currentDate())) {
                                            attendanceReasonBeanArrayList = new ArrayList<>();

                                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                            dialogAttendance();
                                        } else {
                                            db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                            db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                                        }
                                    } else {
                                        llSync.setVisibility(View.GONE);
                                        attendanceReasonBeanArrayList = new ArrayList<>();

                                        attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                        dialogAttendance();
                                    }

                                } else {

                                    if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("false")) {

                                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                            llSync.setVisibility(View.VISIBLE);
                                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                                            }
                                            if (!datesInAttendanceList.contains(currentDate())) {
                                                attendanceReasonBeanArrayList = new ArrayList<>();

                                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                                dialogAttendance();
                                            } else {
                                                db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                                db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                                            }
                                        } else {
                                            llSync.setVisibility(View.GONE);
                                            attendanceReasonBeanArrayList = new ArrayList<>();

                                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                            dialogAttendance();
                                        }


                                    } else {

                                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                            llSync.setVisibility(View.VISIBLE);
                                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                                            }
                                            if (!datesInAttendanceList.contains(currentDate())) {
                                                attendanceReasonBeanArrayList = new ArrayList<>();

                                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                                dialogAttendance();
                                            }
                                        } else {
                                            llSync.setVisibility(View.GONE);

                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    Toast.makeText(HomeActivity.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            } else {
                Toast.makeText(HomeActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }

    }

    @SuppressLint("StaticFieldLeak")
    class isTodayAttendanceMarked extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(HomeActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "is_today_attendance_marked"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("attendance_date", params[1]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                        db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());
                        mDialog.dismiss();
                        new unApprovedStatus().execute();


                        //Toast.makeText(HomeActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                        db.addIsTodayAttendanceMarkeds("false", currentDate(), mUserLoginInfoBean.getUserId());
                        mDialog.dismiss();
                        //false
                        //check previous days attendance, from server get last attendance_date  entry of user
                        new checkPreviousDayAttendance().execute(mUserLoginInfoBean.getUserId(), currentDate());

                    }
                } catch (JSONException e) {
                    mDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            } else {
                mDialog.dismiss();
                Toast.makeText(HomeActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @SuppressLint("StaticFieldLeak")
    class checkPreviousDayAttendance extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(HomeActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "check_previous_day_attendance"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));


            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        previousDayAttendance = new ArrayList<>();

                        JSONArray reasonArray = object.getJSONArray("dateList");

                        for (int i = 0; i < reasonArray.length(); i++) {
                            JSONObject jsonObject = reasonArray.getJSONObject(i);
                            String attendanceId = jsonObject.getString("id");
                            String type = jsonObject.getString("type");
                            String typeId = jsonObject.getString("type_id");
                            String attendance = jsonObject.getString("attendance");
                            String dsr_status = jsonObject.getString("dsr_status");
                            String date = jsonObject.getString("date");

                            PreviousDateAttendance previousDateAttendance = new PreviousDateAttendance(attendanceId, type, typeId, attendance, dsr_status, date);
                            previousDayAttendance.add(previousDateAttendance);

                        }
                        mDialog.dismiss();
                        List<String> dates = getDates(previousDayAttendance.get(0).getDate(), currentDate());
                        previousDayAttendance.clear();
                        if (dates.size() == 0) {
                            isPreviousDayAvailable = false;

                            //check previous days attendance, from server get last attendance_date  entry of user
                            if (Utils.isInternetConnected(HomeActivity.this)) {
                                if (!db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId()).equalsIgnoreCase("true")) {
                                    if (db.getAttendanceDate(currentDate()).size() == 0) {
                                        new attendanceReasonList().execute();
                                    } else {
                                        //SYNC BUTTON WHEN LAST DATE IS DONE FROM PREVIOUS DAY
                                        if (isFromSync) {
                                            isFromSync = false;
                                            attendanceReasonBeanArrayList = new ArrayList<>();

                                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                                            final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                                            if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                                dialogSync(attendanceBeans);
                                            }


                                        }
                                    }

                                } else {
                                    //SYNC BUTTON WHEN LAST DATE IS DONE FROM PREVIOUS DAY
                                    if (isFromSync) {
                                        isFromSync = false;
                                        attendanceReasonBeanArrayList = new ArrayList<>();

                                        attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                        final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                            dialogSync(attendanceBeans);
                                        }
                                    }
                                }

                            } else {
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                            }
                        } else if (dates.size() == 1) {
                            if (currentDate().equals(dates.get(0))) {
                                isPreviousDayAvailable = false;

                                //check previous days attendance, from server get last attendance_date  entry of user
                                if (Utils.isInternetConnected(HomeActivity.this)) {
                                    if (!db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId()).equalsIgnoreCase("true")) {
                                        if (db.getAttendanceDate(currentDate()).size() == 0) {
                                            new attendanceReasonList().execute();
                                        } else {
                                            //SYNC BUTTON WHEN LAST DATE IS DONE FROM PREVIOUS DAY
                                            if (isFromSync) {
                                                isFromSync = false;
                                                attendanceReasonBeanArrayList = new ArrayList<>();

                                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                                                final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                                                if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                                    dialogSync(attendanceBeans);
                                                }


                                            }
                                        }

                                    } else {
                                        //SYNC BUTTON WHEN LAST DATE IS DONE FROM PREVIOUS DAY
                                        if (isFromSync) {
                                            isFromSync = false;
                                            attendanceReasonBeanArrayList = new ArrayList<>();

                                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                            final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                                            if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                                dialogSync(attendanceBeans);
                                            }
                                        }
                                    }

                                } else {
                                    attendanceReasonBeanArrayList = new ArrayList<>();

                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                    dialogAttendance();
                                }


                            }
                        } else if (dates.size() == 2) {
                            if (currentDate().equals(dates.get(1))) {
                                isPreviousDayAvailable = false;
                                dateToAddAttendance = "";
                                ComplexPreferences attendancePref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.PREVIOUS_LIST_PREF, MODE_PRIVATE);
                                attendancePref.putObject(Constant.PREVIOUS_LIST_OBJ, dateToAddAttendance);
                                attendancePref.commit();
                                //check previous days attendance, from server get last attendance_date  entry of user
                                if (Utils.isInternetConnected(HomeActivity.this)) {
                                    if (!db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId()).equalsIgnoreCase("true")) {
                                        if (db.getAttendanceDate(currentDate()).size() == 0) {
                                            new attendanceReasonList().execute();
                                        } else {
                                            //SYNC BUTTON WHEN LAST DATE IS DONE FROM PREVIOUS DAY
                                            if (isFromSync) {
                                                isFromSync = false;
                                                attendanceReasonBeanArrayList = new ArrayList<>();

                                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                                                final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                                                if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                                    dialogSync(attendanceBeans);
                                                }


                                            }
                                        }

                                    } else {
                                        //SYNC BUTTON WHEN LAST DATE IS DONE FROM PREVIOUS DAY
                                        if (isFromSync) {
                                            isFromSync = false;
                                            attendanceReasonBeanArrayList = new ArrayList<>();

                                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                            final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                                            if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                                dialogSync(attendanceBeans);
                                            }
                                        }
                                    }

                                } else {
                                    attendanceReasonBeanArrayList = new ArrayList<>();

                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                    dialogAttendance();
                                }
//

                            }
                        } else {
                            for (int i = 1; i < dates.size(); i++) {
                                if (dates.size() - 1 == i) {
                                    if (currentDate().equals(dates.get(i))) {
                                        if (db.getAttendanceDate(currentDate()).size() == 0) {
                                            if (previousDayAttendance.size() > 0) {
                                                PreviousDateAttendance previousDateAttendance = new PreviousDateAttendance("TODAY");
                                                previousDayAttendance.add(previousDateAttendance);
                                            } else {

                                                isPreviousDayAvailable = false;
                                                attendanceReasonBeanArrayList = new ArrayList<>();

                                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                                                /*if (!db.getIsTodayAttendanceMarked(currentDate()).equalsIgnoreCase("true")) {
                                                    if (db.getAttendanceDate(currentDate()).size() == 0) {
                                                        new attendanceReasonList().execute();
                                                    }

                                                }*/
                                            }
                                        }

                                    } else {
                                        if (db.getAttendance().size() > 0) {
                                            for (int j = 0; j < db.getAttendance().size(); j++) {
                                                if (!db.getAttendance().get(j).getAttendanceDate().equalsIgnoreCase(dates.get(i))) {
                                                    PreviousDateAttendance previousDateAttendance = new PreviousDateAttendance(dates.get(i));
                                                    previousDayAttendance.add(previousDateAttendance);
                                                }
                                            }
                                        } else {
                                            PreviousDateAttendance previousDateAttendance = new PreviousDateAttendance(dates.get(i));
                                            previousDayAttendance.add(previousDateAttendance);
                                        }

                                    }

                                } else {

                                    ArrayList<String> datesInAttendanceList = new ArrayList<>();
                                    if (db.getAttendance() != null && db.getAttendance().size() > 0) {
                                        for (int j = 0; j < db.getAttendance().size(); j++) {
                                            datesInAttendanceList.add(db.getAttendance().get(j).getAttendanceDate());
                                        }

                                        if (!datesInAttendanceList.contains(dates.get(i))) {

                                            PreviousDateAttendance previousDateAttendance = new PreviousDateAttendance(dates.get(i));
                                            previousDayAttendance.add(previousDateAttendance);

                                        } else {
                                            //SYNC BUTTON WHEN LAST DATE IS DONE FROM PREVIOUS DAY
                                            if (isFromSync) {
                                                isFromSync = false;
                                                attendanceReasonBeanArrayList = new ArrayList<>();

                                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                                                final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                                                if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                                    dialogSync(attendanceBeans);
                                                }
                                            }
                                        }
                                    } else {
                                        PreviousDateAttendance previousDateAttendance = new PreviousDateAttendance(dates.get(i));
                                        previousDayAttendance.add(previousDateAttendance);
                                    }


                                }


                            }


                            checkPreviousDialogAttendance();

                        }

                    } else {
                        isPreviousDayAvailable = false;
                        dateToAddAttendance = "";
                        ComplexPreferences attendancePref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.PREVIOUS_LIST_PREF, MODE_PRIVATE);
                        attendancePref.putObject(Constant.PREVIOUS_LIST_OBJ, dateToAddAttendance);
                        attendancePref.commit();
                        //check previous days attendance, from server get last attendance_date  entry of user

                        if (Utils.isInternetConnected(HomeActivity.this)) {
                            mDialog.dismiss();
                            if (!db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId()).equalsIgnoreCase("true")) {
                                if (db.getAttendanceDate(currentDate()).size() == 0) {
                                    new attendanceReasonList().execute();
                                } else {
                                    if (isFromSync) {
                                        isFromSync = false;
                                        attendanceReasonBeanArrayList = new ArrayList<>();

                                        attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                                        final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                            dialogSync(attendanceBeans);
                                        }
                                    }
                                }
                            } else {
                                if (isFromSync) {
                                    isFromSync = false;
                                    attendanceReasonBeanArrayList = new ArrayList<>();

                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                                    final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                                    if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                        dialogSync(attendanceBeans);
                                    }
                                }
                            }

                        } else {
                            mDialog.dismiss();
                            attendanceReasonBeanArrayList = new ArrayList<>();
                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                            dialogAttendance();
                        }

                        //If Not Marked Attendance then reasonList
                        //Toast.makeText(HomeActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    Toast.makeText(HomeActivity.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }

            } else {
                mDialog.dismiss();
                Toast.makeText(HomeActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }

        }

    }

/*
    @SuppressLint("StaticFieldLeak")
    class getProductivity extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(HomeActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Logging in please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getproductivity"));
            nameValuePair.add(new BasicNameValuePair("month", params[0]));
            nameValuePair.add(new BasicNameValuePair("year", params[1]));
            nameValuePair.add(new BasicNameValuePair("userId", mUserLoginInfoBean.getUser_id()));*//*mUserLoginInfoBean.getUser_id())*//*
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);

        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject jsonResponse) {
            if (jsonResponse != null) {

                try {
                    if (jsonResponse.getString("success").equalsIgnoreCase("true")) {


                        int totalProductiveCount = jsonResponse.getInt("totalProductiveCount");
                        int totalnonProductiveCount = jsonResponse.getInt("totalnonProductiveCount");
                        String month = jsonResponse.getString("month");
                        String year = jsonResponse.getString("year");

                        int productivityTotal;
                        int productiveNonProductivCount = totalProductiveCount + totalnonProductiveCount;

                        if (productiveNonProductivCount != 0) {

                            productivityTotal = (int) Math.ceil(((double) totalProductiveCount / (double) productiveNonProductivCount) * 100);
                            String val = productivityTotal + " %";
                            tvProductivity.setText(val);
                            if (productivityTotal >= 100) {
                                String value = "0";
                                float finalPercent = Float.parseFloat(value);
                                productivityProgressView.setProgress(finalPercent);
                                db.addProductivityHome(String.valueOf(productivityTotal), String.valueOf(finalPercent), mUserLoginInfoBean.getUser_id(), month, year);
                            } else if (productivityTotal == 0) {
                                String value = "1.0";
                                float finalPercent = Float.parseFloat(value);
                                productivityProgressView.setProgress(finalPercent);
                                db.addProductivityHome(String.valueOf(productivityTotal), String.valueOf(finalPercent), mUserLoginInfoBean.getUser_id(), month, year);

                            } else {
                                int productivity = 100 - productivityTotal;
                                String value = "0." + productivity;
                                float finalPercent = Float.parseFloat(value);
                                productivityProgressView.setProgress(finalPercent);
                                db.addProductivityHome(String.valueOf(productivityTotal), String.valueOf(finalPercent), mUserLoginInfoBean.getUser_id(), month, year);

                            }
                        } else {
                            productivityTotal = 0;
                            String val = productivityTotal + " %";
                            tvProductivity.setText(val);
                            String value = "1.0";
                            float finalPercent = Float.parseFloat(value);
                            productivityProgressView.setProgress(finalPercent);
                            db.addProductivityHome(String.valueOf(productivityTotal), String.valueOf(finalPercent), mUserLoginInfoBean.getUser_id(), month, year);

                        }
                    } else {
                        int productivityTotal = 0;
                        String val = productivityTotal + " %";
                        tvProductivity.setText(val);
                        String value = "1.0";
                        float finalPercent = Float.parseFloat(value);
                        productivityProgressView.setProgress(finalPercent);
                        db.addProductivityHome(String.valueOf(productivityTotal), String.valueOf(finalPercent), mUserLoginInfoBean.getUser_id(), String.valueOf(month()), String.valueOf(year()));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } else {
                int productivityTotal = 0;
                String val = productivityTotal + " %";
                tvProductivity.setText(val);
                String value = "1.0";
                float finalPercent = Float.parseFloat(value);
                productivityProgressView.setProgress(finalPercent);
                db.addProductivityHome(String.valueOf(productivityTotal), String.valueOf(finalPercent), mUserLoginInfoBean.getUser_id(), String.valueOf(month()), String.valueOf(year()));

            }
            if (Utils.isInternetConnected(HomeActivity.this)) {
                db.deleteAllTargetAchievementRecords(mUserLoginInfoBean.getUser_id());
                new GetTargetList().execute(String.valueOf(month()), String.valueOf(year()));

            } else {

                TargetAchievementProgress targetAchievementProgress1 = db.getTargetAchievementHome(String.valueOf(month()), String.valueOf(year()), mUserLoginInfoBean.getUser_id());
                if (targetAchievementProgress1 != null) {
                    if (targetAchievementProgress1.getTarget() != null && !targetAchievementProgress1.getTarget().equalsIgnoreCase("")) {
                        tvTargetValue.setText(targetAchievementProgress1.getTarget());
                    } else {
                        tvTargetValue.setText("0");
                    }

                    if (targetAchievementProgress1.getAchievement() != null && !targetAchievementProgress1.getAchievement().equalsIgnoreCase("")) {
                        tvAchievementValue.setText(targetAchievementProgress1.getAchievement());

                    } else {
                        tvAchievementValue.setText("0");

                    }
                    if (targetAchievementProgress1.getPercentage() != null && !targetAchievementProgress1.getPercentage().equalsIgnoreCase("")) {
                        tvTargetAchPercent.setText(targetAchievementProgress1.getPercentage() + "%");

                    } else {
                        tvTargetAchPercent.setText("0 %");

                    }


                    if (targetAchievementProgress1.getProgressValueTargetAchievement() != null && !targetAchievementProgress1.getProgressValueTargetAchievement().equalsIgnoreCase("")) {
                        float finalPercent = Float.parseFloat(targetAchievementProgress1.getProgressValueTargetAchievement());
                        targetAchievementProgressView.setProgress(finalPercent);

                    } else {
                        float finalPercent = Float.parseFloat("1.0");
                        targetAchievementProgressView.setProgress(finalPercent);

                    }


                }


            }

            mDialog.dismiss();
        }
    }*/

    @SuppressLint("StaticFieldLeak")
    class checkDsrStatusPreviousDay extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(HomeActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "check_dsr_status_previous_day"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            //     nameValuePair.add(new BasicNameValuePair("date", params[1]));


            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        ArrayList<PreviousDateAttendance> previousDayDSRstatus = new ArrayList<>();

                        JSONArray reasonArray = object.getJSONArray("dateList");

                        for (int i = 0; i < reasonArray.length(); i++) {
                            JSONObject jobj = reasonArray.getJSONObject(i);
                            PreviousDateAttendance previousDateAttendance = new PreviousDateAttendance(jobj.getString("date"), jobj.getString("dsr_status"), jobj.getString("attendance"), jobj.getString("type"), jobj.getString("reason"));
                            previousDayDSRstatus.add(previousDateAttendance);
                        }
                        //If dsr status 0 it means report not submitted else 1 = submit also check attendance type=P or A

                        if (previousDayDSRstatus.size() != 0 && previousDayDSRstatus.get(0).getType().equalsIgnoreCase("F") && previousDayDSRstatus.get(0).getDsr_status().equalsIgnoreCase("0") && previousDayDSRstatus.get(0).getAttendance().equalsIgnoreCase("P")) {
                            returnFromDSR = true;
                            if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("") && offlineDateAdd.equalsIgnoreCase(currentDate()) && previousDayDSRstatus.get(0).getAttendance() != null && previousDayDSRstatus.get(0).getAttendance().equalsIgnoreCase("UA")) {
                                isUnApprove = true;
                            }

                            Toast.makeText(HomeActivity.this, previousDayDSRstatus.get(0).getDate() + " DSR report is in Pending ,Please Send Report First", Toast.LENGTH_SHORT).show();
                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_LIST_PREF, MODE_PRIVATE);
                            AttendanceReasonBean attendanceReasonBean = complexPreferences.getObject(Constant.ATTENDANCE_LIST_OBJ, AttendanceReasonBean.class);
                            attendanceReason = attendanceReasonBean.getAttendanceReason();
                            Intent in_navOut = new Intent(HomeActivity.this, PlaceOrderToDistributor.class);
                            in_navOut.putExtra("previousDayDate", previousDayDSRstatus.get(0).getDate());

                            if (attendanceReason != null) {
                                if (attendanceReason.equalsIgnoreCase("")) {
                                    in_navOut.putExtra("attendanceReason", previousDayDSRstatus.get(0).getReason());

                                } else {
                                    in_navOut.putExtra("attendanceReason", attendanceReason);

                                }
                            } else {
                                in_navOut.putExtra("attendanceReason", previousDayDSRstatus.get(0).getReason());
                            }
                            startActivity(in_navOut);
                        } else if (previousDayDSRstatus.size() != 0 && previousDayDSRstatus.get(0).getType().equalsIgnoreCase("O") && previousDayDSRstatus.get(0).getDsr_status().equalsIgnoreCase("-1") && previousDayDSRstatus.get(0).getAttendance().equalsIgnoreCase("PA") && previousDayDSRstatus.get(0).getReason().equalsIgnoreCase("Institutional / Work From Home")) {
                            returnFromDSR = true;
                            if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("") && offlineDateAdd.equalsIgnoreCase(currentDate()) && previousDayDSRstatus.get(0).getAttendance() != null && previousDayDSRstatus.get(0).getAttendance().equalsIgnoreCase("UA")) {
                                isUnApprove = true;
                            }
                            Toast.makeText(HomeActivity.this, previousDayDSRstatus.get(0).getDate() + " DSR report is in Pending ,Please Send Report First", Toast.LENGTH_SHORT).show();
                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_LIST_PREF, MODE_PRIVATE);
                            AttendanceReasonBean attendanceReasonBean = complexPreferences.getObject(Constant.ATTENDANCE_LIST_OBJ, AttendanceReasonBean.class);
                            attendanceReason = attendanceReasonBean.getAttendanceReason();

                            Intent in_navOut = new Intent(HomeActivity.this, PlaceOrderToDistributor.class);
                            in_navOut.putExtra("previousDayDate", previousDayDSRstatus.get(0).getDate());
                            if (attendanceReason != null) {
                                if (attendanceReason.equalsIgnoreCase("")) {
                                    in_navOut.putExtra("attendanceReason", previousDayDSRstatus.get(0).getReason());

                                } else {
                                    in_navOut.putExtra("attendanceReason", attendanceReason);

                                }
                            } else {
                                in_navOut.putExtra("attendanceReason", previousDayDSRstatus.get(0).getReason());

                            }
                                  /*  Bundle bundle = new Bundle();
                                    bundle.putSerializable("arrayListPreviousDate",  previousDayAttendanceListToDistributor);
                                    in_navOut.putExtras(bundle);*/
                            startActivity(in_navOut);
                            //startActivityForResult(in_navOut, STATIC_INTEGER_VALUE);
                        } else {
                            if (isUnApprove) {
                                isUnApprove = false;
                                if (isFromTab.equalsIgnoreCase("Field Work")) {

                                    if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                        if (db.getAttendanceDate(offlineDateAdd).size() > 0) {

                                            if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("F"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                                    db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                                    if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("F")) {
                                                        offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                        isFromTab = "Field Work";
                                                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                    }
                                                }
                                            } else {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {

                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), offlineDateAdd, getAttendanceReasonData("F").get(0).getAttendanceId(), db.getAttendancerowid());
                                                }
                                            }
                                        } else {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), currentDate(), getAttendanceReasonData("F").get(0).getAttendanceId(), rowId);
                                        }
                                    } else {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), currentDate(), getAttendanceReasonData("F").get(0).getAttendanceId(), rowId);

                                    }
                                } else if (isFromTab.equalsIgnoreCase("Other Work")) {

                                    if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                        if (db.getAttendanceDate(offlineDateAdd).size() > 0) {

                                            if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                                    if (db.getAttendance().get(0).getAttendanceReason() != null && db.getAttendance().get(0).getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid(), db.getAttendance().get(0).getJointUserID(), db.getAttendance().get(0).getJointUserDesig());
                                                    } else {
                                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                                    }
                                                    db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                                    if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("O")) {
                                                        offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                        isFromTab = "Other Work";
                                                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                    }
                                                }
                                            } else {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {

                                                    if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());

                                                    } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());

                                                    } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), offlineDateAdd, institutionalArray.getAttendanceId(), rowId, JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                                    } else {
                                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());
                                                    }
                                                }
                                            }
                                        } else {
                                            if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);

                                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId(), rowId);

                                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId, JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                            } else {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                            }
                                        }
                                    } else {
                                        if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);

                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId(), rowId);

                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId, JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                        } else {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                        }
                                    }
                                } else if (isFromTab.equalsIgnoreCase("Leave")) {
                                    if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                        if (db.getAttendanceDate(offlineDateAdd).size() > 0) {

                                            if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("L"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                                    db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                                    if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("L")) {
                                                        offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                        isFromTab = "Leave";
                                                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                    }
                                                }
                                            } else {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {

                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), offlineDateAdd, getAttendanceReasonData("L").get(0).getAttendanceId(), db.getAttendancerowid());
                                                }
                                            }
                                        } else {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), currentDate(), getAttendanceReasonData("L").get(0).getAttendanceId(), rowId);

                                        }
                                    } else {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), currentDate(), getAttendanceReasonData("L").get(0).getAttendanceId(), rowId);

                                    }


                                } else if (isFromTab.equalsIgnoreCase("Off")) {
                                    if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                        if (db.getAttendanceDate(offlineDateAdd).size() > 0) {

                                            if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("Off"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                                    db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                                    if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("Off")) {
                                                        offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                        isFromTab = "Off";
                                                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                    }
                                                }
                                            } else {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {

                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());
                                                }
                                            }
                                        } else {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                        }
                                    } else {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                    }
                                }
                            } else if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("") && offlineDateAdd.equalsIgnoreCase(currentDate()) && previousDayDSRstatus.get(0).getAttendance() != null && previousDayDSRstatus.get(0).getAttendance().equalsIgnoreCase("UA")) {
                                if (isFromTab.equalsIgnoreCase("Field Work")) {

                                    if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                        if (db.getAttendanceDate(offlineDateAdd).size() > 0) {

                                            if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("F"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                                    db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                                    if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("F")) {
                                                        offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                        isFromTab = "Field Work";
                                                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                    }
                                                }
                                            } else {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {

                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), offlineDateAdd, getAttendanceReasonData("F").get(0).getAttendanceId(), db.getAttendancerowid());
                                                }
                                            }
                                        } else {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), currentDate(), getAttendanceReasonData("F").get(0).getAttendanceId(), rowId);
                                        }
                                    } else {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), currentDate(), getAttendanceReasonData("F").get(0).getAttendanceId(), rowId);

                                    }
                                } else if (isFromTab.equalsIgnoreCase("Other Work")) {

                                    if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                        if (db.getAttendanceDate(offlineDateAdd).size() > 0) {

                                            if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                                    if (db.getAttendance().get(0).getAttendanceReason() != null && db.getAttendance().get(0).getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid(), db.getAttendance().get(0).getJointUserID(), db.getAttendance().get(0).getJointUserDesig());
                                                    } else {
                                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                                    }
                                                    db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                                    if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("O")) {
                                                        offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                        isFromTab = "Other Work";
                                                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                    }
                                                }
                                            } else {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {

                                                    if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());

                                                    } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());

                                                    } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), offlineDateAdd, institutionalArray.getAttendanceId(), rowId, JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                                    } else {
                                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());
                                                    }
                                                }
                                            }
                                        } else {
                                            if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);

                                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId(), rowId);

                                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId, JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                            } else {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                            }
                                        }
                                    } else {
                                        if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);

                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId(), rowId);

                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId, JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                        } else {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                        }
                                    }
                                } else if (isFromTab.equalsIgnoreCase("Leave")) {
                                    if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                        if (db.getAttendanceDate(offlineDateAdd).size() > 0) {

                                            if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("L"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                                    db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                                    if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("L")) {
                                                        offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                        isFromTab = "Leave";
                                                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                    }
                                                }
                                            } else {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {

                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), offlineDateAdd, getAttendanceReasonData("L").get(0).getAttendanceId(), db.getAttendancerowid());
                                                }
                                            }
                                        } else {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), currentDate(), getAttendanceReasonData("L").get(0).getAttendanceId(), rowId);

                                        }
                                    } else {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), currentDate(), getAttendanceReasonData("L").get(0).getAttendanceId(), rowId);

                                    }


                                } else if (isFromTab.equalsIgnoreCase("Off")) {
                                    if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                        if (db.getAttendanceDate(offlineDateAdd).size() > 0) {

                                            if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("Off"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                                    db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                                    if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("Off")) {
                                                        offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                        isFromTab = "Off";
                                                        new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                    }
                                                }
                                            } else {
                                                if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {

                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());
                                                }
                                            }
                                        } else {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), currentDate(), institutionalArray.getAttendanceId(), rowId);

                                        }
                                    } else {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                    }
                                }
                            } else {
                                if (isFromTab.equalsIgnoreCase("Field Work")) {
                                    if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                        if (db.getAttendanceDate(offlineDateAdd).size() > 0) {
                                            if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("F"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId());
                                                db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());

                                                if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("F")) {
                                                    offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                    isFromTab = "Field Work";
                                                    new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                }
                                            }
                                        } else {
                                            if (isPreviousDayAvailable) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), dateToAddAttendance, getAttendanceReasonData("F").get(0).getAttendanceId());

                                            } else {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), currentDate(), getAttendanceReasonData("F").get(0).getAttendanceId());

                                            }
                                        }
                                    } else {
                                        if (isPreviousDayAvailable) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), dateToAddAttendance, getAttendanceReasonData("F").get(0).getAttendanceId());

                                        } else {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), currentDate(), getAttendanceReasonData("F").get(0).getAttendanceId());

                                        }
                                    }
                                } else if (isFromTab.equalsIgnoreCase("Other Work")) {
                                    if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                        if (db.getAttendanceDate(offlineDateAdd).size() > 0) {
/*
                                            if (getAttendanceReasonDataByTypeId(db.getAttendance().get(0).getAttendanceTypeId()) != null && getAttendanceReasonDataByTypeId(db.getAttendance().get(0).getAttendanceTypeId()).get(0).getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
*/
                                            if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                                if (db.getAttendance().get(0).getAttendanceReason() != null && db.getAttendance().get(0).getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid(), db.getAttendance().get(0).getJointUserID(), db.getAttendance().get(0).getJointUserDesig());
                                                } else {
                                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId());
                                                }
                                           /* } else {
                                                new markAttendance().execute(mUserLoginInfoBean.getUser_id(), db.getAttendance().get(0).getAttendanceType(), "0", getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId());
                                            }*/
                                                // new markAttendance().execute(mUserLoginInfoBean.getUser_id(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), offlineDateAdd, getAttendanceReasonData("L").get(0).getAttendanceId());
                                                db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());

                                                if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("O")) {
                                                    offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                    isFromTab = "Other Work";
                                                    new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                }
                                            }

                                        } else {
                                            if (isPreviousDayAvailable) {
                                                if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                                                } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), dateToAddAttendance, institutionalArray.getAttendanceId());
                                                } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId(), JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                                } else {
                                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                                                }

                                            } else {

                                                if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId());

                                                } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId());

                                                } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                                } else {
                                                    new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId());

                                                }


                                            }
                                        }
                                    } else {
                                        if (isPreviousDayAvailable) {
                                            if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), dateToAddAttendance, institutionalArray.getAttendanceId());
                                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId(), JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                            } else {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                                            }

                                        } else {

                                            if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId());

                                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId());

                                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                            } else {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId());

                                            }
                                        }
                                    }
                                } else if (isFromTab.equalsIgnoreCase("Leave")) {
                                    if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                        if (db.getAttendanceDate(offlineDateAdd).size() > 0) {
                                            if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("L"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId());
                                                db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());

                                                if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("L")) {
                                                    offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                    isFromTab = "Leave";
                                                    new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                }
                                            }
                                        } else {
                                            if (isPreviousDayAvailable) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), dateToAddAttendance, getAttendanceReasonData("L").get(0).getAttendanceId());

                                            } else {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), currentDate(), getAttendanceReasonData("L").get(0).getAttendanceId());
                                            }
                                        }
                                    } else {
                                        if (isPreviousDayAvailable) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), dateToAddAttendance, getAttendanceReasonData("L").get(0).getAttendanceId());

                                        } else {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), currentDate(), getAttendanceReasonData("L").get(0).getAttendanceId());
                                        }
                                    }
                                } else if (isFromTab.equalsIgnoreCase("Off")) {
                                    if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                        if (db.getAttendanceDate(offlineDateAdd).size() > 0) {
                                            if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("Off"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId());
                                                db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());

                                                if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("Off")) {
                                                    offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                    isFromTab = "Off";
                                                    new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                }
                                            }
                                        } else {
                                            if (isPreviousDayAvailable) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), dateToAddAttendance, institutionalArray.getAttendanceId());

                                            } else {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), currentDate(), institutionalArray.getAttendanceId());
                                            }
                                        }
                                    } else {
                                        if (isPreviousDayAvailable) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), dateToAddAttendance, institutionalArray.getAttendanceId());

                                        } else {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), currentDate(), institutionalArray.getAttendanceId());
                                        }
                                    }


                                }

                            }
                        }

                    } else {
                        if (isUnApprove) {
                            isUnApprove = false;
                            if (isFromTab.equalsIgnoreCase("Field Work")) {

                                if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                    if (db.getAttendanceDate(offlineDateAdd).size() > 0) {

                                        if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                            if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("F"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                                db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                                if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("F")) {
                                                    offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                    isFromTab = "Field Work";
                                                    new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                }
                                            }
                                        } else {
                                            if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {

                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), offlineDateAdd, getAttendanceReasonData("F").get(0).getAttendanceId(), db.getAttendancerowid());
                                            }
                                        }
                                    } else {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), currentDate(), getAttendanceReasonData("F").get(0).getAttendanceId(), rowId);
                                    }
                                } else {
                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), currentDate(), getAttendanceReasonData("F").get(0).getAttendanceId(), rowId);

                                }
                            } else if (isFromTab.equalsIgnoreCase("Other Work")) {

                                if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                    if (db.getAttendanceDate(offlineDateAdd).size() > 0) {

                                        if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                            if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                                if (db.getAttendance().get(0).getAttendanceReason() != null && db.getAttendance().get(0).getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid(), db.getAttendance().get(0).getJointUserID(), db.getAttendance().get(0).getJointUserDesig());
                                                } else {
                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                                }
                                                db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                                if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("O")) {
                                                    offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                    isFromTab = "Other Work";
                                                    new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                }
                                            }
                                        } else {
                                            if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {

                                                if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());

                                                } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());

                                                } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), offlineDateAdd, institutionalArray.getAttendanceId(), rowId, JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                                } else {
                                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());
                                                }
                                            }
                                        }
                                    } else {
                                        if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);

                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId(), rowId);

                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId, JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                        } else {
                                            new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                        }
                                    }
                                } else {
                                    if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);

                                    } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                    } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId, JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                    } else {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                    }
                                }
                            } else if (isFromTab.equalsIgnoreCase("Leave")) {
                                if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                    if (db.getAttendanceDate(offlineDateAdd).size() > 0) {

                                        if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                            if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("L"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                                db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                                if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("L")) {
                                                    offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                    isFromTab = "Leave";
                                                    new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                }
                                            }
                                        } else {
                                            if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {

                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), offlineDateAdd, getAttendanceReasonData("L").get(0).getAttendanceId(), db.getAttendancerowid());
                                            }
                                        }
                                    } else {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), currentDate(), getAttendanceReasonData("L").get(0).getAttendanceId(), rowId);

                                    }
                                } else {
                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), currentDate(), getAttendanceReasonData("L").get(0).getAttendanceId(), rowId);

                                }


                            } else if (isFromTab.equalsIgnoreCase("Off")) {
                                if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                    if (db.getAttendanceDate(offlineDateAdd).size() > 0) {

                                        if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                            if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {
                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(),
                                                        db.getAttendance().get(0).getDsrStatus(), getAttendance("Off"), offlineDateAdd,
                                                        db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendancerowid());
                                                db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());
                                                if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("Off")) {
                                                    offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                    isFromTab = "Off";
                                                    new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                                }
                                            }
                                        } else {
                                            if (db.getAttendancerowid() != null && !db.getAttendancerowid().equalsIgnoreCase("")) {

                                                new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), offlineDateAdd, institutionalArray.getAttendanceId(), db.getAttendancerowid());
                                            }
                                        }
                                    } else {
                                        new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), currentDate(), institutionalArray.getAttendanceId(), rowId);

                                    }
                                } else {
                                    new markAttendanceUpdate().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), currentDate(), institutionalArray.getAttendanceId(), rowId);
                                }
                            }

                        } else {
                            if (isFromTab.equalsIgnoreCase("Field Work")) {
                                if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                    if (db.getAttendanceDate(offlineDateAdd).size() > 0) {
                                        if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("F"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId());
                                            db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());

                                            if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("F")) {
                                                offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                isFromTab = "Field Work";
                                                new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                            }
                                        }
                                    } else {
                                        if (isPreviousDayAvailable) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), dateToAddAttendance, getAttendanceReasonData("F").get(0).getAttendanceId());

                                        } else {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), currentDate(), getAttendanceReasonData("F").get(0).getAttendanceId());

                                        }
                                    }
                                } else {
                                    if (isPreviousDayAvailable) {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), dateToAddAttendance, getAttendanceReasonData("F").get(0).getAttendanceId());

                                    } else {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("F").get(0).getAttendanceType(), "0", getAttendance("F"), currentDate(), getAttendanceReasonData("F").get(0).getAttendanceId());

                                    }
                                }
                            } else if (isFromTab.equalsIgnoreCase("Other Work")) {
                                if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                    if (db.getAttendanceDate(offlineDateAdd).size() > 0) {
/*
                                            if (getAttendanceReasonDataByTypeId(db.getAttendance().get(0).getAttendanceTypeId()) != null && getAttendanceReasonDataByTypeId(db.getAttendance().get(0).getAttendanceTypeId()).get(0).getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
*/
                                        if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                            if (db.getAttendance().get(0).getAttendanceReason() != null && db.getAttendance().get(0).getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId(), db.getAttendance().get(0).getJointUserID(), db.getAttendance().get(0).getJointUserDesig());
                                            } else {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId());
                                            }
                                           /* } else {
                                                new markAttendance().execute(mUserLoginInfoBean.getUser_id(), db.getAttendance().get(0).getAttendanceType(), "0", getAttendance(db.getAttendance().get(0).getAttendanceType()), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId());
                                            }*/
                                            // new markAttendance().execute(mUserLoginInfoBean.getUser_id(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), offlineDateAdd, getAttendanceReasonData("L").get(0).getAttendanceId());
                                            db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());

                                            if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("O")) {
                                                offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                isFromTab = "Other Work";
                                                new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                            }
                                        }

                                    } else {
                                        if (isPreviousDayAvailable) {
                                            if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), dateToAddAttendance, institutionalArray.getAttendanceId());
                                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId(), JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                            } else {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                                            }

                                        } else {

                                            if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId());
                                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId());
                                            } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                            } else {
                                                new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId());
                                            }


                                        }
                                    }
                                } else {
                                    if (isPreviousDayAvailable) {
                                        if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), dateToAddAttendance, institutionalArray.getAttendanceId());
                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId(), JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                        } else {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), dateToAddAttendance, institutionalArray.getAttendanceId());
                                        }
                                    } else {

                                        if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Institutional / Work From Home")) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "-1", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId());

                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Store Visit (MT)")) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance("F"), currentDate(), institutionalArray.getAttendanceId());

                                        } else if (institutionalArray.getAttendanceReason() != null && institutionalArray.getAttendanceReason().equalsIgnoreCase("Joint Work")) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId(), JointWork_With.getUserID().toString(), JointWork_With.getDesig().toString());
                                        } else {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.attendanceType, "0", getAttendance(institutionalArray.attendanceType), currentDate(), institutionalArray.getAttendanceId());
                                        }
                                    }
                                }
                            } else if (isFromTab.equalsIgnoreCase("Leave")) {
                                if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                    if (db.getAttendanceDate(offlineDateAdd).size() > 0) {
                                        if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("L"), offlineDateAdd, db.getAttendance().get(0).getAttendanceTypeId());
                                            db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());

                                            if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("L")) {
                                                offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                isFromTab = "Leave";
                                                new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                            }
                                        }
                                    } else {
                                        if (isPreviousDayAvailable) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), dateToAddAttendance, getAttendanceReasonData("L").get(0).getAttendanceId());
                                        } else {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), currentDate(), getAttendanceReasonData("L").get(0).getAttendanceId());
                                        }
                                    }
                                } else {
                                    if (isPreviousDayAvailable) {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), dateToAddAttendance, getAttendanceReasonData("L").get(0).getAttendanceId());

                                    } else {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), getAttendanceReasonData("L").get(0).getAttendanceType(), "0", getAttendance("L"), currentDate(), getAttendanceReasonData("L").get(0).getAttendanceId());
                                    }
                                }
                            } else if (isFromTab.equalsIgnoreCase("Off")) {
                                if (offlineDateAdd != null && !offlineDateAdd.equalsIgnoreCase("")) {
                                    if (db.getAttendanceDate(offlineDateAdd).size() > 0) {
                                        if (db.getAttendance() != null && db.getAttendance().size() != 0) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), db.getAttendance().get(0).getDsrStatus(), getAttendance("Off"), offlineDateAdd, institutionalArray.getAttendanceId());
                                            db.deleteAttendance(db.getAttendanceDate(offlineDateAdd).get(0).getKeyId());

                                            if (db.getAttendance() != null && db.getAttendance().size() != 0 && db.getAttendance().get(0).getAttendanceType().equalsIgnoreCase("Off")) {
                                                offlineDateAdd = db.getAttendance().get(0).getAttendanceDate();
                                                isFromTab = "Off";
                                                new checkDsrStatusPreviousDay().execute(mUserLoginInfoBean.getUserId(), db.getAttendance().get(0).getAttendanceDate());
                                            }
                                        }
                                    } else {
                                        if (isPreviousDayAvailable) {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), dateToAddAttendance, institutionalArray.getAttendanceId());

                                        } else {
                                            new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), currentDate(), institutionalArray.getAttendanceId());
                                        }
                                    }
                                } else {
                                    if (isPreviousDayAvailable) {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), dateToAddAttendance, institutionalArray.getAttendanceId());
                                    } else {
                                        new markAttendance().execute(mUserLoginInfoBean.getUserId(), institutionalArray.getAttendanceType(), "0", getAttendance("Off"), currentDate(), institutionalArray.getAttendanceId());
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(HomeActivity.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(HomeActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }

    }

    @SuppressLint("StaticFieldLeak")
    class getAttendanceStatus extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(HomeActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "check_attendance_status_by_manager"));
            nameValuePair.add(new BasicNameValuePair("user_id", mUserLoginInfoBean.getUserId()));
            nameValuePair.add(new BasicNameValuePair("date", currentDate()));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        attendanceTypeStatus = object.getString("attendanceType");
                        attendanceReason = object.getString("attendanceReason");
                        AttendanceReasonBean attendanceReasonBean = new AttendanceReasonBean();
                        attendanceReasonBean.setAttendanceReason(attendanceReason);
                        attendanceReasonBean.setAttendanceType(attendanceTypeStatus);
                        ComplexPreferences attendancePref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_LIST_PREF, MODE_PRIVATE);
                        attendancePref.putObject(Constant.ATTENDANCE_LIST_OBJ, attendanceReasonBean);
                        attendancePref.commit();
                        object.getString("message");
                        if (!object.getString("message").equalsIgnoreCase("")) {

                            //tvUpdateAttendanceStatus.setVisibility(View.VISIBLE);
                            tvUpdateAttendanceStatus.setText(object.getString("message"));
                            db.addAttendanceStatus(attendanceReason, attendanceTypeStatus, object.getString("message"), mUserLoginInfoBean.getUserId(), currentDate());
                            checkSession();
                        } else {

                            tvUpdateAttendanceStatus.setVisibility(View.GONE);
                            db.addAttendanceStatus(attendanceReason, attendanceTypeStatus, "", mUserLoginInfoBean.getUserId(), currentDate());

                        }
                    } else {
                        AttendanceReasonBean attendanceReasonBean = new AttendanceReasonBean();
                        attendanceReasonBean.setAttendanceReason("");
                        attendanceReasonBean.setAttendanceType("");
                        ComplexPreferences attendancePref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_LIST_PREF, MODE_PRIVATE);
                        attendancePref.putObject(Constant.ATTENDANCE_LIST_OBJ, attendanceReasonBean);
                        attendancePref.commit();
                        tvUpdateAttendanceStatus.setVisibility(View.GONE);
                        db.addAttendanceStatus("", "", "", mUserLoginInfoBean.getUserId(), currentDate());
                    }
                    mDialog.dismiss();
                } catch (JSONException e) {

                    Toast.makeText(HomeActivity.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }

            } else {
                db.addAttendanceStatus("", "", "", mUserLoginInfoBean.getUserId(), currentDate());
                mDialog.dismiss();
                Toast.makeText(HomeActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            } //Check today attendance

            if (Utils.isInternetConnected(HomeActivity.this)) {

                AttendanceReasonBean getAttendanceStatus = db.getAttendanceStatusHome(currentDate(), mUserLoginInfoBean.getUserId());
                if (!getAttendanceStatus.attendanceReason.equalsIgnoreCase("") && !getAttendanceStatus.attendanceType.equalsIgnoreCase("")) {


                    String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                    //If current day false but if its last date is already added to sync then need both dialog and sync

                    final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                    if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {

                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);
                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                            }
                            if (!datesInAttendanceList.contains(currentDate())) {
                                attendanceReasonBeanArrayList = new ArrayList<>();
                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();
                                dialogAttendance();
                            } else {
                                db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());
                            }
                        } else {
                            llSync.setVisibility(View.GONE);
                            new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUserId(), currentDate());

                        }

                    } else {

                        if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("false")) {

                            if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                llSync.setVisibility(View.VISIBLE);
                                ArrayList<String> datesInAttendanceList = new ArrayList<>();

                                for (int i = 0; i < attendanceBeans.size(); i++) {
                                    datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                                }
                                if (!datesInAttendanceList.contains(currentDate())) {
                                    attendanceReasonBeanArrayList = new ArrayList<>();

                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                    dialogAttendance();
                                } else {
                                    db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                    db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                                }
                            } else {
                                new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUserId(), currentDate());

                                llSync.setVisibility(View.GONE);

                            }


                        } else {

                            if (attendanceBeans != null && attendanceBeans.size() > 0) {
                                llSync.setVisibility(View.VISIBLE);
                                ArrayList<String> datesInAttendanceList = new ArrayList<>();

                                for (int i = 0; i < attendanceBeans.size(); i++) {
                                    datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                                }
                                if (!datesInAttendanceList.contains(currentDate())) {
                                    attendanceReasonBeanArrayList = new ArrayList<>();

                                    attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                    dialogAttendance();
                                }
                            } else {
                                llSync.setVisibility(View.GONE);

                                new unApprovedStatus().execute();

                            }
                        }
                    }
                } else {


                    final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                    if (attendanceBeans != null && attendanceBeans.size() > 0) {
                        llSync.setVisibility(View.VISIBLE);
                        ArrayList<String> datesInAttendanceList = new ArrayList<>();

                        for (int i = 0; i < attendanceBeans.size(); i++) {
                            datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                        }
                        if (!datesInAttendanceList.contains(currentDate())) {

                            attendanceReasonBeanArrayList = new ArrayList<>();
                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        } else {
                            db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                            db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                        }
                    } else {
                        llSync.setVisibility(View.GONE);
                        db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                        new isTodayAttendanceMarked().execute(mUserLoginInfoBean.getUserId(), currentDate());
                    }
                }
            } else {


                String isTodayAttendanceMarked = db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId());
                //If current day false but if its last date is already added to sync then need both dialog and sync
                final ArrayList<AttendanceBean> attendanceBeans = db.getAttendance();
                if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("")) {

                    if (attendanceBeans != null && attendanceBeans.size() > 0) {
                        llSync.setVisibility(View.VISIBLE);
                        ArrayList<String> datesInAttendanceList = new ArrayList<>();

                        for (int i = 0; i < attendanceBeans.size(); i++) {
                            datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                        }
                        if (!datesInAttendanceList.contains(currentDate())) {
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        } else {
                            db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                            db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                        }
                    } else {
                        llSync.setVisibility(View.GONE);
                        attendanceReasonBeanArrayList = new ArrayList<>();

                        attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                        dialogAttendance();
                    }

                } else {

                    if (isTodayAttendanceMarked != null && isTodayAttendanceMarked.equalsIgnoreCase("false")) {

                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);
                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                            }
                            if (!datesInAttendanceList.contains(currentDate())) {
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                            } else {
                                db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());

                            }
                        } else {
                            llSync.setVisibility(View.GONE);
                            attendanceReasonBeanArrayList = new ArrayList<>();

                            attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                            dialogAttendance();
                        }

                    } else {

                        if (attendanceBeans != null && attendanceBeans.size() > 0) {
                            llSync.setVisibility(View.VISIBLE);
                            ArrayList<String> datesInAttendanceList = new ArrayList<>();

                            for (int i = 0; i < attendanceBeans.size(); i++) {
                                datesInAttendanceList.add(attendanceBeans.get(i).getAttendanceDate());
                            }
                            if (!datesInAttendanceList.contains(currentDate())) {
                                attendanceReasonBeanArrayList = new ArrayList<>();

                                attendanceReasonBeanArrayList = db.getAttendanceReasonList();

                                dialogAttendance();
                            }
                        } else {
                            llSync.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    class unApprovedStatus extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(HomeActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {

            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "unapproved_by_manager"));
            nameValuePair.add(new BasicNameValuePair("user_id", mUserLoginInfoBean.getUserId()));
            nameValuePair.add(new BasicNameValuePair("date", currentDate()));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);

        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        if (object.getBoolean("unapproved")) {
                            mDialog.dismiss();
                            rowId = object.getString("rowId");
                            db.saveRowIdOnUpdate(mUserLoginInfoBean.getUserId(), rowId);
                            showConfirmDialog(null, "unapprove");
                        } else {
                            mDialog.dismiss();
                        }
                    } else {
                        mDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Toast.makeText(HomeActivity.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    e.printStackTrace();
                }
            } else {
                mDialog.dismiss();
                Toast.makeText(HomeActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            } //Check today attendance
        }
    }

    @SuppressLint("StaticFieldLeak")
    class markAttendanceUpdate extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(HomeActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();

            nameValuePair.add(new BasicNameValuePair("method", "mark_attendance_update"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("attendance_type", params[1]));
            nameValuePair.add(new BasicNameValuePair("dsr_status", params[2]));
            nameValuePair.add(new BasicNameValuePair("attendance", params[3]));
            nameValuePair.add(new BasicNameValuePair("attendance_date", params[4]));
            nameValuePair.add(new BasicNameValuePair("attendance_type_Id", params[5]));
            nameValuePair.add(new BasicNameValuePair("rowId", params[6]));
            if (params[1].equals("O") && params[5].equals("5")) {
                nameValuePair.add(new BasicNameValuePair("joint_user_id", params[7]));
                nameValuePair.add(new BasicNameValuePair("joint_designation_id", params[8]));
            }

//            return new HttpUrlConnecti9 = {BasicNameValuePair@18657} "joint_designation_id=4"onJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL3, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);

        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        Toast.makeText(HomeActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        if (!isPreviousDayAvailable) {
                            if (Utils.isInternetConnected(HomeActivity.this)) {
                                db.deleteAllIsTodayAttendanceMarkeds(mUserLoginInfoBean.getUserId());
                                db.addIsTodayAttendanceMarkeds("true", currentDate(), mUserLoginInfoBean.getUserId());
                                new getAttendanceStatus().execute();
                                if (db.getAttendanceDate(currentDate()).size() == 0) {
                                    llSync.setVisibility(View.GONE);
                                }
                            } else {
                                //Attendance status
                                AttendanceReasonBean attendanceReasonBean = db.getAttendanceStatusHome(currentDate(), mUserLoginInfoBean.getUserId());
                                ComplexPreferences attendancePref = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_LIST_PREF, MODE_PRIVATE);
                                attendancePref.putObject(Constant.ATTENDANCE_LIST_OBJ, attendanceReasonBean);
                                attendancePref.commit();
                                if (attendanceReasonBean.getAttendanceMessage() != null && !attendanceReasonBean.getAttendanceMessage().equalsIgnoreCase("")) {
                                    // tvUpdateAttendanceStatus.setVisibility(View.VISIBLE);
                                    tvUpdateAttendanceStatus.setText(attendanceReasonBean.getAttendanceMessage());
                                } else {
                                    tvUpdateAttendanceStatus.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    Toast.makeText(HomeActivity.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            } else {
                Toast.makeText(HomeActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }

    }

    @SuppressLint("StaticFieldLeak")
    class focusedProduct extends AsyncTask<String, Void, JSONObject> {
        // Dialog mDialog;

        @Override
        protected void onPreExecute() {

        /*    mDialog = new Dialog(HomeActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();*/

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList();
            nameValuePair.add(new BasicNameValuePair("method", "focused_product"));
            nameValuePair.add(new BasicNameValuePair("asm_id", mUserLoginInfoBean.getAsmId()));
            nameValuePair.add(new BasicNameValuePair("month", params[0]));
            nameValuePair.add(new BasicNameValuePair("year", params[1]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        //check previous days attendance, from server get last attendance_date  entry of user
                        ArrayList<FocusedProductBean> focusedProductListArray = new ArrayList<>();

                        JSONArray focusedProductList = object.getJSONArray("focusedProductList");
                        String arrayFocusedResult = "";
                        for (int i = 0; i < focusedProductList.length(); i++) {
                            JSONObject jsonObject = focusedProductList.getJSONObject(i);
                            String productName = jsonObject.getString("prod_name");
                            String month = jsonObject.getString("month");
                            String year = jsonObject.getString("year");

                            FocusedProductBean focusedProductBean = new FocusedProductBean(productName, month, year);
                            focusedProductListArray.add(focusedProductBean);

                        }
                        for (int i = 0; i < focusedProductListArray.size(); i++) {

                            if (i == focusedProductListArray.size() - 1) {
                                arrayFocusedResult = arrayFocusedResult.concat(focusedProductListArray.get(i).getProductName());

                            } else {
                                arrayFocusedResult = arrayFocusedResult.concat(focusedProductListArray.get(i).getProductName() + " , ");

                            }
                        }
                        if (db.getFocusedProduct(String.valueOf(month()), String.valueOf(year()), mUserLoginInfoBean.getUserId()).size() == 0) {
                            db.addFocusedProduct(focusedProductListArray, mUserLoginInfoBean.getUserId());
                        }


                        tvFocused.setText("Focus Product Of the Month - " + arrayFocusedResult);


                    } else {


                        // tvFocused.setText("No Focus Product Of the Month                  \t\t\t\t\t\t\t\t\t\t\t        ");
                        tvFocused.setText("Please Clock-In & Clock-Out Timely to mark Your Attendance.                  \t\t\t\t\t\t\t\t\t\t\t        ");
                        //  Toast.makeText(HomeActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    tvFocused.setSelected(true);
                } catch (JSONException e) {

                    Toast.makeText(HomeActivity.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            } else {
                Toast.makeText(HomeActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            //mDialog.dismiss();
        }

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    public boolean dateOfTheMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (maxDay == 31) {
            DateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
            Date date = new Date();
            String dateOfTheMonth = dateFormat.format(date);
            int dateMonth = Integer.parseInt(dateOfTheMonth);
            return dateMonth >= 29;
        } else if (maxDay == 30) {
            DateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
            Date date = new Date();
            String dateOfTheMonth = dateFormat.format(date);
            int dateMonth = Integer.parseInt(dateOfTheMonth);
            return dateMonth >= 28;
        } else if (maxDay < 30) {
            DateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
            Date date = new Date();
            String dateOfTheMonth = dateFormat.format(date);
            int dateMonth = Integer.parseInt(dateOfTheMonth);
            return dateMonth >= 26;
        }
        return false;
    }

    /**
     * Check Clock-In/Clock-Out Session
     */

    void checkSession() {


        reminderView.setVisibility(View.GONE);

        BackgroundWork back = new BackgroundWork(this);
        back.execute("Check Session");
        Objects.requireNonNull(back.getDailog()).setOnDismissListener(dialog -> {
            String result = back.getResult();
            if (result.equals("No session Found") || result.isEmpty()) {
                session.clearSession();
                showAttendanceReminder();
            } else if (result.equals("User Deactivated")) {

                logout();

            } else {

                JSONObject j;
                try {

                    j = new JSONObject(back.getResult()).getJSONObject("response");

                    String time_in = j.getString("Time_IN");
                    String time_out = j.getString("Time_OUT");
                    String status = j.getString("Status");
                    session.createClockInSession(time_in, status);

                    if (!time_out.equals("null")) {
                        session.createClockOutSession(time_out, status);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Session", Objects.requireNonNull(e.getMessage()));
                }
            }
            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(HomeActivity.this, Constant.ATTENDANCE_LIST_PREF, MODE_PRIVATE);
            AttendanceReasonBean attendanceReasonBean = complexPreferences.getObject(Constant.ATTENDANCE_LIST_OBJ, AttendanceReasonBean.class);
            attendanceReason = attendanceReasonBean.getAttendanceReason();
            attendanceTypeStatus = attendanceReasonBean.getAttendanceType();

            if (attendanceTypeStatus != null && !(attendanceTypeStatus.equalsIgnoreCase("L") || attendanceTypeStatus.equalsIgnoreCase("off")) && !attendanceReason.equals("Market Closed")) {
                showAttendanceReminder();
            }

            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            if (db.getIsTodayAttendanceMarked(currentDate(), mUserLoginInfoBean.getUserId()).equalsIgnoreCase("true")) {
                if (session.getUserDetails().get(session.getKEY_ClockIn()) != null) {
                    try {
                        if (sd.format(Objects.requireNonNull(sd.parse(Objects.requireNonNull(session.getUserDetails().get(session.
                                getKEY_ClockIn()))))).equals(sd.format(Calendar.getInstance().getTime())) && session.
                                getUserDetails().get(session.getKEY_ClockOut()) != null) {
                            Toast.makeText(getApplicationContext(), "Already Checked Out For the Day.", Toast.LENGTH_LONG).show();
                        } else if (!sd.format(sd.parse(Objects.requireNonNull(session.getUserDetails().get(session.
                                getKEY_ClockIn())))).equals(sd.format(Calendar.getInstance().getTime())) && session.
                                getUserDetails().get(session.getKEY_ClockOut()) == null) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                            alert.setTitle("Clock Out")
                                    .setMessage("Clock out for " + new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(sd.parse(session.getUserDetails().get(session.getKEY_ClockIn()))) + " is Pending ")
                                    .setCancelable(false)
                                    .setPositiveButton("yes", (dialog1, which) -> startActivity(new Intent(HomeActivity.this, ClockInOut.class)))
                                    .show();

                        } else if (!sd.format(sd.parse(session.getUserDetails().get(session.
                                getKEY_ClockIn()))).equals(sd.format(Calendar.getInstance().getTime())) && session.
                                getUserDetails().get(session.getKEY_ClockOut()) != null) {

                            attendanceReason = attendanceReasonBean.getAttendanceReason();
                            attendanceTypeStatus = attendanceReasonBean.getAttendanceType();

                            if (attendanceTypeStatus != null && !(attendanceTypeStatus.equalsIgnoreCase("L") || attendanceTypeStatus.equalsIgnoreCase("off")) && !attendanceReason.equals("Market Closed")) {
                                startActivity(new Intent(HomeActivity.this, ClockInOut.class));
                            }


                        }
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    attendanceReason = attendanceReasonBean.getAttendanceReason();
                    attendanceTypeStatus = attendanceReasonBean.getAttendanceType();
                    if (attendanceTypeStatus != null && !(attendanceTypeStatus.equalsIgnoreCase("L") || attendanceTypeStatus.equalsIgnoreCase("off")) && !attendanceReason.equals("Market Closed")) {
                        startActivity(new Intent(getApplicationContext(), ClockInOut.class));
                    }
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    void showAttendanceReminder() {

        reminderView.setVisibility(View.VISIBLE);

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());


        if (session.getUserDetails().get(session.getKEY_ClockIn()) != null) {
            try {
                if (sd.format(sd.parse((session.getUserDetails().get(session.getKEY_ClockIn())))).equals(sd.format(Calendar.getInstance().getTime())) && session.getUserDetails().get(session.getKEY_ClockOut())
                        != null
                ) {
                    reminderView.setVisibility(View.GONE);
                } else if (
                        sd.format(sd.parse(session.getUserDetails().get(session.getKEY_ClockIn()))) != sd.format(Calendar.getInstance().getTime())
                                && session.getUserDetails().get(session.getKEY_ClockOut()) == "null"
                ) {
                    reminderText.setText("Clock-Out Pending for " + new SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                    ).format(
                            sd.parse((session.getUserDetails().get(session.getKEY_ClockIn())))
                    ));
                    inOut.setText("Clock-Out");

                } else if (
                        (!sd.format(sd.parse(session.getUserDetails().get(session.getKEY_ClockIn()))).equals(sd.format(Calendar.getInstance().getTime())))
                                && session.getUserDetails().get(session.getKEY_ClockOut()) != null
                ) {
                    reminderText.setText("Clock-In Pending for " + new SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                    ).format(
                            Calendar.getInstance().getTime()
                    ));


                    inOut.setText("Clock-In");

                } else if (
                        (sd.format(sd.parse(session.getUserDetails().get(session.getKEY_ClockIn()))).equals(sd.format(Calendar.getInstance().getTime())))
                                && session.getUserDetails().get(session.getKEY_ClockOut()) == null
                                && time.parse(time.format(Calendar.getInstance().getTime())).getTime()
                                > time.parse("17:29:59").getTime()
                ) {

                    reminderText.setText("Clock-Out Pending for " + new SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                    ).format(
                            sd.parse(session.getUserDetails().get(session.getKEY_ClockIn()))
                    ));
                    inOut.setText("Clock-Out");
                } else {
                    reminderView.setVisibility(View.GONE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            reminderText.setText("Clock-In Pending for " + new SimpleDateFormat(
                    "dd-MM-yyyy",
                    Locale.getDefault()
            ).format(
                    Calendar.getInstance().getTime()
            ));

            inOut.setText("Clock-In");

        }
    }

    @Override
    protected void onStart() {
        checkAppVersion();
        super.onStart();
        checkSession();
    }


    private final void checkAppVersion() {
        BackgroundWork back = new BackgroundWork(this);
        back.execute("Check App Version");
        AlertDialog alert = back.getDailog();
        alert.setOnDismissListener(it -> {
            JSONObject json;
            try {
                json = new JSONObject(back.getResult());

                if (json.getString("success").equals("true") && json.getInt("status code") == 200) {
                    Log.d("App Version", "Up To Date");
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("UPDATE REQUIRE.")
                            .setMessage("Updated version of application is Available on Play store")
                            .setCancelable(false)
                            .setNeutralButton("Ok", (d, i) -> finishAffinity())
                            .setPositiveButton("Update", (dialog, which) -> {
                                try {
                                    Intent viewIntent =
                                            new Intent("android.intent.action.VIEW",
                                                    Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                                    startActivity(viewIntent);
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Unable to Connect Try Again...",
                                            Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}