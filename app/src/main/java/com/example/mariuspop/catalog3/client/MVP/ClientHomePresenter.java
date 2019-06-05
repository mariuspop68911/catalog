package com.example.mariuspop.catalog3.client.MVP;

import android.content.Context;
import android.view.View;

import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.FirebaseRm;
import com.example.mariuspop.catalog3.PreferencesManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.Utils;
import com.example.mariuspop.catalog3.adapters.ClientAlertAdapter;
import com.example.mariuspop.catalog3.adapters.ClientHomeAdapter;
import com.example.mariuspop.catalog3.client.ElevManager;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClasaById;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClientElev;
import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.Materie;
import com.example.mariuspop.catalog3.models.NewsItem;
import com.example.mariuspop.catalog3.models.Nota;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ClientHomePresenter implements FirebaseCallbackClientElev, FirebaseCallbackClasaById {

    private Elev elev;
    private ClientHomeView view;
    private Context context;
    private int count = 0;
    private Clasa clasa;

    ClientHomePresenter(ClientHomeView view, Context context) {
        this.view = view;
        this.context = context;
        getElevFromWs();
    }

    public int getCount() {
        return count;
    }

    void getElevFromWs() {
        FirebaseDb.getElevByCodeNumber(this, PreferencesManager.getStringFromPrefs(Constants.CODE_NUMBER));
    }

    public Elev getElev() {
        return elev;
    }

    @Override
    public void onElevReceived(Elev elev) {
        this.elev = elev;
        if (elev != null) {
            ElevManager.getInstance().setElev(elev);
            view.setToolbarTitle();
            FirebaseDb.getClasaById(this, elev.getClasaId());
        }
    }

    @Override
    public void onClasaReceived(Clasa clasas) {
        clasa = clasas;
        PreferencesManager.saveStringToPrefs(Constants.CURRENT_YEAR, String.valueOf(clasa.getYear()));
        ArrayList<Materie> materies = Utils.getMateriiByThisYear(clasa);
        ClientHomeAdapter adapter = new ClientHomeAdapter(materies, context);
        view.getList().setAdapter(adapter);
        ArrayList<String> alerts = getMessages();
        if (!alerts.isEmpty()) {
            view.getAlertLyout().setVisibility(View.VISIBLE);
            ClientAlertAdapter alertAdapter = new ClientAlertAdapter(getMessages(), context);
            view.getAlertList().setAdapter(alertAdapter);
        } else {
            view.getAlertLyout().setVisibility(View.GONE);
        }

        count = getUnseenNewsSize();
        view.invalidate();

        /*ArrayList<String> news = getNews(clasa);
        if (!news.isEmpty()) {
            view.getNewsLyout().setVisibility(View.VISIBLE);
            ClientNewsAdapter clientNewsAdapter = new ClientNewsAdapter(news, context);
            view.getNewsList().setAdapter(clientNewsAdapter);
        } else {
            view.getNewsLyout().setVisibility(View.GONE);
        }*/

        view.getLoadingPanel().setVisibility(View.GONE);
    }

    public ArrayList<NewsItem> getNews() {
        ArrayList<NewsItem> news = new ArrayList<>();
        for (Materie materie : Utils.getMateriiByThisYear(clasa)) {
            ArrayList<Absenta> absentas = Utils.getAbsenteByMaterieId(elev, materie.getMaterieId(), FirebaseRm.getCurrentSemesterForced());
            for (Absenta absenta : absentas) {
                if (Utils.isToday(absenta.getData().getTime())) {
                    NewsItem newsItem = new NewsItem();
                    newsItem.setText("Astazi elevul a fost absent la materia " + materie.getName() + ", ora:" + Utils.getTime(absenta.getData()) + ".");
                    newsItem.setMaterieId(materie.getMaterieId());
                    newsItem.setMaterieNume(materie.getName());
                    newsItem.setDate(absenta.getData());
                    news.add(newsItem);
                }
            }

            ArrayList<Nota> notas = Utils.getNoteByMaterieId(elev, materie.getMaterieId(), FirebaseRm.getCurrentSemesterForced());
            for (Nota nota : notas) {
                if (Utils.isToday(nota.getData().getTime())) {
                    NewsItem newsItem = new NewsItem();
                    newsItem.setText("Astazi elevul a primit nota " + nota.getValue() + (nota.isTeza() ? " in teza " : "") + " la materia " + materie.getName() + ".");
                    newsItem.setMaterieId(materie.getMaterieId());
                    newsItem.setMaterieNume(materie.getName());
                    newsItem.setDate(nota.getData());
                    news.add(newsItem);
                }
            }
        }

        Comparator compareByDate = new Comparator<NewsItem>() {
            @Override
            public int compare(NewsItem o1, NewsItem o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        };
        Collections.sort(news, compareByDate);

        return news;
    }

    private ArrayList<String> getMessages() {
        ArrayList<String> messages = new ArrayList<>();
        for (Materie materie : Utils.getMateriiByThisYear(clasa)) {
            ArrayList<Absenta> absentas = Utils.getAbsenteByMaterieId(elev, materie.getMaterieId(), FirebaseRm.getCurrentSemesterForced());
            ArrayList<Absenta> absenteNemotivate = new ArrayList<>();
            for (Absenta absenta : absentas) {
                if (!absenta.isMotivata()) {
                    absenteNemotivate.add(absenta);
                }
            }
            if (absenteNemotivate.size() > Constants.LIMITA_ABSENTE) {
                messages.add(context.getResources().getString(R.string.limita_abs_materie) + " " + materie.getName());
            }
        }
        for (Materie materie : Utils.getMateriiByThisYear(clasa)) {
            ArrayList<Nota> notas = Utils.getNoteByMaterieId(elev, materie.getMaterieId(), FirebaseRm.getCurrentSemesterForced());
            double medie = Double.valueOf(Utils.computeMedie(notas));
            boolean medieIssues = medie > 0.0 && medie < 5.0;
            if (medieIssues) {
                messages.add(context.getResources().getString(R.string.medie_sub) + " " + materie.getName());
            }
        }

        return messages;
    }

    public void resetCounter() {
        markAsSeen();
        count = 0;
        view.invalidate();
    }

    private void markAsSeen() {
        for (Elev elev1 : clasa.getElevi()) {
            if (elev1.getElevId() == elev.getElevId()) {
                for (Absenta absenta : Utils.getAbsenteByYear(elev1)) {
                    if (Utils.isToday(absenta.getData().getTime())) {
                        absenta.setSeen(true);
                    }
                }
                for (Nota nota : Utils.getNoteByYear(elev1)) {
                    if (Utils.isToday(nota.getData().getTime())) {
                        nota.setSeen(true);
                    }
                }
            }
        }

        if (count > 0) {
            FirebaseDb.saveClasa(clasa);
        }
    }

    private int getUnseenNewsSize() {
        int counter = 0;
        for (Elev elev1 : clasa.getElevi()) {
            if (elev1.getElevId() == elev.getElevId()) {
                for (Absenta absenta : Utils.getAbsenteByYear(elev1)) {
                    if (Utils.isToday(absenta.getData().getTime()) && !absenta.isSeen()) {
                        counter++;
                    }
                }
                for (Nota nota : Utils.getNoteByYear(elev1)) {
                    if (Utils.isToday(nota.getData().getTime()) && !nota.isSeen()) {
                        counter++;
                    }
                }
            }
        }
        return counter;
    }

}
