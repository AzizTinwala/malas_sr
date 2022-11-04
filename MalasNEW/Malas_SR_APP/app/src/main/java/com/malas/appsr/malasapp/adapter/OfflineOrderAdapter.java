package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.ReasonSubmitBean;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderItemBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;

/**
 * Created by Admin on 25-Apr-18.
 */

public class OfflineOrderAdapter extends BaseAdapter {
    Context context;
    private final ArrayList<TakeOutletOrderItemBean> productList;
    private final ArrayList<ReasonSubmitBean> reasonSubmitBeans;

    public OfflineOrderAdapter(Context context, ArrayList<TakeOutletOrderItemBean> productList, ArrayList<ReasonSubmitBean> reasonSubmitBeans) {
        this.context = context;
        this.productList = productList;
        this.reasonSubmitBeans = reasonSubmitBeans;
    }


    @Override
    public int getCount() {
        if (productList != null) {
            return productList.size();
        } else {
            return reasonSubmitBeans.size();
        }

    }

    @Override
    public Object getItem(int position) {
        if (productList != null) {
            return productList.get(position);
        } else {
            return reasonSubmitBeans.get(position);
        }

    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.offline_list_order, null);
            holder.llOfflineList = convertView.findViewById(R.id.ll_offline_list);
            holder.ll_offline_take_order_list = convertView.findViewById(R.id.ll_list_take_order_offline);

            if (productList != null) {

                holder.tvCatName = convertView.findViewById(R.id.tvCategory_name);
                holder.tvProductName = convertView.findViewById(R.id.tv_product_name);
                holder.tvQuantity = convertView.findViewById(R.id.tv_quantity);
            } else if (reasonSubmitBeans != null) {

                holder.tvOutletName = convertView.findViewById(R.id.tv_outletName);
                holder.tvReason = convertView.findViewById(R.id.tvReason);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (productList != null) {
            holder.llOfflineList.setVisibility(View.VISIBLE);
            holder.ll_offline_take_order_list.setVisibility(View.GONE);
            holder.tvCatName.setText(productList.get(position).getCatName());
            holder.tvProductName.setText(productList.get(position).getProduct_name());
            holder.tvQuantity.setText(productList.get(position).getProduct_qty());


        } else if (reasonSubmitBeans != null) {
            holder.llOfflineList.setVisibility(View.GONE);
            holder.ll_offline_take_order_list.setVisibility(View.VISIBLE);
            holder.tvOutletName.setText(reasonSubmitBeans.get(position).getOutletName());
            holder.tvReason.setText(reasonSubmitBeans.get(position).getReason());

        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvCatName;
        TextView tvProductName;
        TextView tvQuantity;
        LinearLayout llOfflineList;
        LinearLayout ll_offline_take_order_list;
        TextView tvOutletName;
        TextView tvReason;


    }
}
