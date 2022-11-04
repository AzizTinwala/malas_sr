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
import androidx.recyclerview.widget.RecyclerView;

import com.Amitlibs.utils.ComplexPreferences;
import com.malas.appsr.malasapp.BeanClasses.TakeOrderEditedItemProductList;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderItemBean;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderListBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.activities.AddTakeOrder;
import com.malas.appsr.malasapp.activities.EditTakenOrder;
import com.malas.appsr.malasapp.customeArrayList.CArrayListTakeOrderEditedItemProductList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Created by Arwa Ainewala
 */

public class TakeOrderItemListAdapter extends RecyclerView.Adapter<TakeOrderItemListAdapter.ViewHolder> implements Filterable {
    Context mContext;
    //ArrayList<TakeOutletOrderItemBean> categoryArray;
    private final TakeOutletOrderListBean selectedCategory;
    private final TakeOutletOrderListBean selectedCategoryAll ;
    private int parentPosition = 0;
    private final String from;

    public TakeOrderItemListAdapter(Context mContext, TakeOutletOrderListBean categoryArray, int parentPosition, String from) {
        this.mContext = mContext;
        selectedCategory = categoryArray;
//        this.categoryArray = categoryArray.getArryItemList();
        this.selectedCategoryAll=new TakeOutletOrderListBean(selectedCategory);
        this.parentPosition = parentPosition;
        this.from = from;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvItemName.setText(selectedCategory.getArryItemList().get(position).getProduct_name().toUpperCase(Locale.getDefault()));
        holder.tvItemPrice.setText(selectedCategory.getArryItemList().get(position).getProduct_mrp());
        holder.edtQuantity.setOnClickListener(view -> showQuantityDialog(holder.edtQuantity, position));


        if (selectedCategory.getArryItemList().get(position).getProduct_qty() != null) {
            if (!selectedCategory.getArryItemList().get(position).getProduct_qty().equals("0")) {

                holder.edtQuantity.setText(selectedCategory.getArryItemList().get(position).getProduct_qty());
                ComplexPreferences mComplexPreferences = ComplexPreferences.getComplexPreferences(mContext, Constant.EditedOrderProductListPref, Context.MODE_PRIVATE);

                CArrayListTakeOrderEditedItemProductList mTakeOrderEditedItemProductList = mComplexPreferences.getArray(Constant.EditedOrderProductListObj, CArrayListTakeOrderEditedItemProductList.class);
                if (mTakeOrderEditedItemProductList == null)
                    mTakeOrderEditedItemProductList = new CArrayListTakeOrderEditedItemProductList();


                if (from.equals("EditTakenOrder")) {
                    mTakeOrderEditedItemProductList.add(new TakeOrderEditedItemProductList(selectedCategory.getArryItemList().get(position).getProduct_id(), holder.edtQuantity.getText().toString().trim(), selectedCategory.getArryItemList().get(position).getProduct_name(), selectedCategory.getId(), selectedCategory.getItem(), selectedCategory.getOutlet_id(), selectedCategory.getOrder_uni_id()));
                } else {
                    mTakeOrderEditedItemProductList.add(new TakeOrderEditedItemProductList(selectedCategory.getArryItemList().get(position).getProduct_id(), holder.edtQuantity.getText().toString().trim(), selectedCategory.getArryItemList().get(position).getProduct_name(), selectedCategory.getId(), selectedCategory.getItem(), selectedCategory.getOutlet_id()));
                }
                mComplexPreferences.putObject(Constant.EditedOrderProductListObj, mTakeOrderEditedItemProductList);
                mComplexPreferences.commit();


            } else {
                holder.edtQuantity.setText("");
                holder.edtQuantity.setHint("0");
            }

        }
    }

    @Override
    public int getItemCount() {

        return  selectedCategory.getArryItemList().size();
    }



