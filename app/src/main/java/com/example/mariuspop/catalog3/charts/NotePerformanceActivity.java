package com.example.mariuspop.catalog3.charts;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.models.Elev;
import com.github.mikephil.charting.charts.BarChart;


public class NotePerformanceActivity extends AppActivity implements NotePerformanceView {

    private BarChart chart;
    private BarChart chart2;
    private NotePerformancePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chart = findViewById(R.id.chart);
        chart2 = findViewById(R.id.chart2);

        presenter = new NotePerformancePresenter(this, this);

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
    public int getContentAreaLayoutId() {
        return R.layout.activity_note_perf_chart;
    }


    @Override
    public void setPerformanceMaterii(String materieNume1, String materieNume2, String materieNume3) {
        TextView materie1 = findViewById(R.id.materie1);
        TextView materie2 = findViewById(R.id.materie2);
        TextView materie3 = findViewById(R.id.materie3);
        materie1.setText(materieNume1);
        materie2.setText(materieNume2);
        materie3.setText(materieNume3);
    }

    @Override
    public void setMediaGenerala(String medie) {
        TextView mediaGen = findViewById(R.id.media_gen);
        mediaGen.setText(medie);
    }

    @Override
    public BarChart getBestChart() {
        return chart;
    }

    @Override
    public BarChart getWorstChart() {
        return chart2;
    }
}