package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.google.gson.Gson;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.OutletOrderDateBean;
import com.malas.appsr.malasapp.BeanClasses.OutletOrdersBean;
import com.malas.appsr.malasapp.BeanClasses.SaveData;
import com.malas.appsr.malasapp.BeanClasses.ShowOutLetBeen;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderItemBean;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderListBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.ShowOutletOrderAdapter;
import com.malas.appsr.malasapp.dbHandler.DatabaseHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OutletOrdersActivity extends AppCompatActivity {
    /**
     * Object Declaration
     */
    ListView list_view_show_orders;
    TextView tvDistributorName;
    TextView tvRouteName;
    ImageView iv_add;
    ShowOutLetBeen showOutLetBeen;
    DistributerBean distributerBean;
    AsyncTask<String, Void, JSONObject> getOutletOrdersList;
    ArrayList<OutletOrdersBean> listOutletOrders = new ArrayList<>();
    ArrayList<TakeOutletOrderListBean> itemList;
    ShowOutletOrderAdapter showOutletOrderAdapter;
    String firmName = "";
    String is_order_placed = "";

    LinearLayout llSync;
    Button btnSync;
    double latitude = 0;
    double longitude = 0;

    ComplexPreferences mUserPreference;
    UserLoginInfoBean mUserLoginInfoBean;
    String is_stock_taken;
    private DatabaseHandler db;
    private TextView tvEmptyList;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_orders);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Order History");

        // Object Defined

        list_view_show_orders = findViewById(R.id.list_view_show_orders);
        tvDistributorName = findViewById(R.id.tvDistributorName);
        tvRouteName = findViewById(R.id.tvRouteName);
        iv_add = findViewById(R.id.iv_add);
        llSync = findViewById(R.id.ll_sync);
        btnSync = findViewById(R.id.btn_sync);
        tvEmptyList = findViewById(R.id.tv_empty_list);

        db = new DatabaseHandler(this);

        // Getting Message from Previous Activity

        if (getIntent().hasExtra("outletBean")) {
            showOutLetBeen = (ShowOutLetBeen) getIntent().getSerializableExtra("outletBean");
            firmName = showOutLetBeen.getOutlet_name().toUpperCase(Locale.getDefault());
        }

        if (getIntent().hasExtra("is_stock_taken")) {
            is_stock_taken = getIntent().getStringExtra("is_stock_taken");
        }

        if (getIntent().hasExtra("distributerBean")) {
            distributerBean = (DistributerBean) getIntent().getSerializableExtra("distributerBean");

        }

        // Refreshing Offline Tables
        offlineAddData();
        offlineEditData();

        // Getting Data from Preferences
        mUserPreference = ComplexPreferences.getComplexPreferences(OutletOrdersActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = mUserPreference.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

        //getCurrentLocation();

        // Setting Distributor Name & Route name
        tvDistributorName.setText(firmName);
        tvRouteName.setText(showOutLetBeen.getRoute_name().toUpperCase(Locale.getDefault()));

        // Place an Order
        iv_add.setOnClickListener(view -> {
            /*  if (Utils.isInternetConnected(OutletOrdersActivity.this)) {*/


            // Temporary Object Declaration
            ArrayList<OutletOrdersBean> ordersBeans = db.getOrdersData(showOutLetBeen.getOutlet_id());
            ArrayList<TakeOutletOrderItemBean> productList;
            ArrayList<SaveData> saveListFromDb;
            ArrayList<TakeOutletOrderItemBean> productListEdit;
            ArrayList<SaveData> saveListFromDbEdit;

            // Saving Data From Local Database in Object
            saveListFromDb = db.getAllSaveDataRecord(distributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());
            productList = db.getSaveProductRecord(distributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());


            // Saving Data From Local Database in Object
            saveListFromDbEdit = db.getEditSaveDataRecord(distributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());
            productListEdit = db.getEditSaveProductRecord(distributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());


            if (ordersBeans.size() > 0) {
                if (saveListFromDb != null && productList != null && saveListFromDb.size() > 0 && productList.size() > 0) {
                    Toast.makeText(OutletOrdersActivity.this, "Please Connect to Internet And SYNC Order data ", Toast.LENGTH_SHORT).show();

                } else if (saveListFromDbEdit != null && productListEdit != null && saveListFromDbEdit.size() > 0 && productListEdit.size() > 0) {
                    Toast.makeText(OutletOrdersActivity.this, "Please Connect to Internet And SYNC Order data ", Toast.LENGTH_SHORT).show();
                } else {
                  /*  StockPlaced stockPlaced = db.isOrderOtletWisePlaced(distributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id(), showOutLetBeen.getOutlet_id());


                    if (stockPlaced != null) {
                        if (stockPlaced.getIs_order_placed() != null) {
                            is_order_placed = stockPlaced.getIs_order_placed();
                        }

                    }

                    StockPlaced stockPlacedisStock = db.isOrderStockPlaced(distributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id());
                    if (stockPlacedisStock != null) {
                        if (stockPlacedisStock.getIs_order_placed() != null) {
                            is_stock_taken = stockPlacedisStock.getStock_placed();
                        }

                    }
                    *//* }*/
                    /*

                    if (is_stock_taken != null) {
                        switch (is_stock_taken) {
                            case "0":

                                if (is_order_placed != null || !is_order_placed.equals("")) {
                                    switch (is_order_placed) {
                                        case "1":*/
                    if (listOutletOrders != null) {
                        if (listOutletOrders.size() > 0) {
                            if (differenceInTwoDates(listOutletOrders.get(0).getOrder_take_time())) {
                                Intent intent = new Intent(OutletOrdersActivity.this, AddTakeOrder.class);
                                intent.putExtra("showOutLetBeen", showOutLetBeen);
                                intent.putExtra("distributerBean", distributerBean);
                                startActivity(intent);
                            } else {
                                Toast.makeText(OutletOrdersActivity.this, "You have already punched the order for this outlet.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Intent intent = new Intent(OutletOrdersActivity.this, AddTakeOrder.class);
                            intent.putExtra("showOutLetBeen", showOutLetBeen);
                            intent.putExtra("distributerBean", distributerBean);
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(OutletOrdersActivity.this, AddTakeOrder.class);
                        intent.putExtra("showOutLetBeen", showOutLetBeen);
                        intent.putExtra("distributerBean", distributerBean);
                        startActivity(intent);
                    }

                                          /*  break;
                                        case "":

                                            Toast.makeText(OutletOrdersActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();


                                            break;
                                        default:
                                            Toast.makeText(OutletOrdersActivity.this, "You can add only one order before placing the order.", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                } else {
                                    Toast.makeText(OutletOrdersActivity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();

                                }
                                break;
                            case "":
                                Toast.makeText(OutletOrdersActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

                                break;
                            default:
                                Toast.makeText(OutletOrdersActivity.this, "Please Take Stock First", Toast.LENGTH_SHORT).show();
                                break;
                        }*/
                    /*} else {
                        Toast.makeText(OutletOrdersActivity.this, "Please Check your Internet Connection or Take Stock Taken", Toast.LENGTH_SHORT).show();

                    }*/
                }
            } else {

                if (saveListFromDb != null && productList != null && saveListFromDb.size() > 0 && productList.size() > 0) {
                    Toast.makeText(OutletOrdersActivity.this, "Please Connect to Internet And SYNC Order data ", Toast.LENGTH_SHORT).show();

                } else if (saveListFromDbEdit != null && productListEdit != null && saveListFromDbEdit.size() > 0 && productListEdit.size() > 0) {
                    Toast.makeText(OutletOrdersActivity.this, "Please Connect to Internet And SYNC Order data ", Toast.LENGTH_SHORT).show();
                } else {

                /*    StockPlaced stockPlaced = db.isOrderOtletWisePlaced(distributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id(), showOutLetBeen.getOutlet_id());


                    if (stockPlaced != null) {
                        if (stockPlaced.getIs_order_placed() != null) {
                            is_order_placed = stockPlaced.getIs_order_placed();
                        }

                    }

                    StockPlaced stockPlacedisStock = db.isOrderStockPlaced(distributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id());
                    if (stockPlacedisStock != null) {
                        if (stockPlacedisStock.getIs_order_placed() != null) {
                            is_stock_taken = stockPlacedisStock.getStock_placed();
                        }

                    }
                    *//*}*/
                    /*

                    if (is_stock_taken != null) {
                        switch (is_stock_taken) {
                            case "0":

                                if (is_order_placed != null) {
                                    switch (is_order_placed) {
                                        case "1":*/
                    if (listOutletOrders != null) {
                        if (listOutletOrders.size() > 0) {
                            if (differenceInTwoDates(listOutletOrders.get(0).getOrder_take_time())) {

                                Intent intent = new Intent(OutletOrdersActivity.this, AddTakeOrder.class);
                                intent.putExtra("showOutLetBeen", showOutLetBeen);
                                intent.putExtra("distributerBean", distributerBean);
                                startActivity(intent);
                            } else {
                                Toast.makeText(OutletOrdersActivity.this, "You have already punched the order for this outlet.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Intent intent = new Intent(OutletOrdersActivity.this, AddTakeOrder.class);
                            intent.putExtra("showOutLetBeen", showOutLetBeen);
                            intent.putExtra("distributerBean", distributerBean);
                            startActivity(intent);
                        }

                    } else {
                        Intent intent = new Intent(OutletOrdersActivity.this, AddTakeOrder.class);
                        intent.putExtra("showOutLetBeen", showOutLetBeen);
                        intent.putExtra("distributerBean", distributerBean);
                        startActivity(intent);
                    }
                                            /*break;
                                        case "":

                                            Toast.makeText(OutletOrdersActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();


                                            break;
                                        default:
                                            Toast.makeText(OutletOrdersActivity.this, "You can add only one order before placing the order.", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                } else {
                                    Toast.makeText(OutletOrdersActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

                                }
                                break;
                            case "":
                                Toast.makeText(OutletOrdersActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

                                break;
                            default:
                                Toast.makeText(OutletOrdersActivity.this, "Please Take Stock First", Toast.LENGTH_SHORT).show();
                                break;
                        }*/
                 /*   } else {
                        Toast.makeText(OutletOrdersActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

                    }*/

                }
                offlineAddData();
                offlineEditData();

            }

        });

        if (listOutletOrders == null) {
            listOutletOrders = new ArrayList<>();
        } else {
            Collections.sort(listOutletOrders, new OutletOrdersBean.OrderByTimeStampComparator());
            if (listOutletOrders.size() > 0) {
                if (listOutletOrders.size() > 5) {
                    ArrayList<OutletOrdersBean> listOutletOrderstemp = new ArrayList<>();

                    for (int i = 0; i < 5; i++) {
                        listOutletOrderstemp.add(listOutletOrders.get(i));
                    }
                    listOutletOrders.clear();
                    listOutletOrders = listOutletOrderstemp;

                }

            }
        }


        list_view_show_orders.setVisibility(View.VISIBLE);
        tvEmptyList.setVisibility(View.GONE);
        showOutletOrderAdapter = new

                ShowOutletOrderAdapter(OutletOrdersActivity.this, listOutletOrders, showOutLetBeen, distributerBean);
        list_view_show_orders.setAdapter(showOutletOrderAdapter);


        list_view_show_orders.setOnItemClickListener((adapterView, view, position, l) -> {

            if (!listOutletOrders.isEmpty() && position < listOutletOrders.size()) {
                list_view_show_orders.setVisibility(View.VISIBLE);
                tvEmptyList.setVisibility(View.GONE);
                ArrayList<TakeOutletOrderListBean> stockList = listOutletOrders.get(position).getProductList();

                startActivity(new Intent(OutletOrdersActivity.this, ViewTakenOrder.class).putExtra("list", new Gson().toJson(stockList)).putExtra("outletBean", showOutLetBeen));
            } else {
                list_view_show_orders.setVisibility(View.GONE);
                tvEmptyList.setVisibility(View.VISIBLE);
                tvEmptyList.setText("Item not found please try again");
                //  Toast.makeText(OutletOrdersActivity.this, "Item not found please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void offlineEditData() {

        ArrayList<SaveData> saveAddData, saveEditData;
        ArrayList<TakeOutletOrderItemBean> Addproduct, editProductList;
        saveAddData = db.getAllSaveDataRecord(distributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());
        Addproduct = db.getSaveProductRecord(distributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());
        if (saveAddData != null && Addproduct != null) {
            if (saveAddData.size() == 0 && Addproduct.size() == 0) {
                saveEditData = db.getEditSaveDataRecord(distributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());
                editProductList = db.getEditSaveProductRecord(distributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());
                if (saveEditData != null && editProductList != null) {
                    if (saveEditData.size() > 0 && editProductList.size() > 0) {
                        llSync.setVisibility(View.VISIBLE);
                        btnSync.setOnClickListener(v -> {
                            Intent intent = new Intent(OutletOrdersActivity.this, OfflineOrderData.class);
                            intent.putExtra("distributorId", distributerBean.getDistribution_id());
                            intent.putExtra("outletId", showOutLetBeen.getOutlet_id());
                            intent.putExtra("routeName", showOutLetBeen.getRoute_name());
                            intent.putExtra("distributorName", distributerBean.getFirm_name());
                            intent.putExtra("From", "EditRecord");
                            startActivity(intent);
                        });

                    } else {
                        llSync.setVisibility(View.GONE);
                    }
                } else {
                    llSync.setVisibility(View.GONE);
                }
            }


        }
    }

    private void offlineAddData() {
        ArrayList<SaveData> saveList;
        ArrayList<TakeOutletOrderItemBean> productRecord;

        saveList = db.getAllSaveDataRecord(distributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());
        productRecord = db.getSaveProductRecord(distributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());

        if (saveList != null && productRecord != null) {
            if (saveList.size() > 0 && productRecord.size() > 0) {
                llSync.setVisibility(View.VISIBLE);
                btnSync.setOnClickListener(v -> {
                    Intent intent = new Intent(OutletOrdersActivity.this, OfflineOrderData.class);
                    intent.putExtra("distributorId", distributerBean.getDistribution_id());
                    intent.putExtra("outletId", showOutLetBeen.getOutlet_id());
                    intent.putExtra("From", "AddRecord");
                    intent.putExtra("distributorName", distributerBean.getFirm_name());
                    intent.putExtra("routeName", showOutLetBeen.getRoute_name());

                    startActivity(intent);
                });

            } else {
                llSync.setVisibility(View.GONE);
            }
        } else {
            llSync.setVisibility(View.GONE);
        }

    }

    /**
     * On Activity Resume
     */

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isInternetConnected(OutletOrdersActivity.this)) {
            if (showOutLetBeen != null) {
                offlineAddData();
                offlineEditData();
                getOutletOrdersList = new Getalloutletorderlist().execute();
            }
        } else {
            listOutletOrders.clear();
            ArrayList<OutletOrdersBean> ordersBeans = db.getOrdersData(showOutLetBeen.getOutlet_id());
            if (ordersBeans.size() > 0) {
                for (int i = 0; i < ordersBeans.size(); i++) {

                    String order_uni_id = ordersBeans.get(i).getOrder_unique_id();
                    String order_time = ordersBeans.get(i).getOrder_take_time();
                    String order_time_in_long = ordersBeans.get(i).getOrder_time_in_long();
                    is_order_placed = ordersBeans.get(i).getIs_order_placed();

                    String distributorId = ordersBeans.get(i).getDistributorId();
                    String outletId = ordersBeans.get(i).getOutletId();

                    String withSso = ordersBeans.get(i).getWithSso();
                    String withAsm = ordersBeans.get(i).getWithAsm();

                    itemList = new ArrayList<>();
                    ArrayList<TakeOutletOrderListBean> category = db.getCategoryRecord(showOutLetBeen.getOutlet_id(), order_uni_id);
                    for (int j = 0; j < category.size(); j++) {
                        ArrayList<TakeOutletOrderItemBean> productRecord = db.getProductRecord(showOutLetBeen.getOutlet_id(), order_uni_id);
                        ArrayList<TakeOutletOrderItemBean> arryItemList = new ArrayList<>();


                        String cat_id = productRecord.get(j).getCat_id();
                        String product_id = productRecord.get(j).getProduct_id();
                        String product_qty = productRecord.get(j).getProduct_qty();
                        String cat_name = productRecord.get(j).getCatName();
                        String product_name = productRecord.get(j).getProduct_name();
                        String sku_code = productRecord.get(j).getSku_code();
                        String product_mrp = productRecord.get(j).getProduct_mrp();

                        arryItemList.add(new TakeOutletOrderItemBean(product_id, product_qty, product_name, product_mrp, sku_code, false));

                        if (itemList.isEmpty()) {
                            itemList.add(new TakeOutletOrderListBean(cat_id, cat_name, arryItemList, showOutLetBeen.getOutlet_id()));
                        } else {
                            boolean needtoAddnewItem = true;
                            for (int k = 0; k < itemList.size(); k++) {
                                if (itemList.get(k).getId().equalsIgnoreCase(cat_id)) {
                                    needtoAddnewItem = false;
                                    arryItemList.addAll(itemList.get(k).getArryItemList());
                                    itemList.get(k).setArryItemList(arryItemList);
                                }
                            }
                            if (needtoAddnewItem) {
                                itemList.add(new TakeOutletOrderListBean(cat_id, cat_name, arryItemList, showOutLetBeen.getOutlet_id()));
                            }
                        }
                    }


                    listOutletOrders.add(new OutletOrdersBean(order_uni_id, order_time, is_order_placed, withSso, withAsm, itemList, order_time_in_long, distributorId, outletId));

                }

                Collections.sort(listOutletOrders, Collections.reverseOrder(new OutletOrdersBean.OrderByTimeStampComparator()));
                list_view_show_orders.setVisibility(View.VISIBLE);
                tvEmptyList.setVisibility(View.GONE);
                if (listOutletOrders.size() > 0) {
                    if (listOutletOrders.size() > 5) {
                        ArrayList<OutletOrdersBean> listOutletOrderstemp = new ArrayList<>();

                        for (int i = 0; i < 5; i++) {
                            listOutletOrderstemp.add(listOutletOrders.get(i));
                        }
                        listOutletOrders.clear();
                        listOutletOrders = listOutletOrderstemp;

                    }

                }
                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(OutletOrdersActivity.this, Constant.SHOW_OUTLET_ORDER_PREF, MODE_PRIVATE);
                complexPreferences.putObject(Constant.SHOW_OUTLET_ORDER_OBJ, listOutletOrders);
                complexPreferences.commit();
               /* if (listOutletOrders.get(0).getIs_order_placed() != null)
                    is_order_placed = listOutletOrders.get(0).getIs_order_placed();

                ArrayList<StockPlaced> allStockRecord = db.getAllisOrderPlacedOutletWise();
                ArrayList<String> outletIdLIST = new ArrayList<>();

                for (int i = 0; i < allStockRecord.size(); i++) {
                    outletIdLIST.add(allStockRecord.get(i).getOutletId());
                }

                if (allStockRecord.size() > 0) {
                    if (outletIdLIST.contains(showOutLetBeen.getOutlet_id())) {

                        StockPlaced stockPlaced = new StockPlaced();
                        stockPlaced.setDistributorId(distributerBean.getDistribution_id());
                        stockPlaced.setUserId(mUserLoginInfoBean.getUser_id());
                        stockPlaced.setOutletId(showOutLetBeen.getOutlet_id());
                        stockPlaced.setIs_order_placed(is_order_placed);
                        db.updateIsOrderPlacedOutletWise(stockPlaced);
                        //  Log.d("Name: ", update + "");

                    } else {
                        db.addOrderPlacedOutletWise(distributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id(), showOutLetBeen.getOutlet_id(), is_order_placed);

                    }
                } else {
                    db.addOrderPlacedOutletWise(distributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id(), showOutLetBeen.getOutlet_id(), is_order_placed);

                }


                showOutletOrderAdapter = new ShowOutletOrderAdapter(OutletOrdersActivity.this, listOutletOrders, showOutLetBeen, distributerBean);
                list_view_show_orders.setAdapter(showOutletOrderAdapter);
*/
                //    Toast.makeText(OutletOrdersActivity.this, "DATA SAVED OFFLINE", Toast.LENGTH_SHORT).show();


            } else {
                String outletIdOrderNotFound = db.getOutletNotFound(showOutLetBeen.getOutlet_id());
                if (!outletIdOrderNotFound.equals("")) {

                    list_view_show_orders.setVisibility(View.GONE);
                    tvEmptyList.setVisibility(View.VISIBLE);
                    tvEmptyList.setText("No Orders Found for this Outlet");
                    //  Utils.showAlertDialog(OutletOrdersActivity.this, "Outlet Order", "No Orders Found for this Outlet", true);
                   /* ArrayList<StockPlaced> allStockRecord = db.getAllisOrderPlacedOutletWise();
                    ArrayList<String> outletIdLIST = new ArrayList<>();
                    for (int i = 0; i < allStockRecord.size(); i++) {
                        outletIdLIST.add(allStockRecord.get(i).getOutletId());
                    }
                    if (allStockRecord.size() > 0) {
                        if (outletIdLIST.contains(showOutLetBeen.getOutlet_id())) {

                            StockPlaced stockPlaced = new StockPlaced();
                            stockPlaced.setDistributorId(distributerBean.getDistribution_id());
                            stockPlaced.setUserId(mUserLoginInfoBean.getUser_id());
                            stockPlaced.setOutletId(showOutLetBeen.getOutlet_id());
                            stockPlaced.setIs_order_placed("1");
                            db.updateIsOrderPlacedOutletWise(stockPlaced);
                            //  Log.d("Name: ", update + "");

                        } else {
                            db.addOrderPlacedOutletWise(distributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id(), showOutLetBeen.getOutlet_id(), "1");

                        }
                    } else {
                        db.addOrderPlacedOutletWise(distributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id(), showOutLetBeen.getOutlet_id(), "1");

                    }*/
                } else {
                    list_view_show_orders.setVisibility(View.GONE);
                    tvEmptyList.setVisibility(View.VISIBLE);
                    tvEmptyList.setText("Check Your Internet Connection");
                    //   Toast.makeText(OutletOrdersActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
            offlineAddData();
            offlineEditData();
        }
    }

    /**
     * On Activity Pause
     */
    @Override
    protected void onPause() {
        super.onPause();
       /* if (getOutletOrdersList != null && !getOutletOrdersList.isCancelled())
            getOutletOrdersList.cancel(true);*/
    }

    /**
     * Convert Date into Strings
     */
    public String convertIntoDateTime(long datetime) {
        // Log.e("order_take_time ", "" + datetime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datetime * 1000L);
        Date date = calendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault());
        //Log.e("order_take_time", "" + date);
        return simpleDateFormat.format(date);
    }

    /**
     * Converting String into Dates
     */
    private Date convertStringToDate(String dateString) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Date date = null;
        try {
            date = format.parse(dateString);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Get Current Date
     */
    private String getCurrentDate() {

        return new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault()).format(new Date());
    }


    /**
     * Get All Order History of Selected Outlet
     */
    @SuppressLint("StaticFieldLeak")
    public class Getalloutletorderlist extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(OutletOrdersActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getorderlistbyoutlet"));
            nameValuePair.add(new BasicNameValuePair("outlet_id", showOutLetBeen.getOutlet_id()));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {

                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ArrayList<OutletOrdersBean> outletOrdersBean = db.getAllOrderRecord();

                        ArrayList<String> outletId = new ArrayList<>();
                        for (int k = 0; k < outletOrdersBean.size(); k++) {
                            outletId.add(outletOrdersBean.get(k).getOutletId());
                        }
                        if (outletId.contains(showOutLetBeen.getOutlet_id())) {
                            db.deleteAllOrderRecords(showOutLetBeen.getOutlet_id());
                            db.deleteCategoryRecords(showOutLetBeen.getOutlet_id());
                            db.deleteProductRecords(showOutLetBeen.getOutlet_id());
                        }


                        String outletIdOrderNotFound = db.getOutletNotFound(showOutLetBeen.getOutlet_id());

                        if (!outletIdOrderNotFound.equals("")) {
                            db.deleteNoOrderFound(outletIdOrderNotFound);

                        }
                        JSONArray jArray = jsonObject.getJSONArray("list");
                        listOutletOrders = new ArrayList<>();
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jobj = jArray.getJSONObject(i);
                            String order_uni_id = jobj.getString("order_unique_id");
                            String order_time = convertIntoDateTime(Long.parseLong(jobj.getString("order_take_time")));
                            String order_time_in_long = jobj.getString("order_take_time");
                            is_order_placed = jobj.getString("is_order_placed");
                            JSONArray itemarry = jobj.getJSONArray("items");

                            String withSso = jobj.getString("withsso");
                            String withAsm = jobj.getString("withasm");

                            itemList = new ArrayList<>();

                            for (int j = 0; j < itemarry.length(); j++) {
                                ArrayList<TakeOutletOrderItemBean> arryItemList = new ArrayList<>();

                                JSONObject itmnobj = itemarry.getJSONObject(j);
                                String cat_id = itmnobj.getString("cat_id");
                                String product_id = itmnobj.getString("product_id");
                                String product_qty = itmnobj.getString("product_quantity");
                                String cat_name = itmnobj.getString("cat_name");
                                String product_name = itmnobj.getString("product_name");
                                String sku_code = itmnobj.getString("sku_code");
                                String product_mrp = itmnobj.getString("product_mrp");

                                arryItemList.add(new TakeOutletOrderItemBean(product_id, product_qty, product_name, product_mrp, sku_code, false));
                                db.addProduct(new TakeOutletOrderItemBean(product_id, product_qty, product_name, product_mrp, sku_code, showOutLetBeen.getOutlet_id(), cat_id, order_uni_id, cat_name));

                               /* ArrayList<TakeOutletOrderItemBean> takeOutletOrderItemBeans = db.getProductRecord();
                                for (int k = 0; k < takeOutletOrderItemBeans.size(); k++) {
                                    Log.d("PRODUCT", takeOutletOrderItemBeans.get(i).getOutlet_id() + " " + takeOutletOrderItemBeans.get(i).getProduct_name());
                                }*/
                                db.addCategory(new TakeOutletOrderListBean(cat_id, cat_name, order_uni_id, showOutLetBeen.getOutlet_id()));
                                /*ArrayList<TakeOutletOrderListBean> orderListBeans = db.getAllCategoryRecord();
                                for (int k = 0; k < orderListBeans.size(); k++) {
                                    Log.d("CATEGORY", orderListBeans.get(i).getOutlet_id() + " " + orderListBeans.get(i).getItem());
                                }*/

                                if (itemList.isEmpty()) {
                                    itemList.add(new TakeOutletOrderListBean(cat_id, cat_name, arryItemList, showOutLetBeen.getOutlet_id()));
                                } else {
                                    boolean needtoAddnewItem = true;
                                    for (int k = 0; k < itemList.size(); k++) {
                                        if (itemList.get(k).getId().equalsIgnoreCase(cat_id)) {
                                            needtoAddnewItem = false;
                                            arryItemList.addAll(itemList.get(k).getArryItemList());
                                            itemList.get(k).setArryItemList(arryItemList);
                                        }
                                    }
                                    if (needtoAddnewItem) {
                                        itemList.add(new TakeOutletOrderListBean(cat_id, cat_name, arryItemList, showOutLetBeen.getOutlet_id()));
                                    }
                                }
                            }
                            //   db.addOrders(distributerBean.getDistribution_id(), (new OutletOrdersBean(order_uni_id, order_time, is_order_placed, withSso, withAsm, order_time_in_long, showOutLetBeen.getOutlet_id())));
                          /*  ArrayList<OutletOrdersBean> outletOrdersBeans = db.getAllOrderRecord();
                            for (int k = 0; k < outletOrdersBeans.size(); k++) {
                                Log.d("ORDERS", outletOrdersBeans.get(i).getOutletId() + " " + outletOrdersBeans.get(i).getOrder_unique_id());
                            }*/

                            listOutletOrders.add(new OutletOrdersBean(order_uni_id, order_time, is_order_placed, withSso, withAsm, itemList, order_time_in_long, distributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id()));
                        }


                        Collections.sort(listOutletOrders, Collections.reverseOrder(new OutletOrdersBean.OrderByTimeStampComparator()));

                        list_view_show_orders.setVisibility(View.VISIBLE);
                        tvEmptyList.setVisibility(View.GONE);
                        if (listOutletOrders.size() > 0) {
                            if (listOutletOrders.size() > 5) {
                                ArrayList<OutletOrdersBean> listOutletOrderstemp = new ArrayList<>();

                                for (int i = 0; i < 5; i++) {
                                    listOutletOrderstemp.add(listOutletOrders.get(i));
                                }
                                listOutletOrders.clear();
                                listOutletOrders = listOutletOrderstemp;

                            }

                        }

                        for (int i = 0; i < listOutletOrders.size(); i++) {
                            db.addOrders(distributerBean.getDistribution_id(), (new OutletOrdersBean(listOutletOrders.get(i).getOrder_unique_id(), listOutletOrders.get(i).getOrder_take_time(), listOutletOrders.get(i).getIs_order_placed(), listOutletOrders.get(i).getWithSso(), listOutletOrders.get(i).getWithAsm(), listOutletOrders.get(i).getOrder_time_in_long(), showOutLetBeen.getOutlet_id())));

                        }
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(OutletOrdersActivity.this, Constant.SHOW_OUTLET_ORDER_PREF, MODE_PRIVATE);
                        complexPreferences.putObject(Constant.SHOW_OUTLET_ORDER_OBJ, listOutletOrders);
                        complexPreferences.commit();
                        //SET IN DB IS ORDER PLACE
                       /* if (listOutletOrders.get(0).getIs_order_placed() != null)
                            is_order_placed = listOutletOrders.get(0).getIs_order_placed();

                        ArrayList<StockPlaced> allStockRecord = db.getAllisOrderPlacedOutletWise();
                        ArrayList<String> outletIdLIST = new ArrayList<>();
                        for (int i = 0; i < allStockRecord.size(); i++) {
                            outletIdLIST.add(allStockRecord.get(i).getOutletId());
                        }
                        if (allStockRecord.size() > 0) {
                            if (outletIdLIST.contains(showOutLetBeen.getOutlet_id())) {

                                StockPlaced stockPlaced = new StockPlaced();
                                stockPlaced.setDistributorId(distributerBean.getDistribution_id());
                                stockPlaced.setUserId(mUserLoginInfoBean.getUser_id());
                                stockPlaced.setOutletId(showOutLetBeen.getOutlet_id());
                                stockPlaced.setIs_order_placed(is_order_placed);
                                db.updateIsOrderPlacedOutletWise(stockPlaced);
                                //  Log.d("Name: ", update + "");

                            } else {
                                db.addOrderPlacedOutletWise(distributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id(), showOutLetBeen.getOutlet_id(), is_order_placed);

                            }
                        } else {
                            db.addOrderPlacedOutletWise(distributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id(), showOutLetBeen.getOutlet_id(), is_order_placed);

                        }
*/
                        String getOrderDate = listOutletOrders.get(0).getOrder_take_time();
                        String currentDate = getCurrentDate();


                        String currentMonth = (String) DateFormat.format("MM", convertStringToDate(currentDate));
                        String currentYear = (String) DateFormat.format("yyyy", convertStringToDate(currentDate));
                        String orderedMonth = (String) DateFormat.format("MM", convertStringToDate(getOrderDate));
                        String orderedYear = (String) DateFormat.format("yyyy", convertStringToDate(getOrderDate));
                        String highlightornot;
                        String day = (String) DateFormat.format("dd", convertStringToDate(currentDate)); // 20
                        if (day.equals("1")) {
                            if (currentMonth.equals(orderedMonth) && currentYear.equals(orderedYear)) {
                                highlightornot = "false";

                            } else {
                                highlightornot = "true";

                            }


                        } else if (currentMonth.equals(orderedMonth) && currentYear.equals(orderedYear)) {
                            highlightornot = "false";


                        } else {
                            highlightornot = "true";

                        }


                        ArrayList<OutletOrderDateBean> outletOrderDateBeans = db.getAllOutletOrderDate();
                        ArrayList<String> outletIdList = new ArrayList<>();
                        if (outletOrderDateBeans.size() > 0) {
                            for (int i = 0; i < outletOrderDateBeans.size(); i++) {
                                outletIdList.add(outletOrderDateBeans.get(i).getOutletId());
                            }

                            if (outletIdList.contains(jsonObject.getString("outletId"))) {
                                OutletOrderDateBean outletOrderDateBean = new OutletOrderDateBean(distributerBean.getDistribution_id(), showOutLetBeen.getRoute_id(), jsonObject.getString("outletId"), listOutletOrders.get(0).getOrder_take_time(), highlightornot);

                                db.updateOutletOrderDate(outletOrderDateBean);
                            } else {
                                OutletOrderDateBean outletOrderDateBean = new OutletOrderDateBean(distributerBean.getDistribution_id(), showOutLetBeen.getRoute_id(), jsonObject.getString("outletId"), listOutletOrders.get(0).getOrder_take_time(), highlightornot);

                                db.addOutletOrderDate(outletOrderDateBean);


                            }
                        } else {
                            OutletOrderDateBean outletOrderDateBean = new OutletOrderDateBean(distributerBean.getDistribution_id(), showOutLetBeen.getRoute_id(), jsonObject.getString("outletId"), listOutletOrders.get(0).getOrder_take_time(), highlightornot);

                            db.addOutletOrderDate(outletOrderDateBean);
                        }


                        showOutletOrderAdapter = new ShowOutletOrderAdapter(OutletOrdersActivity.this, listOutletOrders, showOutLetBeen, distributerBean);
                        list_view_show_orders.setAdapter(showOutletOrderAdapter);

                    } else {
                        list_view_show_orders.setVisibility(View.GONE);
                        tvEmptyList.setVisibility(View.VISIBLE);
                        tvEmptyList.setText(jsonObject.getString("message"));
                        // Utils.showAlertDialog(OutletOrdersActivity.this, "Outlet Order", jsonObject.getString("message"), true);
                        db.addOutletNoOrder(showOutLetBeen.getOutlet_id());
                     /*   ArrayList<StockPlaced> allStockRecord = db.getAllisOrderPlacedOutletWise();
                        ArrayList<String> outletIdLIST = new ArrayList<>();
                        for (int i = 0; i < allStockRecord.size(); i++) {
                            outletIdLIST.add(allStockRecord.get(i).getOutletId());
                        }
                        if (allStockRecord.size() > 0) {
                            if (outletIdLIST.contains(showOutLetBeen.getOutlet_id())) {

                                StockPlaced stockPlaced = new StockPlaced();
                                stockPlaced.setDistributorId(distributerBean.getDistribution_id());
                                stockPlaced.setUserId(mUserLoginInfoBean.getUser_id());
                                stockPlaced.setOutletId(showOutLetBeen.getOutlet_id());
                                stockPlaced.setIs_order_placed("1");
                                db.updateIsOrderPlacedOutletWise(stockPlaced);
                                //  Log.d("Name: ", update + "");

                            } else {
                                db.addOrderPlacedOutletWise(distributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id(), showOutLetBeen.getOutlet_id(), "1");

                            }
                        } else {
                            db.addOrderPlacedOutletWise(distributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id(), showOutLetBeen.getOutlet_id(), "1");

                        }
*/
                    }
                } catch (JSONException e) {
                    list_view_show_orders.setVisibility(View.GONE);
                    tvEmptyList.setVisibility(View.VISIBLE);
                    tvEmptyList.setText("OOPS!Something Went Wrong");
                    e.printStackTrace();
                }
            } else {
                list_view_show_orders.setVisibility(View.GONE);
                tvEmptyList.setVisibility(View.VISIBLE);
                tvEmptyList.setText("Improper response from server");
                // Utils.showAlertDialog(OutletOrdersActivity.this, "Outlet Order", "Improper response from server", true);
            }
            if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
        }

    }

    /**
     * Compare dates
     */
    boolean checkTwoDates(String date) {

        SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault());
        Date newDate = null;
        try {
            newDate = spf.parse(date);
            spf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            date = spf.format(newDate);


            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = df.format(c);

            return !date.equalsIgnoreCase(formattedDate);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return true;

    }

    /**
     * Difference Between Two date is more then 1 day
     */
    private boolean differenceInTwoDates(String date) {
        Date c = new Date();


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault());
        Date date1 = null;

        try {
            date1 = sdf.parse(date);
            long different = c.getTime() - date1.getTime();
            if (TimeUnit.DAYS.convert(different, TimeUnit.MILLISECONDS) > 1) {
                return true;
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        return false;
    }
}
