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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
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

import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.OutletOrdersBean;
import com.malas.appsr.malasapp.BeanClasses.SaveData;
import com.malas.appsr.malasapp.BeanClasses.ShowOutLetBeen;
import com.malas.appsr.malasapp.BeanClasses.TakeOrderEditedItemProductList;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderItemBean;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderListBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.TakeOrderCategoryAdapter;
import com.malas.appsr.malasapp.adapter.TakeOrderItemListAdapter;
import com.malas.appsr.malasapp.customeArrayList.CArrayListTakeOrderEditedItemProductList;
import com.malas.appsr.malasapp.dbHandler.DatabaseHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

//import android.util.Log;

public class EditTakenOrder extends AppCompatActivity implements TakeOrderCategoryAdapter.CategoryClickListner {

    ArrayList<TakeOutletOrderListBean> itemList;
    ArrayList<TakeOutletOrderListBean> listDataHeader;
    AsyncTask<String, Void, JSONObject> saveDataAsyn;
    AsyncTask<String, Void, JSONObject> itemListAsyn;
    TextView mdistributerName;
    String saveListUniqueorderId;
    TextView route_name;
    RecyclerView lvCategoryList, lvItemList;
    String distributerId, routeId;
    ImageView mSaveButton;

    String mUserid, outletId;
    double latitude = 0;
    double longitude = 0;
    String addressStr = "";
    CheckBox cbSSO, cbASM;
    OutletOrdersBean outletOrdersBean;
    ShowOutLetBeen showOutLetBeen;
    DistributerBean distributerBean;
    LocationManager locationManager;
    LocationListener locationListener;
    String from = "";
    ArrayList<TakeOutletOrderItemBean> productList;
    private DatabaseHandler db;
    TakeOrderItemListAdapter mTakeOrderItemListAdapter;
    TakeOrderCategoryAdapter mTakeOrderCategoryAdapter;
    SearchView searchView;
    JSONObject mJsonObject;
    JSONArray mJsonArray;
    ArrayList<TakeOutletOrderItemBean> takeOutletOrderItemBeans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_taken_order);
        lvCategoryList = findViewById(R.id.category_list);
        lvItemList = findViewById(R.id.item_list);
        mSaveButton = findViewById(R.id.iv_save_button);
        mdistributerName = findViewById(R.id.distributr_name);
        route_name = findViewById(R.id.route_name);
        cbASM = findViewById(R.id.cbASM);
        cbSSO = findViewById(R.id.cbSSO);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Order");
        db = new DatabaseHandler(this);
        getCurrentLocation();


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


        if (getIntent().hasExtra("outletOrdersBean")) {
            outletOrdersBean = (OutletOrdersBean) getIntent().getSerializableExtra("outletOrdersBean");

            if (outletOrdersBean.getWithSso() != null) {
                if (outletOrdersBean.getWithSso().equals("1"))
                    cbSSO.setChecked(true);
            }

            if (outletOrdersBean.getWithAsm() != null) {
                if (outletOrdersBean.getWithAsm().equals("1"))
                    cbASM.setChecked(true);
            }
            if (getIntent().hasExtra("distributerBean")) {
                distributerBean = (DistributerBean) getIntent().getSerializableExtra("distributerBean");
                mdistributerName.setText(distributerBean.getFirm_name().toUpperCase(Locale.getDefault()));
            }
            if (getIntent().hasExtra("showOutLetBeen")) {
                showOutLetBeen = (ShowOutLetBeen) getIntent().getSerializableExtra("showOutLetBeen");


                route_name.setText(showOutLetBeen.getRoute_name().toUpperCase(Locale.getDefault()));
                distributerId = showOutLetBeen.getDistribution_id();
                routeId = showOutLetBeen.getRoute_id();
                outletId = showOutLetBeen.getOutlet_id();
            }


            ComplexPreferences mComplexPreferences = ComplexPreferences.getComplexPreferences(this, Constant.EditedOrderProductListPref, MODE_PRIVATE);
            CArrayListTakeOrderEditedItemProductList mTakeOrderEditedItemProductList = mComplexPreferences.getArray(Constant.EditedOrderProductListObj, CArrayListTakeOrderEditedItemProductList.class);

            if (mTakeOrderEditedItemProductList == null)
                mTakeOrderEditedItemProductList = new CArrayListTakeOrderEditedItemProductList();
            if (mTakeOrderEditedItemProductList != null) {
                if (mTakeOrderEditedItemProductList.size() > 0) {


                    for (int k = 0; k < mTakeOrderEditedItemProductList.size(); k++) {
                        if (mTakeOrderEditedItemProductList.get(k).getOutletId() != null && mTakeOrderEditedItemProductList.get(k).getOutletId().equals(outletId)) {
                            if (mTakeOrderEditedItemProductList.get(k).getOrder_uni_id() != null) {
                                if (mTakeOrderEditedItemProductList.get(k).getOrder_uni_id().equals(outletOrdersBean.getOrder_unique_id())) {
                                    for (int i = 0; i < outletOrdersBean.getProductList().size(); i++) {
                                        for (int j = 0; j < outletOrdersBean.getProductList().get(i).getArryItemList().size(); j++) {
                                            mTakeOrderEditedItemProductList.add(new TakeOrderEditedItemProductList(outletOrdersBean.getProductList().get(i).getArryItemList().get(j).getProduct_id(), outletOrdersBean.getProductList().get(i).getArryItemList().get(j).getProduct_qty(), outletOrdersBean.getProductList().get(i).getArryItemList().get(j).getProduct_name(), outletOrdersBean.getProductList().get(i).getId(), outletOrdersBean.getProductList().get(i).getItem(), outletId, outletOrdersBean.getOrder_unique_id()));
                                            //Log.e("nsc productId " + outletOrdersBean.getProductList().get(i).getArryItemList().get(j).getProduct_id(), "quantity " + outletOrdersBean.getProductList().get(i).getArryItemList().get(j).getProduct_qty());

                                        }
                                    }
                                }
                            }
                        }
                    }

                } else {
                    for (int i = 0; i < outletOrdersBean.getProductList().size(); i++) {
                        for (int j = 0; j < outletOrdersBean.getProductList().get(i).getArryItemList().size(); j++) {
                            mTakeOrderEditedItemProductList.add(new TakeOrderEditedItemProductList(outletOrdersBean.getProductList().get(i).getArryItemList().get(j).getProduct_id(), outletOrdersBean.getProductList().get(i).getArryItemList().get(j).getProduct_qty(), outletOrdersBean.getProductList().get(i).getArryItemList().get(j).getProduct_name(), outletOrdersBean.getProductList().get(i).getId(), outletOrdersBean.getProductList().get(i).getItem(), outletId, outletOrdersBean.getOrder_unique_id()));
                            //Log.e("nsc productId " + outletOrdersBean.getProductList().get(i).getArryItemList().get(j).getProduct_id(), "quantity " + outletOrdersBean.getProductList().get(i).getArryItemList().get(j).getProduct_qty());
                        }
                    }
                }

            } else {
                for (int i = 0; i < outletOrdersBean.getProductList().size(); i++) {
                    for (int j = 0; j < outletOrdersBean.getProductList().get(i).getArryItemList().size(); j++) {
                        mTakeOrderEditedItemProductList.add(new TakeOrderEditedItemProductList(outletOrdersBean.getProductList().get(i).getArryItemList().get(j).getProduct_id(), outletOrdersBean.getProductList().get(i).getArryItemList().get(j).getProduct_qty(), outletOrdersBean.getProductList().get(i).getArryItemList().get(j).getProduct_name(), outletOrdersBean.getProductList().get(i).getId(), outletOrdersBean.getProductList().get(i).getItem(), outletId, outletOrdersBean.getOrder_unique_id()));
                        // Log.e("nsc productId " + outletOrdersBean.getProductList().get(i).getArryItemList().get(j).getProduct_id(), "quantity " + outletOrdersBean.getProductList().get(i).getArryItemList().get(j).getProduct_qty());
                    }
                }
            }


            //Log.e("nsc list size", "" + mTakeOrderEditedItemProductList.size());

            mComplexPreferences.putObject(Constant.EditedOrderProductListObj, mTakeOrderEditedItemProductList);
            mComplexPreferences.commit();

        } else if (getIntent().hasExtra("from")) {
            from = getIntent().getStringExtra("from");

            productList = (ArrayList<TakeOutletOrderItemBean>) getIntent().getExtras().getSerializable("list");
            distributerId = getIntent().getStringExtra("distributorId");
            String distributorName = getIntent().getStringExtra("distributorName");
            String routeName = getIntent().getStringExtra("routeName");
            outletId = getIntent().getStringExtra("outletId");
            saveListUniqueorderId = getIntent().getStringExtra("saveListUniqueorderId");
            route_name.setText(routeName);
            mdistributerName.setText(distributorName);
            if (from.equals("OfflineOrderEdit")) {
                ComplexPreferences mComplexPreferences = ComplexPreferences.getComplexPreferences(this, Constant.EditedOrderProductListPref, MODE_PRIVATE);
                CArrayListTakeOrderEditedItemProductList mTakeOrderEditedItemProductList = new CArrayListTakeOrderEditedItemProductList();
                mTakeOrderEditedItemProductList.clear();
                Collections.sort(productList, (lhs, rhs) -> lhs.getProduct_id().compareTo(rhs.getProduct_id()));
                if (productList != null) {
                    if (productList.size() > 0) {
                        for (int i = 0; i < productList.size(); i++) {
                            mTakeOrderEditedItemProductList.add(new TakeOrderEditedItemProductList(productList.get(i).getProduct_id(), productList.get(i).getProduct_qty(), productList.get(i).getProduct_name(), productList.get(i).getCat_id(), productList.get(i).getCatName(), productList.get(i).getOutlet_id(), saveListUniqueorderId));

                        }

                    }


                }

                //Log.e("nsc list size", "" + mTakeOrderEditedItemProductList.size());

                mComplexPreferences.putObject(Constant.EditedOrderProductListObj, mTakeOrderEditedItemProductList);
                mComplexPreferences.commit();
            }


        }
        mSaveButton.setOnClickListener(v -> {

            ComplexPreferences mComplexPreferences = ComplexPreferences.getComplexPreferences(EditTakenOrder.this, Constant.EditedOrderProductListPref, MODE_PRIVATE);
            CArrayListTakeOrderEditedItemProductList updatedTakeOrderItem = mComplexPreferences.getArray(Constant.EditedOrderProductListObj, CArrayListTakeOrderEditedItemProductList.class);


            try {
                 mJsonObject = new JSONObject();
                 mJsonArray = new JSONArray();
                 takeOutletOrderItemBeans = new ArrayList<>();

                for (int i = 0; i < updatedTakeOrderItem.size(); i++) {
                    JSONObject mTempObj = new JSONObject();
                    if (updatedTakeOrderItem.get(i).getOutletId() != null) {
                        if (updatedTakeOrderItem.get(i).getOutletId().equals(outletId)) {
                            if (updatedTakeOrderItem.get(i).getItem_qty() != null && !updatedTakeOrderItem.get(i).getItem_qty().equals("0")) {

                                mTempObj.put("item_id", updatedTakeOrderItem.get(i).getItem_id());
                                mTempObj.put("item_name", updatedTakeOrderItem.get(i).getProduct_name());
                                mTempObj.put("item_qty", updatedTakeOrderItem.get(i).getItem_qty());
                                if (updatedTakeOrderItem.get(i).getProduct_name() != null) {
                                    String productId = updatedTakeOrderItem.get(i).getItem_id();
                                    String productName = updatedTakeOrderItem.get(i).getProduct_name();
                                    String catId = updatedTakeOrderItem.get(i).getCatId();
                                    String catName = updatedTakeOrderItem.get(i).getCatName();
                                    String productQuantity = updatedTakeOrderItem.get(i).getItem_qty();
                                    String outletId = updatedTakeOrderItem.get(i).getOutletId();
                                    String orderid = updatedTakeOrderItem.get(i).getOrder_uni_id();

                                    mJsonArray.put(mTempObj);
                                    TakeOutletOrderItemBean takeOutletOrderItemBean = new TakeOutletOrderItemBean(catId, catName, productId, productName, productQuantity, outletId, orderid);
                                    takeOutletOrderItemBeans.add(takeOutletOrderItemBean);
                                }

                            }
                        }
                    } else {
                        if (updatedTakeOrderItem.get(i).getItem_qty() != null && !updatedTakeOrderItem.get(i).getItem_qty().equals("0")) {

                            mTempObj.put("item_id", updatedTakeOrderItem.get(i).getItem_id());
                            mTempObj.put("item_name", updatedTakeOrderItem.get(i).getProduct_name());
                            mTempObj.put("item_qty", updatedTakeOrderItem.get(i).getItem_qty());
                            if (updatedTakeOrderItem.get(i).getProduct_name() != null) {
                                String productId = updatedTakeOrderItem.get(i).getItem_id();
                                String productName = updatedTakeOrderItem.get(i).getProduct_name();
                                String catId = updatedTakeOrderItem.get(i).getCatId();
                                String catName = updatedTakeOrderItem.get(i).getCatName();
                                String productQuantity = updatedTakeOrderItem.get(i).getItem_qty();
                                String outletId = updatedTakeOrderItem.get(i).getOutletId();
                                String orderid = updatedTakeOrderItem.get(i).getOrder_uni_id();

                                mJsonArray.put(mTempObj);
                                TakeOutletOrderItemBean takeOutletOrderItemBean = new TakeOutletOrderItemBean(catId, catName, productId, productName, productQuantity, outletId, orderid);
                                takeOutletOrderItemBeans.add(takeOutletOrderItemBean);
                            }

                        }
                    }


                }
                mJsonObject.put("item_list", mJsonArray);
                System.out.print(mJsonObject);
                System.out.print(updatedTakeOrderItem);


                Intent submitconfirmation = new Intent(getApplicationContext(), ConfirmOutletOrders.class);
                submitconfirmation.putExtra("ItemList", mJsonObject.toString());
                startActivityForResult(submitconfirmation, 222);
/*
                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(EditTakenOrder.this, Constant.UserRegInfoPref, MODE_PRIVATE);
                UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

                mUserid = mUserLoginInfoBean.getUser_id();
                String withASM = "0", withSSO = "0";
                if (cbSSO.isChecked())
                    withSSO = "1";
                if (cbASM.isChecked())
                    withASM = "1";
                String orderUniqueId = "";
                if (getIntent().hasExtra("from")) {
                    if (productList != null && productList.size() > 0)
                        orderUniqueId = saveListUniqueorderId;
                } else {
                    orderUniqueId = outletOrdersBean.getOrder_unique_id();
                }
                if (Utils.isInternetConnected(EditTakenOrder.this)) {
                    if (mJsonArray.length() > 0) {
                        saveDataAsyn = new mSaveData().execute(distributerId, mUserid, mJsonObject.toString(), outletId, "" + latitude, "" + longitude, addressStr, withASM, withSSO, orderUniqueId, "");
                    } else {
                        Toast.makeText(EditTakenOrder.this, "Please fill appropriate data", Toast.LENGTH_SHORT).show();
                        mSaveButton.setEnabled(true);

                    }

                } else {

                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());//dd/MM/yyyy
                    Date currentDateTime = new Date();
                    String strDate = sdfDate.format(currentDateTime);
                    Date date = null;
                    try {
                        date = sdfDate.parse(strDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    assert date != null;
                    long time = date.getTime() / 1000;
                    String dateTimeInMillisecond = time + "";
                    //Log.v("current milisecond", date.getTime() + "");
                    if (takeOutletOrderItemBeans.size() > 0) {
                        SaveData saveData = new SaveData(distributerId, mUserid, outletId, "" + latitude, "" + longitude, addressStr, withASM, withSSO, orderUniqueId, dateTimeInMillisecond);
                        if (!getIntent().hasExtra("from")) {
                            db.addEdittedSaveData(saveData);

                        }
                        if (!getIntent().hasExtra("from")) {
                            db.addEdittedProductSave(takeOutletOrderItemBeans, distributerId, outletId);

                        } else {
                            db.deleteEditSaveProductRecordsAfterSubmit(distributerId, outletId);
                            db.addEdittedProductSave(takeOutletOrderItemBeans, distributerId, outletId);

                        }
                        //order is not placed =0
                        //stock not taken =1
                       /* ArrayList<StockPlaced> stockPlacedArrayList = db.getAllisOrderStockPlaced(mUserid);
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
                            //order is not placed placed =1
                            //stock not taken =0
                            stockAddOrUpdate("add");
                        }*/

/*
                        ComplexPreferences mComplexPreferences1 = ComplexPreferences.getComplexPreferences(EditTakenOrder.this, Constant.EditedOrderProductListPref, MODE_PRIVATE);

                        mComplexPreferences1.clear(Constant.EditedOrderProductListPref);

                        Utils.showToast(EditTakenOrder.this, " Saved Order Data Successfully In Offline Mode.");
                        EditTakenOrder.this.finish();
                    } else {
                        Toast.makeText(EditTakenOrder.this, "Please fill appropriate data", Toast.LENGTH_SHORT).show();
                    }
                    mSaveButton.setEnabled(true);
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
                mSaveButton.setEnabled(true);
            }
        });
        prepareListData(itemList, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 222) {
            if (resultCode == Activity.RESULT_OK) {

                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(EditTakenOrder.this, Constant.UserRegInfoPref, MODE_PRIVATE);
                UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

                mUserid = mUserLoginInfoBean.getUserId();
                String withASM = "0", withSSO = "0";
                if (cbSSO.isChecked())
                    withSSO = "1";
                if (cbASM.isChecked())
                    withASM = "1";
                String orderUniqueId = "";
                if (getIntent().hasExtra("from")) {
                    if (productList != null && productList.size() > 0)
                        orderUniqueId = saveListUniqueorderId;
                } else {
                    orderUniqueId = outletOrdersBean.getOrder_unique_id();
                }
                if (Utils.isInternetConnected(EditTakenOrder.this)) {
                    if (mJsonArray.length() > 0) {
                        saveDataAsyn = new mSaveData().execute(distributerId, mUserid, mJsonObject.toString(), outletId, "" + latitude, "" + longitude, addressStr, withASM, withSSO, orderUniqueId, "");
                    } else {
                        Toast.makeText(EditTakenOrder.this, "Please fill appropriate data", Toast.LENGTH_SHORT).show();
                        mSaveButton.setEnabled(true);

                    }

                } else {

                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());//dd/MM/yyyy
                    Date currentDateTime = new Date();
                    String strDate = sdfDate.format(currentDateTime);
                    Date date = null;
                    try {
                        date = sdfDate.parse(strDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    assert date != null;
                    long time = date.getTime() / 1000;
                    String dateTimeInMillisecond = time + "";
                    //Log.v("current milisecond", date.getTime() + "");
                    if (takeOutletOrderItemBeans.size() > 0) {
                        SaveData saveData = new SaveData(distributerId, mUserid, outletId, "" + latitude, "" + longitude, addressStr, withASM, withSSO, orderUniqueId, dateTimeInMillisecond);
                        if (!getIntent().hasExtra("from")) {
                            db.addEdittedSaveData(saveData);

                        }
                        if (!getIntent().hasExtra("from")) {
                            db.addEdittedProductSave(takeOutletOrderItemBeans, distributerId, outletId);

                        } else {
                            db.deleteEditSaveProductRecordsAfterSubmit(distributerId, outletId);
                            db.addEdittedProductSave(takeOutletOrderItemBeans, distributerId, outletId);

                        }
                        //order is not placed =0
                        //stock not taken =1
                       /* ArrayList<StockPlaced> stockPlacedArrayList = db.getAllisOrderStockPlaced(mUserid);
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
                            //order is not placed placed =1
                            //stock not taken =0
                            stockAddOrUpdate("add");
                        }*/


                        ComplexPreferences mComplexPreferences1 = ComplexPreferences.getComplexPreferences(EditTakenOrder.this, Constant.EditedOrderProductListPref, MODE_PRIVATE);

                        mComplexPreferences1.clear(Constant.EditedOrderProductListPref);

                        Utils.showToast(EditTakenOrder.this, " Saved Order Data Successfully In Offline Mode.");
                        EditTakenOrder.this.finish();
                    } else {
                        Toast.makeText(EditTakenOrder.this, "Please fill appropriate data", Toast.LENGTH_SHORT).show();
                    }
                    mSaveButton.setEnabled(true);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(EditTakenOrder.this, Constant.ProductListPref, MODE_PRIVATE);
        Type typeOutlet = new TypeToken<ArrayList<TakeOutletOrderListBean>>() {
        }.getType();
        ArrayList<TakeOutletOrderListBean> itemListArray = complexPreferences.getArray(Constant.ProductListObj, typeOutlet);
        if (itemListArray != null && itemListArray.size() > 0) {
            if (itemListArray.get(0).getOutlet_id() != null) {
                if (itemListArray.get(0).getOutlet_id().equals(outletId)) {

                    itemList = complexPreferences.getArray(Constant.ProductListObj, typeOutlet) == null ? new ArrayList<>() : (ArrayList<TakeOutletOrderListBean>) complexPreferences.getArray(Constant.ProductListObj, typeOutlet);
                    if (itemList != null) prepareListData(itemList, false);
                } else {
                    for (int i = 0; i < itemListArray.size(); i++) {
                        for (int j = 0; j < itemListArray.get(i).getArryItemList().size(); j++) {
                            if (itemListArray.get(i).getArryItemList().get(j).getProduct_qty() != null && !itemListArray.get(i).getArryItemList().get(j).getProduct_qty().equals("0")) {
                                itemListArray.get(i).getArryItemList().get(j).setProduct_qty("0");
                            }
                        }
                    }
                    itemList = itemListArray;
                    prepareListData(itemList, false);
                }
            }
        }
    }

    public ArrayList<TakeOutletOrderListBean> getProductList() {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(EditTakenOrder.this, Constant.ProductListPref, MODE_PRIVATE);
        Type typeOutlet = new TypeToken<ArrayList<TakeOutletOrderListBean>>() {
        }.getType();
        itemList = complexPreferences.getArray(Constant.ProductListObj, typeOutlet) == null ? new ArrayList<>() : (ArrayList<TakeOutletOrderListBean>) complexPreferences.getArray(Constant.ProductListObj, typeOutlet);

        if (itemList != null && itemList.size() > 0) {
            if (itemList.get(0).getOutlet_id() != null) {
                if (itemList.get(0).getOutlet_id().equals(outletId)) {
                    return itemList;
                } else {
                    for (int i = 0; i < itemList.size(); i++) {
                        for (int j = 0; j < itemList.get(i).getArryItemList().size(); j++) {
                            if (itemList.get(i).getArryItemList().get(j).getProduct_qty() != null && !itemList.get(i).getArryItemList().get(j).getProduct_qty().equals("0")) {
                                itemList.get(i).getArryItemList().get(j).setProduct_qty("0");
                            }
                        }
                    }
                }
            }
        }
        return itemList;
    }

    public void saveProductList(ArrayList<TakeOutletOrderListBean> itemList) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(EditTakenOrder.this, Constant.ProductListPref, MODE_PRIVATE);
        complexPreferences.putObject(Constant.ProductListObj, itemList);
        complexPreferences.commit();
    }

    private void prepareListData(ArrayList<TakeOutletOrderListBean> itemList, boolean callApi) {
        if (itemList != null) {
            listDataHeader = new ArrayList<>();
            listDataHeader.addAll(itemList);
            mTakeOrderCategoryAdapter = new TakeOrderCategoryAdapter(EditTakenOrder.this, listDataHeader, this);
            lvCategoryList.setAdapter(mTakeOrderCategoryAdapter);

//            lvCategoryList.setSelection(0);
//            lvCategoryList.setSelected(true);

            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(EditTakenOrder.this, Constant.ProductListPref, MODE_PRIVATE);
            Type typeOutlet = new TypeToken<ArrayList<TakeOutletOrderListBean>>() {
            }.getType();
            itemList = complexPreferences.getArray(Constant.ProductListObj, typeOutlet) == null ? new ArrayList<>() : (ArrayList<TakeOutletOrderListBean>) complexPreferences.getArray(Constant.ProductListObj, typeOutlet);

            if (itemList != null && itemList.size() > 0) {

                if (outletOrdersBean != null) {
                    for (int j = 0; j < outletOrdersBean.getProductList().size(); j++) {
                        if (itemList.get(0).getId() != null && itemList.get(0).getId().equals(outletOrdersBean.getProductList().get(j).getId())) {
                            TakeOutletOrderListBean takeOutletOrderListBean = outletOrdersBean.getProductList().get(j);
                            for (int i = 0; i < takeOutletOrderListBean.getArryItemList().size(); i++) {
                                for (int k = 0; k < itemList.get(0).getArryItemList().size(); k++) {
                                    if (itemList.get(0).getArryItemList().get(k).getProduct_id() != null && itemList.get(0).getArryItemList().get(k).getProduct_id().equals(takeOutletOrderListBean.getArryItemList().get(i).getProduct_id())) {
                                        itemList.get(0).getArryItemList().set(k, takeOutletOrderListBean.getArryItemList().get(i));
                                    }
                                }
                            }
                        }
                    }
                } else if (getIntent().hasExtra("from")) {

                    Collections.sort(productList, (lhs, rhs) -> lhs.getProduct_id().compareTo(rhs.getProduct_id()));

                    for (int j = 0; j < productList.size(); j++) {
                        if (itemList.get(0).getId() != null && itemList.get(0).getId().equals(productList.get(j).getCat_id())) {
                            for (int k = 0; k < itemList.get(0).getArryItemList().size(); k++) {

                                if (productList.get(j).getProduct_id() != null && productList.get(j).getProduct_id().contains(itemList.get(0).getArryItemList().get(k).getProduct_id())) {
                                    if (itemList.get(0).getArryItemList().get(k).getProduct_id() != null && itemList.get(0).getArryItemList().get(k).getProduct_id().equals(productList.get(j).getProduct_id())) {
                                        itemList.get(0).getArryItemList().get(k).setProduct_qty(productList.get(j).getProduct_qty());

                                    } else {
                                        itemList.get(0).getArryItemList().get(k).setProduct_qty("0");

                                    }
                                    itemList.get(0).setOrder_uni_id(saveListUniqueorderId);

                                }
                            }


                        }
                    }
                }


                mTakeOrderItemListAdapter = new TakeOrderItemListAdapter(EditTakenOrder.this, itemList.get(0), 0, "EditTakenOrder");
                lvItemList.setAdapter(mTakeOrderItemListAdapter);
            }
        }

        if (callApi) {
            if (Utils.isInternetConnected(EditTakenOrder.this)) {
                itemListAsyn = new mgetItemList().execute();
            }

        }

    }

  /*  public void stockAddOrUpdate(String addOrUpdate) {
        //stock taken means 0 stock not taken means 1
        //order placed means 1 order not placed 0
        if (addOrUpdate.equals("add")) {
            StockPlaced stockPlaced = new StockPlaced(distributerId, mUserid, "0", "0");

            db.addisorderplaced_stock(stockPlaced);
            db.addOrderPlacedOutletWise(distributerId, mUserid, outletId, "0");
        } else if (addOrUpdate.equals("update")) {
            StockPlaced stockPlaced = new StockPlaced(distributerId, mUserid, "0", "0");
            db.updateIsOrderStockPlaced(stockPlaced);
            StockPlaced stockPlacedoutletWise = new StockPlaced();
            stockPlacedoutletWise.setDistributorId(distributerId);
            stockPlacedoutletWise.setUserId(mUserid);
            stockPlacedoutletWise.setOutletId(outletId);
            stockPlacedoutletWise.setIs_order_placed("0");
            db.updateIsOrderPlacedOutletWise(stockPlaced);
        }
    }*/

    public void getCurrentLocation() {

        boolean result = Utils.checkPermissions(EditTakenOrder.this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION});
        if (result) {
            // Acquire a reference to the system Location Manager
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
                  /*  Log.e("Your Location is", "" + lat + "--" + lon);
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

    @Override
    protected void onPause() {
        super.onPause();

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCategoryClick(int position) {
        mTakeOrderItemListAdapter.getFilter().filter("");
        searchView.setQuery("", false); // clear the text
        Objects.requireNonNull(getSupportActionBar()).collapseActionView();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(EditTakenOrder.this, Constant.ProductListPref, MODE_PRIVATE);
        Type typeOutlet = new TypeToken<ArrayList<TakeOutletOrderListBean>>() {
        }.getType();
        itemList = complexPreferences.getArray(Constant.ProductListObj, typeOutlet) == null ? new ArrayList<>() : (ArrayList<TakeOutletOrderListBean>) complexPreferences.getArray(Constant.ProductListObj, typeOutlet);
        if (itemList != null && itemList.size() > 0) {

            if (outletOrdersBean != null) {

                for (int j = 0; j < outletOrdersBean.getProductList().size(); j++) {
                    if (itemList.get(position).getId() != null && itemList.get(position).getId().equals(outletOrdersBean.getProductList().get(j).getId())) {
                        TakeOutletOrderListBean takeOutletOrderListBean = outletOrdersBean.getProductList().get(j);
                        for (int i = 0; i < takeOutletOrderListBean.getArryItemList().size(); i++) {
                            for (int k = 0; k < itemList.get(position).getArryItemList().size(); k++) {
                                if (itemList.get(position).getArryItemList().get(k).getProduct_id() != null && itemList.get(position).getArryItemList().get(k).getProduct_id().equals(takeOutletOrderListBean.getArryItemList().get(i).getProduct_id())) {
                                    itemList.get(position).getArryItemList().set(k, takeOutletOrderListBean.getArryItemList().get(i));
                                    itemList.get(position).setOrder_uni_id(outletOrdersBean.getOrder_unique_id());

                                }
                            }
                        }
                    }
                }
            } else if (getIntent().hasExtra("from")) {

                Collections.sort(productList, (lhs, rhs) -> lhs.getProduct_id().compareTo(rhs.getProduct_id()));
                for (int j = 0; j < productList.size(); j++) {
                    if (itemList.get(position).getId() != null && itemList.get(position).getId().equals(productList.get(j).getCat_id())) {
                        for (int k = 0; k < itemList.get(position).getArryItemList().size(); k++) {
                            if (itemList.get(position).getArryItemList().get(k).getProduct_id() != null && itemList.get(position).getArryItemList().get(k).getProduct_id().equals(productList.get(j).getProduct_id())) {
                                if (itemList.get(position).getArryItemList().get(k).getProduct_id() != null && itemList.get(position).getArryItemList().get(k).getProduct_id().equals(productList.get(j).getProduct_id())) {
                                    itemList.get(position).getArryItemList().get(k).setProduct_qty(productList.get(j).getProduct_qty());

                                } else {
                                    itemList.get(position).getArryItemList().get(k).setProduct_qty("0");

                                }
                                itemList.get(position).setOrder_uni_id(saveListUniqueorderId);

                            }
                        }

                    }
                }
            }

            mTakeOrderItemListAdapter = new TakeOrderItemListAdapter(EditTakenOrder.this, itemList.get(position), position, "EditTakenOrder");
            lvItemList.setAdapter(mTakeOrderItemListAdapter);
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class mgetItemList extends AsyncTask<String, Void, JSONObject> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(EditTakenOrder.this);
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
                            ArrayList<TakeOutletOrderItemBean> arryItemList = new ArrayList<>();
                            for (int j = 0; j < itmArry.length(); j++) {
                                JSONObject itmObj = itmArry.getJSONObject(j);
                                String product_id = itmObj.getString("product_id");
                                String product_mrp = itmObj.getString("product_mrp");
                                String sku_code = itmObj.getString("sku_code");
                                String product_name = itmObj.getString("product_name");
                                arryItemList.add(new TakeOutletOrderItemBean(product_id, "0", product_name, product_mrp, sku_code, false));
                            }
                            itemList.add(new TakeOutletOrderListBean(id, cat_name, arryItemList, outletId));
                        }
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(EditTakenOrder.this, Constant.ProductListPref, MODE_PRIVATE);
                        complexPreferences.putObject(Constant.ProductListObj, itemList);
                        complexPreferences.commit();
                        prepareListData(itemList, false);


                    } else {
                        Toast.makeText(EditTakenOrder.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(EditTakenOrder.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mSaveData extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(EditTakenOrder.this);
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
            mSaveButton.setEnabled(true);

            if (jsonObject != null) {
                if (mDialog.isShowing()) mDialog.dismiss();
                ComplexPreferences mComplexPreferences = ComplexPreferences.getComplexPreferences(EditTakenOrder.this, Constant.EditedOrderProductListPref, MODE_PRIVATE);
                mComplexPreferences.clear(Constant.EditedOrderProductListPref);
                if (getIntent().hasExtra("from")) {

                    db.deleteEditSaveRecordsAfterSubmit(distributerId, outletId);
                    db.deleteEditSaveProductRecordsAfterSubmit(distributerId, outletId);

                }
                //order is not placed =0
                //stock not taken =1
             /*   ArrayList<StockPlaced> stockPlacedArrayList = db.getAllisOrderStockPlaced(mUserid);
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
                    //order is not placed placed =1
                    //stock not taken =0
                    stockAddOrUpdate("add");
                }

*/
                try {
                    Toast.makeText(EditTakenOrder.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                EditTakenOrder.this.finish();

            } else {
                if (mDialog.isShowing()) mDialog.dismiss();
                Toast.makeText(EditTakenOrder.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }

        }
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
                    mTakeOrderItemListAdapter.getFilter().filter(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (mTakeOrderItemListAdapter != null) {
                        mTakeOrderItemListAdapter.getFilter().filter(s);
                    }
                    return true;
                }
            });

        }
        return true;

    }
}