package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.CustomerData;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.activities.CustomerFeedbackListActivity;

import java.util.ArrayList;

/**
 * Created by Arwa on 04-Oct-18.
 */

public class CustomerDetailAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<CustomerData> customerDataArray;


    public CustomerDetailAdapter(Context context, ArrayList<CustomerData> customerDataArray) {
        this.context = context;
        this.customerDataArray = customerDataArray;


    }

    @Override
    public int getCount() {
        return customerDataArray.size();
    }

    @Override
    public Object getItem(int position) {
        return customerDataArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;


        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.customer_details, null);
            holder.tvCustomerName = convertView.findViewById(R.id.tv_cust_name);
            holder.tvCustomerEmail = convertView.findViewById(R.id.tv_email_id);
            holder.tvCustomerContact = convertView.findViewById(R.id.tv_contact);

            holder.tvReview = convertView.findViewById(R.id.tv_review);


            holder.rbQty = convertView.findViewById(R.id.rb_qty);
            holder.rbtaste = convertView.findViewById(R.id.rb_taste);
            holder.rbpackaging = convertView.findViewById(R.id.rb_packaging);
            holder.tvQtySetRating = convertView.findViewById(R.id.tv_qty_rating);
            holder.tvTasteSetRating = convertView.findViewById(R.id.tv_taste);
            holder.tvPacakagingSetRating = convertView.findViewById(R.id.tv_packaging);

            holder.llQty = convertView.findViewById(R.id.ll_qty);
            holder.llTaste = convertView.findViewById(R.id.ll_taste);
            holder.llPack = convertView.findViewById(R.id.ll_pacakging);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        holder.tvCustomerName.setText(customerDataArray.get(position).getCname());
        holder.tvCustomerEmail.setText(customerDataArray.get(position).getCemail());
        holder.tvCustomerContact.setText(customerDataArray.get(position).getCcontact());
        holder.tvReview.setTag(position);
        if (customerDataArray.get(position).getSold().equalsIgnoreCase("Y")) {

            holder.tvReview.setVisibility(View.VISIBLE);
            holder.llQty.setVisibility(View.VISIBLE);
            holder.llTaste.setVisibility(View.VISIBLE);
            holder.llPack.setVisibility(View.VISIBLE);
            if (customerDataArray.get(position).getAvg_qty().equals("")) {
                holder.rbQty.setRating(Float.parseFloat("0.0"));
                holder.tvQtySetRating.setText("0.0");
            } else {

                holder.rbQty.setRating(Float.parseFloat(customerDataArray.get(position).getAvg_qty()));
                holder.tvQtySetRating.setText(Float.parseFloat(customerDataArray.get(position).getAvg_qty()) + "");
            }
            if (customerDataArray.get(position).getAvg_taste().equals("")) {
                holder.rbtaste.setRating(Float.parseFloat("0.0"));
                holder.tvTasteSetRating.setText("0.0");

            } else {
                holder.rbtaste.setRating(Float.parseFloat(customerDataArray.get(position).getAvg_taste()));
                holder.tvTasteSetRating.setText(Float.parseFloat(customerDataArray.get(position).getAvg_taste()) + "");
            }
            if (customerDataArray.get(position).getAvg_packaging().equals("")) {
                holder.rbpackaging.setRating(Float.parseFloat("0.0"));
                holder.tvPacakagingSetRating.setText("0.0");

            } else {

                holder.rbpackaging.setRating(Float.parseFloat(customerDataArray.get(position).getAvg_packaging()));
                holder.tvPacakagingSetRating.setText(Float.parseFloat(customerDataArray.get(position).getAvg_packaging()) + "");

            }

        } else {
            holder.tvReview.setVisibility(View.GONE);
            holder.llQty.setVisibility(View.GONE);
            holder.llTaste.setVisibility(View.GONE);
            holder.llPack.setVisibility(View.GONE);
        }

        holder.tvReview.setOnClickListener(v -> {
            Intent intent = new Intent(context, CustomerFeedbackListActivity.class);
            intent.putExtra("customerId", customerDataArray.get(position).getId());
            context.startActivity(intent);


        });


        return convertView;
    }

    private class ViewHolder {
        TextView tvCustomerName;
        TextView tvCustomerEmail;
        TextView tvCustomerContact;
        TextView tvReview;

        RatingBar rbQty;
        RatingBar rbtaste;
        RatingBar rbpackaging;
        TextView tvQtySetRating;
        TextView tvTasteSetRating;
        TextView tvPacakagingSetRating;
        LinearLayout llQty, llTaste, llPack;


    }


}
