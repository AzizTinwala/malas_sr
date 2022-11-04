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

public class LeaveRevertRequestAdapter extends RecyclerView.Adapter<LeaveRevertRequestAdapter.LeaveStatusHolder> {
    private final ArrayList<LeaveRequestStatusOject> leaveRevertObject;
    private final Context context;

    public LeaveRevertRequestAdapter(Context context, ArrayList<LeaveRequestStatusOject> leaveRevertObject) {
        this.leaveRevertObject = leaveRevertObject;
        this.context = context;

    }

    @NonNull
    @Override
    public LeaveStatusHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.leave_revert_request_status_table, viewGroup, false);
        return new LeaveStatusHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull LeaveStatusHolder holder, final int position) {
        holder.tvStartDate.setText(Utils.formatDate(leaveRevertObject.get(position).getStartDate()));
        holder.tv_end_date.setText(Utils.formatDate(leaveRevertObject.get(position).getEndDate()));
        holder.tv_date.setText(Utils.formatDateFromtime(leaveRevertObject.get(position).getDate()));
        holder.tvDays.setText(leaveRevertObject.get(position).getDays());
        holder.type.setText(leaveRevertObject.get(position).getType());
        if (leaveRevertObject.get(position).getStatus()==0){
            holder.iv_status.setImageResource(R.drawable.pending_icon);
        }else if (leaveRevertObject.get(position).getStatus()==1){
            holder.iv_status.setImageResource(R.drawable.icon_approve);
        }else if (leaveRevertObject.get(position).getStatus()==2){
            holder.iv_status.setImageResource(R.drawable.icon_reject);
        }else  if (leaveRevertObject.get(position).getStatus()==3){
            holder.iv_status.setImageResource(R.drawable.pending_icon);
        }


    }

    @Override
    public int getItemCount() {
        return leaveRevertObject.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    static class LeaveStatusHolder extends RecyclerView.ViewHolder {
        TextView tvStartDate;
        TextView tv_end_date;
        TextView tvDays;
        TextView tvStatus;
        TextView type;
        TextView tv_date;
        ImageView iv_status;


        LeaveStatusHolder(View itemView) {
            super(itemView);
            tvStartDate = itemView.findViewById(R.id.tv_start_date);
            tv_end_date = itemView.findViewById(R.id.tv_end_date);
            tv_date = itemView.findViewById(R.id.tv_date);
            tvDays = itemView.findViewById(R.id.tv_days);
            tvStatus = itemView.findViewById(R.id.status);
            type = itemView.findViewById(R.id.type);
            iv_status = itemView.findViewById(R.id.iv_status);

        }
    }
}
