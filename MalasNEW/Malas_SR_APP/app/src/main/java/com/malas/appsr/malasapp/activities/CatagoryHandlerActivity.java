package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;

import com.malas.appsr.malasapp.BeanClasses.CategoryListBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.CategoryListAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CatagoryHandlerActivity extends AppCompatActivity {
    ArrayList<CategoryListBean> catagoryInfoList;
    ListView categoryListview;
    AsyncTask<String, Void, JSONObject> mGetCategoryList;
    CategoryListAdapter categoryListAdapter;
    Button btnCategorysubmit;
    String categoryHandlerData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_catagory);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Category Handler");
        categoryListview = findViewById(R.id.list_view);
        btnCategorysubmit = findViewById(R.id.bt_category_submit);
        mGetCategoryList = new mGetCategoryList().execute();

        if (getIntent().hasExtra("categoryHandlerData")) {
            if (getIntent().getStringExtra("categoryHandlerData") != null) {
                categoryHandlerData = getIntent().getStringExtra("categoryHandlerData");
            }
        }

        btnCategorysubmit.setOnClickListener(v -> {

            ArrayList<String> seletedOption = new ArrayList<>();
            for (int i = 0; i < catagoryInfoList.size(); i++) {
                seletedOption.add(catagoryInfoList.get(i).getSelectedOption());

            }
            if (seletedOption.contains("")) {
                Toast.makeText(CatagoryHandlerActivity.this, "Please Fill every category option ", Toast.LENGTH_SHORT).show();
            } else {

                try {

                    JSONArray mJsonArray = new JSONArray();
                    for (int i = 0; i < catagoryInfoList.size(); i++) {
                        if (catagoryInfoList.get(i).getSelectedOption().equalsIgnoreCase("Y") || catagoryInfoList.get(i).getSelectedOption().equalsIgnoreCase("N") || catagoryInfoList.get(i).getSelectedOption().equalsIgnoreCase("NA")) {
                            JSONObject mJsonObject = new JSONObject();
                            mJsonObject.put("cat_id", catagoryInfoList.get(i).getId());
                            mJsonObject.put("cat_status", catagoryInfoList.get(i).getSelectedOption());
                            mJsonArray.put(mJsonObject);
                        }
                    }
                    System.out.println(mJsonArray);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("selectedCategory", new JSONObject().put("category_list", mJsonArray).toString());
                    System.out.println(new JSONObject().put("category_list", mJsonArray));
                    setResult(1, returnIntent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CatagoryHandlerActivity.this, "Unable to process please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGetCategoryList != null && !mGetCategoryList.isCancelled())
            mGetCategoryList.cancel(true);

    }

    @SuppressLint("StaticFieldLeak")
    public class mGetCategoryList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(CatagoryHandlerActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle(" please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getcategorylist"));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        catagoryInfoList = new ArrayList<>();
                        System.out.print(jsonObject.getString("message"));
                   //     Toast.makeText(CatagoryHandlerActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONArray mJsonarry = jsonObject.getJSONArray("categorylist");
                        for (int j = 0; j < mJsonarry.length(); j++) {
                            String selectedOption = "";

                            if (categoryHandlerData != null) {
                                JSONObject mJsonObjInfo = mJsonarry.getJSONObject(j);
                                String category_name = mJsonObjInfo.getString("category_name");
                                String id = mJsonObjInfo.getString("id");
                                String status = mJsonObjInfo.getString("status");
                                JSONObject jsonObject1 = new JSONObject(categoryHandlerData);
                                JSONArray mSelectedJSONArr = jsonObject1.getJSONArray("category_list");
                                for (int i = 0; i < mSelectedJSONArr.length(); i++) {
                                    if (mSelectedJSONArr.getJSONObject(i).getString("cat_id").equals(id)) {
                                        selectedOption = mSelectedJSONArr.getJSONObject(i).getString("cat_status");
                                        status = "1";
                                    }
                                }
                                catagoryInfoList.add(new CategoryListBean(category_name, id, status, selectedOption));

                            } else {
                                JSONObject mJsonObjInfo = mJsonarry.getJSONObject(j);
                                String category_name = mJsonObjInfo.getString("category_name");
                                String id = mJsonObjInfo.getString("id");
                                String status = mJsonObjInfo.getString("status");
                                catagoryInfoList.add(new CategoryListBean(category_name, id, status, ""));
                            }
                        }
                        categoryListAdapter = new CategoryListAdapter(CatagoryHandlerActivity.this, catagoryInfoList);
                        categoryListview.setAdapter(categoryListAdapter);
                    } else {
                        Toast.makeText(CatagoryHandlerActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(CatagoryHandlerActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }
    }
}