package com.orca.dot.auth.internal;

import android.content.Context;

import com.orca.dot.auth.callbacks.ServerCallback;

/**
 * Created by amit on 26/9/16.
 */

public class DotAuth {

    public static void authenticate(Context context, String phoneNum, ServerCallback serverCallback) {
        new AuthenticationAsyncTask().execute(context, phoneNum, serverCallback);
    }

}
