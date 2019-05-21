package com.example.mariuspop.catalog3.client;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.NavigationManager;
import com.example.mariuspop.catalog3.PreferencesManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.client.MVP.ClientHomeActivity;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClientElev;
import com.example.mariuspop.catalog3.models.ClientUser;
import com.example.mariuspop.catalog3.models.Elev;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import mehdi.sakout.fancybuttons.FancyButton;

public class ClientAddPhoneNoActivity extends AppCompatActivity implements FirebaseCallbackClientElev {

    private static final String TAG = ClientAddPhoneNoActivity.class.getSimpleName();
    private Context context;
    private FirebaseCallbackClientElev firebaseCallbackClientElev;
    private RelativeLayout loadingPanel;
    private String codeNoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: hit");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_phoneno_activity);
        context = this;
        firebaseCallbackClientElev = this;
        loadingPanel = findViewById(R.id.loadingPanel);
        final EditText codeNo = findViewById(R.id.codeNo);
        codeNo.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        FancyButton continua = findViewById(R.id.continua);
        continua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeNoText = codeNo.getText().toString();
                if (!codeNoText.isEmpty()) {
                    loadingPanel.setVisibility(View.VISIBLE);
                    PreferencesManager.saveStringToPrefs(Constants.CODE_NUMBER, codeNoText);
                    FirebaseDb.getElevByCodeNumber(firebaseCallbackClientElev, codeNoText);
                } else {
                    Toast.makeText(context, getResources().getString(R.string.hint_phone), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDialog(final Elev elev){
        if (elev != null) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.confirma_elev_dialog);

            dialog.setTitle(getResources().getString(R.string.confirma));

            final TextView numeElev = dialog.findViewById(R.id.nume_elev);
            numeElev.setText(elev.getName());

            FancyButton inapoiButton = dialog.findViewById(R.id.dialogButtonCancel);
            inapoiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            FancyButton okButton = dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    ClientUser clientUser = new ClientUser();
                    clientUser.setUserId(Objects.requireNonNull(firebaseUser).getUid());
                    clientUser.setUserName(firebaseUser.getDisplayName());
                    clientUser.setEmail(firebaseUser.getEmail());
                    clientUser.setInstituteName(elev.getInstituteName());
                    clientUser.setPhoneNumber(elev.getPhoneNumber());
                    clientUser.setElevCode(codeNoText);
                    clientUser.setToken(PreferencesManager.getStringFromPrefs(Constants.FIREBASE_TOCKEN_PREFS));
                    FirebaseDb.saveClientUser(clientUser);
                    NavigationManager.navigateToActivity(context, ClientHomeActivity.class);
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            Toast.makeText(context,"Cod incorect", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onElevReceived(Elev elev) {
        loadingPanel.setVisibility(View.GONE);
        showDialog(elev);
    }
}