package com.example.mariuspop.catalog3.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.NavigationManager;
import com.example.mariuspop.catalog3.PreferencesManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.adapters.CustomAdapterMaterie;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClase;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Materie;
import com.example.mariuspop.catalog3.wizard.AddManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class MateriiActivity extends AppActivity implements FirebaseCallbackClase {
    private Context context;
    private ArrayList<Materie> materii = new ArrayList<>();
    private Clasa clasa;
    private ListView dbListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        dbListView = findViewById(R.id.main_materii_lista);
        dbListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(4000);
                view.startAnimation(animation1);
            }
        });
        clasa = AddManager.getInstance().getClasa();
        final long clasaId = getIntent().getLongExtra(Constants.EXTRA_MESSAGE_CLASA, 0L);
        if (clasaId == 0L || clasa == null) {
            FirebaseDb.getClaseByUserId(this, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        } else {
            displayList();
        }
    }

    @Override
    protected void setToolbarTitle() {
        if (clasa != null) {
            toolbar.setTitle(clasa.getInstitutieName());
            toolbar.setSubtitle(clasa.getName());
        } else {
            toolbar.setTitle(PreferencesManager.getStringFromPrefs(Constants.INSTITUTE_NAME));
        }
    }

    @Override
    public int getContentAreaLayoutId() {
        return R.layout.main_materii_activity;
    }

    @Override
    public void onClaseReceived(ArrayList<Clasa> clase) {
        long clasaId = 0L;
        try {
            clasaId = Long.valueOf(PreferencesManager.getStringFromPrefs(Constants.CURRENT_CLASS));
        } catch (Exception e) {
            NavigationManager.navigateToActivity(context, ScHomeActivity.class);
        }

        if(clasaId == 0L){
            NavigationManager.navigateToActivity(context, ScHomeActivity.class);
        }

        for (Clasa clasaWs : clase) {
            if (clasaWs.getClasaId() == clasaId) {
                clasa = clasaWs;
                AddManager.getInstance().setClasa(clasa);
            }
        }
        setToolbarTitle();
        displayList();
    }

    private void displayList() {
        if (clasa != null) {
            materii = clasa.getMaterii();
            CustomAdapterMaterie adapter = new CustomAdapterMaterie(materii, getApplicationContext());
            dbListView.setAdapter(adapter);
            dbListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(context, EleviActivity.class);
                    intent.putExtra(Constants.EXTRA_MESSAGE_MATERIE, materii.get(position));
                    startActivity(intent);
                }
            });
        }
    }
}
