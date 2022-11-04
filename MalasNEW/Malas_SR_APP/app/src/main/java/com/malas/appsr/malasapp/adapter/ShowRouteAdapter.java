package com.malas.appsr.malasapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.malas.appsr.malasapp.BeanClasses.RouteBean;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.activities.AddRouteActivity;

import java.util.ArrayList;


public class ShowRouteAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<RouteBean> listRoute = new ArrayList<>();

    public ShowRouteAdapter(Context mContext, ArrayList<RouteBean> routeList) {
        this.mContext = mContext;
        this.listRoute = routeList;

    }


    @Override
    public int getCount() {
        return listRoute.size();
    }

    @Override
    public Object getItem(int position) {
        return listRoute.get(position);
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
            convertView = mInflater.inflate(R.layout.show_outlet_row, null);
            holder.tvOutlet = convertView.findViewById(R.id.tv_outlet);
            holder.ivMenu = convertView.findViewById(R.id.iv_menu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvOutlet.setText(listRoute.get(position).getRoute_name());
        holder.ivMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(mContext, v);
//                public MenuItem add(int groupId, int itemId, int order, CharSequence title);
            popup.getMenu().add(0, 1, 0, "Edit");
//                popup.getMenu().add(0, 2, 1, "Delete");

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case 1:
                            Intent intent = new Intent(mContext, AddRouteActivity.class);
                            intent.putExtra("routeBean", listRoute.get(position));
                            mContext.startActivity(intent);
                            //    mContectActivityAdapterAdapter.locationFilter(mUserRegistrationInfoBean.getUsercurrentCity());

                            break;
//                            case 2:

                        //   mContectActivityAdapterAdapter.designationFilter(mUserRegistrationInfoBean.getUserDesignation());

                    }
                    return true;
                }
            });
            popup.show();


        });
        return convertView;
    }

    private class ViewHolder {
        TextView tvOutlet;
        ImageView ivMenu;
        ImageView ivProductive;
        ImageView ivNonProductive;
    }


}
