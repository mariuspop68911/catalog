package com.example.mariuspop.catalog3.main;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.adapters.CustomAdapterElevi;
import com.example.mariuspop.catalog3.adapters.SeenMessagesAdapter;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClasaById;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.Materie;
import com.example.mariuspop.catalog3.models.SeenMessageModel;
import com.example.mariuspop.catalog3.models.mesaje.MesajProf;
import com.example.mariuspop.catalog3.wizard.AddManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EleviActivity extends AppActivity implements FirebaseCallbackClasaById {
    private Clasa clasa;
    private FirebaseCallbackClasaById firebaseCallbackClasaById;
    private LinearLayout alertLayout;
    private Materie materie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseCallbackClasaById = this;
        final Materie materie = (Materie) getIntent().getSerializableExtra(Constants.EXTRA_MESSAGE_MATERIE);
        clasa = AddManager.getInstance().getClasa();

        alertLayout = findViewById(R.id.alertLayout);

        if (clasa != null) {
            handleUi();
        } else {
            FirebaseDb.getClasaById(firebaseCallbackClasaById, materie.getClasaId());
        }

    }

    private void handleUi() {
        ListView dbListView = findViewById(R.id.main_elevi_lista);
        TextView materieNume = findViewById(R.id.materie_nume);
        materie = (Materie) getIntent().getSerializableExtra(Constants.EXTRA_MESSAGE_MATERIE);
        materieNume.setText(materie.getName());
        ArrayList<Elev> elevi = clasa.getElevi();
        final CustomAdapterElevi adapter = new CustomAdapterElevi(true, this, elevi, materie, dbHelper, getApplicationContext());
        dbListView.setAdapter(adapter);
        ArrayList<SeenMessageModel> seenMessageModels = getUnseenMessages();
        if (!seenMessageModels.isEmpty()) {
            alertLayout.setVisibility(View.VISIBLE);
            ListView listView = findViewById(R.id.alertList);
            SeenMessagesAdapter seenMessagesAdapter = new SeenMessagesAdapter(seenMessageModels, this, materie.getMaterieId());
            listView.setAdapter(seenMessagesAdapter);
        } else {
            alertLayout.setVisibility(View.GONE);
        }

    }

    private ArrayList<SeenMessageModel> getUnseenMessages() {
        ArrayList<SeenMessageModel> elevNames = new ArrayList<>();
        for (Elev elev : clasa.getElevi()) {
            ArrayList<MesajProf> mesajProfs = elev.getMesajProfs();
            if (!mesajProfs.isEmpty()) {
                Comparator compareByDate = new Comparator<MesajProf>() {
                    @Override
                    public int compare(MesajProf o1, MesajProf o2) {
                        return o2.getDate().compareTo(o1.getDate());
                    }
                };
                Collections.sort(mesajProfs, compareByDate);
                MesajProf lastMessage = mesajProfs.get(0);
                if (!lastMessage.isProf() && materie.getName().equals(lastMessage.getMaterieName())) {
                    if (!lastMessage.isSeen()) {
                        SeenMessageModel seenMessageModel = new SeenMessageModel();
                        seenMessageModel.setText("Ai un mesaj de la parintele elevului " + elev.getName() + ". Da tap pentru a raspunde");
                        seenMessageModel.setMesajId(lastMessage.getMesajId());
                        elevNames.add(seenMessageModel);
                    }
                }
            }
        }
        return elevNames;
    }

    @Override
    protected void setToolbarTitle() {
        if (clasa != null) {
            toolbar.setTitle(clasa.getInstitutieName());
            toolbar.setSubtitle(clasa.getName());
        }
    }

    @Override
    public int getContentAreaLayoutId() {
        return R.layout.main_elevi_activity;
    }

    @Override
    public void onClasaReceived(Clasa clasa) {
        if (clasa != null) {
            handleUi();
        }
    }
}
