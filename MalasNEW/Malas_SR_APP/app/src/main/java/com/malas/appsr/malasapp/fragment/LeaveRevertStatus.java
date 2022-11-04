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
import com.malas.appsr.malasapp.BeanClasses.LeaveRequestStatusOject;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.adapter.LeaveRevertRequestAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class LeaveRevertStatus extends Fragment implements View.OnClickListener {
    private RecyclerView rv_leave_list;
   private ImageView back_space;
   private TextView tv_empty;
    private UserLoginInfoBean mUserLoginInfoBean;
    private ArrayList<LeaveRequestStatusOject> leaveRequestRevertStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.leave_revert_request_status, container,
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

        rv_leave_list = view.findViewById(R.id.rv_leave_revert_list);
        back_space = view.findViewById(R.id.back_space);
        tv_empty = view.findViewById(R.id.tv_empty);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rv_leave_list.setLayoutManager(linearLayoutManager);
        if (Utils.isInternetConnected(getActivity())) {
            if (mUserLoginInfoBean != null) {
                new mGetLeaveRevertStatus().execute();

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
        fragmentManager1.popBackStack("status_request_list", FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    @SuppressLint("StaticFieldLeak")
    public class mGetLeaveRevertStatus extends AsyncTask<String, Void, JSONObject> {
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
            nameValuePair.add(new BasicNameValuePair("method", "get_revert_leave_status"));
            nameValuePair.add(new BasicNameValuePair("user_id", mUserLoginInfoBean.getUserId()));
            nameValuePair.add(new BasicNameValuePair("desig_id", mUserLoginInfoBean.getDesignationId()));
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        leaveRequestRevertStatus = new ArrayList<>();
                        JSONArray mJsonarry = jsonObject.getJSONArray("getLeaveRevertRequestList");
                        for (int j = 0; j < mJsonarry.length(); j++) {
                            rv_leave_list.setVisibility(View.VISIBLE);
                            tv_empty.setVisibility(View.GONE);
                            JSONObject mJsonObjInfo = mJsonarry.getJSONObject(j);
                            String fdate = mJsonObjInfo.getString("fdate");
                            String tdate = mJsonObjInfo.getString("tdate");
                            String leave_days = mJsonObjInfo.getString("leave_days");
                            int status = mJsonObjInfo.getInt("status");
                            String type1 = mJsonObjInfo.getString("deduct_type");
                            String type = "";
                            if (type1.equalsIgnoreCase("COMPOFF")) {
                                type = Utils.insertString("COMPOFF",
                                        "-",
                                        3);
                            } else {
                                type = type1;
                            }
                            String sendDate = mJsonObjInfo.getString("created_at");
                            leaveRequestRevertStatus.add(new LeaveRequestStatusOject(fdate, sendDate, leave_days, status, type, tdate));
                        }
                        LeaveRevertRequestAdapter leaveRequestAdapter = new LeaveRevertRequestAdapter(getActivity(), leaveRequestRevertStatus);
                        rv_leave_list.setAdapter(leaveRequestAdapter);
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
