package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.OutletDetailsInActivity;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;

/**
 * Created by Arwa on 02-Oct-18.
 */

public class SRDetailAdapter extends BaseAdapter {
    Context context;
    private final ArrayList<OutletDetailsInActivity> outletDetailsInActivities;

    public SRDetailAdapter(Context context, ArrayList<OutletDetailsInActivity> outletDetailsInActivities) {
        this.context = context;
        this.outletDetailsInActivities = outletDetailsInActivities;

    }

    @Override
    public int getCount() {
        return outletDetailsInActivities.size();
    }

    @Override
    public Object getItem(int position) {
        return outletDetailsInActivities.get(position);
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

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.detail_listview_row, null);
            holder.tvOutletName = convertView.findViewById(R.id.tv_outlet_ac);

            holder.rbQty = convertView.findViewById(R.id.rb_avg_qty);
            holder.rbtaste = convertView.findViewById(R.id.rb_avg_taste);
            holder.rbpackaging = convertView.findViewById(R.id.rb_avg_packaging);
            holder.tvQtySetRating = convertView.findViewById(R.id.tv_avg_qty_rating);
            holder.tvTasteSetRating = convertView.findViewById(R.id.tv_avg_taste);
            holder.tvPacakagingSetRating = convertView.findViewById(R.id.tv_avg_packaging);

            holder.llQty = convertView.findViewById(R.id.ll_qty);
            holder.llTaste = convertView.findViewById(R.id.ll_taste);
            holder.llPack = convertView.findViewById(R.id.ll_pacakging);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.tvOutletName.setText(outletDetailsInActivities.get(position).getOutletName());
        if (outletDetailsInActivities.get(position).getAvg_qty().equals("")) {
            holder.rbQty.setRating(Float.parseFloat("0.0"));
            holder.tvQtySetRating.setText("0.0");
        } else {

            holder.rbQty.setRating(Float.parseFloat(outletDetailsInActivities.get(position).getAvg_qty()));
            holder.tvQtySetRating.setText(Float.parseFloat(outletDetailsInActivities.get(position).getAvg_qty()) + "");
        }
        if (outletDetailsInActivities.get(position).getAvg_taste().equals("")) {
            holder.rbtaste.setRating(Float.parseFloat("0.0"));
            holder.tvTasteSetRating.setText("0.0");

        } else {
            holder.rbtaste.setRating(Float.parseFloat(outletDetailsInActivities.get(position).getAvg_taste()));
            holder.tvTasteSetRating.setText(Float.parseFloat(outletDetailsInActivities.get(position).getAvg_taste()) + "");
        }
        if (outletDetailsInActivities.get(position).getAvg_packaging().equals("")) {
            holder.rbpackaging.setRating(Float.parseFloat("0.0"));
            holder.tvPacakagingSetRating.setText("0.0");

        } else {

            holder.rbpackaging.setRating(Float.parseFloat(outletDetailsInActivities.get(position).getAvg_packaging()));
            holder.tvPacakagingSetRating.setText(Float.parseFloat(outletDetailsInActivities.get(position).getAvg_packaging()) + "");

        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvOutletName;
        RatingBar rbQty;
        RatingBar rbtaste;
        RatingBar rbpackaging;
        TextView tvQtySetRating;
        TextView tvTasteSetRating;
        TextView tvPacakagingSetRating;
        LinearLayout llQty, llTaste, llPack;


    }
}
