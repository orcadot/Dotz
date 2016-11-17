package com.orca.dot.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.orca.dot.BaseCallbacks;
import com.orca.dot.BaseQuery;
import com.orca.dot.services.styles.StylesDataPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the data structure for style object.
 *
 * @author Amit on 08-06-2016.
 */
public class HairStyleModel{

    private DatabaseReference databaseReference;
    private BaseQuery mBaseQuery;
    private List<HairStyle> hairStylesList;
    private StylesDataPresenter stylesDataPresenter;

    private static final String TAG = "HairStyleModel";
    /**
     * Required public constructor
     */
    public HairStyleModel(DatabaseReference databaseReference, StylesDataPresenter stylesDataPresenter) {
        this.databaseReference = databaseReference;
        this.hairStylesList = new ArrayList<>();
        this.stylesDataPresenter = stylesDataPresenter;
    }

    public void getHairStyles(){
        hairStylesList.clear();
        mBaseQuery = new BaseQuery(databaseReference, new BaseCallbacks() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue() != null) {
                        HairStyle hairStyle = snapshot.getValue(HairStyle.class);
                        hairStyle.uniqueKey = snapshot.getKey();
                        hairStylesList.add(hairStyle);
                    }
                }
                stylesDataPresenter.updateData(hairStylesList);
            }

            @Override
            public void onError(String message) {
                Log.d(TAG, "onError() called with: message = [" + message + "]");
            }
        });
        mBaseQuery.getDataSnapshot();
    }

    public void removeListener(){
        mBaseQuery.stopListening();
    }

}
