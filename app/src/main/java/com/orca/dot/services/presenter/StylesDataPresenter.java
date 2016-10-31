package com.orca.dot.services.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.orca.dot.model.HairStyle;
import com.orca.dot.services.StylesDataContracts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 26/10/16.
 */

public class StylesDataPresenter implements StylesDataContracts.Presenter {

    private final DatabaseReference mDataReference;
    private final StylesDataContracts.View mDataView;
    private List<HairStyle> mStylesData;
    private ValueEventListener valueListener;
    private static final String TAG = "StylesDataPresenter";

    public StylesDataPresenter(@NonNull DatabaseReference dataRef, @NonNull StylesDataContracts.View dataView) {
        Log.d(TAG, "StylesDataPresenter() called with: dataRef = [" + dataRef + "], dataView = [" + dataView + "]");
        mDataReference = dataRef;
        mDataView = dataView;
        mStylesData = new ArrayList<>();
        dataView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadData(boolean forceUpdate) {

    }

    @Override
    public void favClicked(String styleRef, final int adapterPosition) {
        mDataReference.child(styleRef).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                HairStyle hairStyle = mutableData.getValue(HairStyle.class);
                if (hairStyle == null) {
                    return Transaction.success(mutableData);
                }

                if (hairStyle.likes.containsKey(getUid())) {
                    hairStyle.likesCount = hairStyle.likesCount - 1;
                    hairStyle.likes.remove(getUid());
                } else {

                    hairStyle.likesCount = hairStyle.likesCount + 1;
                    hairStyle.likes.put(getUid(), true);
                }
                mutableData.child("likesCount").setValue(hairStyle.likesCount);
                mutableData.child("likes").setValue(hairStyle.likes);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (databaseError == null) {
                    HairStyle hairStyle = dataSnapshot.getValue(HairStyle.class);
                    mDataView.updateData(adapterPosition, hairStyle);
                }
            }
        });
    }

    @Override
    public void start() {
        valueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "]");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue() != null) {
                        HairStyle hairStyle = snapshot.getValue(HairStyle.class);
                        hairStyle.uniqueKey = snapshot.getKey();
                        mStylesData.add(hairStyle);
                    }
                }
                mDataView.showStylesData(mStylesData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDataReference.addListenerForSingleValueEvent(valueListener);
    }

    @Override
    public void stop() {
        if (valueListener != null) {
            mDataReference.removeEventListener(valueListener);
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
