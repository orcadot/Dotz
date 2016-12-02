package com.orca.dot.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orca.dot.interfaces.OnQueryListener;

/**
 * Created by amit on 30/11/16.
 */

public class UserModel {

    private static final String TAG = "UserModel";

    public static void getUserDetails(final DatabaseReference userRef, final OnQueryListener onUserFetchListener) {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "]");
                onUserFetchListener.onSuccess(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
