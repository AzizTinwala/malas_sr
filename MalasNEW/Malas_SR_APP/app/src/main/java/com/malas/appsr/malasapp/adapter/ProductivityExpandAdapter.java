package com.malas.appsr.malasapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.ProductivityBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 12/13/2017.
 */

public class ProductivityExpandAdapter extends BaseExpandableListAdapter {

    private final Context mContext;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
  private final HashMap<String, ArrayList<ProductivityBean>> _listDataChild;
    private final ArrayList<ProductivityBean> produNonProductiveList;
    private final ArrayList<ProductivityBean> productivelist;
    private final ArrayList productivityList;
    ArrayList<ProductivityBean> childList;


    public ProductivityExpandAdapter(Context mContext, ArrayList<ProductivityBean> productiveNonProductive, ArrayList<ProductivityBean> productivelist, ArrayList productivityList,HashMap<String, ArrayList<ProductivityBean>>childList) {
        this.mContext = mContext;
        this.produNonProductiveList = productiveNonProductive;
        this.productivelist = productivelist;
        this.productivityList = productivityList;
        this._listDataChild=childList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this.produNonProductiveList.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.productivity_list, null);
        }


        //txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
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
        String headerTitle = (String) getGroup(groupPosition);
        ProductivityBean productNonProductive=(ProductivityBean) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          //convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView tvDate = convertView.findViewById(R.id.tvDatelist);
        TextView tvProductiveNonProductiveCount = convertView.findViewById(R.id.tvTotalOutletVisitlist);
        TextView tvProductiveOutletCount = convertView.findViewById(R.id.tvTotalOuletProductivelist);
        TextView tvProductivity = convertView.findViewById(R.id.tvProductivitylist);

        tvDate.setText(productNonProductive.getDate());
        tvProductiveNonProductiveCount.setText(productNonProductive.getOutletCount() + "");
       /* tvProductiveOutletCount.setText(productivelist.get(position).getOutletCount() + "");
        tvProductivity.setText( productivityList.get(position)+" %");*/

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
