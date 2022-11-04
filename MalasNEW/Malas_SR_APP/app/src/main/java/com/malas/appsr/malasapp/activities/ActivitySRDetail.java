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

import com.malas.appsr.malasapp.BeanClasses.OutletDetailsInActivity;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.SRDetailAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by Arwa on 19-Sep-18.
 * *
 */

public class ActivitySRDetail extends AppCompatActivity {
    ListView lvOutletInActivity;
    SRDetailAdapter srDetailAdapter;
    ArrayList<OutletDetailsInActivity> outletDetailsInActivities;
    String activityName, activityId, asmId;
    TextView tvEmpty;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sr_activity_detail);
        Intent intent = getIntent();
        if (intent != null) {
            activityName = intent.getStringExtra("actName");
            activityId = intent.getStringExtra("actId");
            asmId = intent.getStringExtra("asmId");
            Objects.requireNonNull(getSupportActionBar()).setTitle(activityName.toUpperCase());

        }
        context = ActivitySRDetail.this;
        initView();
    }

    private void initView() {
        lvOutletInActivity = findViewById(R.id.lv_outlet_activity);
        tvEmpty = findViewById(R.id.lv_empty);
        new OutletList().execute(asmId, activityId);

    }

    @SuppressLint("StaticFieldLeak")
    public class OutletList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(ActivitySRDetail.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getoutletListInActivity"));
            nameValuePair.add(new BasicNameValuePair("asm_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("act_id", params[1]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        lvOutletInActivity.setVisibility(View.VISIBLE);
                        tvEmpty.setVisibility(View.GONE);
                        JSONArray mJsonArray = object.getJSONArray("outletList");
                        outletDetailsInActivities = new ArrayList<>();
                        for (int i = 0; i < mJsonArray.length(); i++) {

                            String act_id = mJsonArray.getJSONObject(i).getString("actId");
                            String asmId = mJsonArray.getJSONObject(i).getString("asmId");
                            String outletId = mJsonArray.getJSONObject(i).getString("outletId");
                            String outletName = mJsonArray.getJSONObject(i).getString("outletName");
                            String outletUsername = mJsonArray.getJSONObject(i).getString("outletUserName");
                            String outletPassword = mJsonArray.getJSONObject(i).getString("outletPassword");

                            OutletDetailsInActivity outletDetailsInActivity = new OutletDetailsInActivity(act_id, asmId, outletId, outletName, outletUsername, outletPassword);
                            outletDetailsInActivities.add(outletDetailsInActivity);
                        }


                        JSONArray mJsonArray1 = object.getJSONArray("FeedbackList");
                        if (mJsonArray1.length() > 0) {
                            for (int i = 0; i < mJsonArray1.length(); i++) {

                                String outletId = mJsonArray1.getJSONObject(i).getString("outletId");
                                String avg_qty = mJsonArray1.getJSONObject(i).getString("avg_qty");
                                String avg_taste = mJsonArray1.getJSONObject(i).getString("avg_taste");
                                String avg_packaging = mJsonArray1.getJSONObject(i).getString("avg_packaging");

                                for (int j = 0; j < outletDetailsInActivities.size(); j++) {
                                    if (outletDetailsInActivities.get(j).getOutletId().equalsIgnoreCase(outletId)) {
                                        OutletDetailsInActivity outletDetailsInActivity = new OutletDetailsInActivity(outletDetailsInActivities.get(j).getActId(), outletDetailsInActivities.get(j).getAsmId(), outletDetailsInActivities.get(j).getOutletId(), outletDetailsInActivities.get(j).getOutletName(), outletDetailsInActivities.get(j).getUserNameOutlet(), outletDetailsInActivities.get(j).getPasswordOutlet(), avg_qty, avg_taste, avg_packaging);

                                        outletDetailsInActivities.set(j, outletDetailsInActivity);
                                        break;
                                    }
                                }


                            }
                        }

                        srDetailAdapter = new SRDetailAdapter(context, outletDetailsInActivities);
                        lvOutletInActivity.setAdapter(srDetailAdapter);

                        lvOutletInActivity.setOnItemClickListener((parent, v, position, id) -> {
                            Intent intent = new Intent(context, ActivitySelectDetail.class);
                            intent.putExtra("outletUsername", outletDetailsInActivities.get(position).getUserNameOutlet());
                            intent.putExtra("outletPassword", outletDetailsInActivities.get(position).getPasswordOutlet());
                            intent.putExtra("outletName", outletDetailsInActivities.get(position).getOutletName());
                            intent.putExtra("asmId", asmId);
                            intent.putExtra("actId", activityId);
                            intent.putExtra("actName", activityName);
                            intent.putExtra("outletId", outletDetailsInActivities.get(position).getOutletId());
                            startActivity(intent);
                        });
                    } else {
                        lvOutletInActivity.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setText(object.getString("message"));
                        //Utils.showAlertDialog(BroadcastActivity.this, "Broadcast Message", object.getString("message"), true);
                        // Toast.makeText(ActivitySRDetail.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    lvOutletInActivity.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);

                    tvEmpty.setText("OOPS! Something went wrong");

                    // Toast.makeText(ActivitySRDetail.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            } else {
                lvOutletInActivity.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText("Improper response from server");
                // Utils.showAlertDialog(BroadcastActivity.this, "Broadcast Message", "Improper response from server", true);
                // Toast.makeText(ActivitySRDetail.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }

    }

}
