package com.orca.dot.services.styles;

import android.support.annotation.NonNull;
import android.util.Log;

import com.orca.dot.model.FavCartModel;
import com.orca.dot.model.Style;
import com.orca.dot.model.StyleModel;

import java.util.List;


public class StylesDataPresenter implements StylesDataContract.Presenter {

    private static final String TAG = "StylesDataPresenter";
    private final StylesDataContract.View mDataView;
    private StyleModel mStylesModel;
    private FavCartModel mFavCartModel;
    private String userId;
    private String categoryId;

    StylesDataPresenter(@NonNull String userId, @NonNull String categoryId, @NonNull StylesDataContract.View dataView){
        this.userId = userId;
        this.categoryId = categoryId;
        mDataView = dataView;
        dataView.setPresenter(this);

    }

    @Override
    public void favOrAddClicked(final Style style) {
      mFavCartModel.updateFavOrCart(style);
    }

    @Override
    public void start() {
        mStylesModel = new StyleModel.Builder(userId).categoryId(categoryId).dataPresenter(this).build();
        mFavCartModel = new FavCartModel.Builder(userId).build();
        mStylesModel.getData();
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
