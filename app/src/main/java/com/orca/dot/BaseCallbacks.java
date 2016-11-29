package com.orca.dot;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by amit on 2/11/16.
 */

public interface BaseCallbacks {

    void onSuccess(DataSnapshot dataSnapshot);

    void onError(String message) ;
}
