package com.example.mariuspop.catalog3.login;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.NavigationManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.main.MateriiActivity;
import com.example.mariuspop.catalog3.models.Institutie;
import com.example.mariuspop.catalog3.models.ScUser;
import com.example.mariuspop.catalog3.wizard.WizardAddClassesActivity;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Objects;

import mehdi.sakout.fancybuttons.FancyButton;

public class AddInstitutionActivity extends AppCompatActivity {

    private static final String TAG = AddInstitutionActivity.class.getSimpleName();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: hit");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_institution_activity);
        context = this;
        final EditText nume = findViewById(R.id.nume);
        FancyButton continua = findViewById(R.id.continua);
        continua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numeText = nume.getText().toString();
                if (!numeText.isEmpty()) {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    Institutie institutie = new Institutie();
                    institutie.setInstitutieId(Calendar.getInstance().getTimeInMillis());
                    institutie.setNume(numeText);
                    institutie.setUserId(Objects.requireNonNull(firebaseUser).getUid());
                    FirebaseDb.saveInstitute(institutie);
                    ScUser user = new ScUser();
                    user.setUserName(firebaseUser.getDisplayName());
                    user.setEmail(firebaseUser.getEmail());
                    user.setUserId(firebaseUser.getUid());
                    user.setInstituteId(institutie.getInstitutieId());
                    user.setInstituteName(institutie.getNume());
                    FirebaseDb.saveUser(user);
                    NavigationManager.navigateToActivity(context, WizardAddClassesActivity.class);
                } else {
                    Toast.makeText(context, getResources().getString(R.string.hint_inst), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}