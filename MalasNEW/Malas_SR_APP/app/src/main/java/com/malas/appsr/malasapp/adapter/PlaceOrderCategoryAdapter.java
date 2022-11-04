package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malas.appsr.malasapp.BeanClasses.PlaceOrderListBean;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.activities.ShowPlaceOrderNew;

import java.util.ArrayList;

/**
 * Created by Amit Gupta on 14-11-2016.
 * Updated by Aziz Tinwala on 09-04-2021.
 */
public class PlaceOrderCategoryAdapter extends RecyclerView.Adapter<PlaceOrderCategoryAdapter.ViewHolder> {
    Context mContext;
    private final ArrayList<PlaceOrderListBean> categoryArray;
    CategoryClickListner listner;
    int position=-1;

    public PlaceOrderCategoryAdapter(Context mContext, ArrayList<PlaceOrderListBean> categoryArray, CategoryClickListner listner) {
        this.mContext = mContext;
        this.categoryArray = categoryArray;
        this.listner = listner;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_layout, null, false);
        return new ViewHolder(view, listner);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(categoryArray.get(position).getItem());
        holder.itemView.setOnClickListener(view -> {

            listner.onCategoryClick(position);
            this.position= holder.getAbsoluteAdapterPosition();
            notifyDataSetChanged();

        });
        if (this.position == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#3F51B5"));
            holder.tvName.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            holder.tvName.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return categoryArray.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public ViewHolder(@NonNull View itemView, CategoryClickListner listner) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_category);

        }
    }

    public interface CategoryClickListner {
        void onCategoryClick(int position);
    }
}