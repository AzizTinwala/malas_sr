package com.malas.appsr.malasapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.YearBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by xyz on 9/14/2016.
 */
public class YearAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<YearBean> yearList = new ArrayList<>();
    public static ArrayList<YearBean> resultArrayshort = new ArrayList<>();

    public YearAdapter(Context mContext, ArrayList<YearBean> yearList) {
        this.mContext = mContext;
        this.yearList = yearList;
        resultArrayshort.clear();
        resultArrayshort.addAll(yearList);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

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

            resultArrayshort.addAll(yearList);
        } else {
            for (int i = 0; i < yearList.size(); i++) {
                String fullname = yearList.get(i).getName();

                if (fullname.toLowerCase(Locale.getDefault()).contains(charText)) {
                    resultArrayshort.add(yearList.get(i));

                }
            }
        }
        notifyDataSetChanged();
    }
}
