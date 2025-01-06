package com.codemorning.energypredictionapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    private static final String PREFERENCES_NAME = "EnergyAppPreferences";
    private static final String DATA_KEY = "HistoricalData";

    public static void saveHistoricalData(Context context, List<Float> data) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        StringBuilder dataStr = new StringBuilder();
        for (Float value : data) {
            dataStr.append(value).append(",");
        }
        editor.putString(DATA_KEY, dataStr.toString());
        editor.apply();
    }

    public static List<Float> getHistoricalData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String dataStr = preferences.getString(DATA_KEY, "");
        List<Float> data = new ArrayList<>();

        if (!dataStr.isEmpty()) {
            for (String s : dataStr.split(",")) {
                data.add(Float.parseFloat(s));
            }
        }
        return data;
    }
}
