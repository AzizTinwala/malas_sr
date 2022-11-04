package com.malas.appsr.malasapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malas.appsr.malasapp.BeanClasses.TakeStockEditedItemProductList;
import com.malas.appsr.malasapp.R;

import java.util.List;

public class ConfirmStockAdapter extends RecyclerView.Adapter<ConfirmStockAdapter.ViewHolder> {
    List<TakeStockEditedItemProductList> itemList;

    public ConfirmStockAdapter(List<TakeStockEditedItemProductList> itemList) {
        this.itemList = itemList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.confirm_stock_item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(itemList.get(position).getItem_name());
        holder.qty.setText(itemList.get(position).getItem_qty());
        int r = Math.abs(Integer.parseInt(itemList.get(position).getItem_qty())) % Integer.parseInt(itemList.get(position).getItem_case_size());
        int q = Math.abs(Integer.parseInt(itemList.get(position).getItem_qty())) / Integer.parseInt(itemList.get(position).getItem_case_size());
        int halfcase = Integer.parseInt(itemList.get(position).getItem_case_size()) / 2;

        if (r >= halfcase) {
            q = q + 1;
        }
        holder.boxes.setText(String.valueOf(q));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, qty, boxes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.confirm_stock_item_product);
            qty = itemView.findViewById(R.id.confirm_stock_item_qty);
            boxes = itemView.findViewById(R.id.confirm_stock_item_boxes);
        }
    }
}
