package com.malas.appsr.malasapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.malas.appsr.malasapp.R;

import java.util.Objects;

/**
 * Created by Arwa on 02-Oct-18.
 */


public class ActivitySelectDetail extends AppCompatActivity implements View.OnClickListener {


    private String usename;
    private String password;
    private String outletName;
    private String actName;
    private String actId;
    private String asmId;


    private String outletId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        if (intent != null) {
            usename = getIntent().getStringExtra("outletUsername");
            password = getIntent().getStringExtra("outletPassword");
            outletName = getIntent().getStringExtra("outletName");
            actName = getIntent().getStringExtra("actName");
            actId = getIntent().getStringExtra("actId");
            asmId = getIntent().getStringExtra("asmId");
            outletId = getIntent().getStringExtra("outletId");
            Objects.requireNonNull(getSupportActionBar()).setTitle(outletName);

        }

        initView();
    }

    private void initView() {
        Button btnCustomerDetail = findViewById(R.id.btn_customer_details);
        Button btnLoginDetails = findViewById(R.id.btn_login_details);
        Button btnUploadImage = findViewById(R.id.btn_upload);
        btnCustomerDetail.setOnClickListener(this);
        btnLoginDetails.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_customer_details:
                Intent intent1 = new Intent(ActivitySelectDetail.this, CustomerActivityDetail.class);
                intent1.putExtra("asmId", asmId);
                intent1.putExtra("actId", actId);
                intent1.putExtra("actName", actName);
                intent1.putExtra("outletId", outletId);
                intent1.putExtra("outletName", outletName);

                startActivity(intent1);
                break;
            case R.id.btn_login_details:
                Intent intent = new Intent(ActivitySelectDetail.this, ActivityLoginDetailsDialog.class);
                intent.putExtra("outletUsername", usename);
                intent.putExtra("outletPassword", password);

                startActivity(intent);
                break;
            case R.id.btn_upload:

                Intent intent2 = new Intent(ActivitySelectDetail.this, UploadImageForActivity.class);
                intent2.putExtra("asmId", asmId);
                intent2.putExtra("actId", actId);
                intent2.putExtra("actName", actName);
                intent2.putExtra("outletId", outletId);
                intent2.putExtra("outletName", outletName);

                startActivity(intent2);
                break;


        }


    }


}