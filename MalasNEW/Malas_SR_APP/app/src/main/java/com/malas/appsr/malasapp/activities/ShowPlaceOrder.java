package com.malas.appsr.malasapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.google.gson.reflect.TypeToken;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.OrderStockDifference;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.DistributorSpinnerAdapter;
import com.malas.appsr.malasapp.adapter.ShowPlaceOrderAdapterRecyclerView;

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

public class ShowPlaceOrder extends AppCompatActivity {

    EditText spnDistributer;
    ImageView iv_save_button;
    DistributorSpinnerAdapter mDistributorSpinnerAdapter;
    AsyncTask<String, Void, JSONObject> getOrderStockDifference, getDistributorTaskCalling;
    ArrayList<OrderStockDifference> mOrderStockDifferenceList = new ArrayList<>();
    DistributerBean selectedDistributerBean;
    TextView tverritoryname;
    ComplexPreferences mUserPreference;
    UserLoginInfoBean mUserLoginInfoBean;
    ArrayList<DistributerBean> mDistributerList;
    boolean isShowProgress = false;
    ShowPlaceOrderAdapterRecyclerView mAdapter;
    RecyclerView list_view_show_place_order;
    //    NonScrollListView list_view_show_place_order;
    int spinerSelectedPosition = 0;
    double latitude = 0;
    double longitude = 0;
    String addressStr = "";
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_place_order);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Place Order");
        spnDistributer = findViewById(R.id.spnr_distributr);
        iv_save_button = findViewById(R.id.iv_save_button);
        tverritoryname = findViewById(R.id.tv_territory_stock);
