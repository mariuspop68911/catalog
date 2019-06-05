package com.example.mariuspop.catalog3;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mariuspop.catalog3.interfaces.FirebaseRMCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FirebaseRm {

    public static void fetchRemoteConfig(final FirebaseRMCallback firebaseRMCallback) {
        PreferencesManager.removeStringToPrefs(Constants.SEM_OVERRIDE);
        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mFirebaseRemoteConfig.fetch(0).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mFirebaseRemoteConfig.activateFetched();
                    String sem1Start = mFirebaseRemoteConfig.getString(Constants.SEM1_START);
                    String sem1End = mFirebaseRemoteConfig.getString(Constants.SEM1_END);
                    String sem2Start = mFirebaseRemoteConfig.getString(Constants.SEM2_START);
                    String sem2End = mFirebaseRemoteConfig.getString(Constants.SEM2_END);
                    String currentYearStart = mFirebaseRemoteConfig.getString(Constants.CURRENT_YEAR_START);
                    PreferencesManager.saveStringToPrefs(Constants.SEM1_START, sem1Start);
                    PreferencesManager.saveStringToPrefs(Constants.SEM1_END, sem1End);
                    PreferencesManager.saveStringToPrefs(Constants.SEM2_START, sem2Start);
                    PreferencesManager.saveStringToPrefs(Constants.SEM2_END, sem2End);
                    PreferencesManager.saveStringToPrefs(Constants.CURRENT_YEAR_START, currentYearStart);
                } else {
                    Log.d("aa", "fetch firebase remote config failed. Reason = " + task.getException());
                }
                if (firebaseRMCallback != null) {
                    firebaseRMCallback.onRemoteConfigFetched();
                }
            }
        });
    }

    public static String getCurrentSemesterForced() {
        if (PreferencesManager.getStringFromPrefs(Constants.SEM_OVERRIDE) == null || PreferencesManager.getStringFromPrefs(Constants.SEM_OVERRIDE).isEmpty()) {
            String sem1Start = PreferencesManager.getStringFromPrefs(Constants.SEM1_START);
            String sem2Start = PreferencesManager.getStringFromPrefs(Constants.SEM2_START);
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date sem1StartDate = formatter1.parse(sem1Start);
                Date sem2StartDate = formatter1.parse(sem2Start);
                Date currentDate = new Date();
                if (currentDate.compareTo(sem1StartDate) <= 0 || (currentDate.compareTo(sem1StartDate) > 0 && currentDate.compareTo(sem2StartDate) < 0)) {
                    return Constants.SPINNER_SEM_1;
                } else if (currentDate.compareTo(sem2StartDate) >= 0) {
                    return Constants.SPINNER_SEM_2;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "";
        } else {
            return PreferencesManager.getStringFromPrefs(Constants.SEM_OVERRIDE);
        }
    }

    public static String getCurrentSemester() {
        String sem1Start = PreferencesManager.getStringFromPrefs(Constants.SEM1_START);
        String sem2Start = PreferencesManager.getStringFromPrefs(Constants.SEM2_START);
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date sem1StartDate = formatter1.parse(sem1Start);
            Date sem2StartDate = formatter1.parse(sem2Start);
            Date currentDate = new Date();
            if (currentDate.compareTo(sem1StartDate) <= 0 || (currentDate.compareTo(sem1StartDate) > 0 && currentDate.compareTo(sem2StartDate) < 0)) {
                return Constants.SPINNER_SEM_1;
            } else if (currentDate.compareTo(sem2StartDate) >= 0) {
                return Constants.SPINNER_SEM_2;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
