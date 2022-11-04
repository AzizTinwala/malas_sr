package com.malas.appsr.malasapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.Amitlibs.utils.TextUtils;

import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.BeanClasses.UserTerritoryBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.HomeActivity;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.UserTerritoryAdapter;
import com.malas.appsr.malasapp.session.SessionManagement;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText edtUserName, edtPassword;
    ImageView btnRegistration;
    ImageView btnLogin;
    TextView tvForgtPassword;
    AsyncTask<String, Void, JSONObject> mGetloginDetails, mForgetpassword;
    String email, password;
    ArrayList<UserLoginInfoBean> userLoginList;
    String forgetEmail;
    EditText edtEmailDialog;
    Dialog dialogForgetPassword;
    ArrayList<UserTerritoryBean> territoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        //  Objects.requireNonNull(getSupportActionBar()).setTitle("Login Screen");
        edtUserName = findViewById(R.id.edt_user_name);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegistration = findViewById(R.id.btn_register);
        tvForgtPassword = findViewById(R.id.tv_forget_password);


        //  tvForgtPassword.setPaintFlags(tvForgtPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnLogin.setOnClickListener(v -> {
            if (edtUserName.getText().toString().isEmpty()) {
                edtUserName.setError("Enter Email");
            } else if (edtPassword.getText().toString().isEmpty()) {
                edtPassword.setError("Enter password");
            } else if (!TextUtils.isValidEmail(edtUserName.getText().toString())) {
                edtUserName.setError("Enter Valid email");
            } else {

                mGetloginDetails = new mGetloginDetails().execute();
            }
        });
        btnRegistration.setOnClickListener(v -> {
            Intent in = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(in);

        });

        tvForgtPassword.setOnClickListener(v -> {

            dialogForgetPassword = new Dialog(LoginActivity.this);
            dialogForgetPassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogForgetPassword.setContentView(R.layout.forget_dialog);
            dialogForgetPassword.show();
            edtEmailDialog = dialogForgetPassword.findViewById(R.id.edt_forgot_password);
            Button btnsubmt = dialogForgetPassword.findViewById(R.id.btn_submit_dialog);


            btnsubmt.setOnClickListener(v1 -> {

                forgetEmail = edtEmailDialog.getText().toString();

                if (TextUtils.isValidEmail(forgetEmail)) {

                    mForgetpassword = new mForgetpassword().execute();

                } else {

                    edtEmailDialog.setError("Enter Valid Email");
                }
            });

        });
    }


    @Override
    protected void onPause() {
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

        super.onPause();
        if (mGetloginDetails != null && !mGetloginDetails.isCancelled())
            mGetloginDetails.cancel(true);

        if (mForgetpassword != null && !mForgetpassword.isCancelled())
            mForgetpassword.cancel(true);
        //   mForgetpassword
    }


    @SuppressLint("StaticFieldLeak")
    public class mGetloginDetails extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            email = edtUserName.getText().toString();
            password = edtPassword.getText().toString();
            mDialog = new Dialog(LoginActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Logging in please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {


            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "sr_login"));
            nameValuePair.add(new BasicNameValuePair("divicetype", "0"));
            nameValuePair.add(new BasicNameValuePair("email", email));
            nameValuePair.add(new BasicNameValuePair("password", password));
            nameValuePair.add(new BasicNameValuePair("gcmid_uid", "asasasdsadas"));

            //JSONObject jsonObjectFromUrl = new JSONParser().getJsonObjectFromHttp(Constant.BASE_URL, nameValuePair, JSONParser.Http.POST);
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        userLoginList = new ArrayList<>();
                        System.out.print(jsonObject.getString("message"));
                        JSONObject mJsonobj = jsonObject.getJSONObject("user");
                        String dis_name = mJsonobj.getString("dis_name");
                        String territory_name = mJsonobj.getString("territory_name");
                        String device_type = mJsonobj.getString("device_type");
                        String designation_id = mJsonobj.getString("designation_id");
                        String asm_id = mJsonobj.getString("asm_id");
                        String user_address = mJsonobj.getString("user_address");
                        String sso_id = mJsonobj.getString("sso_id");
                        String user_dob = mJsonobj.getString("user_dob");
                        String user_email = mJsonobj.getString("user_email");
                        String user_district_id = mJsonobj.getString("user_district_id");
                        String country_name = mJsonobj.getString("country_name");
                        String user_name = mJsonobj.getString("user_name");
                        String user_teratorry_id = mJsonobj.getString("user_teratorry_id");
                        String user_country_id = mJsonobj.getString("user_country_id");
                        String user_activation_status = mJsonobj.getString("user_activation_status");
                        String user_mobile = mJsonobj.getString("user_mobile");
                        String user_city_id = mJsonobj.getString("user_city_id");
                        String user_id = mJsonobj.getString("user_id");
                        String cname = mJsonobj.getString("cname");
                        String asm_name = mJsonobj.getString("asm_name");
                        String sso_name = mJsonobj.getString("sso_name");
                        String dateof_joining = mJsonobj.getString("dateof_joining");
                        String sales_type;
                        if (mJsonobj.getInt("sales_type") > 0) {
                            sales_type = "MT";
                        } else {
                            sales_type = "GT";
                        }
                        String empcode = mJsonobj.getString("empcode");
                        String weekoff = mJsonobj.getString("week_off");
                        String department = mJsonobj.getString("department");
                        String designation = mJsonobj.getString("designation");
                        String pincode = mJsonobj.getString("pincode");
                        String hr_city_name = mJsonobj.getString("hr_city_name");
                        String hr_state_name = mJsonobj.getString("hr_state_name");
                        //call to service which brings territory Id
                        UserLoginInfoBean mUserLoginInfoBean = new UserLoginInfoBean(
                                dis_name, territory_name, device_type, designation_id, asm_id,
                                user_address, sso_id, user_dob, user_email, user_district_id,
                                country_name, user_name, user_teratorry_id, user_country_id,
                                user_activation_status, user_mobile, user_city_id, user_id, cname,
                                asm_name, sso_name, dateof_joining, sales_type, empcode, weekoff,
                                department, designation, pincode, hr_city_name, hr_state_name);
                        userLoginList.add(mUserLoginInfoBean);
                        if (!mUserLoginInfoBean.getUserTerritoryId().equalsIgnoreCase("0")) {
                            new getUserTerritory().execute(user_id);

                        } else {
                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
                            complexPreferences.putObject(Constant.UserRegInfoObj, mUserLoginInfoBean);
                            complexPreferences.commit();
                            Toast.makeText(LoginActivity.this, "Welcome to Malas D2 Tool", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            i.putExtra("isTerMultiple", false);
                            startActivity(i);
                            finish();
                        }


                    } else {
                        Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(LoginActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }
    }


    @SuppressLint("StaticFieldLeak")
    public class getUserTerritory extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(LoginActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("Logging in please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "get_user_territory"));
            nameValuePair.add(new BasicNameValuePair("user_id", params[0]));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        territoryList = new ArrayList<>();
                        JSONArray mJsonArray = jsonObject.getJSONArray("territorylist");
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            String territoryId = mJsonArray.getJSONObject(i).getString("territory_id");
                            String territoryname = mJsonArray.getJSONObject(i).getString("territory_name");
                            String territorystate = mJsonArray.getJSONObject(i).getString("state_name");
                            UserTerritoryBean userTerritoryBean = new UserTerritoryBean(territoryId, territoryname, territorystate);
                            territoryList.add(userTerritoryBean);
                        }
                        if (territoryList.size() > 1) {
                            territoryDialog();
                        } else {
                            UserLoginInfoBean mUserLoginInfoBean = new UserLoginInfoBean(
                                    userLoginList.get(0).getDisName(),
                                    territoryList.get(0).getTerritoryName(),
                                    userLoginList.get(0).getDeviceType(),
                                    userLoginList.get(0).getDesignationId(),
                                    userLoginList.get(0).getAsmId(),
                                    userLoginList.get(0).getUserAddress(),
                                    userLoginList.get(0).getSsoId(),
                                    userLoginList.get(0).getUserDob(),
                                    userLoginList.get(0).getUserEmail(),
                                    userLoginList.get(0).getUserDistrictId(),
                                    userLoginList.get(0).getCountryName(),
                                    userLoginList.get(0).getUserName(),
                                    territoryList.get(0).getTerritoryId(),
                                    userLoginList.get(0).getUserCountryId(),
                                    userLoginList.get(0).getUserActivationStatus(),
                                    userLoginList.get(0).getUserMobile(),
                                    userLoginList.get(0).getUserCityId(),
                                    userLoginList.get(0).getUserId(),
                                    userLoginList.get(0).getCname(),
                                    userLoginList.get(0).getAsmName(),
                                    userLoginList.get(0).getSsoName(),
                                    userLoginList.get(0).getDateOfJoining(),
                                    userLoginList.get(0).getSalesType(),
                                    userLoginList.get(0).getEmpCode(),
                                    userLoginList.get(0).getWeekOff(),
                                    userLoginList.get(0).getUserDepartment(),
                                    userLoginList.get(0).getUserDesignation(),
                                    userLoginList.get(0).getUserPinCode(),
                                    userLoginList.get(0).getUserCity(),
                                    userLoginList.get(0).getUserState()
                            );
                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
                            complexPreferences.putObject(Constant.UserRegInfoObj, mUserLoginInfoBean);
                            complexPreferences.commit();
                            Toast.makeText(LoginActivity.this, "Welcome to Malas D2 Tool", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            i.putExtra("isTerMultiple", false);
                            startActivity(i);
                            finish();
                        }
                        SessionManagement session = new SessionManagement(getBaseContext());
                        session.initBirthday();

                    } else {
                        Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(LoginActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class mForgetpassword extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {

            mDialog = new Dialog(LoginActivity.this);
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle(" Please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "forgetpassword"));
            nameValuePair.add(new BasicNameValuePair("email_id", forgetEmail));
            //JSONObject jsonObjectFromUrl = new JSONParser().getJsonObjectFromHttp(Constant.BASE_URL, nameValuePair, JSONParser.Http.POST);
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            mDialog.dismiss();
            dialogForgetPassword.dismiss();

            if (jsonObject != null) {

                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        System.out.print(jsonObject.getString("message"));

                    }
                    Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(LoginActivity.this, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void territoryDialog() {

        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.user_territory);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCanceledOnTouchOutside(false);
        // dialog.setCancelable(false);

        final ListView lvAttendance = dialog.findViewById(R.id.lv_user_territory);
        dialog.show();
        final UserTerritoryAdapter userTerritoryAdapter = new UserTerritoryAdapter(LoginActivity.this, territoryList);

        lvAttendance.setAdapter(userTerritoryAdapter);
        lvAttendance.setOnItemClickListener((parent, view, position, id) -> {

            UserLoginInfoBean mUserLoginInfoBean = new UserLoginInfoBean(
                    userLoginList.get(0).getDisName(), territoryList.get(position).getTerritoryName(),
                    userLoginList.get(0).getDeviceType(), userLoginList.get(0).getDesignationId(),
                    userLoginList.get(0).getAsmId(), userLoginList.get(0).getUserAddress(),
                    userLoginList.get(0).getSsoId(), userLoginList.get(0).getUserDob(),
                    userLoginList.get(0).getUserEmail(), userLoginList.get(0).getUserDistrictId(),
                    userLoginList.get(0).getCountryName(), userLoginList.get(0).getUserName(),
                    territoryList.get(position).getTerritoryId(),
                    userLoginList.get(0).getUserCountryId(),
                    userLoginList.get(0).getUserActivationStatus(),
                    userLoginList.get(0).getUserMobile(), userLoginList.get(0).getUserCityId(),
                    userLoginList.get(0).getUserId(), userLoginList.get(0).getCname(),
                    userLoginList.get(0).getAsmName(), userLoginList.get(0).getSsoName(),
                    userLoginList.get(0).getDateOfJoining(), userLoginList.get(0).getSalesType(),
                    userLoginList.get(0).getEmpCode(), userLoginList.get(0).getWeekOff(),
                    userLoginList.get(0).getUserDepartment(),
                    userLoginList.get(0).getUserDesignation(),
                    userLoginList.get(0).getUserPinCode(),
                    userLoginList.get(0).getUserCity(),
                    userLoginList.get(0).getUserState()
            );
            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
            complexPreferences.putObject(Constant.UserRegInfoObj, mUserLoginInfoBean);
            complexPreferences.commit();
            Toast.makeText(LoginActivity.this, "Welcome to Malas D2 Tool", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            i.putExtra("isTerMultiple", true);
            startActivity(i);
            finish();
            dialog.dismiss();
        });
    }
}