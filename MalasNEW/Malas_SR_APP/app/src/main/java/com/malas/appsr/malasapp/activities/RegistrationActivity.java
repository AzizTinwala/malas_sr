package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.AsyncTask;
import com.Amitlibs.utils.ComplexPreferences;
import com.Amitlibs.utils.TextUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.ApiIntentService.RegistrationService;
import com.malas.appsr.malasapp.BeanClasses.CityListBean;
import com.malas.appsr.malasapp.BeanClasses.CountryListBean;
import com.malas.appsr.malasapp.BeanClasses.DistrictListBean;
import com.malas.appsr.malasapp.BeanClasses.StateListBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.CityAdapter;
import com.malas.appsr.malasapp.adapter.CountryAdapter;
import com.malas.appsr.malasapp.adapter.DistrictAdapter;
import com.malas.appsr.malasapp.adapter.StateAdapter;
import com.malas.appsr.malasapp.broadcastreciver.ApiIntentServiceBroadcastReciever;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity implements ApiIntentServiceBroadcastReciever.Receiver {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String PROPERTY_APP_VERSION = "appVersion";
    Bitmap bitmap;
    String convertImage;

    EditText edtUserName, edtPassword, edtEmail, edtAddress, edtcity, edtcountry, edtState, edtDistrict, edtmobile, edtdob, edtConfrmPassword, edtDateOfJoin;
    ImageView user_img;
    Button btnCancel, btnSubmit;
    ApiIntentServiceBroadcastReciever mReceiver;
    ProgressDialog mProgressDialog;
    EditText etserach;
    ArrayList<CityListBean> tempCitylist = new ArrayList<>();
    ArrayList<DistrictListBean> districtlist = new ArrayList<>();
    ArrayList<CountryListBean> countrylist = new ArrayList<>();
    ArrayList<StateListBean> statelist = new ArrayList<>();
    CityAdapter mCityAdapter;
    DistrictAdapter mDistrictAdapter;
    CountryAdapter mCountryAdapter;
    StateListBean selectedState;
    StateAdapter mStateAdapter;
    DistrictListBean selectDistrict;
    CountryListBean selectcountry;
    CityListBean selectcity;
    GoogleCloudMessaging gcm;
    String regid;
    AsyncTask<String, Void, String> mGcmRegistration;
    android.os.AsyncTask<String, Void, JSONObject> mgetAllCountryList;
    String SENDER_ID = "993206218242";

    /**
     * This method is used to generate App version
     *
     * @param context Context of Application
     * @return App Version
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isMyServiceRunning("com.example.ranneeti.services.RegistrationService")) {
            this.finish();

            if (mGcmRegistration != null && !mGcmRegistration.isCancelled())
                mGcmRegistration.cancel(true);


            if (mgetAllCountryList != null && !mgetAllCountryList.isCancelled())
                mgetAllCountryList.cancel(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);
        //  Objects.requireNonNull(getSupportActionBar()).setTitle("Registration Screen");
        edtUserName = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        edtEmail = findViewById(R.id.edt_email);
        edtAddress = findViewById(R.id.edt_address);
        edtmobile = findViewById(R.id.edt_mobile);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSubmit = findViewById(R.id.btn_submit);
        edtdob = findViewById(R.id.edt_dob);
        edtcountry = findViewById(R.id.edt_country);
        edtState = findViewById(R.id.edt_state);
        edtcity = findViewById(R.id.edt_City);
        edtDistrict = findViewById(R.id.edt_District);
        edtConfrmPassword = findViewById(R.id.edt_conform_password);
        edtDateOfJoin = findViewById(R.id.edt_date_of_joining);
        user_img = findViewById(R.id.user_img);


        /*ComplexPreferences mCityDistrictCountryComplexPreferences = ComplexPreferences.getComplexPreferences(RegistrationActivity.this, Constant.CITY_DISTIC_COUNTRY_PREF, MODE_PRIVATE);
        Type typeCity = new TypeToken<ArrayList<CityListBean>>() {
        }.getType();
        Type typeCountry = new TypeToken<ArrayList<CountryListBean>>() {
        }.getType();
        Type typeDistrict = new TypeToken<ArrayList<DistrictListBean>>() {
        }.getType();

        citylist = mCityDistrictCountryComplexPreferences.getArray(Constant.CITY_OBJ, typeCity);
        countrylist = mCityDistrictCountryComplexPreferences.getArray(Constant.COUNTRY_OBJ, typeCountry);
        districtlist = mCityDistrictCountryComplexPreferences.getArray(Constant.DISTIC_OBJ, typeDistrict);
        tempCitylist.addAll(citylist);*/
        mgetAllCountryList = new mGetAllCountryList().execute();


        edtcountry.setOnClickListener(v -> {
            if (countrylist != null) {
                final Dialog dialog = new Dialog(RegistrationActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                @SuppressLint("CutPasteId") final ListView listView_Counry = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                mCountryAdapter = new CountryAdapter(RegistrationActivity.this, countrylist);
                listView_Counry.setAdapter(mCountryAdapter);

                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String st = etserach.getText().toString();
                        if (!s.equals("") && s.length() > 0) {
                            mCountryAdapter.filter(st);
                        } else {
                            mCountryAdapter.filter(st);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView_Counry.setOnItemClickListener((parent, view, position, id) -> {
                    String countryname = CountryAdapter.resultArrayshort.get(position).getCountryName();
                    selectcountry = CountryAdapter.resultArrayshort.get(position);
                    edtcountry.setText(countryname);
                    //  citylist.clear();
                    dialog.dismiss();
                    new mGetStateList().execute(selectcountry.getCountryId());
                });
            }
        });

        edtState.setOnClickListener(v -> {
            if (countrylist != null) {
                final Dialog dialog = new Dialog(RegistrationActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView_Counry = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                mStateAdapter = new StateAdapter(RegistrationActivity.this, statelist);
                listView_Counry.setAdapter(mStateAdapter);

                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String st = etserach.getText().toString();
                        if (!s.equals("") && s.length() > 0) {
                            mStateAdapter.filter(st);
                        } else {
                            mStateAdapter.filter(st);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                listView_Counry.setOnItemClickListener((parent, view, position, id) -> {
                    String countryname = StateAdapter.resultArrayshort.get(position).getStateName();
                    selectedState = StateAdapter.resultArrayshort.get(position);
                    edtState.setText(countryname);
                    //  citylist.clear();
                    dialog.dismiss();
                    new mGetDistrictList().execute(selectedState.getStateId());
                });
            }

        });
        edtcity.setOnClickListener(v -> {

            if (tempCitylist != null) {
                final Dialog dialog = new Dialog(RegistrationActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView_City = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                mCityAdapter = new CityAdapter(RegistrationActivity.this, tempCitylist);
                listView_City.setAdapter(mCityAdapter);

                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String st = etserach.getText().toString();
                        if (!s.equals("") && s.length() > 0) {
                            mCityAdapter.filter(st);
                        } else {
                            mCityAdapter.filter(st);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView_City.setOnItemClickListener((parent, view, position, id) -> {
                    String countryname = CityAdapter.resultArrayshort.get(position).getCname();
                    selectcity = CityAdapter.resultArrayshort.get(position);
                    edtcity.setText(countryname);
                    //  citylist.clear();
                    dialog.dismiss();
                });
            }

        });

        edtDistrict.setOnClickListener(v -> {

            if (districtlist != null) {
                final Dialog dialog = new Dialog(RegistrationActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                @SuppressLint("CutPasteId") final ListView listView_states = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                mDistrictAdapter = new DistrictAdapter(RegistrationActivity.this, districtlist);
                listView_states.setAdapter(mDistrictAdapter);


                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        String st = etserach.getText().toString();
                        if (!s.equals("") && s.length() > 0) {
                            mDistrictAdapter.filter(st);
                        } else {
                            mDistrictAdapter.filter(st);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {


                    }
                });

                listView_states.setOnItemClickListener((parent, view, position, id) -> {
                    String countryname = DistrictAdapter.resultArrayshort.get(position).getDistrictName();
                    selectDistrict = DistrictAdapter.resultArrayshort.get(position);
                    edtDistrict.setText(countryname);
                    /*tempCitylist.clear();
                    for (int i = 0; i < citylist.size(); i++) {
                        if (citylist.get(i).getDistrict_id().equalsIgnoreCase(selectDistrict.getDistrictId())) {
                            tempCitylist.add(citylist.get(i));
                        }
                    }*/
                    dialog.dismiss();
                    new mGetCityList().execute(selectDistrict.getDistrictId());
                });
            }

        });

        edtdob.setOnClickListener(v -> {
            final Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

                private void updateLabel() {
                    String myFormat = "dd-MM-yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    edtdob.setText(sdf.format(myCalendar.getTime()));
                }
            };

            DatePickerDialog mDatePickerDialog = new DatePickerDialog(RegistrationActivity.this, R.style.DialogTheme, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePickerDialog.show();
        });

        edtDateOfJoin.setOnClickListener(v -> {
            final Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

                private void updateLabel() {
                    String myFormat = "dd-MM-yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                    edtDateOfJoin.setText(sdf.format(myCalendar.getTime()));
                }
            };

            DatePickerDialog mDatePickerDialog = new DatePickerDialog(RegistrationActivity.this, R.style.DialogTheme, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePickerDialog.show();
        });

        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alt = new AlertDialog.Builder(RegistrationActivity.this);

                View dv = getLayoutInflater().inflate(R.layout.camera_dailog, null);
                Button cam = dv.findViewById(R.id.dailog_camera);
                Button gallery = dv.findViewById(R.id.dailog_gallery);
                // gallery.isEnabled = false
                alt.setView(dv);
                Dialog dialog = alt.create();
                dialog.show();

                cam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);
                        dialog.dismiss();
                    }
                });


                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(
                                new Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                ), 1
                        );
                        dialog.dismiss();
                    }
                });

            }
        });


        btnSubmit.setOnClickListener(v ->

        {

            if (edtUserName.getText().toString().isEmpty()) {
                edtUserName.setError("Enter user name");
            } else if (edtPassword.getText().toString().isEmpty()) {
                edtPassword.setError("Enter password");
            } else if (edtConfrmPassword.getText().toString().isEmpty()) {
                edtConfrmPassword.setError("Conform password");
            } else if (edtPassword.getText().toString().equals(edtConfrmPassword.getText().toString().isEmpty())) {
                edtPassword.setError("Password and confirm password should be same");
                edtConfrmPassword.setError("Password and confirm password should be same");
            } else if (!TextUtils.isValidEmail(edtEmail.getText().toString().trim())) {
                edtEmail.setError("Enter valid email");
            } else if (edtcountry.getText().toString().isEmpty()) {
                edtcountry.setError("Select country");
            } else if (edtDistrict.getText().toString().isEmpty()) {
                edtDistrict.setError("Select district");
            } else if (edtcity.getText().toString().isEmpty()) {
                edtcity.setError("Select city");
            } else if (edtAddress.getText().toString().isEmpty()) {
                edtAddress.setError("Enter address");
            } else if (!TextUtils.isValidMobileNo(edtmobile.getText().toString().trim())) {
                edtmobile.setError("Enter valid mobile no");
            } else if (edtdob.getText().toString().isEmpty()) {
                edtdob.setError("Enter DOB");
            } else if (edtDateOfJoin.getText().toString().isEmpty()) {
                edtDateOfJoin.setError("Enter date of joining");
            } else {

                mReceiver = new ApiIntentServiceBroadcastReciever(new Handler());
                mReceiver.setReceiver(RegistrationActivity.this);
                String name = edtUserName.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String confrmPassword = edtConfrmPassword.getText().toString();
                String country_id = selectcountry.getCountryId();
                String stateId = selectedState.getStateId();
                String district_id = selectDistrict.getDistrictId();
                String city_id = selectcity.getCid();
                String address = edtAddress.getText().toString();
                String mobile = edtmobile.getText().toString();
                String dob = edtdob.getText().toString();
                String dateOfJoin =Utils.formatDatetoSubmit(edtDateOfJoin.getText().toString());
                /* Send optional extras to Download IntentService */

                if (checkPlayServices()) {

                    gcm = GoogleCloudMessaging.getInstance(RegistrationActivity.this);
                    regid = getRegistrationId();
                    System.out.println("REG: " + regid);
                    if (regid.isEmpty()) {
                        if (Utils.isInternetConnected(RegistrationActivity.this)) {
                            mGcmRegistration = new gcmRegistrationLogin().execute("user_registration", name, email, password, country_id, district_id, city_id, address, mobile, dob, confrmPassword, dateOfJoin, stateId, convertImage);
                        } else
                            Utils.showAlertDialog(RegistrationActivity.this, "Alert", "Internet Connection Unavailable", null);
                    } else {
                        if (Utils.isInternetConnected(RegistrationActivity.this)) {
                            if (!isMyServiceRunning("com.malas.krishna.malasapp.ApiIntentService.RegistrationService")) {
                                showProgressDialog();
                                Intent intent = new Intent(Intent.ACTION_SYNC, null, RegistrationActivity.this, RegistrationService.class);
                                intent.putExtra("method", "user_registration");
                                intent.putExtra("name", name);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                intent.putExtra("country_id", country_id);
                                intent.putExtra("district_id", district_id);
                                intent.putExtra("city_id", city_id);
                                intent.putExtra("address", address);
                                intent.putExtra("mobile", mobile);
                                intent.putExtra("dob", dob);
                                intent.putExtra("gcmid_uid", regid);
                                intent.putExtra("designation_id", "5");
                                intent.putExtra("devicetype", "0");
                                intent.putExtra("receiver", mReceiver);
                                intent.putExtra("confrmPassword", confrmPassword);
                                intent.putExtra("dateOfJoin", dateOfJoin);
                                intent.putExtra("state_id", stateId);
                                startService(intent);
                            } else {
                                System.out.println("Service is already running");
                            }
                        } else
                            Utils.showAlertDialog(RegistrationActivity.this, "Alert", "Internet Connection Unavailable", null);
                    }
                }
            }
        });

        btnCancel.setOnClickListener(v ->

                finish());
    }

    /**
     * This method is used to store registration Id in Complex preferences.
     *
     * @param context Context of class where method is call.
     * @param regId   String value which is used in complex preference.
     */
    private void storeRegistrationId(Context context, String regId) {
        ComplexPreferences mComplexPreferences = ComplexPreferences.getComplexPreferences(RegistrationActivity.this, Constant.USER_GOOGLE_GCM_REG_INFO,
                MODE_PRIVATE);
        mComplexPreferences.putObject(Constant.USER_Google_GCM_ID, regId);
        mComplexPreferences.putObject(PROPERTY_APP_VERSION, getAppVersion(context));
        mComplexPreferences.commit();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(RegistrationActivity.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("Regisration", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId() {
        ComplexPreferences mComplexPreferences = ComplexPreferences.getComplexPreferences(RegistrationActivity.this, Constant.USER_GOOGLE_GCM_REG_INFO,
                MODE_PRIVATE);
        String registrationId = mComplexPreferences.getObject(Constant.USER_Google_GCM_ID, String.class);
        if (TextUtils.isNullOrEmpty(registrationId)) {
            Log.i("RegistrationActivity", "Registration not found.");
            return "";
        }
        int registeredVersion = mComplexPreferences.getObject(PROPERTY_APP_VERSION, Integer.class);
        int currentVersion = getAppVersion(this);
        if (registeredVersion != currentVersion) {
            Log.i("RegistrationActivity", "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @param serviceName = com.example.MyService
     */
    private boolean isMyServiceRunning(String serviceName) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case RegistrationService.STATUS_RUNNING:
                setProgressBarIndeterminateVisibility(true);
                showProgressDialog();
                break;
            case RegistrationService.STATUS_FINISHED:
                setProgressBarIndeterminateVisibility(false);
                hideProgressDialog();
                String response = resultData.getString(Intent.EXTRA_TEXT);
                hideProgressDialog_ShowErrorMessage(response, true);

                break;
            case RegistrationService.STATUS_ERROR:
                String error = resultData.getString(Intent.EXTRA_TEXT);
                hideProgressDialog_ShowErrorMessage(error, true);
                break;
        }
    }

    private void showProgressDialog() {
        mProgressDialog = getProgressDialog();
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage("Registering please wait....");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (!mProgressDialog.isShowing()) mProgressDialog.dismiss();
    }

    private void hideProgressDialog_ShowErrorMessage(String errorMessage, final boolean shouldFinishActivity) {
        if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
        AlertDialog.Builder alert = new AlertDialog.Builder(RegistrationActivity.this);
        alert.setTitle("Alert!");
        alert.setMessage(errorMessage);
        alert.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            if (shouldFinishActivity) RegistrationActivity.this.finish();
        });
        alert.show();
    }

    private ProgressDialog getProgressDialog() {
        return mProgressDialog == null ? new ProgressDialog(RegistrationActivity.this) : mProgressDialog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            try {
                if (requestCode == 0) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    //data.extras ?.get("data") as Bitmap
                } else {
                    Uri contentURI = data.getData();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        ImageDecoder.Source source = ImageDecoder.createSource(
                                getContentResolver(),
                                contentURI
                        );
                        bitmap = ImageDecoder.decodeBitmap(source);
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    }
                }
                user_img.setImageBitmap(bitmap);
                imageUploadToServerFunction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Upload captured image online on server function.
    void imageUploadToServerFunction() {
        ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();

        // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        convertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
    }


    @SuppressLint("StaticFieldLeak")
    public class mGetAllCountryList extends android.os.AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(RegistrationActivity.this);
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
            /*nameValuePair.add(new BasicNameValuePair("routename", params[0]));
            nameValuePair.add(new BasicNameValuePair("distributor_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("user_id", params[2]));*/

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        System.out.print(jsonObject.getString("message"));
                        JSONArray countryArray = jsonObject.getJSONArray("countrylist");
                        countrylist = new ArrayList<>();
                        for (int l = 0; l < countryArray.length(); l++) {
                            JSONObject mJsonObjInfoCountryt = countryArray.getJSONObject(l);
                            String id = mJsonObjInfoCountryt.getString("id");
                            String country_name = mJsonObjInfoCountryt.getString("country_name");
                            countrylist.add(new CountryListBean(id, country_name));
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(RegistrationActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mGetStateList extends android.os.AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(RegistrationActivity.this);
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
                        System.out.print(jsonObject.getString("message"));
                        JSONArray countryArray = jsonObject.getJSONArray("statelist");
                        statelist = new ArrayList<>();
                        for (int l = 0; l < countryArray.length(); l++) {
                            JSONObject mJsonObjInfoCountryt = countryArray.getJSONObject(l);
                            String id = mJsonObjInfoCountryt.getString("state_id");
                            String countryId = mJsonObjInfoCountryt.getString("country_id");
                            String stateName = mJsonObjInfoCountryt.getString("state_name");
                            statelist.add(new StateListBean(id, stateName, countryId));
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(RegistrationActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mGetDistrictList extends android.os.AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(RegistrationActivity.this);
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
                        System.out.print(jsonObject.getString("message"));
                        JSONArray countryArray = jsonObject.getJSONArray("districlist");
                        districtlist = new ArrayList<>();
                        for (int l = 0; l < countryArray.length(); l++) {
                            JSONObject mJsonObjInfoCountryt = countryArray.getJSONObject(l);
                            String id = mJsonObjInfoCountryt.getString("id");
                            String StateId = mJsonObjInfoCountryt.getString("state_id");
                            String stateName = mJsonObjInfoCountryt.getString("dis_name");
                            districtlist.add(new DistrictListBean(id, stateName, StateId));
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(RegistrationActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mGetCityList extends android.os.AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(RegistrationActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Getting City list please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
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
                        System.out.print(jsonObject.getString("message"));
                        JSONArray countryArray = jsonObject.getJSONArray("citylist");
                        tempCitylist = new ArrayList<>();
                        for (int l = 0; l < countryArray.length(); l++) {
                            JSONObject mJsonObjInfoCountryt = countryArray.getJSONObject(l);
                            String city_id = mJsonObjInfoCountryt.getString("cid");
                            String stateId = mJsonObjInfoCountryt.getString("state_id");
                            String districtId = mJsonObjInfoCountryt.getString("district_id");
                            String cityName = mJsonObjInfoCountryt.getString("cname");
                            tempCitylist.add(new CityListBean(districtId, city_id, cityName));
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(RegistrationActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    private class gcmRegistrationLogin extends AsyncTask<String, Void, String> {
        String name = "";
        String email = "";
        String password = "";
        String country_id = "";
        String district_id = "";
        String city_id = "";
        String address = "";
        String mobile = "";
        String dob = "";
        String confrmPassword = "";
        String dateOfJoin = "";
        String stateId = "";
        String img = "";

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(RegistrationActivity.this);
            mProgressDialog.setMessage("Generating GCM ID");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String msg = "fail";
            try {
//                if (gcm == null) {
//                    gcm = GoogleCloudMessaging.getInstance(RegistrationActivity.this);
//                }
//                regid = gcm.register(SENDER_ID);
                regid = "asasasdsadas";
                System.out.println("REG: " + regid);
                storeRegistrationId(RegistrationActivity.this, regid);
                /*"user_registration", name, email, password, country_id, district_id, city_id, address, mobile, dob, confrmPassword, dateOfJoin*/
                name = params[1];
                email = params[2];
                password = params[3];
                country_id = params[4];
                district_id = params[5];
                city_id = params[6];
                address = params[7];
                mobile = params[8];
                dob = params[9];
                confrmPassword = params[10];
                dateOfJoin = params[11];
                stateId = params[12];
                img = params[12];

                msg = "success";
            } catch (Exception ex) {
                Log.e("RegistrationActivity", Objects.requireNonNull(ex.getMessage()));
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            mProgressDialog.dismiss();
            if (msg == "success") {
                if (Utils.isInternetConnected(RegistrationActivity.this)) {
                    if (!isMyServiceRunning("com.malas.krishna.malasapp.ApiIntentService.RegistrationService")) {
                        showProgressDialog();
                        Intent intent = new Intent(Intent.ACTION_SYNC, null, RegistrationActivity.this, RegistrationService.class);
                        intent.putExtra("method", "user_registration");
                        intent.putExtra("name", name);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        intent.putExtra("country_id", country_id);
                        intent.putExtra("district_id", district_id);
                        intent.putExtra("city_id", city_id);
                        intent.putExtra("address", address);
                        intent.putExtra("mobile", mobile);
                        intent.putExtra("dob", dob);
                        intent.putExtra("gcmid_uid", regid);
                        intent.putExtra("designation_id", "5");
                        intent.putExtra("devicetype", "0");
                        intent.putExtra("dateOfJoin", dateOfJoin);
                        intent.putExtra("state_id", stateId);
                        intent.putExtra("receiver", mReceiver);
                        startService(intent);
                    } else {
                        System.out.println("Service is already running");
                    }
                } else
                    Utils.showAlertDialog(RegistrationActivity.this, "Alert", "Internet Connection Unavailable", null);
            }
        }
    }
}