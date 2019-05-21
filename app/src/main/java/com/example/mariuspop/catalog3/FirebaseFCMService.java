package com.example.mariuspop.catalog3;

import android.util.Log;

import com.example.mariuspop.catalog3.models.SMSGateway;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseFCMService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TESTLOGG", "Refreshed token: " + refreshedToken);
        PreferencesManager.saveStringToPrefs(Constants.FIREBASE_TOCKEN_PREFS, refreshedToken);
        if (Constants.IS_SMS_GATEWAY) {
            SMSGateway smsGateway = new SMSGateway("0742204489", refreshedToken);
            FirebaseDb.saveSmsGateway(smsGateway);
        }
        Log.v("TESTLOGG", "Refreshed token: " + PreferencesManager.getStringFromPrefs(Constants.FIREBASE_TOCKEN_PREFS));
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }
}
