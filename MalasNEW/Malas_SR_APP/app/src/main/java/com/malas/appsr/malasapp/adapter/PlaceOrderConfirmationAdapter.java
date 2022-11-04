package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.PlaceOrderItemBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Admin on 31-Jan-18.
 */

public class PlaceOrderConfirmationAdapter  extends BaseAdapter{
    Context context;
    private final ArrayList<PlaceOrderItemBean> placeOrderItemBeans;
   public PlaceOrderConfirmationAdapter(Context context, ArrayList<PlaceOrderItemBean> placeOrderItemBeans){
        this.context=context;
        this.placeOrderItemBeans=placeOrderItemBeans;
    }
    @Override
    public int getCount() {
        return placeOrderItemBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return placeOrderItemBeans.get(position);
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
            convertView = mInflater.inflate(R.layout.confirm_place_order_list, null);
            holder.tvProductname = convertView.findViewById(R.id.tvProductName);
           // holder.tvDifference = (TextView) convertView.findViewById(R.id.tvSOH);
            holder.tvInbox = convertView.findViewById(R.id.SOQ);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.tvProductname.setText(placeOrderItemBeans.get(position).getName().toUpperCase(Locale.getDefault()));
       // holder.tvDifference.setText("" + Math.abs(Integer.parseInt(placeOrderItemBeans.get(position).getDifference())));
        holder.tvInbox.setText(placeOrderItemBeans.get(position). getInboxSize()+ "");
        return convertView;
    }

    private class ViewHolder {
        TextView tvProductname;
      //  TextView tvDifference;
        TextView tvInbox;


    }
}
