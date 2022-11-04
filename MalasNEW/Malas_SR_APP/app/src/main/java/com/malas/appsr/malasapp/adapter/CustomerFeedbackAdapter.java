package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.CustomerFeedbackData;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;

/**
 * Created by Arwa on 04-Oct-18.
 */


public class CustomerFeedbackAdapter extends BaseAdapter {
    Context context;
    private final ArrayList<CustomerFeedbackData> customerFeedbackDataArray;

    public CustomerFeedbackAdapter(Context context, ArrayList<CustomerFeedbackData> customerFeedbackDataArray) {
        this.context = context;
        this.customerFeedbackDataArray = customerFeedbackDataArray;
    }

    @Override
    public int getCount() {
        return customerFeedbackDataArray.size();
    }

    @Override
    public Object getItem(int position) {
        return customerFeedbackDataArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.customer_feedback_details, null);
            holder.tvProductName = convertView.findViewById(R.id.tv_product_name);
            holder.rbQty = convertView.findViewById(R.id.rb_qty);
            holder.rbtaste = convertView.findViewById(R.id.rb_taste);
            holder.rbpackaging = convertView.findViewById(R.id.rb_packaging);
            holder.tvQtySetRating = convertView.findViewById(R.id.tv_qty_rating);
            holder.tvTasteSetRating = convertView.findViewById(R.id.tv_taste);
            holder.tvPacakagingSetRating = convertView.findViewById(R.id.tv_packaging);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.tvProductName.setText(customerFeedbackDataArray.get(position).getProductName());
        holder.rbQty.setRating(Float.parseFloat(customerFeedbackDataArray.get(position).getQty()));
       holder.rbtaste.setRating(Float.parseFloat(customerFeedbackDataArray.get(position).getTaste()));
      holder.rbpackaging.setRating(Float.parseFloat(customerFeedbackDataArray.get(position).getPackaging()));
      holder.tvQtySetRating.setText(Float.parseFloat(customerFeedbackDataArray.get(position).getQty())+"");
      holder.tvTasteSetRating.setText(Float.parseFloat(customerFeedbackDataArray.get(position).getTaste())+"");
      holder.tvPacakagingSetRating.setText(Float.parseFloat(customerFeedbackDataArray.get(position).getPackaging())+"");

        return convertView;
    }

    private class ViewHolder {
        TextView tvProductName;
        RatingBar rbQty;
        RatingBar rbtaste;
        RatingBar rbpackaging;
        TextView tvQtySetRating;
        TextView tvTasteSetRating;
        TextView tvPacakagingSetRating;



    }
}
