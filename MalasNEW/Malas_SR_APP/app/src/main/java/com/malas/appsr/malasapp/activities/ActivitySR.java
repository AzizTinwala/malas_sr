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
import com.Amitlibs.utils.ComplexPreferences;

import com.malas.appsr.malasapp.BeanClasses.ActivitySRBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.ActivityAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Arwa on 18-Sep-18.
 */


public class ActivitySR extends AppCompatActivity {
    ArrayList<ActivitySRBean> activitySRBeanList;
    ListView lvActivityList;
    TextView tvEmpty;
    private Context context;
    private UserLoginInfoBean mUserLoginInfoBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sr_activity);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Activity List");
        context = ActivitySR.this;
        initView();

    }

    private void initView() {
        lvActivityList = findViewById(R.id.gridview);
        tvEmpty = findViewById(R.id.lv_empty);
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ActivitySR.this, Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);


        new activityList().execute(mUserLoginInfoBean.getAsmId());


    }


    @SuppressLint("StaticFieldLeak")
    public class activityList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(ActivitySR.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "sr_activity_list"));
            nameValuePair.add(new BasicNameValuePair("asm_id", params[0]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        lvActivityList.setVisibility(View.VISIBLE);
                        tvEmpty.setVisibility(View.GONE);
                        JSONArray mJsonArray = object.getJSONArray("activityList");
                        activitySRBeanList = new ArrayList<>();
                        for (int i = 0; i < mJsonArray.length(); i++) {

                            String act_id = mJsonArray.getJSONObject(i).getString("act_id");
                            String act_name = mJsonArray.getJSONObject(i).getString("act_name");
                            String act_start_date = mJsonArray.getJSONObject(i).getString("act_start_date");
                            String act_end_date = mJsonArray.getJSONObject(i).getString("act_end_date");

                            ActivitySRBean activitySRBean = new ActivitySRBean(act_id, act_name, act_start_date, act_end_date);
                            activitySRBeanList.add(activitySRBean);
                        }
                        lvActivityList.setAdapter(new ActivityAdapter(ActivitySR.this, activitySRBeanList));

                        lvActivityList.setOnItemClickListener((parent, v, position, id) -> {

                            Intent intent = new Intent(context, ActivitySRDetail.class);
                            intent.putExtra("actName", activitySRBeanList.get(position).getAct_name());
                            intent.putExtra("actId", activitySRBeanList.get(position).getAct_id());
                            intent.putExtra("asmId", mUserLoginInfoBean.getAsmId());

                            startActivity(intent);
                        });
                    } else {
                        lvActivityList.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setText(object.getString("message"));
                    }
                } catch (JSONException e) {
                    lvActivityList.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                    tvEmpty.setText("OOPS! Something went wrong");
                    e.printStackTrace();
                }

            } else {
                lvActivityList.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText("Improper response from server");
            }
            mDialog.dismiss();
        }

    }
}

