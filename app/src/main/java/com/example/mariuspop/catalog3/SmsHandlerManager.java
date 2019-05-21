package com.example.mariuspop.catalog3;

import android.telephony.SmsManager;

import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackSms;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.SMSGateway;

import java.util.ArrayList;

public class SmsHandlerManager implements FirebaseCallbackSms {

    private static final SmsHandlerManager ourInstance = new SmsHandlerManager();

    private String smsText;
    private String phoneNo;
    private ArrayList<Elev> elevi;

    public static SmsHandlerManager getInstance() {
        return ourInstance;
    }

    public void sendSmsToDownloadApp(ArrayList<Elev> elevi) {
        this.elevi = elevi;
        FirebaseDb.getSmsGatewayByPhoneNumber(this, Constants.FINAL_SMS_GATEWAY_NUMBER);
    }

    public void sendSms(String text, String numarTel) {
        smsText = text;
        phoneNo = numarTel;
        String smsGatwayNumber = PreferencesManager.getStringFromPrefs(Constants.ALT_PHONE_NUMBER);
        if (smsGatwayNumber != null && !smsGatwayNumber.isEmpty()) {
            FirebaseDb.getSmsGatewayByPhoneNumber(this, PreferencesManager.getStringFromPrefs(Constants.ALT_PHONE_NUMBER));
        } else {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(numarTel, null, smsText, null, null);
        }
    }

    @Override
    public void onSmsGatewayReceived(SMSGateway smsGateway) {
        if (smsGateway != null && elevi != null) {
            for (Elev elev : elevi) {
                smsText = "Intra pe bit.ly/2Y2kUkA si downloadeaza aplicatia Catalog pentru a vedea situatia scolara a elevului "
                        + elev.getName() + ". Foloseste codul: " + elev.getElevCode() + " . "+ elev.getInstituteName() ;
                phoneNo = elev.getPhoneNumber();
                FirebaseDb.sendSms(phoneNo, smsText, smsGateway.getFirebaseToken());
            }
        }
    }

    public String createNotaSmsText(String numeElev, String nota, String materie) {
        return "Elevul " + numeElev + " a primit nota " + nota + " la materia " + materie + ".";
    }
}
