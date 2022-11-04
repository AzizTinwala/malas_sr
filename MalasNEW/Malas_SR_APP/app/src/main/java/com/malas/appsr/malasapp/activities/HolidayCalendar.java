package com.malas.appsr.malasapp.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Amitlibs.utils.ComplexPreferences;
import com.example.robotocalenderview.RobotoCalendarView;
import com.example.robotocalenderview.RobotoCalendarView.RobotoCalendarListener;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.BeanClasses.leave_monthly_report;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.HolidayAdapter;
import com.malas.appsr.malasapp.model.attendance_report;
import com.malas.appsr.malasapp.serverconnection.BackgroundWork;
import com.tooltip.Tooltip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HolidayCalendar extends AppCompatActivity implements RobotoCalendarListener {

    RobotoCalendarView holidayCalendar;
    RecyclerView holidayRecyclerView;
    HashMap<String, String> event = new HashMap<>();
    List<String> holiday_date = new ArrayList<>();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    HolidayAdapter holidayAdapter;
    ArrayList<attendance_report> attendance = new ArrayList<>();
    TextView present, weekoff, holiday, leave,absent;
    List<leave_monthly_report> leave_list = new ArrayList<>();
    private List<String> absent_list = new ArrayList();
    HashMap<String, Integer> weekdays = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_calendar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Holiday Calendar");

        holidayCalendar = findViewById(R.id.holiday);
        holidayCalendar.setRobotoCalendarListener(this);
        holidayRecyclerView = findViewById(R.id.holiday_summary);
        holidayRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        weekdays.put("SUNDAY", Calendar.SUNDAY);
        weekdays.put("MONDAY", Calendar.MONDAY);
        weekdays.put("TUESDAY", Calendar.TUESDAY);
        weekdays.put("WEDNESDAY", Calendar.WEDNESDAY);
        weekdays.put("THURSDAY", Calendar.THURSDAY);
        weekdays.put("FRIDAY", Calendar.FRIDAY);
        weekdays.put("SATURDAY", Calendar.SATURDAY);

        present = findViewById(R.id.calendar_present);
        weekoff = findViewById(R.id.calendar_weekoff);
        holiday = findViewById(R.id.calendar_holiday);
        leave = findViewById(R.id.calendar_leave);
        absent = findViewById(R.id.calendar_absent);

    }

    @Override
    public void onDayClick(Date date, View view) {
        showtooltip(
                view,
                event.get(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date))
        );
        getweekoff();
        markHoliday();
        markattendence();
        markleave();
        markAbsent();
        holidayCalendar.markDayAsSelectedDay(date, view);
    }

    @Override
    public void onDayLongClick(Date date) {

    }

    @Override
    public void onRightButtonClick() {
        getweekoff();
        getHoliday();
        getAttendance();
        getLeave();
        getAbsent();
    }

    @Override
    public void onLeftButtonClick() {
        getweekoff();
        getHoliday();
        getAttendance();
        getLeave();
        getAbsent();
    }

    private void showtooltip(View view, String msg) {
        if (msg != null) {
            Tooltip t = new Tooltip.Builder(view)
                    .setText(msg)
                    .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                    .setTextColor(getResources().getColor(R.color.White))
                    .setDismissOnClick(true)
                    .setCancelable(true)
                    .show();
            new CountDownTimer(10000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    t.dismiss();
                }
            }.start();
        }
    }

    void getHoliday() {
        event.clear();
        holiday_date.clear();
        Calendar calendar = holidayCalendar.currentmonth();
        BackgroundWork back = new BackgroundWork(this);
        back.execute(
                "Get Holiday",
                String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.MONTH) + 1),
                Integer.toString(calendar.get(Calendar.YEAR)));

        back.getDailog().setOnDismissListener(dialog -> {
            if (!back.getResult().isEmpty()) {
                try {
                    JSONObject json;
                    json = new JSONObject(back.getResult());
                    JSONArray jsonArray = json.optJSONArray("response");

                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject j_obj = jsonArray.getJSONObject(i);
                            holiday_date.add(j_obj.getString("date"));
                            event.put(j_obj.getString("date"), j_obj.getString("description"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holidayAdapter = new HolidayAdapter(holiday_date, event);
                holidayRecyclerView.setAdapter(holidayAdapter);
                markHoliday();
                holiday.setText(holiday_date.size() > 0 ? String.valueOf(holiday_date.size()) : String.valueOf(0));

            } else {
                holidayRecyclerView.setAdapter(null);
                holiday.setText(String.valueOf(0));

            }
        });

    }

    private void markHoliday() {
        Calendar calendar = holidayCalendar.currentmonth();
        Calendar holy = Calendar.getInstance();

        for (int i = 0; i < holiday_date.size(); i++) {
            holy.setTime(ConvertStringToDate(holiday_date.get(i)));
            if (calendar.get(Calendar.MONTH) == holy.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == holy.get(
                    Calendar.YEAR
            )
            ) {
                holidayCalendar.markHoliday(holy.getTime());
            }
        }
    }


    private void getAttendance() {

        attendance.clear();
        Calendar calendar = holidayCalendar.currentmonth();
        BackgroundWork back = new BackgroundWork(this);
        back.execute(
                "Get Monthly Attendance",
                String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.MONTH) + 1),
                String.valueOf(calendar.get(Calendar.YEAR))
        );
        back.getDailog().setOnDismissListener(dialog -> {
            if (!back.getResult().equals("Error: Data Not Found")) {
                try {

                    JSONObject json = new JSONObject(back.getResult());
                    JSONArray jsonArray = json.optJSONArray("response");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j_obj = jsonArray.getJSONObject(i);
                            attendance_report attendance_report = new attendance_report();


                            attendance_report.settimein(j_obj.getString("Time_IN"));
                            attendance_report.setlocation_lat_in(j_obj.getString("Location_Lat_In"));
                            attendance_report.setlocation_lng_in(j_obj.getString("Location_Lng_In"));
                            attendance_report.settime_in_img(j_obj.getString("In_Img_Name"));
                            attendance_report.settimeout(j_obj.getString("Time_OUT"));
                            attendance_report.setlocation_lat_out(j_obj.getString("Location_Lat_Out"));
                            attendance_report.setlocation_lng_out(j_obj.getString("Location_Lng_Out"));
                            attendance_report.settime_out_img(j_obj.getString("Out_Img_Name"));
                            attendance.add(attendance_report);

                            String date = format.format(Objects.requireNonNull(format.parse(j_obj.getString("Time_IN"))));

                            event.put(date,
                                    "  Time IN : " + j_obj.getString("Time_IN") + "\nTime Out : " + j_obj.getString(
                                            "Time_OUT"
                                    ));

                        }
                    }
                    markattendence();
                   if (attendance.size() > 0) {
                        present.setText(String.valueOf(attendance.size()));
                    } else {
                        present.setText(String.valueOf(0));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                present.setText(String.valueOf(0));
            }
        });

    }

    private void markattendence() {
        Calendar calendar = holidayCalendar.currentmonth();
        Calendar attend = Calendar.getInstance();
        for (int i = 0; i < attendance.size(); i++) {
            attend.setTime(ConvertStringToDate(attendance.get(i).gettimein()));
            if (calendar.get(Calendar.MONTH) == attend.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == attend.get(
                    Calendar.YEAR
            )
            ) {
                holidayCalendar.markAttendance(attend.getTime());
            }
        }
    }


    private void getweekoff() {

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(HolidayCalendar.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

        List<Date> disable = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, holidayCalendar.currentmonth().get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int month = cal.get(Calendar.MONTH);
        try {
        do {
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

            if (dayOfWeek ==weekdays.get(mUserLoginInfoBean.getWeekOff().toUpperCase(Locale.getDefault()))) {

                disable.add(cal.getTime());
            }

            cal.add(Calendar.DAY_OF_MONTH, 1);
        } while (cal.get(Calendar.MONTH) == month);
         weekoff.setText(disable.size());
        }catch(Exception e){
            if (e.getMessage() != null) {
                Log.e("Error", e.getMessage());
            }
        }
        this.markWeekOff(disable);
    }

    private void markWeekOff(List<Date> disable) {
        TextView weekoff = this.findViewById(R.id.calendar_weekoff);
        weekoff.setText(disable.size() > 0 ? String.valueOf(disable.size()) : String.valueOf(0));
        Calendar calendar = holidayCalendar.currentmonth();
        Calendar attend = Calendar.getInstance();


        for (int i = 0; i < disable.size(); i++) {
            attend.setTime(disable.get(i));
            if (calendar.get(Calendar.MONTH) == attend.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == attend.get(Calendar.YEAR)) {
                holidayCalendar.markWeekOff(attend.getTime());
            }
        }

    }

    private void getLeave() {

        leave_list.clear();
        Calendar calendar = holidayCalendar.currentmonth();
        BackgroundWork back = new BackgroundWork(this);
        back.execute(
                "Get Monthly Leave",
                String.format("%02d", calendar.get(Calendar.MONTH) + 1),
                String.valueOf(calendar.get(Calendar.YEAR))
        );
        back.getDailog().setOnDismissListener(dialogInterface -> {

            try {
                if (!back.getResult().equals("Error: Data Not Found")) {
                    JSONObject json = new JSONObject(back.getResult());
                    JSONArray jsonArray = json.optJSONArray("response");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j_obj = jsonArray.getJSONObject(i);
                            leave_monthly_report leave_report = new leave_monthly_report();

                            leave_report.setDate(j_obj.getString("date"));
                            leave_report.setReason(j_obj.getString("reason"));
                            leave_report.setRemark(j_obj.getString("remark"));
                            leave_report.setStatus(j_obj.getString("status"));


                            leave_list.add(leave_report);


                            event.put(j_obj.getString("date"),
                                    "Reason : " + j_obj.getString("reason") + "\nRemark : " + j_obj.getString("remark"));
                        }
                    }

                    markleave();
                }
                if (leave_list.size() > 0) {
                    leave.setText(String.valueOf(leave_list.size()));
                } else {
                    leave.setText(String.valueOf(0));
                }
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    Log.e("Error", e.getMessage());
                }
            }
        });
    }

    private void markleave() {
        Calendar calendar = holidayCalendar.currentmonth();
        Calendar attend = Calendar.getInstance();
        for (int i = 0; i < leave_list.size(); i++) {
            attend.setTime(ConvertStringToDate(leave_list.get(i).getDate()));
            if (calendar.get(Calendar.MONTH) == attend.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == attend.get(
                    Calendar.YEAR
            )
            ) {
                holidayCalendar.markLeave(attend.getTime());
            }
        }
    }

    private void getAbsent() {

        absent_list.clear();
        Calendar calendar = holidayCalendar.currentmonth();
        BackgroundWork back = new BackgroundWork(this);
        back.execute(
                "Get Monthly Absent",
                String.format("%02d", calendar.get(Calendar.MONTH) + 1),
                String.valueOf(calendar.get(Calendar.YEAR))
        );
        back.getDailog().setOnDismissListener(dialogInterface -> {

            try {
                if (!back.getResult().equals("Error: Data Not Found")) {
                    JSONObject json = new JSONObject(back.getResult());
                    JSONArray jsonArray = json.optJSONArray("response");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j_obj = jsonArray.getJSONObject(i);


                            absent_list.add(j_obj.getString("date"));




                            event.put(j_obj.getString("date"),
                                    "Remark : Absent");
                        }
                    }

                    markAbsent();
                }
                if (absent_list.size() > 0) {
                    absent.setText(String.valueOf(absent_list.size()));
                } else {
                    absent.setText(String.valueOf(0));
                }
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    Log.e("Error", e.getMessage());
                }
            }
        });
    }

    private void markAbsent() {
        Calendar calendar = holidayCalendar.currentmonth();
        Calendar attend = Calendar.getInstance();
        for (int i = 0; i < absent_list.size(); i++) {
            attend.setTime(ConvertStringToDate(absent_list.get(i)));
            if (calendar.get(Calendar.MONTH) == attend.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == attend.get(
                    Calendar.YEAR
            )
            ) {
                holidayCalendar.markAbsent(attend.getTime());
            }
        }
    }


    private Date ConvertStringToDate(String eventdate) {
        Date date = null;
        try {
            date = format.parse(eventdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    protected void onResume() {
        super.onResume();

        getweekoff();
        getHoliday();
        getLeave();
        getAttendance();
        getAbsent();

    }
}