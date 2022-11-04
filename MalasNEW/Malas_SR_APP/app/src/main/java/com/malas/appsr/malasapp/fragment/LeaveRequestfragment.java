package com.malas.appsr.malasapp.fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.malas.appsr.malasapp.Utils.splitDay;
import static com.malas.appsr.malasapp.Utils.splitMonth;
import static com.malas.appsr.malasapp.Utils.splitYear;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.serverconnection.BackgroundWork;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@SuppressLint("StaticFieldLeak")
public class LeaveRequestfragment extends Fragment implements View.OnClickListener {
    public static final String DATE_DIALOG_1 = "datePicker1";
    private static TextView startDate;
    private UserLoginInfoBean mUserLoginInfoBean;
    public static final String DATE_DIALOG_2 = "datePicker2";
    private static TextView endDate;
    private TextView tv_comp_off;
    private TextView tv_leave_balance;
    private TextView tv_lwp;
    private static TextView tv_leaves_day;
    private AutoCompleteTextView et_reason;
    private Button btn_sub;
    private static LinearLayout ll__leave_request;
    private LinearLayout ll_leave;
    private ArrayAdapter<String> reason;
    private SimpleDateFormat sd;
    private View view;

    static private Integer fin_year_end;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.leave_request, container,
                false);
        sd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        initView(view);
        clickListener();

        int currentYear, currentMonth;

        final Calendar c = Calendar.getInstance();

        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH) + 1;

        if (currentMonth > 3) {
            fin_year_end = currentYear + 1;
        } else {
            fin_year_end = currentYear;
        }

        return view;

    }

    private void clickListener() {
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        btn_sub.setOnClickListener(this);
    }

    private void initView(View view) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        ll__leave_request = view.findViewById(R.id.ll__leave_request);
        ll_leave = view.findViewById(R.id.ll_leave);
        startDate = view.findViewById(R.id.start_date);
        endDate = view.findViewById(R.id.end_date);
        tv_comp_off = view.findViewById(R.id.tv_comp_off);
        tv_leave_balance = view.findViewById(R.id.tv_leave_balance);
        tv_lwp = view.findViewById(R.id.tv_lwp);
        tv_leaves_day = view.findViewById(R.id.tv_leaves_day);
        btn_sub = view.findViewById(R.id.btn_sub);
        et_reason = view.findViewById(R.id.et_reason);
        et_reason.setFocusable(false);
        et_reason.setCursorVisible(false);

        reason = new ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, getReason());
        et_reason.setAdapter(reason);
        et_reason.setOnClickListener(v -> {
            reason.getFilter().filter(null);
            et_reason.showDropDown();
        });
        et_reason.setOnItemClickListener((parent, view1, position, id) -> {
            if (parent.getItemAtPosition(position).toString().equals("OTHER REASON")) {
                et_reason.setText("");
                et_reason.setFocusable(true);
                et_reason.setFocusableInTouchMode(true);
                et_reason.setCursorVisible(true);
                et_reason.requestFocus();
                et_reason.setAdapter(null);
            }
        });
        if (Utils.isInternetConnected(getActivity())) {
            if (mUserLoginInfoBean != null) {
                //   new getLeavePendingMessage().execute(mUserLoginInfoBean.getUser_id(), mUserLoginInfoBean.getDesignation_id());
                new getLeaveBalance().execute(mUserLoginInfoBean.getUserId(), mUserLoginInfoBean.getDesignationId());

            } else {

                Utils.showToast(getActivity(), "Please Login Again");
            }
        } else {
            Utils.showToast(getActivity(), "No Internet Access");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_date:

                DialogFragment newFragment1 = new DatePickerFragment1();
                newFragment1.show(requireActivity().getSupportFragmentManager(), DATE_DIALOG_1);
                break;

            case R.id.end_date:

                if (startDate.getText().length() > 0) {
                    DialogFragment newFragment2 = new DatePickerFragment2();
                    newFragment2.show(requireActivity().getSupportFragmentManager(), DATE_DIALOG_2);
                } else {
                    Utils.showSnackBar(getActivity(), ll__leave_request, "Please Select Start Date");
                }
                break;

            case R.id.btn_sub:

                btn_sub.setEnabled(false);
                submitRequest();
                break;
        }
    }

    private void submitRequest() {
        String start_date = startDate.getText().toString();
        String end_date = endDate.getText().toString();
        String lday = tv_leaves_day.getText().toString();
        String reason = et_reason.getText().toString();
        if (!start_date.isEmpty()) {
            if (!end_date.isEmpty()) {
                if (!reason.isEmpty()) {
                    //    new submitLeaveRequest().execute(user_id, desig_id, Utils.formatDatetoSubmit(start_date), Utils.formatDatetoSubmit(end_date), reason, lday);
                    BackgroundWork back = new BackgroundWork(requireActivity());
                    back.execute("Submit Leave Request", Utils.formatDatetoSubmit(start_date), Utils.formatDatetoSubmit(end_date), lday, reason);
                    back.getDailog().setOnDismissListener(dialogInterface -> {
                        try {
                            JSONObject json = new JSONObject(back.getResult());
                            if (json.getString("success").equals("true") && json.getInt("status code") == 200) {
                                Toast.makeText(getActivity(), "Leave request Sent", Toast.LENGTH_SHORT)
                                        .show();
                                startDate.setText("");
                                endDate.setText("");
                                et_reason.setText("");
                                tv_leaves_day.setText("");
                                if (Utils.isInternetConnected(requireActivity())) {
                                    initView(view);
                                } else {
                                    Utils.showToast(getActivity(), "No Internet Access");
                                }

                            } else if (json.getString("success").equals("false") && json.getInt("status code") == 400) {
                                Utils.showSnackBar(
                                        getActivity(),
                                        ll__leave_request,
                                        json.getString("message")
                                );

                                btn_sub.setEnabled(true);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            btn_sub.setEnabled(true);
                        }
                    });
                } else {
                    Utils.showSnackBar(getActivity(), ll__leave_request, "Please Enter Reason");
                    btn_sub.setEnabled(true);
                }
            } else {
                Utils.showSnackBar(getActivity(), ll__leave_request, "Please Enter End Date");
                btn_sub.setEnabled(true);
            }

        } else {
            Utils.showSnackBar(getActivity(), ll__leave_request, "Please Enter Start Date");
            btn_sub.setEnabled(true);
        }
    }


    public static class DatePickerFragment1 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int year, month, day;
            this.setCancelable(false);
            final Calendar c = Calendar.getInstance();

            if (!startDate.getText().toString().equalsIgnoreCase("")) {
                year = Integer.parseInt(splitYear(startDate.getText().toString()));
                month = Integer.parseInt(splitMonth(startDate.getText().toString())) - 1;
                day = Integer.parseInt(splitDay(startDate.getText().toString()));
            } else {
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            }
            DatePickerDialog d = new DatePickerDialog(requireActivity(), R.style.DatePicker1, this, year, month, day);
            // Move day as first day of the month
            c.set(Calendar.DAY_OF_MONTH, 1);
            d.getDatePicker().setMinDate(c.getTimeInMillis());
            // Move "month" for previous one
            c.add(Calendar.MONTH, 4);
            d.getDatePicker().setMaxDate(c.getTimeInMillis());

            return d;
        }


        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            startDate.setText("");
            endDate.setText("");
            int currentYear, currentMonth, currentDay;

            final Calendar c = Calendar.getInstance();
            currentYear = c.get(Calendar.YEAR);
            currentMonth = c.get(Calendar.MONTH) + 1;
            currentDay = c.get(Calendar.DAY_OF_MONTH);

            int mMonth1 = month + 1;


            String date1 = Utils.formatDatetoSubmit(currentDay + "-" + currentMonth + "-" + currentYear);
            String date2 = Utils.formatDatetoSubmit(dayOfMonth + "-" + mMonth1 + "-" + year);
            if (
                    (c.get(Calendar.MONTH) <= month || c.get(Calendar.YEAR) < year)
            ) {
                if (year == fin_year_end && mMonth1 > 3) {
                    Utils.showSnackBar(getActivity(), ll__leave_request, "You Can't Mark Leave For Next Financial Year");
                } else {
                    String enddate = endDate.getText().toString().trim();
                    if (!enddate.equalsIgnoreCase("")) {
                        String sDate = Utils.splitDay(enddate);
                        String sMonth = Utils.splitMonth(enddate);
                        String syear = Utils.splitYear(enddate);
                        String s1 = Utils.formatDatetoSubmit(sDate + "-" + sMonth + "-" + syear);
                        if (!Utils.compareDates(date2, s1)) {
                            Utils.showSnackBar(getActivity(), ll__leave_request, "Start Date Should be Smaller or equal to End Date");

                        } else {
                            String date = Utils.formatDate(year + "-" + mMonth1 + "-" + dayOfMonth);

                            startDate.setText(date);
                            int days = Utils.differenceInTwoDates(date2, s1);
                            tv_leaves_day.setText(days + "");

                        }
                    } else {
                        String date = Utils.formatDate(year + "-" + mMonth1 + "-" + dayOfMonth);

                        startDate.setText(date);
                    }
                }
            } else {
                Utils.showSnackBar(getActivity(), ll__leave_request, "You are selecting old date");
            }

        }
    }

    public static class DatePickerFragment2 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int year, month, day;
            String startdate = startDate.getText().toString().trim();

            String sDate = Utils.splitDay(startdate);
            String sMonth = Utils.splitMonth(startdate);
            String syear = Utils.splitYear(startdate);

            final Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(syear), (Integer.parseInt(sMonth) - 1), Integer.parseInt(sDate));
            if (!endDate.getText().toString().equalsIgnoreCase("")) {
                year = Integer.parseInt(splitYear(endDate.getText().toString()));
                month = Integer.parseInt(splitMonth(endDate.getText().toString())) - 1;
                day = Integer.parseInt(splitDay(endDate.getText().toString()));
            } else {
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            }
            DatePickerDialog d = new DatePickerDialog(requireActivity(), R.style.DatePicker1, this, year, month, day);
            d.getDatePicker().setMinDate(c.getTimeInMillis());
            // Move "month" for previous one
            c.add(Calendar.MONTH, 1);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.DATE, -1);
            d.getDatePicker().setMaxDate(c.getTimeInMillis());

            return d;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            endDate.setText("");
            final Calendar c = Calendar.getInstance();
            int currentYear = c.get(Calendar.YEAR);
            int currentMonth = c.get(Calendar.MONTH);
            int currentDay = c.get(Calendar.DAY_OF_MONTH);
            int currentMonth1 = currentMonth + 1;

            int mMonth2 = month + 1;
            String date1 = Utils.formatDatetoSubmit(currentDay + "-" + currentMonth1 + "-" + currentYear);
            String date2 = Utils.formatDatetoSubmit(day + "-" + mMonth2 + "-" + year);

            if (c.get(Calendar.MONTH) <= month || c.get(Calendar.YEAR) < year) {

                if (year == fin_year_end && mMonth2 > 3) {
                    Utils.showSnackBar(getActivity(), ll__leave_request, "You Can't Mark Leave For Next Financial Year");
                } else {
                    String startdate = startDate.getText().toString().trim();
                    if (!startdate.equalsIgnoreCase("")) {

                        String sDate = Utils.splitDay(startdate);
                        String sMonth = Utils.splitMonth(startdate);
                        String syear = Utils.splitYear(startdate);
                        String s1 = Utils.formatDatetoSubmit(sDate + "-" + sMonth + "-" + syear);
                        if (!Utils.compareDates(s1, date2)) {
                            Utils.showSnackBar(getActivity(), ll__leave_request, "End Date Should be greater or equal to start Date");

                        } else {

                            String date = Utils.formatDate(year + "-" + mMonth2 + "-" + day);
                            endDate.setText(date);
                            int days = Utils.differenceInTwoDates(s1, date2);
                            tv_leaves_day.setText(days + "");

                        }
                    } else {
                        Utils.showSnackBar(getActivity(), ll__leave_request, "Please Select Start Date");

                    }
                }
            } else {
                Utils.showSnackBar(getActivity(), ll__leave_request, "You are selecting old date");
            }

        }


    }

    @SuppressLint("StaticFieldLeak")
    public class getCompOffBalance extends AsyncTask<String, Void, JSONObject> {
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
            nameValuePair.add(new BasicNameValuePair("method", "getCompoffBalance"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("desig_id", params[1]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        int compoff_balance = object.getInt("compoff_balance");
                        tv_comp_off.setText(compoff_balance + "");
                    } else {
                        tv_comp_off.setText("0");
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

    @SuppressLint("StaticFieldLeak")
    public class getLeavePendingMessage extends AsyncTask<String, Void, JSONObject> {
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
            nameValuePair.add(new BasicNameValuePair("method", "leave_pending_asm"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("desig_id", params[1]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        ll_leave.setVisibility(View.GONE);

                    } else {
                        ll_leave.setVisibility(View.VISIBLE);

                        if (Utils.isInternetConnected(getActivity())) {
                            if (mUserLoginInfoBean != null) {
                                new getLeaveBalance().execute(mUserLoginInfoBean.getUserId(), mUserLoginInfoBean.getDesignationId());
                            } else {
                                Utils.showToast(getActivity(), "Please Login Again");
                            }
                        } else {
                            Utils.showToast(getActivity(), "No Internet Access");
                        }
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

    @SuppressLint("StaticFieldLeak")
    public class getLeaveBalance extends AsyncTask<String, Void, JSONObject> {
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
            nameValuePair.add(new BasicNameValuePair("method", "getLeaveBalance"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("desig_id", params[1]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        int leave_balance = object.getInt("leave_balance");
                        tv_leave_balance.setText(leave_balance + "");

                        JSONObject jsonArray = object.optJSONObject("data");

                        assert jsonArray != null;
                        if (jsonArray.getInt("leave_balance") != 0) {
                            tv_leave_balance.setText(
                                    jsonArray.getString("leave_balance"));
                        } else {
                            tv_leave_balance.setText("0");
                        }

                        if (jsonArray.getInt("compoff_balance") != 0) {
                            tv_comp_off.setText(
                                    jsonArray.getString("compoff_balance"));
                        } else {
                            tv_comp_off.setText("0");
                        }

                        if (jsonArray.getInt("lwp_count") != 0) {
                            tv_lwp.setText(
                                    jsonArray.getString("lwp_count"));
                        } else {
                            tv_lwp.setText("0");
                        }

                    } else {
                        tv_leave_balance.setText("0");
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


            if (Utils.isInternetConnected(getActivity())) {
                new getCompOffBalance().execute(mUserLoginInfoBean.getUserId(), mUserLoginInfoBean.getDesignationId());
            } else {
                Utils.showToast(getActivity(), "No Internet Access");
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
            nameValuePair.add(new BasicNameValuePair("method", "submit_leave_request"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("desig_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("start_date", params[2]));
            nameValuePair.add(new BasicNameValuePair("end_date", params[3]));
            nameValuePair.add(new BasicNameValuePair("reason", params[4]));
            nameValuePair.add(new BasicNameValuePair("days", params[5]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject object) {
            btn_sub.setEnabled(true);
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        startDate.setText("");
                        endDate.setText("");
                        et_reason.setText("");
                        tv_leaves_day.setText("");
                        if (Utils.isInternetConnected(getActivity())) {
                            // new getLeavePendingMessage().execute(mUserLoginInfoBean.getUser_id(), mUserLoginInfoBean.getDesignation_id());
                            if (mUserLoginInfoBean != null) {
                                // new getLeaveBalance().execute(mUserLoginInfoBean.getUser_id(), mUserLoginInfoBean.getDesignation_id());
                                initView(view);
                            } else {
                                Utils.showToast(getActivity(), "Please Login Again");

                            }


                        } else {
                            Utils.showToast(getActivity(), "No Internet Access");
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

    List<String> getReason() {
        BackgroundWork back = new BackgroundWork(requireActivity());
        back.execute("Get Leave Reason");
        List<String> reason = new ArrayList<>();

        back.getDailog().setOnDismissListener(it -> {
            try {
                JSONObject json = new JSONObject(back.getResult());

                JSONArray jsonArray = json.optJSONArray("response");

                for (int i = 0; i <= jsonArray.length(); i++) {
                    JSONObject j_obj = jsonArray.getJSONObject(i);

                    reason.add(j_obj.getString("leave_reason"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        return reason;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView(view);
    }
}
