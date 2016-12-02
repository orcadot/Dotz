package com.orca.dot.services.cart;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orca.dot.interfaces.OnQueryListener;
import com.orca.dot.model.BaseModel;
import com.orca.dot.model.FavCartModel;
import com.orca.dot.model.Footer;
import com.orca.dot.model.Style;
import com.orca.dot.model.StyleCategory;
import com.orca.dot.model.StyleCategoryModel;
import com.orca.dot.model.SubHeader;
import com.orca.dot.model.User;
import com.orca.dot.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.orca.dot.utils.Constants.FIREBASE_LOCATION_USER_LISTS;

class CartPresenter implements CartContract.Presenter {

    private static final String TAG = "CartPresenter";
    private final CartContract.View mCartView;
    private final DatabaseReference userReference;
    private final String userId;
    private List<BaseModel> items;
    private FavCartModel mFavCartModel;

    CartPresenter(@NonNull String userId, @NonNull CartContract.View favView) {
        this.userId = userId;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        this.userReference = databaseReference.child(FIREBASE_LOCATION_USER_LISTS).child(userId);
        this.mCartView = favView;
        items = new ArrayList<>();
        mCartView.setPresenter(this);
    }


    @Override
    public void start() {
        items.clear();

        mFavCartModel = new FavCartModel.Builder(userId).build();

        UserModel.getUserDetails(userReference, new OnQueryListener() {
            @Override
            public void onSuccess(Object o) {
                User user = (User) o;
                Map<String, Map<String, Style>> likedOrAddedStyles = (user.user_likes_cart != null) ?
                        user.user_likes_cart : new HashMap<String, Map<String, Style>>();
                if (likedOrAddedStyles.size() != 0)
                    filterAddedStyles(likedOrAddedStyles);
            }
        });

    }

    private void filterAddedStyles(final Map<String, Map<String, Style>> likedOrAddedStyles) {
        StyleCategoryModel.getStylesCategory(new OnQueryListener() {
            @Override
            public void onSuccess(Object o) {
                int totalPrice = 0;
                List<StyleCategory> styleCategories = (List<StyleCategory>) o;
                Log.i(TAG, "onSuccess: "+styleCategories.size());
                for (StyleCategory styleCategory : styleCategories) {
                    Map<String, Style> map = likedOrAddedStyles.get(styleCategory.category_id);
                    if (map != null && map.size() > 0) {
                        boolean firstTime = true;
                        for (Map.Entry<String, Style> stringStyleEntry : map.entrySet()) {
                            Style style = stringStyleEntry.getValue();
                            if (style.isAdded) {
                                if (firstTime) {
                                    firstTime = false;
                                    items.add(new SubHeader(styleCategory.category_name));
                                }

                                totalPrice = (int) (totalPrice + style.style_price);
                                items.add(style);
                            }
                        }
                    }
                }

                items.add(new Footer(totalPrice));
                mCartView.showCartItems(items);
            }
        });

    }

    @Override
    public void stop() {
    }

    @Override
    public void favOrAddClicked(Style style) {
       mFavCartModel.updateFavOrCart(style);
    }
}
