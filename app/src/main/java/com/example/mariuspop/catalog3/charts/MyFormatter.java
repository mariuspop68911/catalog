package com.example.mariuspop.catalog3.charts;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class MyFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        // write your logic here
        return (int) value + " absente";
    }
}
