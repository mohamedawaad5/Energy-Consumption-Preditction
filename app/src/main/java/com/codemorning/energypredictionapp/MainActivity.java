package com.codemorning.energypredictionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LineChart lineChart;
    private TextView tvEnergyUsage;
    private Button btnFetchData, btnViewTips;

    private Handler handler = new Handler();
    private boolean realTimeEnabled = true;

    private List<Entry> cumulativeEntries = new ArrayList<>();
    int nextTimeStamp = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart = findViewById(R.id.lineChart);
        tvEnergyUsage = findViewById(R.id.tvEnergyUsage);
        btnFetchData = findViewById(R.id.btnFetchData);
        btnViewTips = findViewById(R.id.btnViewTips);

        setupLineChart();

        btnFetchData.setOnClickListener(v -> {
            startRealTimeData();
        });

        btnViewTips.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TipsActivity.class));
        });
    }

    private void setupLineChart() {
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getDescription().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
    }


    private void simulateDataFetch() {
        try {
            TFLiteHelper tfLiteHelper = new TFLiteHelper("model_with_flex_ops_3.tflite", getAssets());

            float[] input = new float[24];
            for (int i = 0; i < 24; i++) {
                input[i] = (float) Math.random() * 10 + 5;
            }

            float[] prediction = tfLiteHelper.predict(input);

            cumulativeEntries.add(new Entry(nextTimeStamp, prediction[0]));

            LineDataSet dataSet = new LineDataSet(cumulativeEntries, "Energy Usage (kWh)");
            dataSet.setColor(ContextCompat.getColor(this, R.color.purple_500));
            dataSet.setCircleColor(ContextCompat.getColor(this, R.color.purple_500));
            LineData lineData = new LineData(dataSet);

            lineChart.setData(lineData);
            lineChart.invalidate();

            nextTimeStamp++;
        } catch (IOException e) {
            Log.d("Awaad", e.getMessage());
        }
    }

    private void startRealTimeData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (realTimeEnabled) {
                    simulateDataFetch();
                    handler.postDelayed(this, 5000);
                }
            }
        }, 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        realTimeEnabled = prefs.getBoolean("RealTimeUpdates", true);

        if (realTimeEnabled) {
            startRealTimeData();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        realTimeEnabled = false;
        handler.removeCallbacksAndMessages(null);
    }
}