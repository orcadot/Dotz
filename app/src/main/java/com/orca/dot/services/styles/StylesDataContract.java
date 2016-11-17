package com.orca.dot.services.styles;

import com.orca.dot.BasePresenter;
import com.orca.dot.BaseView;
import com.orca.dot.model.HairStyle;

import java.util.List;

/**
 * Created by amit on 26/10/16.
 */

public interface StylesDataContract {

    interface View extends BaseView<Presenter> {
        void showStylesData(List<HairStyle> dataList);

        void showUpdatedData(int adapterPosition, HairStyle hairStyle);

        void showFavorites();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void favClicked(String styleRef, int adapterPosition);

        void loadFavorites(String favoritesRef);
    }
}
