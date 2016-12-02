package com.orca.dot.services.styles;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orca.dot.interfaces.OnQueryListener;
import com.orca.dot.model.KeyValue;
import com.orca.dot.model.StyleCategory;
import com.orca.dot.model.StyleCategoryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 27/10/16.
 */

public class TabDataPresenter implements TabDataContract.Presenter {

    private final TabDataContract.View mDataView;
    private static final String TAG = "TabDataPresenter";

    public TabDataPresenter(@NonNull TabDataContract.View dataView) {
        mDataView = dataView;
        dataView.setPresenter(this);
    }

    @Override
    public void start() {
        StyleCategoryModel.getStylesCategory(new OnQueryListener() {
            @Override
            public void onSuccess(Object o) {
                mDataView.populateTabData((List<StyleCategory>) o);
            }
        });
    }

    @Override
    public void stop() {
    }
}
