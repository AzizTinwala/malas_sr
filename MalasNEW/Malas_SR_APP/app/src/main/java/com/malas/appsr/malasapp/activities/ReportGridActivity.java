package com.malas.appsr.malasapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.malas.appsr.malasapp.R;

import java.util.Objects;

/**
 * Created by Arwa on 19/11/2017.
 */

public class ReportGridActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout llAverageLineCut;
    LinearLayout llAverageBillValue;
    LinearLayout llUniqueOutlet;
    LinearLayout ll_topview_report;
    LinearLayout llProductivity;
    private LinearLayout llTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_grid);

        Objects.requireNonNull(getSupportActionBar()).hide();
        initView();
        setOnClickListener();
    }

    private void setOnClickListener() {
        llTarget.setOnClickListener(this);
        llAverageLineCut.setOnClickListener(this);
        llAverageBillValue.setOnClickListener(this);
        llUniqueOutlet.setOnClickListener(this);
        llProductivity.setOnClickListener(this);
        ll_topview_report.setOnClickListener(this);
    }

    private void initView() {

        llTarget = findViewById(R.id.ll_target);
        llAverageLineCut = findViewById(R.id.ll_avg_line_cut);
        llAverageBillValue = findViewById(R.id.ll_average_bill_value);
        llUniqueOutlet = findViewById(R.id.ll_unique_bill_value);
        llProductivity = findViewById(R.id.ll_productivity_report);
        ll_topview_report = findViewById(R.id.ll_topview_report);

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {

            case R.id.ll_target:
                Intent target = new Intent(ReportGridActivity.this, TargetActivity.class);
                startActivity(target);
                break;
            case R.id.ll_avg_line_cut:
                Intent averageLineCut = new Intent(ReportGridActivity.this, AverageLineCutActivity.class);
                startActivity(averageLineCut);
                break;
            case R.id.ll_average_bill_value:
                Intent averageBillValue = new Intent(ReportGridActivity.this, AverageBillReport.class);
                startActivity(averageBillValue);
                break;
            case R.id.ll_unique_bill_value:
                Intent uniqueOutlet = new Intent(ReportGridActivity.this, UniqueBillOutletReport.class);
                startActivity(uniqueOutlet);
                break;
            case R.id.ll_productivity_report:
                Intent productivity = new Intent(ReportGridActivity.this, ProductivityReport.class);
                startActivity(productivity);
                break;
            case R.id.ll_topview_report:
                Intent topview = new Intent(ReportGridActivity.this, TopViewReport.class);
                startActivity(topview);
                break;
        }
    }
}
