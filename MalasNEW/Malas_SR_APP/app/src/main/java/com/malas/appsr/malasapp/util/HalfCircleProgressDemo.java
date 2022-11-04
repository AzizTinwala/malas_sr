package com.malas.appsr.malasapp.util;

import android.graphics.Color;
import android.os.Bundle;import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.malas.appsr.malasapp.R;

import java.util.Random;

/**
 * Created by Arwa on 13-Feb-19.
 */

public class HalfCircleProgressDemo  extends AppCompatActivity implements View.OnClickListener {

        private ProgressView mProgressView, mProgressView1, mProgressView2,
                mProgressLine, mProgressCircle;
        private final Random r = new Random(1);

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.half_circle_activity);

            mProgressView = findViewById(R.id.progressView);
            mProgressView1 = findViewById(R.id.progressView1);
            mProgressView2 = findViewById(R.id.progressView2);
            mProgressLine = findViewById(R.id.progressLine);
            mProgressCircle = findViewById(R.id.progressCircle);
            findViewById(R.id.btn_progress).setOnClickListener(this);
            findViewById(R.id.btn_direction).setOnClickListener(this);

            int[] colorList = new int[]{Color.GREEN, Color.YELLOW, Color.RED};
            mProgressView2.applyGradient(colorList);
            mProgressView1.applyGradient(colorList);
            mProgressLine.applyGradient(colorList);
            mProgressCircle.applyGradient(colorList);

        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_progress) {
                float progress = r.nextFloat();
                mProgressView.setProgress(progress);
                mProgressView1.setProgress(progress);
                mProgressView2.setProgress(progress);
                mProgressLine.setProgress(progress);
                mProgressCircle.setProgress(progress);
            } else if (view.getId() == R.id.btn_direction) {
                mProgressView1.setProgressDirection(toggleDirection(mProgressView1.getProgressDirection()));
                mProgressView2.setProgressDirection(toggleDirection(mProgressView2.getProgressDirection()));
                mProgressCircle.setProgressDirection(toggleDirection(mProgressCircle.getProgressDirection()));
                mProgressLine.setProgressDirection(toggleDirection(mProgressLine.getProgressDirection()));

            }
        }

    private ProgressView.Direction toggleDirection(ProgressView.Direction d) {
        if (d == ProgressView.Direction.FROM_LEFT) {
            return ProgressView.Direction.FROM_RIGHT;
        }

        return ProgressView.Direction.FROM_LEFT;
    }
}
