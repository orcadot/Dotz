package com.orca.dot.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orca.dot.BaseCallbacks;
import com.orca.dot.BaseQuery;
import com.orca.dot.services.styles.StylesDataPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Defines the data structure for style object.
 *
 * @author Amit on 08-06-2016.
 */
public class StyleModel {

    private final StylesDataPresenter styleDataPresenter;
    private DatabaseReference databaseReference;
    ;
    private List<Style> stylesList;
    private static final String TAG = "StyleModel";
    private ValueEventListener valueListener;

    /**
     * Required public constructor
     */
    public StyleModel(DatabaseReference databaseReference, StylesDataPresenter stylesDataPresenter) {
        this.databaseReference = databaseReference;
        this.stylesList = new ArrayList<>();
        this.styleDataPresenter = stylesDataPresenter;
    }

    public void getStyles() {
        stylesList.clear();
        Log.i(TAG, "getStyles: "+databaseReference.toString());
        valueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot.getValue() + "]");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue() != null) {
                        Style style = snapshot.getValue(Style.class);
                        stylesList.add(style);
                    }
                }
                styleDataPresenter.updateData(stylesList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        databaseReference.addListenerForSingleValueEvent(valueListener);


    }

    public void removeListener() {
        databaseReference.removeEventListener(valueListener);
    }

}
