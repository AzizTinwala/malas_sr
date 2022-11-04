package com.malas.appsr.malasapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.malas.appsr.malasapp.BeanClasses.TakeStockEditedItemProductList;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.ConfirmStockAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConfirmStocks extends AppCompatActivity {
    JSONObject itemList;
    RecyclerView confirm_view;
    List<TakeStockEditedItemProductList> data = new ArrayList<>();
    int totalBoxes, totalQty;
    TextView totalBoxesView, totalQtyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_stocks);

        getSupportActionBar().hide();

        confirm_view = findViewById(R.id.stock_confirm_itemview);
        totalBoxesView = findViewById(R.id.stock_confirm_total_boxes);
        totalQtyView = findViewById(R.id.stock_confirm_total_qty);
        Intent intent = getIntent();
        if (intent != null) {
            try {

                itemList = new JSONObject(getIntent().getStringExtra("ItemList"));
                JSONArray mJsonarry = itemList.getJSONArray("item_list");
                totalBoxes = 0;
                totalQty = 0;
                for (int i = 0; i < mJsonarry.length(); i++) {

                    TakeStockEditedItemProductList temp = new TakeStockEditedItemProductList();

                    temp.setItem_id(mJsonarry.getJSONObject(i).getString("item_id"));
                    temp.setItem_name(mJsonarry.getJSONObject(i).getString("item_name"));
                    temp.setItem_qty(mJsonarry.getJSONObject(i).getString("item_qty"));
                    temp.setItem_case_size(mJsonarry.getJSONObject(i).getString("case_size"));
                    data.add(temp);
                    totalQty = totalQty + mJsonarry.getJSONObject(i).getInt("item_qty");
                    int r = Math.abs(mJsonarry.getJSONObject(i).getInt("item_qty")) % mJsonarry.getJSONObject(i).getInt("case_size");
                    int q = Math.abs(mJsonarry.getJSONObject(i).getInt("item_qty")) / mJsonarry.getJSONObject(i).getInt("case_size");
                    int halfcase = mJsonarry.getJSONObject(i).getInt("case_size") / 2;
                    if (r >= halfcase) {
                        q = q + 1;
                    }
                    totalBoxes = totalBoxes +q;

                }
                totalQtyView.setText(String.valueOf( totalQty));
                totalBoxesView.setText(String.valueOf( totalBoxes));

                confirm_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

               DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

                confirm_view.addItemDecoration(dividerItemDecoration);

                 SnapHelper snapHelper = new LinearSnapHelper();
                snapHelper.attachToRecyclerView(confirm_view);


                confirm_view.setAdapter(new ConfirmStockAdapter(data));

                Log.d("Confirm_Json", itemList.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        findViewById(R.id.btn_stock_confirm_cancel).setOnClickListener(v -> finish());
        findViewById(R.id.btn_stock_confirm_confirm).setOnClickListener(v->{
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }
}
