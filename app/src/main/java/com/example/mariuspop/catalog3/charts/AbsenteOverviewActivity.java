package com.example.mariuspop.catalog3.charts;

import android.os.Bundle;
import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.models.Elev;
import com.github.mikephil.charting.charts.PieChart;

public class AbsenteOverviewActivity extends AppActivity implements AbsenteOverviewView {

    private PieChart chart;
    private AbsenteOverviewPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chart = findViewById(R.id.chart);

        presenter = new AbsenteOverviewPresenter(this, this);

    }

    @Override
    public void setToolbarTitle() {
        if (presenter != null) {
            Elev elev = presenter.getElev();
            if (elev != null) {
                toolbar.setTitle(elev.getName());
                toolbar.setSubtitle(elev.getInstituteName());
            }
        }
    }

    @Override
    public PieChart getChart() {
        return chart;
    }

    @Override
    public int getContentAreaLayoutId() {
        return R.layout.activity_abs_chart;
    }

}