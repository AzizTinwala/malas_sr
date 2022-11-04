package com.malas.appsr.malasapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malas.appsr.malasapp.BeanClasses.CompOffRequestStatusOject;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;

import java.util.ArrayList;

public class CompOffRequestAdapter extends RecyclerView.Adapter<CompOffRequestAdapter.CompoffHolder> {
    private final ArrayList<CompOffRequestStatusOject> compOffRequestStatusOjects;
    private final Context context;

    public CompOffRequestAdapter(Context context, ArrayList<CompOffRequestStatusOject> compOffRequestStatusOjects) {
        this.compOffRequestStatusOjects = compOffRequestStatusOjects;
        this.context = context;

    }

    @NonNull
    @Override
    public CompoffHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.leave_compoff_status_table, viewGroup, false);
        return new CompoffHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull CompoffHolder holder, final int position) {
        holder.request_id.setText(compOffRequestStatusOjects.get(position).getRequestId());
        holder.tvStartDate.setText(Utils.formatDate(compOffRequestStatusOjects.get(position).getFdate()));
        holder.tv_end_date.setText(Utils.formatDate(compOffRequestStatusOjects.get(position).getTdate()));
        holder.validity.setText(compOffRequestStatusOjects.get(position).getRemaining_days());
        holder.tv_date.setText(Utils.formatDateFromtime(compOffRequestStatusOjects.get(position).getSendDate()));
        holder.tvDays.setText(compOffRequestStatusOjects.get(position).getLeave_days());
        holder.tv_lreason.setText(compOffRequestStatusOjects.get(position).getLreason());
        if (compOffRequestStatusOjects.get(position).getStatus() == 0) {
            holder.iv_status.setImageResource(R.drawable.pending_icon);
        } else if (compOffRequestStatusOjects.get(position).getStatus() == 1) {
            holder.iv_status.setImageResource(R.drawable.icon_approve);
        } else if (compOffRequestStatusOjects.get(position).getStatus() == 2) {
            holder.iv_status.setImageResource(R.drawable.icon_reject);
        }


    }

    @Override
    public int getItemCount() {
        return compOffRequestStatusOjects.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    static class CompoffHolder extends RecyclerView.ViewHolder {
        TextView request_id;
        TextView tvStartDate;
        TextView tv_end_date;
        TextView tvDays;
        TextView tv_lreason;
        TextView tv_date;
        TextView validity;
        ImageView iv_status;


        CompoffHolder(View itemView) {
            super(itemView);
            request_id = itemView.findViewById(R.id.tv_request_id);
            tvStartDate = itemView.findViewById(R.id.tv_start_date);
            tv_end_date = itemView.findViewById(R.id.tv_end_date);
            tv_date = itemView.findViewById(R.id.tv_date);
            tvDays = itemView.findViewById(R.id.tv_days);
            tv_lreason = itemView.findViewById(R.id.tv_lreason);
            iv_status = itemView.findViewById(R.id.iv_status);
            validity = itemView.findViewById(R.id.tv_validity);

        }
    }
}



