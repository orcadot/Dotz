package com.orca.dot.services.favorites;

import com.orca.dot.BasePresenter;
import com.orca.dot.BaseView;
import com.orca.dot.model.BaseModel;
import com.orca.dot.utils.Prefs;

import java.util.List;

/**
 * Created by amit on 31/10/16.
 */

public interface FavoritesContract {

    interface View extends BaseView<Presenter>{
        void showFavorites(List<? extends BaseModel> data);
    }

    interface Presenter extends BasePresenter{

        void loadFavorites();

    }
}
