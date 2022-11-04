package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.PreviousPlacedOrderBean;
import com.malas.appsr.malasapp.BeanClasses.PreviousPlaceorderArraylistBean;
import com.malas.appsr.malasapp.BeanClasses.TakePreviousPlaceOrderItemList;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.DistributorSpinnerAdapter;
import com.malas.appsr.malasapp.adapter.PreviousPlaceOrderedList;
import com.malas.appsr.malasapp.dbHandler.DatabaseHandler;
import com.malas.appsr.malasapp.view.NonScrollListView;

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

/**
 * Created by Admin on 01-Feb-18.
 */

public class PlaceOrderListActivity extends AppCompatActivity {
    EditText spnDistributer;
    ImageView ivAdd;
    ListView lvShowStock;
    PreviousPlaceOrderedList previousPlaceOrderedList;
    DistributorSpinnerAdapter mDistributorSpinnerAdapter;
    ArrayList<DistributerBean> mDistributerList;
    AsyncTask<String, Void, JSONObject> getDistributorTaskCalling, mGetStockListObj;
    ArrayList<PreviousPlaceorderArraylistBean> itemList;
    ArrayList<PreviousPlacedOrderBean> previousPlacedOrderBeanArrayList;
    int spinerSelectedPosition = 0;
    boolean isShowProgress = false;
    DistributerBean selectedDistributerBean;
    TextView tverritoryname;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_stock);
        spnDistributer = findViewById(R.id.spnr_distributr);
        ivAdd = findViewById(R.id.iv_add);
        lvShowStock = (NonScrollListView) findViewById(R.id.list_view_show_outlet);
        tverritoryname = findViewById(R.id.tv_territory_stock);
        db = new DatabaseHandler(PlaceOrderListActivity.this);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Distributor Order List");

        ComplexPreferences complexPrefs = ComplexPreferences.getComplexPreferences(PlaceOrderListActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = complexPrefs.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        tverritoryname.setText(mUserLoginInfoBean.getTerritoryName().toUpperCase(Locale.getDefault()));

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(PlaceOrderListActivity.this, Constant.takeplaceorderPref, MODE_PRIVATE);
        Type typeTakeStockList = new TypeToken<ArrayList<PreviousPlacedOrderBean>>() {
        }.getType();
        previousPlacedOrderBeanArrayList = complexPreferences.getArray(Constant.takeplaceorderPrefobj, typeTakeStockList);
        lvShowStock.setOnItemClickListener((parent, view, position, id) -> {
            if (!previousPlacedOrderBeanArrayList.isEmpty() && position < previousPlacedOrderBeanArrayList.size()) {
                ArrayList<PreviousPlaceorderArraylistBean> placeorderList = previousPlacedOrderBeanArrayList.get(position).getPreviousPlaceorderArraylistBeans();
                if (selectedDistributerBean != null)
                    startActivity(new Intent(PlaceOrderListActivity.this, ShowPreviousPlacedOrderList.class).putExtra("list", new Gson().toJson(placeorderList)).putExtra("firmName", selectedDistributerBean.getFirm_name()));
                else
                    Toast.makeText(PlaceOrderListActivity.this, "Please Select Distributor First", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PlaceOrderListActivity.this, "Item not found please try again", Toast.LENGTH_SHORT).show();
            }
        });

        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(PlaceOrderListActivity.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
        Type typeDistributor = new TypeToken<ArrayList<DistributerBean>>() {
        }.getType();
        mDistributerList = mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor) == null ? new ArrayList<>() : (ArrayList<DistributerBean>) mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor);
        getDistributorTaskCalling = new mDistributorList().execute(mUserLoginInfoBean.getUserTerritoryId());

        spnDistributer.setOnClickListener(v -> {
            if (mDistributerList != null) {

                final Dialog dialog = new Dialog(PlaceOrderListActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView_Counry = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                mDistributorSpinnerAdapter = new DistributorSpinnerAdapter(PlaceOrderListActivity.this, mDistributerList, "TakeOrder");
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
                    selectedDistributerBean = DistributorSpinnerAdapter.resultArrayshort.get(position);
                    spnDistributer.setText(countryname);
                    //  citylist.clear();
                    dialog.dismiss();
                    try {
                        previousPlacedOrderBeanArrayList = null;
                        isShowProgress = true;
                        previousPlacedOrderBeanArrayList = new ArrayList<>();

                        previousPlaceOrderedList = new PreviousPlaceOrderedList(PlaceOrderListActivity.this, previousPlacedOrderBeanArrayList, selectedDistributerBean);
                        lvShowStock.setAdapter(previousPlaceOrderedList);

                        spinerSelectedPosition = position;
                        ComplexPreferences complexPreferences1 = ComplexPreferences.getComplexPreferences(PlaceOrderListActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
                        UserLoginInfoBean mUserLoginInfoBean1 = complexPreferences1.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
                        String distributerId = mDistributerList.get(position).getDistribution_id();
                        String userId = mUserLoginInfoBean1.getUserId();
                        mGetStockListObj = new mGetPreviousPlacedOrderData().execute("getplacedorder", userId, distributerId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        ivAdd.setOnClickListener(v -> {
            if (selectedDistributerBean != null) {

                long saveDataCount, saveEditCount;
                saveDataCount = db.getDataSaveCount(selectedDistributerBean.getDistribution_id());
                saveEditCount = db.getEditSaveCount(selectedDistributerBean.getDistribution_id());
                if (saveDataCount != 0 || saveEditCount != 0) {
                    Utils.showAlertDialog(PlaceOrderListActivity.this, "Offline Orders Pending ", "Some Orders of this distributor is not save ,Please Save the order than place the order", true);

                } else {
                    startActivity(new Intent(PlaceOrderListActivity.this, ShowPlaceOrderNew.class).putExtra("firmName", selectedDistributerBean.getFirm_name()).putExtra("distribbuterId", selectedDistributerBean.getDistribution_id()));
//                    startActivity(new Intent(PlaceOrderListActivity.this, tempShowPlaceOrderNew.class).putExtra("firmName", selectedDistributerBean.getFirm_name()).putExtra("distribbuterId", selectedDistributerBean.getDistribution_id()));

                }

            } else
                Toast.makeText(PlaceOrderListActivity.this, "Please Select Distributor", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        isShowProgress = false;
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(PlaceOrderListActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
//        if (mDistributerList.size() > 0) {
//            String distributerId = mDistributerList.get(spinerSelectedPosition).getDistribution_id();
        String userId = mUserLoginInfoBean.getUserId();
        if (selectedDistributerBean != null)
            mGetStockListObj = new mGetPreviousPlacedOrderData().execute("getplacedorder", userId, selectedDistributerBean.getDistribution_id());
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGetStockListObj != null && !mGetStockListObj.isCancelled())
            mGetStockListObj.cancel(true);


        if (getDistributorTaskCalling != null && !getDistributorTaskCalling.isCancelled())
            getDistributorTaskCalling.cancel(true);
    }

    @SuppressLint("StaticFieldLeak")
    public class mDistributorList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(PlaceOrderListActivity.this);
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
                        // System.out.print(jsonObject.getString("message").toString());
                     //   Utils.showToast(PlaceOrderListActivity.this, jsonObject.getString("message"));
//                        Toast.makeText(PlaceOrderListActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
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
                        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(PlaceOrderListActivity.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
                        mDistributerListPref.putObject(Constant.DISTRIBUTOR_LIST_OBJ, mDistributerArraylist);
                        mDistributerListPref.commit();
                    } else {
                        Utils.showAlertDialog(PlaceOrderListActivity.this, "Distributor Order", jsonObject.getString("message"), true);
//                        Toast.makeText(PlaceOrderListActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertDialog(PlaceOrderListActivity.this, "Distributor Order", "Improper response from server", true);
//                Toast.makeText(PlaceOrderListActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mGetPreviousPlacedOrderData extends AsyncTask<String, Void, JSONObject> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(PlaceOrderListActivity.this);
            mProgressDialog.setMessage("Getting Previous Placed Order List......");
            mProgressDialog.setCancelable(false);
            if (isShowProgress || (previousPlacedOrderBeanArrayList == null || previousPlacedOrderBeanArrayList.isEmpty()))
                mProgressDialog.show();

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
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONArray jArray = jsonObject.getJSONArray("list");
                        previousPlacedOrderBeanArrayList = new ArrayList<>();
                        itemList = new ArrayList<>();


                        for (int i = 0; i < jArray.length(); i++) {

                            JSONObject jobj = jArray.getJSONObject(i);


                            String datetime = jobj.getString("datetime");
                            String placeordertime = jobj.getString("placeordertime");
                            JSONArray itemarry = jobj.getJSONArray("items");
                            itemList = new ArrayList<>();

                            for (int j = 0; j < itemarry.length(); j++) {
                                ArrayList<TakePreviousPlaceOrderItemList> arryItemList = new ArrayList<>();

                                JSONObject itmnobj = itemarry.getJSONObject(j);
                                String itemid = itmnobj.getString("product_id");
                                String product_name = itmnobj.getString("product_name");
                                String orderquty = itmnobj.getString("order_quantity");
                                String stockquty = itmnobj.getString("stock_quantity");
                                String diffrance = itmnobj.getString("difference");
                                String paket_size = itmnobj.getString("packet_size");
                                String sku_code = itmnobj.getString("sku_code");
                                String catid = itmnobj.getString("cat_id");
                                String category_name = itmnobj.getString("cat_name");
                                String product_mrp = itmnobj.getString("product_mrp");


                                arryItemList.add(new TakePreviousPlaceOrderItemList(itemid, product_mrp, sku_code, product_name, orderquty, stockquty, diffrance, paket_size));
                                if (itemList.isEmpty()) {
                                    itemList.add(new PreviousPlaceorderArraylistBean(catid, category_name, arryItemList));
                                } else {
                                    boolean needtoAddnewItem = true;
                                    for (int k = 0; k < itemList.size(); k++) {
                                        if (itemList.get(k).getId().equalsIgnoreCase(catid)) {
                                            needtoAddnewItem = false;
                                            arryItemList.addAll(itemList.get(k).getArryItemList());
                                            itemList.get(k).setArryItemList(arryItemList);
                                        }
                                    }
                                    if (needtoAddnewItem) {
                                        itemList.add(new PreviousPlaceorderArraylistBean(catid, category_name, arryItemList));
                                    }
                                }
                            }


                            previousPlacedOrderBeanArrayList.add(new PreviousPlacedOrderBean(datetime, placeordertime, itemList));
                        }
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(PlaceOrderListActivity.this, Constant.takeplaceorderPref, MODE_PRIVATE);
                        complexPreferences.putObject(Constant.takeplaceorderPrefobj, previousPlacedOrderBeanArrayList);
                        complexPreferences.commit();

                        if (previousPlacedOrderBeanArrayList.size() > 0) {
                            if (previousPlacedOrderBeanArrayList.size() > 10) {
                                ArrayList<PreviousPlacedOrderBean> previousPlacedOrderBeanArrayListtemp = new ArrayList<>();

                                for (int i = 0; i < 10; i++) {
                                    previousPlacedOrderBeanArrayListtemp.add(previousPlacedOrderBeanArrayList.get(i));
                                }
                                previousPlacedOrderBeanArrayList.clear();
                                previousPlacedOrderBeanArrayList = previousPlacedOrderBeanArrayListtemp;

                            }

                        }
                        previousPlaceOrderedList = new PreviousPlaceOrderedList(PlaceOrderListActivity.this, previousPlacedOrderBeanArrayList, selectedDistributerBean);
                        lvShowStock.setAdapter(previousPlaceOrderedList);

                    } else {
//                        Toast.makeText(PlaceOrderListActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                        Utils.showAlertDialog(PlaceOrderListActivity.this, "Distributor Order", jsonObject.getString("message"), true);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertDialog(PlaceOrderListActivity.this, "Distributor Order", "Improper response from server", true);
//                Toast.makeText(PlaceOrderListActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
        }
    }
}
