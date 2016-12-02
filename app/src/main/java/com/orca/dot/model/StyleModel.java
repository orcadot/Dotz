package com.orca.dot.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orca.dot.interfaces.OnQueryListener;
import com.orca.dot.services.styles.StylesDataPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.orca.dot.utils.Constants.FIREBASE_LOCATION_STYLE_DATA;
import static com.orca.dot.utils.Constants.FIREBASE_LOCATION_USER_LIKES_CART;
import static com.orca.dot.utils.Constants.FIREBASE_LOCATION_USER_LISTS;

/**
 * Defines the data structure for style object.
 *
 * @author Amit on 08-06-2016.
 */
public class StyleModel {

    private final StylesDataPresenter styleDataPresenter;
    private final String categoryId;

    private DatabaseReference styleDataReference;
    private DatabaseReference userReference;

    private ValueEventListener styleEventListener;

    private List<Style> stylesList;

    private static final String TAG = "StyleModel";

    public static class Builder{
        //Required parameters
        private final String userId;

        //Optional parameters
        private String categoryId = "";
        private StylesDataPresenter stylesDataPresenter;

        public Builder(String userId){
            this.userId = userId;
        }

        public Builder categoryId(String categoryId){
            this.categoryId = categoryId;
            return this;
        }

        public Builder dataPresenter(StylesDataPresenter stylesDataPresenter){
            this.stylesDataPresenter = stylesDataPresenter;
            return this;
        }

        public StyleModel build(){
            return new StyleModel(this);
        }
    }

    private StyleModel(Builder builder){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        this.userReference = databaseReference.child(FIREBASE_LOCATION_USER_LISTS).child(builder.userId);
        this.categoryId = builder.categoryId;
        this.styleDataReference = databaseReference.child(FIREBASE_LOCATION_STYLE_DATA).child(categoryId);
        this.stylesList = new ArrayList<>();
        styleDataPresenter = builder.stylesDataPresenter;
    }

    public void getData() {
        fetchUserLikesAndCart();
    }

    private void fetchUserLikesAndCart() {
        UserModel.getUserDetails(userReference, new OnQueryListener() {
            @Override
            public void onSuccess(Object o) {
                User user = (User) o;
                Map<String, Map<String, Style>> likedOrAddedStyles = (user.user_likes_cart != null) ? user.user_likes_cart : new HashMap<String, Map<String, Style>>();
                populateStyles(likedOrAddedStyles.get(categoryId));
            }
        });

    }

    private void populateStyles(final Map<String, Style> likedStyles) {
        stylesList.clear();
        styleEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Style style = snapshot.getValue(Style.class);
                    if (likedStyles != null && likedStyles.get(style.style_id) != null) {
                        style.isLiked = likedStyles.get(style.style_id).isLiked;
                        style.isAdded = likedStyles.get(style.style_id).isAdded;
                        stylesList.add(style);
                    } else
                        stylesList.add(style);

                }
                styleDataPresenter.updateData(stylesList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        styleDataReference.addListenerForSingleValueEvent(styleEventListener);
    }

    public void removeListener() {
        if (styleEventListener != null)
            styleDataReference.removeEventListener(styleEventListener);
    }

}
