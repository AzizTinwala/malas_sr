package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.R;

import java.util.ArrayList;

/**
 * Created by Admin on 11/27/2017.
 */

public class AverageBillValueAdapter extends BaseAdapter {
    Context mContext;
    private final ArrayList uniqueDateList;
    private final ArrayList countOutlet;
    private final ArrayList averageBillValue;
    private final ArrayList value;

    public AverageBillValueAdapter(Context mContext, ArrayList<String> uniqueDateList, ArrayList countOutlet, ArrayList value, ArrayList averageBillValue) {
        this.mContext = mContext;
        this.uniqueDateList = uniqueDateList;
        this.countOutlet = countOutlet;
        this.value = value;
        this.averageBillValue = averageBillValue;

    }


    @Override
    public int getCount() {
        return uniqueDateList.size();
    }

    @Override
    public Object getItem(int position) {
        return uniqueDateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.average_bill_value_list, null);
            holder.tvDate = convertView.findViewById(R.id.tvDate);
            holder.tvTotalValue = convertView.findViewById(R.id.tvTotalValue);
            holder.tvtotaloutletCount = convertView.findViewById(R.id.tvOultet);
            holder.tvBillValue = convertView.findViewById(R.id.tvBillvalue);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.tvDate.setText(uniqueDateList.get(position).toString());
        holder.tvTotalValue.setText(value.get(position).toString() + "");
        holder.tvtotaloutletCount.setText(countOutlet.get(position).toString() + "");
        holder.tvBillValue.setText(averageBillValue.get(position).toString() + "");

        return convertView;
    }

    private class ViewHolder {
        TextView tvDate;
        TextView tvTotalValue;
        TextView tvtotaloutletCount;
        TextView tvBillValue;

    }

}
