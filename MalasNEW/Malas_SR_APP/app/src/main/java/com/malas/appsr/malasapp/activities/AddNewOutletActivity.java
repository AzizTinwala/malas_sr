package com.malas.appsr.malasapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.Amitlibs.utils.TextUtils;
import com.google.gson.reflect.TypeToken;

import com.malas.appsr.malasapp.BeanClasses.CityListBean;
import com.malas.appsr.malasapp.BeanClasses.CountryListBean;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.DistrictListBean;
import com.malas.appsr.malasapp.BeanClasses.RetailerBean;
import com.malas.appsr.malasapp.BeanClasses.ShowOutLetBeen;
import com.malas.appsr.malasapp.BeanClasses.StateListBean;
import com.malas.appsr.malasapp.BeanClasses.TerritoryListBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.TypeOfAppoinmentAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddNewOutletActivity extends AppCompatActivity {

    EditText edtUserName, edtEmail, edtAddress, edtcity, edt_territory, edtcountry, edtmobile, edtState, edtDistrict, edtContactPerson, edtResidentioalAddress, edtContactno;
    TextView tv_catagory, tvCatagoryHandler;
    Button btnCancel, btnSubmit;
    Spinner spnTypeOfAppointment, spnSelectSupplyChain;
    ArrayList<CityListBean> citylist = new ArrayList<>();
    ArrayList<DistrictListBean> districtlist = new ArrayList<>();
    ArrayList<TerritoryListBean> territorylist = new ArrayList<>();
    ArrayList<CountryListBean> countrylist = new ArrayList<>();
    ArrayList<RetailerBean> mTypeOfAppoinmentBeanList = new ArrayList<>();
    ArrayList<StateListBean> statelist = new ArrayList<>();
    String categoryHandlerData = null;
    TypeOfAppoinmentAdapter mTypeOfAppoinmentAdapter;
    CityListBean selectedCityBean;
    DistrictListBean selectedDistrictBean;
    TerritoryListBean selectedTerritoryBean;
    CountryListBean selectedCountryBean;
    StateListBean selectedStateBean;
    RetailerBean selectedTypeOfAppoinmentBean;
    DistributerBean selectedDistributerBean;
    AsyncTask<String, Void, JSONObject> mgetRetailerList, getDistributorTaskCalling;
    AsyncTask<String, Void, JSONObject> mGetAllCountryList, mGetStateList, mGetDistrictList, mGetCityList;
    String distributorId;
    String distributorName;
    String routeName;
    String routeId;
    ShowOutLetBeen showOutLetBeen;
    double latitude = 0;
    double longitude = 0;
    String addressStr = "";

    LocationManager locationManager;
    LocationListener locationListener;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_outlet);

        edtUserName = findViewById(R.id.edt_user_name);
        edtEmail = findViewById(R.id.edt_email_outlet);
        edtAddress = findViewById(R.id.edt_address_outlet);
        edtmobile = findViewById(R.id.edt_mobile_outlet);
        spnTypeOfAppointment = findViewById(R.id.spin_type_of_appontment_outlet);
        spnSelectSupplyChain = findViewById(R.id.spin_slect_supply_chain_outlet);
        edtContactPerson = findViewById(R.id.edt_contect_person_name_outlet);
        edtResidentioalAddress = findViewById(R.id.edt_residtial_address_outlet);
        edtContactno = findViewById(R.id.edt_contact_number_outlet);
        btnCancel = findViewById(R.id.btn_cancel_outlet);
        btnSubmit = findViewById(R.id.btn_submit_outlet);
        tv_catagory = findViewById(R.id.tv_catagory);
        tvCatagoryHandler = findViewById(R.id.tvCatagoryHandler);

        tvCatagoryHandler.setPaintFlags(tvCatagoryHandler.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        edtcity = findViewById(R.id.edt_City);
        edt_territory = findViewById(R.id.edt_territory);
        edtcountry = findViewById(R.id.edt_country);
        edtDistrict = findViewById(R.id.edt_District);
        edtState = findViewById(R.id.edt_state);

        distributorId = getIntent().getStringExtra("distributiorId");
        distributorName = getIntent().getStringExtra("distributerName").toUpperCase(Locale.getDefault());
        routeName = getIntent().getStringExtra("routeName").toUpperCase(Locale.getDefault());
        routeId = getIntent().getStringExtra("routeId");
        if (Objects.requireNonNull(getIntent().getExtras()).containsKey("showOutletBean")) {
            showOutLetBeen = (ShowOutLetBeen) getIntent().getSerializableExtra("showOutletBean");
            Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Outlet");
            categoryHandlerData = showOutLetBeen.getCategory_handlar();
            tvCatagoryHandler.setVisibility(View.GONE);
        } else
            Objects.requireNonNull(getSupportActionBar()).setTitle("Add New Outlet");

        if (getIntent().getExtras().containsKey("selectCategoryBean"))
            selectedDistributerBean = (DistributerBean) getIntent().getSerializableExtra("selectCategoryBean");
        if (getIntent().getExtras().containsKey("selectedDistributerBean"))
            selectedDistributerBean = (DistributerBean) getIntent().getSerializableExtra("selectedDistributerBean");

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddNewOutletActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        ((TextView) findViewById(R.id.territory_name)).setText(/*"Territory:- " + mUserLoginInfoBean.getTerritory_name() +*/ "\nSelected Distributer: " + distributorName + "\nSelected Route: " + routeName);

        getCurrentLocation();


        mGetAllCountryList = new mGetAllCountryList().execute();

        Type typeRetailerBean = new TypeToken<ArrayList<RetailerBean>>() {
        }.getType();
        ComplexPreferences mRetailerPref = ComplexPreferences.getComplexPreferences(AddNewOutletActivity.this, Constant.Retailer_LIST_PREF, MODE_PRIVATE);
        mTypeOfAppoinmentBeanList = mRetailerPref.getArray(Constant.Retailer_LIST_OBJ, typeRetailerBean);
        if (mTypeOfAppoinmentBeanList == null) mTypeOfAppoinmentBeanList = new ArrayList<>();
        mTypeOfAppoinmentAdapter = new TypeOfAppoinmentAdapter(AddNewOutletActivity.this, mTypeOfAppoinmentBeanList);
        spnTypeOfAppointment.setAdapter(mTypeOfAppoinmentAdapter);
        mgetRetailerList = new mGetRetailerTypeList().execute();

        if (showOutLetBeen != null) {
            updateView();
        }

        spnTypeOfAppointment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTypeOfAppoinmentBean = mTypeOfAppoinmentBeanList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tv_catagory.setOnClickListener(v -> {
            Intent categoryIntnt = new Intent(AddNewOutletActivity.this, CatagoryHandlerActivity.class);
            categoryIntnt.putExtra("categoryHandlerData", categoryHandlerData);
            startActivityForResult(categoryIntnt, 1);
        });

        btnSubmit.setOnClickListener(view -> {

            if (edtUserName.getText().toString().isEmpty()) {
                edtUserName.setError("Enter user name");
            } else if (edtEmail.getText().toString().isEmpty()) {
                edtEmail.setError("Enter email");
            } else if (!TextUtils.isValidEmail(edtEmail.getText().toString())) {
                edtEmail.setError("Enter valid email");
            } else if (edtmobile.getText().toString().isEmpty()) {
                edtmobile.setError("Enter mobile");
            } else if (!TextUtils.isValidMobileNo(edtmobile.getText().toString())) {
                edtmobile.setError("Enter valid mobile");
            } else if (edtAddress.getText().toString().isEmpty()) {
                edtAddress.setError("Enter address");
            } else if (edtContactPerson.getText().toString().isEmpty()) {
                edtContactPerson.setError("Enter Contact Person name");
            } else if (edtResidentioalAddress.getText().toString().isEmpty()) {
                edtResidentioalAddress.setError("Enter Residential address");
            } else if (edtContactno.getText().toString().isEmpty()) {
                edtContactno.setError("Enter Contact no");
            } else if (!TextUtils.isValidMobileNo(edtContactno.getText().toString())) {
                edtContactno.setError("Enter Valid Contact no");
            } else if (selectedCountryBean == null) {
                Toast.makeText(AddNewOutletActivity.this, "Please Select Country", Toast.LENGTH_SHORT).show();
            } else if (selectedDistrictBean == null) {
                Toast.makeText(AddNewOutletActivity.this, "Please Select District", Toast.LENGTH_SHORT).show();
            } else if (selectedCityBean == null) {
                Toast.makeText(AddNewOutletActivity.this, "Please Select City", Toast.LENGTH_SHORT).show();
            } else if (selectedTypeOfAppoinmentBean == null) {
                Toast.makeText(AddNewOutletActivity.this, "Please Select Type of appointment", Toast.LENGTH_SHORT).show();
            } else if (selectedStateBean == null) {
                Toast.makeText(AddNewOutletActivity.this, "Please Select state", Toast.LENGTH_SHORT).show();
            } else if (categoryHandlerData == null) {
                Toast.makeText(AddNewOutletActivity.this, "Please Select Category", Toast.LENGTH_SHORT).show();
            } else {
                String outletName = edtUserName.getText().toString().toUpperCase(Locale.getDefault());
                String outLetEmail = edtEmail.getText().toString();
                String outLetMobile = edtmobile.getText().toString();
                String edtOutletAddress = edtAddress.getText().toString().toUpperCase(Locale.getDefault());
                String outLetContactPersonName = edtContactPerson.getText().toString().toUpperCase(Locale.getDefault());
                String outletResidentailAddress = edtResidentioalAddress.getText().toString().toUpperCase(Locale.getDefault());
                String outLetContactPersonNo = edtContactno.getText().toString();
                String selectedCountryId = selectedCountryBean.getCountryId();
                String selectedDistrictId = selectedCityBean.getDistrict_id();
                String selectedCityId = selectedCityBean.getCid();
                String selectedTerritoryId = selectedTerritoryBean.getTerritoryId();
                String selectedStateId = selectedStateBean.getStateId();
                String typeOfAppoinmentId = selectedTypeOfAppoinmentBean.getRetailerId();
                String selectedSupplyChainId = /*selectedSupplyChainBean.getDistribution_id()*/distributorId;
                String selectedRouteId = /*selectedSupplyChainBean.getDistribution_id()*/routeId;
                ComplexPreferences complexPreferences1 = ComplexPreferences.getComplexPreferences(AddNewOutletActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
                UserLoginInfoBean mUserLoginInfoBean1 = complexPreferences1.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

                if (selectedTerritoryId.equals(mUserLoginInfoBean1.getUserTerritoryId())) {
                    if (showOutLetBeen != null) {
                        new addOutletTask().execute(mUserLoginInfoBean1.getUserId(), outletName, outLetEmail, selectedCountryId, selectedCityId, outLetMobile, edtOutletAddress, typeOfAppoinmentId, selectedSupplyChainId, outLetContactPersonName, outletResidentailAddress, outLetContactPersonNo, categoryHandlerData, selectedStateId, selectedRouteId, selectedTerritoryId, "" + latitude, "" + longitude, addressStr, selectedDistrictId,showOutLetBeen.getOutlet_id());
                    } else {
                        new addOutletTask().execute(mUserLoginInfoBean1.getUserId(), outletName, outLetEmail, selectedCountryId, selectedCityId, outLetMobile, edtOutletAddress, typeOfAppoinmentId, selectedSupplyChainId, outLetContactPersonName, outletResidentailAddress, outLetContactPersonNo, categoryHandlerData, selectedStateId, selectedRouteId, selectedTerritoryId, "" + latitude, "" + longitude, addressStr,selectedDistrictId);
                    }
                } else {
                    Toast.makeText(AddNewOutletActivity.this, "You can not add outlet from different territory", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(view -> finish());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            categoryHandlerData = data.getStringExtra("selectedCategory");
            //Log.e("categoryHandlerData ", "" + categoryHandlerData);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (getDistributorTaskCalling != null && !getDistributorTaskCalling.isCancelled())
            getDistributorTaskCalling.cancel(true);

        if (mgetRetailerList != null && !mgetRetailerList.isCancelled())
            mgetRetailerList.cancel(true);

        if (mGetAllCountryList != null && !mGetAllCountryList.isCancelled())
            mGetAllCountryList.cancel(true);

        if (mGetStateList != null && !mGetStateList.isCancelled())
            mGetStateList.cancel(true);

        if (mGetDistrictList != null && !mGetDistrictList.isCancelled())
            mGetDistrictList.cancel(true);

        if (mGetCityList != null && !mGetCityList.isCancelled())
            mGetCityList.cancel(true);
    }

    public void updateView() {

        edtUserName.setText(showOutLetBeen.getOutlet_name());
        edtEmail.setText(showOutLetBeen.getOutlet_email());
        edtAddress.setText(showOutLetBeen.getAddress());
        edtmobile.setText(showOutLetBeen.getMobile_no());
        edtContactPerson.setText(showOutLetBeen.getContact_person_name());
        edtResidentioalAddress.setText(showOutLetBeen.getResidential_address());
        edtContactno.setText(showOutLetBeen.getContact_number());
        if (countrylist != null) {
            for (int i = 0; i < countrylist.size(); i++) {
                if (countrylist.get(i).getCountryId().equals(showOutLetBeen.getCountry_id())) {
                    edtcountry.setText(countrylist.get(i).getCountryName());
                    break;
                }
            }
        }

    }

    public void getCurrentLocation() {

        boolean result = Utils.checkPermissions(AddNewOutletActivity.this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION});
        if (result) {
            // Acquire a reference to the system Location Manager
            locationManager =
                    (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Define a listener that responds to location updates
            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
//                    getAddressByLatLng(latitude, longitude);
                    getAddressByLatLng(latitude, longitude);
                    String lat = Double.toString(location.getLatitude());
                    String lon = Double.toString(location.getLongitude());
                   /* Log.e("Your Location is", "" + lat + "--" + lon);
                    Log.e("Your Location is", " addressStr " + addressStr);
*/
                    if (locationManager != null)
                        locationManager.removeUpdates(locationListener);
                    locationManager = null;
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            // Register the listener with the Location Manager to receive location updates

        /*    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }*/
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    public void getAddressByLatLng(double lat, double lng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses.size() > 0)
                addressStr = addresses.get(0).getAddressLine(0) + " " + addresses.get(0).getLocality() + " " + addresses.get(0).getAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //  Log.e("address", "" + addressStr);
    }

    @SuppressLint("StaticFieldLeak")
    public class mGetAllCountryList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(AddNewOutletActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Getting Country list please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getallcountrylist"));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        // System.out.print(jsonObject.getString("message").toString());
                        JSONArray countryArray = jsonObject.getJSONArray("countrylist");
                        countrylist = new ArrayList<>();
                        for (int l = 0; l < countryArray.length(); l++) {
                            JSONObject mJsonObjInfoCountryt = countryArray.getJSONObject(l);
                            String id = mJsonObjInfoCountryt.getString("id");
                            String country_name = mJsonObjInfoCountryt.getString("country_name");
                            countrylist.add(new CountryListBean(id, country_name));
                        }

                        if (countrylist != null) {
                            if (countrylist.size() > 0) {
                                for (int i = 0; i < countrylist.size(); i++) {
                                    if (selectedDistributerBean != null)
                                        if (selectedDistributerBean.getCountry_id().equals(countrylist.get(i).getCountryId())) {
                                            edtcountry.setText(countrylist.get(i).getCountryName());
                                            new mGetStateList().execute(countrylist.get(i).getCountryId());
                                            selectedCountryBean = countrylist.get(i);
                                        }
                                }
                            }
                        }

                    } else {
                        Utils.showAlertDialog(AddNewOutletActivity.this, "Outlet", jsonObject.getString("message"), true);
//                        Toast.makeText(AddNewOutletActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertDialog(AddNewOutletActivity.this, "Outlet", "Improper response from server", true);
//                Toast.makeText(AddNewOutletActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mGetStateList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(AddNewOutletActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Getting State list please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getstatelist"));
            nameValuePair.add(new BasicNameValuePair("countryid", params[0]));
            /*nameValuePair.add(new BasicNameValuePair("distributor_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("user_id", params[2]));*/

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        //     System.out.print(jsonObject.getString("message").toString());
                        JSONArray countryArray = jsonObject.getJSONArray("statelist");
                        statelist = new ArrayList<>();
                        for (int l = 0; l < countryArray.length(); l++) {
                            JSONObject mJsonObjInfoCountryt = countryArray.getJSONObject(l);
                            String id = mJsonObjInfoCountryt.getString("state_id");
                            String countryId = mJsonObjInfoCountryt.getString("country_id");
                            String stateName = mJsonObjInfoCountryt.getString("state_name");
                            statelist.add(new StateListBean(id, stateName, countryId));
                        }
                        if (statelist != null) {
                            if (statelist.size() > 0) {
                                for (int i = 0; i < statelist.size(); i++) {
                                    if (selectedDistributerBean != null)
                                        if (selectedDistributerBean.getState_id().equals(statelist.get(i).getStateId())) {
                                            edtState.setText(statelist.get(i).getStateName());
                                            new mGetDistrictList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statelist.get(i).getStateId());
                                            new mGetTerritoryList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statelist.get(i).getStateId());
                                            selectedStateBean = statelist.get(i);
                                        }
                                }
                            }
                        }

                    } else {
                        Utils.showAlertDialog(AddNewOutletActivity.this, "Outlet", jsonObject.getString("message"), true);
//                        Toast.makeText(AddNewOutletActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertDialog(AddNewOutletActivity.this, "Outlet", "Improper response from server", true);
//                Toast.makeText(AddNewOutletActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mGetDistrictList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(AddNewOutletActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Getting District list please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getalldistrictlist"));
            nameValuePair.add(new BasicNameValuePair("stateid", params[0]));
            /*nameValuePair.add(new BasicNameValuePair("distributor_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("user_id", params[2]));*/

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        //    System.out.print(jsonObject.getString("message").toString());
                        JSONArray countryArray = jsonObject.getJSONArray("districlist");
                        districtlist = new ArrayList<>();
                        for (int l = 0; l < countryArray.length(); l++) {
                            JSONObject mJsonObjInfoCountryt = countryArray.getJSONObject(l);
                            String id = mJsonObjInfoCountryt.getString("id");
                            String StateId = mJsonObjInfoCountryt.getString("state_id");
                            String stateName = mJsonObjInfoCountryt.getString("dis_name");
                            districtlist.add(new DistrictListBean(id, stateName, StateId));
                        }
                        /*mDistrictAdapter = new DistrictAdapter(AddNewOutletActivity.this, districtlist);
                        spnDistrict.setAdapter(mDistrictAdapter);*/
                        if (districtlist != null) {
                            if (districtlist.size() > 0) {
                                for (int i = 0; i < districtlist.size(); i++) {
                                    if (selectedDistributerBean != null)
                                        if (selectedDistributerBean.getDistrict_id().equals(districtlist.get(i).getDistrictId())) {
                                            edtDistrict.setText(districtlist.get(i).getDistrictName());
                                            new mGetCityList().execute(districtlist.get(i).getDistrictId());
                                            selectedDistrictBean = districtlist.get(i);
                                        }
                                }
                            }
                        }
                    } else {
                        Utils.showAlertDialog(AddNewOutletActivity.this, "Outlet", jsonObject.getString("message"), true);
//                        Toast.makeText(AddNewOutletActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertDialog(AddNewOutletActivity.this, "Outlet", "Improper response from server", true);
//                Toast.makeText(AddNewOutletActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mGetTerritoryList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(AddNewOutletActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Getting Territory list please wait...");
            mDialog.setCancelable(false);
            //mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getterritorybystate"));
            nameValuePair.add(new BasicNameValuePair("state_id", params[0]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        // System.out.print(jsonObject.getString("message").toString());
                        JSONArray countryArray = jsonObject.getJSONArray("territorylist");
                        territorylist = new ArrayList<>();
                        for (int l = 0; l < countryArray.length(); l++) {
                            JSONObject mJsonObjInfoCountryt = countryArray.getJSONObject(l);
                            String id = mJsonObjInfoCountryt.getString("id");
                            String stateId = mJsonObjInfoCountryt.getString("state_id");
                            String territoryName = mJsonObjInfoCountryt.getString("territory_name");
                            String status = mJsonObjInfoCountryt.getString("status");
                            territorylist.add(new TerritoryListBean(id, territoryName, status, stateId));
                        }

                        if (territorylist != null) {
                            if (territorylist.size() > 0) {
                                for (int i = 0; i < territorylist.size(); i++) {
                                    if (selectedDistributerBean != null)
                                        if (selectedDistributerBean.getTerritory_id().equals(territorylist.get(i).getTerritoryId())) {
                                            edt_territory.setText(territorylist.get(i).getTerritoryName());
                                            selectedTerritoryBean = territorylist.get(i);
                                        }
                                }
                            }
                        }
                    } else {
                        Utils.showAlertDialog(AddNewOutletActivity.this, "Outlet", jsonObject.getString("message"), true);
//                        Toast.makeText(AddNewOutletActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertDialog(AddNewOutletActivity.this, "Outlet", "Improper response from server", true);
//                Toast.makeText(AddNewOutletActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mGetCityList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(AddNewOutletActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Getting City list please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getcitylist"));
            nameValuePair.add(new BasicNameValuePair("districtid", params[0]));
            /*nameValuePair.add(new BasicNameValuePair("distributor_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("user_id", params[2]));*/

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        //System.out.print(jsonObject.getString("message").toString());
                        JSONArray countryArray = jsonObject.getJSONArray("citylist");
                        citylist = new ArrayList<>();
                        for (int l = 0; l < countryArray.length(); l++) {
                            JSONObject mJsonObjInfoCountryt = countryArray.getJSONObject(l);
                            String city_id = mJsonObjInfoCountryt.getString("cid");
                            String stateId = mJsonObjInfoCountryt.getString("state_id");
                            String districtId = mJsonObjInfoCountryt.getString("district_id");
                            String cityName = mJsonObjInfoCountryt.getString("cname");
                            citylist.add(new CityListBean(districtId, city_id, cityName));
                        }
                        /*mCityAdapter = new CityAdapter(AddNewOutletActivity.this, citylist);
                        spnCity.setAdapter(mCityAdapter);*/
                        if (citylist != null) {
                            if (citylist.size() > 0) {
                                for (int i = 0; i < citylist.size(); i++) {
                                    if (selectedDistributerBean != null)
                                        if (selectedDistributerBean.getCity_id().equals(citylist.get(i).getCid())) {
                                            edtcity.setText(citylist.get(i).getCname());
                                            selectedCityBean = citylist.get(i);
                                        }
                                }
                            }
                        }
                    } else {
                        Utils.showAlertDialog(AddNewOutletActivity.this, "Outlet", jsonObject.getString("message"), true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertDialog(AddNewOutletActivity.this, "Outlet", "Improper response from server", true);
//                Toast.makeText(AddNewOutletActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mGetRetailerTypeList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(AddNewOutletActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Getting retailer type list please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getretailtertypelist"));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
            //return new JSONParser().getJsonObjectFromHttp(Constant.BASE_URL, nameValuePair, JSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ArrayList<RetailerBean> mRetailerArraylist = new ArrayList<>();
                        JSONArray mJsonArray = jsonObject.getJSONArray("retailertypelist");
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            String retailer_id = mJsonArray.getJSONObject(i).getString("id");
                            String retailer_type = mJsonArray.getJSONObject(i).getString("retailer_type");
                            String retailerActivationStatus = mJsonArray.getJSONObject(i).getString("activation_status");
                            mRetailerArraylist.add(new RetailerBean(retailer_id, retailer_type, retailerActivationStatus));
                        }
                        mTypeOfAppoinmentBeanList.clear();
                        mTypeOfAppoinmentBeanList.addAll(mRetailerArraylist);
                        mTypeOfAppoinmentAdapter = new TypeOfAppoinmentAdapter(AddNewOutletActivity.this, mTypeOfAppoinmentBeanList);
                        spnTypeOfAppointment.setAdapter(mTypeOfAppoinmentAdapter);
                        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(AddNewOutletActivity.this, Constant.Retailer_LIST_PREF, MODE_PRIVATE);
                        mDistributerListPref.putObject(Constant.Retailer_LIST_OBJ, mTypeOfAppoinmentBeanList);
                        mDistributerListPref.commit();


                        if (mTypeOfAppoinmentBeanList != null) {
                            if (mTypeOfAppoinmentBeanList.size() > 0) {
                                for (int i = 0; i < mTypeOfAppoinmentBeanList.size(); i++) {
                                    if (showOutLetBeen != null) {
                                        //Log.e("<NSC>" + showOutLetBeen.getType_appointment(), "" + mTypeOfAppoinmentBeanList.get(i).getRetailerId());
                                        if (showOutLetBeen.getType_appointment().equals(mTypeOfAppoinmentBeanList.get(i).getRetailerId())) {
                                            spnTypeOfAppointment.setSelection(i);
                                        }
                                    }
                                }
                            }
                        }


                    } else {
                        Utils.showAlertDialog(AddNewOutletActivity.this, "Outlet", jsonObject.getString("message"), true);
//                        Toast.makeText(AddNewOutletActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertDialog(AddNewOutletActivity.this, "Outlet", "Improper response from server", true);
//                Toast.makeText(AddNewOutletActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class addOutletTask extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(AddNewOutletActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            if (showOutLetBeen != null)
                mDialog.setTitle("Updating outlet please wait...");
            else
                mDialog.setTitle("Adding outlet please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();

            if (showOutLetBeen != null) {
                nameValuePair.add(new BasicNameValuePair("method", "update_outlet"));
                nameValuePair.add(new BasicNameValuePair("outlet_id", params[20]));
            } else {
                nameValuePair.add(new BasicNameValuePair("method", "add_outlet"));
            }
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("outletname", params[1]));
            nameValuePair.add(new BasicNameValuePair("outletemail", params[2]));
            nameValuePair.add(new BasicNameValuePair("country_id", params[3]));
            nameValuePair.add(new BasicNameValuePair("city_id", params[4]));
            nameValuePair.add(new BasicNameValuePair("mobile", params[5]));
            nameValuePair.add(new BasicNameValuePair("outlet_address", params[6]));
            nameValuePair.add(new BasicNameValuePair("type_appoint", params[7]));
            nameValuePair.add(new BasicNameValuePair("supply_chain", params[8]));
            nameValuePair.add(new BasicNameValuePair("contect_person", params[9]));
            nameValuePair.add(new BasicNameValuePair("resident_address", params[10]));
            nameValuePair.add(new BasicNameValuePair("contact_number", params[11]));
            nameValuePair.add(new BasicNameValuePair("data", params[12]));
            nameValuePair.add(new BasicNameValuePair("state_id", params[13]));
            nameValuePair.add(new BasicNameValuePair("route_id", params[14]));
            nameValuePair.add(new BasicNameValuePair("territory_id", params[15]));
            nameValuePair.add(new BasicNameValuePair("lat", params[16]));
            nameValuePair.add(new BasicNameValuePair("long", params[17]));
            nameValuePair.add(new BasicNameValuePair("address", params[18]));
            nameValuePair.add(new BasicNameValuePair("district_id", params[19]));

            // JSONObject jsonObjectFromUrl = new JSONParser().getJsonObjectFromHttp(Constant.BASE_URL, nameValuePair, JSONParser.Http.POST);
            //  JSONObject jsonObjectFromUrl = new JSONParser().getJsonObjectFromHttp(Constant.BASE_URL, nameValuePair, JSONParser.Http.POST);
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        //   System.out.print(jsonObject.getString("message").toString());
                        Utils.showAlertDialog(AddNewOutletActivity.this, "Outlet", jsonObject.getString("message"), true);
                    } else {
                        Utils.showAlertDialog(AddNewOutletActivity.this, "Outlet", jsonObject.getString("message"), true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertDialog(AddNewOutletActivity.this, "Outlet", "Improper response from server", true);
            }
            if (mDialog.isShowing()) mDialog.dismiss();
            AddNewOutletActivity.this.finish();
        }
    }
}