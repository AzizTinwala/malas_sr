package com.malas.appsr.malasapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.RetailerBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 08-09-2016.
 */
public class TypeOfAppoinmentAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<RetailerBean> TypeOfAppoinmentList = new ArrayList<>();

    public TypeOfAppoinmentAdapter(Context mContext, ArrayList<RetailerBean> TypeOfAppoinmentList) {
        this.mContext = mContext;
        this.TypeOfAppoinmentList = TypeOfAppoinmentList;

    }

    @Override
    public int getCount() {
        return TypeOfAppoinmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return TypeOfAppoinmentList.get(position);
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
        holder.tvName.setText(TypeOfAppoinmentList.get(position).getRetailerType());
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
    }


}
