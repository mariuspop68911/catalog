package com.example.mariuspop.catalog3.client.MVP;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.Utils;
import com.example.mariuspop.catalog3.adapters.AbsenteTransparentAdapter;
import com.example.mariuspop.catalog3.adapters.MesajeAdapter;
import com.example.mariuspop.catalog3.adapters.NoteTransparentAdapter;
import com.example.mariuspop.catalog3.client.ElevManager;
import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.mesaje.MesajProf;
import com.example.mariuspop.catalog3.models.Nota;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

public class ClientMaterieDetailsActivity extends AppActivity implements ClientMaterieDetailsView {

    private Context context;

    private MesajeAdapter mesajeAdapter;
    private long materieId;
    private String materieNume;
    private ScrollView scrolLayout;
    private ClientMaterieDetailsPresenter presenter;
    private EditText sendEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        materieId = getIntent().getLongExtra(Constants.MATERIE_ID, 0L);
        materieNume = getIntent().getStringExtra(Constants.MATERIE_NUME);

        sendEditText = findViewById(R.id.send_edittext);
        TextView materieNumeText = findViewById(R.id.title);
        scrolLayout = findViewById(R.id.scrolLayout);
        RelativeLayout loadingPanel = findViewById(R.id.loadingPanel);
        TextView mediaValue = findViewById(R.id.media_value);
        final FancyButton sendButton = findViewById(R.id.button_chatbox_send);

        presenter = new ClientMaterieDetailsPresenter(this, this);

        handleRecyclers();

        materieNumeText.setText(materieNume);
        loadingPanel.setVisibility(View.VISIBLE);
        mediaValue.setText(Utils.computeMedie(ElevManager.getInstance().getNoteByMaterieId(materieId)));
        sendEditText.addTextChangedListener(getTextWatcherListener(sendButton));
        sendButton.setOnClickListener(getOnClickListener());
        loadingPanel.setVisibility(View.GONE);
    }

    private void handleRecyclers() {

        ArrayList<Nota> noteByMaterie = ElevManager.getInstance().getNoteByMaterieId(materieId);
        ArrayList<Absenta> absenteByMaterie = ElevManager.getInstance().getAbsenteByMaterieId(materieId);
        ArrayList<MesajProf> mesajeByMaterie = ElevManager.getInstance().getMesajeByMaterieId(materieId);

        final RecyclerView noteRecycleView = findViewById(R.id.elev_lista_note);
        noteRecycleView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        noteRecycleView.setLayoutManager(llm);
        NoteTransparentAdapter adapter = new NoteTransparentAdapter(this, noteByMaterie);
        noteRecycleView.setAdapter(adapter);

        final RecyclerView absRecycleView = findViewById(R.id.elev_lista_absente);
        absRecycleView.setHasFixedSize(true);
        LinearLayoutManager llms = new LinearLayoutManager(this);
        absRecycleView.setLayoutManager(llms);
        AbsenteTransparentAdapter adapterAbs = new AbsenteTransparentAdapter(this, absenteByMaterie);
        absRecycleView.setAdapter(adapterAbs);

        final RecyclerView mesajeRecycleView = findViewById(R.id.elev_lista_mesaje);
        mesajeRecycleView.setHasFixedSize(true);
        LinearLayoutManager llmAbss = new LinearLayoutManager(context);
        mesajeRecycleView.setLayoutManager(llmAbss);
        mesajeAdapter = new MesajeAdapter(context, mesajeByMaterie);
        mesajeRecycleView.setAdapter(mesajeAdapter);
    }

    private TextWatcher getTextWatcherListener(final FancyButton sendButton) {
        return new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0) {
                    sendButton.setVisibility(View.VISIBLE);
                } else {
                    sendButton.setVisibility(View.GONE);
                }
            }
        };
    }

    private View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.handleSend(materieNume, materieId);
            }
        };
    }

    @Override
    public void setToolbarTitle() {
        if(presenter != null) {
            Elev elev = presenter.getElev();
            if (elev != null) {
                toolbar.setTitle(elev.getName());
                toolbar.setSubtitle(elev.getInstituteName());
            }
        }
    }

    @Override
    public EditText getSendEditText() {
        return sendEditText;
    }

    @Override
    public MesajeAdapter getMesajeAdapter() {
        return mesajeAdapter;
    }

    @Override
    public ScrollView getScrolLayout() {
        return scrolLayout;
    }

    @Override
    public int getContentAreaLayoutId() {
        return R.layout.client_materie_details_activity;
    }
}