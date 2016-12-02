package com.orca.dot.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orca.dot.interfaces.OnQueryListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 1/12/16.
 */

public class StyleCategoryModel {

    private static final DatabaseReference categoryDatabaseRef = FirebaseDatabase.getInstance().getReference().child("categories");
    private static final List<StyleCategory> styleCategories = new ArrayList<>();

    public static void getStylesCategory(final OnQueryListener onQueryListener){
        styleCategories.clear();
        categoryDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                styleCategories.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue() != null) {
                        StyleCategory styleCategory = snapshot.getValue(StyleCategory.class);
                        styleCategories.add(styleCategory);
                    }
                }
                onQueryListener.onSuccess(styleCategories);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
