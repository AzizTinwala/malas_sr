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
import android.text.format.DateFormat;
import android.util.Log;
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
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.TakeStockItemBean;
import com.malas.appsr.malasapp.BeanClasses.TakeStockUserSaved;
import com.malas.appsr.malasapp.BeanClasses.TakeStoclListBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.DistributorSpinnerAdapter;
import com.malas.appsr.malasapp.adapter.TakeStockAdapter;
import com.malas.appsr.malasapp.view.NonScrollListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TakeStockActivity extends AppCompatActivity {
    EditText spnDistributer;
    ImageView ivAdd;
    ListView lvShowStock;
    TakeStockAdapter takeStockAdapter;
    DistributorSpinnerAdapter mDistributorSpinnerAdapter;
    ArrayList<DistributerBean> mDistributerList;
    AsyncTask<String, Void, JSONObject> getDistributorTaskCalling, mGetStockListObj;
    ArrayList<TakeStoclListBean> itemList;
    ArrayList<TakeStockUserSaved> takeStockList;
    int spinerSelectedPosition = 0;
    boolean isShowProgress = false;
    DistributerBean selectedDistributerBean;
    TextView tverritoryname;
    String is_stock_placed = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_stock);
        spnDistributer = findViewById(R.id.spnr_distributr);
        ivAdd = findViewById(R.id.iv_add);
        lvShowStock = (NonScrollListView) findViewById(R.id.list_view_show_outlet);
        tverritoryname = findViewById(R.id.tv_territory_stock);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Stock List");

        ComplexPreferences complexPrefs = ComplexPreferences.getComplexPreferences(TakeStockActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = complexPrefs.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        tverritoryname.setText(mUserLoginInfoBean.getTerritoryName().toUpperCase(Locale.getDefault()));

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(TakeStockActivity.this, Constant.takestocklistPref, MODE_PRIVATE);
        Type typeTakeStockList = new TypeToken<ArrayList<TakeStockUserSaved>>() {
        }.getType();
        takeStockList = complexPreferences.getArray(Constant.takestocklistPrefobj, typeTakeStockList);

//        takeStockAdapter = new TakeStockAdapter(TakeStockActivity.this, previousPlacedOrderBeanArrayList == null ? new ArrayList() : previousPlacedOrderBeanArrayList);
//        lvShowStock.setAdapter(takeStockAdapter);

        lvShowStock.setOnItemClickListener((parent, view, position, id) -> {
            if (!takeStockList.isEmpty() && position < takeStockList.size()) {
                ArrayList<TakeStoclListBean> stockList = takeStockList.get(position).getStockList();
                if (selectedDistributerBean != null)
                    startActivity(new Intent(TakeStockActivity.this, ShowTakedStock.class).putExtra("list", new Gson().toJson(stockList)).putExtra("firmName", selectedDistributerBean.getFirm_name()));
                else
                    Toast.makeText(TakeStockActivity.this, "Please Select Distributor First", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TakeStockActivity.this, "Item not found please try again", Toast.LENGTH_SHORT).show();
            }
        });

        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(TakeStockActivity.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
        Type typeDistributor = new TypeToken<ArrayList<DistributerBean>>() {
        }.getType();
        mDistributerList = mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor) == null ? new ArrayList<>() : (ArrayList<DistributerBean>) mDistributerListPref.getArray(Constant.DISTRIBUTOR_LIST_OBJ, typeDistributor);
        getDistributorTaskCalling = new mDistributorList().execute(mUserLoginInfoBean.getUserTerritoryId());

        spnDistributer.setOnClickListener(v -> {
            if (mDistributerList != null) {
                is_stock_placed = "1";
                final Dialog dialog = new Dialog(TakeStockActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.statelist_dialogbox);
                final EditText etserach = dialog.findViewById(R.id.edittext_dialog);
                final ListView listView_Counry = dialog.findViewById(R.id.dialogbox_listview);
                dialog.show();
                mDistributorSpinnerAdapter = new DistributorSpinnerAdapter(TakeStockActivity.this, mDistributerList, "TakeOrder");
                listView_Counry.setAdapter(mDistributorSpinnerAdapter);

                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String st = etserach.getText().toString();
                        if (!s.equals("") && s.length() > 0) {
                            mDistributorSpinnerAdapter.filter(st);
                        } else {
                            mDistributorSpinnerAdapter.filter(st);
                        }
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
                        takeStockList = null;
                        isShowProgress = true;
                        if (takeStockList == null) {
                            takeStockList = new ArrayList();
                        } else {
                            Collections.sort(takeStockList, Collections.reverseOrder(new TakeStockUserSaved.OrderByTimeStampComparator()));
                            if (takeStockList.size() > 5) {
                                ArrayList<TakeStockUserSaved> takeStockListtemp = new ArrayList<>();
                                for (int i = 0; i < 5; i++) {
                                    takeStockListtemp.add(takeStockList.get(i));
                                }
                                takeStockList.clear();
                                takeStockList = takeStockListtemp;
                            }
                        }

                        takeStockAdapter = new TakeStockAdapter(TakeStockActivity.this, takeStockList, selectedDistributerBean);
                        lvShowStock.setAdapter(takeStockAdapter);

                        spinerSelectedPosition = position;
                        ComplexPreferences complexPreferences1 = ComplexPreferences.getComplexPreferences(TakeStockActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
                        UserLoginInfoBean mUserLoginInfoBean1 = complexPreferences1.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
                        String distributerId = mDistributerList.get(position).getDistribution_id();
                        String userId = mUserLoginInfoBean1.getUserId();
                        mGetStockListObj = new mGetStockList().execute("getstocklist", userId, distributerId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        });

        ivAdd.setOnClickListener(v -> {
            if (selectedDistributerBean != null) {
                /*if (is_stock_placed.equals("1"))*/
                if (takeStockList != null) {
                    if (takeStockList.size() > 0) {
                        if (!checkTwoDates(takeStockList.get(0).getStock_time())) {
                            startActivity(new Intent(TakeStockActivity.this, AddTakeStock.class).putExtra("firmName", selectedDistributerBean.getFirm_name()).putExtra("distribbuterId", selectedDistributerBean.getDistribution_id()).putExtra("distributornumber", selectedDistributerBean.getMobile_no()));

                        } else {
                            Toast.makeText(TakeStockActivity.this, "You have already punched the stock.", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        startActivity(new Intent(TakeStockActivity.this, AddTakeStock.class).putExtra("firmName", selectedDistributerBean.getFirm_name()).putExtra("distribbuterId", selectedDistributerBean.getDistribution_id()).putExtra("distributornumber", selectedDistributerBean.getMobile_no()));

                    }
                } else {
                    startActivity(new Intent(TakeStockActivity.this, AddTakeStock.class).putExtra("firmName", selectedDistributerBean.getFirm_name()).putExtra("distribbuterId", selectedDistributerBean.getDistribution_id()).putExtra("distributornumber", selectedDistributerBean.getMobile_no()));

                }

               /* else
                    Toast.makeText(TakeStockActivity.this, "You can take one stock at a time", Toast.LENGTH_SHORT).show();
         */
            } else
                Toast.makeText(TakeStockActivity.this, "Please Select Distributor", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        isShowProgress = false;
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(TakeStockActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        String userId = mUserLoginInfoBean.getUserId();
        if (selectedDistributerBean != null)
            mGetStockListObj = new mGetStockList().execute("getstocklist", userId, selectedDistributerBean.getDistribution_id());
//        }

    }

    public boolean checkTwoDates(String datelong) {

        long dateStr = Long.parseLong(datelong);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(dateStr * 1000);
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        return date.equalsIgnoreCase(formattedDate);


    }

    @SuppressLint("StaticFieldLeak")
    public class mDistributorList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(TakeStockActivity.this);
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
//                        Utils.showToast(TakeStockActivity.this, jsonObject.getString("message"));
//                        Toast.makeText(TakeStockActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
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
                        ComplexPreferences mDistributerListPref = ComplexPreferences.getComplexPreferences(TakeStockActivity.this, Constant.DISTRIBUTOR_LIST_PREF, MODE_PRIVATE);
                        mDistributerListPref.putObject(Constant.DISTRIBUTOR_LIST_OBJ, mDistributerArraylist);
                        mDistributerListPref.commit();
                    } else {
                        Utils.showToast(TakeStockActivity.this, jsonObject.getString("message"));
//                        Toast.makeText(TakeStockActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showAlertDialog(TakeStockActivity.this, "Stock", "Improper response from server", true);
//                Toast.makeText(TakeStockActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mGetStockList extends AsyncTask<String, Void, JSONObject> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(TakeStockActivity.this);
            mProgressDialog.setMessage("Getting Previous Stock List......");
            mProgressDialog.setCancelable(false);
            if (isShowProgress || (takeStockList == null || takeStockList.isEmpty()))
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
                        takeStockList = new ArrayList<>();
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jobj = jArray.getJSONObject(i);
                            Log.d("Stock_Case_Size", jobj.toString());

                            String stock_uni_id = jobj.getString("stock_uni_id");
                            String stock_time = jobj.getString("stock_time");
                            String isStockPlaced = jobj.getString("is_stock_placed");
                            JSONArray itemarry = jobj.getJSONArray("items");
                            if (jobj.getString("is_stock_placed").equals("0"))
                                is_stock_placed = "0";
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
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(TakeStockActivity.this, Constant.takestocklistPref, MODE_PRIVATE);
                        complexPreferences.putObject(Constant.takestocklistPrefobj, takeStockList);
                        complexPreferences.commit();


                        Collections.sort(takeStockList, Collections.reverseOrder(new TakeStockUserSaved.OrderByTimeStampComparator()));

                        if (takeStockList.size() > 5) {
                            ArrayList<TakeStockUserSaved> takeStockListtemp = new ArrayList<>();
                            for (int i = 0; i < 5; i++) {
                                takeStockListtemp.add(takeStockList.get(i));
                            }
                            takeStockList.clear();
                            takeStockList = takeStockListtemp;
                        }
                        takeStockAdapter = new TakeStockAdapter(TakeStockActivity.this, takeStockList, selectedDistributerBean);
                        lvShowStock.setAdapter(takeStockAdapter);

                    } else {
//                        Toast.makeText(TakeStockActivity.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                        Utils.showToast(TakeStockActivity.this, jsonObject.getString("message"));
                        is_stock_placed = "1";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertDialog(TakeStockActivity.this, "Stock", "Improper response from server", true);
//                Toast.makeText(TakeStockActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
        }
    }

   /* public Boolean getFormattedDate(String getStockTime) {
        int unitime1 = Integer.parseInt(getStockTime);
        Date date1 = new Date(unitime1 * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy  hh:mm a", Locale.getDefault()); // the format of your date
        sdf.setTimeZone(TimeZone.getDefault()); // give a timezone reference for formating (see comment at the bottom
        String formattedTime = sdf.format(date1);
        String monthNumber = (String) DateFormat.format("MM", date1);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        String currentMonth= String.valueOf(month);
        return monthNumber.equals(currentMonth);

    }*/
}