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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@SuppressLint("StaticFieldLeak")
public class CompoffRequestFragment extends Fragment implements View.OnClickListener {

    public static final String DATE_DIALOG_3 = "datePicker3";

    private static TextView startDate;
    private static int mYear1;
    private static int mMonth1;
    private static int mDay1;
    private UserLoginInfoBean mUserLoginInfoBean;
    public static final String DATE_DIALOG_4 = "datePicker4";
    private static TextView endDate;

    private static TextView tv_leaves_day;
    private static int mYear2;
    private static int mMonth2;
    private static int mDay2;
    private EditText et_reason;
    private Button btn_sub;
    private static LinearLayout ll_comp_off;
    private SimpleDateFormat sd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.compoff_frag, container,
                false);
        sd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        initView(view);
        clickListener();
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
        ll_comp_off = view.findViewById(R.id.ll_comp_off);
        startDate = view.findViewById(R.id.start_date);
        endDate = view.findViewById(R.id.end_date);
        tv_leaves_day = view.findViewById(R.id.tv_leaves_day);
        btn_sub = view.findViewById(R.id.btn_sub);
        et_reason = view.findViewById(R.id.et_reason);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_date:
                DialogFragment newFragment1 = new DatePickerFragment3();
                newFragment1.show(requireActivity().getSupportFragmentManager(), DATE_DIALOG_3);
                break;
            case R.id.end_date:
                DialogFragment newFragment2 = new DatePickerFragment4();
                newFragment2.show(requireActivity().getSupportFragmentManager(), DATE_DIALOG_4);
                break;
            case R.id.btn_sub:

                btn_sub.setEnabled(false);
                submitRequest();
                break;
        }
    }

    /* private void checkClockin(String user_id, String desig_id, String start_date, String end_date, String lday, String reason) {
         BackgroundWork back = new BackgroundWork(requireActivity());
         back.execute("Check ClockIn Eligibility", user_id, desig_id, Utils.formatDatetoSubmit(start_date), Utils.formatDatetoSubmit(end_date));
         back.getDailog().setOnDismissListener(dialogInterface -> {
             try {
                 JSONObject json = new JSONObject(back.getResult());
                 JSONObject response = json.getJSONObject("response");
                 int count = response.getInt("COUNT");
                 if (count == Integer.parseInt(lday)) {
                     new submitCompoffRequest().execute(user_id, desig_id, Utils.formatDatetoSubmit(start_date), Utils.formatDatetoSubmit(end_date), reason, lday);
                     // Toast.makeText(requireActivity(), "Eligible", Toast.LENGTH_SHORT).show();
                 } else {
                     Utils.showSnackBar(getActivity(), ll_comp_off, "Comp-off request can not be Processed as Holiday/Week-Off/Clock-In Not Found");
                     btn_sub.setEnabled(true);
                 }

             } catch (JSONException e) {
                 e.printStackTrace();
                 btn_sub.setEnabled(true);
             }
         });
     }
 */
    private void submitRequest() {
        String start_date = startDate.getText().toString();
        String end_date = endDate.getText().toString();
        String lday = tv_leaves_day.getText().toString();
        String reason = et_reason.getText().toString();
        if (!start_date.isEmpty()) {
            if (!end_date.isEmpty()) {
                if (!reason.isEmpty()) {
                    // checkClockin(user_id,desig_id,start_date,end_date,lday,reason);
                    BackgroundWork back = new BackgroundWork(requireActivity());
                    back.execute("Submit Compoff Request", Utils.formatDatetoSubmit(start_date), Utils.formatDatetoSubmit(end_date), lday, reason);
                    back.getDailog().setOnDismissListener(dialogInterface -> {
                        try {
                            JSONObject json = new JSONObject(back.getResult());
                            if (json.getString("success").equals("true") && json.getInt("status code") == 200) {

                                Toast.makeText(getActivity(), "Compoff request Sent", Toast.LENGTH_SHORT)
                                        .show();
                                startDate.setText("");
                                endDate.setText("");
                                et_reason.setText("");
                                tv_leaves_day.setText("");


                            } else {
                                Utils.showSnackBar(
                                        getActivity(),
                                        ll_comp_off,
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
                    Utils.showSnackBar(getActivity(), ll_comp_off, "Please Enter Reason");
                    btn_sub.setEnabled(true);
                }
            } else {
                Utils.showSnackBar(getActivity(), ll_comp_off, "Please Enter End Date");
                btn_sub.setEnabled(true);
            }

        } else {
            Utils.showSnackBar(getActivity(), ll_comp_off, "Please Enter Start Date");
            btn_sub.setEnabled(true);
        }
    }

    public static class DatePickerFragment3 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int year, month, day;
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
            c.add(Calendar.DATE, -30);
            d.getDatePicker().setMinDate(c.getTimeInMillis());
            c.add(Calendar.DATE, 30);
            d.getDatePicker().setMaxDate(c.getTimeInMillis());

            return d;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            final Calendar c = Calendar.getInstance();
            int currentYear = c.get(Calendar.YEAR);
            int currentMonth = c.get(Calendar.MONTH);
            int currentDay = c.get(Calendar.DAY_OF_MONTH);
            mYear1 = year;
            mMonth1 = month + 1;
            mDay1 = dayOfMonth;
            int currentMonth1 = currentMonth + 1;
            String date1 = Utils.formatDatetoSubmit(currentDay + "-" + currentMonth1 + "-" + currentYear);
            String date2 = Utils.formatDatetoSubmit(mDay1 + "-" + mMonth1 + "-" + mYear1);
            if (!Utils.compareDatesForCompOff(date1, date2)) {
                String enddate = endDate.getText().toString().trim();
                if (!enddate.equalsIgnoreCase("")) {

                    String sDate = Utils.splitDay(enddate);
                    String sMonth = Utils.splitMonth(enddate);
                    String syear = Utils.splitYear(enddate);
                    String s1 = Utils.formatDatetoSubmit(sDate + "-" + sMonth + "-" + syear);
                    if (!Utils.compareDates(date2, s1)) {
                        Utils.showSnackBar(getActivity(), ll_comp_off, "Start Date Should be Smaller or equal to End Date");

                    } else {

                        String date = Utils.formatDate(mYear1 + "-" + mMonth1 + "-" + mDay1);
                        startDate.setText(date);

                        int days = Utils.differenceInTwoDates(date2, s1);
                        tv_leaves_day.setText(days + "");

                    }
                } else {
                    String date = Utils.formatDate(mYear1 + "-" + mMonth1 + "-" + mDay1);
                    startDate.setText(date);

                }

            } else {
                Utils.showSnackBar(getActivity(), ll_comp_off, "You are selecting future date");
            }

        }
    }


    public static class DatePickerFragment4 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int year, month, day;
            final Calendar c = Calendar.getInstance();
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
            c.add(Calendar.DATE, -30);
            d.getDatePicker().setMinDate(c.getTimeInMillis());
            c.add(Calendar.DATE, 30);
            d.getDatePicker().setMaxDate(c.getTimeInMillis());

            return d;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            final Calendar c = Calendar.getInstance();
            int currentYear = c.get(Calendar.YEAR);
            int currentMonth = c.get(Calendar.MONTH);
            int currentDay = c.get(Calendar.DAY_OF_MONTH);
            int currentMonth1 = currentMonth + 1;

            mYear2 = year;
            mMonth2 = month + 1;
            mDay2 = day;

            String date1 = Utils.formatDatetoSubmit(currentDay + "-" + currentMonth1 + "-" + currentYear);
            String date2 = Utils.formatDatetoSubmit(mDay2 + "-" + mMonth2 + "-" + mYear2);


            if (!Utils.compareDatesForCompOff(date1, date2)) {

                String startdate = startDate.getText().toString().trim();
                if (!startdate.equalsIgnoreCase("")) {
                    String sDate = Utils.splitDay(startdate);
                    String sMonth = Utils.splitMonth(startdate);
                    String syear = Utils.splitYear(startdate);
                    String s1 = Utils.formatDatetoSubmit(sDate + "-" + sMonth + "-" + syear);

                    if (!Utils.compareDates(s1, date2)) {
                        Utils.showSnackBar(getActivity(), ll_comp_off, "End Date Should be greater or equal to start Date");

                    } else {
                        String date = Utils.formatDate(mYear2 + "-" + mMonth2 + "-" + mDay2);
                        endDate.setText(date);

                        int days = Utils.differenceInTwoDates(s1, date2);
                        tv_leaves_day.setText(days + "");

                    }
                } else {
                    Utils.showSnackBar(getActivity(), ll_comp_off, "Please Select Start Date");

                }

            } else {
                Utils.showSnackBar(getActivity(), ll_comp_off, "You are selecting future date");
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    class submitCompoffRequest extends AsyncTask<String, Void, JSONObject> {
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
            nameValuePair.add(new BasicNameValuePair("method", "submit_comoff_request"));
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
            mDialog.dismiss();
        }
    }
}
