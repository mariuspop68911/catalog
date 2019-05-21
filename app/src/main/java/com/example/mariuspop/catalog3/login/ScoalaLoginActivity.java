package com.example.mariuspop.catalog3.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.NavigationManager;
import com.example.mariuspop.catalog3.PreferencesManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackInstitutie;
import com.example.mariuspop.catalog3.main.MateriiActivity;
import com.example.mariuspop.catalog3.main.ScHomeActivity;
import com.example.mariuspop.catalog3.models.Institutie;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import mehdi.sakout.fancybuttons.FancyButton;

public class ScoalaLoginActivity extends AppCompatActivity implements FirebaseCallbackInstitutie {

    private static final String TAG = ScoalaLoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 123;
    private RelativeLayout loadingPanel;
    /**
     * Change the null parameter in {@code setContentView()}
     * to a layout resource {@code R.layout.example}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: hit");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        loadingPanel = findViewById(R.id.loadingPanel);
        FancyButton signInButton = findViewById(R.id.sign_in);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            loadingPanel.setVisibility(View.VISIBLE);
            FirebaseDb.getInstituteByUserId(this, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        }
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AuthUI.IdpConfig> providers = Collections.singletonList(
                        new AuthUI.IdpConfig.EmailBuilder().build());

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),RC_SIGN_IN);
            }
        });

        TextView creeazaCont = findViewById(R.id.creeaza_account);

        creeazaCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadingPanel.setVisibility(View.VISIBLE);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDb.getInstituteByUserId(this, Objects.requireNonNull(user).getUid());
            } else {
                Toast.makeText(this, getResources().getString(R.string.error_login), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onInstitutieReceived(Institutie institutie) {
        loadingPanel.setVisibility(View.GONE);
        if (institutie != null) {
            long clasaId = 0L;
            try {
                clasaId = Long.valueOf(PreferencesManager.getStringFromPrefs(Constants.CURRENT_CLASS));
            } catch (Exception e) {
                NavigationManager.navigateToActivity(this, ScHomeActivity.class);
            }
            if(clasaId == 0) {
                NavigationManager.navigateToActivity(this, ScHomeActivity.class);
            } else {
                NavigationManager.navigateToActivity(this, MateriiActivity.class);
            }
        } else {
            NavigationManager.navigateToActivity(this, AddInstitutionActivity.class);
        }
    }
}