package com.champs21.schoolapp.utils;

import android.content.SharedPreferences;

/**
 * Created by RR on 01-Jan-18.
 */

public class AppSharedPreference {
    public static final String keyModelTestPrefs = "modelTestPrefs";

    private static final String keyIsFirstTime = "isFirstTime";

    private static SharedPreferences getSharedPreferences() {
        return MyApplication.getContext().getSharedPreferences(keyModelTestPrefs, 0);
    }

    public static boolean getUsingFirstTime() {
        final SharedPreferences pref = getSharedPreferences();
        return pref.getBoolean(keyIsFirstTime, true);
    }

    public static void setUsingFirstTime(boolean isFirstTime) {
        final SharedPreferences pref = getSharedPreferences();
        final SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(keyIsFirstTime, isFirstTime);
        editor.apply();
    }

    public static void savePrefBoolean(String key, boolean value){
        final SharedPreferences pref = getSharedPreferences();
        final SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public static boolean getPrefBoolean(String key){
        final SharedPreferences pref = getSharedPreferences();
        return pref.getBoolean(key, false);
    }

}
