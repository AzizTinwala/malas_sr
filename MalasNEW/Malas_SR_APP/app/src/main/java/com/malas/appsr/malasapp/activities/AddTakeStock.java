package com.malas.appsr.malasapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.google.gson.reflect.TypeToken;
import com.malas.appsr.malasapp.BeanClasses.TakeStockItemBean;
import com.malas.appsr.malasapp.BeanClasses.TakeStoclListBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.TakeStockCategoryAdapter;
import com.malas.appsr.malasapp.adapter.TakeStockItemListAdapter;
import com.malas.appsr.malasapp.customeArrayList.CArrayListTakeStockEditedItemProductList;
import com.malas.appsr.malasapp.dbHandler.DatabaseHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/*import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;*/

/*import android.util.Log;*/

public class AddTakeStock extends AppCompatActivity implements TakeStockCategoryAdapter.categoryClickListner {
    String jsonStringOnSave;
    ArrayList<TakeStoclListBean> itemList;
    String disIdsved;
    ArrayList<TakeStoclListBean> listDataHeader;
    AsyncTask<String, Void, JSONObject> saveDataAsyn;
    TextView mdistributerName;
    RecyclerView lvCategoryList, lvItemList;
    String distributerId;
    ImageView mSaveButton;
    String mUserid, firmName = "";
    double latitude = 0;
    double longitude = 0;
    String addressStr = "";
    LocationManager locationManager;
    LocationListener locationListener;
    DatabaseHandler db;
    //PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    int otp;
    String OtpUserEntered;
    Dialog dialogOTP;
    TakeStockCategoryAdapter mTakeStockCategoryAdapter;
    TakeStockItemListAdapter mTakeStockItemListAdapter;
    SearchView searchView;
    private String distributorMobileNumber;
    //private FirebaseAuth mAuth;
    private String mVerificationId;
    JSONObject mJsonObject;
    JSONArray mJsonArray;

    ArrayList<String> summation;
    public static int generatePin() throws Exception {
        Random generator = new Random();
        generator.setSeed(System.currentTimeMillis());

        int num = generator.nextInt(99999) + 99999;
        if (num < 100000 || num > 999999) {
            num = generator.nextInt(99999) + 99999;
            if (num < 100000 || num > 999999) {
                throw new Exception("Unable to generate PIN at this time..");
            }
        }
        return num;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_take_stock);

        lvCategoryList = findViewById(R.id.category_list);
        lvItemList = findViewById(R.id.item_list);
        mSaveButton = findViewById(R.id.iv_save_button);
        mdistributerName = findViewById(R.id.distributr_name);

