package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.MonthBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by xyz on 9/14/2016.
 */

public class MonthAdapter extends BaseAdapter {
    Context mContext;
    private final ArrayList<MonthBean> monthList;
    public static ArrayList<MonthBean> resultArrayshort=new ArrayList<>();

    public MonthAdapter(Context mContext, ArrayList<MonthBean> monthList) {
        this.mContext = mContext;
        this.monthList = monthList;
        resultArrayshort.clear();
        resultArrayshort.addAll(monthList);
    }

    @Override
    public int getCount() {
        return resultArrayshort.size();
    }

    @Override
    public Object getItem(int position) {
        return resultArrayshort.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.city_list_row, null);
            holder.tvName = convertView.findViewById(R.id.tv_city);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(resultArrayshort.get(position).getName());
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        resultArrayshort.clear();
        if (charText.length() == 0) {

            resultArrayshort.addAll(monthList);
        } else {
            for (int i = 0; i < monthList.size(); i++) {
                String fullname = monthList.get(i).getName();
                if (fullname.toLowerCase(Locale.getDefault()).contains(charText)) {
                    resultArrayshort.add(monthList.get(i));

                }
            }
        }
        notifyDataSetChanged();
    }
}
