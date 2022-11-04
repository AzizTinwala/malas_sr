package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.google.gson.reflect.TypeToken;
import com.malas.appsr.malasapp.BeanClasses.PlaceOrderItemBean;
import com.malas.appsr.malasapp.BeanClasses.PlaceOrderListBean;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderListBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.PlaceOrderConfirmationAdapter;
import com.malas.appsr.malasapp.dbHandler.DatabaseHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by Arwa on 30-Jan-18.
 */

public class ConfirmPlaceOrderActivity extends Activity {
    ListView lv_place_order_list;
    Button btnConfirm, btnCancel;
    ArrayList<PlaceOrderListBean> itemList;
    String userId, distributorId, latitude, longitude, address;
    ArrayList<PlaceOrderItemBean> getTemporyIOtem;
    TextView tv_text_total;
    int totalInboxSize;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_place_order);

        initView();
        db = new DatabaseHandler(this);

        setOnClick();

    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        lv_place_order_list = findViewById(R.id.lv_place_order_confirm);
        btnConfirm = findViewById(R.id.btn_confirm);
        tv_text_total = findViewById(R.id.tv_text_total);
        btnCancel = findViewById(R.id.btn_cancel);
        Intent intent = getIntent();
        if (intent != null) {
            itemList = (ArrayList<PlaceOrderListBean>) getIntent().getSerializableExtra("itemList");
            userId = intent.getStringExtra("userId");
            distributorId = intent.getStringExtra("distributorId");
            latitude = intent.getStringExtra("latitude");
            longitude = intent.getStringExtra("longitude");
            address = intent.getStringExtra("address");
        }

        if (itemList != null) {
            if (itemList.size() > 0) {
                getTemporyIOtem = new ArrayList<>();
                for (int i = 0; i < itemList.size(); i++) {

                    for (int j = 0; j < itemList.get(i).getArryItemList().size(); j++) {
                        PlaceOrderItemBean itemBean = itemList.get(i).getArryItemList().get(j);
                        if (!itemBean.getInboxSize().equals("0")) {
                            totalInboxSize += Integer.parseInt(itemBean.getInboxSize());
                            tv_text_total.setText(totalInboxSize + "");
                            PlaceOrderItemBean placeOrderItemBean = new PlaceOrderItemBean(itemBean.getId(), itemBean.getName(), itemBean.getOrderQty(), itemBean.getStockQty(), itemBean.getDifference(), itemBean.getPacketSize(), itemBean.getInboxSize(), itemBean.getSkuCode(), itemBean.getCategoryName());
                            getTemporyIOtem.add(placeOrderItemBean);
                        }


                    }
                }
                PlaceOrderConfirmationAdapter adapter = new PlaceOrderConfirmationAdapter(ConfirmPlaceOrderActivity.this, getTemporyIOtem);
                lv_place_order_list.setAdapter(adapter);
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("userId", userId);
        intent.putExtra("distributorId", distributorId);
        intent.putExtra("itemList", itemList);
        setResult(Activity.RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    private void setOnClick() {
        this.setFinishOnTouchOutside(false);

        btnCancel.setOnClickListener(v -> onBackPressed());

        btnConfirm.setOnClickListener(v -> {
            btnConfirm.setEnabled(false);
            if (getTemporyIOtem.size() > 0) {
                JSONObject orderJsonObject = new JSONObject();

                JSONArray jsonArray = new JSONArray();
                JSONObject obj;


                if (itemList != null) {
                    if (itemList.size() > 0) {
                        for (int i = 0; i < itemList.size(); i++) {

                            for (int j = 0; j < itemList.get(i).getArryItemList().size(); j++) {
                                PlaceOrderItemBean itemBean = itemList.get(i).getArryItemList().get(j);
                                obj = new JSONObject();
                                try {
                                    obj.put("item_id", itemBean.getId());
                                    obj.put("item_name", itemBean.getName());
                                    obj.put("orderquty", itemBean.getOrderQty());
                                    obj.put("stockquty", itemBean.getStockQty());
                                    obj.put("diffrance", itemBean.getDifference());
                                    obj.put("paketsize", itemBean.getInboxSize());
                                    obj.put("sku_code", itemBean.getSkuCode());
                                    obj.put("category_name", itemBean.getCategoryName());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                jsonArray.put(obj);
                            }
                        }

                        try {
                            orderJsonObject.put("orders", jsonArray);

                            //  Log.e("", "" + orderJsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        new mSubmitOrderStockDifference().execute(userId, distributorId, orderJsonObject.toString(), "" + latitude, "" + longitude, address);

                    }
                }
            } else {
                Toast.makeText(ConfirmPlaceOrderActivity.this, "Please Fill Appropriate Data", Toast.LENGTH_SHORT).show();
                btnConfirm.setEnabled(true);
            }
        });
    }

    /*public void stockAddOrUpdate(String addOrUpdate) {
        //stock taken means 0
        //order placed means 1
        if (addOrUpdate.equals("add")) {
            StockPlaced stockPlaced = new StockPlaced(distributorId, userId, "1", "1");
            db.addisorderplaced_stock(stockPlaced);

        } else if (addOrUpdate.equals("update")) {
            StockPlaced stockPlaced = new StockPlaced(distributorId, userId, "1", "1");
            db.updateIsOrderStockPlaced(stockPlaced);

        }
    }*/

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage(message);

        if (status != null) {
            // Setting alert dialog icon
            alertDialog.setIcon(R.mipmap.ic_launcher);
        }

        // Setting OK Button
        alertDialog.setButton("OK", (dialog, which) -> {

            finish();
            dialog.cancel();

        });

        // Showing Alert Message
        alertDialog.show();
    }

    @SuppressLint("StaticFieldLeak")
    public class mSubmitOrderStockDifference extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(ConfirmPlaceOrderActivity.this);
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
                        //      Log.e("Result JSON Object", "" + jsonObject.toString());

                        ComplexPreferences mComplexPreferences = ComplexPreferences.getComplexPreferences(ConfirmPlaceOrderActivity.this, Constant.PlaceOrderProductListTempPref, MODE_PRIVATE);
                        mComplexPreferences.clear(Constant.PlaceOrderProductListTempPref);
                        ComplexPreferences mComplexPreferencesEdited = ComplexPreferences.getComplexPreferences(ConfirmPlaceOrderActivity.this, Constant.EditedOrderProductListPref, MODE_PRIVATE);
                        mComplexPreferencesEdited.clear(Constant.EditedOrderProductListPref);
                        ComplexPreferences complexPreferences1 = ComplexPreferences.getComplexPreferences(ConfirmPlaceOrderActivity.this, Constant.ProductListPref, MODE_PRIVATE);
                        Type typeOutlet = new TypeToken<ArrayList<TakeOutletOrderListBean>>() {
                        }.getType();
                        ArrayList<TakeOutletOrderListBean> itemListArray = complexPreferences1.getArray(Constant.ProductListObj, typeOutlet);
                        if (itemListArray != null && itemListArray.size() > 0) {
                            if (itemListArray.get(0).getOutlet_id() != null) {

                                for (int i = 0; i < itemListArray.size(); i++) {
                                    for (int j = 0; j < itemListArray.get(i).getArryItemList().size(); j++) {
                                        if (!itemListArray.get(i).getArryItemList().get(j).getProduct_qty().equals("0")) {
                                            itemListArray.get(i).getArryItemList().get(j).setProduct_qty("0");
                                        }
                                    }
                                }
                            }
                        }
                        complexPreferences1.commit();
                      /*  ArrayList<StockPlaced> stockPlacedArrayList = db.getAllisOrderStockPlaced(userId);
                        ArrayList<String> distributorUserbased = new ArrayList<>();
                        if (stockPlacedArrayList.size() > 0) {
                            for (int i = 0; i < stockPlacedArrayList.size(); i++) {
                                distributorUserbased.add(stockPlacedArrayList.get(i).getDistributorId());
                            }
                            //order is placed =0
                            //stock not taken = 0
                            if (distributorUserbased.contains(distributorId)) {
                                stockAddOrUpdate("update");
                            } else {
                                stockAddOrUpdate("add");
                            }


                        } else {
                            stockAddOrUpdate("add");

                        }*/

                        //  db.deleteisOrderOutletWiseRecords(distributorId);
                        db.updateOrderOutlet(distributorId, "1");
                        // Log.v("updatr",i+"");
                        showAlertDialog(ConfirmPlaceOrderActivity.this, "Distributor Order", jsonObject.getString("message"), true);

                    } else {
                        Utils.showAlertDialog(ConfirmPlaceOrderActivity.this, "Distributor Order", jsonObject.getString("message"), true);
//                        Toast.makeText(ShowPlaceOrderNew.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertDialog(ConfirmPlaceOrderActivity.this, "Distributor Order", "Improper response from server", true);
//                Toast.makeText(ShowPlaceOrderNew.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

}
