package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.PreviousDateAttendance;
import com.malas.appsr.malasapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Arwa on 29-Jan-19.
 */

public class PreviousAttendanceAdapter extends BaseAdapter {
    Context context;
    private final ArrayList<PreviousDateAttendance> attendanceBeanArrayList;
    public static ArrayList<PreviousDateAttendance> resultArrayshort=new ArrayList<>();
    public PreviousAttendanceAdapter(Context context, ArrayList<PreviousDateAttendance > attendanceBeanArrayList) {
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
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.lv_attendance_row, null);
            holder.tvAttendanceReason = convertView.findViewById(R.id.tv_attendance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (resultArrayshort.get(position).getDate().equalsIgnoreCase("TODAY")){
            holder.tvAttendanceReason.setText(resultArrayshort.get(position).getDate());

        }else
        holder.tvAttendanceReason.setText(convertDate(resultArrayshort.get(position).getDate()));
        return convertView;
    }
    private String convertDate(String inputDate){

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());

        Date date;
        try {
            date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return "";
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
                String fullname = attendanceBeanArrayList.get(i).getDate();

                // String firstname = fullname.substring(0, fullname.indexOf(' '));

                if (fullname.toLowerCase(Locale.getDefault()).contains(charText)) {
                    resultArrayshort.add(attendanceBeanArrayList.get(i));

                }
            }
        }
        notifyDataSetChanged();
    }
}
