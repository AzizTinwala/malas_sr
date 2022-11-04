package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.malas.appsr.malasapp.BeanClasses.ImageActivityBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.UploadImageForActivityAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Arwa on 24-Oct-18.
 */

public class UploadImageForActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1;
    ArrayList<ImageActivityBean> imageActivityBeans;
    TextView tvEmpty;
    FloatingActionButton fab;
    private String outletId;
    private String actId;
    private GridView gridView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        Intent intent = getIntent();
        if (intent != null) {

            String outletName = getIntent().getStringExtra("outletName");
            String actName = getIntent().getStringExtra("actName");
            actId = getIntent().getStringExtra("actId");
            String asmId = getIntent().getStringExtra("asmId");
            outletId = getIntent().getStringExtra("outletId");
            Objects.requireNonNull(getSupportActionBar()).setTitle(outletName);

        }


        initView();
    }

    private void initView() {
        gridView = findViewById(R.id.galleryGridView);
        tvEmpty = findViewById(R.id.lv_empty);
        fab = findViewById(R.id.fab);
        new uploadImageList().execute(actId, outletId);
        fab.setOnClickListener(view -> openImageChooser());

    }

    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                String uploadImage = getStringImage(bitmap);
                new uploadImage().execute(uploadImage, actId, outletId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @SuppressLint("StaticFieldLeak")
    public class uploadImageList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(UploadImageForActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "getUploadedImageOfActivity"));
            nameValuePair.add(new BasicNameValuePair("act_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("outlet_id", params[1]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        gridView.setVisibility(View.VISIBLE);
                        tvEmpty.setVisibility(View.GONE);
                        JSONArray mJsonArray = object.getJSONArray("imageDataArray");
                        imageActivityBeans = new ArrayList<>();
                        for (int i = 0; i < mJsonArray.length(); i++) {

                            String img_id = mJsonArray.getJSONObject(i).getString("img_id");
                            String img_url = mJsonArray.getJSONObject(i).getString("img_url");

                            ImageActivityBean imageActivityBean = new ImageActivityBean(img_id, img_url);
                            imageActivityBeans.add(imageActivityBean);
                        }
                        gridView.setAdapter(new UploadImageForActivityAdapter(UploadImageForActivity.this, imageActivityBeans));


                    } else {
                        gridView.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setText(object.getString("message"));
                    }
                } catch (JSONException e) {
                    gridView.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                    tvEmpty.setText("OOPS! Something went wrong");

                    e.printStackTrace();
                }

            } else {
                gridView.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText("Improper response from server");
            }
            mDialog.dismiss();
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class uploadImage extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(UploadImageForActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Loading Activity List please wait");
            mDialog.setCancelable(false);
            mDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "uploadImageActivity"));
            nameValuePair.add(new BasicNameValuePair("image", params[0]));
            nameValuePair.add(new BasicNameValuePair("act_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("outlet_id", params[2]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            if (object != null) {
                try {
                    if (object.getString("success").equalsIgnoreCase("true")) {
                        new uploadImageList().execute(actId, outletId);
                        Toast.makeText(UploadImageForActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(UploadImageForActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(UploadImageForActivity.this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            } else {
                Toast.makeText(UploadImageForActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }

    }

}
