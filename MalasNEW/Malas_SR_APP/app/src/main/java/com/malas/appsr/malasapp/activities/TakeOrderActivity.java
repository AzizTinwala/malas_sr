package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.google.gson.reflect.TypeToken;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.HighLightBean;
import com.malas.appsr.malasapp.BeanClasses.OutletOrderDateBean;
import com.malas.appsr.malasapp.BeanClasses.ReasonSubmitBean;
import com.malas.appsr.malasapp.BeanClasses.RouteBean;
import com.malas.appsr.malasapp.BeanClasses.ShowOutLetBeen;
import com.malas.appsr.malasapp.BeanClasses.TakeStockItemBean;
import com.malas.appsr.malasapp.BeanClasses.TakeStockUserSaved;
import com.malas.appsr.malasapp.BeanClasses.TakeStoclListBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.DistributorSpinnerAdapter;
import com.malas.appsr.malasapp.adapter.RouteAdapter;
import com.malas.appsr.malasapp.adapter.ShowOutletAdapter;
import com.malas.appsr.malasapp.dbHandler.DatabaseHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TakeOrderActivity extends AppCompatActivity {
    private static int productivecount = 0;
    private static int nonproductivecount = 0;
    LinearLayout llSync;
    Button btnSync;
    ArrayList<ShowOutLetBeen> showlistOutlet;
    private final ArrayList<HighLightBean> highLightDateList = new ArrayList<>();
    private EditText spnDistributor, spnRoute;
    private ImageView tvHighlightGreen, tvHighlightRed;
    private TextView tvHighlightGreenText, tvHighlightRedText, emptyListView, tvTotalOutletCount;
    private LinearLayout ll_highlight;
    private ListView lvShowOutLet;
    private ArrayList<DistributerBean> mDistributerList;
    private ArrayList<RouteBean> routeList;
    private ArrayList<ShowOutLetBeen> listOutlet = new ArrayList<>();
    private ShowOutletAdapter showOutletAdapter;
    private RouteAdapter mRouteAdapter;
    private DistributorSpinnerAdapter mDistributorSpinnerAdapter;
    private DistributerBean selectedDistributerBean;
    private RouteBean selectedRouteBean;
    private String is_stock_taken = "1";
    private DatabaseHandler db;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Outlet Order List");
        TextView tvTerritoryName = findViewById(R.id.tv_territory_outlet);
        spnDistributor = findViewById(R.id.spnr_distributr);
        spnRoute = findViewById(R.id.spnr_route);
        lvShowOutLet = findViewById(R.id.list_view_show_outlet);
        tvHighlightGreen = findViewById(R.id.tv_highlight_green);
        ll_highlight = findViewById(R.id.ll_highlight);
        tvHighlightGreenText = findViewById(R.id.tv_highlight_green_text);
        tvHighlightRed = findViewById(R.id.tv_highlight_red);
        tvHighlightRedText = findViewById(R.id.tv_highlight_red_text);
        tvTotalOutletCount = findViewById(R.id.total_outlet);
        llSync = findViewById(R.id.ll_sync);
        btnSync = findViewById(R.id.btn_sync);
        emptyListView = findViewById(R.id.ll_empty_list);
        mDistributerList = new ArrayList<>();
        db = new DatabaseHandler(this);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(TakeOrderActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        tvTerritoryName.setText(mUserLoginInfoBean.getTerritoryName().toUpperCase(Locale.getDefault()));

        Type typeOutlet = new TypeToken<ArrayList<ShowOutLetBeen>>() {
        }.getType();
        ComplexPreferences mOutletListPref = ComplexPreferences.getComplexPreferences(TakeOrderActivity.this, Constant.SHOW_OUTLET_PREF, MODE_PRIVATE);
        listOutlet = mOutletListPref.getArray(Constant.SHOW_OUTLET_OBJ, typeOutlet);
        ArrayList<DistributerBean> distributerBeanArrayList = db.getAllDistributorRecord();

        if (distributerBeanArrayList == null) {
            mDistributerList = new ArrayList<>();
        } else {
            mDistributerList = distributerBeanArrayList;
        }

        spnDistributor.setOnClickListener(v -> {
            if (mDistributerList != null) {
                final Dialog dialog = new Dialog(TakeOrderActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView_Counry = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                mDistributorSpinnerAdapter = new DistributorSpinnerAdapter(TakeOrderActivity.this, mDistributerList, "TakeOrder");
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
                    String countryname = DistributorSpinnerAdapter.resultArrayshort.get(position).getFirm_name().toUpperCase(Locale.getDefault());
                    spnDistributor.setText(countryname);
                    dialog.dismiss();
                    try {
                        selectedDistributerBean = mDistributerList.get(position);
                        ArrayList<RouteBean> routeBeanArrayList = db.getAllRouteRecord();
                        if (routeBeanArrayList == null) {
                            routeList = new ArrayList<>();
                        } else {
                            routeList = routeBeanArrayList;
                        }

                       /* ComplexPreferences mRouteList = ComplexPreferences.getComplexPreferences(TakeOrderActivity.this, Constant.ROUTE_LIST_PREF, MODE_PRIVATE);
                        Type typeRoute = new TypeToken<ArrayList<RouteBean>>() {
                        }.getType();
                        routeList = mRouteList.getArray(Constant.ROUTE_LIST_OBJ, typeRoute) == null ? new ArrayList<RouteBean>() : (ArrayList<RouteBean>) mRouteList.getArray(Constant.ROUTE_LIST_OBJ, typeRoute);
                       */
                        ArrayList<RouteBean> temprouteList = new ArrayList<>();
                        for (int i = 0; i < routeList.size(); i++) {
                            if (routeList.get(i).getDistributor_id().equalsIgnoreCase(mDistributerList.get(position).getDistribution_id())) {
                                temprouteList.add(routeList.get(i));
                            }
                        }
                        routeList.clear();
                        routeList.addAll(temprouteList);

                        spnRoute.setText("");

                        listOutlet = new ArrayList<>();

                        highLightDateList.clear();
                        lvShowOutLet.setVisibility(View.GONE);
                        emptyListView.setVisibility(View.GONE);

                      /*  showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", highLightDateList);
                        lvShowOutLet.setAdapter(showOutletAdapter);
                        showOutletAdapter.notifyDataSetChanged();*/
                        tvTotalOutletCount.setVisibility(View.GONE);
                        ll_highlight.setVisibility(View.GONE);


                      /*  ComplexPreferences complexPreferences1 = ComplexPreferences.getComplexPreferences(TakeOrderActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
                        UserLoginInfoBean mUserLoginInfoBean12 = complexPreferences1.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
                        String distributerId = mDistributerList.get(position).getDistribution_id();
                        String userId = mUserLoginInfoBean12.getUser_id();
                        if (Utils.isInternetConnected(TakeOrderActivity.this)) {
                            new mGetStockList().execute("getstocklist", userId, distributerId);
                        } else {
                            StockPlaced stockPlaced = db.isOrderStockPlaced(distributerId, userId);

                            if (showOutletAdapter != null) {
                                if (stockPlaced != null) {
                                    showOutletAdapter.setIsStockTaken(stockPlaced.getStock_placed());

                                } else {
                                    showOutletAdapter.setIsStockTaken("1");

                                }

                            }

                        }*/

                        llSync.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        });


        spnRoute.setOnClickListener(v -> {
            if (routeList != null) {
                final Dialog dialog = new Dialog(TakeOrderActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView_Counry = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                mRouteAdapter = new RouteAdapter(TakeOrderActivity.this, routeList);
                listView_Counry.setAdapter(mRouteAdapter);

                productivecount = 0;
                nonproductivecount = 0;
                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String st = etserach.getText().toString();
                        mRouteAdapter.filter(st);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView_Counry.setOnItemClickListener((parent, view, position, id) -> {
                    String countryname = RouteAdapter.resultArrayshort.get(position).getRoute_name().toUpperCase(Locale.getDefault());
                    selectedRouteBean = RouteAdapter.resultArrayshort.get(position);
                    spnRoute.setText(countryname);
                    dialog.dismiss();
                    try {
                        selectedRouteBean = routeList.get(position);
                        ComplexPreferences mUserPreference = ComplexPreferences.getComplexPreferences(TakeOrderActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
                        UserLoginInfoBean mUserLoginInfoBean1 = mUserPreference.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
                        if (selectedDistributerBean != null) {
                            if (Utils.isInternetConnected(TakeOrderActivity.this)) {

                                new Getalloutletlist().execute(mUserLoginInfoBean1.getUserTerritoryId(), selectedDistributerBean.getDistribution_id(), selectedRouteBean.getRoutes_id());

                                ArrayList<ReasonSubmitBean> reasonSubmitBeans = db.getAllReasonSubmitRecord();
                                for (int i = 0; i < reasonSubmitBeans.size(); i++) {
                                    Log.v("SUBMIT REASON", reasonSubmitBeans.get(i).getDistributorId() + reasonSubmitBeans.get(i).getOutletId() + reasonSubmitBeans.get(i).getUserId());
                                }
                                offlineReason();
                            } else {

                                ArrayList<ShowOutLetBeen> showOutLetBeensWhileOffline;
                                showOutLetBeensWhileOffline = db.getOutletRecordWhileOffline(selectedDistributerBean.getDistribution_id(), selectedRouteBean.getRoutes_id());

                                if (showOutLetBeensWhileOffline != null || showOutLetBeensWhileOffline.size() == 0) {
                                    ArrayList<String> stringRouteId = new ArrayList<>();
                                    for (int i = 0; i < showOutLetBeensWhileOffline.size(); i++) {
                                        stringRouteId.add(showOutLetBeensWhileOffline.get(i).getRoute_id());
                                    }

                                    if (stringRouteId.contains(selectedRouteBean.getRoutes_id())) {
                                        listOutlet.clear();
                                        listOutlet = new ArrayList<>();
                                        listOutlet.addAll(showOutLetBeensWhileOffline);
                                        highLightDateList.clear();
                                        tvTotalOutletCount.setVisibility(View.VISIBLE);
                                        tvTotalOutletCount.setText("Total Outlet Count : " + listOutlet.size());
                                        ArrayList<OutletOrderDateBean> outletOrderDateBeans = db.getOutletOrderDate(selectedDistributerBean.getDistribution_id(), selectedRouteBean.getRoutes_id());


                                        for (int i = 0; i < outletOrderDateBeans.size(); i++) {


                                            if (outletOrderDateBeans.get(i).getHighlightOrNot().equals("true")) {
                                                nonproductivecount = nonproductivecount + 1;

                                            } else if (outletOrderDateBeans.get(i).getHighlightOrNot().equals("false")) {

                                                productivecount = productivecount + 1;

                                            }

                                            HighLightBean highLightBean = new HighLightBean(outletOrderDateBeans.get(i).getOutletId(), outletOrderDateBeans.get(i).getOrderDate(), outletOrderDateBeans.get(i).getHighlightOrNot());
                                            highLightDateList.add(highLightBean);
                                        }

                                      /*  StockPlaced stockPlaced = db.isOrderStockPlaced(selectedDistributerBean.getDistribution_id(), mUserLoginInfoBean1.getUser_id());
                                        String stock_placed;
                                        if (stockPlaced != null) {
                                            stock_placed = stockPlaced.getStock_placed();
                                        } else {
                                            stock_placed = "1";
                                        }*/
                                        lvShowOutLet.setVisibility(View.VISIBLE);
                                        emptyListView.setVisibility(View.GONE);
                                        //     showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", stock_placed, highLightDateList);
                                        showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", highLightDateList);
                                        lvShowOutLet.setAdapter(showOutletAdapter);
                                        showOutletAdapter.notifyDataSetChanged();
                                        ll_highlight.setVisibility(View.VISIBLE);
                                        //  tvHighlightGreen.setBackgroundColor(Color.GREEN);
                                        //  tvHighlightRed.setBackgroundColor(Color.RED);
                                        tvHighlightGreenText.setText(productivecount + " Productive ");
                                        tvHighlightRedText.setText(nonproductivecount + " Non Productive ");

                                    } else {
                                        listOutlet.clear();
                                        lvShowOutLet.setVisibility(View.GONE);
                                        emptyListView.setVisibility(View.VISIBLE);
                                        /*showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", highLightDateList);
                                        lvShowOutLet.setAdapter(showOutletAdapter);
                                        showOutletAdapter.notifyDataSetChanged();*/
                                        tvTotalOutletCount.setVisibility(View.GONE);
                                        ll_highlight.setVisibility(View.GONE);
                                        emptyListView.setText("Check Internet Connection");
                                        //  Utils.showAlertDialog(TakeOrderActivity.this, "", "Check Internet Connection", true);

                                    }
                                    offlineReason();

                                } else {
                                    listOutlet.clear();
                                    lvShowOutLet.setVisibility(View.GONE);
                                    emptyListView.setVisibility(View.VISIBLE);
                                    tvTotalOutletCount.setVisibility(View.GONE);
                                    ll_highlight.setVisibility(View.GONE);
                                    emptyListView.setText("Check Internet Connection");
                                    // Utils.showAlertDialog(TakeOrderActivity.this, "", "Check Internet Connection", true);

                                }
                            }
                        } else {
                            listOutlet.clear();
                            showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", highLightDateList);
                            lvShowOutLet.setAdapter(showOutletAdapter);
                            showOutletAdapter.notifyDataSetChanged();
                            tvTotalOutletCount.setVisibility(View.GONE);
                            ll_highlight.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();

        productivecount = 0;
        nonproductivecount = 0;
        ComplexPreferences mUserPreference = ComplexPreferences.getComplexPreferences(TakeOrderActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = mUserPreference.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        //   getDistributorTaskCalling = new mDistributorList().execute(mUserLoginInfoBean.getUser_teratorry_id());
        //  addAllroute = new getAllroute().execute(mUserLoginInfoBean.getUser_teratorry_id());

        if (showOutletAdapter == null && selectedRouteBean == null && selectedDistributerBean == null) {
            if (Utils.isInternetConnected(TakeOrderActivity.this)) {
                new mDistributorList().execute(mUserLoginInfoBean.getUserTerritoryId());
            } else {
                ArrayList<DistributerBean> distributerBeanArrayList = db.getAllDistributorRecord();
                mDistributerList = distributerBeanArrayList;
                if (distributerBeanArrayList == null) {
                    mDistributerList = new ArrayList<>();
                } else {
                    mDistributerList = distributerBeanArrayList;
                }
            }
        }
        if (showOutletAdapter == null && selectedRouteBean == null && selectedDistributerBean == null) {

            if (Utils.isInternetConnected(TakeOrderActivity.this)) {

                new getAllroute().execute(mUserLoginInfoBean.getUserTerritoryId());
            } else {

                ArrayList<RouteBean> routeBeanArrayList = db.getAllRouteRecord();
                if (routeBeanArrayList == null) {
                    routeList = new ArrayList<>();
                } else {
                    routeList = routeBeanArrayList;
                }
            }
        }
     /*   if (showOutletAdapter != null && selectedRouteBean != null && selectedDistributerBean != null) {
            if (Utils.isInternetConnected(TakeOrderActivity.this)) {
                new mGetStockList().execute("getstocklist", mUserLoginInfoBean.getUser_id(), selectedDistributerBean.getDistribution_id());
            } else {
                StockPlaced stockPlaced = db.isOrderStockPlaced(selectedDistributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id());

                if (showOutletAdapter != null)
                    if (stockPlaced != null) {
                        showOutletAdapter.setIsStockTaken(stockPlaced.getStock_placed());
                    } else {
                        showOutletAdapter.setIsStockTaken("1");
                    }


            }
        }*/
        if (showOutletAdapter != null && selectedRouteBean != null && selectedDistributerBean != null) {
            offlineReason();
            if (showlistOutlet == null) {
                if (Utils.isInternetConnected(TakeOrderActivity.this)) {
                    new Getalloutletlist().execute(mUserLoginInfoBean.getUserTerritoryId(), selectedDistributerBean.getDistribution_id(), selectedRouteBean.getRoutes_id());

                } else {
                    ArrayList<ShowOutLetBeen> showOutLetBeensWhileOffline;
                    showOutLetBeensWhileOffline = db.getOutletRecordWhileOffline(selectedDistributerBean.getDistribution_id(), selectedRouteBean.getRoutes_id());
                    if (showOutLetBeensWhileOffline != null || showOutLetBeensWhileOffline.size() == 0) {
                        ArrayList<String> stringRouteId = new ArrayList<>();
                        for (int i = 0; i < showOutLetBeensWhileOffline.size(); i++) {
                            stringRouteId.add(showOutLetBeensWhileOffline.get(i).getRoute_id());
                        }

                        if (stringRouteId.contains(selectedRouteBean.getRoutes_id())) {
                            listOutlet.clear();
                            listOutlet = new ArrayList<>();
                            listOutlet.addAll(showOutLetBeensWhileOffline);
                            highLightDateList.clear();
                            tvTotalOutletCount.setVisibility(View.VISIBLE);
                            tvTotalOutletCount.setText("Total Outlet Count : " + listOutlet.size());
                            ArrayList<OutletOrderDateBean> outletOrderDateBeans = db.getOutletOrderDate(selectedDistributerBean.getDistribution_id(), selectedRouteBean.getRoutes_id());


                            for (int i = 0; i < outletOrderDateBeans.size(); i++) {


                                if (outletOrderDateBeans.get(i).getHighlightOrNot().equals("true")) {
                                    nonproductivecount = nonproductivecount + 1;

                                } else if (outletOrderDateBeans.get(i).getHighlightOrNot().equals("false")) {

                                    productivecount = productivecount + 1;

                                }

                                HighLightBean highLightBean = new HighLightBean(outletOrderDateBeans.get(i).getOutletId(), outletOrderDateBeans.get(i).getOrderDate(), outletOrderDateBeans.get(i).getHighlightOrNot());
                                highLightDateList.add(highLightBean);
                            }
                           /* StockPlaced stockPlaced = db.isOrderStockPlaced(selectedDistributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id());
                            String stock_placed;
                            if (stockPlaced != null) {
                                stock_placed = stockPlaced.getStock_placed();
                            } else {
                                stock_placed = "1";
                            }*/
                            lvShowOutLet.setVisibility(View.VISIBLE);
                            emptyListView.setVisibility(View.GONE);
                            // showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", stock_placed, highLightDateList);
                            showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", highLightDateList);
                            lvShowOutLet.setAdapter(showOutletAdapter);
                            showOutletAdapter.notifyDataSetChanged();
                            ll_highlight.setVisibility(View.VISIBLE);
                            //         tvHighlightGreen.setBackgroundColor(Color.GREEN);
                            //      tvHighlightRed.setBackgroundColor(Color.RED);
                            tvHighlightGreenText.setText(productivecount + " Productive ");
                            tvHighlightRedText.setText(nonproductivecount + " Non Productive ");

                        } else {
                           /* showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", highLightDateList);
                            lvShowOutLet.setAdapter(showOutletAdapter);
                            showOutletAdapter.notifyDataSetChanged();*/
                            lvShowOutLet.setVisibility(View.GONE);
                            emptyListView.setVisibility(View.VISIBLE);
                            tvTotalOutletCount.setVisibility(View.GONE);
                            ll_highlight.setVisibility(View.GONE);
                            emptyListView.setText("Check Internet Connection");
                            // Utils.showAlertDialog(TakeOrderActivity.this, "", "Check Internet Connection", true);

                        }
                    } else {
                        lvShowOutLet.setVisibility(View.GONE);
                        emptyListView.setVisibility(View.VISIBLE);
                     /*   showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", highLightDateList);
                        lvShowOutLet.setAdapter(showOutletAdapter);
                        showOutletAdapter.notifyDataSetChanged();*/
                        tvTotalOutletCount.setVisibility(View.GONE);
                        ll_highlight.setVisibility(View.GONE);
                        emptyListView.setText("Check Internet Connection");
                        //  Utils.showAlertDialog(TakeOrderActivity.this, "", "Check Internet Connection", true);

                    }
                }
            } else {
                if (spnRoute.getText().toString().equals("")) {
                    listOutlet.clear();
                    lvShowOutLet.setVisibility(View.GONE);
                    emptyListView.setVisibility(View.GONE);
                   /* showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", highLightDateList);
                    lvShowOutLet.setAdapter(showOutletAdapter);
                    showOutletAdapter.notifyDataSetChanged();*/
                    tvTotalOutletCount.setVisibility(View.GONE);
                    ll_highlight.setVisibility(View.GONE);

                } else {
                    ArrayList<ShowOutLetBeen> showOutLetBeensWhileOffline;
                    showOutLetBeensWhileOffline = db.getOutletRecordWhileOffline(selectedDistributerBean.getDistribution_id(), selectedRouteBean.getRoutes_id());
                    if (showOutLetBeensWhileOffline != null || showOutLetBeensWhileOffline.size() == 0) {
                        ArrayList<String> stringRouteId = new ArrayList<>();
                        for (int i = 0; i < showOutLetBeensWhileOffline.size(); i++) {
                            stringRouteId.add(showOutLetBeensWhileOffline.get(i).getRoute_id());
                        }

                        if (stringRouteId.contains(selectedRouteBean.getRoutes_id())) {
                            listOutlet.clear();
                            listOutlet = new ArrayList<>();
                            listOutlet.addAll(showOutLetBeensWhileOffline);
                            highLightDateList.clear();
                            tvTotalOutletCount.setVisibility(View.VISIBLE);
                            tvTotalOutletCount.setText("Total Outlet Count : " + listOutlet.size());
                            ArrayList<OutletOrderDateBean> outletOrderDateBeans = db.getOutletOrderDate(selectedDistributerBean.getDistribution_id(), selectedRouteBean.getRoutes_id());


                            for (int i = 0; i < outletOrderDateBeans.size(); i++) {


                                if (outletOrderDateBeans.get(i).getHighlightOrNot().equals("true")) {
                                    nonproductivecount = nonproductivecount + 1;

                                } else if (outletOrderDateBeans.get(i).getHighlightOrNot().equals("false")) {

                                    productivecount = productivecount + 1;

                                }

                                HighLightBean highLightBean = new HighLightBean(outletOrderDateBeans.get(i).getOutletId(), outletOrderDateBeans.get(i).getOrderDate(), outletOrderDateBeans.get(i).getHighlightOrNot());
                                highLightDateList.add(highLightBean);
                            }
                        /*    StockPlaced stockPlaced = db.isOrderStockPlaced(selectedDistributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id());
                            String stock_placed;
                            if (stockPlaced != null) {
                                stock_placed = stockPlaced.getStock_placed();
                            } else {
                                stock_placed = "1";
                            }*/
                            lvShowOutLet.setVisibility(View.VISIBLE);
                            emptyListView.setVisibility(View.GONE);
                            //   showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", stock_placed, highLightDateList);
                            showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", highLightDateList);
                            lvShowOutLet.setAdapter(showOutletAdapter);
                            showOutletAdapter.notifyDataSetChanged();
                            ll_highlight.setVisibility(View.VISIBLE);
                            //     tvHighlightGreen.setBackgroundColor(Color.GREEN);
                            //  tvHighlightRed.setBackgroundColor(Color.RED);
                            tvHighlightGreenText.setText(productivecount + " Productive ");
                            tvHighlightRedText.setText(nonproductivecount + " Non Productive ");

                        } else {
                           /* showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", highLightDateList);
                            lvShowOutLet.setAdapter(showOutletAdapter);
                            showOutletAdapter.notifyDataSetChanged();*/
                            lvShowOutLet.setVisibility(View.GONE);
                            emptyListView.setVisibility(View.VISIBLE);
                            tvTotalOutletCount.setVisibility(View.GONE);
                            ll_highlight.setVisibility(View.GONE);
                            emptyListView.setText("Check Internet Connection");
                            //  Utils.showAlertDialog(TakeOrderActivity.this, "", "Check Internet Connection", true);

                        }
                    } else {
                       /* showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", highLightDateList);
                        lvShowOutLet.setAdapter(showOutletAdapter);
                        showOutletAdapter.notifyDataSetChanged();*/
                        lvShowOutLet.setVisibility(View.GONE);
                        emptyListView.setVisibility(View.VISIBLE);
                        tvTotalOutletCount.setVisibility(View.GONE);
                        ll_highlight.setVisibility(View.GONE);
                        emptyListView.setText("Check Internet Connection");
                        //  Utils.showAlertDialog(TakeOrderActivity.this, "", "Check Internet Connection", true);

                    }
                }
            }
        }

    }

    public void offlineReason() {
        ArrayList<ReasonSubmitBean> reasonSubmitBeans = db.getReasonSubmitRecord(selectedDistributerBean.getDistribution_id(), selectedRouteBean.getRoutes_id());
        if (reasonSubmitBeans != null) {
            if (reasonSubmitBeans.size() > 0) {
                llSync.setVisibility(View.VISIBLE);
                btnSync.setOnClickListener(v -> {
                    Intent intent = new Intent(TakeOrderActivity.this, OfflineOrderData.class);
                    intent.putExtra("distributorId", selectedDistributerBean.getDistribution_id());
                    intent.putExtra("routeId", selectedRouteBean.getRoutes_id());

                    intent.putExtra("From", "TakeOrder");
                    startActivity(intent);
                });

            } else {
                llSync.setVisibility(View.GONE);
            }
        } else {
            llSync.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*  if (getDistributorTaskCalling != null && !getDistributorTaskCalling.isCancelled())
           getDistributorTaskCalling.cancel(true);

        if (addAllroute != null && !addAllroute.isCancelled())
            addAllroute.cancel(true);

        if (getalloutletlist != null && !getalloutletlist.isCancelled())
            getalloutletlist.cancel(true);


        if (getDateOfOutletAsync != null && !getDateOfOutletAsync.isCancelled())
            getDateOfOutletAsync.cancel(true);*/

    }

    private String checkJsonString(JSONArray mJsonArray, int position, String keyName) {
        String jsonStr = "";
        try {
            jsonStr = mJsonArray.getJSONObject(position).getString(keyName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    @SuppressLint("StaticFieldLeak")
    public class mDistributorList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(TakeOrderActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Distributor List please wait");
            mDialog.setCancelable(false);
            /*   if (mDistributerList.isEmpty()) {*/
            mDialog.show();
            //}
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getdistributorlist"));
            nameValuePair.add(new BasicNameValuePair("territory_id", params[0]));
            //JSONObject jsonObjectFromUrl = new JSONParser().getJsonObjectFromHttp(Constant.BASE_URL, nameValuePair, JSONParser.Http.POST);

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        // if (mDistributerList.isEmpty())
                        //                    Toast.makeText(TakeOrderActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        ArrayList<DistributerBean> mDistributerArraylist = new ArrayList<>();
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
                        db.deleteAllRecords();
                        db.addDistributor(mDistributerList);


                        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(TakeOrderActivity.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
                        mDistributerListPref.putObject(Constant.DISTRIBUTOR_LIST_OBJ, mDistributerArraylist);
                        mDistributerListPref.commit();
                    } else {
                        //Utils.showAlertDialog(TakeOrderActivity.this, "Order", jsonObject.getString("message"), true);
                        Toast.makeText(TakeOrderActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertDialog(TakeOrderActivity.this, "Order", "Improper response from server", true);
//                Toast.makeText(TakeOrderActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class getAllroute extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(TakeOrderActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Route List please wait");
            mDialog.setCancelable(false);
            /*if (mDistributerList.isEmpty()) {*/
            mDialog.show();
            //  }
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getallrouteslist"));
            nameValuePair.add(new BasicNameValuePair("territory_id", params[0]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        if (mDistributerList.isEmpty())
                            Utils.showToast(TakeOrderActivity.this, jsonObject.getString("message"));
                        ArrayList<RouteBean> routeList = new ArrayList<>();
                        JSONArray mJsonArray = jsonObject.getJSONArray("routeslist");
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            String distributor_id = mJsonArray.getJSONObject(i).getString("distributor_id");
                            String routes_id = mJsonArray.getJSONObject(i).getString("routes_id");
                            String activation_status = mJsonArray.getJSONObject(i).getString("activation_status");
                            String route_name = mJsonArray.getJSONObject(i).getString("route_name");

                            routeList.add(new RouteBean(distributor_id, routes_id, activation_status, route_name));
                        }

                        db.deleteAllRouteRecords();
                        db.addRoute(routeList);
                       /* // Reading adll contacts
                        Log.d("Reading: ", "Reading all contacts..");
                        ArrayList<RouteBean> contacts1 = db.getAllRouteRecord();

                        for (int i = 0; i < contacts1.size(); i++) {
                            String log = "Id: " + contacts1.get(i).getDistributor_id() + " ,Name: " + contacts1.get(i).getRoutes_id() + " ,Phone: " + contacts1.get(i).getRoute_name();
                            // Writing Contacts to log
                            Log.d("Name: ", log);
                        }*/
                        //  Log.d("count",db.getContactsCount()+"");


                        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(TakeOrderActivity.this, Constant.ROUTE_LIST_PREF, MODE_PRIVATE);
                        mDistributerListPref.putObject(Constant.ROUTE_LIST_OBJ, routeList);
                        mDistributerListPref.commit();
                    } else {
                        Utils.showToast(TakeOrderActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertDialog(TakeOrderActivity.this, "Order", "Improper response from server", true);
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class Getalloutletlist extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(TakeOrderActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Outlet List please wait");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getalloutletlist"));
            nameValuePair.add(new BasicNameValuePair("territory_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("distributor_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("route_id", params[2]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {

                    showlistOutlet = new ArrayList<>();
                    if (jsonObject.getString("success").equals("true")) {
                        JSONArray mJsonArray = jsonObject.getJSONArray("ouletlist");

                        for (int i = 0; i < mJsonArray.length(); i++) {
                            String category_handlar = checkJsonString(mJsonArray, i, "categoryhandler");
                            String contact_person_name = checkJsonString(mJsonArray, i, "contact_person_name");
                            String outlet_email = checkJsonString(mJsonArray, i, "outlet_email");
                            String type_appointment = checkJsonString(mJsonArray, i, "type_appointment");
                            String activation_status = checkJsonString(mJsonArray, i, "activation_status");
                            String country_id = checkJsonString(mJsonArray, i, "country_id");
                            String supply_chain = checkJsonString(mJsonArray, i, "supply_chain");
                            String contact_number = checkJsonString(mJsonArray, i, "contact_number");
                            String city_id = checkJsonString(mJsonArray, i, "city_id");
                            String outlet_name = checkJsonString(mJsonArray, i, "outlet_name");
                            String addedby = checkJsonString(mJsonArray, i, "addedby");
                            String address = checkJsonString(mJsonArray, i, "outlet_address");
                            String residential_address = checkJsonString(mJsonArray, i, "residential_address");
                            String outlet_id = checkJsonString(mJsonArray, i, "outlet_id");
                            String mobile_no = checkJsonString(mJsonArray, i, "mobile_no");
                            String distribution_id = checkJsonString(mJsonArray, i, "distribution_id");
                            String route_id = checkJsonString(mJsonArray, i, "route_id");
                            String distributor_name = checkJsonString(mJsonArray, i, "distributor_name");
                            String route_name = checkJsonString(mJsonArray, i, "route_name");
                            String state_id = checkJsonString(mJsonArray, i, "state_id");
                            String district_id = checkJsonString(mJsonArray, i, "district_id");
                            String territory_id = checkJsonString(mJsonArray, i, "territory_id");

                            showlistOutlet.add(new ShowOutLetBeen(category_handlar, contact_person_name, outlet_email, type_appointment, activation_status, country_id, supply_chain, contact_number, city_id, outlet_name, addedby, address, residential_address, outlet_id, mobile_no, distribution_id, route_id, distributor_name, route_name, state_id, district_id, territory_id));
                        }

                        ArrayList<ShowOutLetBeen> dbOutletList = db.getAllOutletRecord();
                        ArrayList<String> stringRouteId = new ArrayList<>();
                        for (int i = 0; i < dbOutletList.size(); i++) {
                            stringRouteId.add(dbOutletList.get(i).getRoute_id());
                        }


                        if (stringRouteId.contains(showlistOutlet.get(0).getRoute_id())) {
                            db.deleteOutlet(showlistOutlet.get(0).getDistribution_id(), showlistOutlet.get(0).getRoute_id());
                        }
                        db.addOutlet(showlistOutlet);


                        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(TakeOrderActivity.this, Constant.SHOW_OUTLET_PREF, MODE_PRIVATE);
                        mDistributerListPref.putObject(Constant.SHOW_OUTLET_OBJ, showlistOutlet);
                        mDistributerListPref.commit();
                        listOutlet = new ArrayList<>();
                        listOutlet.addAll(showlistOutlet);
                        highLightDateList.clear();
                        tvTotalOutletCount.setVisibility(View.VISIBLE);
                        tvTotalOutletCount.setText("Total Outlet Count : " + listOutlet.size());

                        ArrayList<String> outletIdList = new ArrayList<>();
                        for (int i = 0; i < listOutlet.size(); i++) {
                            outletIdList.add(listOutlet.get(i).getOutlet_id());
                        }
                        try {
                            JSONObject mJsonObject = new JSONObject();

                            JSONArray mJsonArray1 = new JSONArray();
                            for (int i = 0; i < outletIdList.size(); i++) {
                                JSONObject mTempObj = new JSONObject();

                                mTempObj.put("outlet_id", outletIdList.get(i));

                                mJsonArray1.put(mTempObj);
                            }
                            mJsonObject.put("outlet_id_list", mJsonArray1);
                            new Getalloutletorderlist().execute(mJsonObject.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {
                        listOutlet.clear();

                        tvTotalOutletCount.setVisibility(View.GONE);
                        ll_highlight.setVisibility(View.GONE);
                        lvShowOutLet.setVisibility(View.GONE);
                        emptyListView.setVisibility(View.VISIBLE);
                        emptyListView.setText(jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    tvTotalOutletCount.setVisibility(View.GONE);
                    ll_highlight.setVisibility(View.GONE);
                    lvShowOutLet.setVisibility(View.GONE);
                    emptyListView.setVisibility(View.VISIBLE);
                    emptyListView.setText("OOPS! Something Went Wrong");
                    e.printStackTrace();
                }

            } else {
                tvTotalOutletCount.setVisibility(View.GONE);
                ll_highlight.setVisibility(View.GONE);
                lvShowOutLet.setVisibility(View.GONE);
                emptyListView.setVisibility(View.VISIBLE);
                emptyListView.setText("Improper response from server");
                //Utils.showAlertDialog(TakeOrderActivity.this, "Order", "Improper response from server", true);
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mGetStockList extends AsyncTask<String, Void, JSONObject> {
        ArrayList<TakeStoclListBean> itemList;
        ArrayList<TakeStockUserSaved> takeStockList;

        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(TakeOrderActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }


        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", params[0]));
            nameValuePair.add(new BasicNameValuePair("user_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("distributor_id", params[2]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                ComplexPreferences complexPreferences1 = ComplexPreferences.getComplexPreferences(TakeOrderActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);

                UserLoginInfoBean mUserLoginInfoBean = complexPreferences1.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONArray jArray = jsonObject.getJSONArray("list");
                        takeStockList = new ArrayList<>();
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jobj = jArray.getJSONObject(i);
                            String stock_uni_id = jobj.getString("stock_uni_id");
                            String stock_time = jobj.getString("stock_time");
                            String isStockPlaced = jobj.getString("is_stock_placed");
                            JSONArray itemarry = jobj.getJSONArray("items");
                            if (jobj.getString("is_stock_placed").equals("0"))
                                is_stock_taken = "0";
                            else
                                is_stock_taken = "1";


                            itemList = new ArrayList<>();

                            for (int j = 0; j < itemarry.length(); j++) {
                                ArrayList<TakeStockItemBean> arryItemList = new ArrayList<>();

                                JSONObject itmnobj = itemarry.getJSONObject(j);
                                String cat_id = itmnobj.getString("cat_id");
                                String sku_code = itmnobj.getString("sku_code");
                                String product_id = itmnobj.getString("product_id");
                                String product_mrp = itmnobj.getString("product_mrp");
                                String Case_Size = itmnobj.getString("Case_Size");
                                String cat_name = itmnobj.getString("cat_name");
                                String product_name = itmnobj.getString("product_name");
                                String quantity;

                                try {
                                    quantity = itmnobj.getString("quantity");
                                } catch (Exception e) {
                                    quantity = "0";
                                    e.printStackTrace();
                                }

                                arryItemList.add(new TakeStockItemBean(product_id, product_mrp, sku_code, product_name, quantity, false, Case_Size));
                                if (itemList.isEmpty()) {
                                    itemList.add(new TakeStoclListBean(cat_id, cat_name, arryItemList));
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
                                        itemList.add(new TakeStoclListBean(cat_id, cat_name, arryItemList));
                                    }
                                }
                            }
                            takeStockList.add(new TakeStockUserSaved(stock_uni_id, stock_time, isStockPlaced, itemList));
                        }

                      /*  ArrayList<StockPlaced> allStockRecord = db.getAllisOrderStockPlaced(mUserLoginInfoBean.getUser_id());
                        ArrayList<String> distributorList = new ArrayList<>();
                        for (int i = 0; i < allStockRecord.size(); i++) {
                            distributorList.add(allStockRecord.get(i).getDistributorId());
                        }
                        if (distributorList.contains(selectedDistributerBean.getDistribution_id())) {
                            StockPlaced stockPlaced = new StockPlaced(selectedDistributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id(), is_stock_taken, "");
                            db.updateIsStockPlaced(stockPlaced);
                            //  Log.d("Name: ", update + "");

                        } else {
                            StockPlaced stockPlaced = new StockPlaced(selectedDistributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id(), is_stock_taken, "1");

                            db.addisorderplaced_stock(stockPlaced);

                        }
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(TakeOrderActivity.this, Constant.takestocklistPref, MODE_PRIVATE);
                        complexPreferences.putObject(Constant.takestocklistPrefobj, takeStockList);
                        complexPreferences.commit();*/

                       /* if (showOutletAdapter != null)
                            showOutletAdapter.setIsStockTaken(is_stock_taken);*/

                    } else {
                       /* ArrayList<StockPlaced> allStockRecord = db.getAllisOrderStockPlaced(mUserLoginInfoBean.getUser_id());
                        ArrayList<String> distributorList = new ArrayList<>();
                        for (int i = 0; i < allStockRecord.size(); i++) {
                            distributorList.add(allStockRecord.get(i).getDistributorId());
                        }
                        if (distributorList.contains(selectedDistributerBean.getDistribution_id())) {
                            StockPlaced stockPlaced = new StockPlaced(selectedDistributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id(), is_stock_taken, "");
                            db.updateIsStockPlaced(stockPlaced);
                            //  Log.d("Name: ", update + "");

                        } else {
                            StockPlaced stockPlaced = new StockPlaced(selectedDistributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id(), is_stock_taken, "1");

                            db.addisorderplaced_stock(stockPlaced);

                        }*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertDialog(TakeOrderActivity.this, "Order", "Improper response from server", true);
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class Getalloutletorderlist extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(TakeOrderActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading List please wait");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getorderdatebyoutletlist"));
            nameValuePair.add(new BasicNameValuePair("data", params[0]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {

                try {
                    ComplexPreferences complexPreferences1 = ComplexPreferences.getComplexPreferences(TakeOrderActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);

                    UserLoginInfoBean mUserLoginInfoBean = complexPreferences1.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
/*
                    StockPlaced stockPlaced = db.isOrderStockPlaced(selectedDistributerBean.getDistribution_id(), mUserLoginInfoBean.getUser_id());
                    String stock_placed;
                    if (stockPlaced != null) {
                        stock_placed = stockPlaced.getStock_placed();
                    } else {
                        stock_placed = "1";
                    }*/
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        JSONArray jArray = jsonObject.getJSONArray("highlightarray");
                        ArrayList<HighLightBean> highLightBeans = new ArrayList<>();
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jobj = jArray.getJSONObject(i);
                            String order_time = jobj.getString("order_take_time");
                            String outletId = jobj.getString("outlet_id");
                            String highlightornot = jobj.getString("highlightornot");
                            HighLightBean highLightBean = new HighLightBean(outletId, order_time, highlightornot);
                            highLightBeans.add(highLightBean);


                        }


                        try {

                            ArrayList<OutletOrderDateBean> outletOrderDateBeans = db.getAllOutletOrderDate();
                            ArrayList<String> outletIdList = new ArrayList<>();
                            for (int i = 0; i < outletOrderDateBeans.size(); i++) {
                                outletIdList.add(outletOrderDateBeans.get(i).getOutletId());
                            }

                            for (int k = 0; k < highLightBeans.size(); k++) {
                                if (outletIdList.contains(highLightBeans.get(k).getOutletId())) {
                                    OutletOrderDateBean outletOrderDateBean = new OutletOrderDateBean(selectedDistributerBean.getDistribution_id(), selectedRouteBean.getRoutes_id(), highLightBeans.get(k).getOutletId(), highLightBeans.get(k).getOutletDate(), highLightBeans.get(k).getHighlightOrNot());

                                    db.updateOutletOrderDate(outletOrderDateBean);
                                } else {
                                    OutletOrderDateBean outletOrderDateBean = new OutletOrderDateBean(selectedDistributerBean.getDistribution_id(), selectedRouteBean.getRoutes_id(), highLightBeans.get(k).getOutletId(), highLightBeans.get(k).getOutletDate(), highLightBeans.get(k).getHighlightOrNot());

                                    db.addOutletOrderDate(outletOrderDateBean);


                                }
                            }
                            lvShowOutLet.setVisibility(View.VISIBLE);
                            emptyListView.setVisibility(View.GONE);
                            // showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", stock_placed, highLightBeans);
                            showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", highLightBeans);
                            lvShowOutLet.setAdapter(showOutletAdapter);
                            showOutletAdapter.notifyDataSetChanged();
                            ll_highlight.setVisibility(View.VISIBLE);
//                            tvHighlightGreen.setBackgroundColor(Color.GREEN);
//                            tvHighlightRed.setBackgroundColor(Color.RED);
                            tvHighlightGreenText.setText(jsonObject.getString("productiveCount") + " Productive");
                            tvHighlightRedText.setText(jsonObject.getString("nonproductiveCount") + " Non Productive");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        if (listOutlet.size() > 0) {
                            lvShowOutLet.setVisibility(View.VISIBLE);
                            emptyListView.setVisibility(View.GONE);
                            //showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", stock_placed, null);
                            showOutletAdapter = new ShowOutletAdapter(TakeOrderActivity.this, listOutlet, selectedDistributerBean, selectedRouteBean, "takeOrder", null);
                            lvShowOutLet.setAdapter(showOutletAdapter);
                            showOutletAdapter.notifyDataSetChanged();
                        } else {
                            lvShowOutLet.setVisibility(View.GONE);
                            emptyListView.setVisibility(View.VISIBLE);

                            emptyListView.setText(jsonObject.getString("message"));
                        }


                       /* ll_highlight.setVisibility(View.VISIBLE);
                        tvHighlightGreen.setBackgroundColor(Color.GREEN);
                        tvHighlightRed.setBackgroundColor(Color.RED);
                        tvHighlightGreenText.setText(jsonObject.getString("productiveCount") + " Productive");
                        tvHighlightRedText.setText(jsonObject.getString("nonproductiveCount") + " Non Productive");
*/

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    lvShowOutLet.setVisibility(View.GONE);
                    emptyListView.setVisibility(View.VISIBLE);

                    emptyListView.setText("OOPS!Something Went Wrong");
                }
            } else {
                lvShowOutLet.setVisibility(View.GONE);
                emptyListView.setVisibility(View.VISIBLE);

                emptyListView.setText("Improper response from server");
                //  Utils.showAlertDialog(TakeOrderActivity.this, "Outlet Order", "Improper response from server", true);
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.registration_menu, menu);
        MenuItem item = menu.findItem(R.id.registration_search);
        if (item != null) {
            searchView = (SearchView) item.getActionView();
            searchView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    showOutletAdapter.getFilter().filter(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (showOutletAdapter != null) {
                        showOutletAdapter.getFilter().filter(s);
                    }
                    return true;
                }
            });

        }
        return true;

    }

}