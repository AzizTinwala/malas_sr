package com.malas.appsr.malasapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.AttendanceBean;
import com.malas.appsr.malasapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Arwa on 03-Apr-19.
 */

public class SyncAttendanceAdapter extends BaseAdapter {
    Context context;
    ArrayList<AttendanceBean> attendanceBeanArrayList;
    public SyncAttendanceAdapter(Context context, ArrayList<AttendanceBean> attendanceBeanArrayList) {
        this.context=context;
        this.attendanceBeanArrayList=attendanceBeanArrayList;

    }

    @Override
    public int getCount() {
        return attendanceBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return attendanceBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.lv_attendance_sync_row, null);
            holder.tvAttendanceType = convertView.findViewById(R.id.tv_attendance_type);
            holder.tvAttendanceDate = convertView.findViewById(R.id.tv_attendance_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (attendanceBeanArrayList.get(position).getAttendanceType().equalsIgnoreCase("F")){
            holder.tvAttendanceType.setText("Field Work");

        }else if (attendanceBeanArrayList.get(position).getAttendanceType().equalsIgnoreCase("O")){
            holder.tvAttendanceType.setText("Other work");

        }else if (attendanceBeanArrayList.get(position).getAttendanceType().equalsIgnoreCase("L")){
            holder.tvAttendanceType.setText("Leave");

        }else if (attendanceBeanArrayList.get(position).getAttendanceType().equalsIgnoreCase("off")){
            holder.tvAttendanceType.setText("DAY OFF");

        }

        holder.tvAttendanceDate.setText(convertDate(attendanceBeanArrayList.get(position).getAttendanceDate()));
        return convertView;
    }

    private class ViewHolder {
        TextView tvAttendanceType;
        TextView tvAttendanceDate;
    }

    public String convertDate(String inputDate) {

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        Date date;
        try {
            date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
