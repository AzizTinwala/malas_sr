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
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.MonthBean;
import com.malas.appsr.malasapp.BeanClasses.ProductivityBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.BeanClasses.YearBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.adapter.MonthAdapter;
import com.malas.appsr.malasapp.adapter.ProductivityAdapter;
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
import java.util.Objects;

/**
 * Created by Arwa on 10/11/2017.
 */

@SuppressWarnings("ALL")
public class ProductivityReport extends AppCompatActivity {
    ArrayList<MonthBean> mMonthList;
    ArrayList<YearBean> mYearList;
    Context context;
    DistributerBean selectedDistributerBean;
    MonthAdapter monthAdapter;
    YearAdapter yearAdapter;
    ArrayList<ProductivityBean> productiveBeanArrayList;
    ArrayList<ProductivityBean> nonproductiveBeanArrayList;
    ArrayList<ProductivityBean> productiveOutletNameBeanArrayList;
    ProductivityAdapter productivityAdapter;
    ArrayList<ProductivityBean> productiveNonProductive = new ArrayList();
    private ListView lvProductivity;
    private EditText spnr_financialYear, spnr_financialMonth, spnr_distributor;
    private TextView tv_territory;
    private TextView tvTotalOutletVisit;
    private TextView tvTotalOutletProductive;
    private TextView tvProductivity;
    private UserLoginInfoBean mUserLoginInfoBean;
    private TextView tvEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productivity_report);
        context = ProductivityReport.this;
        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("Report");
        lvProductivity = (ListView) findViewById(R.id.lvProductivity);
        spnr_financialYear = (EditText) findViewById(R.id.spnr_financialYear);
        spnr_financialMonth = (EditText) findViewById(R.id.spnr_financialMonth);
        tv_territory = (TextView) findViewById(R.id.tv_territory);
        tvEmptyList = (TextView) findViewById(R.id.tvEmptyList);

        tvTotalOutletVisit = (TextView) findViewById(R.id.tv_total_outlet_visit);
        tvTotalOutletProductive = (TextView) findViewById(R.id.tv_total_outlet_productive);
        tvProductivity = (TextView) findViewById(R.id.tv_productivity);


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
            new getProductivity().execute(year, month);
        }
    }

    public class getProductivity extends AsyncTask<String, Void, JSONObject> {
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
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("method", "getproductivity"));
            nameValuePair.add(new BasicNameValuePair("year", params[0]));
            nameValuePair.add(new BasicNameValuePair("month", params[1]));
            nameValuePair.add(new BasicNameValuePair("userId", mUserLoginInfoBean.getUserId()));/*mUserLoginInfoBean.getUser_id())*/
            JSONObject jsonObjectFromUrl = new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
            return jsonObjectFromUrl;

        }

        @Override
        protected void onPostExecute(JSONObject jsonResponse) {
            if (jsonResponse != null) {
                lvProductivity.setVisibility(View.VISIBLE);
                tvEmptyList.setVisibility(View.GONE);
                productiveBeanArrayList = new ArrayList<>();
                nonproductiveBeanArrayList = new ArrayList<>();
                productiveOutletNameBeanArrayList = new ArrayList<>();
                try {
                    if (jsonResponse.getString("success").equalsIgnoreCase("true")) {

                        if (jsonResponse.getString("producNonProductive").equalsIgnoreCase("both")) {

                            productiveNonProductive.clear();
                            JSONArray mJsonArray = jsonResponse.getJSONArray("productiveList");
                            JSONArray mJsonArrayproductiveoutletNameData = jsonResponse.getJSONArray("productiveoutletNameData");

                            JSONArray mJsonArrayNonProductive = jsonResponse.getJSONArray("nonproductiveList");

                            for (int i = 0; i < mJsonArray.length(); i++) {
                                JSONObject mJsonobj = mJsonArray.getJSONObject(i);
                                int totalOutletCount = mJsonobj.getInt("OUTLET COUNT");
                                String unitimeDate = mJsonobj.getString("UNITIME");
                                String date = mJsonobj.getString("ORDER_DAY");
                                //Date Added to List
                                ProductivityBean productivityBean = new ProductivityBean(date, totalOutletCount);
                                productiveBeanArrayList.add(productivityBean);
                            }
                            for (int i = 0; i < mJsonArrayproductiveoutletNameData.length(); i++) {
                                JSONObject mJsonobj = mJsonArrayproductiveoutletNameData.getJSONObject(i);
                                String totalOutletNAme = mJsonobj.getString("OUTLET NAME");

                                String date = mJsonobj.getString("ORDER_DAY");
                                //Date Added to List
                                ProductivityBean productivityBean = new ProductivityBean(date, totalOutletNAme);
                                productiveOutletNameBeanArrayList.add(productivityBean);
                            }
                            for (int i = 0; i < mJsonArrayNonProductive.length(); i++) {
                                JSONObject mJsonobj = mJsonArrayNonProductive.getJSONObject(i);
                                int totalOutletCount = mJsonobj.getInt("OUTLET COUNT");
                                String unitimeDate = mJsonobj.getString("UNITIME");
                                String date = mJsonobj.getString("ORDER_DAY");
                                //Date Added to List
                                ProductivityBean productivityBean = new ProductivityBean(date, totalOutletCount);
                                nonproductiveBeanArrayList.add(productivityBean);

                            }
                            boolean isDateEqual = false;
                            int sum = 0;

                            for (int i = 0; i < productiveBeanArrayList.size(); i++) {
                                isDateEqual = false;
                                sum = 0;
                                for (int j = 0; j < nonproductiveBeanArrayList.size(); j++) {
                                    if (productiveBeanArrayList.get(i).getDate().equals(nonproductiveBeanArrayList.get(j).getDate())) {
                                        isDateEqual = true;
                                        sum = productiveBeanArrayList.get(i).getOutletCount() + nonproductiveBeanArrayList.get(j).getOutletCount();
                                    }
                                }
                                if (!isDateEqual) {
                                    sum = productiveBeanArrayList.get(i).getOutletCount() + 0;
                                }
                                ProductivityBean productivityBean = new ProductivityBean(productiveBeanArrayList.get(i).getDate(), sum);
                                productiveNonProductive.add(productivityBean);


                            }

                            int sum1 = 0;
                            for (int i = 0; i < productiveNonProductive.size(); i++)
                                sum1 += productiveNonProductive.get(i).getOutletCount();

                            int sum2 = 0;
                            for (int i = 0; i < productiveBeanArrayList.size(); i++)
                                sum2 += productiveBeanArrayList.get(i).getOutletCount();


                           /* for (int j = 0; j < nonproductiveBeanArrayList.size(); j++) {
                                sum = 0;

                                for (int k = 0; k < productiveNonProductive.size(); k++) {
                                    if (!nonproductiveBeanArrayList.get(j).getDate().equals(productiveNonProductive.get(k).getDate())) {
                                        sum = nonproductiveBeanArrayList.get(j).getOutletCount() + 0;
                                        ProductivityBean productivityBean = new ProductivityBean(nonproductiveBeanArrayList.get(j).getDate(), sum);
                                        productiveNonProductive.add(productivityBean);

                                    }
                                }

                            }*/
                           /* for (int i = 0; i < productiveNonProductive.size(); i++) {
                                Log.v("COUNT PRODUCTIVITY", productiveNonProductive.get(i).getDate() + " " + productiveNonProductive.get(i).getOutletCount());
                            }
*/


                            int totalProductiveCount = jsonResponse.getInt("totalProductiveCount");
                            int totalnonProductiveCount = jsonResponse.getInt("totalnonProductiveCount");

                            int productivityTotal = 0;
                            int productiveNonProductivCount = totalProductiveCount + totalnonProductiveCount;
                         tvTotalOutletVisit.setText(sum1 + "");
                            // tvTotalOutletVisit.setText(productiveNonProductivCount + "");
                            //tvTotalOutletProductive.setText(totalProductiveCount + "");
                            tvTotalOutletProductive.setText(sum2 + "");
                            if (sum1 != 0) {
                                //(int) Math.ceil(a / 100.0)
                                // productivityTotal = (int) Math.round(((double) totalProductiveCount / (double) productiveNonProductivCount) * 100);
                                productivityTotal = (int) Math.round(((double) sum2 / (double) sum1) * 100);
                            } else {
                                productivityTotal = 0;
                            }
                            tvProductivity.setText(productivityTotal + " %");

                        } else if (jsonResponse.getString("producNonProductive").equalsIgnoreCase("productive")) {

                            JSONArray mJsonArray = jsonResponse.getJSONArray("productiveList");
                            JSONArray mJsonArrayproductiveoutletNameData = jsonResponse.getJSONArray("productiveoutletNameData");

                            productiveNonProductive.clear();
                            for (int i = 0; i < mJsonArray.length(); i++) {
                                JSONObject mJsonobj = mJsonArray.getJSONObject(i);
                                int totalOutletCount = mJsonobj.getInt("OUTLET COUNT");
                                String unitimeDate = mJsonobj.getString("UNITIME");
                                String date = mJsonobj.getString("ORDER_DAY");
                                //Date Added to List
                                ProductivityBean productivityBean = new ProductivityBean(date, totalOutletCount);
                                productiveBeanArrayList.add(productivityBean);
                                productiveNonProductive.add(productivityBean);
                            }
                            for (int i = 0; i < mJsonArrayproductiveoutletNameData.length(); i++) {
                                JSONObject mJsonobj = mJsonArrayproductiveoutletNameData.getJSONObject(i);
                                String totalOutletNAme = mJsonobj.getString("OUTLET NAME");

                                String date = mJsonobj.getString("ORDER_DAY");
                                //Date Added to List
                                ProductivityBean productivityBean = new ProductivityBean(date, totalOutletNAme);
                                productiveOutletNameBeanArrayList.add(productivityBean);
                            }
                            int totalProductiveCount = jsonResponse.getInt("totalProductiveCount");
                            int sum1 = 0;
                            for (int i = 0; i < productiveNonProductive.size(); i++)
                                sum1 += productiveNonProductive.get(i).getOutletCount();

                            int sum2 = 0;
                            for (int i = 0; i < productiveBeanArrayList.size(); i++)
                                sum2 += productiveBeanArrayList.get(i).getOutletCount();


                            int productivityTotal = 0;
                            //  int productiveNonProductivCount = totalProductiveCount;
                           /* tvTotalOutletVisit.setText(productiveNonProductivCount + "");
                            tvTotalOutletProductive.setText(totalProductiveCount + "");*/
                            tvTotalOutletVisit.setText(sum1 + "");
                            tvTotalOutletProductive.setText(sum2 + "");
                            if (sum1 != 0) {
                                //(int) Math.ceil(a / 100.0)
                                productivityTotal = (int) Math.round(((double) sum2 / (double) sum1) * 100);
                                //productivityTotal = (int) Math.round(((double) totalProductiveCount / (double) productiveNonProductivCount) * 100);
                            } else {
                                productivityTotal = 0;
                            }
                            tvProductivity.setText(productivityTotal + " %");
                        } else if (jsonResponse.getString("producNonProductive").equalsIgnoreCase("nonproductive")) {


                            JSONArray mJsonArrayNonProductive = jsonResponse.getJSONArray("nonproductiveList");

                            productiveNonProductive.clear();
                            for (int i = 0; i < mJsonArrayNonProductive.length(); i++) {
                                JSONObject mJsonobj = mJsonArrayNonProductive.getJSONObject(i);
                                int totalOutletCount = mJsonobj.getInt("OUTLET COUNT");
                                String unitimeDate = mJsonobj.getString("UNITIME");
                                String date = mJsonobj.getString("ORDER_DAY");
                                //Date Added to List
                                ProductivityBean productivityBean = new ProductivityBean(date, totalOutletCount);
                                nonproductiveBeanArrayList.add(productivityBean);

                                productiveNonProductive.add(productivityBean);

                            }
                            int sum1 = 0;
                            for (int i = 0; i < productiveNonProductive.size(); i++)
                                sum1 += productiveNonProductive.get(i).getOutletCount();

                            int totalnonProductiveCount = jsonResponse.getInt("totalnonProductiveCount");

                            int productivityTotal = 0;
                            int productiveNonProductivCount = totalnonProductiveCount;
                            //  tvTotalOutletVisit.setText( productiveNonProductivCount);
                            tvTotalOutletVisit.setText(sum1);
                            tvTotalOutletProductive.setText("0");
                            if (sum1 != 0) {
                                //(int) Math.ceil(a / 100.0)
                                productivityTotal = (int) Math.round(((double) 0 / (double) sum1) * 100);
                            } else {
                                productivityTotal = 0;
                            }
                            tvProductivity.setText(productivityTotal + " %");
                        }
                        //  Log.v("SIZE", productiveNonProductive.size() + " " + productiveBeanArrayList.size() + " " + nonproductiveBeanArrayList.size());
                       /* int productNonProd = 0, productive = 0;
                        int productivity = 0;
                        int productivityTotal = 0;
                        if (productiveNonProductive != null && productiveBeanArrayList != null) {
                            for (int k = 0; k < productiveNonProductive.size(); k++) {
                                productNonProd += productiveNonProductive.get(k).getOutletCount();
                                productive += productiveBeanArrayList.get(k).getOutletCount();
                            }
                        } else {
                            ProductivityBean productivityBean = new ProductivityBean("", 0);
                            productiveBeanArrayList.add(productivityBean);
                            for (int k = 0; k < productiveNonProductive.size(); k++) {
                                productNonProd += productiveNonProductive.get(k).getOutletCount();
                                productive += productiveBeanArrayList.get(k).getOutletCount();
                            }
                        }*/

                        int productivity = 0;
                        ArrayList<Integer> productivityList = new ArrayList();
                        for (int k = 0; k < productiveNonProductive.size(); k++) {
                            if (productiveNonProductive.get(k).getOutletCount() != 0) {
                                productivity = (int) Math.round(((double) productiveBeanArrayList.get(k).getOutletCount() / (double) productiveNonProductive.get(k).getOutletCount()) * 100);

                                productivityList.add(productivity);
                            } else {
                                productivity = 0;
                                productivityList.add(productivity);
                            }
                        }
                      /*  ArrayList<ArrayList<ProductivityBean>> combined2d = new ArrayList<ArrayList<ProductivityBean>>();
                        combined2d.add(productiveNonProductive);
                        combined2d.add(productiveBeanArrayList);
                  //      combined2d.add(productivityList);
                        ArrayList<ProductivityBean> beans= combined2d.get(0);

                        Log.v("SIZE",beans.size()+"");
*/
                        if (productiveBeanArrayList != null) {
                            productivityAdapter = new ProductivityAdapter(context, productiveNonProductive, productiveBeanArrayList, productivityList);
                            lvProductivity.setAdapter(productivityAdapter);

                        } else {
                            ProductivityBean productivityBean = new ProductivityBean("", 0);
                            productiveBeanArrayList.add(productivityBean);
                            productivityAdapter = new ProductivityAdapter(context, productiveNonProductive, productiveBeanArrayList, productivityList);
                            lvProductivity.setAdapter(productivityAdapter);

                        }

                    } else {
                        tvEmptyList.setVisibility(View.VISIBLE);
                        lvProductivity.setVisibility(View.GONE);
                        tvEmptyList.setText(jsonResponse.getString("message"));
                        tvTotalOutletVisit.setText(0 + "");
                        tvTotalOutletProductive.setText(0 + "");
                        tvProductivity.setText(0 + "%");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    tvEmptyList.setVisibility(View.VISIBLE);
                    lvProductivity.setVisibility(View.GONE);
                    tvEmptyList.setText("Oops! Something Went Wrong");
                    tvTotalOutletVisit.setText(0 + "");
                    tvTotalOutletProductive.setText(0 + "");
                    tvProductivity.setText(0 + "%");
                }

            } else {
                tvEmptyList.setVisibility(View.VISIBLE);
                lvProductivity.setVisibility(View.GONE);
                tvEmptyList.setText("Improper response from server");
                tvTotalOutletVisit.setText(0 + "");
                tvTotalOutletProductive.setText(0 + "");
                tvProductivity.setText(0 + "%");

            }
            mDialog.dismiss();
        }
    }

}
