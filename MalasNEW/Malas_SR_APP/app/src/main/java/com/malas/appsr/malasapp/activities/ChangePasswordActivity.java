package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;

import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText edt_old_password, edt_new_password, edt_conform_new_password;
    Button btn_submit;
    AsyncTask<String, Void, JSONObject> changePasswordAsyn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Change Password");
        edt_old_password = findViewById(R.id.edt_old_password);
        edt_new_password = findViewById(R.id.edt_new_password);
        edt_conform_new_password = findViewById(R.id.edt_conform_new_password);
        btn_submit = findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(v -> {
            if (edt_old_password.getText().toString().isEmpty()) {
                edt_old_password.setError("Enter old password");
            } else if (edt_new_password.getText().toString().isEmpty()) {
                edt_new_password.setError("Enter new password");
            } else if (edt_conform_new_password.getText().toString().isEmpty()) {
                edt_conform_new_password.setError("Conform new password");
            } else if (!edt_new_password.getText().toString().trim().equals(edt_conform_new_password.getText().toString().trim())) {
                edt_conform_new_password.setError("New password and confirm password should be same");
            } else {
                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ChangePasswordActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
                UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);

                changePasswordAsyn = new mChangePassword().execute(mUserLoginInfoBean.getUserId(), edt_old_password.getText().toString().trim(), edt_new_password.getText().toString().trim());
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    public class mChangePassword extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(ChangePasswordActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Changing Password...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "changepassword"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            nameValuePair.add(new BasicNameValuePair("oldpassword", params[1]));
            nameValuePair.add(new BasicNameValuePair("newpassword", params[2]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                if (mDialog.isShowing()) mDialog.dismiss();


                try {
                    Utils.showAlertDialog(ChangePasswordActivity.this, "Change Password", jsonObject.getString("message"), true);
//                    Toast.makeText(ChangePasswordActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    if (jsonObject.getString("success").equals("true"))
                        ChangePasswordActivity.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                if (mDialog.isShowing()) mDialog.dismiss();
                Utils.showAlertDialog(ChangePasswordActivity.this, "Target", "Improper response from server", true);
//                Toast.makeText(ChangePasswordActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }

        }
    }

}