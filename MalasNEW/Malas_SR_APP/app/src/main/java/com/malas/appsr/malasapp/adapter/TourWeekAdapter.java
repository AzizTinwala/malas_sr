package com.malas.appsr.malasapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malas.appsr.malasapp.R;

public class TourWeekAdapter extends RecyclerView.Adapter<TourWeekAdapter.ViewHolder> {
    String[] weeks;
    OnClickListener listener;

    public TourWeekAdapter(String[] weeks, OnClickListener listener) {
        this.listener = listener;
        this.weeks=weeks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tour_week_item, parent, false);
        return new ViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TourWeekAdapter.ViewHolder holder, int position) {
        holder.name.setText(weeks[position]);
    }

    @Override
    public int getItemCount() {
        return weeks.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(@NonNull View itemView, OnClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.week_name);
            itemView.setOnClickListener(v -> listener.onClick(getAbsoluteAdapterPosition()));

        }
    }

    public interface OnClickListener {
        void onClick(int position);
    }
}
