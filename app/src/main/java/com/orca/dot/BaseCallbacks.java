package com.orca.dot;

import com.google.firebase.database.DataSnapshot;
import com.orca.dot.Exceptions.DataSnapshotException;
import com.orca.dot.model.HairStyle;

import java.util.List;

/**
 * Created by amit on 2/11/16.
 */

public interface BaseCallbacks {

    void onSuccess(DataSnapshot dataSnapshot);

    void onError(String message) ;
}
