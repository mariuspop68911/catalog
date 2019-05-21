package com.example.mariuspop.catalog3.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.Utils;
import com.example.mariuspop.catalog3.adapters.ClasaPerformanceAbsAdapter;
import com.example.mariuspop.catalog3.adapters.ClasaPerformanceMediiAdapter;
import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.AbsenteWrapper;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.MediiEleviWrapper;
import com.example.mariuspop.catalog3.wizard.AddManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ClassPerformanceActivity extends AppActivity implements ClassPerformanceView {

    private Context context;
    private RelativeLayout loadingPanel;
    private ListView mediiListView;
    private ListView absListView;
    private ClassPerformancePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        loadingPanel = findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.VISIBLE);

        mediiListView = findViewById(R.id.class_performance_medii_lista);
        absListView = findViewById(R.id.class_performance_absente_lista);

        presenter = new ClassPerformancePresenter(this);

    }

    @Override
    public void setUi(Clasa clasa) {
        if (clasa != null) {
            ArrayList<MediiEleviWrapper> mediiEleviWrappers = new ArrayList<>();
            for (Elev elev : clasa.getElevi()) {
                if (!elev.getNote().isEmpty()) {
                    MediiEleviWrapper mediiEleviWrapper = new MediiEleviWrapper();
                    mediiEleviWrapper.setNumeElev(elev.getName());
                    mediiEleviWrapper.setMedie(Double.valueOf(Utils.getMedieGenerala(AddManager.getInstance().getClasa(), elev)));
                    mediiEleviWrappers.add(mediiEleviWrapper);
                }
            }
            Comparator<MediiEleviWrapper> compareByMedie = new Comparator<MediiEleviWrapper>() {
                @Override
                public int compare(MediiEleviWrapper o1, MediiEleviWrapper o2) {
                    return o2.getMedie().compareTo(o1.getMedie());
                }
            };
            Collections.sort(mediiEleviWrappers, compareByMedie);
            final ClasaPerformanceMediiAdapter adapter = new ClasaPerformanceMediiAdapter(mediiEleviWrappers, this);
            mediiListView.setAdapter(adapter);

            Comparator<AbsenteWrapper> compareByAbs = new Comparator<AbsenteWrapper>() {
                @Override
                public int compare(AbsenteWrapper d, AbsenteWrapper d1) {
                    return d1.getNumarAbsNemotivate() - d.getNumarAbsNemotivate();
                }
            };

            ArrayList<AbsenteWrapper> eleviWithAbs = new ArrayList<>();
            for (Elev elev : clasa.getElevi()) {
                if (!elev.getAbsente().isEmpty()) {
                    ArrayList<Absenta> absNemotivate = new ArrayList<>();
                    for (Absenta absenta : elev.getAbsente()) {
                        if (!absenta.isMotivata()) {
                            absNemotivate.add(absenta);
                        }
                    }
                    if (!absNemotivate.isEmpty()) {
                        AbsenteWrapper absenteWrapper = new AbsenteWrapper();
                        absenteWrapper.setNumeElev(elev.getName());
                        absenteWrapper.setNumarAbsNemotivate(absNemotivate.size());
                        eleviWithAbs.add(absenteWrapper);
                    }
                }
            }
            Collections.sort(eleviWithAbs, compareByAbs);
            final ClasaPerformanceAbsAdapter absAdapter = new ClasaPerformanceAbsAdapter(eleviWithAbs, this);
            absListView.setAdapter(absAdapter);
        }
        loadingPanel.setVisibility(View.GONE);
    }

    @Override
    protected void setToolbarTitle() {
        toolbar.setTitle(getResources().getString(R.string.situatie_clasa));
        toolbar.setSubtitle("");
    }

    @Override
    public int getContentAreaLayoutId() {
        return R.layout.class_performance_activity;
    }

}
