package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.malas.appsr.malasapp.BeanClasses.ProductivityBean;
import com.malas.appsr.malasapp.BeanClasses.TargetAchievementProgress;
import com.malas.appsr.malasapp.BeanClasses.TargetBean;
import com.malas.appsr.malasapp.BeanClasses.TotalOutletCountHome;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.dbHandler.DatabaseHandler;
import com.malas.appsr.malasapp.util.ProgressView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class TopViewReport extends AppCompatActivity {
    private ProgressView productivityProgressView;
    private ProgressView targetAchievementProgressView;
    private TextView tvTargetValue;
    private TextView tvAchievementValue;
    private TextView tvTargetAchPercent;
    private TextView tvProductivity;
    private TextView tvTotalOutlet, tvBilledOutlet, tvUnBilledOutlet;
    private TextView tvTotalDS;
    private TextView tvUnBilledDS, tvBilledDS;
    private DatabaseHandler db;
    private UserLoginInfoBean mUserLoginInfoBean;
    ArrayList<ProductivityBean> productiveBeanArrayList;
    ArrayList<ProductivityBean> nonproductiveBeanArrayList;
    ArrayList<ProductivityBean> productiveOutletNameBeanArrayList;
    ArrayList<ProductivityBean> productiveNonProductive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topview_report);
        initView();
    }

    private void initView() {
        db = new DatabaseHandler(this);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Report");

        productivityProgressView = findViewById(R.id.progressView1);
        targetAchievementProgressView = findViewById(R.id.progressView2);
        tvTargetValue = findViewById(R.id.tv_progress_target_value);
        tvAchievementValue = findViewById(R.id.tv_progress_ach_value);
        tvTargetAchPercent = findViewById(R.id.tv_target_percent);
        tvProductivity = findViewById(R.id.tv_productivity);
        tvTotalOutlet = findViewById(R.id.tv_total_outlet_value);
        tvBilledOutlet = findViewById(R.id.tv_total_billed_value);
        tvUnBilledOutlet = findViewById(R.id.tv_total_unbilled_value);
        tvTotalDS = findViewById(R.id.tv_total_ds_value);
        tvBilledDS = findViewById(R.id.tv_total_billed_ds_value);
        tvUnBilledDS = findViewById(R.id.tv_total_unbilled_ds_value);
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(TopViewReport.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        progressView();

    }


    private void progressView() {

        //Progress
        // int[] colorList = new int[]{Color.GREEN, Color.YELLOW, Color.RED};
        //value from server product product 60 remaining 40
        //value from server 89% remaing 11
        //It meqns substract the value from 100
        productivityProgressView.setProgressColor(Color.parseColor("#d9d9d9"));
        productivityProgressView.setBackgroundColor(Color.parseColor("#00cccc"));
        targetAchievementProgressView.setProgressColor(Color.parseColor("#d9d9d9"));
        targetAchievementProgressView.setBackgroundColor(Color.parseColor("#00cccc"));

        if (Utils.isInternetConnected(TopViewReport.this)) {
            db.deleteAllProductivityRecords(mUserLoginInfoBean.getUserId());
            new getProductivity().execute(String.valueOf(month()), String.valueOf(year()));


        } else {
            TargetAchievementProgress targetAchievementProgress = db.getProductivity(String.valueOf(month()), String.valueOf(year()), mUserLoginInfoBean.getUserId());
            if (targetAchievementProgress != null) {

                if (targetAchievementProgress.getProductivity() != null && !targetAchievementProgress.getProductivity().equalsIgnoreCase("")) {
                    String val = targetAchievementProgress.getProductivity() + " %";
                    tvProductivity.setText(val);
                } else {
                    String val = 0 + " %";
                    tvProductivity.setText(val);
                }
                if (targetAchievementProgress.getProgressValueProductivity() != null && !targetAchievementProgress.getProgressValueProductivity().equalsIgnoreCase("")) {
                    productivityProgressView.setProgress(Float.parseFloat(targetAchievementProgress.getProgressValueProductivity()));

                } else {
                    productivityProgressView.setProgress(Float.parseFloat("1.0"));

                }
            }


            ///TARGET ACHIEVEEMNT

            TargetAchievementProgress targetAchievementProgress1 = db.getTargetAchievementHome(String.valueOf(month()), String.valueOf(year()), mUserLoginInfoBean.getUserId());
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


            //TOTAL OUTLET COUNT

            TotalOutletCountHome totalOutletCountHome = db.getTotalOutletHome(String.valueOf(month()), String.valueOf(year()), mUserLoginInfoBean.getUserId());
            if (totalOutletCountHome != null) {
                if (totalOutletCountHome.getTotalOutlet() != null && !totalOutletCountHome.getTotalOutlet().equalsIgnoreCase("")) {
                    String totalOutletCount = totalOutletCountHome.getTotalOutlet();
                    tvTotalOutlet.setText(totalOutletCount);
                } else {
                    String totalOutletCount = "0";
                    tvTotalOutlet.setText(totalOutletCount);
                }


                if (totalOutletCountHome.getBilledOutlet() != null && !totalOutletCountHome.getBilledOutlet().equalsIgnoreCase("")) {

                    String totalBilledCount = totalOutletCountHome.getBilledOutlet();
                    tvBilledOutlet.setText(totalBilledCount);


                } else {
                    String totalBilledCount = "0";
                    tvBilledOutlet.setText(totalBilledCount);

                }


                if (totalOutletCountHome.getUnbilledOutlet() != null && !totalOutletCountHome.getUnbilledOutlet().equalsIgnoreCase("")) {

                    String totalUnbilledOutlet = totalOutletCountHome.getUnbilledOutlet();
                    tvUnBilledOutlet.setText(totalUnbilledOutlet);


                } else {
                    String totalUnbilledOutlet = "0";
                    tvUnBilledOutlet.setText(totalUnbilledOutlet);

                }


            } else {
                String totalOutletCount = "0";
                String totalBilledCount = "0";
                String totalUnbilledOutlet = "0";

                tvTotalOutlet.setText(totalOutletCount);
                tvBilledOutlet.setText(totalBilledCount);
                tvUnBilledOutlet.setText(totalUnbilledOutlet);
            }


            //TOTAL DS

            TotalOutletCountHome totalDsCounr = db.getDSHome(String.valueOf(month()), String.valueOf(year()), mUserLoginInfoBean.getUserId());
            if (totalDsCounr != null) {
                if (totalDsCounr.getTotalDS() != null && !totalDsCounr.getTotalDS().equalsIgnoreCase("")) {
                    String totalDs = totalDsCounr.getTotalDS();
                    tvTotalDS.setText(totalDs);
                } else {
                    String totalDs = "0";
                    tvTotalDS.setText(totalDs);
                }


                if (totalDsCounr.getBilledDS() != null && !totalDsCounr.getBilledDS().equalsIgnoreCase("")) {

                    String totalBilledDSCount = totalDsCounr.getBilledDS();
                    tvBilledDS.setText(totalBilledDSCount);


                } else {
                    String totalBilledDSCount = "0";
                    tvBilledDS.setText(totalBilledDSCount);

                }


                if (totalDsCounr.getUnbilledDS() != null && !totalDsCounr.getUnbilledDS().equalsIgnoreCase("")) {

                    String totalUnbilledDS = totalDsCounr.getUnbilledDS();
                    tvUnBilledDS.setText(totalUnbilledDS);


                } else {
                    String totalUnbilledDS = "0";
                    tvUnBilledDS.setText(totalUnbilledDS);

                }


            } else {
                String totalDSCount = "0";
                String totalBilledDSCount = "0";
                String totalUnbilledDS = "0";

                tvTotalDS.setText(totalDSCount);
                tvBilledDS.setText(totalBilledDSCount);
                tvUnBilledDS.setText(totalUnbilledDS);
            }


        }}
    @SuppressLint("StaticFieldLeak")
    public class getProductivity extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(TopViewReport.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading in please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getproductivity"));
            nameValuePair.add(new BasicNameValuePair("month", params[0]));
            nameValuePair.add(new BasicNameValuePair("year", params[1]));
            nameValuePair.add(new BasicNameValuePair("userId", mUserLoginInfoBean.getUserId()));/*mUserLoginInfoBean.getUser_id())*/
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);

        }

        @Override
        protected void onPostExecute(JSONObject jsonResponse) {
            if (jsonResponse != null) {
                int productivityTotal = 0;
                productiveBeanArrayList = new ArrayList<>();
                nonproductiveBeanArrayList = new ArrayList<>();
                productiveOutletNameBeanArrayList = new ArrayList<>();
                productiveNonProductive = new ArrayList<>();
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
                            int sum1 = 0;
                            for (int i = 0; i < productiveNonProductive.size(); i++)
                                sum1 += productiveNonProductive.get(i).getOutletCount();

                            int sum2 = 0;
                            for (int i = 0; i < productiveBeanArrayList.size(); i++)
                                sum2 += productiveBeanArrayList.get(i).getOutletCount();

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

                            if (sum1 != 0) {
                                //(int) Math.ceil(a / 100.0)
                                productivityTotal = (int) Math.round(((double) 0 / (double) sum1) * 100);
                            } else {
                                productivityTotal = 0;
                            }
                            tvProductivity.setText(productivityTotal + " %");
                        }

                        if (productivityTotal >= 100) {
                            String value = "0";
                            float finalPercent = Float.parseFloat(value);
                            productivityProgressView.setProgress(finalPercent);
                            db.addProductivityHome(String.valueOf(productivityTotal), String.valueOf(finalPercent), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));
                        } else if (productivityTotal == 0) {
                            String value = "1.0";
                            float finalPercent = Float.parseFloat(value);
                            productivityProgressView.setProgress(finalPercent);
                            db.addProductivityHome(String.valueOf(productivityTotal), String.valueOf(finalPercent), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));

                        } else {
                            int productivity = 100 - productivityTotal;
                            String value = "0." + productivity;
                            float finalPercent = Float.parseFloat(value);
                            productivityProgressView.setProgress(finalPercent);
                            db.addProductivityHome(String.valueOf(productivityTotal), String.valueOf(finalPercent), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));

                        }
                        mDialog.dismiss();
                    } else {


                        String val = productivityTotal + " %";
                        tvProductivity.setText(val);
                        String value = "1.0";
                        float finalPercent = Float.parseFloat(value);
                        productivityProgressView.setProgress(finalPercent);
                        db.addProductivityHome(String.valueOf(productivityTotal), String.valueOf(finalPercent), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));
                        mDialog.dismiss();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    mDialog.dismiss();
                    productivityTotal = 0;
                    String val = productivityTotal + " %";
                    tvProductivity.setText(val);
                    String value = "1.0";
                    float finalPercent = Float.parseFloat(value);
                    productivityProgressView.setProgress(finalPercent);
                    db.addProductivityHome(String.valueOf(productivityTotal), String.valueOf(finalPercent), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));


                }

            } else {
                mDialog.dismiss();
                int productivityTotal = 0;
                String val = productivityTotal + " %";
                tvProductivity.setText(val);
                String value = "1.0";
                float finalPercent = Float.parseFloat(value);
                productivityProgressView.setProgress(finalPercent);
                db.addProductivityHome(String.valueOf(productivityTotal), String.valueOf(finalPercent), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));

            }
            if (Utils.isInternetConnected(TopViewReport.this)) {
                db.deleteAllTargetAchievementRecords(mUserLoginInfoBean.getUserId());
                new GetTargetList().execute(String.valueOf(month()), String.valueOf(year()));

            } else {

                TargetAchievementProgress targetAchievementProgress1 = db.getTargetAchievementHome(String.valueOf(month()), String.valueOf(year()), mUserLoginInfoBean.getUserId());
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

        }
    }

    private int month() {
        Calendar c = Calendar.getInstance();

        return c.get(Calendar.MONTH) + 1;
    }

    private int year() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    @SuppressLint("StaticFieldLeak")
    class GetTargetList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(TopViewReport.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading List Please Wait");
            mDialog.setCancelable(false);

            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "targetAchievementProgress"));
            nameValuePair.add(new BasicNameValuePair("month", params[0]));
            nameValuePair.add(new BasicNameValuePair("year", params[1]));
            nameValuePair.add(new BasicNameValuePair("territory_id", mUserLoginInfoBean.getUserTerritoryId()));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            List<TargetBean> targetList;
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
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
                        }

                        tvTargetValue.setText("" + (int) categoriesTargetTotal);
                        tvAchievementValue.setText("" + (int) categoriesAchievementTotal);
                        int targetAch;
                        if (categoriesTargetTotal != 0) {
                            categoriesPercentageTotal = (categoriesAchievementTotal / categoriesTargetTotal) * 100;
                            targetAch = Math.round(categoriesPercentageTotal);
                            tvTargetAchPercent.setText("" + Math.round(categoriesPercentageTotal) + "%");
                            if (Math.round(categoriesPercentageTotal) >= 100) {
                                String value = "0";
                                float finalPercent = Float.parseFloat(value);
                                targetAchievementProgressView.setProgress(finalPercent);
                                db.addTargetAchievementHome(String.valueOf(categoriesTargetTotal), String.valueOf(categoriesAchievementTotal), String.valueOf(Math.round(categoriesPercentageTotal)), String.valueOf(finalPercent), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));
                            } else if (Math.round(categoriesPercentageTotal) == 0) {
                                tvTargetAchPercent.setText("0 %");
                                String value = "1.0";
                                float finalPercent = Float.parseFloat(value);
                                targetAchievementProgressView.setProgress(finalPercent);
                                db.addTargetAchievementHome(String.valueOf(categoriesTargetTotal), String.valueOf(categoriesAchievementTotal), String.valueOf(Math.round(categoriesPercentageTotal)), String.valueOf(finalPercent), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));

                            } else {
                                int targetProgress = 100 - targetAch;
                                String value = "0." + targetProgress;
                                float finalPercent = Float.parseFloat(value);
                                targetAchievementProgressView.setProgress(finalPercent);
                                db.addTargetAchievementHome(String.valueOf(categoriesTargetTotal), String.valueOf(categoriesAchievementTotal), String.valueOf(Math.round(categoriesPercentageTotal)), String.valueOf(finalPercent), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));

                            }

                        } else {
                            tvTargetAchPercent.setText("0 %");
                            String value = "1.0";
                            float finalPercent = Float.parseFloat(value);
                            targetAchievementProgressView.setProgress(finalPercent);
                            db.addTargetAchievementHome(String.valueOf(categoriesTargetTotal), String.valueOf(categoriesAchievementTotal), String.valueOf(Math.round(categoriesPercentageTotal)), String.valueOf(finalPercent), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));


                        }
                        mDialog.dismiss();
                    } else if (jsonObject.getString("success").equalsIgnoreCase("false")) {
                        Utils.showToast(TopViewReport.this, jsonObject.getString("message"));
                        String value = "1.0";
                        tvTargetValue.setText("0");
                        tvAchievementValue.setText("0");
                        tvTargetAchPercent.setText("0 %");
                        float finalPercent = Float.parseFloat(value);
                        targetAchievementProgressView.setProgress(finalPercent);
                        db.addTargetAchievementHome(String.valueOf(0), String.valueOf(0), String.valueOf(Math.round(0)), String.valueOf(finalPercent), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));
                        mDialog.dismiss();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mDialog.dismiss();
                }

            } else {
                String value = "1.0";
                tvTargetValue.setText("0");
                tvAchievementValue.setText("0");
                tvTargetAchPercent.setText("0 %");
                float finalPercent = Float.parseFloat(value);
                targetAchievementProgressView.setProgress(finalPercent);
                db.addTargetAchievementHome(String.valueOf(0), String.valueOf(0), String.valueOf(Math.round(0)), String.valueOf(finalPercent), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));
                mDialog.dismiss();
            }
            if (Utils.isInternetConnected(TopViewReport.this)) {
                db.deleteAllTotalOutletHomeRecords(mUserLoginInfoBean.getUserId());
                new getOutletCount().execute(String.valueOf(month()), String.valueOf(year()));

            } else {
                TotalOutletCountHome totalOutletCountHome = db.getTotalOutletHome(String.valueOf(month()), String.valueOf(year()), mUserLoginInfoBean.getUserId());
                if (totalOutletCountHome != null) {
                    if (totalOutletCountHome.getTotalOutlet() != null && !totalOutletCountHome.getTotalOutlet().equalsIgnoreCase("")) {
                        String totalOutletCount = totalOutletCountHome.getTotalOutlet();
                        tvTotalOutlet.setText(totalOutletCount);
                    } else {
                        String totalOutletCount = "0";
                        tvTotalOutlet.setText(totalOutletCount);
                    }


                    if (totalOutletCountHome.getBilledOutlet() != null && !totalOutletCountHome.getBilledOutlet().equalsIgnoreCase("")) {

                        String totalBilledCount = totalOutletCountHome.getBilledOutlet();
                        tvBilledOutlet.setText(totalBilledCount);


                    } else {
                        String totalBilledCount = "0";
                        tvBilledOutlet.setText(totalBilledCount);

                    }


                    if (totalOutletCountHome.getUnbilledOutlet() != null && !totalOutletCountHome.getUnbilledOutlet().equalsIgnoreCase("")) {

                        String totalUnbilledOutlet = totalOutletCountHome.getUnbilledOutlet();
                        tvUnBilledOutlet.setText(totalUnbilledOutlet);


                    } else {
                        String totalUnbilledOutlet = "0";
                        tvUnBilledOutlet.setText(totalUnbilledOutlet);

                    }


                } else {
                    String totalOutletCount = "0";
                    String totalBilledCount = "0";
                    String totalUnbilledOutlet = "0";

                    tvTotalOutlet.setText(totalOutletCount);
                    tvBilledOutlet.setText(totalBilledCount);
                    tvUnBilledOutlet.setText(totalUnbilledOutlet);
                }


            }


        }
    }
    @SuppressLint("StaticFieldLeak")
    class getOutletCount extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(TopViewReport.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Logging in please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "outletCountProgress"));
            nameValuePair.add(new BasicNameValuePair("month", params[0]));
            nameValuePair.add(new BasicNameValuePair("year", params[1]));
            nameValuePair.add(new BasicNameValuePair("territoryId", mUserLoginInfoBean.getUserTerritoryId()));/*mUserLoginInfoBean.getUser_id())*/
            nameValuePair.add(new BasicNameValuePair("userId", mUserLoginInfoBean.getUserId()));/*mUserLoginInfoBean.getUser_id())*/
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);

        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject jsonResponse) {
            if (jsonResponse != null) {

                try {
                    if (jsonResponse.getString("success").equalsIgnoreCase("true")) {
                        int totalOutletCount = jsonResponse.getInt("totalOutlet");
                        int totalBilledCount = jsonResponse.getInt("totalBilled");
                        int totalUnBilledOutlet = jsonResponse.getInt("totalUnBilled");

                        tvTotalOutlet.setText(totalOutletCount + "");
                        tvBilledOutlet.setText(totalBilledCount + "");
                        tvUnBilledOutlet.setText(totalUnBilledOutlet + "");

                        db.addTotalOutletHome(String.valueOf(totalOutletCount), String.valueOf(totalBilledCount), String.valueOf(totalUnBilledOutlet), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));
                        mDialog.dismiss();
                    } else {
                        tvTotalOutlet.setText("0");
                        tvBilledOutlet.setText("0");
                        tvUnBilledOutlet.setText("0");
                        db.addTotalOutletHome(String.valueOf(0), String.valueOf(0), String.valueOf(0), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));
                        mDialog.dismiss();

                    }
                } catch (JSONException e) {
                    tvTotalOutlet.setText("0");
                    tvBilledOutlet.setText("0");
                    tvUnBilledOutlet.setText("0");
                    db.addTotalOutletHome(String.valueOf(0), String.valueOf(0), String.valueOf(0), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));
                    mDialog.dismiss();
                    e.printStackTrace();

                }

            } else {
                mDialog.dismiss();
                tvTotalOutlet.setText("0");
                tvBilledOutlet.setText("0");
                tvUnBilledOutlet.setText("0");
                db.addTotalOutletHome(String.valueOf(0), String.valueOf(0), String.valueOf(0), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));


            }

            if (Utils.isInternetConnected(TopViewReport.this)) {
                db.deleteAllDSHomeRecords(mUserLoginInfoBean.getUserId());
                new getDSCount().execute(String.valueOf(month()), String.valueOf(year()));

            } else {
                TotalOutletCountHome totalDsCount = db.getDSHome(String.valueOf(month()), String.valueOf(year()), mUserLoginInfoBean.getUserId());
                if (totalDsCount != null) {
                    if (totalDsCount.getTotalDS() != null && !totalDsCount.getTotalDS().equalsIgnoreCase("")) {
                        String totalDs = totalDsCount.getTotalDS();
                        tvBilledDS.setText(totalDs);
                    } else {
                        String totalDs = "0";
                        tvBilledDS.setText(totalDs);
                    }


                    if (totalDsCount.getBilledDS() != null && !totalDsCount.getBilledDS().equalsIgnoreCase("")) {

                        String totalBilledDSCount = totalDsCount.getBilledDS();
                        tvBilledDS.setText(totalBilledDSCount);


                    } else {
                        String totalBilledDSCount = "0";
                        tvBilledDS.setText(totalBilledDSCount);

                    }


                    if (totalDsCount.getUnbilledDS() != null && !totalDsCount.getUnbilledDS().equalsIgnoreCase("")) {

                        String totalUnBilledDS = totalDsCount.getUnbilledDS();
                        tvUnBilledDS.setText(totalUnBilledDS);


                    } else {
                        String totalUnBilledDS = "0";
                        tvUnBilledDS.setText(totalUnBilledDS);

                    }


                } else {
                    String totalDSCount = "0";
                    String totalBilledDSCount = "0";
                    String totalUnBilledDS = "0";

                    tvTotalDS.setText(totalDSCount);
                    tvBilledDS.setText(totalBilledDSCount);
                    tvUnBilledDS.setText(totalUnBilledDS);
                }


            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    class getDSCount extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(TopViewReport.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Logging in please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "distributorCountProgress"));
            nameValuePair.add(new BasicNameValuePair("month", params[0]));
            nameValuePair.add(new BasicNameValuePair("year", params[1]));
            nameValuePair.add(new BasicNameValuePair("userId", mUserLoginInfoBean.getUserId()));/*mUserLoginInfoBean.getUser_id())*/
            nameValuePair.add(new BasicNameValuePair("territoryId", mUserLoginInfoBean.getUserTerritoryId()));/*mUserLoginInfoBean.getUser_id())*/
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);

        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject jsonResponse) {
            if (jsonResponse != null) {

                try {
                    if (jsonResponse.getString("success").equalsIgnoreCase("true")) {


                        int totalDSCount = jsonResponse.getInt("totalDSCount");
                        int totalBilledDSCount = jsonResponse.getInt("billedDSCount");
                        int totalUnbilledDSOutlet = jsonResponse.getInt("unbilledDSCount");

                        tvTotalDS.setText(totalDSCount + "");
                        tvBilledDS.setText(totalBilledDSCount + "");
                        tvUnBilledDS.setText(totalUnbilledDSOutlet + "");
                        db.addTotalDSHome(String.valueOf(totalDSCount), String.valueOf(totalBilledDSCount), String.valueOf(totalUnbilledDSOutlet), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));
                        mDialog.dismiss();

                    } else {
                        tvTotalDS.setText("0");
                        tvBilledDS.setText("0");
                        tvUnBilledDS.setText("0");

                        db.addTotalDSHome(String.valueOf(0), String.valueOf(0), String.valueOf(0), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));
                        mDialog.dismiss();
                    }
                } catch (JSONException e) {
                    tvTotalDS.setText("0");
                    tvBilledDS.setText("0");
                    tvUnBilledDS.setText("0");

                    db.addTotalDSHome(String.valueOf(0), String.valueOf(0), String.valueOf(0), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));
                    mDialog.dismiss();
                    e.printStackTrace();

                }

            } else {
                tvTotalDS.setText("0");
                tvBilledDS.setText("0");
                tvUnBilledDS.setText("0");
                mDialog.dismiss();
                db.addTotalDSHome(String.valueOf(0), String.valueOf(0), String.valueOf(0), mUserLoginInfoBean.getUserId(), String.valueOf(month()), String.valueOf(year()));

            }


        }
    }
}
