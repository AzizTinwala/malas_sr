package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malas.appsr.malasapp.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Arwa on 25-Sep-18.
 */

public class ProductSoldSpinnerAdapter extends BaseAdapter{
    Context context;
    private final ArrayList<String> productSoldList;
    public static ArrayList<String > resultArrayshort=new ArrayList<>();

    public ProductSoldSpinnerAdapter(Context context, ArrayList<String> productSoldList) {
       this. context=context;this.productSoldList=productSoldList;
        resultArrayshort.clear();
        resultArrayshort.addAll(productSoldList);
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
       ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = mInflater.inflate(R.layout.city_list_row, null);
            holder.tvName = convertView.findViewById(R.id.tv_city);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(resultArrayshort.get(position));
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        resultArrayshort.clear();
        if (charText.length() == 0) {

            resultArrayshort.addAll(productSoldList);
        } else {
            for (int i = 0; i < productSoldList.size(); i++) {
                String fullname = productSoldList.get(i);

                // String firstname = fullname.substring(0, fullname.indexOf(' '));

                if (fullname.toLowerCase(Locale.getDefault()).contains(charText)) {
                    resultArrayshort.add(productSoldList.get(i));

                }
            }
        }
        notifyDataSetChanged();
    }
}
