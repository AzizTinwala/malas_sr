package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.google.android.material.snackbar.Snackbar;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.BeanClasses.CategoryListBean;
import com.malas.appsr.malasapp.BeanClasses.UniqueBillBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.adapter.ReportCategoryAdapter;
import com.malas.appsr.malasapp.adapter.UniqueBillOutletAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Arwa on 10/11/2017.
 */

public class UniqueBillOutletReport extends AppCompatActivity {
    ListView lv_category;
    ArrayList<CategoryListBean> catagoryInfoList;
    Context context;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener fromDate;
    DatePickerDialog.OnDateSetListener toDate;
    private ListView lvUniqueOutlet;
    private EditText spnr_fromDate, spnr_ToDate, spnr_category;
    private TextView tvFinalOutletVisitedCount;
    private UserLoginInfoBean mUserLoginInfoBean;
    private ReportCategoryAdapter categoryListAdapter;
    private TextView tvEmptyList;
    private LinearLayout llUniqueBill;

    public static boolean isDateAfter(String fromStringDate, String toStringDate) {
        try {
            String myFormatString = "dd-MM-yyyy"; // for example
            SimpleDateFormat df = new SimpleDateFormat(myFormatString, Locale.getDefault());
            Date toDate = df.parse(toStringDate);
            Date fromDate = df.parse(fromStringDate);

            if (toDate.after(fromDate)) {
                return true;
            } else return toDate.equals(fromDate);
        } catch (Exception e) {

            return false;
        }


    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unique_outlet_report);
        context = UniqueBillOutletReport.this;
        initView();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {

        Objects.requireNonNull(getSupportActionBar()).setTitle("Report");
        llUniqueBill = findViewById(R.id.ll_layout);
        lvUniqueOutlet = findViewById(R.id.lvUniqueOutlet);
        spnr_fromDate = findViewById(R.id.spnr_fromDate);
        spnr_ToDate = findViewById(R.id.spnr_toDate);
        spnr_category = findViewById(R.id.spnr_category);
        tvEmptyList = findViewById(R.id.tvEmptyList);

        catagoryInfoList = new ArrayList<>();

        TextView tv_territory = findViewById(R.id.tv_territory);
        tvFinalOutletVisitedCount = findViewById(R.id.tv_total_visit_count);


        myCalendar.set(Calendar.YEAR, myCalendar
                .get(Calendar.YEAR));
        myCalendar.set(Calendar.MONTH, myCalendar
                .get(Calendar.MONTH));
        myCalendar.set(Calendar.DAY_OF_MONTH, myCalendar
                .get(Calendar.DAY_OF_MONTH));
        updateToDate();

        myCalendar.set(Calendar.YEAR, myCalendar
                .get(Calendar.YEAR));
        myCalendar.set(Calendar.MONTH, myCalendar
                .get(Calendar.MONTH));
        myCalendar.set(Calendar.DAY_OF_MONTH, 1);
        updateFromDate();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        tv_territory.setText(mUserLoginInfoBean.getTerritoryName());


        new mGetCategoryList().execute();

        spnr_category.setText("ALL");
        getTargetData("0");
        fromDate = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateFromDate();
            if (catagoryInfoList.size() > 0) {
                String categoryID = "";
                for (int i = 0; i < catagoryInfoList.size(); i++) {
                    if (catagoryInfoList.get(i).getCategory_name().equalsIgnoreCase(spnr_category.getText().toString())) {
                        categoryID = catagoryInfoList.get(i).getId();
                    }
                }
                getTargetData(categoryID);
            } else {
                getTargetData("0");
            }
        };

        spnr_fromDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(context, fromDate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        toDate = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateToDate();
            if (catagoryInfoList != null) {
                if (catagoryInfoList.size() > 0) {
                    String categoryID = "";
                    for (int i = 0; i < catagoryInfoList.size(); i++) {
                        if (catagoryInfoList.get(i).getCategory_name().equalsIgnoreCase(spnr_category.getText().toString())) {
                            categoryID = catagoryInfoList.get(i).getId();
                        }
                    }
                    getTargetData(categoryID);
                } else {
                    getTargetData("0");
                }
            }
        };

