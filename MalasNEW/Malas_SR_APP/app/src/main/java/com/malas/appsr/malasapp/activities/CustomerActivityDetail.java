package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;

import com.malas.appsr.malasapp.BeanClasses.CustomerData;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.CustomerDetailAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Arwa on 04-Oct-18.
 */

public class CustomerActivityDetail extends AppCompatActivity {
    ListView lvCustDetail;
    ArrayList<CustomerData> customerDataArray;
    TextView tvEmpty;
    private Context context;
    private String actId;
    private String asmId;
    private String outletId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        Intent intent = getIntent();
        if (intent != null) {
            String outletName = getIntent().getStringExtra("outletName");
            String actName = getIntent().getStringExtra("actName");
            actId = getIntent().getStringExtra("actId");
            asmId = getIntent().getStringExtra("asmId");
            outletId = getIntent().getStringExtra("outletId");

            Objects.requireNonNull(getSupportActionBar()).setTitle(outletName.toUpperCase());
        }

        context = CustomerActivityDetail.this;
        initView();
        new customerDetails().execute(asmId, actId, outletId);
    }

    private void initView() {
        lvCustDetail = findViewById(R.id.lv_cust_detail);
        tvEmpty = findViewById(R.id.lv_empty);

    }

    @SuppressLint("StaticFieldLeak")
    public class customerDetails extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(CustomerActivityDetail.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getCustomerActivityDetail"));
            nameValuePair.add(new BasicNameValuePair("asm_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("act_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("outlet_id", params[2]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        lvCustDetail.setVisibility(View.VISIBLE);
                        tvEmpty.setVisibility(View.GONE);
                        JSONArray mJsonArray = object.getJSONArray("customerDataArray");
                        //JSONArray mJsonFeedbackArray = object.getJSONArray("customerFeedbackArray");
                        customerDataArray = new ArrayList<>();
                        //customerFeedbackDataArrayList = new ArrayList<>();

                        for (int i = 0; i < mJsonArray.length(); i++) {

                            String custId = mJsonArray.getJSONObject(i).getString("cid");
                            String cname = mJsonArray.getJSONObject(i).getString("cust_name");
                            String cEmail = mJsonArray.getJSONObject(i).getString("cust_email");
                            String cContact = mJsonArray.getJSONObject(i).getString("cust_contact");
                            String sold = mJsonArray.getJSONObject(i).getString("sold");
                            String qty = mJsonArray.getJSONObject(i).getString("avg_qty");
                            String taste = mJsonArray.getJSONObject(i).getString("avg_taste");
                            String packaging = mJsonArray.getJSONObject(i).getString("avg_packaging");

                            CustomerData customerData = new CustomerData(custId, cname, cEmail, cContact, sold, qty, taste, packaging);
                            customerDataArray.add(customerData);
                        }
                        CustomerDetailAdapter customerDetailAdapter = new CustomerDetailAdapter(context, customerDataArray);
                        lvCustDetail.setAdapter(customerDetailAdapter);

                    } else {
                        lvCustDetail.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setText(object.getString("message"));
                        //Toast.makeText(CustomerActivityDetail.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    lvCustDetail.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);

                    tvEmpty.setText("OOPS! Something went wrong");

                    //  Toast.makeText(CustomerActivityDetail.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            } else {
                lvCustDetail.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText("Improper response from server");
                // Utils.showAlertDialog(BroadcastActivity.this, "Broadcast Message", "Improper response from server", true);
                //   Toast.makeText(CustomerActivityDetail.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }

    }

}
