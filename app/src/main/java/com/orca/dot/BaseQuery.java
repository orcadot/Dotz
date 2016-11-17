package com.orca.dot;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orca.dot.Exceptions.DataSnapshotException;

/**
 * Created by amit on 2/11/16.
 */

public class BaseQuery {

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private BaseCallbacks mBaseCallbacks;


    private static final String TAG = "BaseQuery";

    public BaseQuery(DatabaseReference databaseReference, BaseCallbacks baseCallbacks) {
        this.databaseReference = databaseReference;
        this.mBaseCallbacks = baseCallbacks;
    }

    public void getDataSnapshot(){

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mBaseCallbacks.onSuccess(dataSnapshot);
                Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "]");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mBaseCallbacks.onError(databaseError.getMessage());
            }
        };
        databaseReference.addValueEventListener(valueEventListener);

    }

    public void stopListening(){
        if(valueEventListener != null){
            databaseReference.removeEventListener(valueEventListener);
        }
    }
}
