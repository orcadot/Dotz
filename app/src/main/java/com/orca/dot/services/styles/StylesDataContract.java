package com.orca.dot.services.styles;

import com.orca.dot.BasePresenter;
import com.orca.dot.BaseView;
import com.orca.dot.model.Style;

import java.util.List;

/**
 * Created by amit on 26/10/16.
 */

public interface StylesDataContract {

    interface View extends BaseView<StylesDataContract.Presenter> {
        void showStylesData(List<Style> dataList);
    }

    interface Presenter extends BasePresenter {

        void favOrAddClicked(final Style style);

    }
}
