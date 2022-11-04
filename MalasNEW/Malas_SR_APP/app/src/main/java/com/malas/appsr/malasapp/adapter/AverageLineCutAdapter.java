package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.AverageLineCutBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;

/**
 * Created by Admin on 11/24/2017.
 */

public class AverageLineCutAdapter extends BaseAdapter {
    Context mContext;
    private final ArrayList<AverageLineCutBean> averageLineCutBeanArrayList;


    public AverageLineCutAdapter(Context mContext, ArrayList<AverageLineCutBean> averageLineCutBeanArrayList) {
        this.mContext = mContext;
        this.averageLineCutBeanArrayList = averageLineCutBeanArrayList;

    }


    @Override
    public int getCount() {
        return averageLineCutBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return averageLineCutBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.average_line_cut_list, null);
            holder.tvDate = convertView.findViewById(R.id.tvDate);
            holder.tvDistributor = convertView.findViewById(R.id.tvDistributor);
            holder.tvOutletCount = convertView.findViewById(R.id.tvCountOulet);
            holder.tvCategory = convertView.findViewById(R.id.tvCountCategory);
            holder.tvSku = convertView.findViewById(R.id.tvTotalSKU);
            holder.tvLinecut = convertView.findViewById(R.id.tvLineCut);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.tvDate.setText(averageLineCutBeanArrayList.get(position).getDate());
        holder.tvDistributor.setText(averageLineCutBeanArrayList.get(position).getDistributorName());
        holder.tvOutletCount.setText(averageLineCutBeanArrayList.get(position).getCountOutletVisited()+"");
        holder.tvCategory.setText(averageLineCutBeanArrayList.get(position).getCategoryCount()+"");
        holder.tvSku.setText(averageLineCutBeanArrayList.get(position).getSkuCount()+"");
        holder.tvLinecut.setText(averageLineCutBeanArrayList.get(position).getLinecut()+"");
        return convertView;
    }

    private class ViewHolder {
        TextView tvDate;
        TextView tvDistributor;
        TextView tvCategory;
        TextView tvSku;
        TextView tvLinecut;
        TextView tvOutletCount;
    }


}
