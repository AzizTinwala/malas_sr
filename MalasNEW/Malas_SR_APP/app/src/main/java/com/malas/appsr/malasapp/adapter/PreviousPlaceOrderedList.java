package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Amitlibs.utils.ComplexPreferences;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.PreviousPlacedOrderBean;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Admin on 01-Feb-18.
 */

public class PreviousPlaceOrderedList extends BaseAdapter {

    Context mContext;
    private final ArrayList<PreviousPlacedOrderBean>  previousPlacedOrderList;

    public PreviousPlaceOrderedList(Context mContext, ArrayList<PreviousPlacedOrderBean>  previousPlacedOrderList, DistributerBean selectedDistributerBean) {
        this.mContext = mContext;
        this. previousPlacedOrderList =  previousPlacedOrderList;
        DistributerBean selectedDistributerBean1 = selectedDistributerBean;
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(mContext, Constant.UserRegInfoPref, Context.MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        String userId = mUserLoginInfoBean.getUserId();
    }

    @Override
    public int getCount() {
        return  previousPlacedOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        return  previousPlacedOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.take_stock_row, null);
            holder.tvOutlet = convertView.findViewById(R.id.tv_outlet);
            holder.tvdate = convertView.findViewById(R.id.tv_date);
            holder.ivMenu = convertView.findViewById(R.id.iv_menu);
            holder.ivMenu.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvOutlet.setTextColor(Color.BLACK);
        holder.tvdate.setTextColor(Color.BLACK);
        holder.tvOutlet.setText("Placed Order On ");


        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss ",Locale.getDefault()); // the format of your date
        sdf.setTimeZone(TimeZone.getDefault()); // give a timezone reference for formating (see comment at the bottom
        Date date;
        try {
            date = inputFormat.parse(previousPlacedOrderList.get(position).getDatetime());
            String formattedTime = sdf.format(date);
            holder.tvdate.setText(formattedTime);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvOutlet, tvdate;
        ImageView ivMenu;
    }


}
