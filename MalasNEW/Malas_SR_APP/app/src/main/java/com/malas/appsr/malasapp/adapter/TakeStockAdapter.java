package com.malas.appsr.malasapp.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.Amitlibs.net.HttpUrlConnectionJSONParser;
import com.Amitlibs.utils.ComplexPreferences;
import com.malas.appsr.malasapp.BeanClasses.DistributerBean;
import com.malas.appsr.malasapp.BeanClasses.TakeStockUserSaved;
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean;
import com.malas.appsr.malasapp.Constant;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.activities.EditTakenStock;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class TakeStockAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<TakeStockUserSaved> takeStockList;

    private final String userId;
    private final DistributerBean selectedDistributerBean;

    public TakeStockAdapter(Context mContext, ArrayList<TakeStockUserSaved> takeStockList, DistributerBean selectedDistributerBean) {
        this.mContext = mContext;
        this.takeStockList = takeStockList;
        this.selectedDistributerBean = selectedDistributerBean;
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(mContext, Constant.UserRegInfoPref, Context.MODE_PRIVATE);
        UserLoginInfoBean mUserLoginInfoBean = complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean.class);
        userId = mUserLoginInfoBean.getUserId();

    }

    @Override
    public int getCount() {

        return takeStockList.size();


    }

    @Override
    public Object getItem(int position) {
        return takeStockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.take_stock_row, null);
            holder.tvOutlet = convertView.findViewById(R.id.tv_outlet);
            holder.tvdate = convertView.findViewById(R.id.tv_date);
            holder.ivMenu = convertView.findViewById(R.id.iv_menu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvOutlet.setText("Stock On ");

        int unitime1 = Integer.parseInt(takeStockList.get(position).getStock_time());
        Date date1 = new Date(unitime1 * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy  hh:mm a", Locale.getDefault()); // the format of your date
        sdf.setTimeZone(TimeZone.getDefault()); // give a timezone reference for formating (see comment at the bottom
        String formattedTime = sdf.format(date1);
        holder.tvdate.setText(formattedTime);


        holder.ivMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(mContext, v);
//                public MenuItem add(int groupId, int itemId, int order, CharSequence title);
            popup.getMenu().add(0, 1, 0, "Edit");
//                popup.getMenu().add(0, 2, 1, "Delete");

            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case 1:
                        if (takeStockList.get(position).getIs_stock_placed().equals("0")) {

                            if (takeStockList != null) {
                                if (takeStockList.size() > 0) {
                                    if (checkTwoDates(takeStockList.get(0).getStock_time())) {

                                        mContext.startActivity(new Intent(mContext, EditTakenStock.class).putExtra("firmName", selectedDistributerBean.getFirm_name()).putExtra("distribbuterId", selectedDistributerBean.getDistribution_id()).putExtra("TakeStockUserSaved", takeStockList.get(position)).putExtra("distributornumber", selectedDistributerBean.getMobile_no()));
                                    }else {
                                        Toast.makeText(mContext, "Take new Stock for today.", Toast.LENGTH_LONG).show();

                                    }
                                }else{
                                    mContext.startActivity(new Intent(mContext, EditTakenStock.class).putExtra("firmName", selectedDistributerBean.getFirm_name()).putExtra("distribbuterId", selectedDistributerBean.getDistribution_id()).putExtra("TakeStockUserSaved", takeStockList.get(position)).putExtra("distributornumber", selectedDistributerBean.getMobile_no()));

                                }
                            }else{
                                mContext.startActivity(new Intent(mContext, EditTakenStock.class).putExtra("firmName", selectedDistributerBean.getFirm_name()).putExtra("distribbuterId", selectedDistributerBean.getDistribution_id()).putExtra("TakeStockUserSaved", takeStockList.get(position)).putExtra("distributornumber", selectedDistributerBean.getMobile_no()));

                            }
                        } else {
                            Toast.makeText(mContext, "You can not edit this stock", Toast.LENGTH_SHORT).show();
                        }

                        break;
//                            case 2:
//                                String stockId = takeStockList.get(position).getStock_uni_id();
//                                new mDeleteStockList().execute("delete_stock", stockId, userId, "" + position);
//                                break;
                }
                return true;
            });
            popup.show();


        });


        return convertView;
    }

    public boolean checkTwoDates(String datelong) {

        long dateStr = Long.parseLong(datelong);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(dateStr * 1000);
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        return date.equalsIgnoreCase(formattedDate);


    }

    private class ViewHolder {
        TextView tvOutlet, tvdate;
        ImageView ivMenu;
    }

    public class mDeleteStockList extends AsyncTask<String, Void, JSONObject> {
        ProgressDialog mProgressDialog;
        int position;

        @Override
        protected void onPreExecute() {
            if (true/*itemList == null*/) {
                mProgressDialog = new ProgressDialog(mContext);
                mProgressDialog.setMessage("Deleting Stock please wait......");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("method", params[0]));
            nameValuePair.add(new BasicNameValuePair("stock_id", params[1]));
            nameValuePair.add(new BasicNameValuePair("user_id", params[2]));
            position = Integer.parseInt(params[3]);
            return new HttpUrlConnectionJSONParser().getJsonObjectFromHttpUrlConnection(Constant.BASE_URL, nameValuePair, HttpUrlConnectionJSONParser.Http.POST);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        takeStockList.remove(position);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mContext, "Improper response from server", Toast.LENGTH_SHORT).show();
            }
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
        }
    }


}