package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.RouteBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by xyz on 9/14/2016.
 */
public class RouteAdapter extends BaseAdapter {
    Context mContext;
    private final ArrayList<RouteBean> distributorList ;
    public static ArrayList<RouteBean> resultArrayshort=new ArrayList<>();

    public RouteAdapter(Context mContext, ArrayList<RouteBean> distributorList) {
        this.mContext = mContext;
        this.distributorList = distributorList;
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
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.city_list_row, null);
            holder.tvName = convertView.findViewById(R.id.tv_city);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(resultArrayshort.get(position).getRoute_name().toUpperCase(Locale.getDefault()));
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        resultArrayshort.clear();
        if (charText.length() == 0) {

            resultArrayshort.addAll(distributorList);
        } else {
            for (int i = 0; i < distributorList.size(); i++) {
                String fullname = distributorList.get(i).getRoute_name();

                // String firstname = fullname.substring(0, fullname.indexOf(' '));

                if (fullname.toLowerCase(Locale.getDefault()).contains(charText)) {
                    resultArrayshort.add(distributorList.get(i));

                }
            }
        }
        notifyDataSetChanged();
    }
}
