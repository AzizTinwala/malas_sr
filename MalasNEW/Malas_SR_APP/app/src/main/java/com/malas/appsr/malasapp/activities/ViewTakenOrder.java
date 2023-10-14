package com.malas.appsr.malasapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.malas.appsr.malasapp.BeanClasses.ShowOutLetBeen;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderItemBean;
import com.malas.appsr.malasapp.BeanClasses.TakeOutletOrderListBean;
import com.malas.appsr.malasapp.BeanClasses.TakenOrderBean;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.JamOutletOrderExpandListAdapter;
import com.malas.appsr.malasapp.adapter.TakenOrderAdapter;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    RecyclerView rView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_take_order);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Taken Order");

        // get the listview
        expListView = findViewById(R.id.lvjam);
        tvDistributorName = findViewById(R.id.tvDistributorName);
        tvRouteName = findViewById(R.id.tvRouteName);
        rView = findViewById(R.id.product_view);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rView.addItemDecoration(dividerItemDecoration);

        rView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        if (getIntent().hasExtra("outletBean")) {
            showOutLetBeen = (ShowOutLetBeen) getIntent().getSerializableExtra("outletBean");
            tvDistributorName.setText(showOutLetBeen.getOutlet_name());
            tvRouteName.setText(showOutLetBeen.getRoute_name());
        }


        Type typeTakeStockList = new TypeToken<ArrayList<TakeOutletOrderListBean>>() {
        }.getType();

        itemList = new Gson().fromJson(getIntent().getStringExtra("list"), typeTakeStockList);
        //    prepareListData(itemList);
        prepareListDataTable(itemList);
        Log.e("TAG", "onCreate: " + itemList.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  prepareListData(itemList);
        prepareListDataTable(itemList);

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

    private void prepareListDataTable(ArrayList<TakeOutletOrderListBean> itemList) {
        List<TakenOrderBean> newList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            for (int j = 0; j < itemList.get(i).getArryItemList().size(); j++) {
                newList.add(new TakenOrderBean(itemList.get(i).getArryItemList().get(j).getProduct_id(),
                        itemList.get(i).getArryItemList().get(j).getProduct_name(),
                        itemList.get(i).getId(),
                        itemList.get(i).getItem(),
                        itemList.get(i).getArryItemList().get(j).getProduct_qty()));
            }
        }
        Log.e("TAG", "prepareListDataTable: " + newList.toString());
        rView.setAdapter(new TakenOrderAdapter(newList));
    }
}

