package com.example.mariuspop.catalog3.charts;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.PopupWindow;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.example.mariuspop.catalog3.NavigationManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.adapters.AbsenteSimpleAdapter;
import com.example.mariuspop.catalog3.client.ElevManager;
import com.example.mariuspop.catalog3.client.MVP.ClientHomeActivity;
import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.Elev;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AbsenteOverviewPresenter {

    private Elev elev;

    public AbsenteOverviewPresenter(final Context context, final AbsenteOverviewView view) {

        elev = ElevManager.getInstance().getElev();
        if (elev != null) {
            view.setToolbarTitle();
            List<PieEntry> entries = new ArrayList<PieEntry>();

            HashMap<String, List<Absenta>> hashMap = new HashMap<>();

            for (Absenta absenta : elev.getAbsente()) {

                if (!hashMap.containsKey(absenta.getMaterieNume())) {
                    ArrayList<Absenta> list = new ArrayList<>();
                    list.add(absenta);

                    hashMap.put(absenta.getMaterieNume(), list);
                } else {
                    hashMap.get(absenta.getMaterieNume()).add(absenta);
                }
            }
            Iterator it = hashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                PieEntry pieEntry = new PieEntry(((ArrayList<Absenta>) pair.getValue()).size());
                pieEntry.setData(((ArrayList<Absenta>) pair.getValue()).get(0).getMaterieId());
                pieEntry.setLabel(String.valueOf(pair.getKey()));
                entries.add(pieEntry);
                it.remove();
            }
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setColors(new int[]{R.color.btn_login, R.color.btn_logut_bg1, R.color.fancy2, R.color.browser_actions_divider_color, R.color.browser_actions_bg_grey,
                    R.color.colorAccent, R.color.bl_l, R.color.colorPrimary, R.color.comments, R.color.green_warm, R.color.red_warm}, context);
            PieData lineData = new PieData(dataSet);
            lineData.setValueFormatter(new MyFormatter());
            view.getChart().setData(lineData);
            view.getChart().setDescription(null);
            view.getChart().invalidate();

            view.getChart().setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    String materieId = ((PieEntry) e).getData().toString();

                    BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(context).inflate(R.layout.bbl_layout, null);
                    ArrayList<Absenta> absenteByMaterie = ElevManager.getInstance().getAbsenteByMaterieId(Long.valueOf(materieId));

                    final RecyclerView absRecycleView = bubbleLayout.findViewById(R.id.abs_rv);
                    absRecycleView.setHasFixedSize(true);
                    LinearLayoutManager llms = new LinearLayoutManager(context);
                    absRecycleView.setLayoutManager(llms);
                    AbsenteSimpleAdapter adapterAbs = new AbsenteSimpleAdapter(context, absenteByMaterie);
                    absRecycleView.setAdapter(adapterAbs);

                    PopupWindow popupWindow = BubblePopupHelper.create(context, bubbleLayout);
                    int[] location = new int[2];
                    location[0] = (int) h.getXPx();
                    location[1] = (int) h.getYPx();
                    //chart.getLocationInWindow(location);
                    bubbleLayout.setArrowDirection(ArrowDirection.BOTTOM);
                    popupWindow.showAtLocation(view.getChart(), Gravity.NO_GRAVITY, location[0], location[1] + view.getChart().getHeight() / 2 - 250);
                }

                @Override
                public void onNothingSelected() {

                }
            });
        } else {
            NavigationManager.navigateToActivity(context, ClientHomeActivity.class);
        }
    }

    public Elev getElev() {
        return elev;
    }
}
