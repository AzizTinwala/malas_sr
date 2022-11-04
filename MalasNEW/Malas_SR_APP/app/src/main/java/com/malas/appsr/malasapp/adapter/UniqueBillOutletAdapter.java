package com.malas.appsr.malasapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.UniqueBillBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Arwa on 10/11/2017.
 */

public class UniqueBillOutletAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<UniqueBillBean> uniqueOutletBean;


    public UniqueBillOutletAdapter(Context mContext, ArrayList<UniqueBillBean> uniqueOutletBean) {
        this.mContext = mContext;
        this.uniqueOutletBean = uniqueOutletBean;

    }


    @Override
    public int getCount() {
        return uniqueOutletBean.size();
    }

    @Override
    public Object getItem(int position) {
        return uniqueOutletBean.get(position);
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
            assert mInflater != null;
            convertView = mInflater.inflate(R.layout.unique_outlet_report_layout, null);
            holder.tvOutletName = convertView.findViewById(R.id.tvOutletName);
            holder.tvOutletVisitedCount = convertView.findViewById(R.id.tvlvCount);
            holder.tvRouteName = convertView.findViewById(R.id.tvRouteName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvOutletName.setText(uniqueOutletBean.get(position).getOutletName().toUpperCase(Locale.getDefault()));
        holder.tvOutletVisitedCount.setText(uniqueOutletBean.get(position).getOutletCount()+"");
        holder.tvRouteName.setText(uniqueOutletBean.get(position).getRouteName().toUpperCase(Locale.getDefault()));
        return convertView;
    }

    private class ViewHolder {
        TextView tvOutletName;
        TextView tvOutletVisitedCount;
        TextView tvRouteName;
    }


}
