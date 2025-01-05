package com.codemorning.energypredictionapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class TipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        ListView lvTips = findViewById(R.id.lvTips);

        // Sample tips
        String[] tips = {
                "Unplug electronics when not in use.",
                "Use energy-efficient light bulbs.",
                "Run full loads in your dishwasher and washing machine.",
                "Set your thermostat a few degrees lower in winter.",
                "Turn off lights when leaving a room."
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tips);
        lvTips.setAdapter(adapter);
    }
}