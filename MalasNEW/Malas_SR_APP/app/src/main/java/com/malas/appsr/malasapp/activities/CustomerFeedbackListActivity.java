package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;

import com.malas.appsr.malasapp.BeanClasses.CustomerFeedbackData;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.CustomerFeedbackAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Arwa on 08-Oct-18.
 */

public class CustomerFeedbackListActivity extends Activity implements View.OnClickListener {
    String cid;
    CustomerFeedbackAdapter customerFeedbackAdapter;
    ArrayList<CustomerFeedbackData> customerFeedbackDataArray;
    Button btnBack;
    private ListView listView;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_customer_feedback_list_details);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Intent intent = getIntent();
        if (intent != null) {
            cid = getIntent().getStringExtra("customerId");


        }

        context = CustomerFeedbackListActivity.this;
        initView();

    }

    private void initView() {
        listView = findViewById(R.id.lv_feedback);
        btnBack = findViewById(R.id.btn_ok);

        new customerDetails().execute(cid);


        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    public class customerDetails extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(context);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getCustomerFeedbackDetail"));
            nameValuePair.add(new BasicNameValuePair("cid", params[0]));

            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {

                        JSONArray mJsonFeedbackArray = object.getJSONArray("customerFeedbackArray");
                        customerFeedbackDataArray = new ArrayList<>();
                        for (int i = 0; i < mJsonFeedbackArray.length(); i++) {


                            String cid = mJsonFeedbackArray.getJSONObject(i).getString("id");
                            String qty = mJsonFeedbackArray.getJSONObject(i).getString("qty");
                            String taste = mJsonFeedbackArray.getJSONObject(i).getString("taste");
                            String packaging = mJsonFeedbackArray.getJSONObject(i).getString("packaging");
                            String product_name = mJsonFeedbackArray.getJSONObject(i).getString("product_name");

                            CustomerFeedbackData customerFeedbackData = new CustomerFeedbackData(cid, qty, taste, packaging, product_name);
                            customerFeedbackDataArray.add(customerFeedbackData);
                        }
                        customerFeedbackAdapter = new CustomerFeedbackAdapter(context, customerFeedbackDataArray);

                        listView.setAdapter(customerFeedbackAdapter);
                    } else {

                        //Utils.showAlertDialog(BroadcastActivity.this, "Broadcast Message", object.getString("message"), true);
                        Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
                    finish();
                    e.printStackTrace();
                }

            } else {

                // Utils.showAlertDialog(BroadcastActivity.this, "Broadcast Message", "Improper response from server", true);
                Toast.makeText(context, "Improper response from server", Toast.LENGTH_SHORT).show();
                finish();
            }
            mDialog.dismiss();
        }

    }

}
