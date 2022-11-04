package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.CategoryListBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Admin on 12/5/2017.
 */

public class ReportCategoryAdapter extends BaseAdapter {
    Context mContext;
    private final ArrayList<CategoryListBean> categoryListBeans ;
    public static ArrayList<CategoryListBean> resultArrayshort = new ArrayList<>();

    @Override
    public int getCount() {
        return resultArrayshort.size();
    }

    public ReportCategoryAdapter(Context mContext, ArrayList<CategoryListBean> categoryListBeans) {
        this.mContext = mContext;
        this.categoryListBeans = categoryListBeans;

        resultArrayshort.clear();
        resultArrayshort.addAll(categoryListBeans);
    }

    @Override
    public Object getItem(int position) {
        return resultArrayshort.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.category_spinnner_row, null);

            holder.tvName = convertView.findViewById(R.id.tv_city);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(resultArrayshort.get(position).getCategory_name());
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        resultArrayshort.clear();
        if (charText.length() == 0) {

            resultArrayshort.addAll(categoryListBeans);
        } else {
            for (int i = 0; i < categoryListBeans.size(); i++) {
                String fullname = categoryListBeans.get(i).getCategory_name();

                // String firstname = fullname.substring(0, fullname.indexOf(' '));

                if (fullname.toLowerCase(Locale.getDefault()).contains(charText)) {
                    resultArrayshort.add(categoryListBeans.get(i));

                }
            }
        }
        notifyDataSetChanged();
    }
}
