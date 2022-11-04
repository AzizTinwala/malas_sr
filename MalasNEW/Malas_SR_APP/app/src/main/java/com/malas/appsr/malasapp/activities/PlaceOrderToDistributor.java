package com.malas.appsr.malasapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.google.gson.reflect.TypeToken;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.AttendanceReasonBean;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.DistributorSpinnerAdapter;
import com.malas.appsr.malasapp.session.SessionManagement;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

//import android.util.Log;

public class PlaceOrderToDistributor extends AppCompatActivity {

    public boolean dsr_update;
    RelativeLayout rlPlaceOrder;
    AsyncTask<String, Void, JSONObject> getDistributorTaskCalling;
    EditText spnDistributer;
    DistributorSpinnerAdapter mDistributorSpinnerAdapter;
    DistributerBean selectedDistributerBean;
    TextView tverritoryname;
    ComplexPreferences mUserPreference;
    UserLoginInfoBean mUserLoginInfoBean;
    ArrayList<DistributerBean> mDistributerList;
    int spinerSelectedPosition = 0;
    double latitude = 0;
    double longitude = 0;
    String addressStr = "";
    LocationManager locationManager;
    LocationListener locationListener;
    String previousDayDate;
    private String attendanceReason;
    private boolean ordersPresent;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_to_distributor);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Daily Sales Report");

        session = new SessionManagement(this);

        if (getIntent() != null) {
            previousDayDate = getIntent().getStringExtra("previousDayDate");
            attendanceReason = getIntent().getStringExtra("attendanceReason");

        }
        if (attendanceReason == null) {

            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(PlaceOrderToDistributor.this, Constant.ATTENDANCE_LIST_PREF, MODE_PRIVATE);
            AttendanceReasonBean attendanceReasonBean = complexPreferences.getObject(Constant.ATTENDANCE_LIST_OBJ, AttendanceReasonBean.class);
            attendanceReason = attendanceReasonBean.getAttendanceReason();

        }
        if (previousDayDate != null) {
            showAlertDialog("abc", previousDayDate);

        } else {
            showAlertDialog("abc", currentDate());

            //  new mGenerateDailyReport().execute(mUserLoginInfoBean.getUser_id(), selectedDistributerBean.getDistribution_id(), currentDate());//or previousdaydate
        }


        spnDistributer = findViewById(R.id.spnr_distributr);
        tverritoryname = findViewById(R.id.tv_territory_stock);
        rlPlaceOrder = findViewById(R.id.rlPlaceOrder);


        mUserPreference = ComplexPreferences.getComplexPreferences(PlaceOrderToDistributor.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = mUserPreference.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

        tverritoryname.setText(mUserLoginInfoBean.getTerritoryName());

        getCurrentLocation();

        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(PlaceOrderToDistributor.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
        Type typeDistributor = new TypeToken<ArrayList<DistributerBean>>() {
        }.getType();
        mDistributerList = mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor) == null ? new ArrayList<>() : (ArrayList<DistributerBean>) mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor);
        getDistributorTaskCalling = new mDistributorList().execute(mUserLoginInfoBean.getUserTerritoryId());

        spnDistributer.setOnClickListener(v -> {
            if (mDistributerList != null) {
                final Dialog dialog = new Dialog(PlaceOrderToDistributor.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView_Counry = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                mDistributorSpinnerAdapter = new DistributorSpinnerAdapter(PlaceOrderToDistributor.this, mDistributerList, "TakeOrder");
                listView_Counry.setAdapter(mDistributorSpinnerAdapter);

                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String st = etserach.getText().toString();
                        mDistributorSpinnerAdapter.filter(st);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView_Counry.setOnItemClickListener((parent, view, position, id) -> {
                    String countryname = DistributorSpinnerAdapter.resultArrayshort.get(position).getFirm_name();
                    selectedDistributerBean = DistributorSpinnerAdapter.resultArrayshort.get(position);
                    spnDistributer.setText(countryname);
                    dialog.dismiss();
                    try {
                        spinerSelectedPosition = position;
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(PlaceOrderToDistributor.this, Constant.UserRegInfoPref, MODE_PRIVATE);
                        UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
                        String distributerId = mDistributerList.get(position).getDistribution_id();
                        String userId = mUserLoginInfoBean.getUserId();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        });


        rlPlaceOrder.setOnClickListener(v -> {

            if (selectedDistributerBean != null) {

                if (getIntent() != null) {
                    previousDayDate = getIntent().getStringExtra("previousDayDate");
                    attendanceReason = getIntent().getStringExtra("attendanceReason");

                }
                if (attendanceReason == null) {

                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(PlaceOrderToDistributor.this, Constant.ATTENDANCE_LIST_PREF, MODE_PRIVATE);
                    AttendanceReasonBean attendanceReasonBean = complexPreferences.getObject(Constant.ATTENDANCE_LIST_OBJ, AttendanceReasonBean.class);
                    attendanceReason = attendanceReasonBean.getAttendanceReason();

                }
                if (previousDayDate != null) {
                    showAlertDialog(selectedDistributerBean.getFirm_name(), previousDayDate);

                } else {
                    showAlertDialog(selectedDistributerBean.getFirm_name(), currentDate());

                    //  new mGenerateDailyReport().execute(mUserLoginInfoBean.getUser_id(), selectedDistributerBean.getDistribution_id(), currentDate());//or previousdaydate
                }

            } else {
                Toast.makeText(PlaceOrderToDistributor.this, "Please Select Distributor First", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String currentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return df.format(c.getTime());
    }

    public void getCurrentLocation() {

        boolean result = Utils.checkPermissions(PlaceOrderToDistributor.this, new String[]{
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

    @Override
    protected void onResume() {
        super.onResume();
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

    public void showAlertDialog(String message, String date) {

        AlertDialog.Builder builder = new AlertDialog.Builder(PlaceOrderToDistributor.this);

        builder.setCancelable(false);
        builder.setIcon(R.mipmap.malas_logo);
        builder.setTitle("Confirmation of Day End");
        //  builder.setMessage("DO YOU WANT TO END THE DAY WITH " + message);
        builder.setMessage("DO YOU WANT TO END THE DAY & GENERATE DSR ?");
        builder.setPositiveButton("YES", (dialog, which) -> {

            //   new mGenerateDailyReport().execute(mUserLoginInfoBean.getUser_id(), selectedDistributerBean.getDistribution_id(), date);//or previousdaydate
            new mGenerateDailyReport().execute(mUserLoginInfoBean.getUserId(), "0", date);//or previousdaydate
            dialog.dismiss();
            if (Utils.isInternetConnected(this)) {
               if (session.getUserDetails().get(session.getKEY_ClockOut()) == null && session.isClockedIN()) {
                    startActivity(new Intent(PlaceOrderToDistributor.this, ClockInOut.class));
                }
            }
        });
        builder.setNegativeButton("NO", (dialog, which) -> {
            dialog.dismiss();
            finish();
        });
        builder.show();
    }

    @SuppressLint("StaticFieldLeak")
    public class mGenerateDailyReport extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(PlaceOrderToDistributor.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "generatedailyreport"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("distributor_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("date", params[2]));
//            nameValuePair.add(new BasicNameValuePair("data", params[2]));
//            nameValuePair.add(new BasicNameValuePair("lat", params[3]));
//            nameValuePair.add(new BasicNameValuePair("long", params[4]));
//            nameValuePair.add(new BasicNameValuePair("address", params[5]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        //Log.e("Result JSON Object", "" + jsonObject.toString());
                        //  Toast.makeText(PlaceOrderToDistributor.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                        //                   Utils.showToast(PlaceOrderToDistributor.this, jsonObject.getString("message"));
                        // update dsr_status of attendance 1


                        if (attendanceReason != null && attendanceReason.equalsIgnoreCase("Institutional Work")) {
                            //if order send so status 1
                            //if reason then 2

                            //if both then also 2
                            ordersPresent = true;
                            if (previousDayDate != null) {
                                new checkReasonStatus().execute(mUserLoginInfoBean.getUserId(), previousDayDate);
                            } else {
                                new checkReasonStatus().execute(mUserLoginInfoBean.getUserId(), currentDate());
                            }
                            //if reason present then updatedsr status to 2 else 1

                        } else {
                            //new updateDsrStatus().execute(mUserLoginInfoBean.getUser_id(), "F", "1");

                            if (previousDayDate != null) {
                                new updateDsrStatusNoOrder().execute(mUserLoginInfoBean.getUserId(), "F", "P", previousDayDate, "1");
                            } else {
                                new updateDsrStatusNoOrder().execute(mUserLoginInfoBean.getUserId(), "F", "P", currentDate(), "1");
                            }

                        }

                    } else {
                        //check for institutional wheather reason for the user is available if yes then make status 2 else if no orders then Absent and 0
                        //but if both orders and reason available then 2

                        if (attendanceReason != null && attendanceReason.equalsIgnoreCase("Institutional Work")) {
                            ordersPresent = false;
                            if (previousDayDate != null) {
                                new checkReasonStatus().execute(mUserLoginInfoBean.getUserId(), previousDayDate);
                            } else {
                                new checkReasonStatus().execute(mUserLoginInfoBean.getUserId(), currentDate());

                            }
                        } else {
                            if (previousDayDate != null) {
                                new updateDsrStatusNoOrder().execute(mUserLoginInfoBean.getUserId(), "F", "A", previousDayDate, "0");
                            } else {
                                new updateDsrStatusNoOrder().execute(mUserLoginInfoBean.getUserId(), "F", "A", currentDate(), "0");
                            }
                        }
                        Utils.showToast(PlaceOrderToDistributor.this, jsonObject.getString("message"));

                        // Toast.makeText(PlaceOrderToDistributor.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertDialog(PlaceOrderToDistributor.this, "Daily Report", "Improper response from server", true);
                finish();
//              Toast.makeText(PlaceOrderToDistributor.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class checkReasonStatus extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(PlaceOrderToDistributor.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "sr_reason_status"));
            nameValuePair.add(new BasicNameValuePair("userId", params[0]));
            nameValuePair.add(new BasicNameValuePair("date", params[1]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        if (object.getInt("count") > 0) {
                            if (previousDayDate != null) {
                                new updateDsrStatusNoOrder().execute(mUserLoginInfoBean.getUserId(), "O", "PA", previousDayDate, "2");
                            } else {
                                new updateDsrStatusNoOrder().execute(mUserLoginInfoBean.getUserId(), "O", "PA", currentDate(), "2");
                            }
                        } else if (object.getInt("count") == 0) {
                            if (ordersPresent) {
                                if (previousDayDate != null) {
                                    new updateDsrStatusNoOrder().execute(mUserLoginInfoBean.getUserId(), "O", "PA", previousDayDate, "1");
                                } else {
                                    new updateDsrStatusNoOrder().execute(mUserLoginInfoBean.getUserId(), "O", "PA", currentDate(), "1");
                                }
                            } else {
                                if (previousDayDate != null) {
                                    new updateDsrStatusNoOrder().execute(mUserLoginInfoBean.getUserId(), "O", "A", previousDayDate, "0");
                                } else {
                                    new updateDsrStatusNoOrder().execute(mUserLoginInfoBean.getUserId(), "O", "A", currentDate(), "0");
                                }
                            }
                            //  Toast.makeText(PlaceOrderToDistributor.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {

                    Toast.makeText(PlaceOrderToDistributor.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            } else {
                Toast.makeText(PlaceOrderToDistributor.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class updateDsrStatusNoOrder extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(PlaceOrderToDistributor.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "update_dsr_status_no_order"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("attendance_type", params[1]));
            nameValuePair.add(new BasicNameValuePair("attendance", params[2]));
            nameValuePair.add(new BasicNameValuePair("date", params[3]));
            nameValuePair.add(new BasicNameValuePair("status", params[4]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        dsr_update = true;
                        SharedPreferences sharedpreferences = getSharedPreferences(Constant.Update_DSR_PREF, Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putBoolean(Constant.Update_DSR_OBJ, dsr_update);


                        editor.apply();

                        /*Intent resultIntent = new Intent();
                        resultIntent.putExtra("dsr_update", dsr_update);
                        setResult(Activity.RESULT_OK, resultIntent);*/
                        finish();
                    }
                    Toast.makeText(PlaceOrderToDistributor.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(PlaceOrderToDistributor.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(PlaceOrderToDistributor.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class mDistributorList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(PlaceOrderToDistributor.this);
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
//                        Toast.makeText(PlaceOrderToDistributor.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                        //                     Utils.showToast(PlaceOrderToDistributor.this, jsonObject.getString("message"));
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
                        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(PlaceOrderToDistributor.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
                        mDistributerListPref.putObject(Constant.DISTRIBUTOR_LIST_OBJ, mDistributerArraylist);
                        mDistributerListPref.commit();
                    } else {
                        Utils.showToast(PlaceOrderToDistributor.this, jsonObject.getString("message"));
//                        Toast.makeText(PlaceOrderToDistributor.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertDialog(PlaceOrderToDistributor.this, "Daily Report", "Improper response from server", true);
//                Toast.makeText(PlaceOrderToDistributor.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }

    }

    // Showing Alert Message

}
