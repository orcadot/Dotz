package com.orca.dot.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orca.dot.services.styles.StylesDataPresenter;

import static com.orca.dot.utils.Constants.FIREBASE_LOCATION_USER_LIKES_CART;
import static com.orca.dot.utils.Constants.FIREBASE_LOCATION_USER_LISTS;

/**
 * Created by amit on 2/12/16.
 */

public class FavCartModel {

    private final String userId;
    private final DatabaseReference userReference;

    public static class Builder{
        //Required parameters
        private final String userId;

        public Builder(String userId){
            this.userId = userId;
        }


        public FavCartModel build(){
            return new FavCartModel(this);
        }
    }

    private FavCartModel(Builder builder){
        userId = builder.userId;
        userReference = FirebaseDatabase.getInstance().getReference().child(FIREBASE_LOCATION_USER_LISTS).child(userId);
    }
    public void updateFavOrCart(Style style) {
        if (style.isLiked || style.isAdded)
            userReference.child(FIREBASE_LOCATION_USER_LIKES_CART).child(style.category_id).child(style.style_id).setValue(style);
        else
            userReference.child(FIREBASE_LOCATION_USER_LIKES_CART).child(style.category_id).child(style.style_id).removeValue();
    }
}
