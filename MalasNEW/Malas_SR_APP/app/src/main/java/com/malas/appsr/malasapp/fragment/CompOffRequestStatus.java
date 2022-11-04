package com.malas.appsr.malasapp.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.malas.appsr.malasapp.BeanClasses.CompOffRequestStatusOject;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.CompOffRequestAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class CompOffRequestStatus extends Fragment implements View.OnClickListener {
    RecyclerView rv_leave_list;
    ImageView back_space;
    private UserLoginInfoBean mUserLoginInfoBean;
    private ArrayList<CompOffRequestStatusOject> compOffRequestStatusOjects;
    private TextView tv_empty;

    @Nullable
    @Override
    public View onCreateView(@NonNull
                                     LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.compoff_request_status, container,
                false);
        initView(view);
        clickListener();
        return view;

    }

    private void clickListener() {
        back_space.setOnClickListener(this);
    }

    private void initView(View view) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), Constant.UserRegInfoPref, MODE_PRIVATE);
        mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        tv_empty = view.findViewById(R.id.tv_empty);

        rv_leave_list = view.findViewById(R.id.rv_compoff_list);
        back_space = view.findViewById(R.id.back_space);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rv_leave_list.setLayoutManager(linearLayoutManager);
        if (Utils.isInternetConnected(getActivity())) {
            if (mUserLoginInfoBean != null) {
                new mGetCompoffList().execute();
            } else {
                rv_leave_list.setVisibility(View.GONE);
                tv_empty.setVisibility(View.VISIBLE);
                tv_empty.setText("Please Check Internet Connection");
            }
        }
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager1 = requireActivity().getSupportFragmentManager();
        fragmentManager1.popBackStack("status_compoff_request_list", FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    @SuppressLint("StaticFieldLeak")
    public class mGetCompoffList extends AsyncTask<String, Void, JSONObject> {
        Dialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(requireActivity());
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mDialog.setContentView(R.layout.progess_dialoge);
            mDialog.setTitle("please wait...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<>();
            nameValuePair.add(new BasicNameValuePair("method", "get_copoff_list"));
            nameValuePair.add(new BasicNameValuePair("user_id", mUserLoginInfoBean.getUserId()));
            nameValuePair.add(new BasicNameValuePair("desig_id", mUserLoginInfoBean.getDesignationId()));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        compOffRequestStatusOjects = new ArrayList<>();
                        JSONArray mJsonarry = jsonObject.getJSONArray("compoffRequestList");
                        for (int j = 0; j < mJsonarry.length(); j++) {
                            rv_leave_list.setVisibility(View.VISIBLE);
                            tv_empty.setVisibility(View.GONE);

                            JSONObject mJsonObjInfo = mJsonarry.getJSONObject(j);
                            String requestId = mJsonObjInfo.getString("request_id");
                            String fdate = mJsonObjInfo.getString("fdate");
                            String tdate = mJsonObjInfo.getString("tdate");
                            String leave_days = mJsonObjInfo.getString("leave_days");
                            String lreason = mJsonObjInfo.getString("reason");
                            int status = mJsonObjInfo.getInt("status");
                            String sendDate = mJsonObjInfo.getString("created_at");
                            String remaining_days = mJsonObjInfo.getString("remaining_days");

                            compOffRequestStatusOjects.add(new CompOffRequestStatusOject(requestId, fdate, tdate, sendDate, leave_days, status, lreason, remaining_days));
                        }
                        CompOffRequestAdapter compOffRequestAdapter = new CompOffRequestAdapter(getActivity(), compOffRequestStatusOjects);
                        rv_leave_list.setAdapter(compOffRequestAdapter);
                    } else {
                        rv_leave_list.setVisibility(View.GONE);
                        tv_empty.setVisibility(View.VISIBLE);
                        tv_empty.setText(jsonObject.getString("message"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    rv_leave_list.setVisibility(View.GONE);
                    tv_empty.setVisibility(View.VISIBLE);
                    tv_empty.setText("Something went wrong.");
                }
            } else {
                rv_leave_list.setVisibility(View.GONE);
                tv_empty.setVisibility(View.VISIBLE);
                tv_empty.setText("Improper response from server");
            }
            mDialog.dismiss();
        }
    }


}
