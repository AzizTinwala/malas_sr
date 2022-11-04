package com.malas.appsr.malasapp.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.malas.appsr.malasapp.BeanClasses.LeaveRequestStatusOject;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.LeaveRevertRequestSubmitAdapter;
import com.malas.appsr.malasapp.serverconnection.BackgroundWork;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.malas.appsr.malasapp.Utils.splitDay;
import static com.malas.appsr.malasapp.Utils.splitMonth;
import static com.malas.appsr.malasapp.Utils.splitYear;

public class RevertLeaveRequestFragment extends Fragment implements LeaveRevertRequestSubmitAdapter.ItemClicks {

    private RecyclerView rv_leave_list;
    private TextView tv_empty;
    private UserLoginInfoBean mUserLoginInfoBean;
    private ArrayList<LeaveRequestStatusOject> leaveRequestRevertStatus;
    public static final String DATE_PICKER_5 = "datePicker5";
    private static TextView startDate;
    private static int mYear1;
    private static int mMonth1;
    private static int mDay1;
    public static final String DATE_PICKER_6 = "datePicker6";
    private static TextView endDateSelection;
    private static TextView tv_leaves_day;
    private static int mYear2;
    private static int mMonth2;
    private static int mDay2;
    public static CardView cd_revert;
    public static String setStartDateInitial;
    public static String setEndDateInitial;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.revert_frag, container,
                false);
        initView(view);
        return view;

    }


    private void initView(View view) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        rv_leave_list = view.findViewById(R.id.rv_leave_revert_sub_list);
        tv_empty = view.findViewById(R.id.tv_empty);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rv_leave_list.setLayoutManager(linearLayoutManager);

        if (Utils.isInternetConnected(getActivity())) {
            new mGetLeaveRevertStatus().execute();

        } else {
            rv_leave_list.setVisibility(View.GONE);
            tv_empty.setVisibility(View.VISIBLE);
            tv_empty.setText("Please Check Internet Connection");
         }
    }

    @Override
    public void startDateClick(TextView et_start_date, TextView et_end_date, TextView et_days, String initialStartDate, String initialEndDate, CardView cd_revert1) {
        startDate = et_start_date;
        tv_leaves_day = et_days;
        endDateSelection = et_end_date;
        cd_revert = cd_revert1;

        setStartDateInitial = initialStartDate;
        setEndDateInitial = initialEndDate;
        DialogFragment newFragment1 = new DatePickerFragment5();
        newFragment1.show(requireActivity().getSupportFragmentManager(), DATE_PICKER_5);

    }

    @Override
    public void endDateClick(TextView et_start_date, TextView et_end_date, TextView et_days, String initialStartDate, String initialEndDate, CardView cd_revert1) {
        startDate = et_start_date;
        tv_leaves_day = et_days;
        endDateSelection = et_end_date;
        cd_revert = cd_revert1;
        setStartDateInitial = initialStartDate;
        setEndDateInitial = initialEndDate;
        DialogFragment newFragment2 = new DatePickerFragment6();
        newFragment2.show(requireActivity().getSupportFragmentManager(), DATE_PICKER_6);

    }

    @Override
    public void submitClick(String et_start_date, String et_end_date, TextView et_days, LeaveRequestStatusOject leaveRequestStatusOject, CardView cd_revert1) {
        cd_revert = cd_revert1;
        String s;
        if (tv_leaves_day!=null){
             s = tv_leaves_day.getText().toString();

        }else{
             s = et_days.getText().toString();

        }
    //  checkClockin(mUserLoginInfoBean.getUser_id(),mUserLoginInfoBean.getDesignation_id(),et_start_date,et_end_date,s,leaveRequestStatusOject,cd_revert1);
      submitRequest(et_start_date,et_end_date,s,leaveRequestStatusOject);

    }
    private void submitRequest(String et_start_date, String et_end_date, String s, LeaveRequestStatusOject leaveRequestStatusOject) {
        BackgroundWork back = new BackgroundWork(getActivity());
        back.execute(
                "Submit Leave Revert Request",
                leaveRequestStatusOject.getLvid(),
                Utils.formatDatetoSubmit(et_start_date),
                Utils.formatDatetoSubmit(et_end_date),
                s,
                leaveRequestStatusOject.getDtype1()
            );

        back.getDailog().setOnDismissListener(v-> {
        try {

            JSONObject json = new JSONObject(back.getResult());
Log.d("Status Code", String.valueOf( json.getInt("status code")));
            if (json.getString("success").equals("true") && json.getInt("status code") == 200) {
                Utils.showSnackBar(
                        getActivity(),
                        rv_leave_list,
                        "Leave Revert Successful Waiting For Manager Approval"
                 );
            } else {
                Utils.showSnackBar(
                        getActivity(),
                        rv_leave_list,
                        json.getString("message"));

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    });
    }

    private void checkClockin(String user_id, String designation_id, String et_start_date, String et_end_date,String days, LeaveRequestStatusOject leaveRequestStatusOject, CardView cd_revert1) {
        BackgroundWork back = new BackgroundWork(requireActivity());
        back.execute("Check ClockIn Eligibility",user_id,designation_id, Utils.formatDatetoSubmit(et_start_date), Utils.formatDatetoSubmit(et_end_date));
        back.getDailog().setOnDismissListener(dialogInterface -> {
            try {
                JSONObject json = new JSONObject(back.getResult());
                JSONObject response = json.getJSONObject("response");
                int count = response.getInt("COUNT");
                if(count==Integer.parseInt(days)){
                    new submitLeaveRequest().execute(Utils.formatDatetoSubmit(et_start_date), Utils.formatDatetoSubmit(et_end_date), days, leaveRequestStatusOject.getDtype1(), leaveRequestStatusOject.getLvid());   // Toast.makeText(requireActivity(), "Eligible", Toast.LENGTH_SHORT).show();
                }
                else{
                    Utils.showSnackBar(getActivity(), cd_revert, "Clock-In Not Found");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    public class mGetLeaveRevertStatus extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(requireActivity());
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "revert_leave"));
            nameValuePair.add(new BasicNameValuePair("user_id", mUserLoginInfoBean.getUserId()));
            nameValuePair.add(new BasicNameValuePair("desig_id", mUserLoginInfoBean.getDesignationId()));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            /*{"success":"true","message":"Data Available",
            "leaveRevertRequestList":[{"id":"91","request_id":"LVA5efa382950154","user_id":"329","desig_id":"5","fdate":"2020-6-30","tdate":"2020-07-04","leave_days":"5","reason":"fdgvbbdhftud","reject_reason":"-","status":"1","type":"LEAVE","deduct_type":"LWP","created_at":"2020-06-30 00:21:21","updated_at":"2020-06-30 00:21:21"}],"successCode":"MALAS-APP-SUCC-1"}*/
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        leaveRequestRevertStatus = new ArrayList<>();
                        JSONArray mJsonarry = jsonObject.getJSONArray("leaveRevertRequestList");
                        for (int j = 0; j < mJsonarry.length(); j++) {
                            rv_leave_list.setVisibility(View.VISIBLE);
                            tv_empty.setVisibility(View.GONE);
                            JSONObject mJsonObjInfo = mJsonarry.getJSONObject(j);
                            String request_id = mJsonObjInfo.getString("request_id");
                            String fdate = mJsonObjInfo.getString("fdate");
                            String tdate = mJsonObjInfo.getString("tdate");
                            String type = mJsonObjInfo.getString("type");
                            String leave_days = mJsonObjInfo.getString("leave_days");
                            int status = mJsonObjInfo.getInt("status");
                            String dtype1 = mJsonObjInfo.getString("deduct_type");
                            String dtype = "";
                            if (dtype1.equalsIgnoreCase("COMPOFF")) {
                                dtype = Utils.insertString("COMPOFF",
                                        "-",
                                        3);
                            } else {
                                dtype = dtype1;
                            }
                            String sendDate = mJsonObjInfo.getString("created_at");

                            if (Utils.thirtyDaysDifference(fdate)) {
                                leaveRequestRevertStatus.add(new LeaveRequestStatusOject(request_id, fdate, sendDate, leave_days, status, type, tdate, dtype1, dtype));
                            }
                        }
                        LeaveRevertRequestSubmitAdapter leaveRequestAdapter = new LeaveRevertRequestSubmitAdapter(getActivity(), leaveRequestRevertStatus, RevertLeaveRequestFragment.this);
                        rv_leave_list.setAdapter(leaveRequestAdapter);
                    } else {

                        rv_leave_list.setVisibility(View.GONE);
                        tv_empty.setVisibility(View.VISIBLE);
                        tv_empty.setText(jsonObject.getString("message"));

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    rv_leave_list.setVisibility(View.GONE);
                    tv_empty.setVisibility(View.VISIBLE);
                    tv_empty.setText("Something went wrong.");

                }
            } else {

                rv_leave_list.setVisibility(View.GONE);
                tv_empty.setVisibility(View.VISIBLE);
                tv_empty.setText("Improper response from server");

            }
            mDialog.dismiss();
        }
    }

    public static class DatePickerFragment5 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            int year, month, day;
            if (!startDate.getText().toString().equalsIgnoreCase("")) {
                year = Integer.parseInt(splitYear(startDate.getText().toString()));
                month = Integer.parseInt(splitMonth(startDate.getText().toString())) - 1;
                day = Integer.parseInt(splitDay(startDate.getText().toString()));
            } else {
                year = Integer.parseInt(splitYear(setStartDateInitial));
                month = Integer.parseInt(splitMonth(setStartDateInitial)) - 1;
                day = Integer.parseInt(splitDay(setStartDateInitial));

            }
            return new DatePickerDialog(requireActivity(), R.style.DatePicker1, this, year, month, day);
        }


        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String styear = "", stmonth = "", stday = "";
            styear = splitYear(setStartDateInitial);
            stmonth = splitMonth(setStartDateInitial);
            stday = splitDay(setStartDateInitial);

            mYear1 = year;
            mMonth1 = month + 1;
            mDay1 = dayOfMonth;

            String date1 = Utils.formatDatetoSubmit(stday + "-" + stmonth + "-" + styear);
            String date2 = Utils.formatDatetoSubmit(mDay1 + "-" + mMonth1 + "-" + mYear1);


            if (Utils.compareDates(date1, date2)) {
                String enddate = setEndDateInitial;
                if (!enddate.equalsIgnoreCase("")) {
                    String sDate = String.valueOf(splitDay(enddate));
                    String sMonth = String.valueOf(splitMonth(enddate));
                    String syear = String.valueOf(splitYear(enddate));
                    String s1 = Utils.formatDatetoSubmit(sDate + "-" + sMonth + "-" + syear);
                    if (!Utils.compareDates(date2, s1)) {
                        Utils.showSnackBar(getActivity(), cd_revert, "Start Date Should be Smaller or equal to End Date");

                    } else {
                        String enddateSelection = endDateSelection.getText().toString();
                        if (!enddateSelection.equalsIgnoreCase("")) {
                            String eDate = String.valueOf(splitDay(enddateSelection));
                            String eMonth = String.valueOf(splitMonth(enddateSelection));
                            String eyear = String.valueOf(splitYear(enddateSelection));
                            String e1 = Utils.formatDatetoSubmit(eDate + "-" + eMonth + "-" + eyear);


                            if (!Utils.compareDates(date2, e1)) {
                                Utils.showSnackBar(getActivity(), cd_revert, "Start Date Should be Smaller or equal to End Date");

                            } else {
                                String date = Utils.formatDate(mYear1 + "-" + mMonth1 + "-" + mDay1);
                                startDate.setText(date);

                                int days = Utils.differenceInTwoDates(date2, e1);
                                tv_leaves_day.setText(days + "");

                            }
                        } else {

                            String date = Utils.formatDate(mYear1 + "-" + mMonth1 + "-" + mDay1);
                            startDate.setText(date);

                        }


                    }
                } else {

                    String date = Utils.formatDate(mYear1 + "-" + mMonth1 + "-" + mDay1);
                    startDate.setText(date);
                }

            } else {
                Utils.showSnackBar(getActivity(), cd_revert, "Please Select start date and end date between your request dates");
            }

        }
    }

    public static class DatePickerFragment6 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            int year, month, day;
            if (!endDateSelection.getText().toString().equalsIgnoreCase("")) {
                year = Integer.parseInt(splitYear(endDateSelection.getText().toString()));
                month = Integer.parseInt(splitMonth(endDateSelection.getText().toString())) - 1;
                day = Integer.parseInt(splitDay(endDateSelection.getText().toString()));
            } else {
                year = Integer.parseInt(splitYear(setEndDateInitial));
                month = Integer.parseInt(splitMonth(setEndDateInitial)) - 1;
                day = Integer.parseInt(splitDay(setEndDateInitial));
            }
            return new DatePickerDialog(requireActivity(), R.style.DatePicker2, this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            final Calendar c = Calendar.getInstance();
            int styear = Integer.parseInt(splitYear(setEndDateInitial));
            int stmonth = Integer.parseInt(splitMonth(setEndDateInitial));
            int stday = Integer.parseInt(splitDay(setEndDateInitial));
            mYear2 = year;
            mMonth2 = month + 1;
            mDay2 = day;


            String date1 = Utils.formatDatetoSubmit(stday + "-" + stmonth + "-" + styear);
            String date2 = Utils.formatDatetoSubmit(mDay2 + "-" + mMonth2 + "-" + mYear2);

            if (!Utils.compareDatesForCompOff(date1, date2)) {

                String startdate = startDate.getText().toString().trim();
                if (!startdate.equalsIgnoreCase("")) {
                    String sDate = splitDay(startdate);
                    String sMonth = splitMonth(startdate);
                    String syear = splitYear(startdate);
                    String s1 = Utils.formatDatetoSubmit(sDate + "-" + sMonth + "-" + syear);
                    if (!Utils.compareDates(s1, date2)) {
                        Utils.showSnackBar(getActivity(), cd_revert, "End Date Should be greater or equal to start Date");

                    } else {

                        String date = Utils.formatDate(mYear2 + "-" + mMonth2 + "-" + mDay2);
                        endDateSelection.setText(date);
                        int days = Utils.differenceInTwoDates(s1, date2);
                        tv_leaves_day.setText(days + "");

                    }
                } else {
                    Utils.showSnackBar(getActivity(), cd_revert, "Please Select Start Date");

                }

            } else {
                Utils.showSnackBar(getActivity(), cd_revert, "You are selecting future date from End date");
            }


        }

    }

    @SuppressLint("StaticFieldLeak")
    class submitLeaveRequest extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(requireActivity());
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "submit_revert_leave"));
            nameValuePair.add(new BasicNameValuePair("user_id", mUserLoginInfoBean.getUserId()));
            nameValuePair.add(new BasicNameValuePair("desig_id", mUserLoginInfoBean.getDesignationId()));
            nameValuePair.add(new BasicNameValuePair("start_date", params[0]));
            nameValuePair.add(new BasicNameValuePair("end_date", params[1]));
            nameValuePair.add(new BasicNameValuePair("days", params[2]));
            nameValuePair.add(new BasicNameValuePair("leave_type", params[3]));//deduct
            nameValuePair.add(new BasicNameValuePair("lvid", params[4]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        if (startDate != null)
                            startDate.setText("");
                        if (endDateSelection != null)
                            endDateSelection.setText("");
                        if (tv_leaves_day != null)
                            tv_leaves_day.setText("");
                        if (Utils.isInternetConnected(getActivity())) {
                            new mGetLeaveRevertStatus().execute();
                        } else {
                            Toast.makeText(getActivity(), "No Internet Access", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getActivity(), "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();

            }
        }


    }


}