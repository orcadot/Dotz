package com.orca.dot.services;

import com.orca.dot.BasePresenter;
import com.orca.dot.BaseView;
import com.orca.dot.model.HairStyle;

import java.util.List;

/**
 * Created by amit on 26/10/16.
 */

public interface StylesDataContracts {

    interface View extends BaseView<Presenter> {
        void showStylesData(List<HairStyle> dataList);

        void updateData(int adapterPosition, HairStyle hairStyle);
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadData(boolean forceUpdate);

        void favClicked(String styleRef, int adapterPosition);
    }
}
