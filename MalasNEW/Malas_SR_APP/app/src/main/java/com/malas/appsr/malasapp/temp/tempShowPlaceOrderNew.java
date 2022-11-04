package com.malas.appsr.malasapp.temp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.google.gson.reflect.TypeToken;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.PlaceOrderItemBean;
import com.malas.appsr.malasapp.BeanClasses.PlaceOrderListBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.activities.ConfirmPlaceOrderActivity;

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

public class tempShowPlaceOrderNew extends AppCompatActivity implements tempPlaceOrderCategoryAdapter.tempPlaceOrderCategoryItemClicked {

    ArrayList<PlaceOrderListBean> itemList;
    ArrayList<PlaceOrderListBean> listDataHeader;
    AsyncTask<String, Void, JSONObject> saveDataAsyn;
    AsyncTask<String, Void, JSONObject> getOrderStockDifference, getDistributorTaskCalling;
    RecyclerView lvCategoryList, lvItemList;
    ImageView mSaveButton;
    double latitude = 0;
    double longitude = 0;
    String addressStr = "";
    LocationManager locationManager;
    LocationListener locationListener;
    TextView tverritoryname;
    ArrayList<DistributerBean> mDistributerList;
    ComplexPreferences mUserPreference;
    UserLoginInfoBean mUserLoginInfoBean;

