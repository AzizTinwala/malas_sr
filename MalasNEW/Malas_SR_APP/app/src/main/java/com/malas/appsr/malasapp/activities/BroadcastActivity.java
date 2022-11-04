package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;

import com.malas.appsr.malasapp.BeanClasses.BroadcastBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.BroadcastAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Admin on 29-Dec-17.
 */

public class BroadcastActivity extends AppCompatActivity {

    TextView tvEmpty;
    ListView lvBroadcast;
    ArrayList<BroadcastBean> brodcastBean = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Broadcast");
        initView();
        new broadcastMessage().execute();

    }

    private void initView() {

        tvEmpty = findViewById(R.id.tv_Empty);

        lvBroadcast = findViewById(R.id.lv_broadcast);

    }

    @SuppressLint("SetTextI18n")
    private void setData(ArrayList<BroadcastBean> brodcastBean) {
        if (brodcastBean != null && brodcastBean.size() > 0) {
            lvBroadcast.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            BroadcastAdapter broadcastAdapter = new BroadcastAdapter(BroadcastActivity.this, brodcastBean);
            lvBroadcast.setAdapter(broadcastAdapter);

        } else {
            lvBroadcast.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText("DATA NOT AVAILABLE");
        }
    }

    private String getCurrentDate() {

        return new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault()).format(new Date());
    }

    private Date convertStringToDate(String dateString, String currentOrBroadcast) {
        Date date = null;
        if (currentOrBroadcast.equals("currentDate")) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

            try {
                date = format.parse(dateString);
                System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        } else {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat broadcastformat = new SimpleDateFormat("dd-MM-yyyy");


            try {
                date = broadcastformat.parse(dateString);
                System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

        return date;
    }

    @SuppressLint("StaticFieldLeak")
    public class broadcastMessage extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(BroadcastActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Distributor List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getBroadcastMessage"));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        JSONArray mJsonArray = object.getJSONArray("broadcastData");
                        for (int i = 0; i < mJsonArray.length(); i++) {

                            String time = mJsonArray.getJSONObject(i).getString("TIME");
                            String currentMonth = (String) DateFormat.format("MM", convertStringToDate(getCurrentDate(), "currentDate"));
                            String orderedMonth = (String) DateFormat.format("MM", convertStringToDate(time, "broadcast"));
                            if (currentMonth.equals(orderedMonth)) {
                                String imgUrl = mJsonArray.getJSONObject(i).getString("IMAGE PATH");
                                String title = mJsonArray.getJSONObject(i).getString("TITLE");
                                String message = mJsonArray.getJSONObject(i).getString("MESSAGE");
                                BroadcastBean broadcastBean = new BroadcastBean(imgUrl, title, message, time);
                                brodcastBean.add(broadcastBean);
                            }
                        }
                        setData(brodcastBean);
                    } else {
                        lvBroadcast.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setText(object.getString("message"));
                        // Utils.showAlertDialog(BroadcastActivity.this, "Broadcast Message", object.getString("message"), true);
//                        Toast.makeText(PlaceOrderToDistributor.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    lvBroadcast.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                    tvEmpty.setText("OOps! Something went wrong");
                    e.printStackTrace();
                }

            } else {
                lvBroadcast.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText("Improper response from server");
                // Utils.showAlertDialog(BroadcastActivity.this, "Broadcast Message", "Improper response from server", true);
//                Toast.makeText(PlaceOrderToDistributor.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }
    }
}
