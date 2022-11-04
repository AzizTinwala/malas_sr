package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.BroadcastBean;
import com.malas.appsr.malasapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 06-Jan-18.
 */

public class BroadcastAdapter extends BaseAdapter {
    Context mContext;
    private final ArrayList<BroadcastBean> broadcastBeanArrayList;

    public BroadcastAdapter(Context mContext, ArrayList<BroadcastBean> broadcastBeanArrayList) {
        this.mContext = mContext;
        this.broadcastBeanArrayList = broadcastBeanArrayList;

    }


    @Override
    public int getCount() {
        return broadcastBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return broadcastBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.broadcast_row_layout, null);
            holder.ivBroadcast = convertView.findViewById(R.id.iv_broadcast);
            holder.tvTitle = convertView.findViewById(R.id.tv_broadcast_title);
            holder.tvMessage = convertView.findViewById(R.id.tv_broadcast_message);
            holder.tvTime = convertView.findViewById(R.id.tv_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvMessage.setMovementMethod(new ScrollingMovementMethod());

        Picasso.with(mContext)
                .load("https://www.malasportal.in/mportal/Api/firebase/" + broadcastBeanArrayList.get(position).getImageUrl())
                .error(R.mipmap.ic_launcher)
                .into(holder.ivBroadcast);

        holder.tvTitle.setText(broadcastBeanArrayList.get(position).getTitle());
        holder.tvMessage.setText(broadcastBeanArrayList.get(position).getMessage());
        holder.tvTime.setText(broadcastBeanArrayList.get(position).getTime());
        return convertView;
    }

    private class ViewHolder {

        ImageView ivBroadcast;
        TextView tvTitle, tvMessage;

        TextView tvTime;
    }


}