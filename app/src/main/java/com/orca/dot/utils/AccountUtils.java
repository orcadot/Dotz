package com.orca.dot.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * Created by amit on 30/11/16.
 */

public class AccountUtils {

    // These names are the prefixes;
    private static final String PREFIX_PREF_DOT_NAME = "dot_name_";
    private static final String PREFIX_PREF_DOT_PHONE = "dot_phone_";
    private static final String PREFIX_PREF_DOT_IMAGE_URL = "dot_image_url_";
    private static final String PREF_ACTIVE_ACCOUNT = "chosen_account";
    private static final String PREFIX_PREF_PROFILE_FILLED = "profile_filled";

    private static SharedPreferences getSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Specify whether the app has an active account set.
     *
     * @param context Context used to lookup {@link SharedPreferences} the value is stored with.
     */
    public static boolean hasActiveAccount(final Context context) {
        return !TextUtils.isEmpty(getActiveAccountName(context));
    }

    /**
     * Return the accountName the app is using as the active Google Account.
     *
     * @param context Context used to lookup {@link SharedPreferences} the value is stored with.
     */
    public static String getActiveAccountName(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(PREF_ACTIVE_ACCOUNT, null);
    }

    public static void setActiveAccount(final Context context, final String accountName) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(PREF_ACTIVE_ACCOUNT, accountName).apply();
    }

    public static void setName(final Context context, final String name) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(PREFIX_PREF_DOT_NAME, name).apply();
    }

    public static String getName(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getString(PREFIX_PREF_DOT_NAME, null) : null;
    }

    public static void setPhone(final Context context, final String phoneNumber) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(PREFIX_PREF_DOT_PHONE, phoneNumber);
    }

    public static String getPhone(final Context context){
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getString(PREFIX_PREF_DOT_PHONE, null) : null;
    }

    public static void setImageUrl(final Context context, final String imageUrl) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(PREFIX_PREF_DOT_IMAGE_URL, imageUrl).apply();
    }

    public static String getImageUrl(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getString(PREFIX_PREF_DOT_IMAGE_URL, null) : null;
    }

    public static void setProfileFilled(final Context context, final boolean isProfileFilled) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putBoolean(PREFIX_PREF_PROFILE_FILLED, isProfileFilled).apply();
    }

    public static boolean getProfileFilled(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return hasActiveAccount(context) ? sp.getBoolean(PREFIX_PREF_PROFILE_FILLED, false) : false;
    }
}
