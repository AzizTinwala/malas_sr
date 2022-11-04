package com.malas.appsr.malasapp.ApiIntentService;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.malas.appsr.malasapp.Constant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amit on 11/07/2016.
 */
public class RegistrationService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "DownloadService";

    public RegistrationService() {
        super(RegistrationService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String method = intent.getStringExtra("method");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        String country_id = intent.getStringExtra("country_id");
        String district_id = intent.getStringExtra("district_id");
        String city_id = intent.getStringExtra("city_id");
        String address = intent.getStringExtra("address");
        String mobile = intent.getStringExtra("mobile");
        String dob = intent.getStringExtra("dob");
        String designation_id = intent.getStringExtra("designation_id");
        String devicetype = intent.getStringExtra("devicetype");
        String gcmid_uid = intent.getStringExtra("gcmid_uid");
        String confrmPassword = intent.getStringExtra("confrmPassword");
        String dateOfJoin = intent.getStringExtra("dateOfJoin");
        String stateId = intent.getStringExtra("state_id");
        Bundle bundle = new Bundle();
        receiver.send(STATUS_RUNNING, bundle);

        if (!TextUtils.isEmpty(method)) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("method", method));
            nameValuePair.add(new BasicNameValuePair("name", name));
            nameValuePair.add(new BasicNameValuePair("email", email));
            nameValuePair.add(new BasicNameValuePair("password", password));
            nameValuePair.add(new BasicNameValuePair("country_id", country_id));
            nameValuePair.add(new BasicNameValuePair("district_id", district_id));
            nameValuePair.add(new BasicNameValuePair("city_id", city_id));
            nameValuePair.add(new BasicNameValuePair("address", address));
            nameValuePair.add(new BasicNameValuePair("mobile", mobile));
            nameValuePair.add(new BasicNameValuePair("dob", dob));
            nameValuePair.add(new BasicNameValuePair("gcmid_uid", gcmid_uid));
            nameValuePair.add(new BasicNameValuePair("devicetype", devicetype));
            nameValuePair.add(new BasicNameValuePair("designation_id", designation_id));
            nameValuePair.add(new BasicNameValuePair("dateofjoin", dateOfJoin));
            nameValuePair.add(new BasicNameValuePair("state_id", stateId));
            System.out.println("Method:- " + method + ", name:- " + name + ", email:- " + email + ", password:- " + password + ", country_id:- " + country_id + ", district_id:- " + district_id + ", city_id:- " + city_id + ", address:- " + address + ", mobile:- " + mobile + ", dob:- " + dob + ", gcmid_uid:- " + gcmid_uid + ", devicetype:- " + devicetype);
            JSONObject jsonObjectFromUrl = new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
            try {
                /* Sending result back to activity */
                if (null != jsonObjectFromUrl) {
                    String status = jsonObjectFromUrl.getString("success");
                    if (status.equalsIgnoreCase("true")) {
                        bundle.putString(Intent.EXTRA_TEXT, jsonObjectFromUrl.getString("message"));
                        receiver.send(STATUS_FINISHED, bundle);
                    } else {
                        bundle.putString(Intent.EXTRA_TEXT, jsonObjectFromUrl.getString("message"));
                        receiver.send(STATUS_ERROR, bundle);
                        Toast.makeText(this, "No Data Avaliable", Toast.LENGTH_LONG).show();
                    }
                } else {
                    bundle.putString(Intent.EXTRA_TEXT, jsonObjectFromUrl.getString("message"));
                    receiver.send(STATUS_ERROR, bundle);
                }
            } catch (Exception e) {
                /* Sending error message back to activity */
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
        }
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }
}