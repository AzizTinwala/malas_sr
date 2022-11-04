package com.malas.appsr.malasapp.BeanClasses;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.malas.appsr.malasapp.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Arwa on 25-Sep-18.
 */

public class ProductSelectedSpinnerAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    Context context;
   public SparseBooleanArray mCheckStates;
    ArrayList<ProductInActivityBean> productToBeInActivity;
    public static ArrayList<ProductInActivityBean> resultArrayshort = new ArrayList<>();

    public ProductSelectedSpinnerAdapter(Context context, ArrayList<ProductInActivityBean> productToBeInActivity) {
        this.context = context;
        this.productToBeInActivity = productToBeInActivity;
        resultArrayshort.clear();
        resultArrayshort.addAll(productToBeInActivity);
        mCheckStates = new SparseBooleanArray(productToBeInActivity.size());
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.product_select_act, null);
            holder.tvName = convertView.findViewById(R.id.tv_product_name);
            holder.chkproName = convertView.findViewById(R.id.ck_product);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(resultArrayshort.get(position).getProductName());
        holder.chkproName.setTag(position);
        holder.chkproName.setChecked(mCheckStates.get(position, false));
        holder.chkproName.setOnCheckedChangeListener(this);
        return convertView;
    }
    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);

    }

    public void toggle(int position) {
        setChecked(position, !isChecked(position));

    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {

        mCheckStates.put((Integer) buttonView.getTag(), isChecked);

    }


    private class ViewHolder {
        TextView tvName;
        CheckBox chkproName;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        resultArrayshort.clear();
        if (charText.length() == 0) {

            resultArrayshort.addAll(productToBeInActivity);
        } else {
            for (int i = 0; i < productToBeInActivity.size(); i++) {
                String fullname = productToBeInActivity.get(i).getProductName();

                // String firstname = fullname.substring(0, fullname.indexOf(' '));

                if (fullname.toLowerCase(Locale.getDefault()).contains(charText)) {
                    resultArrayshort.add(productToBeInActivity.get(i));

                }
            }
        }
        notifyDataSetChanged();
    }
}
