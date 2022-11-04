package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.CategoryListBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;

/**
 * Created by xyz on 9/27/2016.
 */
public class CategoryListAdapter extends BaseAdapter {
    Context mContext;
    private final ArrayList<CategoryListBean> catagoryInfoList;

    public CategoryListAdapter(Context mContext, ArrayList<CategoryListBean> catagoryInfoList) {
        this.mContext = mContext;
        this.catagoryInfoList = catagoryInfoList;
    }

    @Override
    public int getCount() {
        return catagoryInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return catagoryInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;

        holder = new ViewHolder();

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.category_row, null);

        holder.tvName = convertView.findViewById(R.id.tv_row_category);
        holder.radioGroup = convertView.findViewById(R.id.rgroup);
        holder.rbYes = convertView.findViewById(R.id.rb_yes);
        holder.rbNo = convertView.findViewById(R.id.rb_no);
        holder.rbMalas = convertView.findViewById(R.id.rb_na);
        final ViewHolder finalHolder = holder;
        holder.rbYes.setOnClickListener(v -> {
            catagoryInfoList.get(position).setSelectedOption("Y");
            System.out.println("In checked listner position:- " + position);
            System.out.println(catagoryInfoList.get(position).toString());
            finalHolder.rbYes.setChecked(true);
            finalHolder.rbNo.setChecked(false);
            finalHolder.rbMalas.setChecked(false);
        });
        holder.rbNo.setOnClickListener(v -> {
            catagoryInfoList.get(position).setSelectedOption("N");
            System.out.println("In checked listner position:- " + position);
            System.out.println(catagoryInfoList.get(position).toString());
            finalHolder.rbYes.setChecked(false);
            finalHolder.rbNo.setChecked(true);
            finalHolder.rbMalas.setChecked(false);
        });
        holder.rbMalas.setOnClickListener(v -> {
            catagoryInfoList.get(position).setSelectedOption("NA");
            System.out.println("In checked listner position:- " + position);
            System.out.println(catagoryInfoList.get(position).toString());
            finalHolder.rbYes.setChecked(false);
            finalHolder.rbNo.setChecked(false);
            finalHolder.rbMalas.setChecked(true);
        });
        holder.tvName.setText(catagoryInfoList.get(position).getCategory_name());
        if (catagoryInfoList.get(position).getSelectedOption().equalsIgnoreCase("Y")) {
            holder.rbYes.setChecked(true);
            holder.rbNo.setChecked(false);
            holder.rbMalas.setChecked(false);
        } else if (catagoryInfoList.get(position).getSelectedOption().equalsIgnoreCase("N")) {
            holder.rbYes.setChecked(false);
            holder.rbNo.setChecked(true);
            holder.rbMalas.setChecked(false);
        } else if (catagoryInfoList.get(position).getSelectedOption().equalsIgnoreCase("NA")) {
            holder.rbYes.setChecked(false);
            holder.rbNo.setChecked(false);
            holder.rbMalas.setChecked(true);
        } else {
            holder.rbYes.setChecked(false);
            holder.rbNo.setChecked(false);
            holder.rbMalas.setChecked(false);
        }
        System.out.println("position:- " + position);
        System.out.println(catagoryInfoList.get(position).toString());
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        RadioGroup radioGroup;
        RadioButton rbYes, rbNo, rbMalas;
    }
}