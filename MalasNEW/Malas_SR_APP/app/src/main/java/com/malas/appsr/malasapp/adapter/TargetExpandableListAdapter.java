package com.malas.appsr.malasapp.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.TargetBean;
import com.malas.appsr.malasapp.BeanClasses.TargetProductBean;
import com.malas.appsr.malasapp.R;

public class TargetExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context _context;
    private final List<TargetBean> _listDataHeader; // header titles

    public TargetExpandableListAdapter(Context context, List<TargetBean> listDataHeader) {
        this._context = context;
        this._listDataHeader = listDataHeader;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataHeader.get(groupPosition).getTargetProductList().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final TargetProductBean child = (TargetProductBean) getChild(groupPosition, childPosition);
        try {


            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.target_child, null);
            }

            TextView tvChildCategoryName = convertView.findViewById(R.id.tvChildCategoryName);
            TextView tvChildTarget = convertView.findViewById(R.id.tvChildTarget);
            TextView tvChildAchievement = convertView.findViewById(R.id.tvChildAchievement);
            TextView tvChildPercentage = convertView.findViewById(R.id.tvChildPercentage);

            tvChildCategoryName.setText(child.getProductname());
            tvChildTarget.setText(child.getProducttarget());
            tvChildAchievement.setText(child.getProductarchive());
            tvChildPercentage.setText(Math.round(Float.parseFloat(child.getProductpercentage())) + "%");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataHeader.get(groupPosition).getTargetProductList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        TargetBean header = (TargetBean) getGroup(groupPosition);
        try {


            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.target_group, null);
            }

            TextView tvGroupCategoryName = convertView.findViewById(R.id.tvGroupCategoryName);
            TextView tvGroupTarget = convertView.findViewById(R.id.tvGroupTarget);
            TextView tvGroupAchievement = convertView.findViewById(R.id.tvGroupAchievement);
            TextView tvGroupPercentage = convertView.findViewById(R.id.tvGroupPercentage);

            tvGroupCategoryName.setText(header.getCat_name());
            tvGroupTarget.setText(header.getTarget());
            tvGroupAchievement.setText(header.getCat_achived());
            tvGroupPercentage.setText(Math.round(Float.parseFloat(header.getCat_per())) + "%");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}