package com.example.mariuspop.catalog3.wizard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.PreferencesManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.adapters.CustomAdapterMaterie;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClasaById;
import com.example.mariuspop.catalog3.main.MateriiActivity;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Materie;

import java.util.ArrayList;
import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;

public class NewYearActivity extends AppActivity implements FirebaseCallbackClasaById {

    private Context context;

    private ListView adaugaMateriiLista;

    private EditText clasaNameEdit;

    private ArrayList<Materie> materii;
    private ArrayList<Materie> materiiToDisplay;

    private Clasa clasa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        FirebaseDb.getClasaById(this, Long.valueOf(PreferencesManager.getStringFromPrefs(Constants.CURRENT_CLASS)));
    }

    @Override
    protected void setToolbarTitle() {
        toolbar.setTitle(PreferencesManager.getStringFromPrefs(Constants.INSTITUTE_NAME));
        toolbar.setSubtitle(AddManager.getInstance().getClasa().getName());
    }

    @Override
    public void onClasaReceived(final Clasa clasa) {
        this.clasa = clasa;
        materii = clasa.getMaterii();

        adaugaMateriiLista = findViewById(R.id.adauga_materii_lista);
        clasaNameEdit = findViewById(R.id.edit);

        materiiToDisplay = new ArrayList<>();
        CustomAdapterMaterie adapter = new CustomAdapterMaterie(materiiToDisplay, getApplicationContext());
        adaugaMateriiLista.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_dialog);
                dialog.setTitle(getResources().getString(R.string.adauga_materie));
                final EditText name = dialog.findViewById(R.id.edit1);
                final CheckBox checkBox = dialog.findViewById(R.id.checkBox1);
                Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nameText = name.getText().toString();
                        if (!nameText.isEmpty()) {
                            Materie materie = new Materie(name.getText().toString());
                            materie.setYear(clasa.getYear() + 1);
                            materie.setTeza(checkBox.isChecked());
                            materie.setMaterieId(Calendar.getInstance().getTimeInMillis());
                            materiiToDisplay.add(materie);
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
                if (!materiiToDisplay.isEmpty() && !clasaNameEdit.getText().toString().isEmpty()) {
                    materii.addAll(materiiToDisplay);
                    clasa.setYear(clasa.getYear() + 1);
                    clasa.setCurrentYearStart(PreferencesManager.getStringFromPrefs(Constants.CURRENT_YEAR_START));
                    clasa.setName(clasaNameEdit.getText().toString());
                    clasa.setMaterii(materii);
                    FirebaseDb.saveClasa(clasa);
                    Intent intent = new Intent(context, MateriiActivity.class);
                    intent.putExtra(Constants.EXTRA_MESSAGE_CLASA, clasa.getClasaId());
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Completeaza", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public int getContentAreaLayoutId() {
        return R.layout.new_year_layout;
    }
}
