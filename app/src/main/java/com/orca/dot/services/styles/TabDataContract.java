package com.orca.dot.services.styles;

import android.support.v4.media.RatingCompat;

import com.orca.dot.BasePresenter;
import com.orca.dot.BaseView;
import com.orca.dot.model.KeyValue;
import com.orca.dot.model.StyleCategory;

import java.util.List;

/**
 * Created by amit on 26/10/16.
 */

public interface TabDataContract {

    interface View extends BaseView<TabDataContract.Presenter> {
        void populateTabData(List<StyleCategory> tabData);
    }

    interface Presenter extends BasePresenter {

    }

}