//        list_view_show_place_order = (NonScrollListView) findViewById(R.id.list_view_show_place_order);
        list_view_show_place_order = findViewById(R.id.list_view_show_place_order);

        mUserPreference = ComplexPreferences.getComplexPreferences(ShowPlaceOrder.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = mUserPreference.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

        tverritoryname.setText(mUserLoginInfoBean.getTerritoryName());


        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ShowPlaceOrder.this, Constant.PlaceOrderProductListPref, MODE_PRIVATE);
        Type typeOrderStockDifferenceList = new TypeToken<ArrayList<OrderStockDifference>>() {
        }.getType();
        mOrderStockDifferenceList = complexPreferences.getArray(Constant.PlaceOrderProductListObj, typeOrderStockDifferenceList);

        getCurrentLocation();

        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(ShowPlaceOrder.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
        Type typeDistributor = new TypeToken<ArrayList<DistributerBean>>() {
        }.getType();
        mDistributerList = mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor) == null ? new ArrayList<>() : (ArrayList<DistributerBean>) mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor);
        getDistributorTaskCalling = new mDistributorList().execute(mUserLoginInfoBean.getUserTerritoryId());


        spnDistributer.setOnClickListener(v -> {
            if (mDistributerList != null) {
                final Dialog dialog = new Dialog(ShowPlaceOrder.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView_Counry = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                mDistributorSpinnerAdapter = new DistributorSpinnerAdapter(ShowPlaceOrder.this, mDistributerList, "TakeOrder");
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
                    String countryname = DistributorSpinnerAdapter.resultArrayshort.get(position).getFirm_name();
                    selectedDistributerBean = DistributorSpinnerAdapter.resultArrayshort.get(position);
                    spnDistributer.setText(countryname);
                    //  citylist.clear();
                    dialog.dismiss();
                    try {
                        mOrderStockDifferenceList = null;
                        mOrderStockDifferenceList = new ArrayList<>();
                        isShowProgress = true;
                        mAdapter = new ShowPlaceOrderAdapterRecyclerView(ShowPlaceOrder.this, mOrderStockDifferenceList == null ? new ArrayList() : mOrderStockDifferenceList);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        list_view_show_place_order.setLayoutManager(mLayoutManager);
                        list_view_show_place_order.setItemAnimator(new DefaultItemAnimator());
                        list_view_show_place_order.setAdapter(mAdapter);

                        spinerSelectedPosition = position;
                        ComplexPreferences complexPreferences1 = ComplexPreferences.getComplexPreferences(ShowPlaceOrder.this, Constant.UserRegInfoPref, MODE_PRIVATE);
                        UserLoginInfoBean mUserLoginInfoBean = complexPreferences1.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
                        String distributerId = mDistributerList.get(position).getDistribution_id();
                        String userId = mUserLoginInfoBean.getUserId();
                        getOrderStockDifference = new mOrderStockDifference().execute(userId, distributerId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        });


        iv_save_button.setOnClickListener(v -> {
            if (selectedDistributerBean != null) {

                JSONObject orderJsonObject = new JSONObject();

                ComplexPreferences complexPreferences12 = ComplexPreferences.getComplexPreferences(ShowPlaceOrder.this, Constant.PlaceOrderProductListPref, MODE_PRIVATE);
                Type typeOrderStockDifferenceList1 = new TypeToken<ArrayList<OrderStockDifference>>() {
                }.getType();
                ArrayList<OrderStockDifference> mChangedOrderStockDifferenceList = complexPreferences12.getArray(Constant.PlaceOrderProductListObj, typeOrderStockDifferenceList1);

                JSONArray jsonArray = new JSONArray();
                JSONObject obj;

                if (mChangedOrderStockDifferenceList != null) {
                    for (int i = 0; i < mChangedOrderStockDifferenceList.size(); i++) {
                        obj = new JSONObject();
                        try {
                            obj.put("item_id", mChangedOrderStockDifferenceList.get(i).getId());
                            obj.put("item_name", mChangedOrderStockDifferenceList.get(i).getName());
                            obj.put("orderquty", mChangedOrderStockDifferenceList.get(i).getOrderQty());
                            obj.put("stockquty", mChangedOrderStockDifferenceList.get(i).getStockQty());
                            obj.put("diffrance", mChangedOrderStockDifferenceList.get(i).getDifference());
                            obj.put("paketsize", mChangedOrderStockDifferenceList.get(i).getInboxSize());
                            obj.put("sku_code", mChangedOrderStockDifferenceList.get(i).getSkuCode());
                            obj.put("category_name", mChangedOrderStockDifferenceList.get(i).getCategoryName());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArray.put(obj);
                    }

                    try {
                        orderJsonObject.put("orders", jsonArray);

                        Log.e("", "" + orderJsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new mSubmitOrderStockDifference().execute(mUserLoginInfoBean.getUserId(), selectedDistributerBean.getDistribution_id(), orderJsonObject.toString(), "" + latitude, "" + longitude, addressStr);
                }

            } else {
                Toast.makeText(ShowPlaceOrder.this, "Please Select Distributor First", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

//        isShowProgress = false;
//        getOrderStockDifference = new mOrderStockDifference().execute(mUserLoginInfoBean.getUser_id());
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (getOrderStockDifference != null && !getOrderStockDifference.isCancelled())
            getOrderStockDifference.cancel(true);
        if (getDistributorTaskCalling != null && !getDistributorTaskCalling.isCancelled())
            getDistributorTaskCalling.cancel(true);

    }

    public void getCurrentLocation() {

        boolean result = Utils.checkPermissions(ShowPlaceOrder.this, new String[]{
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
                    getAddressByLatLng(latitude, longitude);
                    String lat = Double.toString(location.getLatitude());
                    String lon = Double.toString(location.getLongitude());
                    Log.e("Your Location is", "" + lat + "--" + lon);
                    Log.e("Your Location is", " addressStr " + addressStr);
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
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
        // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        Log.e("address", "" + addressStr);
    }

    @SuppressLint("StaticFieldLeak")
    public class mSubmitOrderStockDifference extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(ShowPlaceOrder.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "placeorder"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("distributorid", params[1]));
            nameValuePair.add(new BasicNameValuePair("data", params[2]));
            nameValuePair.add(new BasicNameValuePair("lat", params[3]));
            nameValuePair.add(new BasicNameValuePair("long", params[4]));
            nameValuePair.add(new BasicNameValuePair("address", params[5]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.e("Result JSON Object", "" + jsonObject);
                        Toast.makeText(ShowPlaceOrder.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ShowPlaceOrder.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(ShowPlaceOrder.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mOrderStockDifference extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(ShowPlaceOrder.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Orders List please wait");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getorderstockdiffrence"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("distributor_id", params[1]));
            //JSONObject jsonObjectFromUrl = new JSONParser().getJsonObjectFromHttp(Constant.BASE_URL, nameValuePair, JSONParser.Http.POST);

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Toast.makeText(ShowPlaceOrder.this, "Order Stock Difference List get successfully", Toast.LENGTH_SHORT).show();
                        ArrayList<OrderStockDifference> mOrderStockDifferenceTempList = new ArrayList<>();
                        System.out.print(jsonObject.getString("message"));
                        Toast.makeText(ShowPlaceOrder.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONArray mJsonArray = jsonObject.getJSONArray("output");
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            String item_id = mJsonArray.getJSONObject(i).getString("item_id");
                            String item_name = mJsonArray.getJSONObject(i).getString("item_name");
                            String orderquty = mJsonArray.getJSONObject(i).getString("orderquty");
                            String stockquty = mJsonArray.getJSONObject(i).getString("stockquty");
                            String difference = mJsonArray.getJSONObject(i).getString("diffrance");
                            String pktSize = mJsonArray.getJSONObject(i).getString("paketsize");
                            String sku_code = mJsonArray.getJSONObject(i).getString("sku_code");
                            String cat_name = mJsonArray.getJSONObject(i).getString("category_name");

                            String inboxSize;
                            if (Integer.parseInt(difference) > 0) {
                                inboxSize = "0";
                            } else {
                                int r = Math.abs(Integer.parseInt(difference)) % Integer.parseInt(pktSize);
                                int q = Math.abs(Integer.parseInt(difference)) / Integer.parseInt(pktSize);
                                System.out.println("nsc Quotient : " + q);
                                System.out.println("nsc Remainder : " + r);
                                if (r > 0) {
                                    q = q + 1;
                                }

                                inboxSize = "" + q;
                            }


                            mOrderStockDifferenceTempList.add(new OrderStockDifference(item_id, item_name, orderquty, stockquty, difference, pktSize, sku_code, cat_name, inboxSize));
                        }
                        mOrderStockDifferenceList.clear();
                        mOrderStockDifferenceList.addAll(mOrderStockDifferenceTempList);
                        ComplexPreferences mPlaceOrderListPref = ComplexPreferences.getComplexPreferences(ShowPlaceOrder.this, Constant.PlaceOrderProductListPref, MODE_PRIVATE);
                        mPlaceOrderListPref.putObject(Constant.PlaceOrderProductListObj, mOrderStockDifferenceTempList);
                        mPlaceOrderListPref.commit();


                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ShowPlaceOrder.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(ShowPlaceOrder.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mDistributorList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(ShowPlaceOrder.this);
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
                        System.out.print(jsonObject.getString("message"));
                        Toast.makeText(ShowPlaceOrder.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(ShowPlaceOrder.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
                        mDistributerListPref.putObject(Constant.DISTRIBUTOR_LIST_OBJ, mDistributerArraylist);
                        mDistributerListPref.commit();
                    } else {
                        Toast.makeText(ShowPlaceOrder.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(ShowPlaceOrder.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }

    }
}