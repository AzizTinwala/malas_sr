package com.malas.appsr.malasapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.BeanClasses.TakeStockItemBean;
import com.malas.appsr.malasapp.BeanClasses.TakeStoclListBean;
import com.malas.appsr.malasapp.adapter.JamExpandListAdapter;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ShowTakedStock extends AppCompatActivity {

    JamExpandListAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<TakeStoclListBean> listDataHeader;
    HashMap<TakeStoclListBean, ArrayList<TakeStockItemBean>> listDataChild;
    AsyncTask<String, Void, JSONObject> mGetloginDetails;
    ArrayList<TakeStockItemBean> arryItemList;
    ArrayList<TakeStoclListBean> itemList;
    TextView spnr_distributr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_stock_second);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Take Stock");

        // get the listview
        expListView = findViewById(R.id.lvjam);
        spnr_distributr = findViewById(R.id.spnr_distributr);

        spnr_distributr.setText(getIntent().getStringExtra("firmName"));

        Type typeTakeStockList = new TypeToken<ArrayList<TakeStoclListBean>>() {
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
    private void prepareListData(ArrayList<TakeStoclListBean> itemList) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        for (int i = 0; i < itemList.size(); i++) {
            listDataHeader.add(itemList.get(i));
            listDataChild.put(itemList.get(i), itemList.get(i).getArryItemList());
        }
        listAdapter = new JamExpandListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

}