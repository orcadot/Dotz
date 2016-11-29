package com.orca.dot.services.styles;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orca.dot.model.KeyValue;
import com.orca.dot.model.StyleCategory;

import java.util.ArrayList;

/**
 * Created by amit on 27/10/16.
 */

public class TabDataPresenter implements TabDataContract.Presenter {

    private final DatabaseReference mDataReference;
    private final TabDataContract.View mDataView;
    private final ArrayList<StyleCategory> mTabData;
    private ValueEventListener valueListener;

    private static final String TAG = "TabDataPresenter";

    public TabDataPresenter(@NonNull DatabaseReference dataRef, @NonNull TabDataContract.View dataView) {
        mDataReference = dataRef;
        mDataView = dataView;
        mTabData = new ArrayList<>();
        dataView.setPresenter(this);
    }

    @Override
    public void start() {
        valueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTabData.clear();
                Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "]");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue() != null) {
                        StyleCategory styleCategory = snapshot.getValue(StyleCategory.class);
                        mTabData.add(styleCategory);
                    }
                }
                mDataView.populateTabData(mTabData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDataReference.addListenerForSingleValueEvent(valueListener);
    }

    @Override
    public void stop() {

    }
}
