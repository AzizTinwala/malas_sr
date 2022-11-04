package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.CountryListBean;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Administrator on 08-09-2016.
 */
public class CountryAdapter extends BaseAdapter {
    Context mContext;
    private final ArrayList<CountryListBean> countrylistNew;
    public static ArrayList<CountryListBean> resultArrayshort=new ArrayList<>();
    public CountryAdapter(Context mContext, ArrayList<CountryListBean> citylistNew) {
        this.mContext = mContext;
        this.countrylistNew = citylistNew;
        resultArrayshort.clear();
        resultArrayshort.addAll(citylistNew);
    }




    @Override
    public int getCount() {
        return resultArrayshort.size();
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
        ViewHolder holder ;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.state_list_textview, null);
            holder.tvName = convertView.findViewById(R.id.textviewstate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(resultArrayshort.get(position).getCountryName());
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
    }



    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        resultArrayshort.clear();
        if (charText.length() == 0) {

            resultArrayshort.addAll(countrylistNew);
        } else {
            for (int i = 0; i < countrylistNew.size(); i++) {
                String fullname = countrylistNew.get(i).getCountryName();

                // String firstname = fullname.substring(0, fullname.indexOf(' '));

                if (fullname.toLowerCase(Locale.getDefault()).contains(charText)) {
                    resultArrayshort.add(countrylistNew.get(i));

                }
            }
        }
        notifyDataSetChanged();
    }
}
