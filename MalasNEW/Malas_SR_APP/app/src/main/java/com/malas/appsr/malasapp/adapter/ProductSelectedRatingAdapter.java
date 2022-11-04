package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;


import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.ProductRatingArrayBean;
import com.malas.appsr.malasapp.BeanClasses.ProductRatingBean;

import com.malas.appsr.malasapp.R;


import java.util.ArrayList;


/**
 * Created by Arwa on 26-Sep-18.
 */

public class ProductSelectedRatingAdapter extends BaseAdapter {
    Context context;

    private final ArrayList<ProductRatingBean> productRatingList;
    private String qty;
    private String taste;
    private String packaging;
    public ArrayList<ProductRatingArrayBean> getProductRatingList;


    public ProductSelectedRatingAdapter(Context context, ArrayList<ProductRatingBean> productRatingList) {
        this.context = context;
        this.productRatingList = productRatingList;

        getProductRatingList = new ArrayList<>();
        getProductRatingList.clear();


    }


    @Override
    public int getCount() {
        return productRatingList.size();
    }


    @Override
    public Object getItem(int position) {
        return productRatingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.activity_rating_act, null);
            holder.tvProductName = convertView.findViewById(R.id.selected_product_name);
            holder.qty = convertView.findViewById(R.id.spnr_qty);
            holder.taste = convertView.findViewById(R.id.spnr_taste);
            holder.packaging = convertView.findViewById(R.id.spnr_packaging);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvProductName.setText(productRatingList.get(position).getProductName());


        if (getProductRatingList != null && getProductRatingList.size() > 0) {
            ArrayList<Integer> posList = new ArrayList<>();
            for (int i = 0; i < getProductRatingList.size(); i++) {
                posList.add(getProductRatingList.get(i).getPosition());

            }
            if (!posList.contains(position)) {

                ProductRatingArrayBean productRatingArrayBean = new ProductRatingArrayBean();
                productRatingArrayBean.setName(productRatingList.get(position).getProductName());
                productRatingArrayBean.setPosition(position);
                productRatingArrayBean.setSku(productRatingList.get(position).getSku());
                getProductRatingList.add(productRatingArrayBean);


            }


        } else {
            ProductRatingArrayBean productRatingArrayBean = new ProductRatingArrayBean();
            productRatingArrayBean.setName(productRatingList.get(position).getProductName());
            productRatingArrayBean.setPosition(position);
            productRatingArrayBean.setSku(productRatingList.get(position).getSku());
            getProductRatingList.add(productRatingArrayBean);
        }

        holder.qty.setOnClickListener(v -> {
            final int pos = position;
            if (productRatingList.get(position).getQty() != null) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_rating_number);

                final ListView productListView = dialog.findViewById(R.id.dialo_listview);
                dialog.show();

                RatingAdapter ratingAdapter = new RatingAdapter(context, productRatingList.get(position).getQty());
                productListView.setAdapter(ratingAdapter);

                productListView.setOnItemClickListener((parent1, view, position1, id) -> {
                    qty = RatingAdapter.ratingOfAll.get(position1);
                    holder.qty.setText(qty);

                    dialog.dismiss();
                    try {
                        for (int i = 0; i < getProductRatingList.size(); i++) {
                            if (getProductRatingList.get(i).getPosition() == pos) {
                                ProductRatingArrayBean productRatingArrayBean = new ProductRatingArrayBean();

                                productRatingArrayBean.setTaste(getProductRatingList.get(i).getTaste());
                                productRatingArrayBean.setName(getProductRatingList.get(i).getName());
                                productRatingArrayBean.setSku(getProductRatingList.get(i).getSku());
                                productRatingArrayBean.setPosition(getProductRatingList.get(i).getPosition());
                                productRatingArrayBean.setQty(qty);
                                productRatingArrayBean.setPackaging(getProductRatingList.get(i).getPackaging());
                                getProductRatingList.remove(i);
                                getProductRatingList.add(i, productRatingArrayBean);
                                break;
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        });
        holder.taste.setOnClickListener(v -> {
            final int pos = position;
            if (productRatingList.get(position).getTaste() != null) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_rating_number);

                final ListView productListView = dialog.findViewById(R.id.dialo_listview);
                dialog.show();

                RatingAdapter ratingAdapter = new RatingAdapter(context, productRatingList.get(position).getTaste());
                productListView.setAdapter(ratingAdapter);

                productListView.setOnItemClickListener((parent12, view, position12, id) -> {
                   taste= RatingAdapter.ratingOfAll.get(position12);
                    holder.taste.setText(taste);


                    dialog.dismiss();
                    try {
                        for (int i = 0; i < getProductRatingList.size(); i++) {
                            if (getProductRatingList.get(i).getPosition() == pos) {
                                ProductRatingArrayBean productRatingArrayBean = new ProductRatingArrayBean();
                                productRatingArrayBean.setTaste(taste);
                                productRatingArrayBean.setName(getProductRatingList.get(i).getName());
                                productRatingArrayBean.setSku(getProductRatingList.get(i).getSku());
                                productRatingArrayBean.setPosition(getProductRatingList.get(i).getPosition());
                                productRatingArrayBean.setQty(getProductRatingList.get(i).getQty());
                                productRatingArrayBean.setPackaging(getProductRatingList.get(i).getPackaging());
                                getProductRatingList.remove(i);
                                getProductRatingList.add(i, productRatingArrayBean);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        });
        holder.packaging.setOnClickListener(v -> {
            final int pos = position;
            if (productRatingList.get(position).getPackaging() != null) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_rating_number);

                final ListView productListView = dialog.findViewById(R.id.dialo_listview);
                dialog.show();

                RatingAdapter ratingAdapter = new RatingAdapter(context, productRatingList.get(position).getPackaging());
                productListView.setAdapter(ratingAdapter);

                productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent13, View view, int position13, long id) {
                        packaging= RatingAdapter.ratingOfAll.get(position13);
                        holder.packaging.setText(packaging);

                        dialog.dismiss();
                        try {
                            for (int i = 0; i < getProductRatingList.size(); i++) {
                                if (getProductRatingList.get(i).getPosition() == pos) {
                                    ProductRatingArrayBean productRatingArrayBean = new ProductRatingArrayBean();
                                    productRatingArrayBean.setTaste(getProductRatingList.get(i).getTaste());
                                    productRatingArrayBean.setName(getProductRatingList.get(i).getName());
                                    productRatingArrayBean.setSku(getProductRatingList.get(i).getSku());
                                    productRatingArrayBean.setPosition(getProductRatingList.get(i).getPosition());
                                    productRatingArrayBean.setQty(getProductRatingList.get(i).getQty());
                                    productRatingArrayBean.setPackaging(packaging);
                                    getProductRatingList.remove(i);
                                    getProductRatingList.add(i, productRatingArrayBean);
                                    break;
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
        return convertView;
    }


    private class ViewHolder {
        TextView tvProductName;
        EditText qty;
        EditText taste;
        EditText packaging;

    }


}
