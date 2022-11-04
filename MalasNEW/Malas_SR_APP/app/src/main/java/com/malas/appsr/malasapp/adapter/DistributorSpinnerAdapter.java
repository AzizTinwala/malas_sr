package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;
import java.util.Locale;


public class DistributorSpinnerAdapter extends BaseAdapter {
    public static ArrayList<DistributerBean> resultArrayshort = new ArrayList<>();
    Context mContext;
    private final ArrayList<DistributerBean> distributorList;
    private final String From;

    public DistributorSpinnerAdapter(Context mContext, ArrayList<DistributerBean> distributorList, String From) {
        this.mContext = mContext;
        this.distributorList = distributorList;
        this.From = From;
        resultArrayshort.clear();
        resultArrayshort.addAll(distributorList);
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
        ViewHolder holder ;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (From.equalsIgnoreCase("ShowOutLet"))
                convertView = mInflater.inflate(R.layout.spinner_list_row, null);
            else
                convertView = mInflater.inflate(R.layout.city_list_row, null);
            holder.tvName = convertView.findViewById(R.id.tv_city);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(resultArrayshort.get(position).getFirm_name().toUpperCase(Locale.getDefault()));
        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        resultArrayshort.clear();
        if (charText.length() == 0) {

            resultArrayshort.addAll(distributorList);
        } else {
            for (int i = 0; i < distributorList.size(); i++) {
                String fullname = distributorList.get(i).getFirm_name();

                // String firstname = fullname.substring(0, fullname.indexOf(' '));

                if (fullname.toLowerCase(Locale.getDefault()).contains(charText)) {
                    resultArrayshort.add(distributorList.get(i));

                }
            }
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tvName;
    }
}