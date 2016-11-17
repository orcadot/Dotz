package com.orca.dot.utils;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by amit on 31/10/16.
 */

public class Providers {

    @NonNull
    public static String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
