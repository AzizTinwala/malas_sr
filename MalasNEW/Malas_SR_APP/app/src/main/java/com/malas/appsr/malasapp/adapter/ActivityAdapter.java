package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.ActivitySRBean;
import com.malas.appsr.malasapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Arwa on 18-Sep-18.
 */

public class ActivityAdapter extends BaseAdapter {
    Context mContext;

    private final ArrayList<ActivitySRBean> activitySRBeanArrayList;


    public ActivityAdapter(Context mContext, ArrayList<ActivitySRBean> activitySRBeanArrayList) {
        this.mContext = mContext;

        this.activitySRBeanArrayList = activitySRBeanArrayList;

    }


    @Override
    public int getCount() {
        return activitySRBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return activitySRBeanArrayList.get(position);
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
            convertView = mInflater.inflate(R.layout.activity_gridview_list, null);
            holder.tvactivityName = convertView.findViewById(R.id.tv_grid);
            holder.tvStartDate = convertView.findViewById(R.id.tv_start_date);
            holder.tvEndDate = convertView.findViewById(R.id.tv_end_date);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.tvactivityName.setText(activitySRBeanArrayList.get(position).getAct_name());
        holder.tvStartDate.setText(convertDateFormat(activitySRBeanArrayList.get(position).getAct_start_date()));
        holder.tvEndDate.setText(convertDateFormat(activitySRBeanArrayList.get(position).getAct_end_date()));


        return convertView;
    }

    private String convertDateFormat(String strInputdate) {

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        Date date;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(strInputdate);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateStr;
    }

    private class ViewHolder {

        TextView tvactivityName;
        TextView tvStartDate;
        TextView tvEndDate;


    }

}
