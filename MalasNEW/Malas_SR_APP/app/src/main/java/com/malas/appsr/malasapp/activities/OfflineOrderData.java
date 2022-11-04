package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.google.gson.reflect.TypeToken;

import com.malas.appsr.malasapp.BeanClasses.ReasonSubmitBean;
import com.malas.appsr.malasapp.BeanClasses.SaveData;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderItemBean;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderListBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.OfflineOrderAdapter;
import com.malas.appsr.malasapp.dbHandler.DatabaseHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by Admin on 22-Apr-18.
 */

public class OfflineOrderData extends Activity {
    ListView lvOrder;
    Button btnSave;
    String distributorId, outletId;
    AsyncTask<String, Void, JSONObject> saveDataAsyn;
    AsyncTask<String, Void, JSONObject> saveEditDataAsyn;

    ArrayList<TakeOutletOrderItemBean> productList;
    ArrayList<SaveData> saveListFromDb;
    JSONObject mJsonObject;
    String from;
    LinearLayout llOffline, llOfflineTakeOrder;
    ArrayList<ReasonSubmitBean> reasonSubmitBeans;
    LinearLayout llSaveEdit;
    private DatabaseHandler db;
    private Button btnSaveReason, btnEdit;
    private String routeName, distributorName;
    private UserLoginInfoBean mUserLoginInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_order);
        initview();
        db = new DatabaseHandler(this);
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(OfflineOrderData.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

        Intent intent = getIntent();

        if (intent != null) {
            from = getIntent().getStringExtra("From");
            distributorId = getIntent().getStringExtra("distributorId");
            outletId = getIntent().getStringExtra("outletId");
            String routeId = getIntent().getStringExtra("routeId");
            routeName = getIntent().getStringExtra("routeName");
            distributorName = getIntent().getStringExtra("distributorName");

            switch (from) {
                case "AddRecord": {
                    if (saveListFromDb != null && productList != null) {
                        saveListFromDb.clear();
                        productList.clear();
                    }
                    saveListFromDb = db.getAllSaveDataRecord(distributorId, outletId);
                    productList = db.getSaveProductRecord(distributorId, outletId);
                    llOffline.setVisibility(View.VISIBLE);
                    llSaveEdit.setVisibility(View.VISIBLE);
                    btnSaveReason.setVisibility(View.GONE);
                    llOfflineTakeOrder.setVisibility(View.GONE);
                    Collections.sort(productList, (lhs, rhs) -> lhs.getProduct_id().compareTo(rhs.getProduct_id()));
                    OfflineOrderAdapter offlineOrderAdapter = new OfflineOrderAdapter(OfflineOrderData.this, productList, null);
                    lvOrder.setAdapter(offlineOrderAdapter);

                    break;
                }
                case "EditRecord": {
                    if (saveListFromDb != null && productList != null) {
                        saveListFromDb.clear();
                        productList.clear();
                    }
                    saveListFromDb = db.getEditSaveDataRecord(distributorId, outletId);
                    productList = db.getEditSaveProductRecord(distributorId, outletId);
                    llOffline.setVisibility(View.VISIBLE);
                    llOfflineTakeOrder.setVisibility(View.GONE);
                    llSaveEdit.setVisibility(View.VISIBLE);
                    btnSaveReason.setVisibility(View.GONE);
                    Collections.sort(productList, (lhs, rhs) -> lhs.getProduct_id().compareTo(rhs.getProduct_id()));
                    OfflineOrderAdapter offlineOrderAdapter = new OfflineOrderAdapter(OfflineOrderData.this, productList, null);
                    lvOrder.setAdapter(offlineOrderAdapter);

                    break;
                }
                case "TakeOrder": {
                    reasonSubmitBeans = db.getReasonSubmitRecord(distributorId, routeId);
                    llOffline.setVisibility(View.GONE);
                    llSaveEdit.setVisibility(View.GONE);
                    btnSaveReason.setVisibility(View.VISIBLE);
                    llOfflineTakeOrder.setVisibility(View.VISIBLE);

                    OfflineOrderAdapter offlineOrderAdapter = new OfflineOrderAdapter(OfflineOrderData.this, null, reasonSubmitBeans);
                    lvOrder.setAdapter(offlineOrderAdapter);

                    break;
                }
            }


        }
        setOnClick();

    }

    private void initview() {
        lvOrder = findViewById(R.id.lv_offline_order);
        btnSaveReason = findViewById(R.id.btn_save_reason);
        btnSave = findViewById(R.id.btn_save);
        btnEdit = findViewById(R.id.btn_edit);
        llSaveEdit = findViewById(R.id.ll_save_edit);
        llOfflineTakeOrder = findViewById(R.id.ll_offline_take_order);
        llOffline = findViewById(R.id.ll_offline);

    }

    private void setOnClick() {
        this.setFinishOnTouchOutside(false);
        btnEdit.setOnClickListener(v -> {
                    if (from.equals("EditRecord")) {
                        Intent intent = new Intent(OfflineOrderData.this, EditTakenOrder.class);
                        intent.putExtra("from", "OfflineOrderEdit");

                        intent.putExtra("list", productList);
                        intent.putExtra("distributorId", distributorId);
                        intent.putExtra("routeName", routeName);
                        intent.putExtra("distributorName", distributorName);
                        intent.putExtra("outletId", outletId);
                        intent.putExtra("saveListUniqueorderId", saveListFromDb.get(0).getOrder_unique_Id());
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(OfflineOrderData.this, AddTakeOrder.class);
                        intent.putExtra("from", "OfflineOrderAdd");

                        intent.putExtra("list", productList);
                        intent.putExtra("distributorId", distributorId);
                        intent.putExtra("routeName", routeName);
                        intent.putExtra("distributorName", distributorName);
                        intent.putExtra("outletId", outletId);
                        intent.putExtra("saveListUniqueorderId", saveListFromDb.get(0).getOrder_unique_Id());
                        startActivity(intent);
                        finish();
                    }
                }
        );
        btnSaveReason.setOnClickListener(v -> {
            if (Utils.isInternetConnected(OfflineOrderData.this)) {
                if (from.equals("TakeOrder")) {
                    if (reasonSubmitBeans != null && reasonSubmitBeans.size() > 0) {
                        for (int i = 0; i < reasonSubmitBeans.size(); i++) {
                            new mSubmitReason().execute(reasonSubmitBeans.get(i).getUserId(), reasonSubmitBeans.get(i).getOutletId(), reasonSubmitBeans.get(i).getReasonId(), reasonSubmitBeans.get(i).getDistributorId(), reasonSubmitBeans.get(i).getLatitude(), reasonSubmitBeans.get(i).getLongitude(), reasonSubmitBeans.get(i).getAddress());

                        }
                        db.deleteReasonSubmitRecord(distributorId);
                        Toast.makeText(OfflineOrderData.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                }

            } else {
                Toast.makeText(OfflineOrderData.this, "Connect To Internet", Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(v -> {

            if (Utils.isInternetConnected(OfflineOrderData.this)) {

                if (saveListFromDb != null && saveListFromDb.size() > 0) {
                    String distributerId = "", mUserid = "", outletId = "", latitude = "", longitude = "", addressStr = "", withASM = "", withSSO = "";
                    String timeTaken = "", orderUniqueId = "";
                    for (int i = 0; i < saveListFromDb.size(); i++) {
                        distributerId = saveListFromDb.get(i).getDistributorId();
                        mUserid = saveListFromDb.get(i).getUserId();
                        outletId = saveListFromDb.get(i).getOutletId();
                        latitude = saveListFromDb.get(i).getLatitude();
                        longitude = saveListFromDb.get(i).getLongitude();
                        addressStr = saveListFromDb.get(i).getAddress();
                        withSSO = saveListFromDb.get(i).getWithSSo();
                        withASM = saveListFromDb.get(i).getWithAsm();
                        timeTaken = saveListFromDb.get(i).getTimeTakenOrderOffline();
                        if (from.equals("EditRecord")) {
                            orderUniqueId = saveListFromDb.get(i).getOrder_unique_Id();
                        }
                    }

                    try {
                        mJsonObject = new JSONObject();

                        JSONArray mJsonArray = new JSONArray();

                        if (productList != null) {
                            for (int i = 0; i < productList.size(); i++) {
                                JSONObject mTempObj = new JSONObject();

                                mTempObj.put("item_id", productList.get(i).getProduct_id());

                                mTempObj.put("item_qty", productList.get(i).getProduct_qty());

                                mJsonArray.put(mTempObj);
                            }
                            mJsonObject.put("item_list", mJsonArray);
                            System.out.print(mJsonObject);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (mJsonObject != null) {
                        if (from.equals("EditRecord")) {
                            saveEditDataAsyn = new mSaveEditData().execute(distributerId, mUserid, mJsonObject.toString(), outletId, "" + latitude, "" + longitude, addressStr, withASM, withSSO, orderUniqueId, timeTaken);
                        } else {
                            saveDataAsyn = new mSaveData().execute(distributerId, mUserid, mJsonObject.toString(), outletId, "" + latitude, "" + longitude, addressStr, withASM, withSSO, timeTaken);

                        }
                    }
                }

            } else {
                Toast.makeText(OfflineOrderData.this, "Connect To Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

   /* public void stockAddOrUpdateWhileAdding(String addOrUpdate) {
        //stock taken means 0 stock not taken means 1
        //order placed means 1 order not placed 0


        if (addOrUpdate.equals("add")) {
            StockPlaced stockPlaced = new StockPlaced(distributorId, mUserLoginInfoBean.getUser_id(), "0", "0");

            db.addisorderplaced_stock(stockPlaced);
            db.addOrderPlacedOutletWise(distributorId, mUserLoginInfoBean.getUser_id(), outletId, "0");

        } else if (addOrUpdate.equals("update")) {
            StockPlaced stockPlaced = new StockPlaced(distributorId, mUserLoginInfoBean.getUser_id(), "0", "0");
            db.updateIsOrderStockPlaced(stockPlaced);
            StockPlaced stockPlacedoutletWise = new StockPlaced();
            stockPlacedoutletWise.setDistributorId(distributorId);
            stockPlacedoutletWise.setUserId(mUserLoginInfoBean.getUser_id());
            stockPlacedoutletWise.setOutletId(outletId);
            stockPlacedoutletWise.setIs_order_placed("0");
            db.updateIsOrderPlacedOutletWise(stockPlaced);


        }
    }*/

  /*  public void stockAddOrUpdate(String addOrUpdate) {
        //stock taken means 0 stock not taken means 1
        //order placed means 1 order not placed 0

        if (addOrUpdate.equals("add")) {
            StockPlaced stockPlaced = new StockPlaced(distributorId, mUserLoginInfoBean.getUser_id(), "0", "0");

            db.addisorderplaced_stock(stockPlaced);
            db.addOrderPlacedOutletWise(distributorId, mUserLoginInfoBean.getUser_id(), outletId, "0");

        } else if (addOrUpdate.equals("update")) {
            StockPlaced stockPlaced = new StockPlaced(distributorId, mUserLoginInfoBean.getUser_id(), "0", "0");
            db.updateIsOrderStockPlaced(stockPlaced);
            StockPlaced stockPlacedoutletWise = new StockPlaced();
            stockPlacedoutletWise.setDistributorId(distributorId);
            stockPlacedoutletWise.setUserId(mUserLoginInfoBean.getUser_id());
            stockPlacedoutletWise.setOutletId(outletId);
            stockPlacedoutletWise.setIs_order_placed("0");
            db.updateIsOrderPlacedOutletWise(stockPlaced);

        }
    }*/

    @SuppressLint("StaticFieldLeak")
    public class mSaveData extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(OfflineOrderData.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Saving Data to server...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "take_order"));
            nameValuePair.add(new BasicNameValuePair("distributor_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("user_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("data", params[2]));
            nameValuePair.add(new BasicNameValuePair("outlet_id", params[3]));
            nameValuePair.add(new BasicNameValuePair("lat", params[4]));
            nameValuePair.add(new BasicNameValuePair("long", params[5]));
            nameValuePair.add(new BasicNameValuePair("address", params[6]));
            nameValuePair.add(new BasicNameValuePair("withasm", params[7]));
            nameValuePair.add(new BasicNameValuePair("withsso", params[8]));
            nameValuePair.add(new BasicNameValuePair("timestamp", params[9]));
//            JSONObject jsonObjectFromUrl = new JSONParser().getJsonObjectFromHttp(Constant.BASE_URL, nameValuePair, JSONParser.Http.POST);
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                if (mDialog.isShowing()) mDialog.dismiss();
                db.deleteSaveRecordsAfterSubmit(distributorId, outletId);
                db.deleteSaveProductRecordsAfterSubmit(distributorId, outletId);


                //order is not placed =0
                //stock not taken =1
              /*  ArrayList<StockPlaced> stockPlacedArrayList = db.getAllisOrderStockPlaced(mUserLoginInfoBean.getUser_id());
                ArrayList<String> distributorUserbased = new ArrayList<>();
                //order is not placed placed =1
                //stock not taken =0
                if (stockPlacedArrayList.size() > 0) {
                    for (int i = 0; i < stockPlacedArrayList.size(); i++) {
                        distributorUserbased.add(stockPlacedArrayList.get(i).getDistributorId());
                    }
                    if (distributorUserbased.contains(distributorId)) {
                        stockAddOrUpdateWhileAdding("update");

                    } else {
                        stockAddOrUpdateWhileAdding("add");
                    }
                } else stockAddOrUpdateWhileAdding("add");
*/
                ComplexPreferences mComplexPreferences = ComplexPreferences.getComplexPreferences(OfflineOrderData.this, Constant.EditedOrderProductListPref, MODE_PRIVATE);
                mComplexPreferences.clear(Constant.EditedOrderProductListPref);

                ArrayList<TakeOutletOrderListBean> itemList;
                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(OfflineOrderData.this, Constant.ProductListPref, MODE_PRIVATE);
                Type typeOutlet = new TypeToken<ArrayList<TakeOutletOrderListBean>>() {
                }.getType();
                itemList = complexPreferences.getArray(Constant.ProductListObj, typeOutlet) == null ? new ArrayList<>() : (ArrayList<TakeOutletOrderListBean>) complexPreferences.getArray(Constant.ProductListObj, typeOutlet);

                for (int i = 0; i < itemList.size(); i++) {
                    for (int j = 0; j < itemList.get(i).getArryItemList().size(); j++) {

                        itemList.get(i).getArryItemList().get(j).setProduct_qty("0");

                        //  itemList.get(i).setOutlet_id(outletId);


                    }
                }
                complexPreferences.putObject(Constant.ProductListObj, itemList);
                complexPreferences.commit();

//                Toast.makeText(AddTakeOrder.this, "Order added successfully", Toast.LENGTH_SHORT).show();
                try {
                    Utils.showToast(OfflineOrderData.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OfflineOrderData.this.finish();

            } else {
                if (mDialog.isShowing()) mDialog.dismiss();
                Utils.showAlertDialog(OfflineOrderData.this, "Order", "Improper response from server", true);
//                Toast.makeText(AddTakeOrder.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mSaveEditData extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(OfflineOrderData.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Saving Data to server...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "update_order"));
            nameValuePair.add(new BasicNameValuePair("distributor_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("user_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("data", params[2]));
            nameValuePair.add(new BasicNameValuePair("outlet_id", params[3]));
            nameValuePair.add(new BasicNameValuePair("lat", params[4]));
            nameValuePair.add(new BasicNameValuePair("long", params[5]));
            nameValuePair.add(new BasicNameValuePair("address", params[6]));
            nameValuePair.add(new BasicNameValuePair("withasm", params[7]));
            nameValuePair.add(new BasicNameValuePair("withsso", params[8]));
            nameValuePair.add(new BasicNameValuePair("order_id", params[9]));
            nameValuePair.add(new BasicNameValuePair("timestamp", params[10]));
//            JSONObject jsonObjectFromUrl = new JSONParser().getJsonObjectFromHttp(Constant.BASE_URL, nameValuePair, JSONParser.Http.POST);
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                if (mDialog.isShowing()) mDialog.dismiss();

                db.deleteEditSaveRecordsAfterSubmit(distributorId, outletId);
                db.deleteEditSaveProductRecordsAfterSubmit(distributorId, outletId);

                ComplexPreferences mComplexPreferences = ComplexPreferences.getComplexPreferences(OfflineOrderData.this, Constant.EditedOrderProductListPref, MODE_PRIVATE);
                mComplexPreferences.clear(Constant.EditedOrderProductListPref);
/*
                ArrayList<StockPlaced> stockPlacedArrayList = db.getAllisOrderStockPlaced(mUserLoginInfoBean.getUser_id());
                ArrayList<String> distributorUserbased = new ArrayList<>();
                if (stockPlacedArrayList.size() > 0) {
                    for (int i = 0; i < stockPlacedArrayList.size(); i++) {
                        distributorUserbased.add(stockPlacedArrayList.get(i).getDistributorId());
                    }
                    if (distributorUserbased.contains(distributorId)) {
                        stockAddOrUpdate("update");

                    } else {
                        stockAddOrUpdate("add");
                    }
                } else {
                    //order is not placed placed =1
                    //stock not taken =0
                    stockAddOrUpdate("add");
                }*/

                try {
                    Toast.makeText(OfflineOrderData.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OfflineOrderData.this.finish();

            } else {
                if (mDialog.isShowing()) mDialog.dismiss();
                Toast.makeText(OfflineOrderData.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    class mSubmitReason extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(OfflineOrderData.this);
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
            /*nameValuePair.add(new BasicNameValuePair("timestamp", params[7]));*/

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        //  Log.e("Result JSON Object", "" + jsonObject.toString());
//                        if (mDialog.isShowing()) mDialog.dismiss();
                        Utils.showToast(OfflineOrderData.this, jsonObject.getString("message"));
                        if (mDialog != null)
                            mDialog.dismiss();
                        finish();
                    } else {
                        // if (mDialog.isShowing()) mDialog.dismiss();
                        Utils.showToast(OfflineOrderData.this, jsonObject.getString("message"));
                        if (mDialog != null)
                            mDialog.dismiss();
                        finish();
                        //    Utils.showAlertDialog(OfflineOrderData.this, "Outlet Order", jsonObject.getString("message"), true);
//                        Toast.makeText(OutletOrdersActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    if (mDialog != null)
                        mDialog.dismiss();
                    e.printStackTrace();
                }

            } else {
                // if (mDialog.isShowing()) mDialog.dismiss();
                Utils.showToast(OfflineOrderData.this, "Improper response from server");

                if (mDialog != null) mDialog.dismiss();
                finish();
//                Toast.makeText(OutletOrdersActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }

        }
    }


}

