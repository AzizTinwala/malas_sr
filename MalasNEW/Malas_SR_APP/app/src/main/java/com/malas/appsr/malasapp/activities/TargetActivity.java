package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.MonthBean;
import com.malas.appsr.malasapp.BeanClasses.TargetBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.BeanClasses.YearBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.DistributorSpinnerAdapter;
import com.malas.appsr.malasapp.adapter.MonthAdapter;
import com.malas.appsr.malasapp.adapter.TargetExpandableListAdapter;
import com.malas.appsr.malasapp.adapter.YearAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class TargetActivity extends AppCompatActivity {

    ExpandableListView lvExpTarget;
    EditText spnr_financialYear, spnr_financialMonth, spnr_distributor;
    TargetExpandableListAdapter mAdapter;
    ArrayList<DistributerBean> mDistributerList;
    ArrayList<MonthBean> mMonthList;
    ArrayList<YearBean> mYearList;
    AsyncTask<String, Void, JSONObject> getDistributorTaskCalling;
    DistributerBean selectedDistributerBean;
    DistributorSpinnerAdapter mDistributorSpinnerAdapter;
    MonthAdapter monthAdapter;
    YearAdapter yearAdapter;
    int lastExpandedPosition = -1;
    TextView tvCurrentDate, tv_territory_target, tvTargetTotal, tvAchievementTotal, tvPercentageTotal;
    UserLoginInfoBean mUserLoginInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Report");
        lvExpTarget = findViewById(R.id.lvExpTarget);
        spnr_financialYear = findViewById(R.id.spnr_financialYear);
        spnr_financialMonth = findViewById(R.id.spnr_financialMonth);
        spnr_distributor = findViewById(R.id.spnr_distributor);
        tvCurrentDate = findViewById(R.id.tv_current_date);
        tv_territory_target = findViewById(R.id.tv_territory_target);
        tvTargetTotal = findViewById(R.id.tvTargetTotal);
        tvAchievementTotal = findViewById(R.id.tvAchievementTotal);
        tvPercentageTotal = findViewById(R.id.tvPercentageTotal);

        tvCurrentDate.setText(getCurrentDate());
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(TargetActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        tv_territory_target.setText(mUserLoginInfoBean.getTerritoryName());


        lvExpTarget.setOnGroupExpandListener(groupPosition -> {

            if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                lvExpTarget.collapseGroup(lastExpandedPosition);
            }
            lastExpandedPosition = groupPosition;

        });

        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(TargetActivity.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
        Type typeDistributor = new TypeToken<ArrayList<DistributerBean>>() {
        }.getType();
        mDistributerList = mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor) == null ? new ArrayList<>() : (ArrayList<DistributerBean>) mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor);

        mDistributerList.add(0, new DistributerBean("-1", "", "", "", "", "", "", "", "Total", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        mDistributorSpinnerAdapter = new DistributorSpinnerAdapter(TargetActivity.this, mDistributerList, "");

        mMonthList = new ArrayList<>();
        mMonthList = getAllMonths();
        mYearList = new ArrayList<>();
        mYearList = getAllYears();

        spnr_distributor.setOnClickListener(v -> {
            if (mDistributerList != null) {
                final Dialog dialog = new Dialog(TargetActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                mDistributorSpinnerAdapter = new DistributorSpinnerAdapter(TargetActivity.this, mDistributerList, "");
                listView.setAdapter(mDistributorSpinnerAdapter);

                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String st = etserach.getText().toString();
                        mDistributorSpinnerAdapter.filter(st);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    dialog.dismiss();
                    setDistributorData(position);
                });
            }

        });


        spnr_financialMonth.setOnClickListener(v -> {
            if (mMonthList != null) {

                final Dialog dialog = new Dialog(TargetActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                monthAdapter = new MonthAdapter(TargetActivity.this, mMonthList);
                listView.setAdapter(monthAdapter);

                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String st = etserach.getText().toString();
                        if (!s.equals("") && s.length() > 0) {
                            monthAdapter.filter(st);
                        } else {
                            monthAdapter.filter(st);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    String countryname = MonthAdapter.resultArrayshort.get(position).getName();
                    spnr_financialMonth.setText(countryname);
                    dialog.dismiss();
                    try {
                        if (selectedDistributerBean.getFirm_name().equals("Total"))
                            getTargetData(mMonthList.get(position).getId(), spnr_financialYear.getText().toString().trim(), mUserLoginInfoBean.getUserTerritoryId(), "-1");
                        else
                            getTargetData(mMonthList.get(position).getId(), spnr_financialYear.getText().toString().trim(), selectedDistributerBean.getDistribution_id(), "0");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        spnr_financialYear.setOnClickListener(v -> {
            if (mYearList != null) {
                final Dialog dialog = new Dialog(TargetActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                yearAdapter = new YearAdapter(TargetActivity.this, mYearList);
                listView.setAdapter(yearAdapter);

                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String st = etserach.getText().toString();
                        if (!s.equals("") && s.length() > 0) {
                            yearAdapter.filter(st);
                        } else {
                            yearAdapter.filter(st);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    String countryname = YearAdapter.resultArrayshort.get(position).getName();
                    spnr_financialYear.setText(countryname);
                    dialog.dismiss();
                    try {
                        if (mMonthList != null) {
                            for (MonthBean month : mMonthList) {
                                if (month.getName().equals(spnr_financialMonth.getText().toString().trim())) {

                                    if (selectedDistributerBean.getFirm_name().equals("Total"))
                                        getTargetData(month.getId(), mYearList.get(position).getId(), mUserLoginInfoBean.getUserTerritoryId(), "-1");
                                    else
                                        getTargetData(month.getId(), mYearList.get(position).getId(), selectedDistributerBean.getDistribution_id(), "0");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        });

        spnr_financialMonth.setText(getCurrentMonth());
        spnr_financialYear.setText(getCurrentYear());

        setDistributorData(0);

    }

    public void setDistributorData(int position) {
        String countryname = DistributorSpinnerAdapter.resultArrayshort.get(position).getFirm_name();
        spnr_distributor.setText(countryname);

        try {
            selectedDistributerBean = mDistributerList.get(position);

            String distributerId = mDistributerList.get(position).getDistribution_id();

            if (mMonthList != null) {
                for (MonthBean month : mMonthList) {
                    if (month.getName().equals(spnr_financialMonth.getText().toString().trim())) {
                        if (selectedDistributerBean.getFirm_name().equals("Total"))
                            getTargetData(month.getId(), spnr_financialYear.getText().toString().trim(), mUserLoginInfoBean.getUserTerritoryId(), "-1");
                        else
                            getTargetData(month.getId(), spnr_financialYear.getText().toString().trim(), distributerId, "0");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrentDate() {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();

        return format.format(date);
    }

    public String getCurrentYear() {
        DateFormat format = new SimpleDateFormat("yyyy", Locale.getDefault());
        Date date = new Date();

        return format.format(date);
    }

    public String getCurrentMonth() {
        DateFormat format = new SimpleDateFormat("MMMM", Locale.getDefault());
        Date date = new Date();

        return format.format(date);
    }

    public void getTargetData(String month, String year, String distributorId, String status) {

        if (year.equals("")) {
            Toast.makeText(this, "Please Select Year", Toast.LENGTH_SHORT).show();
        } else if (month.equals("")) {
            Toast.makeText(this, "Please Select Month", Toast.LENGTH_SHORT).show();
        } else if (distributorId.equals("")) {
            Toast.makeText(this, "Please Select Distributor", Toast.LENGTH_SHORT).show();
        } else {
            new GetTargetList().execute(month, year, distributorId, status);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        ComplexPreferences mUserPreference = ComplexPreferences.getComplexPreferences(TargetActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = mUserPreference.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        getDistributorTaskCalling = new mDistributorList().execute(mUserLoginInfoBean.getUserTerritoryId());
    }

    public ArrayList<MonthBean> getAllMonths() {
        ArrayList<MonthBean> monthList = new ArrayList<>();
        MonthBean monthBean;
        monthBean = new MonthBean("January", "1");
        monthList.add(monthBean);
        monthBean = new MonthBean("February", "2");
        monthList.add(monthBean);
        monthBean = new MonthBean("March", "3");
        monthList.add(monthBean);
        monthBean = new MonthBean("April", "4");
        monthList.add(monthBean);
        monthBean = new MonthBean("May", "5");
        monthList.add(monthBean);
        monthBean = new MonthBean("June", "6");
        monthList.add(monthBean);
        monthBean = new MonthBean("July", "7");
        monthList.add(monthBean);
        monthBean = new MonthBean("August", "8");
        monthList.add(monthBean);
        monthBean = new MonthBean("September", "9");
        monthList.add(monthBean);
        monthBean = new MonthBean("October", "10");
        monthList.add(monthBean);
        monthBean = new MonthBean("November", "11");
        monthList.add(monthBean);
        monthBean = new MonthBean("December", "12");
        monthList.add(monthBean);


        return monthList;
    }

    public ArrayList<YearBean> getAllYears() {
        ArrayList<YearBean> yearList = new ArrayList<>();

        YearBean yearBean;

        yearBean = new YearBean("2016", "2016");
        yearList.add(yearBean);
        yearBean = new YearBean("2017", "2017");
        yearList.add(yearBean);
        yearBean = new YearBean("2018", "2018");
        yearList.add(yearBean);
        yearBean = new YearBean("2019", "2019");
        yearList.add(yearBean);
        yearBean = new YearBean("2020", "2020");
        yearList.add(yearBean);
        yearBean = new YearBean("2021", "2021");
        yearList.add(yearBean);
        yearBean = new YearBean("2022", "2022");
        yearList.add(yearBean);
        yearBean = new YearBean("2023", "2023");
        yearList.add(yearBean);
        yearBean = new YearBean("2024", "2024");
        yearList.add(yearBean);
        yearBean = new YearBean("2025", "2025");
        yearList.add(yearBean);
        yearBean = new YearBean("2026", "2026");
        yearList.add(yearBean);
        yearBean = new YearBean("2027", "2027");
        yearList.add(yearBean);
        yearBean = new YearBean("2028", "2028");
        yearList.add(yearBean);
        yearBean = new YearBean("2029", "2029");
        yearList.add(yearBean);
        yearBean = new YearBean("2030", "2030");
        yearList.add(yearBean);


        return yearList;
    }

    @SuppressLint("StaticFieldLeak")
    public class GetTargetList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(TargetActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading List Please Wait");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "gettargetdata"));
            nameValuePair.add(new BasicNameValuePair("month", params[0]));
            nameValuePair.add(new BasicNameValuePair("year", params[1]));
            if (selectedDistributerBean.getFirm_name().equals("Total"))
                nameValuePair.add(new BasicNameValuePair("territory_id", params[2]));
            else
                nameValuePair.add(new BasicNameValuePair("distributor_id", params[2]));
            nameValuePair.add(new BasicNameValuePair("status", params[3]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            //  Log.e("Json object", "" + jsonObject);
            List<TargetBean> targetList = new ArrayList<>();
            if (jsonObject != null) {

                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Utils.showToast(TargetActivity.this, jsonObject.getString("message"));
                        JSONArray mJsonArray = jsonObject.getJSONArray("list");
                        GsonBuilder builder = new GsonBuilder();
                        Gson mGson = builder.create();
                        targetList = Arrays.asList(mGson.fromJson(mJsonArray.toString(), TargetBean[].class));

                        float categoriesTargetTotal = 0;
                        float categoriesAchievementTotal = 0;
                        float categoriesPercentageTotal = 0;

                        for (TargetBean targetBean : targetList) {
                            categoriesTargetTotal = categoriesTargetTotal + Float.parseFloat(targetBean.getTarget());
                            categoriesAchievementTotal = categoriesAchievementTotal + Float.parseFloat(targetBean.getCat_achived());
//                        categoriesPercentageTotal = categoriesPercentageTotal + Math.round(Float.parseFloat(targetBean.getCat_per()));
                        }

                        tvTargetTotal.setText("" + (int) categoriesTargetTotal);
                        tvAchievementTotal.setText("" + (int) categoriesAchievementTotal);
                        if (categoriesTargetTotal != 0) {
                            categoriesPercentageTotal = (categoriesAchievementTotal / categoriesTargetTotal) * 100;
                            tvPercentageTotal.setText("" + Math.round(categoriesPercentageTotal) + "%");
                        } else {
                            tvPercentageTotal.setText("0 %");
                        }
                    } else {
                        tvTargetTotal.setText("0");
                        tvAchievementTotal.setText("0");

                        tvPercentageTotal.setText("0 %");
                        Utils.showAlertDialog(TargetActivity.this, "Target", jsonObject.getString("message"), true);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    tvTargetTotal.setText("0");
                    tvAchievementTotal.setText("0");
                    tvPercentageTotal.setText("0 %");
                }

            } else {
                tvTargetTotal.setText("0");
                tvAchievementTotal.setText("0");

                tvPercentageTotal.setText("0 %");
                Utils.showAlertDialog(TargetActivity.this, "Target", "Improper response from server", true);

            }
            mAdapter = new TargetExpandableListAdapter(TargetActivity.this, targetList);
            lvExpTarget.setAdapter(mAdapter);

            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mDistributorList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(TargetActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Distributor List please wait");
            mDialog.setCancelable(false);
            if (mDistributerList.isEmpty()) {
                mDialog.show();
            }
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList();
            nameValuePair.add(new BasicNameValuePair("method", "getdistributorlist"));
            nameValuePair.add(new BasicNameValuePair("territory_id", params[0]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        if (mDistributerList.isEmpty())
                            Utils.showToast(TargetActivity.this, jsonObject.getString("message"));
                        ArrayList<DistributerBean> mDistributerArraylist = new ArrayList<>();
                        System.out.print(jsonObject.getString("message"));
                        JSONArray mJsonArray = jsonObject.getJSONArray("distribulist");
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            String distribution_id = mJsonArray.getJSONObject(i).getString("distribution_id");
                            String contact_person_name = mJsonArray.getJSONObject(i).getString("contact_person_name");
                            String email_id = mJsonArray.getJSONObject(i).getString("email_id");
                            String mobile_no = mJsonArray.getJSONObject(i).getString("mobile_no");
                            String phone_no = mJsonArray.getJSONObject(i).getString("phone_no");
                            String district_id = mJsonArray.getJSONObject(i).getString("district_id");
                            String city_id = mJsonArray.getJSONObject(i).getString("city_id");
                            String country_id = mJsonArray.getJSONObject(i).getString("country_id");
                            String firm_name = mJsonArray.getJSONObject(i).getString("firm_name");
                            String tin = mJsonArray.getJSONObject(i).getString("tin");
                            String cst_no = mJsonArray.getJSONObject(i).getString("cst_no");
                            String address_firm = mJsonArray.getJSONObject(i).getString("address_firm");
                            String address_godown = mJsonArray.getJSONObject(i).getString("address_godown");
                            String constitution_firm = mJsonArray.getJSONObject(i).getString("constitution_firm");
                            String bank_name = mJsonArray.getJSONObject(i).getString("bank_name");
                            String branch = mJsonArray.getJSONObject(i).getString("branch");
                            String account_no = mJsonArray.getJSONObject(i).getString("account_no");
                            String ifsc_code = mJsonArray.getJSONObject(i).getString("ifsc_code");
                            String activation_status = mJsonArray.getJSONObject(i).getString("activation_status");
                            String designation_id = mJsonArray.getJSONObject(i).getString("designation_id");
                            String territory_id = mJsonArray.getJSONObject(i).getString("territory_id");
                            String state_id = mJsonArray.getJSONObject(i).getString("state_id");
                            mDistributerArraylist.add(new DistributerBean(distribution_id, contact_person_name, email_id, mobile_no, phone_no, district_id, city_id, country_id, firm_name, tin, cst_no, address_firm, address_godown, constitution_firm, bank_name, branch, account_no, ifsc_code, activation_status, designation_id, territory_id, state_id));
                        }

                        mDistributerList.clear();
                        mDistributerList.addAll(mDistributerArraylist);

                        mDistributerList.add(0, new DistributerBean("-1", "", "", "", "", "", "", "", "Total", "", "", "", "", "", "", "", "", "", "", "", "", ""));

                        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(TargetActivity.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
                        mDistributerListPref.putObject(Constant.DISTRIBUTOR_LIST_OBJ, mDistributerArraylist);
                        mDistributerListPref.commit();

                    } else {
                        Utils.showToast(TargetActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertDialog(TargetActivity.this, "Target", "Improper response from server", true);
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }
}