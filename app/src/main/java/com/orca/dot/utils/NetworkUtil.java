package com.orca.dot.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by dot on 25/9/16.
 */

public class NetworkUtil {

    private Context _context;
    private static final String TAG = "NetworkUtil";

    public NetworkUtil(Context context) {
        this._context = context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean hasActiveInternetConnection() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnValue = p1.waitFor();
            boolean reachable = (returnValue == 0);
            Log.i(TAG, "hasActiveInternetConnection " + reachable);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "hasActiveInternetConnection " + false);
        return false;
    }

}
