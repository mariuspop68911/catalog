package com.example.mariuspop.catalog3.wizard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.PreferencesManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.SmsHandlerManager;
import com.example.mariuspop.catalog3.adapters.CustomAdapterElevi;
import com.example.mariuspop.catalog3.main.MateriiActivity;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

import mehdi.sakout.fancybuttons.FancyButton;

public class WizardAddEleviActivity extends AppActivity {

    private EditText edit;

    private Context context;

    private ListView adaugaEleviLista;

    private ArrayList<Elev> elevi = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        adaugaEleviLista = findViewById(R.id.adauga_elevi_lista);
        CustomAdapterElevi adapter = new CustomAdapterElevi(false, this, elevi, null, null, getApplicationContext());
        adaugaEleviLista.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_elev_dialog);
                dialog.setTitle(getResources().getString(R.string.adauga_elev));
                final EditText name = dialog.findViewById(R.id.edit1);
                final EditText phone = dialog.findViewById(R.id.edit2);
                Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nameText = name.getText().toString();
                        String phoneText = phone.getText().toString();
                        if (!nameText.isEmpty() && !phoneText.isEmpty()) {
                            Elev elev = new Elev(nameText, phoneText);
                            elev.setInstituteName(AddManager.getInstance().getClasa().getInstitutieName());
                            elev.setClasaId(Calendar.getInstance().getTimeInMillis());
                            elev.setElevId(Calendar.getInstance().getTimeInMillis() + 100);
                            Random rd = new Random();
                            String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                            char letter1 = abc.charAt(rd.nextInt(abc.length()));
                            char letter2 = abc.charAt(rd.nextInt(abc.length()));
                            String elevCode = String.valueOf(letter1) + String.valueOf(letter2)
                                    + String.valueOf(elev.getElevId()).substring(String.valueOf(elev.getElevId()).length() - 4);
                            elev.setElevCode(elevCode);
                            elevi.add(elev);
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        FancyButton continua = findViewById(R.id.continua);
        continua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!elevi.isEmpty()) {
                    Clasa clasa = AddManager.getInstance().getClasa();
                    if (Constants.SMS_ENABLED) {
                        SmsHandlerManager.getInstance().sendSmsToDownloadApp(elevi);
                    }

                    clasa.setUserId(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                    clasa.setElevi(elevi);
                    long clasaId = dbHelper.saveClasaAndDependencies(clasa);
                    FirebaseDb.saveClasa(clasa);
                    Intent intent = new Intent(context, MateriiActivity.class);
                    intent.putExtra(Constants.EXTRA_MESSAGE_CLASA, clasaId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(context, "Completeaza", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void setToolbarTitle() {
        toolbar.setTitle(PreferencesManager.getStringFromPrefs(Constants.INSTITUTE_NAME));
        toolbar.setSubtitle(AddManager.getInstance().getClasa().getName());
    }

    @Override
    public int getContentAreaLayoutId() {
        return R.layout.main_add_elevi;
    }
}