    private void showQuantityDialog(final EditText etQty, final int position) {
        LayoutInflater li = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams") View promptsView = li.inflate(R.layout.quantity_dialog, null);

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
                            if (userInput.getText().toString().trim().equals("")) {
                                etQty.setHint("0");
                                etQty.setText("");
                            } else {
                                etQty.setText(userInput.getText().toString().trim());
                                selectedCategory.getArryItemList().get(position).setProduct_qty(userInput.getText().toString().trim());
                                int pos= getPosition( selectedCategory.getArryItemList().get(position));
                                selectedCategoryAll.getArryItemList().get(pos).setProduct_qty(userInput.getText().toString().trim());

                                selectedCategory.getArryItemList().get(position).setItemChanges(true);

                                if (from.equals("EditTakenOrder")) {
                                    ArrayList<TakeOutletOrderListBean> mTempList = ((EditTakenOrder) mContext).getProductList();
                                    mTempList.get(parentPosition).setArryItemList(selectedCategoryAll.getArryItemList());
                                    ((EditTakenOrder) mContext).saveProductList(mTempList);
                                } else {
                                    ArrayList<TakeOutletOrderListBean> mTempList = ((AddTakeOrder) mContext).getProductList();
                                    mTempList.get(parentPosition).setArryItemList(selectedCategoryAll.getArryItemList());
                                    ((AddTakeOrder) mContext).saveProductList(mTempList);
                                }

                                ComplexPreferences mComplexPreferences = ComplexPreferences.getComplexPreferences(mContext, Constant.EditedOrderProductListPref, Context.MODE_PRIVATE);

                                CArrayListTakeOrderEditedItemProductList mTakeOrderEditedItemProductList = mComplexPreferences.getArray(Constant.EditedOrderProductListObj, CArrayListTakeOrderEditedItemProductList.class);
                                if (mTakeOrderEditedItemProductList == null)
                                    mTakeOrderEditedItemProductList = new CArrayListTakeOrderEditedItemProductList();

                                if (from.equals("EditTakenOrder")) {
                                    mTakeOrderEditedItemProductList.add(new TakeOrderEditedItemProductList(selectedCategory.getArryItemList().get(position).getProduct_id(), userInput.getText().toString().trim(), selectedCategory.getArryItemList().get(position).getProduct_name(), selectedCategory.getId(), selectedCategory.getItem(), selectedCategory.getOutlet_id(), selectedCategory.getOrder_uni_id()));
                                } else {
                                    mTakeOrderEditedItemProductList.add(new TakeOrderEditedItemProductList(selectedCategory.getArryItemList().get(position).getProduct_id(), userInput.getText().toString().trim(), selectedCategory.getArryItemList().get(position).getProduct_name(), selectedCategory.getId(), selectedCategory.getItem(), selectedCategory.getOutlet_id()));

                                }
                                mComplexPreferences.putObject(Constant.EditedOrderProductListObj, mTakeOrderEditedItemProductList);
                                mComplexPreferences.commit();
                            }
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // show it
        alertDialog.show();
    }

    private int getPosition(TakeOutletOrderItemBean takeOutletOrderItemBean) {

        for(int i=0;i<selectedCategoryAll.getArryItemList().size();i++){
            if(selectedCategoryAll.getArryItemList().get(i).getSku_code().equals(takeOutletOrderItemBean.getSku_code())){
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
                List<TakeOutletOrderItemBean> filterList = new ArrayList<>();
                if (charSequence.toString().isEmpty()) {
                    filterList.addAll(selectedCategoryAll.getArryItemList());

                } else {
                    ArrayList<TakeOutletOrderItemBean> temp = selectedCategoryAll.getArryItemList();
                    for (TakeOutletOrderItemBean i : selectedCategoryAll.getArryItemList()) {
                        if (i.getProduct_name().toLowerCase(Locale.getDefault())
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
                selectedCategory.getArryItemList().addAll((Collection<? extends TakeOutletOrderItemBean>) filterResults.values );
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        TextView tvItemPrice;
        EditText edtQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemPrice = itemView.findViewById(R.id.tv_item_price);
            edtQuantity = itemView.findViewById(R.id.et_item_quantity);
        }
    }
}