        lvCategoryList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);

        lvCategoryList.addItemDecoration(dividerItemDecoration);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(lvCategoryList);

        lvItemList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true));
        DividerItemDecoration itemdividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        lvItemList.addItemDecoration(itemdividerItemDecoration);

        SnapHelper itemsnapHelper = new LinearSnapHelper();
        itemsnapHelper.attachToRecyclerView(lvItemList);

        db = new DatabaseHandler(this);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Stock");

        getCurrentLocation();

        if (getIntent().hasExtra("firmName")) {
            firmName = getIntent().getStringExtra("firmName").toUpperCase(Locale.getDefault());
        }

        mdistributerName.setText(firmName);
        distributerId = getIntent().getStringExtra("distribbuterId");
        distributorMobileNumber = getIntent().getStringExtra("distributornumber");


       /* lvCategoryList.setOnItemClickListener((parent, view, position, arg3) -> {
            view.setSelected(true);
            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddTakeStock.this, Constant.ProductListPref, MODE_PRIVATE);
            Type typeOutlet = new TypeToken<ArrayList<TakeStoclListBean>>() {
            }.getType();

            if (itemList != null)
                itemList.clear();
            itemList = complexPreferences.getArray(Constant.ProductListStockObj, typeOutlet) == null ? new ArrayList<>() : (ArrayList<TakeStoclListBean>) complexPreferences.getArray(Constant.ProductListStockObj, typeOutlet);
            disIdsved = complexPreferences.getObject(Constant.distributor_id, String.class);
            if (itemList != null && itemList.size() > 0) {
                TakeStockItemListAdapter mTakeStockItemListAdapter = new TakeStockItemListAdapter(AddTakeStock.this, itemList.get(position), position, "AddStock");
                lvItemList.setAdapter(mTakeStockItemListAdapter);
            }
        });
*/
        mSaveButton.setOnClickListener(v -> {
            ComplexPreferences mComplexPreferences1 = ComplexPreferences.getComplexPreferences(AddTakeStock.this, Constant.EditedProductListPref, MODE_PRIVATE);
            CArrayListTakeStockEditedItemProductList updatedTakeStockItem1 = mComplexPreferences1.getArray(Constant.EditedProductListObj, CArrayListTakeStockEditedItemProductList.class);
            try {
                 mJsonObject = new JSONObject();
                 mJsonArray = new JSONArray();
                summation = new ArrayList<>();
                if (updatedTakeStockItem1 != null) {
                    if (updatedTakeStockItem1.size() > 0) {

                        for (int i = 0; i < updatedTakeStockItem1.size(); i++) {
                            if (updatedTakeStockItem1.get(i).getItem_qty() != null && !updatedTakeStockItem1.get(i).getItem_qty().equals("0")) {
                                JSONObject mTempObj = new JSONObject();
                                mTempObj.put("item_id", updatedTakeStockItem1.get(i).getItem_id());
                                mTempObj.put("item_name", updatedTakeStockItem1.get(i).getItem_name());
                                mTempObj.put("item_qty", updatedTakeStockItem1.get(i).getItem_qty());
                                mTempObj.put("case_size", updatedTakeStockItem1.get(i).getItem_case_size());
                                int r = Math.abs(Integer.parseInt(updatedTakeStockItem1.get(i).getItem_qty())) % Integer.parseInt(updatedTakeStockItem1.get(i).getItem_case_size());
                                int q = Math.abs(Integer.parseInt(updatedTakeStockItem1.get(i).getItem_qty())) / Integer.parseInt(updatedTakeStockItem1.get(i).getItem_case_size());
                                int halfcase = Integer.parseInt(updatedTakeStockItem1.get(i).getItem_case_size()) / 2;

                                Log.d("Item_ID", updatedTakeStockItem1.get(i).getItem_id());
                                Log.d("Item_QTY", updatedTakeStockItem1.get(i).getItem_qty());
                                Log.d("Case Size", updatedTakeStockItem1.get(i).getItem_case_size());
                                Log.d("Quotient", "" + q);
                                Log.d("Remainder", "" + r);

                                System.out.println("nsc Quotient : " + q);
                                System.out.println("nsc Remainder : " + r);
                                if (r >= halfcase) {
                                    q = q + 1;
                                }
                                summation.add("" + q);

                                mJsonArray.put(mTempObj);
                            }

                        }
                        mJsonObject.put("item_list", mJsonArray);
                        System.out.print(mJsonObject);
                        System.out.print(updatedTakeStockItem1);

                        Intent submitconfirmation = new Intent(getApplicationContext(), ConfirmStocks.class);
                        submitconfirmation.putExtra("ItemList", mJsonObject.toString());
                        startActivityForResult(submitconfirmation,111);

/*
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddTakeStock.this, Constant.UserRegInfoPref, MODE_PRIVATE);
                        UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

                        mUserid = mUserLoginInfoBean.getUser_id();

                        if (mJsonArray.length() > 0) {
                            ///OTP HERE
                            // if date is 27 or greater then that of the month display  otpGeneration dialog else save data
                            if (dateOfTheMonth()) {
                                //dialogOtpByFirebase(mJsonObject);
                                jsonStringOnSave = mJsonObject.toString();
                                otp = generatePin();
                                int boxesQuantity = 0;
                                if (summation.size() > 0) {
                                    for (int i = 0; i < summation.size(); i++) {
                                        boxesQuantity = boxesQuantity + Integer.parseInt(summation.get(i));
                                    }
                                }
                                //Dear <<Distributor Name>>, your stock verification code is 123456  for the <<24 boxes>> taken by <<SrName>> on the date <<21/08/2019>>
                                //Your Stock Verification Code for 3 Boxes stock taken by Furqan on 22/8/2019 is
                                int stockBoxes = boxesQuantity;
                                String Message = "Dear " + firmName + ",\n     Your Stock Verification Code for " + stockBoxes + " boxes stock taken by " + mUserLoginInfoBean.getUser_name() + " on " + currentDate() + " is " + otp + "\nMala's Fruit Products";
                                //new otpGeneration().execute(distributorMobileNumber, Message);
                                new saveOtpOnServer().execute(String.valueOf(otp), distributorMobileNumber, Message);

                            } else {
                                saveDataAsyn = new mSaveData().execute(distributerId, mUserid, mJsonObject.toString(), "" + latitude, "" + longitude, addressStr);
                            }

                        } else {
                            Toast.makeText(AddTakeStock.this, "Please fill appropriate data", Toast.LENGTH_SHORT).show();
                        }*/
                    } else {
                        Toast.makeText(AddTakeStock.this, "Please enter quantity for at least on item", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddTakeStock.this, "Please enter quantity for at least on item", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        if (itemList != null)
            prepareListData(itemList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111) {
            if (resultCode == Activity.RESULT_OK) {
                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddTakeStock.this, Constant.UserRegInfoPref, MODE_PRIVATE);
                UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

                mUserid = mUserLoginInfoBean.getUserId();

                if (mJsonArray.length() > 0) {
                    ///OTP HERE
                    // if date is 27 or greater then that of the month display  otpGeneration dialog else save data
                    if (dateOfTheMonth()) {
                        //dialogOtpByFirebase(mJsonObject);
                        jsonStringOnSave = mJsonObject.toString();
                        try {
                            otp = generatePin();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        int boxesQuantity = 0;
                        if (summation.size() > 0) {
                            for (int i = 0; i < summation.size(); i++) {
                                boxesQuantity = boxesQuantity + Integer.parseInt(summation.get(i));
                            }
                        }
                        //Dear <<Distributor Name>>, your stock verification code is 123456  for the <<24 boxes>> taken by <<SrName>> on the date <<21/08/2019>>
                        //Your Stock Verification Code for 3 Boxes stock taken by Furqan on 22/8/2019 is
                        int stockBoxes = boxesQuantity;
                        String Message = "Dear " + firmName + ",\n     Your Stock Verification Code for " + stockBoxes + " boxes stock taken by " + mUserLoginInfoBean.getUserName() + " on " + currentDate() + " is " + otp + "\nMala's Fruit Products";
                        //new otpGeneration().execute(distributorMobileNumber, Message);
                        new saveOtpOnServer().execute(String.valueOf(otp), distributorMobileNumber, Message);

                    } else {
                        saveDataAsyn = new mSaveData().execute(distributerId, mUserid, mJsonObject.toString(), "" + latitude, "" + longitude, addressStr);
                    }

                } else {
                    Toast.makeText(AddTakeStock.this, "Please fill appropriate data", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
/* private void dialogOtpByFirebase(JSONObject mJsonObject) {

        final Dialog dialog = new Dialog(AddTakeStock.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.otp_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        final TextView tvDistributorNumber = dialog.findViewById(R.id.tvDistributorNumber);
        EditText edtOtp = dialog.findViewById(R.id.otp);
        Button btnConfirmOtp = dialog.findViewById(R.id.btnConfirmOTP);
        Button btnCancelotp = dialog.findViewById(R.id.btnCancelOTP);
        tvDistributorNumber.setText("Distributor Number " + distributorMobileNumber);
        dialog.show();
        //mAuth = FirebaseAuth.getInstance();

        btnConfirmOtp.setOnClickListener(v -> {
            String code = edtOtp.getText().toString().trim();
            if (code.isEmpty() || code.length() < 6) {
                edtOtp.setError("Enter valid code");
                edtOtp.requestFocus();
                return;
            }

            //verifying the code entered manually
            verifyVerificationCode(code, mJsonObject);
            // saveDataAsyn = new mSaveData().execute(distributerId, mUserid, mJsonObject.toString(), "" + latitude, "" + longitude, addressStr);
            dialog.dismiss();
        });
        btnCancelotp.setOnClickListener(v -> {
            dialog.dismiss();

        });
*/
       /* mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                //Getting the code sent by SMS
                String code = phoneAuthCredential.getSmsCode();

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    edtOtp.setText(code);
                    //verifying the code
                    verifyVerificationCode(code, mJsonObject);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(AddTakeStock.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                // Toast.makeText(AddTakeStock.this, s + "verification code", Toast.LENGTH_LONG).show();
                //storing the verification id that is sent to the user
                mVerificationId = s;
            }
        };
        sendVerificationCode(distributorMobileNumber);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, JSONObject mJsonObject) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(AddTakeStock.this, task -> {
                    if (task.isSuccessful()) {
                        //verification successful we will start the profile activity
                      *//*  Intent intent = new Intent(VerifyOtp.this, ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*//*
                        Toast.makeText(AddTakeStock.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                        saveDataAsyn = new mSaveData().execute(distributerId, mUserid, mJsonObject.toString(), "" + latitude, "" + longitude, addressStr);


                    } else {

                        //verification unsuccessful.. display an error message

                        String message;
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered";
                        } else {
                            message = "Something is wrong, we will fix it soon...";

                        }
                        Toast.makeText(AddTakeStock.this, message, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void verifyVerificationCode(String code, JSONObject mJsonObject) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential, mJsonObject);
    }

    private void sendVerificationCode(String no) {


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + no,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }*/

    @Override
    protected void onResume() {
        super.onResume();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddTakeStock.this, Constant.ProductListPref, MODE_PRIVATE);
        Type typeOutlet = new TypeToken<ArrayList<TakeStoclListBean>>() {
        }.getType();
        itemList = complexPreferences.getArray(Constant.ProductListStockObj, typeOutlet) == null ? new ArrayList<>() : (ArrayList<TakeStoclListBean>) complexPreferences.getArray(Constant.ProductListStockObj, typeOutlet);
        disIdsved = complexPreferences.getObject(Constant.distributor_id, String.class);


        ComplexPreferences mComplexPreferences = ComplexPreferences.getComplexPreferences(AddTakeStock.this, Constant.EditedProductListPref, MODE_PRIVATE);
        CArrayListTakeStockEditedItemProductList updatedTakeStockItem = mComplexPreferences.getArray(Constant.EditedProductListObj, CArrayListTakeStockEditedItemProductList.class);
        if (itemList != null) {
            if (disIdsved != null) {
                if (disIdsved.equalsIgnoreCase(distributerId)) {
                    prepareListData(itemList);
                } else {
                    if (updatedTakeStockItem != null) {
                        mComplexPreferences.clear(Constant.EditedProductListPref);
                    }
                    new mgetItemList().execute();
                }
            } else {
                if (updatedTakeStockItem != null) {
                    mComplexPreferences.clear(Constant.EditedProductListPref);
                }
                new mgetItemList().execute();

            }

        } else {
            if (updatedTakeStockItem != null) {
                mComplexPreferences.clear(Constant.EditedProductListPref);
            }
            new mgetItemList().execute();
        }
    }

    public ArrayList<TakeStoclListBean> getProductList() {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddTakeStock.this, Constant.ProductListPref, MODE_PRIVATE);
        Type typeOutlet = new TypeToken<ArrayList<TakeStoclListBean>>() {
        }.getType();
        itemList = complexPreferences.getArray(Constant.ProductListStockObj, typeOutlet) == null ? new ArrayList<>() : (ArrayList<TakeStoclListBean>) complexPreferences.getArray(Constant.ProductListStockObj, typeOutlet);
        disIdsved = complexPreferences.getObject(Constant.distributor_id, String.class);
        return itemList;
    }

    public void saveProductList(ArrayList<TakeStoclListBean> itemList) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddTakeStock.this, Constant.ProductListPref, MODE_PRIVATE);
        complexPreferences.putObject(Constant.ProductListStockObj, itemList);
        complexPreferences.putObject(Constant.distributor_id, distributerId);
        complexPreferences.commit();
    }

    private void prepareListData(ArrayList<TakeStoclListBean> itemList) {
        if (itemList != null) {
            listDataHeader = new ArrayList<>();
            listDataHeader.addAll(itemList);
            mTakeStockCategoryAdapter = new TakeStockCategoryAdapter(AddTakeStock.this, listDataHeader, this);
            lvCategoryList.setAdapter(mTakeStockCategoryAdapter);

            //  lvCategoryList.setSelection(0);
            lvCategoryList.setSelected(true);

            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddTakeStock.this, Constant.ProductListPref, MODE_PRIVATE);
            Type typeOutlet = new TypeToken<ArrayList<TakeStoclListBean>>() {
            }.getType();
            itemList = complexPreferences.getArray(Constant.ProductListStockObj, typeOutlet) == null ? new ArrayList<>() : (ArrayList<TakeStoclListBean>) complexPreferences.getArray(Constant.ProductListStockObj, typeOutlet);
            disIdsved = complexPreferences.getObject(Constant.distributor_id, String.class);
            if (itemList != null && itemList.size() > 0) {
                mTakeStockItemListAdapter = new TakeStockItemListAdapter(AddTakeStock.this, itemList.get(0), 0, "AddStock");
                lvItemList.setAdapter(mTakeStockItemListAdapter);
            }
        }
        /*
        if (callApi)

            new mgetItemList().execute();

        */


    }
/*
    public void stockAddOrUpdate(String addOrUpdate) {
        //stock taken means 0
        //order placed means 1
        if (addOrUpdate.equals("add")) {
            StockPlaced stockPlaced = new StockPlaced(distributerId, mUserid, "0", "1");

            db.addisorderplaced_stock(stockPlaced);
        } else if (addOrUpdate.equals("update")) {
            StockPlaced stockPlaced = new StockPlaced(distributerId, mUserid, "0", "1");
            db.updateIsOrderStockPlaced(stockPlaced);

        }
    }*/

    /**
     * Get Current Location Of User
     */
    public void getCurrentLocation() {

        boolean result = Utils.checkPermissions(AddTakeStock.this, new String[]{
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
              /*      Log.e("Your Location is", "" + lat + "--" + lon);
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

    /**
     * Convert Lat-Lng In address
     */

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

        //Log.e("address", "" + addressStr);
    }

    /**
     * Check if date is Month Closing Dates
     */

    public boolean dateOfTheMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (maxDay == 31) {
            DateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
            Date date = new Date();
            String dateOfTheMonth = dateFormat.format(date);
            int dateMonth = Integer.parseInt(dateOfTheMonth);
            return dateMonth >= 29;
        } else if (maxDay == 30) {
            DateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
            Date date = new Date();
            String dateOfTheMonth = dateFormat.format(date);
            int dateMonth = Integer.parseInt(dateOfTheMonth);
            return dateMonth >= 28;
        } else if (maxDay < 30) {
            DateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
            Date date = new Date();
            String dateOfTheMonth = dateFormat.format(date);
            int dateMonth = Integer.parseInt(dateOfTheMonth);
            return dateMonth >= 26;
        }
        return false;
    }

    /**
     * Otp Dialog box
     */
    private void dialogOtplink() {

        dialogOTP = new Dialog(AddTakeStock.this);
        dialogOTP.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOTP.setContentView(R.layout.otp_dialog);
        dialogOTP.setCanceledOnTouchOutside(false);
        dialogOTP.setCancelable(false);

        final TextView tvDistributorNumber = dialogOTP.findViewById(R.id.tvDistributorNumber);
        EditText edtOtp = dialogOTP.findViewById(R.id.otp);
        Button btnConfirmOtp = dialogOTP.findViewById(R.id.btnConfirmOTP);
        Button btnCancelOtp = dialogOTP.findViewById(R.id.btnCancelOTP);
        tvDistributorNumber.setText("Distributor Number " + distributorMobileNumber);
        dialogOTP.show();


        btnConfirmOtp.setOnClickListener(v -> {
            String code = edtOtp.getText().toString().trim();
            if (code.isEmpty() || code.length() < 6) {
                edtOtp.setError("Enter valid code");
                edtOtp.requestFocus();
            } else {
                OtpUserEntered = edtOtp.getText().toString().trim();
                new getOtp().execute();
            }

        });
        btnCancelOtp.setOnClickListener(v -> {
            dialogOTP.dismiss();

        });


    }

    public String currentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        return df.format(c);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void OnCategoryClick(int position) {
        mTakeStockItemListAdapter.getFilter().filter("");
        searchView.setQuery("", false); // clear the text
        Objects.requireNonNull(getSupportActionBar()).collapseActionView();
        // searchView.setIconified(true); // close the search editor and make search icon again
        //view.setSelected(true);
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddTakeStock.this, Constant.ProductListPref, MODE_PRIVATE);
        Type typeOutlet = new TypeToken<ArrayList<TakeStoclListBean>>() {
        }.getType();

        if (itemList != null)
            itemList.clear();
        itemList = complexPreferences.getArray(Constant.ProductListStockObj, typeOutlet) == null ? new ArrayList<>() : (ArrayList<TakeStoclListBean>) complexPreferences.getArray(Constant.ProductListStockObj, typeOutlet);
        disIdsved = complexPreferences.getObject(Constant.distributor_id, String.class);
        if (itemList != null && itemList.size() > 0) {
            mTakeStockItemListAdapter = new TakeStockItemListAdapter(AddTakeStock.this, itemList.get(position), position, "AddStock");
            lvItemList.setAdapter(mTakeStockItemListAdapter);
        }
        searchView.setQuery(searchView.getQuery(), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.registration_menu, menu);
        MenuItem item = menu.findItem(R.id.registration_search);
        if (item != null) {
            searchView = (SearchView) item.getActionView();
            searchView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            searchView.setQueryHint("Enter Product Name");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    mTakeStockItemListAdapter.getFilter().filter(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (mTakeStockItemListAdapter != null) {
                        mTakeStockItemListAdapter.getFilter().filter(s);
                    }
                    return true;
                }
            });

        }
        return true;

    }

    @SuppressLint("StaticFieldLeak")
    public class mgetItemList extends AsyncTask<String, Void, JSONObject> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(AddTakeStock.this);
            mProgressDialog.setMessage("Fetching Items List from server......");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getproductlist"));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONArray jArray = jsonObject.getJSONArray("list");
                        itemList = new ArrayList<>();
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jobj = jArray.getJSONObject(i);
                            String id = jobj.getString("id");
                            String cat_name = jobj.getString("cat_name");
                            JSONArray itmArry = jobj.getJSONArray("items");
                            ArrayList<TakeStockItemBean> arryItemList = new ArrayList<>();
                            for (int j = 0; j < itmArry.length(); j++) {
                                JSONObject itmObj = itmArry.getJSONObject(j);
                                String product_id = itmObj.getString("product_id");
                                String product_mrp = itmObj.getString("product_mrp");
                                String sku_code = itmObj.getString("sku_code");
                                String product_name = itmObj.getString("product_name");
                                String Case_Size = itmObj.getString("Case_Size");
                                arryItemList.add(new TakeStockItemBean(product_id, product_mrp, sku_code, product_name, "0", false, Case_Size));
                            }
                            itemList.add(new TakeStoclListBean(id, cat_name, arryItemList));
                        }
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddTakeStock.this, Constant.ProductListPref, MODE_PRIVATE);
                        complexPreferences.putObject(Constant.ProductListStockObj, itemList);
                        complexPreferences.putObject(Constant.distributor_id, distributerId);
                        complexPreferences.commit();
                        prepareListData(itemList);
                    } else {
                        Utils.showToast(AddTakeStock.this, jsonObject.getString("message"));
//                        Toast.makeText(AddTakeStock.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertDialog(AddTakeStock.this, "Stock", "Improper response from server", true);
//                Toast.makeText(AddTakeStock.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
        }
    }

    /**
     * Send Otp to Distributor Phone
     */

    @SuppressLint("StaticFieldLeak")
    public class mSaveData extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(AddTakeStock.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Saving Data to server...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "takestock"));
            nameValuePair.add(new BasicNameValuePair("distributor_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("user_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("data", params[2]));
            nameValuePair.add(new BasicNameValuePair("lat", params[3]));
            nameValuePair.add(new BasicNameValuePair("long", params[4]));
            nameValuePair.add(new BasicNameValuePair("address", params[5]));
//            JSONObject jsonObjectFromUrl = new JSONParser().getJsonObjectFromHttp(Constant.BASE_URL, nameValuePair, JSONParser.Http.POST);
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                if (mDialog.isShowing()) mDialog.dismiss();
/*
                ComplexPreferences mComplexPreferences = ComplexPreferences.getComplexPreferences(AddTakeStock.this, Constant.EditedProductListPref, MODE_PRIVATE);
*/
                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddTakeStock.this, "", MODE_PRIVATE);
                complexPreferences.clear(Constant.EditedProductListPref);

                // ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddTakeStock.this, Constant.ProductListPref, MODE_PRIVATE);
                complexPreferences.clear(Constant.ProductListPref);


                //mComplexPreferences.commit();
           /*     ArrayList<StockPlaced> stockPlacedArrayList = db.getAllisOrderStockPlaced(mUserid);
                ArrayList<String> distributorUserbased = new ArrayList<>();
                if (stockPlacedArrayList.size() > 0) {
                    for (int i = 0; i < stockPlacedArrayList.size(); i++) {
                        distributorUserbased.add(stockPlacedArrayList.get(i).getDistributorId());
                    }
                    if (distributorUserbased.contains(distributerId)) {
                        stockAddOrUpdate("update");

                    } else {
                        stockAddOrUpdate("add");
                    }
                } else {
                    //order is  placed placed =0
                    //stock  taken =1
                    stockAddOrUpdate("add");
                }*/
