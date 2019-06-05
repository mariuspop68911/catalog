package com.example.mariuspop.catalog3.charts;

import android.content.Context;
import android.graphics.Color;

import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.FirebaseRm;
import com.example.mariuspop.catalog3.NavigationManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.Utils;
import com.example.mariuspop.catalog3.client.ElevManager;
import com.example.mariuspop.catalog3.client.MVP.ClientHomeActivity;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClasaById;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.Materie;
import com.example.mariuspop.catalog3.models.MediiWrapper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotePerformancePresenter implements FirebaseCallbackClasaById {

    private ArrayList<MediiWrapper> mediiWrappersOnlyNonZero;
    private Elev elev;
    private Context context;
    private NotePerformanceView view;

    NotePerformancePresenter(Context context, NotePerformanceView view) {
        this.context = context;
        this.view = view;
        elev = ElevManager.getInstance().getElev();
        if (elev != null) {
            view.setToolbarTitle();
            FirebaseDb.getClasaById(this, elev.getClasaId());
        } else {
            NavigationManager.navigateToActivity(context, ClientHomeActivity.class);
        }
    }

    public Elev getElev() {
        return elev;
    }

    @Override
    public void onClasaReceived(Clasa clasa) {

        mediiWrappersOnlyNonZero = getMediiWrapers(clasa);

        handleBestChart(mediiWrappersOnlyNonZero);
        handleWorstChart(mediiWrappersOnlyNonZero);
        setMedieGenerala();
        setMateriiPerformance();
    }

    private ArrayList<MediiWrapper> mediiWrappersOnlyNonZero(ArrayList<MediiWrapper> mediiWrappers) {
        ArrayList<MediiWrapper> mediiWrappersOnlyNonZeros = new ArrayList<>();
        for (MediiWrapper mediiWrapper : mediiWrappers) {
            if (mediiWrapper.getMedie() > 0.0) {
                mediiWrappersOnlyNonZeros.add(mediiWrapper);
            }
        }
        return mediiWrappersOnlyNonZeros;
    }

    private void setMateriiPerformance() {
        if (mediiWrappersOnlyNonZero != null && !mediiWrappersOnlyNonZero.isEmpty() && mediiWrappersOnlyNonZero.size() > 1) {
            Collections.reverse(mediiWrappersOnlyNonZero);
            try {
                view.setPerformanceMaterii(mediiWrappersOnlyNonZero.get(0).getNumeMaterie(), mediiWrappersOnlyNonZero.get(1).getNumeMaterie(),
                        mediiWrappersOnlyNonZero.get(2).getNumeMaterie());
            } catch (IndexOutOfBoundsException ignored) {

            }
        }
    }

    private void setMedieGenerala() {
        double medieGeneralaSuma = 0.0;
        int counter = 0;
        for (MediiWrapper mediiWrapper : mediiWrappersOnlyNonZero) {
            counter++;
            medieGeneralaSuma = medieGeneralaSuma + mediiWrapper.getMedie();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String formatted = df.format(medieGeneralaSuma / counter);
        view.setMediaGenerala(formatted);
    }

    private void handleBestChart(ArrayList<MediiWrapper> mediiWrappersOnlyNonZero) {
        Collections.reverse(mediiWrappersOnlyNonZero);
        ArrayList<MediiWrapper> first = new ArrayList<>();
        for (int i = 0; i < mediiWrappersOnlyNonZero.size(); i++) {
            if (i < 5) {
                first.add(mediiWrappersOnlyNonZero.get(i));
            } else {
                break;
            }
        }

        List<Double> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (MediiWrapper mediiWrapper : first) {
            entries.add(mediiWrapper.getMedie());
            labels.add(mediiWrapper.getNumeMaterie());
        }

        createGraph(view.getBestChart(), labels, entries, context.getResources().getColor(R.color.fancy2));
    }

    private void handleWorstChart(ArrayList<MediiWrapper> mediiWrappersOnlyNonZero) {
        Collections.reverse(mediiWrappersOnlyNonZero);
        ArrayList<MediiWrapper> first = new ArrayList<>();
        for (int i = 0; i < mediiWrappersOnlyNonZero.size(); i++) {
            if (i < 4) {
                first.add(mediiWrappersOnlyNonZero.get(i));
            } else {
                break;
            }
        }

        List<Double> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (MediiWrapper mediiWrapper : first) {
            entries.add(mediiWrapper.getMedie());
            labels.add(mediiWrapper.getNumeMaterie());
        }

        createGraph(view.getWorstChart(), labels, entries, context.getResources().getColor(R.color.red_warm));
    }

    private ArrayList<MediiWrapper> getMediiWrapers(Clasa clasa) {
        ArrayList<MediiWrapper> mediiWrappers = new ArrayList<>();
        for (Materie materie : Utils.getMateriiByThisYear(clasa)) {
            String media = Utils.computeMedie(ElevManager.getInstance().getNoteByMaterieId(materie.getMaterieId(), FirebaseRm.getCurrentSemesterForced()));
            MediiWrapper mediiWrapper = new MediiWrapper();
            mediiWrapper.setMedie(Double.valueOf(media));
            mediiWrapper.setNumeMaterie(materie.getName());
            mediiWrappers.add(mediiWrapper);
        }

        Comparator<MediiWrapper> compareByMedie = new Comparator<MediiWrapper>() {
            @Override
            public int compare(MediiWrapper o1, MediiWrapper o2) {
                return o1.getMedie().compareTo(o2.getMedie());
            }
        };
        ArrayList<MediiWrapper> mediiWrappers1 = mediiWrappersOnlyNonZero(mediiWrappers);
        Collections.sort(mediiWrappers1, compareByMedie);
        return mediiWrappers1;
    }

    private void createGraph(BarChart chart, List<String> graph_label, List<Double> userScore, int color) {

        try {
            chart.setDrawBarShadow(false);
            chart.setDrawValueAboveBar(true);
            chart.getDescription().setEnabled(false);
            chart.setPinchZoom(false);

            chart.setDrawGridBackground(false);


            YAxis yAxis = chart.getAxisLeft();
            yAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) value);
                }
            });

            yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

            yAxis.setGranularity(1f);
            yAxis.setGranularityEnabled(true);

            chart.getAxisRight().setEnabled(false);


            XAxis xAxis = chart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setCenterAxisLabels(false);
            xAxis.setDrawGridLines(true);
/*
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    DecimalFormat df = new DecimalFormat("#.##");
                    return df.format(value);
                }
            });*/

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(graph_label));

            List<BarEntry> yVals1 = new ArrayList<BarEntry>();

            for (int i = 0; i < userScore.size(); i++) {
                DecimalFormat df = new DecimalFormat("#.##");
                String formatted = df.format(userScore.get(i));
                yVals1.add(new BarEntry(i, Float.valueOf(formatted)));
            }


            BarDataSet set1;

            if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
                set1.setValues(yVals1);
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {
                // create 2 datasets with different types
                set1 = new BarDataSet(yVals1, null);
                //set1.setDrawValues(false);
                set1.setColor(/*Color.rgb(255, 204, 0)*/color);


                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
                chart.setData(data);

            }

            chart.setFitBars(true);
            chart.getLegend().setEnabled(false);

            Legend l = chart.getLegend();
            l.setFormSize(12f); // set the size of the legend forms/shapes
            l.setForm(Legend.LegendForm.SQUARE); // set what type of form/shape should be used

            l.setTextSize(10f);
            l.setTextColor(Color.BLACK);
            l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
            l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis

            chart.invalidate();

            chart.animateY(2000);

        } catch (Exception ignored) {
        }
    }
}
