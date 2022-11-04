package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderItemBean;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderListBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class JamOutletOrderExpandListAdapter extends BaseExpandableListAdapter {

    private final Context _context;
    private final ArrayList<TakeOutletOrderListBean> _listDataHeader; // header titles
    // child data in format of header title, child title
    private final HashMap<TakeOutletOrderListBean, ArrayList<TakeOutletOrderItemBean>> _listDataChild;

    public JamOutletOrderExpandListAdapter(Context context, ArrayList<TakeOutletOrderListBean> listDataHeader,
                                           HashMap<TakeOutletOrderListBean, ArrayList<TakeOutletOrderItemBean>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return Objects.requireNonNull(this._listDataChild.get(this._listDataHeader.get(groupPosition)))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final TakeOutletOrderItemBean childObject = (TakeOutletOrderItemBean) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.jam_outlet_order_list_item, null);
        }

        TextView lbproduct = convertView.findViewById(R.id.lbproduct);
        TextView lblqty = convertView.findViewById(R.id.lblqty);

        lbproduct.setText(childObject.getProduct_name());
        lblqty.setText(childObject.getProduct_qty());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(this._listDataChild.get(this._listDataHeader.get(groupPosition)))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition).getItem();
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.jam_list_group_for_outlet_order, null);
        }

        LinearLayout llHeader = convertView.findViewById(R.id.llHeader);

        if (isExpanded) {
            llHeader.setVisibility(View.VISIBLE);
        } else {
            llHeader.setVisibility(View.GONE);
        }


        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

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