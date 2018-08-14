package com.neurosky.algo_sdk_sample;

import android.icu.text.SimpleDateFormat;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Date;

public class CustomValueForm implements IAxisValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        long now = System.currentTimeMillis();

        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(date);

        return formatDate;
    }
}

