package com.example.mariuspop.catalog3.main;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.NavigationManager;
import com.example.mariuspop.catalog3.PreferencesManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.adapters.ScHomeAdapter;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClase;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.wizard.WizardAddClassesActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;


public class ScHomeActivity extends AppActivity implements ScHomeView {

    private Context context;
    private RelativeLayout loadingPanel;
    private ListView dbListView;
    private TextView empty;
    private ScHomePresenter scHomePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        loadingPanel = findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.VISIBLE);

        dbListView = findViewById(R.id.main_clase_lista);
        FloatingActionButton adaugaButton = findViewById(R.id.adauga_clasa_button);
        empty = findViewById(R.id.empty);
        adaugaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToActivity(context, WizardAddClassesActivity.class);
            }
        });

        scHomePresenter = new ScHomePresenter(this);

    }

    @Override
    public void setUi(ArrayList<Clasa> clase) {
        if (clase != null && !clase.isEmpty()) {
            empty.setVisibility(View.INVISIBLE);
            final ScHomeAdapter adapter = new ScHomeAdapter(clase, this);
            dbListView.setAdapter(adapter);
        } else {
            empty.setVisibility(View.VISIBLE);
        }
        loadingPanel.setVisibility(View.GONE);
    }

    @Override
    protected void setToolbarTitle() {
        toolbar.setTitle(getResources().getString(R.string.alege_clasa));
        toolbar.setSubtitle("");
    }

    @Override
    public int getContentAreaLayoutId() {
        return R.layout.main_clase_activity;
    }

}
