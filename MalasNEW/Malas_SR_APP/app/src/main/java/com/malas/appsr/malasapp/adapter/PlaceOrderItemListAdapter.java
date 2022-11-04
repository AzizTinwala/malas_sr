package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.malas.appsr.malasapp.BeanClasses.PlaceOrderItemBean;
import com.malas.appsr.malasapp.BeanClasses.PlaceOrderListBean;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.activities.ShowPlaceOrderNew;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PlaceOrderItemListAdapter extends RecyclerView.Adapter<PlaceOrderItemListAdapter.ViewHolder> implements Filterable {
    Context mContext;
    private final PlaceOrderListBean selectedCategory;
    private final PlaceOrderListBean selectedCategoryAll;
    private final int parentPosition;

    public PlaceOrderItemListAdapter(Context mContext, PlaceOrderListBean categoryArray, int parentPosition) {

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
        holder.tvProductName.setText(selectedCategory.getArryItemList().get(position).getName().toUpperCase(Locale.getDefault()));

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

        final ViewHolder finalHolder = holder;
        holder.tvInbox.setOnClickListener(view -> showQuantityDialog(finalHolder.tvInbox, position));

        holder.tvDifference.setText("" + Math.abs(Integer.parseInt(selectedCategory.getArryItemList().get(position).getDifference())));

    }


    @Override
    public int getItemCount() {
        return selectedCategory.getArryItemList().size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvDifference, tvInbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvInbox = itemView.findViewById(R.id.tvInbox);
            tvDifference = itemView.findViewById(R.id.tvDifference);

        }
    }


//    public void showQuantityDialog(final TextView etQty, final int position) {
//        LayoutInflater li = LayoutInflater.from(mContext);
//        View promptsView = li.inflate(R.layout.difference_dialog, null);
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
//
//        alertDialogBuilder.setView(promptsView);
//
//        final EditText userInput = (EditText) promptsView
//                .findViewById(R.id.editTextDialogUserInput);
//
//        if (!etQty.getText().toString().trim().equals("")) {
//            userInput.setText(etQty.getText().toString().trim());
//            userInput.setSelection(etQty.getText().toString().trim().length());
//        }
//
//        // set dialog message
//        alertDialogBuilder
//                .setCancelable(false)
//                .setPositiveButton("Submit",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                if (Integer.parseInt(userInput.getText().toString().trim()) >= Math.abs(Integer.parseInt(selectedCategory.getArryItemList().get(position).getInboxSize()))) {
//                                    selectedCategory.getArryItemList().get(position).setInboxSize(userInput.getText().toString().trim());
//                                    ComplexPreferences mPlaceOrderListPref = ComplexPreferences.getComplexPreferences(mContext, Constant.PlaceOrderProductListPref, MODE_PRIVATE);
//                                    mPlaceOrderListPref.putObject(Constant.PlaceOrderProductListObj, showPlaceOrderList);
//                                    mPlaceOrderListPref.commit();
//                                    etQty.setText(userInput.getText().toString().trim());
//                                } else
//                                    Toast.makeText(mContext, "You can not decrease box size", Toast.LENGTH_SHORT).show();
//
//                                dialog.dismiss();
//                            }
//                        })
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.dismiss();
//                            }
//                        });
//
//        // create alert dialog
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        // show it
//        alertDialog.show();
//    }

    //    private void showQuantityDialog(final TextView etQty, final int position) {
