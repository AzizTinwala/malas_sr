package com.malas.appsr.malasapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.ReasonBean;
import com.malas.appsr.malasapp.BeanClasses.ReasonSubmitBean;
import com.malas.appsr.malasapp.BeanClasses.ShowOutLetBeen;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.ReasonSpinnerAdapter;
import com.malas.appsr.malasapp.dbHandler.DatabaseHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Admin on 26-Dec-17.
 */

public class SRReasonActivity extends Activity {
    ListView lvReasonList;
    Button btnCancel, btnSave;
    ReasonSpinnerAdapter mReasonSpinnerAdapter;
    ArrayList<ReasonBean> mReasonList;
    String reasonId;
    LocationManager locationManager;
    LocationListener locationListener;
    double latitude = 0;
    double longitude = 0;
    String addressStr = "";
    ReasonBean reasonBean;
    DatabaseHandler db;
    private ShowOutLetBeen showOutLetBeen;
    private DistributerBean distributerBean;
    private UserLoginInfoBean mUserLoginInfoBean;
    private String reasonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_reason);


        initview();
        db = new DatabaseHandler(SRReasonActivity.this);
        setOnClick();
        if (getIntent().hasExtra("outletBean")) {
            showOutLetBeen = (ShowOutLetBeen) getIntent().getSerializableExtra("outletBean");
        }


        if (getIntent().hasExtra("distributerBean")) {
            distributerBean = (DistributerBean) getIntent().getSerializableExtra("distributerBean");
            String firmName = distributerBean.getFirm_name();
        }

        if (Utils.isInternetConnected(SRReasonActivity.this)) {
            new mReasonList().execute();

        } else {
            ArrayList<ReasonBean> reasonBeanList = db.getAllReasonListRecord();

            mReasonSpinnerAdapter = new ReasonSpinnerAdapter(SRReasonActivity.this, reasonBeanList);
            lvReasonList.setAdapter(mReasonSpinnerAdapter);

        }
        getCurrentLocation();

        ComplexPreferences mUserPreference = ComplexPreferences.getComplexPreferences(SRReasonActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = mUserPreference.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

    }

    private void setOnClick() {
        this.setFinishOnTouchOutside(false);

        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {

            if (reasonId != null) {

                if (Utils.isInternetConnected(SRReasonActivity.this)) {

                    new mSubmitReason().execute(mUserLoginInfoBean.getUserId(), showOutLetBeen.getOutlet_id(), reasonId, distributerBean.getDistribution_id(), "" + latitude, "" + longitude, addressStr);

                } else {
                    ReasonSubmitBean reasonSubmitBean = new ReasonSubmitBean(mUserLoginInfoBean.getUserId(), showOutLetBeen.getOutlet_id(), reasonId, distributerBean.getDistribution_id(), "" + latitude, "" + longitude, addressStr, showOutLetBeen.getOutlet_name(), reasonName, showOutLetBeen.getRoute_id());
                    db.addSubmittedReasonData(reasonSubmitBean);
                    //ArrayList<ReasonSubmitBean> reasonSubmitBeans = db.getAllReasonSubmitRecord();
                    /*for (int i = 0; i < reasonSubmitBeans.size(); i++) {
                        Log.v("SUBMIT REASON", reasonSubmitBeans.get(i).getDistributorId() + reasonSubmitBeans.get(i).getOutletId() + reasonSubmitBeans.get(i).getUserId());
                    }*/
                    Toast.makeText(SRReasonActivity.this, "Reason Added Successfully In offline Mode", Toast.LENGTH_SHORT).show();

                    finish();
                }
            } else {
                Toast.makeText(SRReasonActivity.this, "Please Select Reason", Toast.LENGTH_SHORT).show();
            }
        });
        lvReasonList.setOnItemClickListener((parent, view, position, id) -> {
            view.setSelected(true);

            reasonBean = (ReasonBean) parent.getItemAtPosition(position);
            reasonId = reasonBean.getId();
            reasonName = reasonBean.getReason();
            // Toast.makeText(SRReasonActivity.this, "ID" + reasonId, Toast.LENGTH_SHORT).show();
        });
    }

    private void initview() {
        lvReasonList = findViewById(R.id.lv_reason);
        btnCancel = findViewById(R.id.btn_cancel_reason);
        btnSave = findViewById(R.id.btn_save_reason);
    }

    public void getCurrentLocation() {

        boolean result = Utils.checkPermissions(SRReasonActivity.this, new String[]{
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
                    // Log.e("Your Location is", "" + lat + "--" + lon);
                    //  Log.e("Your Location is", " addressStr " + addressStr);

                    if (locationListener != null) {
                        if (locationManager != null)
                            locationManager.removeUpdates(locationListener);
                    }
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

        // Log.e("address", "" + addressStr);
    }

    @SuppressLint("StaticFieldLeak")
    public class mReasonList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(SRReasonActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Reasons List please wait");
            mDialog.setCancelable(false);
//            if (mReasonList.isEmpty())
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getallreason"));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        mReasonList = new ArrayList<>();
                        System.out.print(jsonObject.getString("message"));

                        JSONArray reasonArray = jsonObject.getJSONArray("reason");

                        for (int i = 0; i < reasonArray.length(); i++) {
                            JSONObject jobj = reasonArray.getJSONObject(i);
                            mReasonList.add(new ReasonBean(jobj.getString("id"), jobj.getString("reason")));
                        }


                        ArrayList<ReasonBean> reasonBeans = db.getAllReasonListRecord();
                        if (reasonBeans.size() > 0) {
                            db.deleteReasonList();
                            db.addReasonList(mReasonList);
                        } else {
                            db.addReasonList(mReasonList);
                        }


                        mReasonSpinnerAdapter = new ReasonSpinnerAdapter(SRReasonActivity.this, mReasonList);
                        lvReasonList.setAdapter(mReasonSpinnerAdapter);
                        /*lvReasonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                view.setSelected(true);

                                reasonBean = (ReasonBean) parent.getItemAtPosition(position);
                                reasonId = reasonBean.getId();
                                // Toast.makeText(SRReasonActivity.this, "ID" + reasonId, Toast.LENGTH_SHORT).show();
                            }
                        });
*/
                  /*      ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(SRReasonActivity.this, Constant.REASON_LIST_PREF, MODE_PRIVATE);
                         mDistributerListPref.putObject(Constant.REASON_LIST_OBJ, mReasonArraylist);
                        mDistributerListPref.commit();
                        mReasonList.clear();*/
                        /*    mReasonList.addAll(mReasonArraylist);*/
                    } else {
                        Utils.showToast(SRReasonActivity.this, jsonObject.getString("message"));
//                        Toast.makeText(OutletOrdersActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertDialog(SRReasonActivity.this, "Outlet Order", "Improper response from server", true);
//                Toast.makeText(OutletOrdersActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mSubmitReason extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(SRReasonActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "add_reason"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("outletid", params[1]));
            nameValuePair.add(new BasicNameValuePair("reason_id", params[2]));
            nameValuePair.add(new BasicNameValuePair("distributor_id", params[3]));
            nameValuePair.add(new BasicNameValuePair("lat", params[4]));
            nameValuePair.add(new BasicNameValuePair("long", params[5]));
            nameValuePair.add(new BasicNameValuePair("address", params[6]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        //Log.e("Result JSON Object", "" + jsonObject.toString());
                        db.deleteSrStatusCountByID(showOutLetBeen.getOutlet_id());
                        db.addSrReasonCountByID(mUserLoginInfoBean.getUserId(), showOutLetBeen.getOutlet_id(), getCurrentDate(), "1");

                        //   Utils.showAlertDialog(SRReasonActivity.this, "Outlet Order", jsonObject.getString("message").toString(), true);
                        Utils.showToast(SRReasonActivity.this, jsonObject.getString("message"));
                        if (mDialog != null) mDialog.dismiss();

                        finish();
                    } else {
                        Utils.showToast(SRReasonActivity.this, jsonObject.getString("message"));
//                        Toast.makeText(OutletOrdersActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                        if (mDialog != null) mDialog.dismiss();

                        finish();
                    }
                } catch (JSONException e) {
                    if (mDialog != null) mDialog.dismiss();
                    e.printStackTrace();
                }

            } else {
                Utils.showToast(SRReasonActivity.this, "Improper response from server");
//                          if(mDialog != null) mDialog.dismiss();  Toast.makeText(OutletOrdersActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCurrentDate() {

        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }
}
