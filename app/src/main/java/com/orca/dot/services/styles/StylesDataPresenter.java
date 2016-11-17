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
import com.orca.dot.model.HairStyle;
import com.orca.dot.model.HairStyleModel;
import com.orca.dot.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.orca.dot.utils.Providers.getUid;


public class StylesDataPresenter implements StylesDataContract.Presenter {

    private static final String TAG = "StylesDataPresenter";
    private final DatabaseReference mDataReference;
    private final DatabaseReference mUserLikesReference;
    private final String dataNode;
    private final StylesDataContract.View mDataView;
    private List<HairStyle> mStylesData;
    private ValueEventListener valueListener;
    private HairStyleModel mHairStyleModel;

    StylesDataPresenter(@NonNull String dataRef, @NonNull StylesDataContract.View dataView) {
        dataNode = dataRef;
        mDataReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_STYLE_DATA_NODE).child(dataNode);
        mUserLikesReference = FirebaseDatabase.getInstance().getReference().child("user-likes").child(getUid());
        mDataView = dataView;
        mStylesData = new ArrayList<>();
        dataView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void favClicked(final String styleRef, final int adapterPosition) {
        Log.d(TAG, "favClicked() called with: styleRef = [" +mDataReference.child(styleRef) + "], adapterPosition = [" + adapterPosition + "]");
        mDataReference.child(styleRef).runTransaction(new Transaction.Handler() {
            boolean mLiked;


            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                HairStyle hairStyle = mutableData.getValue(HairStyle.class);
                if (hairStyle == null) {
                    return Transaction.success(mutableData);
                }

                if (hairStyle.likes.containsKey(getUid())) {
                    hairStyle.likesCount = hairStyle.likesCount - 1;
                    hairStyle.likes.remove(getUid());
                    mLiked = false;
                } else {

                    hairStyle.likesCount = hairStyle.likesCount + 1;
                    hairStyle.likes.put(getUid(), true);
                    mLiked = true;
                }

                Log.d(TAG, "doTransaction() called with: mutableData = [" + mutableData + "]");
                mutableData.setValue(hairStyle);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (databaseError == null) {
                    Log.d(TAG, "onComplete() called with: databaseError = [" + databaseError + "], b = [" + b + "], dataSnapshot = [" + dataSnapshot + "]");
                    HairStyle hairStyle = dataSnapshot.getValue(HairStyle.class);
                    updateToUserRef(hairStyle, styleRef, mLiked);
                    mDataView.showUpdatedData(adapterPosition, hairStyle);
                }
                else
                    Log.i(TAG, "onComplete: "+databaseError);
            }
        });
    }

    @Override
    public void loadFavorites(String favoritesRef) {

    }

    private void updateToUserRef(HairStyle hairStyle, String styleRef, boolean mLiked) {
        if (!mLiked) {
            mUserLikesReference.child(dataNode).child(styleRef).removeValue();
        } else
            mUserLikesReference.child(dataNode).child(styleRef).setValue(hairStyle);
    }

    @Override
    public void start() {
        mHairStyleModel = new HairStyleModel(mDataReference, this);
        mHairStyleModel.getHairStyles();

    }

    @Override
    public void stop() {
       mHairStyleModel.removeListener();
    }


    public void updateData(List<HairStyle> hairStylesList) {
        if((hairStylesList != null) && (!hairStylesList.isEmpty())){
            mDataView.showStylesData(hairStylesList);
        }
        else
            Log.i(TAG, "start: hairstyles empty");
    }
}
