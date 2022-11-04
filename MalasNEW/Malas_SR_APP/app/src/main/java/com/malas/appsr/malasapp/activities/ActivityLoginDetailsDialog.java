package com.malas.appsr.malasapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.malas.appsr.malasapp.R;


/**
 * Created by Arwa on 03-Oct-18.
 */

public class ActivityLoginDetailsDialog extends Activity implements View.OnClickListener {
    TextView tvUserName;
    TextView tvPassword;
    TextView tvURL;
    String usename;
    String password;
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_details_outlet);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        usename = getIntent().getStringExtra("outletUsername");
        password = getIntent().getStringExtra("outletPassword");
        initview();


    }

    private void initview() {
        tvUserName = findViewById(R.id.tv_username_detail);
        tvPassword = findViewById(R.id.tv_password_detail);
        tvURL = findViewById(R.id.tv_url);
        btnOk = findViewById(R.id.btn_ok);
        tvURL.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        tvUserName.setText(usename);
        tvPassword.setText(password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                finish();
                break;
            case R.id.tv_url:
                String url = tvURL.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
    }
}
