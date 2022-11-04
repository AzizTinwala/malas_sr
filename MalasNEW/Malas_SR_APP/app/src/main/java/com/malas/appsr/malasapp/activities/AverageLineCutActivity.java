package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.google.android.material.snackbar.Snackbar;

import com.malas.appsr.malasapp.BeanClasses.AverageLineCutBean;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.MonthBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.BeanClasses.YearBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.AverageLineCutAdapter;
import com.malas.appsr.malasapp.adapter.MonthAdapter;
import com.malas.appsr.malasapp.adapter.YearAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

//import android.util.Log;


public class AverageLineCutActivity extends AppCompatActivity {
    ArrayList<MonthBean> mMonthList;
    ArrayList<YearBean> mYearList;
    Context context;
    DistributerBean selectedDistributerBean;
    MonthAdapter monthAdapter;
    YearAdapter yearAdapter;
    AverageLineCutBean averageLineCutBeans;
    ArrayList<AverageLineCutBean> averageLineCutBeanArrayList;
    AverageLineCutAdapter averageLineCutAdapter;
    private ListView lvAvgLineCut;
    private EditText spnr_financialYear, spnr_financialMonth;
    private TextView tvtotalSKU;
    private TextView tvTotalLineCut;
    private UserLoginInfoBean mUserLoginInfoBean;
    private TextView tvTotalCountOutlet;
    private TextView tvCategoryCount;
    private TextView tv_total_sku;
    private TextView tv_total_category;
    private TextView tvEmptyList;
    private LinearLayout llAverageLineCut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_line_cut);
        context = AverageLineCutActivity.this;
        initView();
    }

    private void initView() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Report");
        llAverageLineCut = findViewById(R.id.ll_layout);
        lvAvgLineCut = findViewById(R.id.lvAvgLineCut);
        spnr_financialYear = findViewById(R.id.spnr_financialYear);
        spnr_financialMonth = findViewById(R.id.spnr_financialMonth);
        tvEmptyList = findViewById(R.id.tvEmptyList);
        TextView tv_territory = findViewById(R.id.tv_territory);
        tv_total_sku = findViewById(R.id.tv_total_sku);
        tvTotalCountOutlet = findViewById(R.id.tv_total_count_outlet);
        tvCategoryCount = findViewById(R.id.tv_category_count);
        tv_total_category = findViewById(R.id.tv_total_category);
        tvtotalSKU = findViewById(R.id.tv_sku);
        tvTotalLineCut = findViewById(R.id.tv_total_line_cut);
        mMonthList = new ArrayList<>();
        mMonthList = getAllMonths();
        mYearList = new ArrayList<>();
        mYearList = getAllYears();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AverageLineCutActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        tv_territory.setText(mUserLoginInfoBean.getTerritoryName());


        spnr_financialYear.setOnClickListener(v -> {
            if (mYearList != null) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                yearAdapter = new YearAdapter(context, mYearList);
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
                    String year = YearAdapter.resultArrayshort.get(position).getName();
                    spnr_financialYear.setText(year);
                    try {
                        if (mMonthList != null) {
                            for (MonthBean month : mMonthList) {
                                if (month.getName().equals(spnr_financialMonth.getText().toString().trim())) {

                                    getData(spnr_financialYear.getText().toString().trim(), month.getId());
                                    new getTotalCategory().execute(spnr_financialYear.getText().toString().trim(), month.getId());
                                    new getTotalSKU().execute(spnr_financialYear.getText().toString().trim(), month.getId());

                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                });
            }

        });

        spnr_financialMonth.setOnClickListener(v -> {
            if (mMonthList != null) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                monthAdapter = new MonthAdapter(context, mMonthList);
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
                    // Log.v("MONTH YEAR", spnr_financialMonth.getText().toString().trim() + "" + mYearList.get(position).getName());
                    try {
                        if (mMonthList != null) {
                            for (MonthBean month : mMonthList) {
                                if (month.getName().equals(spnr_financialMonth.getText().toString().trim())) {

                                    getData(spnr_financialYear.getText().toString().trim(), month.getId());
                                    new getTotalCategory().execute(spnr_financialYear.getText().toString().trim(), month.getId());
                                    new getTotalSKU().execute(spnr_financialYear.getText().toString().trim(), month.getId());

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
/*
        spnr_financialYear.setText(getAllYears().get(0).getName());
*/
        spnr_financialYear.setText(getCurrentYear());
        try {
            if (mMonthList != null) {
                for (MonthBean month : mMonthList) {
                    if (month.getName().equals(spnr_financialMonth.getText().toString().trim())) {

                        getData(spnr_financialYear.getText().toString().trim(), month.getId());
                        new getTotalCategory().execute(spnr_financialYear.getText().toString().trim(), month.getId());
                        new getTotalSKU().execute(spnr_financialYear.getText().toString().trim(), month.getId());

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getCurrentMonth() {
        DateFormat format = new SimpleDateFormat("MMMM", Locale.getDefault());
        Date date = new Date();

        return format.format(date);
    }

    public String getCurrentYear() {
        DateFormat format = new SimpleDateFormat("yyyy", Locale.getDefault());
        Date date = new Date();

        return format.format(date);
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

    public void getData(String year, String month) {

        if (year.equals("")) {
            Toast.makeText(this, "Please Select Year", Toast.LENGTH_SHORT).show();
        } else if (month.equals("")) {
            Toast.makeText(this, "Please Select Month", Toast.LENGTH_SHORT).show();
        } else {
            new getAverageLineValue().execute(year, month);
        }
    }


    @SuppressLint("StaticFieldLeak")
    public class getAverageLineValue extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(context);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Logging in please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "average_line_cut"));
            nameValuePair.add(new BasicNameValuePair("year", params[0]));
            nameValuePair.add(new BasicNameValuePair("month", params[1]));
            nameValuePair.add(new BasicNameValuePair("userId", mUserLoginInfoBean.getUserId()));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);

        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject jsonResponse) {
            if (jsonResponse != null) {
                lvAvgLineCut.setVisibility(View.VISIBLE);
                tvEmptyList.setVisibility(View.GONE);
                try {
                    if (jsonResponse.getString("success").equalsIgnoreCase("true")) {
                        JSONObject skuCategoryCount = jsonResponse.getJSONObject("countSkuCategoryData");
                        // Log.v("val", skuCategoryCount.getString("SKU COUNT"));
                        JSONArray mJsonArray = jsonResponse.getJSONArray("averagelinedata");
                        averageLineCutBeanArrayList = new ArrayList<>();
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            JSONObject mJsonobj = mJsonArray.getJSONObject(i);
                            int visitedOutletCount = mJsonobj.getInt("OUTLET COUNT");
                            String distributorName = mJsonobj.getString("DISTRIBUTOR");
                            int skuCount = mJsonobj.getInt("SKU COUNT");

                            int categoryCount = mJsonobj.getInt("CATEGORY COUNT");
                            String unitimeDate = mJsonobj.getString("UNITIME");

                            String date = mJsonobj.getString("ORDER_DAY");
                            int lineCut = (int) Math.ceil(((double) skuCount / (double) visitedOutletCount));
                            averageLineCutBeans = new AverageLineCutBean(date, distributorName, visitedOutletCount, categoryCount, skuCount, lineCut);

                            averageLineCutBeanArrayList.add(averageLineCutBeans);

                        }
                        //     Toast.makeText(context, "avera" + averageLineCutBeanArrayList.get(0).getDate(), Toast.LENGTH_SHORT).show();

                        averageLineCutAdapter = new AverageLineCutAdapter(context, averageLineCutBeanArrayList);
                        lvAvgLineCut.setAdapter(averageLineCutAdapter);
                        int outletCountTotal = 0;
                        int skuCountTotal = skuCategoryCount.getInt("SKU COUNT");
                        int categoryCountTotal = skuCategoryCount.getInt("CATEGORY COUNT");
                        int outletCountTotalFromDb = skuCategoryCount.getInt("OUTLET COUNT");

                        for (int i = 0; i < averageLineCutBeanArrayList.size(); i++) {
                            outletCountTotal += averageLineCutBeanArrayList.get(i).getCountOutletVisited();


                         /*   skuCountTotal += averageLineCutBeanArrayList.get(i).getSkuCount();
                            categoryCountTotal += averageLineCutBeanArrayList.get(i).getCategoryCount();*/
                        }
                        tvTotalCountOutlet.setText(outletCountTotal + "");
                        tvtotalSKU.setText(skuCountTotal + "");
                        tvCategoryCount.setText(categoryCountTotal + "");
                        int totalLineCut = (int) Math.ceil(((double) skuCountTotal / (double) outletCountTotalFromDb));
                        tvTotalLineCut.setText(totalLineCut + "");
                    } else {
                        lvAvgLineCut.setVisibility(View.GONE);
                        tvEmptyList.setVisibility(View.VISIBLE);
                        tvTotalCountOutlet.setText(0 + "");
                        tvtotalSKU.setText(0 + "");
                        tvCategoryCount.setText(0 + "");
                        tvTotalLineCut.setText(0 + "");
                        tvEmptyList.setText(jsonResponse.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    lvAvgLineCut.setVisibility(View.GONE);
                    tvEmptyList.setVisibility(View.VISIBLE);
                    tvTotalCountOutlet.setText(0 + "");
                    tvtotalSKU.setText(0 + "");
                    tvCategoryCount.setText(0 + "");
                    tvTotalLineCut.setText(0 + "");
                    tvEmptyList.setText("OOPs! Something Went Wrong");
                }

            } else {
                lvAvgLineCut.setVisibility(View.GONE);
                tvEmptyList.setVisibility(View.VISIBLE);
                tvTotalCountOutlet.setText(0 + "");
                tvtotalSKU.setText(0 + "");
                tvCategoryCount.setText(0 + "");
                tvTotalLineCut.setText(0 + "");
                tvEmptyList.setText("Improper response from server");
            }
            mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class getTotalSKU extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(context);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Logging in please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getTotalSku"));
            nameValuePair.add(new BasicNameValuePair("year", params[0]));
            nameValuePair.add(new BasicNameValuePair("month", params[1]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);

        }

        @Override
        protected void onPostExecute(JSONObject jsonResponse) {
            if (jsonResponse != null) {
                try {
                    if (jsonResponse.getString("success").equalsIgnoreCase("true")) {
                        JSONObject mJsonObject = jsonResponse.getJSONObject("totalSKU");
                        String totalSKU = mJsonObject.getString("SKU TOTAL");

                        tv_total_sku.setText(totalSKU);
                    } else {
                        Snackbar.make(llAverageLineCut, jsonResponse.getString("message"), Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(llAverageLineCut, "OOPs! Something Went Wrong", Snackbar.LENGTH_SHORT).show();

                }

            } else {
                Snackbar.make(llAverageLineCut, "Improper response from server", Snackbar.LENGTH_SHORT).show();


            }
            mDialog.dismiss();
        }
    }


    @SuppressLint("StaticFieldLeak")
    public class getTotalCategory extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(context);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Logging in please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getTotalCategory"));
            nameValuePair.add(new BasicNameValuePair("year", params[0]));
            nameValuePair.add(new BasicNameValuePair("month", params[1]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);

        }

        @Override
        protected void onPostExecute(JSONObject jsonResponse) {
            if (jsonResponse != null) {
                try {
                    if (jsonResponse.getString("success").equalsIgnoreCase("true")) {
                        JSONObject mJsonObject = jsonResponse.getJSONObject("totalCategory");
                        String totalCategory = mJsonObject.getString("CATEGORY TOTAL");

                        tv_total_category.setText(totalCategory);
                    } else {
                        Snackbar.make(llAverageLineCut, jsonResponse.getString("message"), Snackbar.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(llAverageLineCut, "OOPs! Something Went Wrong", Snackbar.LENGTH_SHORT).show();

                }

            } else {
                Snackbar.make(llAverageLineCut, "Improper response from server", Snackbar.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }
    }

}
