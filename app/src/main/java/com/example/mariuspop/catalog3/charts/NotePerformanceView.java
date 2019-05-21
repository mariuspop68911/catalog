package com.example.mariuspop.catalog3.charts;

import com.github.mikephil.charting.charts.BarChart;

public interface NotePerformanceView {

    void setPerformanceMaterii(String materieNume1, String materieNume2, String materieNume3);
    void setMediaGenerala(String medie);
    BarChart getBestChart();
    BarChart getWorstChart();
    void setToolbarTitle();
}
