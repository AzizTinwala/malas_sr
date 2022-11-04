package com.malas.appsr.malasapp.temp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.malas.appsr.malasapp.BeanClasses.PlaceOrderListBean;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.Utils;
import com.malas.appsr.malasapp.activities.ShowPlaceOrderNew;

import java.util.ArrayList;
import java.util.Objects;

public class tempPlaceOrderItemListAdapter extends RecyclerView.Adapter<tempPlaceOrderItemListAdapter.ViewHolder> {
    Context mContext;
    private final PlaceOrderListBean selectedCategory;
    private final PlaceOrderListBean selectedCategoryAll;
    private final int parentPosition;

    public tempPlaceOrderItemListAdapter(Context mContext, PlaceOrderListBean categoryArray, int parentPosition) {
        this.mContext = mContext;
        selectedCategory = categoryArray;
        selectedCategoryAll = new PlaceOrderListBean(categoryArray);
        this.parentPosition = parentPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_order_item_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvProductName.setText(selectedCategory.getArryItemList().get(position).getName());

        if (Integer.parseInt(selectedCategory.getArryItemList().get(position).getDifference()) > 0) {
            holder.tvDifference.setTextColor(ContextCompat.getColor(mContext, R.color.ForestGreen));
        } else {
            holder.tvDifference.setTextColor(ContextCompat.getColor(mContext, R.color.IndianRed));
        }
        if (selectedCategory.getArryItemList().get(position).getInboxSize() != null) {
            if (!selectedCategory.getArryItemList().get(position).getInboxSize().equals("0")) {
                holder.tvInbox.setText(selectedCategory.getArryItemList().get(position).getInboxSize());
            } else {
                holder.tvInbox.setText("");
                holder.tvInbox.setHint("0");

            }
        }

        holder.tvInbox.setOnClickListener(view -> showQuantityDialog(holder.tvInbox, position));

        holder.tvDifference.setText("" + Math.abs(Integer.parseInt(selectedCategory.getArryItemList().get(position).getDifference())));


    }

    @Override
    public int getItemCount() {
        return selectedCategory.getArryItemList().size();
    }

    private void showQuantityDialog(final TextView etQty, final int position) {
        LayoutInflater li = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams") View promptsView = li.inflate(R.layout.difference_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = promptsView
                .findViewById(R.id.editTextDialogUserInput);

        if (!etQty.getText().toString().trim().equals("")) {
            userInput.setText(etQty.getText().toString().trim());
        }

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit",
                        (dialog, id) -> {
                            //if (Integer.parseInt(userInput.getText().toString().trim()) >= Math.abs(Integer.parseInt(itemList.get(parentPosition).getArryItemList().get(position).getInboxSize()))) {itemList.get(parentPosition).getArryItemList().get(position).getInboxSize();
                            if (userInput.getText().toString().trim().equals("")) {
                                dialog.cancel();
                            } else {
                                if (Math.abs(Integer.parseInt(selectedCategory.getArryItemList().get(position).getDifference()))
                                        <= (Integer.parseInt(selectedCategory.getArryItemList().get(position).getPacketSize())
                                        * Integer.parseInt(userInput.getText().toString().trim()))) {
                                    etQty.setText(userInput.getText().toString().trim());
                                    selectedCategory.getArryItemList().get(position).setInboxSize(userInput.getText().toString().trim());
                                    ArrayList<PlaceOrderListBean> mTempList = ((ShowPlaceOrderNew) mContext).getProductList();
                                    mTempList.get(parentPosition).setArryItemList(selectedCategory.getArryItemList());
                                    ((ShowPlaceOrderNew) mContext).saveProductList(mTempList);
                                } else {
                                    Utils.showAlertDialog(mContext,"Insufficient Stock","You can not decrease the quantity as Distributor does not have enough stock to supply pending orders.",true);
                                    //Toast.makeText(mContext, "You can not decrease box size", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // show it
        alertDialog.show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductName, tvDifference, tvInbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvInbox = itemView.findViewById(R.id.tvInbox);
            tvDifference = itemView.findViewById(R.id.tvDifference);
        }
    }
}
