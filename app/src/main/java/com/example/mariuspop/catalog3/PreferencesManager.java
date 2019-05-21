package com.example.mariuspop.catalog3;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    public static void saveStringToPrefs(String key, String value) {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringFromPrefs(String key) {
        SharedPreferences prefs = MyApplication.getAppContext().getSharedPreferences("MyPref", 0);
        return prefs.getString(key, null);
    }

    public static void removeStringToPrefs(String key) {
        SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.apply();
    }
}
