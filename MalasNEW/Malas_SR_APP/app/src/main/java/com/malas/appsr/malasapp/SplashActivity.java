package com.malas.appsr.malasapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.Amitlibs.utils.ComplexPreferences;
import com.google.firebase.messaging.FirebaseMessaging;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.activities.LoginActivity;
import com.malas.appsr.malasapp.session.SessionManagement;

public class SplashActivity extends AppCompatActivity {

    /*AsyncTask<String, Void, JSONObject> mGetrequestListAsynOb;
    public ArrayList<CityListBean> citylist;
    public ArrayList<DistrictListBean> districtList;
    public ArrayList<CountryListBean> countrylist;*/

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        if (!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            runHandler();
        }

        //Subscribe topic for FCM.
        //https://www.malasportal.in/mportal/Api/firebase/index.php to send notification from here
        FirebaseMessaging.getInstance().subscribeToTopic("malasSR")
                .addOnCompleteListener(task -> {
                    String msg = "Successfull";
                    if (!task.isSuccessful()) {
                        msg = "Failed";
                    }
                });
    }


    public void runHandler() {
        new Handler().postDelayed(() -> {

            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(SplashActivity.this, Constant.UserRegInfoPref, MODE_PRIVATE);
            UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
            Intent i;
            if (mUserLoginInfoBean != null) {
                SessionManagement session = new SessionManagement(this);
                session.isBirthday();
                i = new Intent(SplashActivity.this, HomeActivity.class);
            } else {
                //mGetrequestListAsynOb = new GetRequestList().execute();
                i = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(i);
            finish();
        }, 3000);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {


            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {

                    if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "storage granted");

                        }
                    }

                    if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "location granted");
                        }

                    }
                    if (permissions[i].equals(Manifest.permission.CAMERA)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "Camera granted");
                        }
                        runHandler();
                    }

                }
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    /*
    public class GetRequestList extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("method", "getcdclist"));
            // JSONObject jsonObjectFromUrl = new JSONParser().getJsonObjectFromHttp(Constant.BASE_URL, nameValuePair, JSONParser.Http.POST);
            JSONObject jsonObjectFromUrl = new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);

            if (jsonObjectFromUrl != null) {
                try {
                    if (jsonObjectFromUrl.getString("success").equalsIgnoreCase("true")) {
                        System.out.print(jsonObjectFromUrl.getString("message").toString());
                        JSONArray mJsonArray = jsonObjectFromUrl.getJSONArray("list");
                        //  scheduledFragmentDataArray.clear();
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            JSONObject mJsonObjInfo = mJsonArray.getJSONObject(i);
                            JSONArray districtArray = mJsonObjInfo.getJSONArray("district");
                            JSONArray cityArray = mJsonObjInfo.getJSONArray("city");
                            JSONArray countryArray = mJsonObjInfo.getJSONArray("country");
                            districtList = new ArrayList<>();
                            for (int j = 0; j < districtArray.length(); j++) {
                                JSONObject mJsonObjInfoDistrict = districtArray.getJSONObject(j);
                                String Disid = mJsonObjInfoDistrict.getString("id");
                                String country_id = mJsonObjInfoDistrict.getString("country_id");
                                String dis_name = mJsonObjInfoDistrict.getString("dis_name");
                                districtList.add(new DistrictListBean(Disid, dis_name, country_id));
                            }
                            citylist = new ArrayList<>();
                            for (int k = 0; k < cityArray.length(); k++) {
                                JSONObject mJsonObjInfocity = cityArray.getJSONObject(k);
                                String district_id = mJsonObjInfocity.getString("district_id");
                                String cid = mJsonObjInfocity.getString("cid");
                                String cname = mJsonObjInfocity.getString("cname");
                                citylist.add(new CityListBean(district_id, cid, cname));
                            }
                            countrylist = new ArrayList<>();
                            for (int l = 0; l < countryArray.length(); l++) {
                                JSONObject mJsonObjInfoCountryt = countryArray.getJSONObject(l);
                                String id = mJsonObjInfoCountryt.getString("id");
                                String country_name = mJsonObjInfoCountryt.getString("country_name");
                                countrylist.add(new CountryListBean(id, country_name));
                            }
                            ComplexPreferences mCityDistrictCountryComplexPreferences = ComplexPreferences.getComplexPreferences(SplashActivity.this, Constant.CITY_DISTIC_COUNTRY_PREF, MODE_PRIVATE);
                            mCityDistrictCountryComplexPreferences.putObject(Constant.DISTIC_OBJ, districtList);
                            mCityDistrictCountryComplexPreferences.putObject(Constant.CITY_OBJ, citylist);
                            mCityDistrictCountryComplexPreferences.putObject(Constant.COUNTRY_OBJ, countrylist);
                            mCityDistrictCountryComplexPreferences.commit();
                        }
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }
            return jsonObjectFromUrl;

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

        }
    }*/


}
