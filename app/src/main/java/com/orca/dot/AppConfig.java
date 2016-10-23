package com.orca.dot;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by amit on 30/9/16.
 */

public class AppConfig extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
