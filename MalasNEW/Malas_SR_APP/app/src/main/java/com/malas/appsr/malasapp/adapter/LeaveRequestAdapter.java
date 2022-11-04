package com.malas.appsr.malasapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malas.appsr.malasapp.BeanClasses.LeaveRequestStatusOject;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;

import java.util.ArrayList;

public class LeaveRequestAdapter extends RecyclerView.Adapter<LeaveRequestAdapter.LeaveStatusHolder> {
    private final ArrayList<LeaveRequestStatusOject> leaveRequestStatusOjects;
    private final Context context;

    public LeaveRequestAdapter(Context context, ArrayList<LeaveRequestStatusOject> subjectTopicObjects) {
        this.leaveRequestStatusOjects = subjectTopicObjects;
        this.context = context;

    }

    @NonNull
    @Override
    public LeaveStatusHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.leave_request_status_table, viewGroup, false);
        return new LeaveStatusHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull LeaveStatusHolder holder, final int position) {
        holder.tvStartDate.setText(Utils.formatDate(leaveRequestStatusOjects.get(position).getStartDate()));
        holder.tv_end_date.setText(Utils.formatDate(leaveRequestStatusOjects.get(position).getEndDate()));
        holder.tv_date.setText(Utils.formatDateFromtime(leaveRequestStatusOjects.get(position).getDate()));
        holder.tvDays.setText(leaveRequestStatusOjects.get(position).getDays());
       // holder.tvStatus.setText(leaveRequestStatusOjects.get(position).getStatus());
        holder.type.setText(leaveRequestStatusOjects.get(position).getType());
        if (leaveRequestStatusOjects.get(position).getRejectReason().equalsIgnoreCase("-")) {
            holder.ll_reject.setVisibility(View.GONE);
        } else {
            holder.ll_reject.setVisibility(View.VISIBLE);
            holder.tvReasonIfReject.setText(leaveRequestStatusOjects.get(position).getRejectReason());

        }
        holder.tv_lreason.setText(leaveRequestStatusOjects.get(position).getLreason());
        if (leaveRequestStatusOjects.get(position).getStatus()==0){
            holder.iv_status.setImageResource(R.drawable.pending_icon);
        }else if (leaveRequestStatusOjects.get(position).getStatus()==1){
            holder.iv_status.setImageResource(R.drawable.icon_approve);
        }else if (leaveRequestStatusOjects.get(position).getStatus()==2){
            holder.iv_status.setImageResource(R.drawable.icon_reject);
        }


    }

    @Override
    public int getItemCount() {
        return leaveRequestStatusOjects.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    static class LeaveStatusHolder extends RecyclerView.ViewHolder {
        TextView tvStartDate;
        TextView tv_end_date;
        TextView tvDays;
        TextView tvReasonIfReject;
        TextView tvStatus;
        TextView type;
        TextView tv_lreason;
        TextView tv_date;
        LinearLayout ll_reject;
        ImageView iv_status;


        LeaveStatusHolder(View itemView) {
            super(itemView);
            tvStartDate = itemView.findViewById(R.id.tv_start_date);
            tv_end_date = itemView.findViewById(R.id.tv_end_date);
            tv_date = itemView.findViewById(R.id.tv_date);
            tvDays = itemView.findViewById(R.id.tv_days);
            tvStatus = itemView.findViewById(R.id.status);
            type = itemView.findViewById(R.id.type);
            tvReasonIfReject = itemView.findViewById(R.id.tv_reason_if_reject);
            tv_lreason = itemView.findViewById(R.id.tv_lreason);
            ll_reject = itemView.findViewById(R.id.ll_reject);
            iv_status = itemView.findViewById(R.id.iv_status);

        }
    }
}