//        LayoutInflater li = LayoutInflater.from(mContext);
//        @SuppressLint("InflateParams") View promptsView = li.inflate(R.layout.difference_dialog, null);
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                mContext);
//
//        alertDialogBuilder.setView(promptsView);
//
//        final EditText userInput = promptsView
//                .findViewById(R.id.editTextDialogUserInput);
//
//        if (!etQty.getText().toString().trim().equals("")) {
//            userInput.setText(etQty.getText().toString().trim());
//        }
//
//        // set dialog message
//        alertDialogBuilder
//                .setCancelable(false)
//                .setPositiveButton("Submit",
//                        (dialog, id) -> {
//                            if (userInput.getText().toString().trim().equals("")) {
//                                etQty.setHint("0");
//                                etQty.setText("");
//                            } else {
//                                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(mContext, Constant.PlaceOrderProductListTempPref, MODE_PRIVATE);
//                                Type typePlaceOrder = new TypeToken<ArrayList<PlaceOrderListBean>>() {
//                                }.getType();
//                                ArrayList<PlaceOrderListBean> itemList = complexPreferences.getArray(Constant.PlaceOrderProductList, typePlaceOrder) == null ? new ArrayList<>() : (ArrayList<PlaceOrderListBean>) complexPreferences.getArray(Constant.PlaceOrderProductList, typePlaceOrder);
//
//                                if (itemList != null && itemList.size() > 0) {
//                                    if (Math.abs(Integer.parseInt(itemList.get(parentPosition).getArryItemList().get(position).getInboxSize())) != Math.abs(Integer.parseInt(selectedCategory.getArryItemList().get(position).getInboxSize()))) {
//                                        //if (Integer.parseInt(userInput.getText().toString().trim()) >= Math.abs(Integer.parseInt(itemList.get(parentPosition).getArryItemList().get(position).getInboxSize()))) {itemList.get(parentPosition).getArryItemList().get(position).getInboxSize();
//                                        if (Integer.parseInt(userInput.getText().toString().trim()) >= Math.abs(Integer.parseInt(selectedCategory.getArryItemList().get(position).getInboxSize()))) {
//                                            etQty.setText(userInput.getText().toString().trim());
//                                            selectedCategory.getArryItemList().get(position).setInboxSize(userInput.getText().toString().trim());
//                                            ArrayList<PlaceOrderListBean> mTempList = ((ShowPlaceOrderNew) mContext).getProductList();
//                                            mTempList.get(parentPosition).setArryItemList(selectedCategory.getArryItemList());
//                                            ((ShowPlaceOrderNew) mContext).saveProductList(mTempList);
//                                        } else {
//                                            Toast.makeText(mContext, "You can not decrease box size", Toast.LENGTH_SHORT).show();
//                                        }
//                                    } else {
//                                        if (Integer.parseInt(userInput.getText().toString().trim()) >= Math.abs(Integer.parseInt(selectedCategory.getArryItemList().get(position).getInboxSize()))) {
//                                            etQty.setText(userInput.getText().toString().trim());
//                                            selectedCategory.getArryItemList().get(position).setInboxSize(userInput.getText().toString().trim());
//                                            ArrayList<PlaceOrderListBean> mTempList = ((ShowPlaceOrderNew) mContext).getProductList();
//                                            mTempList.get(parentPosition).setArryItemList(selectedCategory.getArryItemList());
//                                            ((ShowPlaceOrderNew) mContext).saveProductList(mTempList);
//                                        } else {
//                                            Toast.makeText(mContext, "You can not decrease box size", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//
//
//                            }
//                        })
//                .setNegativeButton("Cancel",
//                        (dialog, id) -> dialog.cancel());
//
//        // create alert dialog
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        Objects.requireNonNull(alertDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        // show it
//        alertDialog.show();
//    }
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
                                etQty.setText(userInput.getText().toString().trim());
                                selectedCategory.getArryItemList().get(position).setInboxSize(userInput.getText().toString().trim());

                                int pos=getPosition( selectedCategory.getArryItemList().get(position));
                                selectedCategoryAll.getArryItemList().get(pos).setInboxSize(userInput.getText().toString().trim());


                                ArrayList<PlaceOrderListBean> mTempList = ((ShowPlaceOrderNew) mContext).getProductList();
                                mTempList.get(parentPosition).setArryItemList(selectedCategoryAll.getArryItemList());
                                ((ShowPlaceOrderNew) mContext).saveProductList(mTempList);
/*                                if (
                                        (Math.abs(Integer.parseInt(selectedCategory.getArryItemList().get(position).getDifference()))
                                                <= (Integer.parseInt(selectedCategory.getArryItemList().get(position).getPacketSize())
                                                * Integer.parseInt(userInput.getText().toString().trim())))
                                                ||
                                                (Integer.parseInt(selectedCategory.getArryItemList().get(position).getDifference())
                                                        > (Integer.parseInt(selectedCategory.getArryItemList().get(position).getPacketSize())
                                                        * Integer.parseInt(userInput.getText().toString().trim())) && Integer.parseInt(selectedCategory.getArryItemList().get(position).getDifference()) > 0)
                                ) {
                                    etQty.setText(userInput.getText().toString().trim());
                                    selectedCategory.getArryItemList().get(position).setInboxSize(userInput.getText().toString().trim());
                                    ArrayList<PlaceOrderListBean> mTempList = ((ShowPlaceOrderNew) mContext).getProductList();
                                    mTempList.get(parentPosition).setArryItemList(selectedCategory.getArryItemList());
                                    ((ShowPlaceOrderNew) mContext).saveProductList(mTempList);
                                } else {
                                    Utils.showAlertDialog(mContext, "Insufficient Stock", "You can not decrease the quantity as Distributor does not have enough stock to supply pending orders.", true);
                                    //Toast.makeText(mContext, "You can not decrease box size", Toast.LENGTH_SHORT).show();
                                }*/
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
    private int getPosition(PlaceOrderItemBean placeOrderItemBean) {

        for(int i=0;i<selectedCategoryAll.getArryItemList().size();i++){
            if(selectedCategoryAll.getArryItemList().get(i).getSkuCode().equals(placeOrderItemBean.getSkuCode())){
                return i;
            }
        }
        return -1;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<PlaceOrderItemBean> filterList = new ArrayList<>();
                if (charSequence.toString().isEmpty()) {
                    filterList.addAll(selectedCategoryAll.getArryItemList());

                } else {
                    ArrayList temp = selectedCategoryAll.getArryItemList();
                    for (PlaceOrderItemBean i : selectedCategoryAll.getArryItemList()) {
                        if (i.getName().toLowerCase(Locale.getDefault())
                                .contains(
                                        charSequence.toString().toLowerCase(Locale.getDefault())
                                )
                        ) {
                            filterList.add(i);
                        }

                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                selectedCategory.getArryItemList().clear();
                selectedCategory.getArryItemList().addAll((Collection<? extends PlaceOrderItemBean>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }
}