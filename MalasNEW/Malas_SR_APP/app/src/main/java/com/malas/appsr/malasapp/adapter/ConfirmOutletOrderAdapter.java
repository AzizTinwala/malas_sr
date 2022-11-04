package com.malas.appsr.malasapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malas.appsr.malasapp.BeanClasses.TakeStockEditedItemProductList;
import com.malas.appsr.malasapp.R;

import java.util.List;
import java.util.Locale;

public class ConfirmOutletOrderAdapter extends RecyclerView.Adapter<ConfirmOutletOrderAdapter.ViewHolder> {
    List<TakeStockEditedItemProductList> itemList;

    public ConfirmOutletOrderAdapter(List<TakeStockEditedItemProductList> itemList) {
        this.itemList = itemList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.confirm_outlet_order_item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(itemList.get(position).getItem_name().toUpperCase(Locale.getDefault()));
        holder.qty.setText(itemList.get(position).getItem_qty());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, qty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.confirm_outlet_order_item_product);
            qty = itemView.findViewById(R.id.confirm_outlet_order_item_qty);

        }
    }
}