//                Toast.makeText(AddTakeStock.this, "Stock added successfully", Toast.LENGTH_SHORT).show();
                try {
                    Utils.showToast(AddTakeStock.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AddTakeStock.this.finish();

            } else {
                if (mDialog.isShowing()) mDialog.dismiss();
//                Toast.makeText(AddTakeStock.this, "Improper response from server", Toast.LENGTH_SHORT).show();
                Utils.showAlertDialog(AddTakeStock.this, "Stock", "Improper response from server", true);
            }

        }
    }

    /**
     * Send Otp to Distributor Phone
     */

    @SuppressLint("StaticFieldLeak")
    public class otpGeneration extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(AddTakeStock.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Saving Data to server...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            /*"http://login.businesslead.co.in/api/mt/SendSMS?user=Malas&password=malas123&senderid=MalaFP&channel=Trans&DCS=0&flashsms=0&number=959526383&text=hello&route=6*/
//            String url = "http://login.businesslead.co.in/api/mt/SendSMS";
//            List<NameValuePair> nameValuePair = new ArrayList<>();
//            nameValuePair.add(new BasicNameValuePair("user", "Malas"));
//            nameValuePair.add(new BasicNameValuePair("password", "malas123"));
//            nameValuePair.add(new BasicNameValuePair("senderid", "MalaFP"));
//            nameValuePair.add(new BasicNameValuePair("channel", "Trans"));
//            nameValuePair.add(new BasicNameValuePair("DCS", "0"));
//            nameValuePair.add(new BasicNameValuePair("flashsms", "0"));
//            nameValuePair.add(new BasicNameValuePair("number", params[0]));
//            nameValuePair.add(new BasicNameValuePair("text", params[1]));
//            nameValuePair.add(new BasicNameValuePair("route", "6"));


            String url = "http://smsjust.com/sms/user/urlsms.php";
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("username", "malasfruit"));
            nameValuePair.add(new BasicNameValuePair("pass", "PaRx!51@"));
            nameValuePair.add(new BasicNameValuePair("senderid", "MalasP"));
            nameValuePair.add(new BasicNameValuePair("dest_mobileno", params[0]));
            nameValuePair.add(new BasicNameValuePair("message", params[1]));
            nameValuePair.add(new BasicNameValuePair("msgtype", "TXT"));
            nameValuePair.add(new BasicNameValuePair("response", "Y"));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(url, nameValuePair, HttpUrlConnectionJSONParser.Http.GET);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            //{"ErrorCode":"13","ErrorMessage":"mobile numbers not valid","JobId":null,"MessageData":null}
            //{"ErrorCode":"000","ErrorMessage":"Done","JobId":"26283473","MessageData":[{"Number":"918149525824","MessageId":"cTRu9vU9P0eD6CDzA6Jhvw"}]}
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("ErrorCode").equalsIgnoreCase("000")) {
                        new saveOtpOnServer().execute(String.valueOf(otp));
                    } else {
                        Toast.makeText(AddTakeStock.this, jsonObject.getString("ErrorMessage"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            mDialog.dismiss();
        }

    }

    /**
     * Save Otp on server
     */

    @SuppressLint("StaticFieldLeak")
    public class saveOtpOnServer extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(AddTakeStock.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Saving Data to server...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "insert_otp"));
            nameValuePair.add(new BasicNameValuePair("userId", mUserid));
            nameValuePair.add(new BasicNameValuePair("distributorId", distributerId));
            nameValuePair.add(new BasicNameValuePair("otp", params[0]));
            nameValuePair.add(new BasicNameValuePair("phone", params[1]));
            nameValuePair.add(new BasicNameValuePair("message", params[2]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        dialogOtplink();
                    } else {
                        Toast.makeText(AddTakeStock.this, "Improper Response from server", Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            mDialog.dismiss();
        }

    }

    /**
     * Get Otp from server to verify
     */

    @SuppressLint("StaticFieldLeak")
    public class getOtp extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;


        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(AddTakeStock.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Saving Data to server...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "get_otp"));
            nameValuePair.add(new BasicNameValuePair("userId", mUserid));
            nameValuePair.add(new BasicNameValuePair("distributorId", distributerId));


            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONArray jArray = jsonObject.getJSONArray("otpList");
                        JSONObject jobj = jArray.getJSONObject(0);
                        String otp = jobj.getString("otp");
                        if (OtpUserEntered.equalsIgnoreCase(otp)) {

                            dialogOTP.dismiss();
                            Toast.makeText(AddTakeStock.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                            saveDataAsyn = new mSaveData().execute(distributerId, mUserid, jsonStringOnSave, "" + latitude, "" + longitude, addressStr);

                        } else {
                            Toast.makeText(AddTakeStock.this, "OTP Not Valid.Please Confirm the Otp.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        dialogOTP.dismiss();
                        Toast.makeText(AddTakeStock.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(AddTakeStock.this, "Improper Response from server", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }

    }
}
