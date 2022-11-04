package com.malas.appsr.malasapp.adapter;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class ShowPlaceOrderAdapterRecyclerView extends RecyclerView.Adapter<ShowPlaceOrderAdapterRecyclerView.MyViewHolder> {

    Context mContext;
    private final ArrayList<OrderStockDifference> showPlaceOrderList;
    private final String userId;

    public ShowPlaceOrderAdapterRecyclerView(Context showPlaceOrder, ArrayList<OrderStockDifference> showPlaceOrderList) {
        this.mContext = showPlaceOrder;
        this.showPlaceOrderList = showPlaceOrderList;
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(mContext, Constant.UserRegInfoPref, MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        userId = mUserLoginInfoBean.getUserId();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName, tvProductName, tvDifference, tvInbox;

        public MyViewHolder(View view) {
            super(view);
            tvCategoryName = view.findViewById(R.id.tvCategoryName);
            tvProductName = view.findViewById(R.id.tvProductName);
            tvInbox = view.findViewById(R.id.tvInbox);
            tvDifference = view.findViewById(R.id.tvDifference);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_order_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tvCategoryName.setText(showPlaceOrderList.get(position).getCategoryName());
        holder.tvProductName.setText(showPlaceOrderList.get(position).getName());
        if (Integer.parseInt(showPlaceOrderList.get(position).getDifference()) > 0) {
            holder.tvDifference.setTextColor(ContextCompat.getColor(mContext, R.color.ForestGreen));
        } else {
            holder.tvDifference.setTextColor(ContextCompat.getColor(mContext, R.color.IndianRed));
        }
        holder.tvInbox.setText(showPlaceOrderList.get(position).getInboxSize());

        final MyViewHolder finalHolder = holder;
        holder.tvInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQuantityDialog(finalHolder.tvInbox, position);
            }
        });


        holder.tvDifference.setText("" + Math.abs(Integer.parseInt(showPlaceOrderList.get(position).getDifference())));
    }

    @Override
    public int getItemCount() {
        return showPlaceOrderList.size();
    }

    public void showQuantityDialog(final TextView etQty, final int position) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View promptsView = li.inflate(R.layout.difference_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

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
                                    showPlaceOrderList.get(position).setInboxSize(userInput.getText().toString().trim());
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