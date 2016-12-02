package com.orca.dot.services.favorites;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orca.dot.model.BaseModel;
import com.orca.dot.model.Style;
import com.orca.dot.model.SubHeader;
import com.orca.dot.utils.Providers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 31/10/16.
 */

public class FavoritesPresenter implements FavoritesContract.Presenter {

    private static final String TAG = "CartPresenter";
    private final DatabaseReference mFavReference;
    private final FavoritesContract.View mFavView;
    private ValueEventListener valueEventListener;
    private List<BaseModel> items;

    public FavoritesPresenter(@NonNull FavoritesContract.View favView) {
        this.mFavReference = FirebaseDatabase.getInstance().getReference().child("user-user_likes").child(Providers.getUid());
        this.mFavView = favView;
        items = new ArrayList<>();
        mFavView.setPresenter(this);
    }

    @Override
    public void loadFavorites() {

    }

    @Override
    public void start() {
        items.clear();
        Log.d(TAG, "start() called");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    items.add(new SubHeader(snapshot.getKey()));
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        Style style = itemSnapshot.getValue(Style.class);
                        items.add(style);
                    }Log.i(TAG, "onDataChange: "+ snapshot.getValue()+ "  "+snapshot.getKey());
                }

                mFavView.showFavorites(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mFavReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void stop() {
        if (valueEventListener != null)
            mFavReference.removeEventListener(valueEventListener);
    }

}
