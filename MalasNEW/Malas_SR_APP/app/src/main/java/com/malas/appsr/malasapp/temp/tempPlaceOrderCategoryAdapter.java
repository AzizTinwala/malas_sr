package com.malas.appsr.malasapp.temp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malas.appsr.malasapp.BeanClasses.PlaceOrderListBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;

public class tempPlaceOrderCategoryAdapter extends RecyclerView.Adapter<tempPlaceOrderCategoryAdapter.viewHolder> {
    Context mContext;
    private final ArrayList<PlaceOrderListBean> categoryArray;
    private final tempPlaceOrderCategoryItemClicked tempPlaceOrderCategoryItemClicked;

    public tempPlaceOrderCategoryAdapter(
            Context mContext, ArrayList<PlaceOrderListBean> categoryArray, tempPlaceOrderCategoryItemClicked tempPlaceOrderCategoryItemClicked) {
        this.mContext = mContext;
        this.categoryArray = categoryArray;
        this.tempPlaceOrderCategoryItemClicked = tempPlaceOrderCategoryItemClicked;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_layout, parent, false);
        return new viewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.tvName.setText(categoryArray.get(position).getItem());
    }

    @Override
    public int getItemCount() {
        return categoryArray.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_category);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tempPlaceOrderCategoryItemClicked.tempPlaceOrderCategoryOnClick(getAbsoluteAdapterPosition());
                }
            });
        }

    }

    interface tempPlaceOrderCategoryItemClicked {
        void tempPlaceOrderCategoryOnClick(int position);
    }
}
