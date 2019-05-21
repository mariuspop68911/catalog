package com.example.mariuspop.catalog3;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mariuspop.catalog3.models.SMSGateway;
import com.example.mariuspop.catalog3.wizard.WizardAddClassesActivity;

import mehdi.sakout.fancybuttons.FancyButton;

public class SettingsActivity extends AppActivity {

    private Context context;
    private Switch gatewaySwitch;
    private LinearLayout gatewayLayout;
    private LinearLayout altPhoneLayout;
    private TextView gatewayPhoneText;
    private Switch altPhoneSwitch;
    private TextView altPhoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        FloatingActionButton button = findViewById(R.id.adauga_clasa_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WizardAddClassesActivity.class);
                startActivity(intent);
            }
        });

        handleGateway();
        handleAltPhone();
    }

    @Override
    protected void setToolbarTitle() {

    }

    @Override
    public int getContentAreaLayoutId() {
        return R.layout.main_settings_activity;
    }

    private void handleGateway(){
        gatewaySwitch = findViewById(R.id.simpleSwitch);
        gatewayLayout = findViewById(R.id.nr_phone_layout);
        gatewayPhoneText = findViewById(R.id.nr_phone_text);
        if (PreferencesManager.getStringFromPrefs(Constants.SMSGATWAY_PHONE_NUMBER) != null &&
                !PreferencesManager.getStringFromPrefs(Constants.SMSGATWAY_PHONE_NUMBER).isEmpty()) {
            gatewaySwitch.setChecked(true);
            gatewayPhoneText.setText(PreferencesManager.getStringFromPrefs(Constants.SMSGATWAY_PHONE_NUMBER));
            gatewayLayout.setVisibility(View.VISIBLE);
        } else {
            gatewaySwitch.setChecked(false);
            gatewayLayout.setVisibility(View.GONE);
        }
        gatewaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.add_sms_gateway);
                    dialog.setTitle(context.getResources().getString(R.string.adauga_sms_gateway_title));
                    final EditText nrPhoneEdit = dialog.findViewById(R.id.nr_phone_edit);
                    FancyButton dialogButtonOk = dialog.findViewById(R.id.dialogButtonOK);
                    FancyButton dialogButtonCancel = dialog.findViewById(R.id.dialogButtonCancel);
                    dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gatewaySwitch.setChecked(false);
                            dialog.dismiss();
                        }
                    });
                    dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String nrTel = nrPhoneEdit.getText().toString();
                            if (!nrTel.isEmpty()) {
                                String token = PreferencesManager.getStringFromPrefs(Constants.FIREBASE_TOCKEN_PREFS);
                                SMSGateway smsGateway = new SMSGateway(nrTel, token);
                                FirebaseDb.saveSmsGateway(smsGateway);
                                gatewayPhoneText.setText(nrTel);
                                gatewayLayout.setVisibility(View.VISIBLE);
                            } else {
                                gatewaySwitch.setChecked(false);
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                } else {
                    FirebaseDb.removeSmsGatewayByPhoneNumber(PreferencesManager.getStringFromPrefs(Constants.SMSGATWAY_PHONE_NUMBER));
                    gatewayLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void handleAltPhone(){
        altPhoneSwitch = findViewById(R.id.switch_alt_phone);
        altPhoneLayout = findViewById(R.id.alt_nr_phone_layout);
        altPhoneText = findViewById(R.id.alt_nr_phone_text);

        if (PreferencesManager.getStringFromPrefs(Constants.ALT_PHONE_NUMBER) != null &&
                !PreferencesManager.getStringFromPrefs(Constants.ALT_PHONE_NUMBER).isEmpty()) {
            altPhoneSwitch.setChecked(true);
            altPhoneText.setText(PreferencesManager.getStringFromPrefs(Constants.ALT_PHONE_NUMBER));
            altPhoneLayout.setVisibility(View.VISIBLE);
        } else {
            altPhoneSwitch.setChecked(false);
            altPhoneLayout.setVisibility(View.GONE);
        }
        altPhoneSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.add_sms_alt_phone);
                    dialog.setTitle(context.getResources().getString(R.string.adauga_sms_alt_phone_title));
                    final EditText nrPhoneEdit = dialog.findViewById(R.id.nr_phone_edit);
                    FancyButton dialogButtonOk = dialog.findViewById(R.id.dialogButtonOK);
                    FancyButton dialogButtonCancel = dialog.findViewById(R.id.dialogButtonCancel);
                    dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            altPhoneSwitch.setChecked(false);
                            dialog.dismiss();
                        }
                    });
                    dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String nrTel = nrPhoneEdit.getText().toString();
                            if (!nrTel.isEmpty()) {
                                PreferencesManager.saveStringToPrefs(Constants.ALT_PHONE_NUMBER, nrTel);
                                altPhoneText.setText(nrTel);
                                altPhoneLayout.setVisibility(View.VISIBLE);
                            } else {
                                altPhoneSwitch.setChecked(false);
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                } else {
                    PreferencesManager.removeStringToPrefs(Constants.ALT_PHONE_NUMBER);
                    altPhoneLayout.setVisibility(View.GONE);
                }
            }
        });
    }


}
