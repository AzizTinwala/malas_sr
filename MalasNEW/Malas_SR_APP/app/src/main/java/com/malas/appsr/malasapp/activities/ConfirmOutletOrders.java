package com.malas.appsr.malasapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.malas.appsr.malasapp.BeanClasses.TakeStockEditedItemProductList;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.ConfirmOutletOrderAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConfirmOutletOrders extends AppCompatActivity {
    JSONObject itemList;
    RecyclerView confirm_view;
    List<TakeStockEditedItemProductList> data = new ArrayList<>();
    int  totalQty;
    TextView  totalQtyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_outlet_orders);

        getSupportActionBar().hide();

        confirm_view = findViewById(R.id.outlet_order_confirm_itemview);
        totalQtyView = findViewById(R.id.outlet_order_confirm_total_qty);
        Intent intent = getIntent();
        if (intent != null) {
            try {

                itemList = new JSONObject(getIntent().getStringExtra("ItemList"));
                JSONArray mJsonarry = itemList.getJSONArray("item_list");
                Log.d("Confirm_Json", itemList.toString());
                totalQty = 0;
                for (int i = 0; i < mJsonarry.length(); i++) {

                    TakeStockEditedItemProductList temp = new TakeStockEditedItemProductList();

                    temp.setItem_id(mJsonarry.getJSONObject(i).getString("item_id"));
                    temp.setItem_name(mJsonarry.getJSONObject(i).getString("item_name"));
                    temp.setItem_qty(mJsonarry.getJSONObject(i).getString("item_qty"));
                    temp.setItem_case_size("0");
                    data.add(temp);
                    totalQty = totalQty + mJsonarry.getJSONObject(i).getInt("item_qty");


                }
                totalQtyView.setText(String.valueOf(totalQty));

                confirm_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

                confirm_view.addItemDecoration(dividerItemDecoration);

                SnapHelper snapHelper = new LinearSnapHelper();
                snapHelper.attachToRecyclerView(confirm_view);


                confirm_view.setAdapter(new ConfirmOutletOrderAdapter(data));

                Log.d("Confirm_Json", itemList.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        findViewById(R.id.btn_outlet_order_confirm_cancel).setOnClickListener(v -> finish());
        findViewById(R.id.btn_outlet_order_confirm_confirm).setOnClickListener(v -> {
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }
}