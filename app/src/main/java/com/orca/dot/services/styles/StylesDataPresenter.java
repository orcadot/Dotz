package com.orca.dot.services.styles;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.orca.dot.model.Style;
import com.orca.dot.model.StyleModel;
import com.orca.dot.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.orca.dot.utils.Providers.getUid;


public class StylesDataPresenter implements StylesDataContract.Presenter {

    private static final String TAG = "StylesDataPresenter";
    private final DatabaseReference mDataReference;
    private final DatabaseReference mUserLikesReference;

    private final StylesDataContract.View mDataView;
    private final DatabaseReference mUserAddReference;
    private List<Style> mStylesData;
    private ValueEventListener valueListener;
    private StyleModel mStylesModel;
    private String categoryId;

    StylesDataPresenter(@NonNull DatabaseReference dataRef, @NonNull StylesDataContract.View dataView){
        mDataReference = dataRef;
        mUserLikesReference = FirebaseDatabase.getInstance().getReference().child("user-likes").child(getUid());
        mUserAddReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USER_LISTS).child(getUid()).child("adds");
        mDataView = dataView;
        mStylesData = new ArrayList<>();
        dataView.setPresenter(this);

    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void favClicked(final String styleRef, final int adapterPosition) {
       /* Log.d(TAG, "favClicked() called with: styleRef = [" +mDataReference.child(styleRef) + "], adapterPosition = [" + adapterPosition + "]");
        mDataReference.child(styleRef).runTransaction(new Transaction.Handler() {
            boolean mLiked;
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Style style = mutableData.getValue(Style.class);
                if (style == null) {
                    return Transaction.success(mutableData);
                }

                if (style.likes.containsKey(getUid())) {
                    style.likesCount = style.likesCount - 1;
                    style.likes.remove(getUid());
                    mLiked = false;
                } else {

                    style.likesCount = style.likesCount + 1;
                    style.likes.put(getUid(), true);
                    mLiked = true;
                }

                Log.d(TAG, "doTransaction() called with: mutableData = [" + mutableData + "]");
                mutableData.setValue(style);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (databaseError == null) {
                    Log.d(TAG, "onComplete() called with: databaseError = [" + databaseError + "], b = [" + b + "], dataSnapshot = [" + dataSnapshot + "]");
                    Style style = dataSnapshot.getValue(Style.class);
                    updateToUserRef(style, styleRef, mLiked);
                    mDataView.showUpdatedData(adapterPosition, style);
                }
                else
                    Log.i(TAG, "onComplete: "+databaseError);
            }
        });*/
    }

    @Override
    public void loadFavorites(String favoritesRef) {
    }


    private void updateToUserRef(Style style, String styleRef, boolean mLiked) {
        if (!mLiked) {
            //mUserLikesReference.child(dataNode).child(styleRef).removeValue();
        }
            //mUserLikesReference.child(dataNode).child(styleRef).setValue(style);
    }

    @Override
    public void addClicked(String styleRef, int adapterPosition) {
        mUserAddReference.child(styleRef).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d(TAG, "onComplete() called with: databaseError = [" + databaseError + "], b = [" + b + "], dataSnapshot = [" + dataSnapshot + "]");
            }
        });
    }

    @Override
    public void start() {
        mStylesModel = new StyleModel(mDataReference, this);
        mStylesModel.getStyles();
    }

    @Override
    public void stop() {
       mStylesModel.removeListener();
    }


    public void updateData(List<Style> stylesList) {
        if((stylesList != null) && (!stylesList.isEmpty())){
            mDataView.showStylesData(stylesList);
        }
        else
            Log.i(TAG, "start: hairstyles empty");
    }


}
