package com.example.mariuspop.catalog3.main;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.adapters.CustomAdapterElevi;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.Materie;
import com.example.mariuspop.catalog3.wizard.AddManager;

import java.util.ArrayList;

public class EleviActivity extends AppActivity {
    private Clasa clasa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView dbListView = findViewById(R.id.main_elevi_lista);
        clasa = AddManager.getInstance().getClasa();
        final Materie materie = (Materie) getIntent().getSerializableExtra(Constants.EXTRA_MESSAGE_MATERIE);
        ArrayList<Elev> elevi = clasa.getElevi();

        TextView materieNume = findViewById(R.id.materie_nume);
        materieNume.setText(materie.getName());

        final CustomAdapterElevi adapter = new CustomAdapterElevi(true, this, elevi, materie, dbHelper, getApplicationContext());
        dbListView.setAdapter(adapter);
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
}
