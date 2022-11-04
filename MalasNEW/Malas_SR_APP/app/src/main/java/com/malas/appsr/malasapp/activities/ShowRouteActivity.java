package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.google.gson.reflect.TypeToken;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.RouteBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.DistributorSpinnerAdapter;
import com.malas.appsr.malasapp.adapter.ShowRouteAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ShowRouteActivity extends AppCompatActivity {

    EditText spnDistributer;
    ImageView ivAdd;
    ListView lvRoute;
    ArrayList<DistributerBean> mDistributerList;

    ArrayList<RouteBean> routeList = new ArrayList<>();
    ShowRouteAdapter showRouteAdapter;

    AsyncTask<String, Void, JSONObject> getDistributorTaskCalling, addAllroute;
    DistributorSpinnerAdapter mDistributorSpinnerAdapter;
    DistributerBean selectedDistributerBean;

    TextView tverritoryname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_route);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Route List");
        tverritoryname = findViewById(R.id.tv_territory_route);
        spnDistributer = findViewById(R.id.spnr_distributr);
        ivAdd = findViewById(R.id.iv_add);
        lvRoute = findViewById(R.id.list_view_show_outlet);


        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ShowRouteActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        tverritoryname.setText(mUserLoginInfoBean.getTerritoryName().toUpperCase(Locale.getDefault()));


        Type typeRoute = new TypeToken<ArrayList<RouteBean>>() {
        }.getType();
        ComplexPreferences mRouteListPref = ComplexPreferences.getComplexPreferences(ShowRouteActivity.this, Constant.ROUTE_LIST_PREF, MODE_PRIVATE);
        routeList = mRouteListPref.getArray(Constant.ROUTE_LIST_OBJ, typeRoute);

        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(ShowRouteActivity.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
        Type typeDistributor = new TypeToken<ArrayList<DistributerBean>>() {
        }.getType();
        mDistributerList = mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor) == null ? new ArrayList<>() : (ArrayList<DistributerBean>) mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor);

        spnDistributer.setOnClickListener(v -> {
            if (mDistributerList != null) {
                final Dialog dialog = new Dialog(ShowRouteActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView_Counry = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                mDistributorSpinnerAdapter = new DistributorSpinnerAdapter(ShowRouteActivity.this, mDistributerList, "TakeOrder");
                listView_Counry.setAdapter(mDistributorSpinnerAdapter);

                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String st = etserach.getText().toString();
                        if (!s.equals("") && s.length() > 0) {
                            mDistributorSpinnerAdapter.filter(st);
                        } else {
                            mDistributorSpinnerAdapter.filter(st);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView_Counry.setOnItemClickListener((parent, view, position, id) -> {
                    String countryname = DistributorSpinnerAdapter.resultArrayshort.get(position).getFirm_name().toUpperCase(Locale.getDefault());
                    //selectCategoryBean= DistributorSpinnerAdapter.resultArrayshort.get(position);
                    spnDistributer.setText(countryname);
                    //  citylist.clear();
                    dialog.dismiss();
                    try {
                        selectedDistributerBean = mDistributerList.get(position);

                        ComplexPreferences mDistributerListPref1 = ComplexPreferences.getComplexPreferences(ShowRouteActivity.this, Constant.ROUTE_LIST_PREF, MODE_PRIVATE);
                        Type typeRoute1 = new TypeToken<ArrayList<RouteBean>>() {
                        }.getType();
                        routeList = mDistributerListPref1.getArray(Constant.ROUTE_LIST_OBJ, typeRoute1);
                        ArrayList<RouteBean> temprouteList = new ArrayList<>();
                        if (routeList != null) {
                            for (int i = 0; i < routeList.size(); i++) {
                                if (routeList.get(i).getDistributor_id().equalsIgnoreCase(mDistributerList.get(position).getDistribution_id())) {
                                    temprouteList.add(routeList.get(i));
                                }
                            }
                            routeList.clear();
                            routeList.addAll(temprouteList);
                        } else {
                            routeList = new ArrayList<>();
                        }

                        showRouteAdapter = new ShowRouteAdapter(ShowRouteActivity.this, routeList);
                        lvRoute.setAdapter(showRouteAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });


        ivAdd.setOnClickListener(v -> {
            if (selectedDistributerBean == null) {
                Toast.makeText(ShowRouteActivity.this, "Please Select Distributor", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ShowRouteActivity.this, AddRouteActivity.class);
                intent.putExtra("distributerBean", selectedDistributerBean);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ComplexPreferences mUserPreference = ComplexPreferences.getComplexPreferences(ShowRouteActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = mUserPreference.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        getDistributorTaskCalling = new mDistributorList().execute(mUserLoginInfoBean.getUserTerritoryId());
        addAllroute = new getAllroute().execute(mUserLoginInfoBean.getUserTerritoryId());

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (getDistributorTaskCalling != null && !getDistributorTaskCalling.isCancelled())
            getDistributorTaskCalling.cancel(true);

        if (addAllroute != null && !addAllroute.isCancelled())
            addAllroute.cancel(true);

    }

    @SuppressLint("StaticFieldLeak")
    public class mDistributorList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(ShowRouteActivity.this);
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
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getdistributorlist"));
            nameValuePair.add(new BasicNameValuePair("territory_id", params[0]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ArrayList<DistributerBean> mDistributerArraylist = new ArrayList<>();
                        //if (mDistributerList.isEmpty())
                        //   Utils.showAlertDialog(ShowRouteActivity.this, "Route", jsonObject.getString("message"), true);
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
                        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(ShowRouteActivity.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
                        mDistributerListPref.putObject(Constant.DISTRIBUTOR_LIST_OBJ, mDistributerArraylist);
                        mDistributerListPref.commit();
                    } else {
                        Utils.showAlertDialog(ShowRouteActivity.this, "Route", jsonObject.getString("message"), true);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertDialog(ShowRouteActivity.this, "Route", "Improper response from server", true);
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class getAllroute extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(ShowRouteActivity.this);
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
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getallrouteslist"));
            nameValuePair.add(new BasicNameValuePair("territory_id", params[0]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ArrayList<RouteBean> routeListTemp = new ArrayList<>();
                        System.out.print(jsonObject.getString("message"));
                        if (mDistributerList.isEmpty())
                            Utils.showAlertDialog(ShowRouteActivity.this, "Route", jsonObject.getString("message"), true);
                        JSONArray mJsonArray = jsonObject.getJSONArray("routeslist");
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            String distributor_id = mJsonArray.getJSONObject(i).getString("distributor_id");
                            String routes_id = mJsonArray.getJSONObject(i).getString("routes_id");
                            String activation_status = mJsonArray.getJSONObject(i).getString("activation_status");
                            String route_name = mJsonArray.getJSONObject(i).getString("route_name");
                            routeListTemp.add(new RouteBean(distributor_id, routes_id, activation_status, route_name));
                        }

                        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(ShowRouteActivity.this, Constant.ROUTE_LIST_PREF, MODE_PRIVATE);
                        mDistributerListPref.putObject(Constant.ROUTE_LIST_OBJ, routeListTemp);
                        mDistributerListPref.commit();

                        routeList = new ArrayList<>();
                        routeList.addAll(routeListTemp);


                        if (selectedDistributerBean != null) {

                            ArrayList<RouteBean> temprouteList = new ArrayList<>();
                            if (routeList != null) {
                                for (int i = 0; i < routeList.size(); i++) {
                                    if (routeList.get(i).getDistributor_id().equalsIgnoreCase(selectedDistributerBean.getDistribution_id())) {
                                        temprouteList.add(routeList.get(i));
                                    }
                                }
                                routeList.clear();
                                routeList.addAll(temprouteList);
                            } else {
                                routeList = new ArrayList<>();
                            }

                            showRouteAdapter = new ShowRouteAdapter(ShowRouteActivity.this, routeList);
                            lvRoute.setAdapter(showRouteAdapter);
                        }


                    } else {
                        Utils.showAlertDialog(ShowRouteActivity.this, "Route", jsonObject.getString("message"), true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertDialog(ShowRouteActivity.this, "Route", "Improper response from server", true);
            }
            if (mDialog.isShowing()) mDialog.dismiss();

        }
    }


}
