package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.OutletOrdersBean;
import com.malas.appsr.malasapp.BeanClasses.SaveData;
import com.malas.appsr.malasapp.BeanClasses.ShowOutLetBeen;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderItemBean;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.activities.EditTakenOrder;
import com.malas.appsr.malasapp.dbHandler.DatabaseHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class ShowOutletOrderAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<OutletOrdersBean> outletList;
    private final ShowOutLetBeen showOutLetBeen;
    private final DistributerBean distributerBean;
    private final DatabaseHandler db;

    public ShowOutletOrderAdapter(Context mContext, ArrayList<OutletOrdersBean> outletList, ShowOutLetBeen showOutLetBeen, DistributerBean distributerBean) {
        this.mContext = mContext;
        this.outletList = outletList;
        this.showOutLetBeen = showOutLetBeen;
        this.distributerBean = distributerBean;
        db = new DatabaseHandler(mContext);
    }


    @Override
    public int getCount() {
        return outletList.size();
    }

    @Override
    public Object getItem(int position) {
        return outletList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.show_outlet_order_row, null);
            holder.tvOutlet = convertView.findViewById(R.id.tv_outlet);
            holder.tv_datetime = convertView.findViewById(R.id.tv_datetime);
            holder.ivMenu = convertView.findViewById(R.id.iv_menu);
            holder.llOutletRow = convertView.findViewById(R.id.llOutletRow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvOutlet.setText("Order On");
        holder.tv_datetime.setText(outletList.get(position).getOrder_take_time());

        holder.ivMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(mContext, v);
            popup.getMenu().add(0, 1, 0, "Edit");

            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case 1:

                        ArrayList<TakeOutletOrderItemBean> productList;
                        ArrayList<SaveData> saveListFromDb;
                        ArrayList<TakeOutletOrderItemBean> productListEdit;
                        ArrayList<SaveData> saveListFromDbEdit;

                        saveListFromDb = db.getAllSaveDataRecord(distributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());
                        productList = db.getSaveProductRecord(distributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());


                        saveListFromDbEdit = db.getEditSaveDataRecord(distributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());
                        productListEdit = db.getEditSaveProductRecord(distributerBean.getDistribution_id(), showOutLetBeen.getOutlet_id());

                        if (saveListFromDb != null && productList != null && saveListFromDb.size() > 0 && productList.size() > 0) {
                            Toast.makeText(mContext, "Please Connect to Internet And SYNC Order data ", Toast.LENGTH_SHORT).show();

                        } else if (saveListFromDbEdit != null && productListEdit != null && saveListFromDbEdit.size() > 0 && productListEdit.size() > 0) {
                            Toast.makeText(mContext, "Please Connect to Internet And SYNC Order data ", Toast.LENGTH_SHORT).show();
                        } else {
                                if (outletList != null) {
                                    if (outletList.size() > 0) {

                                        if (differenceInTwoDates(outletList.get(position).getOrder_take_time())) {
                                            Intent intent = new Intent(mContext, EditTakenOrder.class);
                                            intent.putExtra("showOutLetBeen", showOutLetBeen);
                                            intent.putExtra("outletOrdersBean", outletList.get(position));
                                            intent.putExtra("distributerBean", distributerBean);
                                            mContext.startActivity(intent);
                                        } else {
                                            Toast.makeText(mContext, " Take new order for this outlet .", Toast.LENGTH_LONG).show();

                                        }
                                    } else {
                                        Intent intent = new Intent(mContext, EditTakenOrder.class);
                                        intent.putExtra("showOutLetBeen", showOutLetBeen);
                                        intent.putExtra("outletOrdersBean", outletList.get(position));
                                        intent.putExtra("distributerBean", distributerBean);
                                        mContext.startActivity(intent);
                                    }

                                } else {
                                    Intent intent = new Intent(mContext, EditTakenOrder.class);
                                    intent.putExtra("showOutLetBeen", showOutLetBeen);
                                    intent.putExtra("outletOrdersBean", outletList.get(position));
                                    intent.putExtra("distributerBean", distributerBean);
                                    mContext.startActivity(intent);

                                }


                        }
                        break;
                }

                return true;
            });


            popup.show();


        });

        return convertView;
    }

    private boolean getCurrentAndClickDate(String dateClicked) {
        String[] dateClickedStringArray = dateClicked.split(" ");
        String date = dateClickedStringArray[0];
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date clickedDate = null;
        try {
            clickedDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String strDate = mdformat.format(calendar.getTime());
        Date dateCurrent = null;

        try {
            dateCurrent = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff, days = 0;
        if (dateCurrent != null && clickedDate != null) {
            diff = dateCurrent.getTime() - clickedDate.getTime();
            days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        }
        return days <= 4;
    }

    private boolean checkTwoDates(String date) {

        SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault());
        Date newDate = null;
        try {
            newDate = spf.parse(date);
            spf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            date = spf.format(newDate);


            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = df.format(c);

            return date.equalsIgnoreCase(formattedDate);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return false;

    }

    private class ViewHolder {
        TextView tvOutlet, tv_datetime;
        ImageView ivMenu;
        LinearLayout llOutletRow;
    }
    private boolean differenceInTwoDates(String date) {
        Date c = new Date();


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault());
        Date date1 = null;

        try {
            date1 = sdf.parse(date);
            long different = c.getTime() - date1.getTime();
            if (TimeUnit.DAYS.convert(different, TimeUnit.MILLISECONDS) > 1) {
                return false;
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        return true;
    }
}
