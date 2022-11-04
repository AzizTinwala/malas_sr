package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.JointWorkBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;
import java.util.Locale;

public class JointWorkAdapter extends BaseAdapter {
    Context context;
    private final ArrayList<JointWorkBean> jointWorkBeanArrayList;
    public static ArrayList<JointWorkBean> resultArrayshort=new ArrayList<>();
     public JointWorkAdapter(Context context, ArrayList<JointWorkBean> jointWorkBeanArrayList) {
        this.context=context;
        this.jointWorkBeanArrayList=jointWorkBeanArrayList;
        resultArrayshort.clear();
        resultArrayshort.addAll(jointWorkBeanArrayList);
    }

    @Override
    public int getCount() {
        return jointWorkBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return jointWorkBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JointWorkAdapter.ViewHolder holder ;

        if (convertView == null) {
            holder = new JointWorkAdapter.ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.lv_attendance_row, null);
            holder.tvAttendanceReason = convertView.findViewById(R.id.tv_attendance);
            convertView.setTag(holder);
        } else {
            holder = (JointWorkAdapter.ViewHolder) convertView.getTag();
        }
        holder.tvAttendanceReason.setText(resultArrayshort.get(position).getUserName());
        return convertView;
    }

    private class ViewHolder {
        TextView tvAttendanceReason;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        resultArrayshort.clear();
        if (charText.length() == 0) {

            resultArrayshort.addAll(jointWorkBeanArrayList);
        } else {
            for (int i = 0; i < jointWorkBeanArrayList.size(); i++) {
                String fullname = jointWorkBeanArrayList.get(i).getUserName();

                // String firstname = fullname.substring(0, fullname.indexOf(' '));

                if (fullname.toLowerCase(Locale.getDefault()).contains(charText)) {
                    resultArrayshort.add(jointWorkBeanArrayList.get(i));

                }
            }
        }
        notifyDataSetChanged();
    }
}

