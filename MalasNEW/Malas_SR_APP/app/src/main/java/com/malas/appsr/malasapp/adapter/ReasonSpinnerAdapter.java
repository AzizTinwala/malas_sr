package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.ReasonBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;


public class ReasonSpinnerAdapter extends BaseAdapter {
    Context mContext;
    private final ArrayList<ReasonBean> reasonList ;


    @Override
    public int getCount() {
        return reasonList.size();
    }

    public ReasonSpinnerAdapter(Context mContext, ArrayList<ReasonBean> reasonList) {
        this.mContext = mContext;
        this.reasonList = reasonList;

    }

    @Override
    public Object getItem(int position) {
        return reasonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.reason_row, null);

            holder.tvName = convertView.findViewById(R.id.tv_reason);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(reasonList.get(position).getReason());
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
    }


}