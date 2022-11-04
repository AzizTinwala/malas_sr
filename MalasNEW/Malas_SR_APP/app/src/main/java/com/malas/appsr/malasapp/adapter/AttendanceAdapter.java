package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.AttendanceReasonBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Arwa on 05-Dec-18.
 */

public class AttendanceAdapter extends BaseAdapter {
    Context context;
    private final ArrayList<AttendanceReasonBean > attendanceBeanArrayList;
    public static ArrayList<AttendanceReasonBean> resultArrayshort=new ArrayList<>();
    public AttendanceAdapter(Context context, ArrayList<AttendanceReasonBean > attendanceBeanArrayList) {
        this.context=context;
        this.attendanceBeanArrayList=attendanceBeanArrayList;
        resultArrayshort.clear();
        resultArrayshort.addAll(attendanceBeanArrayList);
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

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder holder ;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.lv_attendance_row, null);
            holder.tvAttendanceReason = convertView.findViewById(R.id.tv_attendance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvAttendanceReason.setText(resultArrayshort.get(position).getAttendanceReason());
        return convertView;
    }

    private class ViewHolder {
        TextView tvAttendanceReason;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        resultArrayshort.clear();
        if (charText.length() == 0) {

            resultArrayshort.addAll(attendanceBeanArrayList);
        } else {
            for (int i = 0; i < attendanceBeanArrayList.size(); i++) {
                String fullname = attendanceBeanArrayList.get(i).getAttendanceReason();

                // String firstname = fullname.substring(0, fullname.indexOf(' '));

                if (fullname.toLowerCase(Locale.getDefault()).contains(charText)) {
                    resultArrayshort.add(attendanceBeanArrayList.get(i));

                }
            }
        }
        notifyDataSetChanged();
    }
}
