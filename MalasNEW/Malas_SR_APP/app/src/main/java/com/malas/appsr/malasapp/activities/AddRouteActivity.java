package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.google.gson.reflect.TypeToken;

import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.RouteBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddRouteActivity extends AppCompatActivity {

    ArrayList<DistributerBean> mDistributerList;
    //    DistributorSpinnerAdapter mDistributorSpinnerAdapter;
    TextView tvDistributorName;
    Button btnSave;
    EditText edtRouteName;
    AsyncTask<String, Void, JSONObject> addRouteTaskCalling, maddRouteTask;
    AsyncTask<String, Void, JSONObject> getDistributorTaskCalling;

    UserLoginInfoBean mUserLoginInfoBean;
    RouteBean routeBean;
    DistributerBean distributerBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_route);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Route");
        tvDistributorName = findViewById(R.id.tvDistributorName);
        btnSave = findViewById(R.id.btn_save);
        edtRouteName = findViewById(R.id.edt_route);
        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(AddRouteActivity.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
        Type typeDistributor = new TypeToken<ArrayList<DistributerBean>>() {
        }.getType();
        mDistributerList = mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor) == null ? new ArrayList<>() : (ArrayList<DistributerBean>) mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor);

        if (getIntent().hasExtra("routeBean")) {
            if (getIntent().getSerializableExtra("routeBean") != null) {
                routeBean = (RouteBean) getIntent().getSerializableExtra("routeBean");
                getSupportActionBar().setTitle("Update Route");
                edtRouteName.setText(routeBean.getRoute_name().toUpperCase(Locale.getDefault()));
                for (DistributerBean distributor : mDistributerList) {
                    if (distributor.getDistribution_id().equals(routeBean.getDistributor_id()))
                        tvDistributorName.setText(distributor.getFirm_name());
                }
            }
        }


        if (getIntent().hasExtra("distributerBean")) {
            if (getIntent().getSerializableExtra("distributerBean") != null) {
                distributerBean = (DistributerBean) getIntent().getSerializableExtra("distributerBean");
                tvDistributorName.setText(distributerBean.getFirm_name().toUpperCase(Locale.getDefault()));
            }
        }


        ComplexPreferences mUserPreference = ComplexPreferences.getComplexPreferences(AddRouteActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = mUserPreference.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
/*

        mDistributorSpinnerAdapter = new DistributorSpinnerAdapter(AddRouteActivity.this, mDistributerList, "");
        mDistributorSpinner.setAdapter(mDistributorSpinnerAdapter);
        if (routeBean != null) {
            for (int i = 0; i < mDistributerList.size(); i++) {
                if (mDistributerList.get(i).getDistribution_id().equals(routeBean.getDistributor_id()))
                    mDistributorSpinner.setSelection(i);
            }
        }
*/

/*        getDistributorTaskCalling = new mDistributorList().execute(mUserLoginInfoBean.getUser_teratorry_id());


        mDistributorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDistributerBean = mDistributerList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        btnSave.setOnClickListener(v -> {
            String routeName = edtRouteName.getText().toString().toUpperCase(Locale.getDefault());
            if (routeName.isEmpty()) {
                edtRouteName.setError("Enter route name");
            } else {
                if (Utils.isInternetConnected(AddRouteActivity.this)) {
                    if (routeBean != null) {
                        maddRouteTask = new mAddRouteTask().execute(routeName, routeBean.getDistributor_id(), mUserLoginInfoBean.getUserId(), routeBean.getRoutes_id());
                    } else {
                        maddRouteTask = new mAddRouteTask().execute(routeName, distributerBean.getDistribution_id(), mUserLoginInfoBean.getUserId());
                    }
                } else {
                    Toast.makeText(AddRouteActivity.this, "No Internet connection found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (getDistributorTaskCalling != null) getDistributorTaskCalling.cancel(true);
        if (addRouteTaskCalling != null) addRouteTaskCalling.cancel(true);
        if (maddRouteTask != null) maddRouteTask.cancel(true);
    }

   /* public class mDistributorList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(AddRouteActivity.this);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Distributor List please wait");
            mDialog.setCancelable(false);
            if (mDistributerList.isEmpty()) {
                mDialog.show();
            }
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("method", "getdistributorlist"));
            nameValuePair.add(new BasicNameValuePair("territory_id", params[0]));
//            JSONObject jsonObjectFromUrl = new JSONParser().getJsonObjectFromHttp(Constant.BASE_URL, nameValuePair, JSONParser.Http.POST);
            JSONObject jsonObjectFromUrl = new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
            return jsonObjectFromUrl;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ArrayList<DistributerBean> mDistributerArraylist = new ArrayList<>();
                        System.out.print(jsonObject.getString("message").toString());
                        Toast.makeText(AddRouteActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
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
                        mDistributorSpinnerAdapter = new DistributorSpinnerAdapter(AddRouteActivity.this, mDistributerArraylist, "");
                        mDistributorSpinner.setAdapter(mDistributorSpinnerAdapter);

                        if (routeBean != null) {
                            for (int i = 0; i < mDistributerList.size(); i++) {
                                if (mDistributerList.get(i).getDistribution_id().equals(routeBean.getDistributor_id()))
                                    mDistributorSpinner.setSelection(i);
                            }
                        }

                        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(AddRouteActivity.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
                        mDistributerListPref.putObject(Constant.DISTRIBUTOR_LIST_OBJ, mDistributerArraylist);
                        mDistributerListPref.commit();
                    } else {
                        Toast.makeText(AddRouteActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(AddRouteActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }*/

    @SuppressLint("StaticFieldLeak")
    public class mAddRouteTask extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(AddRouteActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            if (routeBean != null)
                mDialog.setTitle("Updating route please wait...");
            else
                mDialog.setTitle("Adding route please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            if (routeBean != null) {
                nameValuePair.add(new BasicNameValuePair("method", "editroute"));
                nameValuePair.add(new BasicNameValuePair("route_id", params[3]));
            } else {
                nameValuePair.add(new BasicNameValuePair("method", "addroute"));
            }

            nameValuePair.add(new BasicNameValuePair("routename", params[0]));
            nameValuePair.add(new BasicNameValuePair("distributor_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("user_id", params[2]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        System.out.print(jsonObject.getString("message"));
                        Toast.makeText(AddRouteActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        AddRouteActivity.this.finish();
                    } else {
                        Toast.makeText(AddRouteActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(AddRouteActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }
}