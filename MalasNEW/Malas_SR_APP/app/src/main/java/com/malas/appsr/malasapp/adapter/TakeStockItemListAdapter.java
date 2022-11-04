package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.util.Log;
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
import com.malas.appsr.malasapp.BeanClasses.TakeStockEditedItemProductList;
import com.malas.appsr.malasapp.BeanClasses.TakeStockItemBean;
import com.malas.appsr.malasapp.BeanClasses.TakeStoclListBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.activities.AddTakeStock;
import com.malas.appsr.malasapp.activities.EditTakenStock;
import com.malas.appsr.malasapp.customeArrayList.CArrayListTakeStockEditedItemProductList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TakeStockItemListAdapter extends RecyclerView.Adapter<TakeStockItemListAdapter.ViewHolder> implements Filterable {

    Context mContext;
    ArrayList<TakeStockItemBean> categoryArray, categoryArrayAll;
    TakeStoclListBean selectedCategory;
    int parentPosition = 0;
    String from;

    public TakeStockItemListAdapter(Context mContext, TakeStoclListBean categoryArray, int parentPosition, String from) {
        this.mContext = mContext;
        selectedCategory = categoryArray;
        this.categoryArray = categoryArray.getArryItemList();
        this.categoryArrayAll= new ArrayList<>(categoryArray.getArryItemList());
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
        holder.edtQuantity.setOnClickListener(view -> showQuantityDialog(holder.edtQuantity, position));
        holder.tvItemName.setText(categoryArray.get(position).getProduct_name().toUpperCase(Locale.getDefault()));
        holder.tvItemPrice.setText(categoryArray.get(position).getProduct_mrp());

        if (categoryArray.get(position).getQuantity() != null) {
            if (!categoryArray.get(position).getQuantity().equals("0")) {
                holder.edtQuantity.setText(categoryArray.get(position).getQuantity());
            } else {
                holder.edtQuantity.setText("");
                holder.edtQuantity.setHint("0");
            }
        } else {
            holder.edtQuantity.setText("");
            holder.edtQuantity.setHint("0");
        }
        Log.d(categoryArray.get(position).getProduct_id(),categoryArray.get(position).getCase_Size());

    }

    @Override
    public int getItemCount() {
        return categoryArray.size();
    }


    private void showQuantityDialog(final EditText etQty, final int position) {
        LayoutInflater li = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams") View promptsView = li.inflate(R.layout.quantity_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = promptsView.findViewById(R.id.editTextDialogUserInput);

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
                                selectedCategory.getArryItemList().get(position).setQuantity(userInput.getText().toString().trim());
                                int pos=getPosition( selectedCategory.getArryItemList().get(position));
                                categoryArrayAll.get(pos).setQuantity(userInput.getText().toString().trim());
                                 //selectedCategory.getArryItemList().get(position).setItemChanges(true);

                                if (from.equals("EditStock")) {
                                    ArrayList<TakeStoclListBean> mTempList = ((EditTakenStock) mContext).getProductList();
                                    mTempList.get(parentPosition).setArryItemList(categoryArrayAll);
                                    ((EditTakenStock) mContext).saveProductList(mTempList);
                                } else {
                                    ArrayList<TakeStoclListBean> mTempList = ((AddTakeStock) mContext).getProductList();
                                    mTempList.get(parentPosition).setArryItemList(categoryArrayAll);
                                    ((AddTakeStock) mContext).saveProductList(mTempList);
                                }

                                ComplexPreferences mComplexPreferences = ComplexPreferences.getComplexPreferences(mContext, Constant.EditedProductListPref, Context.MODE_PRIVATE);
                                CArrayListTakeStockEditedItemProductList mTakeStockEditedItemProductList = mComplexPreferences.getArray(Constant.EditedProductListObj, CArrayListTakeStockEditedItemProductList.class);

                                if (mTakeStockEditedItemProductList == null)
                                    mTakeStockEditedItemProductList = new CArrayListTakeStockEditedItemProductList();
                                TakeStockEditedItemProductList tempStockData=new TakeStockEditedItemProductList(selectedCategory.getArryItemList().get(position).getProduct_id(), userInput.getText().toString().trim(), selectedCategory.getArryItemList().get(position).getCase_Size(),selectedCategory.getArryItemList().get(position).getProduct_name());
                                mTakeStockEditedItemProductList.add(tempStockData);

                                mComplexPreferences.putObject(Constant.EditedProductListObj, mTakeStockEditedItemProductList);
                                mComplexPreferences.commit();
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

    private int getPosition(TakeStockItemBean takeStockItemBean) {

        for(int i=0;i<categoryArrayAll.size();i++){
            if(categoryArrayAll.get(i).getSku_code().equals(takeStockItemBean.getSku_code())){
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
                List<TakeStockItemBean> filterList = new ArrayList();
                if (charSequence.toString().isEmpty()) {
                    filterList.addAll(categoryArrayAll);

                } else {
                    ArrayList temp = categoryArrayAll;
                    for (TakeStockItemBean i : categoryArrayAll) {
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
                categoryArray.clear();
                categoryArray.addAll((Collection<? extends TakeStockItemBean>) filterResults.values );
                notifyDataSetChanged();
            }
        };
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
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