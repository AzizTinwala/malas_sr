package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.R;

import java.util.ArrayList;

/**
 * Created by Arwa on 26-Sep-18.
 */

class RatingAdapter extends BaseAdapter{
    Context mContext;
    static ArrayList<String> ratingOfAll = new ArrayList<>();


     RatingAdapter(Context context, ArrayList<String> ratingOfAll) {
        this.mContext = context;
        RatingAdapter.ratingOfAll = ratingOfAll;
    }


    @Override
    public int getCount() {
        return ratingOfAll.size();
    }





    @Override
    public Object getItem(int position) {
        return ratingOfAll.get(position);
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

                convertView = mInflater.inflate(R.layout.rating_row, null);
            holder.tvName =  convertView.findViewById(R.id.tv_city);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(ratingOfAll.get(position));
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
    }

}
