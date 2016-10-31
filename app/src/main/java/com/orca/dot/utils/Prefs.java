package com.orca.dot.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.orca.dot.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by amit on 23/10/16.
 */

public class Prefs {

    SharedPreferences preferences;
    private Context context;

    public Prefs(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(context.getString(R.string.profile_prefs_file_key), MODE_PRIVATE);
    }

    public String getStringFromPrefs(String lookupKey) {
        return preferences.getString(lookupKey, "");
    }

    public void saveStringToPrefs(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public boolean getBooleanFromPrefs(String lookupKey) {
        return preferences.getBoolean(lookupKey, false);
    }

    public void saveBoolToPrefs(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public int getIntFromPrefs(String lookupKey) {
        return preferences.getInt(lookupKey, 0);
    }

    public void saveIntToPrefs(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
