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

import com.malas.appsr.malasapp.BeanClasses.PreviousPlaceorderArraylistBean;
import com.malas.appsr.malasapp.BeanClasses.TakePreviousPlaceOrderItemList;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Admin on 04-Feb-18.
 */

public class ExpandPreviousPlaceOrderAdapter extends BaseExpandableListAdapter {

    private final Context _context;
    private final ArrayList<PreviousPlaceorderArraylistBean> _listDataHeader; // header titles
    // child data in format of header title, child title
    private final HashMap<PreviousPlaceorderArraylistBean, ArrayList<TakePreviousPlaceOrderItemList>> _listDataChild;

    public ExpandPreviousPlaceOrderAdapter(Context context, ArrayList<PreviousPlaceorderArraylistBean> listDataHeader,
                                           HashMap<PreviousPlaceorderArraylistBean, ArrayList<TakePreviousPlaceOrderItemList>> listChildData) {
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

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final TakePreviousPlaceOrderItemList childObject = (TakePreviousPlaceOrderItemList) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.previous_place_order_list, null);
        }

        TextView lbproduct = convertView.findViewById(R.id.lbproduct);
        TextView lbmrp = convertView.findViewById(R.id.lbmrp);
        TextView lblqty = convertView.findViewById(R.id.lblqty);
        TextView soq = convertView.findViewById(R.id.soq);

        lbproduct.setText(childObject.getProduct_name());
        lbmrp.setText(childObject.getProduct_mrp());
        lblqty.setText("" + Math.abs(Integer.parseInt(childObject.getDifference())));
        soq.setText(childObject.getPacketSize());
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
            convertView = infalInflater.inflate(R.layout.previous_place_order_list_group, null);
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
