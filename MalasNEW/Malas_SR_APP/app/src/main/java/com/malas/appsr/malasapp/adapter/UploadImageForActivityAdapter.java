package com.malas.appsr.malasapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.malas.appsr.malasapp.BeanClasses.ImageActivityBean;
import com.malas.appsr.malasapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Arwa on 24-Oct-18.
 */

public class UploadImageForActivityAdapter extends BaseAdapter {
    Context mContext;

    ArrayList<ImageActivityBean> imageActivityBeanArrayList;


    public UploadImageForActivityAdapter(Context mContext,ArrayList<ImageActivityBean> imageActivityBeanArrayList) {
        this.mContext = mContext;

        this.imageActivityBeanArrayList = imageActivityBeanArrayList;

    }
    @Override
    public int getCount() {
        return imageActivityBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageActivityBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.activity_image_upload_grid_row, null);
            holder.imgActivity = convertView.findViewById(R.id.img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Picasso.with(mContext).load(imageActivityBeanArrayList.get(position).getImgUrl()).error(R.drawable.logo).into(  holder.imgActivity);



        return convertView;
    }

    private class ViewHolder {

        ImageView imgActivity;


    }

}
