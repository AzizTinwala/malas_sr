package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.malas.appsr.malasapp.BeanClasses.UserTerritoryBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;


public class UserTerritoryAdapter extends BaseAdapter {
    Context context;
    private final ArrayList<UserTerritoryBean> userTerritoryBeanList;

    public UserTerritoryAdapter(Context context, ArrayList<UserTerritoryBean> userTerritoryBeanList) {
        this.context = context;
        this.userTerritoryBeanList = userTerritoryBeanList;

    }

    @Override
    public int getCount() {
        return userTerritoryBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return userTerritoryBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.lv_user_territory_row, null);
            holder.tvUserTerritory = convertView.findViewById(R.id.tv_user_territory);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvUserTerritory.setText(userTerritoryBeanList.get(position).getTerritoryName()+" ("+userTerritoryBeanList.get(position).getTerritoryState()+")");
        return convertView;
    }

    private class ViewHolder {
        TextView tvUserTerritory;
    }

}