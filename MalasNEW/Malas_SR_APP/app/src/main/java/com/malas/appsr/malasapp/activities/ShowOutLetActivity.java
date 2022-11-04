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
import android.util.Log;
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
import com.malas.appsr.malasapp.BeanClasses.ShowOutLetBeen;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.DistributorSpinnerAdapter;
import com.malas.appsr.malasapp.adapter.RouteAdapter;
import com.malas.appsr.malasapp.adapter.ShowOutletAdapter;

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

public class ShowOutLetActivity extends AppCompatActivity {

    EditText spnDistributer, spnRoute;
    ImageView ivAdd;
    ListView lvShowOutLet;
    ArrayList<DistributerBean> mDistributerList;
    ArrayList<RouteBean> routeList;
    ArrayList<ShowOutLetBeen> listOutlet = new ArrayList<>();
    ShowOutletAdapter showOutletAdapter;
    RouteAdapter mRouteAdapter;
    AsyncTask<String, Void, JSONObject> getDistributorTaskCalling, addAllroute, getalloutletlist;
    DistributorSpinnerAdapter mDistributorSpinnerAdapter;
    DistributerBean selectedDistributerBean;
    RouteBean selectedRouteBean;
    TextView tverritoryname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_outlet);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Outlet list");
        tverritoryname = findViewById(R.id.tv_territory_outlet);
        spnDistributer = findViewById(R.id.spnr_distributr);
        spnRoute = findViewById(R.id.spnr_route);
        ivAdd = findViewById(R.id.iv_add);
        lvShowOutLet = findViewById(R.id.list_view_show_outlet);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ShowOutLetActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        tverritoryname.setText(mUserLoginInfoBean.getTerritoryName().toUpperCase(Locale.getDefault()));

        Type typeOutlet = new TypeToken<ArrayList<ShowOutLetBeen>>() {}.getType();
        ComplexPreferences mOutletListPref = ComplexPreferences.getComplexPreferences(ShowOutLetActivity.this, Constant.SHOW_OUTLET_PREF, MODE_PRIVATE);
        listOutlet = mOutletListPref.getArray(Constant.SHOW_OUTLET_OBJ, typeOutlet);
        if (listOutlet == null) listOutlet = new ArrayList<>();

        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(ShowOutLetActivity.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
        Type typeDistributor = new TypeToken<ArrayList<DistributerBean>>() {
        }.getType();
        mDistributerList = mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor) == null ? new ArrayList<>() : (ArrayList<DistributerBean>) mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor);

        spnDistributer.setOnClickListener(v -> {
            if (mDistributerList != null) {
                final Dialog dialog = new Dialog(ShowOutLetActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView_Counry = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                mDistributorSpinnerAdapter = new DistributorSpinnerAdapter(ShowOutLetActivity.this, mDistributerList, "TakeOrder");
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
                    spnDistributer.setText(countryname);
                    dialog.dismiss();
                    try {
                        selectedDistributerBean = mDistributerList.get(position);
                        ComplexPreferences mRouteList = ComplexPreferences.getComplexPreferences(ShowOutLetActivity.this, Constant.ROUTE_LIST_PREF, MODE_PRIVATE);
                        Type typeRoute = new TypeToken<ArrayList<RouteBean>>() {
                        }.getType();
                        routeList = mRouteList.getArray(Constant.ROUTE_LIST_OBJ, typeRoute) == null ? new ArrayList<>() : (ArrayList<RouteBean>) mRouteList.getArray(Constant.ROUTE_LIST_OBJ, typeRoute);
                        ArrayList<RouteBean> temprouteList = new ArrayList<>();
                        for (int i = 0; i < routeList.size(); i++) {
                            if (routeList.get(i).getDistributor_id().equalsIgnoreCase(mDistributerList.get(position).getDistribution_id())) {
                                temprouteList.add(routeList.get(i));
                            }
                        }
                        routeList.clear();
                        routeList.addAll(temprouteList);

                        listOutlet = new ArrayList<>();
                        if (showOutletAdapter != null && selectedRouteBean != null) {
                            showOutletAdapter = new ShowOutletAdapter(ShowOutLetActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "showOutlet");
                            lvShowOutLet.setAdapter(showOutletAdapter);
                            showOutletAdapter.notifyDataSetChanged();
                        }


                        spnRoute.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        });

        spnRoute.setOnClickListener(v -> {
            if (routeList != null) {
                final Dialog dialog = new Dialog(ShowOutLetActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView_Counry = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                mRouteAdapter = new RouteAdapter(ShowOutLetActivity.this, routeList);
                listView_Counry.setAdapter(mRouteAdapter);

                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String st = etserach.getText().toString();
                        if (!s.equals("") && s.length() > 0) {
                            mRouteAdapter.filter(st);
                        } else {
                            mRouteAdapter.filter(st);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView_Counry.setOnItemClickListener((parent, view, position, id) -> {
                    String countryname = RouteAdapter.resultArrayshort.get(position).getRoute_name().toUpperCase(Locale.getDefault());
                    selectedRouteBean = RouteAdapter.resultArrayshort.get(position);
                    spnRoute.setText(countryname);
                    dialog.dismiss();

                    try {
                        selectedRouteBean = routeList.get(position);
                        ComplexPreferences mUserPreference = ComplexPreferences.getComplexPreferences(ShowOutLetActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
                        UserLoginInfoBean mUserLoginInfoBean1 = mUserPreference.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
                        if (selectedDistributerBean != null)
                            getalloutletlist = new Getalloutletlist().execute(mUserLoginInfoBean1.getUserTerritoryId(), selectedDistributerBean.getDistribution_id(), selectedRouteBean.getRoutes_id());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        });


        ivAdd.setOnClickListener(v -> {

            if (selectedDistributerBean == null || selectedRouteBean == null) {
                Toast.makeText(ShowOutLetActivity.this, "Select Distributer and Route", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(ShowOutLetActivity.this, AddNewOutletActivity.class)
                        .putExtra("distributerName", selectedDistributerBean.getFirm_name())
                        .putExtra("distributiorId", selectedDistributerBean.getDistribution_id())
                        .putExtra("routeName", selectedRouteBean.getRoute_name())
                        .putExtra("routeId", selectedRouteBean.getRoutes_id())
                        .putExtra("selectCategoryBean", selectedDistributerBean));
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        ComplexPreferences mUserPreference = ComplexPreferences.getComplexPreferences(ShowOutLetActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = mUserPreference.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        getDistributorTaskCalling = new mDistributorList().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, mUserLoginInfoBean.getUserTerritoryId());
        addAllroute = new getAllroute().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, mUserLoginInfoBean.getUserTerritoryId());

        if (selectedDistributerBean != null && selectedRouteBean != null)
            getalloutletlist = new Getalloutletlist().execute(mUserLoginInfoBean.getUserTerritoryId(), selectedDistributerBean.getDistribution_id(), selectedRouteBean.getRoutes_id());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getDistributorTaskCalling != null && !getDistributorTaskCalling.isCancelled())
            getDistributorTaskCalling.cancel(true);

        if (addAllroute != null && !addAllroute.isCancelled())
            addAllroute.cancel(true);

        if (getalloutletlist != null && !getalloutletlist.isCancelled())
            getalloutletlist.cancel(true);
    }

    public String checkJsonString(JSONArray mJsonArray, int position, String keyName) {
        String jsonStr = "";
        try {
            jsonStr = mJsonArray.getJSONObject(position).getString(keyName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    public void updateOutletList() {

        try {
            ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(ShowOutLetActivity.this, Constant.SHOW_OUTLET_PREF, MODE_PRIVATE);
            Type typeRoute = new TypeToken<ArrayList<ShowOutLetBeen>>() {
            }.getType();
            listOutlet = mDistributerListPref.getArray(Constant.SHOW_OUTLET_OBJ, typeRoute);
            if (listOutlet == null)
                listOutlet = new ArrayList<>();
            ArrayList<ShowOutLetBeen> tempListOutlet = new ArrayList<>();
            for (int i = 0; i < listOutlet.size(); i++) {
                if (listOutlet.get(i).getDistribution_id().equalsIgnoreCase(selectedRouteBean.getDistributor_id()) && listOutlet.get(i).getRoute_id().equalsIgnoreCase(selectedRouteBean.getRoutes_id())) {
                    tempListOutlet.add(listOutlet.get(i));
                }
            }
            listOutlet.clear();
            listOutlet.addAll(tempListOutlet);
            showOutletAdapter = new ShowOutletAdapter(ShowOutLetActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "showOutlet");
            lvShowOutLet.setAdapter(showOutletAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mDistributorList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(ShowOutLetActivity.this);
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
                        if (mDistributerList.isEmpty())
                            Utils.showAlertDialog(ShowOutLetActivity.this, "Outlet", jsonObject.getString("message"), true);
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

                        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(ShowOutLetActivity.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
                        mDistributerListPref.putObject(Constant.DISTRIBUTOR_LIST_OBJ, mDistributerArraylist);
                        mDistributerListPref.commit();
                    } else {
                        Utils.showAlertDialog(ShowOutLetActivity.this, "Outlet", jsonObject.getString("message"), true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertDialog(ShowOutLetActivity.this, "Outlet", "Improper response from server", true);
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class getAllroute extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(ShowOutLetActivity.this);
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
                        ArrayList<RouteBean> routeList = new ArrayList<>();
                     //   if (mDistributerList.isEmpty())
                            //Utils.showAlertDialog(ShowOutLetActivity.this, "Outlet", jsonObject.getString("message"), true);
                        JSONArray mJsonArray = jsonObject.getJSONArray("routeslist");
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            String distributor_id = mJsonArray.getJSONObject(i).getString("distributor_id");
                            String routes_id = mJsonArray.getJSONObject(i).getString("routes_id");
                            String activation_status = mJsonArray.getJSONObject(i).getString("activation_status");
                            String route_name = mJsonArray.getJSONObject(i).getString("route_name");

                            routeList.add(new RouteBean(distributor_id, routes_id, activation_status, route_name));
                        }

                        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(ShowOutLetActivity.this, Constant.ROUTE_LIST_PREF, MODE_PRIVATE);
                        mDistributerListPref.putObject(Constant.ROUTE_LIST_OBJ, routeList);
                        mDistributerListPref.commit();
                    } else {
                        Utils.showAlertDialog(ShowOutLetActivity.this, "Outlet", jsonObject.getString("message"), true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertDialog(ShowOutLetActivity.this, "Outlet", "Improper response from server", true);
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class Getalloutletlist extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(ShowOutLetActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading List please wait");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getalloutletlist"));
            nameValuePair.add(new BasicNameValuePair("territory_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("distributor_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("route_id", params[2]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.e("Json object", "" + jsonObject);
            if (jsonObject != null) {
                try {
                    List<ShowOutLetBeen> showlistOutlet = new ArrayList<>();

                   // Utils.showAlertDialog(ShowOutLetActivity.this, "Outlet", jsonObject.getString("message"), true);
                    if (jsonObject.getString("success").equals("true")) {
                        JSONArray mJsonArray = jsonObject.getJSONArray("ouletlist");

                        for (int i = 0; i < mJsonArray.length(); i++) {
                            String category_handlar = checkJsonString(mJsonArray, i, "categoryhandler");
                            String contact_person_name = checkJsonString(mJsonArray, i, "contact_person_name");
                            String outlet_email = checkJsonString(mJsonArray, i, "outlet_email");
                            String type_appointment = checkJsonString(mJsonArray, i, "type_appointment");
                            String activation_status = checkJsonString(mJsonArray, i, "activation_status");
                            String country_id = checkJsonString(mJsonArray, i, "country_id");
                            String supply_chain = checkJsonString(mJsonArray, i, "supply_chain");
                            String contact_number = checkJsonString(mJsonArray, i, "contact_number");
                            String city_id = checkJsonString(mJsonArray, i, "city_id");
                            String outlet_name = checkJsonString(mJsonArray, i, "outlet_name");
                            String addedby = checkJsonString(mJsonArray, i, "addedby");
                            String address = checkJsonString(mJsonArray, i, "outlet_address");
                            String residential_address = checkJsonString(mJsonArray, i, "residential_address");
                            String outlet_id = checkJsonString(mJsonArray, i, "outlet_id");
                            String mobile_no = checkJsonString(mJsonArray, i, "mobile_no");
                            String distribution_id = checkJsonString(mJsonArray, i, "distribution_id");
                            String route_id = checkJsonString(mJsonArray, i, "route_id");
                            String distributor_name = checkJsonString(mJsonArray, i, "distributor_name");
                            String route_name = checkJsonString(mJsonArray, i, "route_name");
                            String state_id = checkJsonString(mJsonArray, i, "state_id");
                            String district_id = checkJsonString(mJsonArray, i, "district_id");
                            String territory_id = checkJsonString(mJsonArray, i, "territory_id");

                            showlistOutlet.add(new ShowOutLetBeen(category_handlar, contact_person_name, outlet_email, type_appointment, activation_status, country_id, supply_chain, contact_number, city_id, outlet_name, addedby, address, residential_address, outlet_id, mobile_no, distribution_id, route_id, distributor_name, route_name, state_id, district_id, territory_id));
                        }
                        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(ShowOutLetActivity.this, Constant.SHOW_OUTLET_PREF, MODE_PRIVATE);
                        mDistributerListPref.putObject(Constant.SHOW_OUTLET_OBJ, showlistOutlet);
                        mDistributerListPref.commit();
                        listOutlet = new ArrayList<>();
                        listOutlet.addAll(showlistOutlet);

                        if (selectedDistributerBean != null && selectedRouteBean != null) {
                            updateOutletList();
                        }

                    } else {
                        listOutlet = new ArrayList<>();
                        showOutletAdapter = new ShowOutletAdapter(ShowOutLetActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "showOutlet");
                        lvShowOutLet.setAdapter(showOutletAdapter);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertDialog(ShowOutLetActivity.this, "Outlet", "Improper response from server", true);

            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }
}