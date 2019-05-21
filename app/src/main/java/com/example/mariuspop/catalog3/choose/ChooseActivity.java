package com.example.mariuspop.catalog3.choose;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.NavigationManager;
import com.example.mariuspop.catalog3.PreferencesManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.client.ClientLoginActivity;
import com.example.mariuspop.catalog3.login.ScoalaLoginActivity;

import mehdi.sakout.fancybuttons.FancyButton;

public class ChooseActivity extends AppCompatActivity {

    private static final String TAG = ChooseActivity.class.getSimpleName();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: hit");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_activity);
        context = this;
        LinearLayout linearLayout = findViewById(R.id.layout);
        TextView gatewayText = findViewById(R.id.gateway_text);
        if(!Constants.IS_SMS_GATEWAY) {
            linearLayout.setVisibility(View.VISIBLE);
            gatewayText.setVisibility(View.GONE);
            String mode = PreferencesManager.getStringFromPrefs(Constants.APP_MODE);
            if (mode != null && !PreferencesManager.getStringFromPrefs(Constants.APP_MODE).isEmpty()) {
                if (PreferencesManager.getStringFromPrefs(Constants.APP_MODE).equals(Constants.APP_MODE_PROFESOR)) {
                    NavigationManager.navigateToActivity(this, ScoalaLoginActivity.class);
                } else {
                    NavigationManager.navigateToActivity(context, ClientLoginActivity.class);
                }
                return;
            }
            FancyButton cadruDidactic = findViewById(R.id.cadru_didactic);
            FancyButton parinte = findViewById(R.id.parinte);
            cadruDidactic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferencesManager.saveStringToPrefs(Constants.APP_MODE, Constants.APP_MODE_PROFESOR);
                    NavigationManager.navigateToActivity(context, ScoalaLoginActivity.class);
                }
            });
            parinte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferencesManager.saveStringToPrefs(Constants.APP_MODE, Constants.APP_MODE_PARINTE);
                    NavigationManager.navigateToActivity(context, ClientLoginActivity.class);
                }
            });
        } else {
            linearLayout.setVisibility(View.GONE);
            gatewayText.setVisibility(View.VISIBLE);
        }
    }
}