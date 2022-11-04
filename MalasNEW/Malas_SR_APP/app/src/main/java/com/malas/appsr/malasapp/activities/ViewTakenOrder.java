package com.malas.appsr.malasapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.BeanClasses.ShowOutLetBeen;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderItemBean;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderListBean;
import com.malas.appsr.malasapp.adapter.JamOutletOrderExpandListAdapter;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ViewTakenOrder extends AppCompatActivity {

    JamOutletOrderExpandListAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<TakeOutletOrderListBean> listDataHeader;
    HashMap<TakeOutletOrderListBean, ArrayList<TakeOutletOrderItemBean>> listDataChild;
    AsyncTask<String, Void, JSONObject> mGetloginDetails;
    ArrayList<TakeOutletOrderItemBean> arryItemList;
    ArrayList<TakeOutletOrderListBean> itemList;
    TextView tvDistributorName, tvRouteName;
    ShowOutLetBeen showOutLetBeen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_take_order);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Taken Order");

        // get the listview
        expListView = findViewById(R.id.lvjam);
        tvDistributorName = findViewById(R.id.tvDistributorName);
        tvRouteName = findViewById(R.id.tvRouteName);

        if (getIntent().hasExtra("outletBean")) {
            showOutLetBeen = (ShowOutLetBeen) getIntent().getSerializableExtra("outletBean");
            tvDistributorName.setText(showOutLetBeen.getOutlet_name());
            tvRouteName.setText(showOutLetBeen.getRoute_name());
        }


        Type typeTakeStockList = new TypeToken<ArrayList<TakeOutletOrderListBean>>() {
        }.getType();

        itemList = new Gson().fromJson(getIntent().getStringExtra("list"), typeTakeStockList);
        prepareListData(itemList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareListData(itemList);

        //mGetloginDetails = new mGetPreviousPlacedOrderData().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGetloginDetails != null && !mGetloginDetails.isCancelled())
            mGetloginDetails.cancel(true);

    }

    /*
     * Preparing the list data
     */
    private void prepareListData(ArrayList<TakeOutletOrderListBean> itemList) {

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        for (int i = 0; i < itemList.size(); i++) {
            listDataHeader.add(itemList.get(i));
            listDataChild.put(itemList.get(i), itemList.get(i).getArryItemList());
        }

        listAdapter = new JamOutletOrderExpandListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }
}
