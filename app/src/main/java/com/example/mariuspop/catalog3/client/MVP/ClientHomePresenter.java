package com.example.mariuspop.catalog3.client.MVP;

import android.content.Context;
import android.view.View;

import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.FirebaseDb;
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
import com.example.mariuspop.catalog3.models.Nota;

import java.util.ArrayList;

public class ClientHomePresenter implements FirebaseCallbackClientElev, FirebaseCallbackClasaById {

    private Elev elev;
    private ClientHomeView view;
    private Context context;

    ClientHomePresenter(ClientHomeView view, Context context) {
        this.view = view;
        this.context = context;
        getElevFromWs();
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
        ElevManager.getInstance().setElev(elev);
        view.setToolbarTitle();
        FirebaseDb.getClasaById(this, elev.getClasaId());
    }

    @Override
    public void onClasaReceived(Clasa clasa) {
        ArrayList<Materie> materies = clasa.getMaterii();
        ClientHomeAdapter adapter = new ClientHomeAdapter(materies, context);
        view.getList().setAdapter(adapter);
        ArrayList<String> alerts = getMessages(clasa);
        if (!alerts.isEmpty()) {
            view.getAlertLyout().setVisibility(View.VISIBLE);
            ClientAlertAdapter alertAdapter = new ClientAlertAdapter(getMessages(clasa), context);
            view.getAlertList().setAdapter(alertAdapter);
        } else {
            view.getAlertLyout().setVisibility(View.GONE);
        }

        view.getLoadingPanel().setVisibility(View.GONE);
    }

    private ArrayList<String> getMessages(Clasa clasa) {
        ArrayList<String> messages = new ArrayList<>();
        for (Materie materie : clasa.getMaterii()) {
            ArrayList<Absenta> absentas = Utils.getAbsenteByMaterieId(elev, materie.getMaterieId());
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
        for (Materie materie : clasa.getMaterii()) {
            ArrayList<Nota> notas = Utils.getNoteByMaterieId(elev, materie.getMaterieId());
            double medie = Double.valueOf(Utils.computeMedie(notas));
            boolean medieIssues = medie > 0.0 && medie < 5.0;
            if (medieIssues) {
                messages.add(context.getResources().getString(R.string.medie_sub) + " "  + materie.getName());
            }
        }

        return messages;
    }

}
