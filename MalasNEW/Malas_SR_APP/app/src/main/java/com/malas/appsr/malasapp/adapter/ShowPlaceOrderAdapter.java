package com.malas.appsr.malasapp.adapter;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Amitlibs.utils.ComplexPreferences;
import com.malas.appsr.malasapp.BeanClasses.OrderStockDifference;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ADV on 21-Feb-17.
 */

public class ShowPlaceOrderAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<OrderStockDifference> showPlaceOrderList;
    String userId;

    public ShowPlaceOrderAdapter(Context showPlaceOrder, ArrayList<OrderStockDifference> showPlaceOrderList) {
        this.mContext = showPlaceOrder;
        this.showPlaceOrderList = showPlaceOrderList;
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(mContext, Constant.UserRegInfoPref, MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        userId = mUserLoginInfoBean.getUserId();
    }

    @Override
    public int getCount() {
        return showPlaceOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        return showPlaceOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.place_order_row, null);
            holder.tvCategoryName = convertView.findViewById(R.id.tvCategoryName);
            holder.tvProductName = convertView.findViewById(R.id.tvProductName);
            holder.tvInbox = convertView.findViewById(R.id.tvInbox);
            holder.tvDifference = convertView.findViewById(R.id.tvDifference);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvCategoryName.setText(showPlaceOrderList.get(position).getCategoryName());
        holder.tvProductName.setText(showPlaceOrderList.get(position).getName());
        if (Integer.parseInt(showPlaceOrderList.get(position).getDifference()) > 0) {
            holder.tvDifference.setTextColor(ContextCompat.getColor(mContext, R.color.ForestGreen));
            showPlaceOrderList.get(position).setInboxSize("0");
            showPlaceOrderList.set(position, showPlaceOrderList.get(position));
            holder.tvInbox.setText("0");
        } else {
            holder.tvDifference.setTextColor(ContextCompat.getColor(mContext, R.color.IndianRed));
            int r = Math.abs(Integer.parseInt(showPlaceOrderList.get(position).getDifference())) % Integer.parseInt(showPlaceOrderList.get(position).getPacketSize());
            int q = Math.abs(Integer.parseInt(showPlaceOrderList.get(position).getDifference())) / Integer.parseInt(showPlaceOrderList.get(position).getPacketSize());
            System.out.println("nsc Quotient : " + q);
            System.out.println("nsc Remainder : " + r);
            if (r > 0) {
                q = q + 1;
            }

            holder.tvInbox.setText("" + q);
            showPlaceOrderList.get(position).setInboxSize("" + q);
            showPlaceOrderList.set(position, showPlaceOrderList.get(position));
        }

        final ViewHolder finalHolder = holder;
        holder.tvInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQuantityDialog(finalHolder.tvInbox, position);
            }
        });


        ComplexPreferences mPlaceOrderListPref = ComplexPreferences.getComplexPreferences(mContext, Constant.PlaceOrderProductListPref, MODE_PRIVATE);
        mPlaceOrderListPref.putObject(Constant.PlaceOrderProductListObj, showPlaceOrderList);
        mPlaceOrderListPref.commit();


        holder.tvDifference.setText("" + Math.abs(Integer.parseInt(showPlaceOrderList.get(position).getDifference())));

        return convertView;
    }

    private static class ViewHolder {
        TextView tvCategoryName, tvProductName, tvDifference, tvInbox;
    }


    public void showQuantityDialog(final TextView etQty, final int position) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View promptsView = li.inflate(R.layout.difference_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = promptsView
                .findViewById(R.id.editTextDialogUserInput);

        if (!etQty.getText().toString().trim().equals("")) {
            userInput.setText(etQty.getText().toString().trim());
            userInput.setSelection(etQty.getText().toString().trim().length());
        }

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (Integer.parseInt(userInput.getText().toString().trim()) >= Math.abs(Integer.parseInt(showPlaceOrderList.get(position).getInboxSize()))) {
                                    showPlaceOrderList.get(position).setDifference(userInput.getText().toString().trim());
                                    ComplexPreferences mPlaceOrderListPref = ComplexPreferences.getComplexPreferences(mContext, Constant.PlaceOrderProductListPref, MODE_PRIVATE);
                                    mPlaceOrderListPref.putObject(Constant.PlaceOrderProductListObj, showPlaceOrderList);
                                    mPlaceOrderListPref.commit();
                                    etQty.setText(userInput.getText().toString().trim());
                                } else
                                    Toast.makeText(mContext, "You can not decrease box size", Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // show it
        alertDialog.show();
    }
}