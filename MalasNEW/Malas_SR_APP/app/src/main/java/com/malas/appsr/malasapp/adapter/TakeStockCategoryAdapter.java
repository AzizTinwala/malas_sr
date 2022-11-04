package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malas.appsr.malasapp.BeanClasses.TakeStoclListBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;

/**
 * Created by Amit Gupta on 14-11-2016.
 * Edited  by Aziz On 31-08-2021.
 */

public class TakeStockCategoryAdapter extends RecyclerView.Adapter<TakeStockCategoryAdapter.ViewHolder> {
    Context mContext;
    ArrayList<TakeStoclListBean> categoryArray;
    categoryClickListner listner;
int position=-1;
    public TakeStockCategoryAdapter(Context mContext, ArrayList<TakeStoclListBean> categoryArray, categoryClickListner listner) {
        this.mContext = mContext;
        this.categoryArray = categoryArray;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_layout, null, false);

        return new ViewHolder(view, listner, categoryArray);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(categoryArray.get(position).getItem());

        holder.itemView.setOnClickListener((View.OnClickListener) view -> {

            listner.OnCategoryClick(position);
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

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        Integer position;
        categoryClickListner listner;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(@NonNull View itemView, categoryClickListner listner, ArrayList<TakeStoclListBean> categoryArray) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_category);
            this.listner = listner;

        }
    }

    public interface categoryClickListner {
        void OnCategoryClick(int position);
    }


}