    LinearLayout llCategoryItems;
    private String firmName;
    private String distributerId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_show_place_order_new);
        lvCategoryList = findViewById(R.id.category_list);
        lvItemList =  findViewById(R.id.item_list);
        TextView mdistributerName = findViewById(R.id.spnr_distributr);
        mSaveButton = findViewById(R.id.iv_save_button);
        tverritoryname = findViewById(R.id.tv_territory_stock);
        llCategoryItems = findViewById(R.id.llCategoryItems);
        llCategoryItems.setVisibility(View.GONE);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Distributor Order");

        getCurrentLocation();
        if (getIntent().hasExtra("firmName")) {
            firmName = getIntent().getStringExtra("firmName");
        }

        mdistributerName.setText(firmName);
        distributerId = getIntent().getStringExtra("distribbuterId");


        mUserPreference = ComplexPreferences.getComplexPreferences(this, Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = mUserPreference.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

        tverritoryname.setText(mUserLoginInfoBean.getTerritoryName());
        llCategoryItems.setVisibility(View.GONE);
        clearList();
        getOrderStockDifference = new mGetPlaceOrderItemList().execute(mUserLoginInfoBean.getUserId(), distributerId);

     /*   lvCategoryList.setOnItemClickListener((parent, view, position, arg3) -> {
            //view.setSelected(true);
            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(this, Constant.PlaceOrderProductListPref, MODE_PRIVATE);
            Type typePlaceOrder = new TypeToken<ArrayList<PlaceOrderListBean>>() {
            }.getType();
            if (itemList != null)
                itemList.clear();
            itemList = complexPreferences.getArray(Constant.PlaceOrderProductListObj, typePlaceOrder) == null ? new ArrayList<>() : (ArrayList<PlaceOrderListBean>) complexPreferences.getArray(Constant.PlaceOrderProductListObj, typePlaceOrder);
            if (itemList != null && itemList.size() > 0) {
                PlaceOrderItemListAdapter mPlaceOrderItemListAdapter = new PlaceOrderItemListAdapter(this, itemList.get(position), position);
                lvItemList.setAdapter(mPlaceOrderItemListAdapter);
            }
        });*/

        mSaveButton.setOnClickListener(v -> {


            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(this, Constant.PlaceOrderProductListPref, MODE_PRIVATE);
            Type typePlaceOrder = new TypeToken<ArrayList<PlaceOrderListBean>>() {
            }.getType();
            if (itemList != null)
                itemList.clear();
            itemList = complexPreferences.getArray(Constant.PlaceOrderProductListObj, typePlaceOrder) == null ? new ArrayList<PlaceOrderListBean>() : (ArrayList<PlaceOrderListBean>) complexPreferences.getArray(Constant.PlaceOrderProductListObj, typePlaceOrder);


            if (itemList != null) {
                if (itemList.size() > 0) {
                    Intent submitconfirmation = new Intent(this, ConfirmPlaceOrderActivity.class);
                    submitconfirmation.putExtra("itemList", getProductList());
                    submitconfirmation.putExtra("userId", mUserLoginInfoBean.getUserId());
                    submitconfirmation.putExtra("distributorId", distributerId);
                    submitconfirmation.putExtra("latitude", latitude);
                    submitconfirmation.putExtra("longitude", longitude);
                    submitconfirmation.putExtra("address", addressStr);
                    startActivityForResult(submitconfirmation, 1);
                }
            } else {
                Utils.showAlertDialog(this, "Place Order", "No Order Found", true);
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//Toast.makeText(ShowPlaceOrderNew.this,"RESUME",Toast.LENGTH_SHORT).show();

        //   getProductList();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void clearList() {
        ComplexPreferences mComplexPreferences = ComplexPreferences.getComplexPreferences(this, Constant.PlaceOrderProductListPref, MODE_PRIVATE);
        mComplexPreferences.clear(Constant.PlaceOrderProductListPref);
        if (itemList != null)
            itemList.clear();
    }

    public ArrayList<PlaceOrderListBean> getProductList() {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(this, Constant.PlaceOrderProductListPref, MODE_PRIVATE);
        Type typePlaceOrder = new TypeToken<ArrayList<PlaceOrderListBean>>() {
        }.getType();
        itemList = complexPreferences.getArray(Constant.PlaceOrderProductListObj, typePlaceOrder) == null ? new ArrayList<>() : (ArrayList<PlaceOrderListBean>) complexPreferences.getArray(Constant.PlaceOrderProductListObj, typePlaceOrder);
        return itemList;
    }

    public void saveProductList(ArrayList<PlaceOrderListBean> itemList) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(this, Constant.PlaceOrderProductListPref, MODE_PRIVATE);
        complexPreferences.putObject(Constant.PlaceOrderProductListObj, itemList);
        complexPreferences.commit();
    }

    private void prepareListData(ArrayList<PlaceOrderListBean> itemList) {
        if (itemList != null) {
            llCategoryItems.setVisibility(View.VISIBLE);
            listDataHeader = new ArrayList<>();
            listDataHeader.addAll(itemList);
            tempPlaceOrderCategoryAdapter mPlaceOrderCategoryAdapter = new tempPlaceOrderCategoryAdapter(this, listDataHeader,this);
            lvCategoryList.setAdapter(mPlaceOrderCategoryAdapter);

            lvCategoryList.setSelected(true);

            if (itemList.size() > 0) {
                tempPlaceOrderItemListAdapter mPlaceOrderItemListAdapter = new tempPlaceOrderItemListAdapter(this, itemList.get(0), 0);
                lvItemList.setAdapter(mPlaceOrderItemListAdapter);
            }
        }
    }

    public void getCurrentLocation() {

        boolean result = Utils.checkPermissions(this, new String[]{
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
                   /* Log.e("Your Location is", "" + lat + "--" + lon);
                    Log.e("Your Location is", " addressStr " + addressStr);*/
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
        // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        //Log.e("address", "" + addressStr);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<PlaceOrderListBean> itemListOnCancel = (ArrayList<PlaceOrderListBean>) data.getSerializableExtra("itemList");
                prepareListData(itemListOnCancel);
                saveProductList(itemListOnCancel);
            } else if (resultCode == 0) {
                System.out.println("RESULT CANCELLED");
                finish();

            }
        }
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (status != null) {
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.mipmap.malas_logo : R.drawable.icon_approve);
        }  // No icon set to preference


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                finish();
                dialog.cancel();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void tempPlaceOrderCategoryOnClick(int position) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(this, Constant.PlaceOrderProductListPref, MODE_PRIVATE);
        Type typePlaceOrder = new TypeToken<ArrayList<PlaceOrderListBean>>() {
        }.getType();
        if (itemList != null)
            itemList.clear();
        itemList = complexPreferences.getArray(Constant.PlaceOrderProductListObj, typePlaceOrder) == null ? new ArrayList<>() : (ArrayList<PlaceOrderListBean>) complexPreferences.getArray(Constant.PlaceOrderProductListObj, typePlaceOrder);
        if (itemList != null && itemList.size() > 0) {
            tempPlaceOrderItemListAdapter mPlaceOrderItemListAdapter = new tempPlaceOrderItemListAdapter(this, itemList.get(position), position);
            lvItemList.setAdapter(mPlaceOrderItemListAdapter);
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class mGetPlaceOrderItemList extends AsyncTask<String, Void, JSONObject> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(getApplicationContext());
            mProgressDialog.setMessage("Fetching Items List from server......");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            //  nameValuePair.add(new BasicNameValuePair("method", "getorderstockdiffrencenew"));
            nameValuePair.add(new BasicNameValuePair("method", "getorderstockdiffrence"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("distributor_id", params[1]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONArray jArray = jsonObject.getJSONArray("output");
                        itemList = new ArrayList<>();
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jobj = jArray.getJSONObject(i);
                            String id = jobj.getString("id");
                            String cat_name = jobj.getString("cat_name");
                            JSONArray itmArry = jobj.getJSONArray("items");
                            ArrayList<PlaceOrderItemBean> arryItemList = new ArrayList<>();
                            for (int j = 0; j < itmArry.length(); j++) {
                                String item_id = itmArry.getJSONObject(j).getString("item_id");
                                String item_name = itmArry.getJSONObject(j).getString("item_name");
                                String orderquty = itmArry.getJSONObject(j).getString("orderquty");
                                String stockquty = itmArry.getJSONObject(j).getString("stockquty");
                                String difference = itmArry.getJSONObject(j).getString("diffrance");
                                String pktSize = itmArry.getJSONObject(j).getString("paketsize");
                                String sku_code = itmArry.getJSONObject(j).getString("sku_code");


                                String inboxSize = null;
                                try {
                                    inboxSize = "0";
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
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                                arryItemList.add(new PlaceOrderItemBean(item_id, item_name, orderquty, stockquty, difference, pktSize, inboxSize, sku_code, cat_name));
                            }

                            itemList.add(new PlaceOrderListBean(id, cat_name, arryItemList));

                        }
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getApplicationContext(), Constant.PlaceOrderProductListPref, MODE_PRIVATE);
                        complexPreferences.putObject(Constant.PlaceOrderProductListObj, itemList);
                        ComplexPreferences complexPreferencesTemp = ComplexPreferences.getComplexPreferences(getApplicationContext(), Constant.PlaceOrderProductListTempPref, MODE_PRIVATE);

                        Type typePlaceOrder = new TypeToken<ArrayList<PlaceOrderListBean>>() {
                        }.getType();
                        ArrayList<PlaceOrderListBean> itemListtemp = complexPreferencesTemp.getArray(Constant.PlaceOrderProductList, typePlaceOrder) == null ? new ArrayList<>() : (ArrayList<PlaceOrderListBean>) complexPreferencesTemp.getArray(Constant.PlaceOrderProductList, typePlaceOrder);
                        if (itemListtemp != null && itemListtemp.size() == 0) {
                            complexPreferencesTemp.putObject(Constant.PlaceOrderProductList, itemList);

                        }

                        complexPreferences.commit();
                        complexPreferencesTemp.commit();
                        prepareListData(itemList);


                    } else {
                        showAlertDialog(getApplicationContext(), "Place Order", jsonObject.getString("message"), true);
//                        Toast.makeText(ShowPlaceOrderNew.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertDialog(getApplicationContext(), "Place Order", "Improper response from server", true);
//                Toast.makeText(ShowPlaceOrderNew.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
        }
    }
    @SuppressLint("StaticFieldLeak")
    public class getdifference extends AsyncTask<String, Void, JSONObject> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(getApplicationContext());
            mProgressDialog.setMessage("Fetching Items List from server......");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            //  nameValuePair.add(new BasicNameValuePair("method", "getorderstockdiffrencenew"));
            nameValuePair.add(new BasicNameValuePair("method", "getplaceorderlist"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("distributor_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("item_id", params[2]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONArray jArray = jsonObject.getJSONArray("output");
                        itemList = new ArrayList<>();
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jobj = jArray.getJSONObject(i);
                            String id = jobj.getString("id");
                            String cat_name = jobj.getString("cat_name");
                            JSONArray itmArry = jobj.getJSONArray("items");
                            ArrayList<PlaceOrderItemBean> arryItemList = new ArrayList<>();
                            for (int j = 0; j < itmArry.length(); j++) {
                                String item_id = itmArry.getJSONObject(j).getString("item_id");
                                String item_name = itmArry.getJSONObject(j).getString("item_name");
                                String orderquty = itmArry.getJSONObject(j).getString("orderquty");
                                String stockquty = itmArry.getJSONObject(j).getString("stockquty");
                                String difference = itmArry.getJSONObject(j).getString("diffrance");
                                String pktSize = itmArry.getJSONObject(j).getString("paketsize");
                                String sku_code = itmArry.getJSONObject(j).getString("sku_code");


                                String inboxSize = null;
                                try {
                                    inboxSize = "0";
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
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                                arryItemList.add(new PlaceOrderItemBean(item_id, item_name, orderquty, stockquty, difference, pktSize, inboxSize, sku_code, cat_name));
                            }

                            itemList.add(new PlaceOrderListBean(id, cat_name, arryItemList));

                        }
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getApplicationContext(), Constant.PlaceOrderProductListPref, MODE_PRIVATE);
                        complexPreferences.putObject(Constant.PlaceOrderProductListObj, itemList);
                        ComplexPreferences complexPreferencesTemp = ComplexPreferences.getComplexPreferences(getApplicationContext(), Constant.PlaceOrderProductListTempPref, MODE_PRIVATE);

                        Type typePlaceOrder = new TypeToken<ArrayList<PlaceOrderListBean>>() {
                        }.getType();
                        ArrayList<PlaceOrderListBean> itemListtemp = complexPreferencesTemp.getArray(Constant.PlaceOrderProductList, typePlaceOrder) == null ? new ArrayList<>() : (ArrayList<PlaceOrderListBean>) complexPreferencesTemp.getArray(Constant.PlaceOrderProductList, typePlaceOrder);
                        if (itemListtemp != null && itemListtemp.size() == 0) {
                            complexPreferencesTemp.putObject(Constant.PlaceOrderProductList, itemList);

                        }

                        complexPreferences.commit();
                        complexPreferencesTemp.commit();
                        prepareListData(itemList);


                    } else {
                        showAlertDialog(getApplicationContext(), "Place Order", jsonObject.getString("message"), true);
//                        Toast.makeText(ShowPlaceOrderNew.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertDialog(getApplicationContext(), "Place Order", "Improper response from server", true);
//                Toast.makeText(ShowPlaceOrderNew.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
        }
    }

}