package com.malas.appsr.malasapp.activities;

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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.malas.appsr.malasapp.BeanClasses.AverageBillValueBean;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.MonthBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.BeanClasses.YearBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.AverageBillValueAdapter;
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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

//import android.util.Log;

/**
 * Created by Arwa on 10/11/2017.
 */

@SuppressWarnings("ALL")
public class AverageBillReport extends AppCompatActivity {
    AverageBillValueBean averageBillValueBean;
    ArrayList<AverageBillValueBean> averageBillValueBeanArrayList;
    AverageBillValueAdapter averageBillValueAdapter;
    ArrayList<MonthBean> mMonthList;
    ArrayList<YearBean> mYearList;
    Context context;
    DistributerBean selectedDistributerBean;
    MonthAdapter monthAdapter;
    YearAdapter yearAdapter;
    private ListView lvAvgBillValue;
    private EditText spnr_financialYear, spnr_financialMonth, spnr_distributor;
    private TextView tv_territory;
    private TextView tvtotalOrderValue;
    private TextView tvTotalOutletCount;
    private TextView tvAverageBillValue;
    private UserLoginInfoBean mUserLoginInfoBean;
    private TextView tvEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_bill_report);
        context = AverageBillReport.this;
        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("Report");
        lvAvgBillValue = (ListView) findViewById(R.id.lvAvgBillValue);
        spnr_financialYear = (EditText) findViewById(R.id.spnr_financialYear);
        spnr_financialMonth = (EditText) findViewById(R.id.spnr_financialMonth);
        tvEmptyList = (TextView) findViewById(R.id.tvEmptyList);

        tv_territory = (TextView) findViewById(R.id.tv_territory);
        tvtotalOrderValue = (TextView) findViewById(R.id.tv_total_order_value);
        tvTotalOutletCount = (TextView) findViewById(R.id.tv_total_outlet_count);
        tvAverageBillValue = (TextView) findViewById(R.id.tv_average_bill_value);


        mMonthList = new ArrayList<>();
        mMonthList = getAllMonths();
        mYearList = new ArrayList<>();
        mYearList = getAllYears();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        tv_territory.setText(mUserLoginInfoBean.getTerritoryName());
        spnr_financialYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mYearList != null) {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.statelist_dialogbox);
                    final EditText etserach = (EditText) dialog.findViewById(R.id.edittext_dialog);
                    final ListView listView = (ListView) dialog.findViewById(R.id.dialogbox_listview);
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
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String year = YearAdapter.resultArrayshort.get(position).getName();
                            spnr_financialYear.setText(year);
                            try {
                                if (mMonthList != null) {
                                    for (MonthBean month : mMonthList) {
                                        if (month.getName().equals(spnr_financialMonth.getText().toString().trim())) {

                                            getData(spnr_financialYear.getText().toString().trim(), month.getId());

                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    });
                } else {

                }

            }
        });

        spnr_financialMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMonthList != null) {

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.statelist_dialogbox);
                    final EditText etserach = (EditText) dialog.findViewById(R.id.edittext_dialog);
                    final ListView listView = (ListView) dialog.findViewById(R.id.dialogbox_listview);
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
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String countryname = MonthAdapter.resultArrayshort.get(position).getName();
                            spnr_financialMonth.setText(countryname);
                            dialog.dismiss();
                            // Log.v("MONTH YEAR", spnr_financialMonth.getText().toString().trim() + "" + mYearList.get(position).getName());
                            try {
                                if (mMonthList != null) {
                                    for (MonthBean month : mMonthList) {
                                        if (month.getName().equals(spnr_financialMonth.getText().toString().trim())) {

                                            getData(spnr_financialYear.getText().toString().trim(), month.getId());

                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } else {

                }

            }
        });

        spnr_financialMonth.setText(getCurrentMonth());
        spnr_financialYear.setText(getCurrentYear());
        try {
            if (mMonthList != null) {
                for (MonthBean month : mMonthList) {
                    if (month.getName().equals(spnr_financialMonth.getText().toString().trim())) {

                        getData(spnr_financialYear.getText().toString().trim(), month.getId());

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String getCurrentMonth() {
        DateFormat format = new SimpleDateFormat("MMMM");
        Date date = new Date();

        return format.format(date);
    }

    public String getCurrentYear() {
        DateFormat format = new SimpleDateFormat("yyyy");
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
        yearBean = new YearBean("2030", "2031");
        yearList.add(yearBean);
        yearBean = new YearBean("2030", "2032");
        yearList.add(yearBean);
        yearBean = new YearBean("2030", "2033");
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
            new getAverageBillValue().execute(year, month);
        }
    }

    public class getAverageBillValue extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(context);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Logging in please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("method", "getaveragebillvalue"));
            nameValuePair.add(new BasicNameValuePair("year", params[0]));
            nameValuePair.add(new BasicNameValuePair("month", params[1]));
            nameValuePair.add(new BasicNameValuePair("userId", mUserLoginInfoBean.getUserId()));
            JSONObject jsonObjectFromUrl = new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
            return jsonObjectFromUrl;

        }

        @Override
        protected void onPostExecute(JSONObject jsonResponse) {
            if (jsonResponse != null) {
                lvAvgBillValue.setVisibility(View.VISIBLE);
                tvEmptyList.setVisibility(View.GONE);
                try {
                    if (jsonResponse.getString("success").equalsIgnoreCase("true")) {
                        JSONArray mJsonArray = jsonResponse.getJSONArray("averagebillvalue");
                        averageBillValueBeanArrayList = new ArrayList<>();
                        ArrayList<String> dateList = new ArrayList<>();
                        ArrayList<String> uniquedateList = new ArrayList<>();
                        ArrayList<String> valueList = new ArrayList<>();
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            JSONObject mJsonobj = mJsonArray.getJSONObject(i);
                            String visitedOutletCount = mJsonobj.getString("OUTLET Id");
                            int value = mJsonobj.getInt("TOTAL");
                            String unitimeDate = mJsonobj.getString("UNITIME");
                            String date = mJsonobj.getString("ORDER_DAY");
                            //Date Added to List
                            dateList.add(date);
                            averageBillValueBean = new AverageBillValueBean(date, value, visitedOutletCount, "0");
                            averageBillValueBeanArrayList.add(averageBillValueBean);

                        }
                        //
                        Set<String> uniqueDateListSet = new LinkedHashSet<>(dateList);
                        System.out.printf("\nUnique values using TreeSet - Sorted order: %s%n", uniqueDateListSet);
                        Iterator<String> itr = uniqueDateListSet.iterator();
                        while (itr.hasNext()) {
                            uniquedateList.add(itr.next());
                        }
                        //Outlet List Unique SEt
                        Set<String> outletList = new LinkedHashSet<>();
                        ArrayList sumValue = new ArrayList();
                        ArrayList countOutlet = new ArrayList();
                        for (int i = 0; i < uniquedateList.size(); i++) {
                            int sum = 0;
                            int count = 0;
                            for (int j = 0; j < averageBillValueBeanArrayList.size(); j++) {
                                if (averageBillValueBeanArrayList.get(j).getOrderDate().equals(uniquedateList.get(i).toString())) {
                                   /* Log.v("orderdate n uniquedate", averageBillValueBeanArrayList.get(j).getOrderDate() + " " + uniquedateList.get(i).toString());
                                    Log.v("value", j + " " + averageBillValueBeanArrayList.get(j).getValue());
                                   */
                                    sum = averageBillValueBeanArrayList.get(j).getValue() + sum;
                                    outletList.add(averageBillValueBeanArrayList.get(j).getOutletCount());
                                }
                            }
                            //count of unique outlet id
                            count = outletList.size();
                            //  Log.v("outletList", count + "");
                            countOutlet.add(i, count);
                            //sum of value i.e: mrp*quantity sumation group by unique date
                            sumValue.add(i, sum);
                            //Log.v("value", sumValue.get(i).toString());
                            outletList.clear();
                        }
                        ArrayList averageBillValue = new ArrayList();
                        for (int i = 0; i < uniquedateList.size(); i++) {
                            averageBillValue.add((int) Math.ceil(((double) ((int) sumValue.get(i))) / ((double) ((int) countOutlet.get(i)))));
                        }
                        averageBillValueAdapter = new AverageBillValueAdapter(context, uniquedateList, countOutlet, sumValue, averageBillValue);
                        lvAvgBillValue.setAdapter(averageBillValueAdapter);
                        int outletCount = 0, valueTotal = 0;
                        for (int i = 0; i < uniquedateList.size(); i++) {
                            outletCount += ((int) countOutlet.get(i));
                            valueTotal += ((int) sumValue.get(i));


                        }
                        tvTotalOutletCount.setText(outletCount + "");
                        tvtotalOrderValue.setText(valueTotal + "");
                        tvAverageBillValue.setText((int) Math.ceil((double) valueTotal / (double) outletCount) + "");


                    } else {
                        lvAvgBillValue.setVisibility(View.GONE);
                        tvEmptyList.setVisibility(View.VISIBLE);
                        tvEmptyList.setText(jsonResponse.getString("message"));
                        tvTotalOutletCount.setText(0 + "");
                        tvtotalOrderValue.setText(0 + "");
                        tvAverageBillValue.setText(0 + "");

                    }
                } catch (JSONException e) {
                    lvAvgBillValue.setVisibility(View.GONE);
                    tvEmptyList.setVisibility(View.VISIBLE);
                    tvEmptyList.setText("Oops! Something Went Wrong");
                    tvTotalOutletCount.setText(0 + "");
                    tvtotalOrderValue.setText(0 + "");
                    tvAverageBillValue.setText(0 + "");

                    e.printStackTrace();
                }

            } else {
                lvAvgBillValue.setVisibility(View.GONE);
                tvEmptyList.setVisibility(View.VISIBLE);
                tvEmptyList.setText("Improper response from server");
                tvTotalOutletCount.setText(0 + "");
                tvtotalOrderValue.setText(0 + "");
                tvAverageBillValue.setText(0 + "");

            }
            mDialog.dismiss();
        }
    }

}
