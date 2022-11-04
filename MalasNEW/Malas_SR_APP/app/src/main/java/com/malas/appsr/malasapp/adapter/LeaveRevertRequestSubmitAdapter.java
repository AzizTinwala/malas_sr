package com.malas.appsr.malasapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.malas.appsr.malasapp.BeanClasses.LeaveRequestStatusOject;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;

import java.util.ArrayList;

public class LeaveRevertRequestSubmitAdapter extends RecyclerView.Adapter<LeaveRevertRequestSubmitAdapter.LeaveStatusHolder> {
    private final ArrayList<LeaveRequestStatusOject> leaveRevertObject;
    private final Context context;
    private final ItemClicks itemClicks;

    public LeaveRevertRequestSubmitAdapter(Context context, ArrayList<LeaveRequestStatusOject> leaveRevertObject, ItemClicks itemClicks) {
        this.leaveRevertObject = leaveRevertObject;
        this.context = context;
        this.itemClicks = itemClicks;
    }

    @NonNull
    @Override
    public LeaveRevertRequestSubmitAdapter.LeaveStatusHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.leave_revert_submit_request, viewGroup, false);
        return new LeaveRevertRequestSubmitAdapter.LeaveStatusHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull LeaveRevertRequestSubmitAdapter.LeaveStatusHolder holder, final int position) {
        holder.tvStartDate.setText(Utils.formatDate(leaveRevertObject.get(position).getStartDate()));
        holder.tv_end_date.setText(Utils.formatDate(leaveRevertObject.get(position).getEndDate()));
        holder.tv_date.setText(Utils.formatDateFromtime(leaveRevertObject.get(position).getDate()));
        holder.tvDays.setText(leaveRevertObject.get(position).getDays());
        holder.type.setText(leaveRevertObject.get(position).getDtype1());
        if (leaveRevertObject.get(position).getStatus() == 0) {
            holder.tvStatus.setText("Pending");
        } else if (leaveRevertObject.get(position).getStatus() == 1) {
            holder.tvStatus.setText("Approved");
        } else if (leaveRevertObject.get(position).getStatus() == 2) {
            holder.tvStatus.setText("Rejected");
        }

        holder.ll_revert.setOnClickListener(v -> {
            holder.ll_revert.setVisibility(View.GONE);
            holder.ll_close.setVisibility(View.VISIBLE);
            holder.ll_revert_submit_request.setVisibility(View.VISIBLE);
            holder.et_start_date.setText(Utils.formatDate(leaveRevertObject.get(position).getStartDate()));
            holder.et_end_date.setText(Utils.formatDate(leaveRevertObject.get(position).getEndDate()));
            holder.et_days.setText(leaveRevertObject.get(position).getDays());


        });
        holder.ll_close.setOnClickListener(v -> {
            holder.ll_revert.setVisibility(View.VISIBLE);
            holder.ll_close.setVisibility(View.GONE);
            holder.ll_revert_submit_request.setVisibility(View.GONE);
            holder.et_start_date.setText("");
            holder.et_end_date.setText("");
            holder.et_days.setText("");
        });

        holder.et_start_date.setOnClickListener(v12 -> {
            itemClicks.startDateClick(holder.et_start_date, holder.et_end_date, holder.et_days, Utils.formatDate(leaveRevertObject.get(position).getStartDate()), Utils.formatDate(leaveRevertObject.get(position).getEndDate()), holder.cd_revert);
        });
        holder.et_end_date.setOnClickListener(v1 -> {
            itemClicks.endDateClick(holder.et_start_date, holder.et_end_date, holder.et_days, Utils.formatDate(leaveRevertObject.get(position).getStartDate()), Utils.formatDate(leaveRevertObject.get(position).getEndDate()), holder.cd_revert);
        });
        holder.btn_sub.setOnClickListener(v13 -> {
            itemClicks.submitClick(holder.et_start_date.getText().toString(), holder.et_end_date.getText().toString(), holder.tvDays, leaveRevertObject.get(position), holder.cd_revert);
        });
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
        LinearLayout ll_revert;
        LinearLayout ll_close;
        LinearLayout ll_revert_submit_request;
        TextView et_start_date;
        TextView et_end_date;
        TextView et_days;
        Button btn_sub;
        CardView cd_revert;

        LeaveStatusHolder(View itemView) {
            super(itemView);
            tvStartDate = itemView.findViewById(R.id.tv_start_date);
            tv_end_date = itemView.findViewById(R.id.tv_end_date);
            tv_date = itemView.findViewById(R.id.tv_date);
            tvDays = itemView.findViewById(R.id.tv_days);
            tvStatus = itemView.findViewById(R.id.status);
            type = itemView.findViewById(R.id.type);
            ll_revert = itemView.findViewById(R.id.ll_revert);
            ll_close = itemView.findViewById(R.id.ll_close);
            ll_revert_submit_request = itemView.findViewById(R.id.ll_revert_submit_request);
            et_start_date = itemView.findViewById(R.id.start_date);
            et_end_date = itemView.findViewById(R.id.end_date);
            et_days = itemView.findViewById(R.id.tv_leaves_day);
            btn_sub = itemView.findViewById(R.id.btn_sub);
            cd_revert = itemView.findViewById(R.id.cd_revert);

        }
    }

    public interface ItemClicks {

        void endDateClick(TextView et_start_date, TextView et_end_date, TextView et_days, String initialStartDate, String initialEndDate, CardView cd_revert);

        void submitClick(String et_start_date, String et_end_date, TextView s, LeaveRequestStatusOject leaveRequestStatusOject, CardView cd_revert);

        void startDateClick(TextView et_start_date, TextView et_end_date, TextView et_days, String initialStartDate, String initialEndDate, CardView cd_revert);
    }
}
