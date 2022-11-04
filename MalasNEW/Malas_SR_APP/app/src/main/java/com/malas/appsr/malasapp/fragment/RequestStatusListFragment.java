package com.malas.appsr.malasapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.malas.appsr.malasapp.R;

import kotlin.jvm.internal.Ref;

public class RequestStatusListFragment extends androidx.fragment.app.Fragment implements View.OnClickListener {
    private LinearLayout ll_leave_request;
    private LinearLayout ll_revert_request;
    private LinearLayout ll_comp_off_request;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.list_frag, container,
                false);
        initView(view);
        clickListener();
        return view;
    }

    private void clickListener() {
        ll_leave_request.setOnClickListener(this);
        ll_comp_off_request.setOnClickListener(this);
        ll_revert_request.setOnClickListener(this);
    }

    private void initView(View view) {
        ll_leave_request = view.findViewById(R.id.ll_leave_requ);
        ll_comp_off_request = view.findViewById(R.id.ll_comp_req);
        ll_revert_request = view.findViewById(R.id.ll_revert_req);
    }

    @Override
    public void onClick(View v) {
        final Ref.BooleanRef flag = new Ref.BooleanRef();
        flag.element = false;
        switch ((v.getId())) {
            case R.id.ll_leave_requ:
                FragmentManager fragmentManager2 = requireActivity().getSupportFragmentManager();
                Fragment argumentFragment = new LeaveRequestStatus();

                fragmentManager2.beginTransaction()
                        .add(R.id.fl_status, argumentFragment).addToBackStack("status_leave_request_list").commit();//now replace the argument fragment
                fragmentManager2.addOnBackStackChangedListener(() -> {
                    if (flag.element){
                        showView();
                        flag.element=false;
                    }else{
                        hideView();
                        flag.element=true;
                    }
                });
                break;
            case R.id.ll_comp_req:
                FragmentManager fragmentManager3 = requireActivity().getSupportFragmentManager();
                Fragment argumentFragment2 = new CompOffRequestStatus();

                fragmentManager3.beginTransaction()
                        .add(R.id.fl_status, argumentFragment2).addToBackStack("status_compoff_request_list").commit();//now replace the argument fragment
                fragmentManager3.addOnBackStackChangedListener(() -> {
                    if (flag.element){
                        showView();
                        flag.element=false;
                    }else{
                        hideView();
                        flag.element=true;
                    }
                });
                break;
            case R.id.ll_revert_req:
                FragmentManager fragmentManager4 = requireActivity().getSupportFragmentManager();
                Fragment argumentFragment3 = new LeaveRevertStatus();

                fragmentManager4.beginTransaction()
                        .add(R.id.fl_status, argumentFragment3).addToBackStack("status_request_list").commit();//now replace the argument fragment
                fragmentManager4.addOnBackStackChangedListener(() -> {
                    if (flag.element){
                        showView();
                        flag.element=false;
                    }else{
                        hideView();
                        flag.element=true;
                    }
                });
                break;
        }
    }

    private void hideView() {
        ll_leave_request.setVisibility(View.GONE);
        ll_comp_off_request.setVisibility(View.GONE);
        ll_revert_request.setVisibility(View.GONE);
    }

    private void showView() {
        ll_leave_request.setVisibility(View.VISIBLE);
        ll_comp_off_request.setVisibility(View.VISIBLE);
        ll_revert_request.setVisibility(View.VISIBLE);
    }
}
