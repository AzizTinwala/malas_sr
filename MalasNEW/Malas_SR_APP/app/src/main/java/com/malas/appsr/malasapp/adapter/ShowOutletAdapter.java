package com.malas.appsr.malasapp.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.HighLightBean;
import com.malas.appsr.malasapp.BeanClasses.OutletOrderDateBean;
import com.malas.appsr.malasapp.BeanClasses.OutletOrdersBean;
import com.malas.appsr.malasapp.BeanClasses.ReasonSubmitBean;
import com.malas.appsr.malasapp.BeanClasses.RouteBean;
import com.malas.appsr.malasapp.BeanClasses.SaveData;
import com.malas.appsr.malasapp.BeanClasses.ShowOutLetBeen;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderItemBean;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderListBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.activities.AddNewOutletActivity;
import com.malas.appsr.malasapp.activities.AddTakeOrder;
import com.malas.appsr.malasapp.activities.OutletOrdersActivity;
import com.malas.appsr.malasapp.activities.SRReasonActivity;
import com.malas.appsr.malasapp.dbHandler.DatabaseHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class ShowOutletAdapter extends BaseAdapter implements Filterable {
    private final UserLoginInfoBean mUserLoginInfoBean;
    Context mContext;
    private final ArrayList<ShowOutLetBeen> outletList;
    private ArrayList<ShowOutLetBeen> outletList_All;
    private final DistributerBean selectedDistributerBean;
    private final RouteBean selectedRouteBean;
    private final String from;
    private ShowOutLetBeen showOutLetBeen;
    ArrayList<OutletOrdersBean> listOutletOrders = new ArrayList<>();
    //  private String is_stock_taken;
    // ArrayList<OutletOrdersBean> listOutletOrders = new ArrayList<>();
    private ArrayList<HighLightBean> highLightBeans = new ArrayList<>();
    private final ArrayList<String> ouletIdToChangeColor = new ArrayList<>();

    private final DatabaseHandler db;
    private int pos;

    public ShowOutletAdapter(Context mContext, ArrayList<ShowOutLetBeen> outletList, DistributerBean selectedDistributerBean, RouteBean selectedRouteBean, String from, ArrayList<HighLightBean> highLightBeans) {
        this.mContext = mContext;
        this.outletList = outletList;
        this.outletList_All = new ArrayList<>(outletList);
        this.selectedDistributerBean = selectedDistributerBean;
        this.selectedRouteBean = selectedRouteBean;
        this.from = from;
        this.highLightBeans = highLightBeans;
        db = new DatabaseHandler(mContext);
        ouletIdToChangeColor.clear();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(mContext, Constant.UserRegInfoPref, MODE_PRIVATE);

        mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

        offlineAddData();
        offlineEditData();

    }

    /* public ShowOutletAdapter(Context mContext, ArrayList<ShowOutLetBeen> outletList, DistributerBean selectedDistributerBean, RouteBean selectedRouteBean, String from, String is_stock_taken, ArrayList<HighLightBean> highLightBeans) {
        this.mContext = mContext;
        this.outletList = outletList;
        this.selectedDistributerBean = selectedDistributerBean;
        this.selectedRouteBean = selectedRouteBean;
        this.from = from;

        this.highLightBeans = highLightBeans;
        db = new DatabaseHandler(mContext);
        ouletIdToChangeColor.clear();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(mContext, Constant.UserRegInfoPref, MODE_PRIVATE);

        mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

        offlineAddData();
        offlineEditData();
    }*/

    public ShowOutletAdapter(Context mContext, ArrayList<ShowOutLetBeen> outletList, DistributerBean selectedDistributerBean, RouteBean selectedRouteBean, String from) {
        this.mContext = mContext;
        this.outletList = outletList;
        this.selectedDistributerBean = selectedDistributerBean;
        this.selectedRouteBean = selectedRouteBean;
        this.from = from;
        db = new DatabaseHandler(mContext);
        ouletIdToChangeColor.clear();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(mContext, Constant.UserRegInfoPref, MODE_PRIVATE);

        mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

        offlineAddData();
        offlineEditData();
    }


    @Override
    public int getCount() {
        return outletList.size();
    }

    @Override
    public Object getItem(int position) {
        return outletList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        if (outletList.size() > 0) {

            return getCount();
        }
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (outletList.size() > 0) {
            return position;
        }
        return 1;
    }

    private String getCurrentDate() {

        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    @SuppressLint("StaticFieldLeak")
    class reasonStatus extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(mContext);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "sr_reason_status_by_outlet_id"));
            nameValuePair.add(new BasicNameValuePair("userId", mUserLoginInfoBean.getUserId()));
            nameValuePair.add(new BasicNameValuePair("date", getCurrentDate()));
            nameValuePair.add(new BasicNameValuePair("outletId", params[0]));


            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        //Log.e("Result JSON Object", "" + jsonObject.toString());
                        String count = jsonObject.getString("count");
                        db.deleteSrStatusCountByID(outletList.get(pos).getOutlet_id());
                        db.addSrReasonCountByID(mUserLoginInfoBean.getUserId(), outletList.get(pos).getOutlet_id(), getCurrentDate(), count);
                        if (count != null && count.equalsIgnoreCase("0")) {
                           /* db.deleteSrStatusByID(outletList.get(pos).getOutlet_id());
                            db.addSrReasonByOutletId(mUserLoginInfoBean.getUser_id(),outletList.get(pos).getOutlet_id(),getCurrentDate());
                         */
                            Intent intent = new Intent(mContext, SRReasonActivity.class);
                            intent.putExtra("outletBean", outletList.get(pos));
                            intent.putExtra("distributerBean", selectedDistributerBean);
                            mContext.startActivity(intent);

                        } else {
                            Utils.showToast(mContext, "Reason already available for this date ");

                        }


                    } else {
                        Utils.showToast(mContext, jsonObject.getString("message"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Utils.showToast(mContext, "Improper response from server");
//                Toast.makeText(OutletOrdersActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();

            }
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.show_outlet_row, null);
            holder.tvOutlet = convertView.findViewById(R.id.tv_outlet);
            holder.ivMenu = convertView.findViewById(R.id.iv_menu);
            holder.llOutletRow = convertView.findViewById(R.id.llOutletRow);
            holder.ivProductive = convertView.findViewById(R.id.iv_productive);
            holder.tvHighlight = convertView.findViewById(R.id.tv_highlight);
            holder.ivNonProductive = convertView.findViewById(R.id.iv_non_productive);
            holder.ivHistory = convertView.findViewById(R.id.iv_history);

            //  holder.llOutletRow.setTag(holder);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (from.equals("takeOrder")) {

            if (ouletIdToChangeColor != null && ouletIdToChangeColor.contains(outletList.get(position).getOutlet_id())) {
                holder.llOutletRow.setBackgroundResource(R.color.transparentlight);

            }
        }
        holder.tvOutlet.setText(outletList.get(position).getOutlet_name().toUpperCase(Locale.getDefault()));

        if (from.equals("takeOrder")) {
            if (highLightBeans != null) {
                if (highLightBeans.size() == outletList.size()) {
                    if (highLightBeans.get(position).getOutletId().equals(outletList.get(position).getOutlet_id())) {
                        if (highLightBeans.get(position).getHighlightOrNot().equals("true")) {

                            holder.tvOutlet.setTextColor(mContext.getResources().getColor(R.color.FireBrick));
                        } else {

                            holder.tvOutlet.setTextColor(Color.BLACK);

                        }
                    }
                }
            }
        }
        if (from.equals("takeOrder")) {
            holder.ivMenu.setVisibility(View.GONE);
            holder.ivProductive.setVisibility(View.VISIBLE);
            holder.ivNonProductive.setVisibility(View.VISIBLE);
            holder.ivHistory.setVisibility(View.VISIBLE);

            holder.ivProductive.setOnClickListener(v -> {
                // Place an Order
                /*  if (Utils.isInternetConnected(OutletOrdersActivity.this)) {*/
               showOutLetBeen= outletList.get(position);

                // Temporary Object Declaration
                ArrayList<OutletOrdersBean> ordersBeans = db.getOrdersData(showOutLetBeen.getOutlet_id());
                ArrayList<TakeOutletOrderItemBean> productList;
                ArrayList<SaveData> saveListFromDb;
                ArrayList<TakeOutletOrderItemBean> productListEdit;
                ArrayList<SaveData> saveListFromDbEdit;

                // Saving Data From Local Database in Object
                saveListFromDb = db.getAllSaveDataRecord(selectedDistributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());
                productList = db.getSaveProductRecord(selectedDistributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());


                // Saving Data From Local Database in Object
                saveListFromDbEdit = db.getEditSaveDataRecord(selectedDistributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());
                productListEdit = db.getEditSaveProductRecord(selectedDistributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());


                if (ordersBeans.size() > 0) {
                    if (saveListFromDb != null && productList != null && saveListFromDb.size() > 0 && productList.size() > 0) {
                        Toast.makeText(mContext, "Please Connect to Internet And SYNC Order data ", Toast.LENGTH_SHORT).show();

                    } else if (saveListFromDbEdit != null && productListEdit != null && saveListFromDbEdit.size() > 0 && productListEdit.size() > 0) {
                        Toast.makeText(mContext, "Please Connect to Internet And SYNC Order data ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (listOutletOrders != null) {
                            if (listOutletOrders.size() > 0) {
                                if (differenceInTwoDates(listOutletOrders.get(0).getOrder_take_time())) {
                                    Intent intent = new Intent(mContext, AddTakeOrder.class);
                                    intent.putExtra("showOutLetBeen", showOutLetBeen);
                                    intent.putExtra("distributerBean", selectedDistributerBean);
                                    mContext.startActivity(intent);
                                } else {
                                    Toast.makeText(mContext, "You have already punched the order for this outlet.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Intent intent = new Intent(mContext, AddTakeOrder.class);
                                intent.putExtra("showOutLetBeen", showOutLetBeen);
                                intent.putExtra("distributerBean", selectedDistributerBean);
                                mContext.startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(mContext, AddTakeOrder.class);
                            intent.putExtra("showOutLetBeen", showOutLetBeen);
                            intent.putExtra("distributerBean", selectedDistributerBean);
                            mContext.startActivity(intent);
                        }

                    }
                } else {

                    if (saveListFromDb != null && productList != null && saveListFromDb.size() > 0 && productList.size() > 0) {
                        Toast.makeText(mContext, "Please Connect to Internet And SYNC Order data ", Toast.LENGTH_SHORT).show();

                    } else if (saveListFromDbEdit != null && productListEdit != null && saveListFromDbEdit.size() > 0 && productListEdit.size() > 0) {
                        Toast.makeText(mContext, "Please Connect to Internet And SYNC Order data ", Toast.LENGTH_SHORT).show();
                    } else {

                        if (listOutletOrders != null) {
                            if (listOutletOrders.size() > 0) {
                                if (differenceInTwoDates(listOutletOrders.get(0).getOrder_take_time())) {

                                    Intent intent = new Intent(mContext, AddTakeOrder.class);
                                    intent.putExtra("showOutLetBeen", showOutLetBeen);
                                    intent.putExtra("distributerBean", selectedDistributerBean);
                                    mContext.startActivity(intent);
                                } else {
                                    Toast.makeText(mContext, "You have already punched the order for this outlet.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Intent intent = new Intent(mContext, AddTakeOrder.class);
                                intent.putExtra("showOutLetBeen", showOutLetBeen);
                                intent.putExtra("distributerBean", selectedDistributerBean);
                                mContext.startActivity(intent);
                            }

                        } else {
                            Intent intent = new Intent(mContext, AddTakeOrder.class);
                            intent.putExtra("showOutLetBeen", showOutLetBeen);
                            intent.putExtra("distributerBean", selectedDistributerBean);
                            mContext.startActivity(intent);
                        }

                    }
                    offlineAddData();
                    offlineEditData();

                }

            });
            holder.ivHistory.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, OutletOrdersActivity.class);
                intent.putExtra("outletBean", outletList.get(position));
                intent.putExtra("distributerBean", selectedDistributerBean);
                /*if (is_stock_taken != null)
                    intent.putExtra("is_stock_taken", is_stock_taken);*/
                mContext.startActivity(intent);
            });

            holder.ivNonProductive.setOnClickListener(view -> {

                ArrayList<ReasonSubmitBean> reasonSubmitBeans = db.getReasonSubmitRecord(selectedDistributerBean.getDistribution_id(), selectedRouteBean.getRoutes_id());
                ArrayList<String> outletId = new ArrayList<>();
                if (reasonSubmitBeans != null) {
                    for (int i = 0; i < reasonSubmitBeans.size(); i++) {
                        outletId.add(reasonSubmitBeans.get(i).getOutletId());
                    }
                }

                if (outletId.contains(outletList.get(position).getOutlet_id())) {
                    Toast.makeText(mContext, "This Outlet Data is not saved yet,Please Save the data by connecting to Internet", Toast.LENGTH_SHORT).show();
                } else {
                    pos = position;
                    if (Utils.isInternetConnected(mContext)) {
                        new reasonStatus().execute(outletList.get(position).getOutlet_id());

                    } else {
                        /*int count=db.getSrStatusById(mUserLoginInfoBean.getUser_id(),outletList.get(pos).getOutlet_id(),getCurrentDate());
                        if (count==0) {*/
                        int srStatusBycount = db.getSrStatusCountById(mUserLoginInfoBean.getUserId(), outletList.get(pos).getOutlet_id(), getCurrentDate());
                        if (srStatusBycount == 0) {
                            Intent intent = new Intent(mContext, SRReasonActivity.class);
                            intent.putExtra("outletBean", outletList.get(pos));
                            intent.putExtra("distributerBean", selectedDistributerBean);
                            mContext.startActivity(intent);
                        } else {
                            Utils.showToast(mContext, "Reason already available for this date ");

                        }


                       /* } else {
                            Utils.showToast(mContext, "Reason already available for this date ");

                        }*/
                    }


                }


            });

        } else {
            holder.ivProductive.setVisibility(View.GONE);
            holder.ivNonProductive.setVisibility(View.GONE);
            holder.ivHistory.setVisibility(View.GONE);

            holder.ivMenu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(mContext, v);
//                public MenuItem add(int groupId, int itemId, int order, CharSequence title);
                popup.getMenu().add(0, 1, 0, "Edit");
//                    popup.getMenu().add(0, 2, 1, "Delete");

                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == 1) {
                        if (selectedDistributerBean == null) {
                            Toast.makeText(mContext, "Please select a distributor", Toast.LENGTH_SHORT).show();
                        } else if (selectedRouteBean == null) {
                            Toast.makeText(mContext, "Please select a route", Toast.LENGTH_SHORT).show();
                        } else {
                            mContext.startActivity(new Intent(mContext, AddNewOutletActivity.class)
                                    .putExtra("distributerName", selectedDistributerBean.getFirm_name())
                                    .putExtra("distributiorId", selectedDistributerBean.getDistribution_id())
                                    .putExtra("routeName", selectedRouteBean.getRoute_name())
                                    .putExtra("routeId", selectedRouteBean.getRoutes_id())
                                    .putExtra("showOutletBean", outletList.get(position))
                                    .putExtra("selectedDistributerBean", selectedDistributerBean));
                        }
                        //    mContectActivityAdapterAdapter.locationFilter(mUserRegistrationInfoBean.getUsercurrentCity());

                        //                                case 2:
//                                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(mContext, Constant.UserRegInfoPref, mContext.MODE_PRIVATE);
//                                    UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
//
//                                    new mDeleteOutLetAsync().execute(mUserLoginInfoBean.getUser_id(), outletList.get(position).getOutlet_id());
                    }

                    return true;
                });

                popup.show();


            });
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvOutlet;
        ImageView ivMenu;
        LinearLayout llOutletRow;
        ImageView ivProductive;
        ImageView ivNonProductive;
        ImageView ivHistory;
        TextView tvHighlight;
    }

 /*   public void setIsStockTaken(String is_stock_taken) {
        this.is_stock_taken = is_stock_taken;
    }*/

    private ArrayList<String> offlineEditData() {

        ArrayList<SaveData> saveEditData;


        for (int i = 0; i < outletList.size(); i++) {
            saveEditData = db.getEditSaveDataRecord(selectedDistributerBean.getDistribution_id(), outletList.get(i).getOutlet_id());
            if (saveEditData != null && saveEditData.size() > 0) {
                ouletIdToChangeColor.add(saveEditData.get(0).getOutletId());

            }
        }

        return ouletIdToChangeColor;

    }

    private ArrayList<String> offlineAddData() {
        ArrayList<SaveData> saveList;

        for (int i = 0; i < outletList.size(); i++) {
            saveList = db.getAllSaveDataRecord(selectedDistributerBean.getDistribution_id(), outletList.get(i).getOutlet_id());

            if (saveList != null && saveList.size() > 0) {
                ouletIdToChangeColor.add(outletList.get(i).getOutlet_id());

            }
        }

        return ouletIdToChangeColor;
    }

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

    public class Getalloutletorderlist extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;
        DistributerBean distributerBean;
        String is_order_placed;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(mContext);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading List please wait");
            mDialog.setCancelable(false);
            mDialog.show();
            distributerBean = selectedDistributerBean;
            is_order_placed = "";
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

                            ArrayList<TakeOutletOrderListBean> itemList = new ArrayList<>();

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
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(mContext, Constant.SHOW_OUTLET_ORDER_PREF, MODE_PRIVATE);
                        complexPreferences.putObject(Constant.SHOW_OUTLET_ORDER_OBJ, listOutletOrders);
                        complexPreferences.commit();

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
                    } else {
                        db.addOutletNoOrder(showOutLetBeen.getOutlet_id());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
        }

    }

    public String convertIntoDateTime(long datetime) {
        // Log.e("order_take_time ", "" + datetime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datetime * 1000L);
        Date date = calendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault());
        //Log.e("order_take_time", "" + date);
        return simpleDateFormat.format(date);
    }

    private Date convertStringToDate(String dateString) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Date date = null;
        try {
            date = format.parse(dateString);
            System.out.println(date);
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<ShowOutLetBeen> filterList = new ArrayList<>();
                if (charSequence.toString().isEmpty()) {
                    filterList.addAll(outletList_All);

                } else {
                    ArrayList<ShowOutLetBeen> temp = outletList_All;
                    for (ShowOutLetBeen i : outletList_All) {
                        if (i.getOutlet_name().toLowerCase(Locale.getDefault())
                                .contains(
                                        charSequence.toString().toLowerCase(Locale.getDefault())
                                )
                        ) {
                            filterList.add(i);
                        }

                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                outletList.clear();
                outletList.addAll((Collection<? extends ShowOutLetBeen>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

}