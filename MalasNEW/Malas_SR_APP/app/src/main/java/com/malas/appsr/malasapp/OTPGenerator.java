package com.malas.appsr.malasapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.malas.appsr.malasapp.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("Registered")
public class OTPGenerator extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        new getProductivity().execute();
    }

    @SuppressLint("StaticFieldLeak")
    class getProductivity extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(OTPGenerator.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Logging in please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String Baseurl = "http://login.businesslead.co.in/api/mt/SendSMS?user=Malas&password=malas123&senderid=MalaFP&channel=Trans&DCS=0&flashsms=0&number=919422346652&text=hello&route=6" ;

            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("userId", "5"));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Baseurl, nameValuePair, HttpUrlConnectionJSONParser.Http.GET);

        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject jsonResponse) {
            if (jsonResponse != null) {

                Toast.makeText(OTPGenerator.this,"SUceess",Toast.LENGTH_SHORT);
            }
        }
    }

}
