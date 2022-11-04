package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.ProductivityBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;

/**
 * Created by Admin on 11/30/2017.
 */

public class ProductivityAdapter extends BaseAdapter {
    Context mContext;
    private final ArrayList<ProductivityBean> produNonProductiveList;
    private final ArrayList<ProductivityBean> productivelist;
    private final ArrayList productivityList;

    public ProductivityAdapter(Context mContext, ArrayList<ProductivityBean> productiveNonProductive, ArrayList<ProductivityBean> productivelist, ArrayList productivityList) {
        this.mContext = mContext;
        this.produNonProductiveList = productiveNonProductive;
        this.productivelist = productivelist;
        this.productivityList = productivityList;

    }


    @Override
    public int getCount() {
        return produNonProductiveList.size();
    }

    @Override
    public Object getItem(int position) {
        return produNonProductiveList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.productivity_list, null);
            holder.tvDate = convertView.findViewById(R.id.tvDatelist);
            holder.tvProductiveNonProductiveCount = convertView.findViewById(R.id.tvTotalOutletVisitlist);
            holder.tvProductiveOutletCount = convertView.findViewById(R.id.tvTotalOuletProductivelist);
            holder.tvProductivity = convertView.findViewById(R.id.tvProductivitylist);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.tvDate.setText(produNonProductiveList.get(position).getDate());
        holder.tvProductiveNonProductiveCount.setText(produNonProductiveList.get(position).getOutletCount() + "");
        holder.tvProductiveOutletCount.setText(productivelist.get(position).getOutletCount() + "");
        holder.tvProductivity.setText( productivityList.get(position)+" %");
        /*List tempList=new ArrayList();
        for (int i=0;i<productivelist.size();i++){
            for(int j=0;j<productiveOutletNameBeanArrayList.size();j++)
            {
                if (productivelist.get(i).getDate().equalsIgnoreCase(productiveOutletNameBeanArrayList.get(i).getDate())){

                }

            }
        }*/
        return convertView;
    }

    private class ViewHolder {
        TextView tvDate;
        TextView tvProductiveOutletCount;
        TextView tvProductivity;
        TextView tvProductiveNonProductiveCount;
    }

}
