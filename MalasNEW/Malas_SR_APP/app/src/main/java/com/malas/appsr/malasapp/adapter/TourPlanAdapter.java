package com.malas.appsr.malasapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malas.appsr.malasapp.BeanClasses.TourPlanBean;
import com.malas.appsr.malasapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TourPlanAdapter extends RecyclerView.Adapter<TourPlanAdapter.ViewHolder> {
    List<TourPlanBean> tpList;
    OnClickListener listener;

    public TourPlanAdapter(List<TourPlanBean> tpList, OnClickListener listener) {
        this.tpList = tpList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tour_plan_layout, parent, false);
        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.date.setText(new SimpleDateFormat("dd", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(tpList.get(position).getDate())));
            holder.day.setText(new SimpleDateFormat("EEE", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(tpList.get(position).getDate())));
            holder.town.setText(String.format("%s-%s", tpList.get(position).getTownFrom(), tpList.get(position).getTownFrom()));
            holder.town.setText(String.format("%s-%s", tpList.get(position).getTownFrom(), tpList.get(position).getWorkType()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, day, town, work;

        public ViewHolder(View itemView, OnClickListener listener) {
            super(itemView);
            date = itemView.findViewById(R.id.tour_date);
            day = itemView.findViewById(R.id.tour_day);
            town = itemView.findViewById(R.id.tour_town);
            work = itemView.findViewById(R.id.tour_work);
        }
    }

    public interface OnClickListener {
        void onPlanClick(int position);

        void onPlanLongClick(int position);

    }
}
