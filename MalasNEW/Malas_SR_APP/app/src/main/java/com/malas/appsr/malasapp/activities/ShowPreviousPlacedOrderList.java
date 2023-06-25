package com.malas.appsr.malasapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.BeanClasses.PreviousPlaceorderArraylistBean;
import com.malas.appsr.malasapp.BeanClasses.TakePreviousPlaceOrderItemList;
import com.malas.appsr.malasapp.adapter.ExpandPreviousPlaceOrderAdapter;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Admin on 01-Feb-18.
 */

public class ShowPreviousPlacedOrderList extends AppCompatActivity {

    ExpandPreviousPlaceOrderAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<PreviousPlaceorderArraylistBean> listDataHeader;
    HashMap<PreviousPlaceorderArraylistBean, ArrayList<TakePreviousPlaceOrderItemList>> listDataChild;
    AsyncTask<String, Void, JSONObject> mGetloginDetails;
    ArrayList<TakePreviousPlaceOrderItemList> arryItemList;
    ArrayList<PreviousPlaceorderArraylistBean> itemList;
    TextView spnr_distributr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_stock_second);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Placed Order");

        // get the listview
        expListView = findViewById(R.id.lvjam);
        spnr_distributr = findViewById(R.id.spnr_distributr);

        spnr_distributr.setText(getIntent().getStringExtra("firmName"));

        Type previousPlaceorderArraylistBean = new TypeToken<ArrayList<PreviousPlaceorderArraylistBean>>() {
        }.getType();

        itemList = new Gson().fromJson(getIntent().getStringExtra("list"), previousPlaceorderArraylistBean);
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
    private void prepareListData(ArrayList<PreviousPlaceorderArraylistBean> itemList) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        for (int i = 0; i < itemList.size(); i++) {
            listDataHeader.add(itemList.get(i));
            listDataChild.put(itemList.get(i), itemList.get(i).getArryItemList());
        }
        listAdapter = new ExpandPreviousPlaceOrderAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }


}
