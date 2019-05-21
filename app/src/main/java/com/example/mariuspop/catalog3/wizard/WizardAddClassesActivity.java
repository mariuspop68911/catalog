package com.example.mariuspop.catalog3.wizard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.PreferencesManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackInstitutie;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Institutie;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Objects;

import mehdi.sakout.fancybuttons.FancyButton;

public class WizardAddClassesActivity extends AppActivity implements FirebaseCallbackInstitutie {

    private EditText edit;
    private FancyButton continua;
    private Context context;
    private FirebaseCallbackInstitutie firebaseCallbackInstitutie;
    private Clasa clasa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        firebaseCallbackInstitutie = this;
        edit = findViewById(R.id.edit);
        continua = findViewById(R.id.continua);
        edit.setHint(getResources().getString(R.string.numele_clasei));
        continua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit.getText().toString().isEmpty()) {
                    clasa = new Clasa(edit.getText().toString());
                    String instituteName = PreferencesManager.getStringFromPrefs(Constants.INSTITUTE_NAME);
                    if (instituteName != null && instituteName.isEmpty()) {
                        FirebaseDb.getInstituteByUserId(firebaseCallbackInstitutie, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                    } else {
                        handleContinue(instituteName);
                    }
                } else {
                    Toast.makeText(context, "Completeaza", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void setToolbarTitle() {
        toolbar.setTitle(PreferencesManager.getStringFromPrefs(Constants.INSTITUTE_NAME));
        toolbar.setSubtitle("");
    }

    @Override
    public int getContentAreaLayoutId() {
        return R.layout.main_add;
    }

    @Override
    public void onInstitutieReceived(Institutie institutie) {
        handleContinue(institutie.getNume());
    }

    private void handleContinue(String instituteName) {
        clasa.setInstitutieName(instituteName);
        clasa.setScoalaToken(PreferencesManager.getStringFromPrefs(Constants.FIREBASE_TOCKEN_PREFS));
        AddManager.getInstance().setClasa(clasa);
        Intent intent = new Intent(context, WizardAddMateriiActivity.class);
        startActivity(intent);
    }
}