        spnr_ToDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(context, toDate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });


        spnr_category.setOnClickListener(v -> {
            if (catagoryInfoList != null) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                lv_category = dialog.findViewById(R.id.dialogbox_listview);


                dialog.show();

                categoryListAdapter = new ReportCategoryAdapter(context, catagoryInfoList);
                lv_category.setAdapter(categoryListAdapter);

                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String st = etserach.getText().toString();
                        if (!s.equals("") && s.length() > 0) {
                            categoryListAdapter.filter(st);
                        } else {
                            categoryListAdapter.filter(st);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                lv_category.setOnItemClickListener((parent, view, position, id) -> {
                    String categoryName = ReportCategoryAdapter.resultArrayshort.get(position).getCategory_name();
                    String categoryId = ReportCategoryAdapter.resultArrayshort.get(position).getId();
                    //   selectCategoryBean = ReportCategoryAdapter.resultArrayshort.get(position);
                    spnr_category.setText(categoryName);
                    //   Log.v("POSITION", selectCategoryBean +"");
                    getTargetData(categoryId);
                    dialog.dismiss();

                });


            }
        });
    }

    private void updateFromDate() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        spnr_fromDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateToDate() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        spnr_ToDate.setText(sdf.format(myCalendar.getTime()));
    }

    public void getTargetData(String categoryId) {

        if (spnr_fromDate.getText().toString().equals("")) {
            Toast.makeText(this, "Please Select From Date", Toast.LENGTH_SHORT).show();
        } else if (spnr_ToDate.getText().toString().equals("")) {
            Toast.makeText(this, "Please Select To Date", Toast.LENGTH_SHORT).show();
        } else if (spnr_category.getText().toString().equals("")) {
            Toast.makeText(this, "Please Select Category", Toast.LENGTH_SHORT).show();
        } else {
            if (isDateAfter(spnr_fromDate.getText().toString(), spnr_ToDate.getText().toString())) {

                new GetUniqueOutlet().execute(categoryId);
            } else {
                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setMessage("Please Enter Valid To Date.")
                        .setNeutralButton("OK", (dialog, which) -> {
                            // do nothing - it will just close when clicked
                        }).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mGetCategoryList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(context);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle(" please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getcategorylist"));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        Snackbar.make(llUniqueBill, jsonObject.getString("message"), Snackbar.LENGTH_SHORT).show();
                        JSONArray mJsonarry = jsonObject.getJSONArray("categorylist");
                        CategoryListBean categoryListBean1 = new CategoryListBean("0", "ALL");

                        catagoryInfoList.add(categoryListBean1);
                        for (int j = 0; j < mJsonarry.length(); j++) {
                            JSONObject mJsonObjInfo = mJsonarry.getJSONObject(j);
                            String category_name = mJsonObjInfo.getString("category_name");
                            String id = mJsonObjInfo.getString("id");
                            CategoryListBean categoryListBean = new CategoryListBean(id, category_name);

                            catagoryInfoList.add(categoryListBean);
                        }

                    } else {
                        Snackbar.make(llUniqueBill, jsonObject.getString("message"), Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Snackbar.make(llUniqueBill, "Improper response from server", Snackbar.LENGTH_SHORT).show();

            }
            mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class GetUniqueOutlet extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(context);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle(" please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getuniqueoutlet"));
            nameValuePair.add(new BasicNameValuePair("fromDate", spnr_fromDate.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("toDate", spnr_ToDate.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("userId", mUserLoginInfoBean.getUserId()));
            nameValuePair.add(new BasicNameValuePair("categoryId", params[0]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject jsonResponse) {
            if (jsonResponse != null) {
                try {
                    if (jsonResponse.getString("success").equalsIgnoreCase("true")) {
                        lvUniqueOutlet.setVisibility(View.VISIBLE);
                        tvEmptyList.setVisibility(View.GONE);

                        JSONArray mJsonArray = jsonResponse.getJSONArray("uniqueOutletList");
                        ArrayList<UniqueBillBean> uniqueBillBeanArray = new ArrayList<>();

                        for (int i = 0; i < mJsonArray.length(); i++) {
                            JSONObject mJsonobj = mJsonArray.getJSONObject(i);
                            String outletName = mJsonobj.getString("OUTLET_NAME");
                            int outletCount = mJsonobj.getInt("OUTLET_COUNT");
                            String unitimeDate = mJsonobj.getString("UNITIME");
                            String CategoryId = mJsonobj.getString("CATEGORY ID");
                            String routeName = mJsonobj.getString("ROUTE_NAME");
                            //Date Added to List

                            UniqueBillBean uniqueBillBean = new UniqueBillBean(outletName, outletCount, routeName);
                            uniqueBillBeanArray.add(uniqueBillBean);
                        }

                        int count;
                        for (int i = 0; i < uniqueBillBeanArray.size(); i++) {
                            for (int j = i + 1; j < uniqueBillBeanArray.size(); j++) {
                                if (uniqueBillBeanArray.get(i).getOutletName().equals(uniqueBillBeanArray.get(j).getOutletName())) {
                                    count = uniqueBillBeanArray.get(i).getOutletCount() + uniqueBillBeanArray.get(j).getOutletCount();
                                    UniqueBillBean uniqueBillBean = new UniqueBillBean(uniqueBillBeanArray.get(i).getOutletName(), count, uniqueBillBeanArray.get(i).getRouteName());
                                    uniqueBillBeanArray.set(i, uniqueBillBean);
                                    uniqueBillBeanArray.remove(j);
                                }
                            }

                        }
                        int ouletCountTotal = 0;
                        for (int i = 0; i < uniqueBillBeanArray.size(); i++) {
                            /*if (uniqueBillBeanArray.get(i).getOutletCount() > 1) {
                                ouletCountTotal += 1;

                            } else {*/
                            ouletCountTotal += uniqueBillBeanArray.get(i).getOutletCount();


                            // Log.v("UNIQUE BILL REPORT", uniqueBillBeanArray.get(i).getOutletName() + "" + uniqueBillBeanArray.get(i).getOutletCount());
                        }

                        tvFinalOutletVisitedCount.setText(ouletCountTotal + "");
                        UniqueBillOutletAdapter uniqueBilladapter = new UniqueBillOutletAdapter(context, uniqueBillBeanArray);
                        lvUniqueOutlet.setAdapter(uniqueBilladapter);
                    } else {
                        lvUniqueOutlet.setVisibility(View.GONE);
                        tvEmptyList.setVisibility(View.VISIBLE);
                        tvEmptyList.setText(jsonResponse.getString("message"));
                        tvFinalOutletVisitedCount.setText("0");
                        // Toast.makeText(context, jsonResponse.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    lvUniqueOutlet.setVisibility(View.GONE);
                    tvEmptyList.setVisibility(View.VISIBLE);
                    tvEmptyList.setText("Oops! Something Went Wrong");
                    tvFinalOutletVisitedCount.setText("0");
                    e.printStackTrace();
                }
            } else {
                lvUniqueOutlet.setVisibility(View.GONE);
                tvEmptyList.setVisibility(View.VISIBLE);
                tvEmptyList.setText("Improper response from server");
                tvFinalOutletVisitedCount.setText("0");

            }
            mDialog.dismiss();
        }
    }
}
